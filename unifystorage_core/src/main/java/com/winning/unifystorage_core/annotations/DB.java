package com.winning.unifystorage_core.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.realm.RealmObject;

import static java.lang.annotation.ElementType.METHOD;
/**
 * 用来标识操作本地数据库
 * */
@Documented
@Target(METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DB {
    Class<? extends RealmObject> table();//对应的表，这里是对象实体
}
