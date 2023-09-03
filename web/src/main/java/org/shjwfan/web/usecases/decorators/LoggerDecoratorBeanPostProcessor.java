package org.shjwfan.web.usecases.decorators;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Service;

@Service
@Conditional(LoggerDecoratorCondition.class)
public class LoggerDecoratorBeanPostProcessor implements BeanPostProcessor {

  private final Map<Class<?>, Class<?>> beanInterfaceToLoggingDecoratorClass = new HashMap<>();

  public LoggerDecoratorBeanPostProcessor() {
    Reflections reflections = new Reflections("org.shjwfan");
    Set<Class<? extends LoggerDecorator>> loggingDecorators = reflections.getSubTypesOf(LoggerDecorator.class);

    for (Class<?> loggingDecorator : loggingDecorators) {
      Class<?>[] loggingDecoratorInterfaces = loggingDecorator.getInterfaces();
      for (Class<?> loggingDecoratorInterface : loggingDecoratorInterfaces) {
        beanInterfaceToLoggingDecoratorClass.put(loggingDecoratorInterface, loggingDecorator);
      }
    }
  }

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    Class<?> beanClass = bean.getClass();
    Class<?>[] beanInterfaces = beanClass.getInterfaces();

    if (beanInterfaces.length == 0) {
      return bean;
    }

    int index = 0;
    Class<?> beanInterface;
    Class<?> loggingDecoratorClass;

    do {
      beanInterface = beanInterfaces[index];
      loggingDecoratorClass = beanInterfaceToLoggingDecoratorClass.get(beanInterface);
      index++;
    } while (index < beanInterfaces.length && loggingDecoratorClass == null);

    if (loggingDecoratorClass == null) {
      return bean;
    }

    try {
      return loggingDecoratorClass.getConstructor(beanInterface).newInstance(bean);
    } catch (Exception e) {
      return bean;
    }
  }
}
