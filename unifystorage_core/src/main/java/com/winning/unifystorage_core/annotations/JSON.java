package com.winning.unifystorage_core.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
/**
 * 用来标识要存储key-value数据
 * */
@Documented
@Target(METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JSON {
    String key() default "";
    Class<?> convert() default Object.class;
}
