package com.winning.unifystorage_core.Handler;

import com.winning.unifystorage_core.HandlerAdapter;
import com.winning.unifystorage_core.UStorage;
import com.winning.unifystorage_core.Utils.CommonUtil;
import com.winning.unifystorage_core.annotations.FIND;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class FindHandler extends HandlerAdapter {
    private static final String EQUAL_TO = "=";
    private static final String GREATER_THAN = ">";
    private static final String LESS_THAN = "<";
    private static final String GREATER_THAN_OR_EQUAL_TO = ">=";
    private static final String LESS_THAN_OR_EQUAL_TO = "<=";

    private Class<? extends RealmObject> table;
    private String orderBy;
    private String where;
    private String groupby;
    private int limit;
    private int offset;
    private boolean eager;

    private FindHandler(Annotation[] annotations, Class<? extends RealmObject> table){
        this.table = table;
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

    public static HandlerAdapter parseAnnotations(Annotation[] annotations, Class<? extends RealmObject> table){
        return new FindHandler(annotations, table);
    }

    @Override
    public <ReturnT> ReturnT invoke(Object[] args, Type[] parameterTypes, Annotation[][] parameterAnnotationsArray) {
        RealmQuery<? extends RealmObject> query = UStorage.realm.where(this.table);



        return null;
    }

    private RealmResults<? extends RealmObject> whereFilter(RealmQuery<? extends RealmObject> query){
        if (!CommonUtil.isEmptyStr(where)){

        }

        return null;
    }
}
