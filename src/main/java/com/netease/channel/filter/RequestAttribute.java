package com.netease.channel.filter;

import java.lang.annotation.*;

/**
 * get parameter from Request Attribute
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestAttribute {

    /**
     * The request parameter to bind to.
     */
    String value();

    boolean required() default true;//non-use

}
