package com.winning.unifystorage_core;

import com.winning.unifystorage_core.Handler.GetJsonHandler;
import com.winning.unifystorage_core.Handler.JsonHandler;
import com.winning.unifystorage_core.annotations.GETJSON;
import com.winning.unifystorage_core.annotations.JSON;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class JSONServiceMethod<ReturnT> extends ServiceMethod<ReturnT> {
    private final Annotation[] methodAnnotations;
    private final Annotation[][] parameterAnnotationsArray;
    private final Type[] parameterTypes;
    private HandlerAdapter storageHandler;
    private String key;
    private Class<?> convert;

    private JSONServiceMethod(Method method, String key, Class<?> convert){
        this.methodAnnotations = method.getAnnotations();
        this.parameterTypes = method.getGenericParameterTypes();
        this.parameterAnnotationsArray = method.getParameterAnnotations();
        this.key = key;
        this.convert = convert;

        if (null != method){
            for (Annotation annotation : method.getAnnotations()){
                parseHandler(annotation, method.getAnnotations());
            }
        }
    }

    static <ReturnT> JSONServiceMethod<ReturnT> parseAnnotations(
            UStorage storage, Method method, String key,Class<?> convert) {


        return new JSONServiceMethod<>(method, key, convert);
    }

    private void parseHandler(Annotation annotation, Annotation[] annotations) {
        if (annotation instanceof JSON){
            this.storageHandler = JsonHandler.parseAnnotations(annotations, this.key, this.convert);
        }else if(annotation instanceof GETJSON){
            this.storageHandler = GetJsonHandler.parseAnnotations(annotations, this.key, this.convert);
        }
    }


    @Override
    ReturnT invoke(Object[] args) {
        if (null ==  this.storageHandler){
            throw new IllegalArgumentException("annotation is not exits! please check your code");
        }
        return  this.storageHandler.invoke(args, parameterTypes, parameterAnnotationsArray);
    }
}
