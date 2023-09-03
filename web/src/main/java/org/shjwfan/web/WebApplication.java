package org.shjwfan.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@EnableAutoConfiguration
@ComponentScan("org.shjwfan")
@Configuration
public class WebApplication {

  public static void main(String[] args) {
    SpringApplication application = new SpringApplication(WebApplication.class);
    application.setBanner((environment, sourceClass, out) -> {
      try {
        out.write("""                                                                                  
              ####  #    #   ##   #####  #####  ####   ####  #####    ##   #####  #    # ###### #####
             #    # #    #  #  #  #    #   #   #    # #    # #    #  #  #  #    # #    # #      #    #
             #      ###### #    # #    #   #   #    # #      #    # #    # #    # ###### #####  #    #
             #      #    # ###### #####    #   #    # #  ### #####  ###### #####  #    # #      #####
             #    # #    # #    # #   #    #   #    # #    # #   #  #    # #      #    # #      #   #
              ####  #    # #    # #    #   #    ####   ####  #    # #    # #      #    # ###### #    #
            """.getBytes());
      } catch (Exception e) {
        throw new IllegalStateException(e);
      }
    });
    application.run(args);
  }
}
