package com.winning.unifystorage_core.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
/**
 * 用来标识要做删除操作
 * */
@Documented
@Target(METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DELETE {
    String orderBy() default "";
    String where() default "";
    String distinct() default "";
    int limit() default 0;
    boolean eager() default true;
}
