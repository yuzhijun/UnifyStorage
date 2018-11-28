package com.winning.unifystorage_core.Handler;

import com.winning.unifystorage_core.HandlerAdapter;
import com.winning.unifystorage_core.annotations.FIND;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public class FindHandler extends HandlerAdapter {
    private String orderBy;
    private String where;
    private String groupby;
    private int limit;
    private int offset;
    private boolean eager;

    private FindHandler(Annotation[] annotations){
        buildField(annotations);
    }

    private void buildField(Annotation[] annotations) {
        if (null != annotations){
            for (Annotation annotation : annotations){
                if (annotation instanceof FIND){
                    FIND find = (FIND) annotation;
                    this.orderBy = find.orderBy();
                    this.where = find.where();
                    this.groupby = find.groupBy();
                    this.limit = find.limit();
                    this.offset = find.offset();
                    this.eager = find.eager();
                }
            }
        }
    }

    public static HandlerAdapter parseAnnotations(Annotation[] annotations){
        return new FindHandler(annotations);
    }

    @Override
    public <ReturnT> ReturnT invoke(Object[] args, Type[] parameterTypes, Annotation[][] parameterAnnotationsArray) {
        return null;
    }
}
