package com.winning.unifystorage_core.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
/**
 * 用来标识要获取key对应的value值
 * */
@Documented
@Target(METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GETJSON {
    String key() default "";
    Class<?> convert() default String.class;
}
