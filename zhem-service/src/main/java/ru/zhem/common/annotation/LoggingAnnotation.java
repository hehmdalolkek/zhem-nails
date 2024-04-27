package ru.zhem.common.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoggingAnnotation {

    String module() default "";

    String operation() default "";

}
