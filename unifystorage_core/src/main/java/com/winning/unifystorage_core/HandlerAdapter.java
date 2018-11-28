package com.winning.unifystorage_core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public abstract class HandlerAdapter {
    public abstract <ReturnT> ReturnT invoke(Object[] args, Type[] parameterTypes, Annotation[][] parameterAnnotationsArray);
}
