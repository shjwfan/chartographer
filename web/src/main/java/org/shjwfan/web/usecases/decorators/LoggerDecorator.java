package org.shjwfan.web.usecases.decorators;

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;

public abstract class LoggerDecorator {

  protected final Logger logger = getLogger(getClass());
}
