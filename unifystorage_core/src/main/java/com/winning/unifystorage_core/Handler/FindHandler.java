package com.winning.unifystorage_core.Handler;

import com.winning.unifystorage_core.HandlerAdapter;
import com.winning.unifystorage_core.UStorage;
import com.winning.unifystorage_core.Utils.FindConditionUtil;
import com.winning.unifystorage_core.annotations.FIND;
import com.winning.unifystorage_core.model.DbResult;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * 用于封装查询操作的类
 * @author yuzhijun 2018/11/30
 * */
public class FindHandler extends HandlerAdapter {
    private Class<? extends RealmObject> table;
    private String orderBy;
    private String where;
    private String distinct;
    private int limit;
    private boolean eager;
    private DbResult dbResult;

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
                    this.distinct = find.distinct();
                    this.limit = find.limit();
                    this.eager = find.eager();
                }
            }
        }
    }

    public static HandlerAdapter parseAnnotations(Annotation[] annotations,final Class<? extends RealmObject> table){
        return new FindHandler(annotations, table);
    }

    @SuppressWarnings("unchecked")
    @Override
    public DbResult invoke(Object[] args, Type[] parameterTypes, Annotation[][] parameterAnnotationsArray) {
        dbResult = new DbResult();
        try{
            RealmQuery<? extends RealmObject> query = UStorage.realm.where(this.table);
            RealmQuery<? extends RealmObject> whereFilteredQuery = FindConditionUtil.whereFilter(where, query, args , parameterTypes);

            RealmQuery<? extends RealmObject> otherFilteredQuery = FindConditionUtil.otherFilter(whereFilteredQuery, orderBy, limit, distinct);

            RealmResults result = otherFilteredQuery.findAllAsync();//这个地方所有的查询操作都是用异步的方式
            result.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults>() {
                @Override
                public void onChange(RealmResults realmResults, OrderedCollectionChangeSet changeSet) {
                    dbResult.setDbFindCallBack(realmResults, changeSet);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

        return dbResult;
    }
}
