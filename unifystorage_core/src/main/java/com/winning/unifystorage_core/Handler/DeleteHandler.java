package com.winning.unifystorage_core.Handler;

import android.support.annotation.NonNull;

import com.winning.unifystorage_core.HandlerAdapter;
import com.winning.unifystorage_core.UStorage;
import com.winning.unifystorage_core.Utils.FindConditionUtil;
import com.winning.unifystorage_core.annotations.DELETE;
import com.winning.unifystorage_core.model.DbResult;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class DeleteHandler extends HandlerAdapter {



    private Class<? extends RealmObject> table;
    private String orderBy;
    private String where;
    private String distinct;
    private int limit;
    private boolean eager;
    private DbResult dbResult;
    private DeleteHandler(Annotation[] annotations, Class<? extends RealmObject> table){
        this.table = table;
        buildField(annotations);
    }

    private void buildField(Annotation[] annotations) {
        if (null != annotations){
            for (Annotation annotation : annotations){
                if (annotation instanceof DELETE){
                    DELETE find = (DELETE) annotation;

                    this.orderBy = find.orderBy();
                    this.where = find.where();
                    this.distinct = find.distinct();
                    this.limit = find.limit();
                    this.eager = find.eager();
                }
            }
        }
    }
    public static HandlerAdapter parseAnnotations(Annotation[] annotations, final Class<? extends RealmObject> table){
        return new DeleteHandler(annotations,table);
    }

    @Override
    public DbResult invoke(final Object[] args, final Type[] parameterTypes, Annotation[][] parameterAnnotationsArray) {
        dbResult = new DbResult();
        deleteByQuery(args,parameterTypes);
        return dbResult;
    }


    private void deleteByQuery(Object[] args,Type[] parameterTypes) {
        try {
            RealmQuery<? extends RealmObject> query = UStorage.realm.where(this.table);
            RealmQuery<? extends RealmObject> whereFilteredQuery = FindConditionUtil.whereFilter(where, query, args, parameterTypes);

            RealmQuery<? extends RealmObject> otherFilteredQuery = FindConditionUtil.otherFilter(whereFilteredQuery, orderBy, limit, distinct);
            final RealmResults result = otherFilteredQuery.findAllAsync();

            result.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults>() {
                @Override
                public void onChange(RealmResults realmResults, OrderedCollectionChangeSet changeSet) {
                    if (OrderedCollectionChangeSet.State.INITIAL == changeSet.getState()){
                        if (realmResults.size() > 0) {
                            deleteByQueryTransaction(realmResults);
                        }else {
                            dbResult.setCount(0);
                            dbResult.setResultCallback(false,new Throwable("No data was selected"));
                        }
                    }

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            dbResult.setResultCallback(false, e.getCause());
        }
    }

    private void deleteByQueryTransaction(final RealmResults realmResults){
        UStorage.realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {
                dbResult.setCount(realmResults.size());
                boolean success = realmResults.deleteAllFromRealm();
                dbResult.setResultCallback(success,null);

            }
        });

    }
}
