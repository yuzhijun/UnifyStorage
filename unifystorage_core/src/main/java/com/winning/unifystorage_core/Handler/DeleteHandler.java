package com.winning.unifystorage_core.Handler;

import android.support.annotation.NonNull;

import com.winning.unifystorage_core.HandlerAdapter;
import com.winning.unifystorage_core.UStorage;
import com.winning.unifystorage_core.Utils.CommonUtil;
import com.winning.unifystorage_core.Utils.FindConditionUtil;
import com.winning.unifystorage_core.annotations.FIND;
import com.winning.unifystorage_core.annotations.Model;
import com.winning.unifystorage_core.annotations.Param;
import com.winning.unifystorage_core.exception.ErrorParamsException;
import com.winning.unifystorage_core.model.DbResult;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class DeleteHandler extends HandlerAdapter {

    private static final String EQUAL_TO = "\\w+(\\s)?=(\\s)?[?](\\s)?";
    private static final String GREATER_THAN = "\\w+(\\s)?>(\\s)?[?](\\s)?";
    private static final String LESS_THAN = "\\w+(\\s)?<(\\s)?[?](\\s)?";
    private static final String GREATER_THAN_OR_EQUAL_TO = "\\w+(\\s)?>=(\\s)?[?](\\s)?";
    private static final String LESS_THAN_OR_EQUAL_TO = "\\w+(\\s)?<=(\\s)?[?](\\s)?";
    private static final String CONTAINS = "\\w+(\\s)?contains(\\s)?[?](\\s)?";
    private static final String LIKE = "\\w+(\\s)?like(\\s)?[?](\\s)?";
    private static final String ISNOTNULL = "\\w+(\\s)?notnull(\\s)?";
    private static final String NULL = "\\w+(\\s)?null(\\s)?";
    private static final String IN = "\\w+(\\s)?in(\\s)?[?](\\s)?";
    private static final String AND_OR = "and|or";

    private List<String> linkCondition = new ArrayList<>();
    private String[][] patternArray = {{EQUAL_TO, "="}, {GREATER_THAN, ">"}, {LESS_THAN, "<"}, {GREATER_THAN_OR_EQUAL_TO, ">="},
            {LESS_THAN_OR_EQUAL_TO, "<="}, {CONTAINS, "contains"}, {LIKE, "like"}, {ISNOTNULL, "notnull"}, {NULL, "null"}, {IN, "in"}};

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
    public static HandlerAdapter parseAnnotations(Annotation[] annotations, final Class<? extends RealmObject> table){
        return new DeleteHandler(annotations,table);
    }

    @Override
    public DbResult invoke(final Object[] args, final Type[] parameterTypes, Annotation[][] parameterAnnotationsArray) {
        dbResult = new DbResult();

       if (checkIfValidModel(args,parameterAnnotationsArray)){
            deleteByModel(dbResult,args,parameterTypes);
        }else if (checkIfValidParam(parameterAnnotationsArray)){
            deleteByQuery(args,parameterTypes);
        }

        return dbResult;
    }

    private DbResult deleteByModel(final DbResult dbResult, final Object[] args, final Type[] parameterTypes){
        UStorage.realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {

                Class<?> rawType = CommonUtil.getRawType(parameterTypes[0]);
                 if (RealmResults.class.isAssignableFrom(rawType)){
                    RealmResults realmResults = (RealmResults) args[0];
                     dbResult.setCount(realmResults.size());
                     boolean success = realmResults.deleteAllFromRealm();
                    dbResult.setResultCallback(success,new Throwable("fail to delete data"));
                }else {
                    dbResult.setCount(0);
                    dbResult.setResultCallback(false,new Throwable("save method parameter is invalid,please check your code"));
                }
            }
        });
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
                    if (realmResults != null) {
                        deleteByQueryTransaction(realmResults);
                    }
                    result.removeAllChangeListeners();
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
                if (realmResults.size() > 0){
                    dbResult.setCount(realmResults.size());
                    boolean success = realmResults.deleteAllFromRealm();
                    dbResult.setResultCallback(success,null);
                }else {
                    dbResult.setCount(0);
                    dbResult.setResultCallback(false,new Throwable("No data was selected"));
                }

            }
        });

    }
    /**
     * 验证通过实体来删除表数据 参数是否合法
     * @param args
     * @param parameterAnnotationsArray
     * @return
     */
    private boolean checkIfValidModel(Object[] args , Annotation[][] parameterAnnotationsArray){
        if (args.length == 1
                &&parameterAnnotationsArray.length == 1
                && parameterAnnotationsArray[0].length == 1
                && parameterAnnotationsArray[0][0].annotationType() == Model.class){
            return true;
        }
       return false;
    }
    /**
     * 验证通过条件来删除表数据  参数是否合法
     * @param parameterAnnotationsArray
     * @return
     */
    private boolean checkIfValidParam(Annotation[][] parameterAnnotationsArray){
        for (Annotation[] temp : parameterAnnotationsArray){
            for (Annotation annotation : temp){
                if (annotation.annotationType() != Param.class){
                    throw new ErrorParamsException("delete method parameter is invalid,please check your code");
                }
            }
        }
        return true;
    }



}
