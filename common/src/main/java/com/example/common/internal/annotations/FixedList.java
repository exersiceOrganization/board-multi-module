package com.example.common.internal.annotations;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FixedList {
    /** List Size */
    int size() default -1;

    /** List Size Reference Field Name */
    String sizeRef() default "";
}
