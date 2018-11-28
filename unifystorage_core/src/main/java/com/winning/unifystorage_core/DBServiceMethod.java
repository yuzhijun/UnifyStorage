package com.winning.unifystorage_core;

import com.winning.unifystorage_core.Handler.FindHandler;
import com.winning.unifystorage_core.Handler.SaveHandler;
import com.winning.unifystorage_core.annotations.FIND;
import com.winning.unifystorage_core.annotations.INSERT;
import com.winning.unifystorage_core.annotations.SAVE;
import com.winning.unifystorage_core.annotations.SAVEORUPDATE;
import com.winning.unifystorage_core.annotations.UPDATE;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class DBServiceMethod<ReturnT> extends ServiceMethod<ReturnT> {
    private final Annotation[][] parameterAnnotationsArray;
    private final Type[] parameterTypes;

    private static HandlerAdapter storageHandler;

    private DBServiceMethod(Method method){
        this.parameterTypes = method.getGenericParameterTypes();
        this.parameterAnnotationsArray = method.getParameterAnnotations();
    }

    static <ReturnT> DBServiceMethod<ReturnT> parseAnnotations(
            UStorage storage, Method method) {

        if (null != method){
            for (Annotation annotation : method.getAnnotations()){
                parseHandler(annotation, method.getAnnotations());
            }
        }

        return new DBServiceMethod<>(method);
    }

    private static void parseHandler(Annotation annotation, Annotation[] annotations) {
        if (annotation instanceof FIND){
            storageHandler = FindHandler.parseAnnotations(annotations);
        }else if(annotation instanceof SAVE){
            storageHandler = SaveHandler.parseAnnotations(annotations);
        }else if(annotation instanceof SAVEORUPDATE){

        }else if(annotation instanceof UPDATE){

        }else if(annotation instanceof INSERT){

        }
    }

    @Override
    ReturnT invoke(Object[] args) {
        if (null == storageHandler){
            throw new IllegalArgumentException("annotation is not exits! please check your code");
        }
        return storageHandler.invoke(args, parameterTypes, parameterAnnotationsArray);
    }
}
