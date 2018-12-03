package com.winning.unifystorage_core;

import com.winning.unifystorage_core.Handler.DeleteHandler;
import com.winning.unifystorage_core.Handler.FindHandler;
import com.winning.unifystorage_core.Handler.SaveHandler;
import com.winning.unifystorage_core.Handler.SaveOrUpdateHandler;
import com.winning.unifystorage_core.annotations.FIND;
import com.winning.unifystorage_core.annotations.DELETE;
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

    private HandlerAdapter storageHandler;
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
            this.storageHandler = FindHandler.parseAnnotations(annotations, this.table);
        }else if(annotation instanceof SAVE){
            this.storageHandler = SaveHandler.parseAnnotations(annotations);
        }else if(annotation instanceof SAVEORUPDATE){
            this.storageHandler = SaveOrUpdateHandler.parseAnnotations(annotations);
        }else if(annotation instanceof UPDATE){
            //TODO
        }else if(annotation instanceof DELETE){
            this.storageHandler = DeleteHandler.parseAnnotations(annotations, this.table);
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
