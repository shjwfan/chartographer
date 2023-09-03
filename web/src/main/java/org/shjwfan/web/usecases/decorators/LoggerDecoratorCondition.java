package org.shjwfan.web.usecases.decorators;

import java.util.Objects;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class LoggerDecoratorCondition implements Condition {

  private static final String PROPERTY = "chartographer.enable-logger-decorators";

  @Override
  public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    Environment environment = context.getEnvironment();
    return Objects.equals(environment.getProperty(PROPERTY), "enable");
  }
}
