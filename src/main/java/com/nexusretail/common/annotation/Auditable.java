package com.nexusretail.common.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Auditable {
    String actionMethod();
    String action();
    String entity();
    String entityIdSpEL() default "";
}