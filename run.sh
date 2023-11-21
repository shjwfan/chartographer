#!/bin/bash

IMAGE_NAME="chartographer"
IMAGE_TAG="latest"
DOCKERFILE="./Dockerfile"
CONTAINER_NAME="chartographer"

./mvnw clean package

if [ "$(docker ps -a -q -f name=$CONTAINER_NAME)" ]; then
  echo "Stop and remove existing Chartographer Docker container..."
  docker stop $CONTAINER_NAME
  docker rm $CONTAINER_NAME
fi

if [ "$(docker images -q $IMAGE_NAME:$IMAGE_TAG)" ]; then
  echo "Remove existing Chartographer Docker image..."
  docker rmi $IMAGE_NAME:$IMAGE_TAG
fi

docker build -t "$IMAGE_NAME:$IMAGE_TAG" -f "$DOCKERFILE" .

if [ $? -eq 0 ]; then
  echo "Chartographer Docker image has been built: $IMAGE_NAME:$IMAGE_TAG."
  docker run -d --name $CONTAINER_NAME -p 8080:8080 -p 5005:5005 "$IMAGE_NAME:$IMAGE_TAG"

  if [ "$(docker ps -q -f name=$CONTAINER_NAME)" ]; then
    echo "Chartographer Docker container has been started."
  else
    echo "Failed to start Chartographer Docker container."
  fi

else
  echo "Failed to build Chartographer Docker image."
fi
