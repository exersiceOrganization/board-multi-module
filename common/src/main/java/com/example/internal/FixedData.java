package com.example.internal;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FixedData  {
    public enum  PAD_TYPE {
        LEFT
        , RIGHT
    }

    int[] value() default  0;

    int[] length() default 0;

    char padChar() default Character.UNASSIGNED;

    /**
     * BigDecimal Type
     *
     * @retrun
     */
    boolean signed() default false;

    PAD_TYPE padType() default PAD_TYPE.LEFT;
}
