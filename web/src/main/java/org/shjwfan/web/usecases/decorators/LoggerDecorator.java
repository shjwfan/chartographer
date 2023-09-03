package org.shjwfan.web.usecases.decorators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class LoggerDecorator {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
}
