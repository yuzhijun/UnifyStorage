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

import io.realm.RealmObject;

public class DBServiceMethod<ReturnT> extends ServiceMethod<ReturnT> {
    private final Annotation[][] parameterAnnotationsArray;
    private final Type[] parameterTypes;

    private static HandlerAdapter storageHandler;
    private Class<? extends RealmObject> table;

    private DBServiceMethod(Method method, Class<? extends RealmObject> table){
        this.parameterTypes = method.getGenericParameterTypes();
        this.parameterAnnotationsArray = method.getParameterAnnotations();
        this.table = table;

        if (null != method){
            for (Annotation annotation : method.getAnnotations()){
                parseHandler(annotation, method.getAnnotations());
            }
        }
    }

    static <ReturnT> DBServiceMethod<ReturnT> parseAnnotations(
            UStorage storage, Method method, Class<? extends RealmObject> table) {

        return new DBServiceMethod<>(method, table);
    }

    private void parseHandler(Annotation annotation, Annotation[] annotations) {
        if (annotation instanceof FIND){
            storageHandler = FindHandler.parseAnnotations(annotations, this.table);
        }else if(annotation instanceof SAVE){
            storageHandler = SaveHandler.parseAnnotations(annotations,this.table);

        }else if(annotation instanceof SAVEORUPDATE){
             //TODO
        }else if(annotation instanceof UPDATE){
            //TODO
        }else if(annotation instanceof INSERT){
            //TODO
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
