# Chartographer

Once upon a time, I was trying to get a job at a company, and they gave me this test task. 📜

## How to run?
1. Firstly, install [Docker](https://www.docker.com/).
2. Then run `sh run.sh` and enjoy.

From **charta** (*Latin*) or **харта** - one of the names for a [papyrus](https://en.wikipedia.org/wiki/Papyrus).

The task is to create the **Chartographer** service - a service for restoring images of ancient scrolls and papyri.
The images are BMP's and are created step by step (as fragments).

## HTTP API

```
POST /api/v1/chartas/?width={width}&height={height}
```

Create a new image of a papyrus with the given dimensions in pixels, where `{width}` and `{height}` are positive integers not exceeding `50,000` and `50,000`, respectively.
The request body is empty.
The response body returns `{id}` - an identifier of the image in string representation.
Response code: `201 Created`.

```
POST /api/v1/chartas/{id}/?x={x}&y={y}&width={width}&height={height}
```

Save the image fragment with dimensions `{width} x {height}` and coordinates `({x};{y})`.
Coordinates refer to the position of the top-left corner of the fragment relative to the top-left corner of the entire image.
Request body: the image in BMP format (RGB color, 24 bits per pixel).
The response body is empty.
Response code: `200 OK`.

```
GET /api/v1/chartas/{id}/?x={x}&y={y}&width={width}&height={height}
```

Retrieve a restored portion of the image with dimensions `{width} x {height}` and coordinates `({x};{y})`,
where `{width}` and `{height}` are positive integers not exceeding 5,000.
Coordinates refer to the position of the top-left corner of the fragment relative to the top-left corner of the entire image.
The response body: the BMP (RGB color, 24 bits per pixel).
Response code: `200 OK`.

```
DELETE /api/v1/chartas/{id}/
```

Delete the image with the identifier `{id}`.
The request body and the response body are empty.
Response code: `200 OK`.

### Error Handling

1. Requests for an image with an `{id}` that doesn't exist should result in a `404 Not Found` response.
2. Requests with incorrect parameters `{width}` or `{height}` should result in a `400 Bad Request` response.
3. Requests with fragments that do not intersect with the image's coordinates should result in a `400 Bad Request` response.
   However, fragments can *partially* extend beyond the image's boundaries (see notes) - such requests are considered valid.

### Notes

1. Image dimensions do not exceed `50,000 x 50,000`.
2. Some images cannot fit entirely in memory.
   Storage on disk should be considered.
3. Image format is [BMP](https://en.wikipedia.org/wiki/BMP). RGB color (without alpha channel), 24 bits per pixel.
4. If a loaded reconstructed fragment overlaps with a previously reconstructed part, the new fragment is applied regardless.
5. If a fragment is requested where parts have not been reconstructed yet, the unreconstructed areas are filled with white.
   Similarly, the part of a fragment that falls outside the image's boundaries is filled with white (see example below).
6. If a reconstructing fragment overlaps image boundaries, the part of the fragment outside the image is ignored.
   Example: image size - `50 x 100`, fragment size - `50 x 50`, top-left corner coordinates `(25;25)`.
   The right half of the fragment is ignored. Schematically depicted below.

```
╔═════════╗
║         ║
║    ┌────╫────┐
║    │    ║    │
║    │    ║    │
║    │    ║    │
║    └────╫────┘
║         ║
╚═════════╝
```

## Requirements

- Code compilation and execution with Java 17.
- Build the service using Apache Maven with the command `mvn clean package`.
- The service should be assembled into a fat JAR, containing all dependencies.
- Launch the service with the command `java -jar chartographer-1.0.0.jar /base/path` in the `target` directory of the project,
  where `/base/path` is the path to the directory where the service can store images.
- The service should listen for HTTP requests on the standard port (`8080`).
- The source code should adhere to the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html).
