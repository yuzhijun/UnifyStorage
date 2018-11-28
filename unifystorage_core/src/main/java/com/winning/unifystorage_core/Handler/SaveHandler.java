package com.winning.unifystorage_core.Handler;

import com.winning.unifystorage_core.HandlerAdapter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public class SaveHandler extends HandlerAdapter {

    private SaveHandler(){

    }

    public static HandlerAdapter parseAnnotations(Annotation[] annotations){

        return new SaveHandler();
    }

    @Override
    public <ReturnT> ReturnT invoke(Object[] args, Type[] parameterTypes, Annotation[][] parameterAnnotationsArray) {
        return null;
    }
}
