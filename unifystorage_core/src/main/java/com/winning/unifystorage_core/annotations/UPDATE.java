package com.winning.unifystorage_core.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
/**
 * 用来标识要做本地数据库更新操作
 * */
@Documented
@Target(METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UPDATE {
    String orderBy() default "";
    String where() default "";
    String set() default "";
    String distinct() default "";
    int limit() default 0;
    boolean eager() default true;
}
