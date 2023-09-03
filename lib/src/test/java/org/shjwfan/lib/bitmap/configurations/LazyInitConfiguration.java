package org.shjwfan.lib.bitmap.configurations;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan(value = "org.shjwfan", lazyInit = true)
@Configuration
public class LazyInitConfiguration {
}
