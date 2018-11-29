package com.winning.unifystorage_core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class JSONServiceMethod<ReturnT> extends ServiceMethod<ReturnT> {
    private final Annotation[] methodAnnotations;
    private final Annotation[][] parameterAnnotationsArray;
    private final Type[] parameterTypes;
    private String key;

    private JSONServiceMethod(Method method, String key){
        this.methodAnnotations = method.getAnnotations();
        this.parameterTypes = method.getGenericParameterTypes();
        this.parameterAnnotationsArray = method.getParameterAnnotations();
        this.key = key;
    }

    static <ReturnT> JSONServiceMethod<ReturnT> parseAnnotations(
            UStorage storage, Method method, String key) {


        return new JSONServiceMethod<>(method, key);
    }


    @Override
    ReturnT invoke(Object[] args) {


        return null;
    }
}
