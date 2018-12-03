package com.winning.unifystorage_core.Handler;

import android.support.annotation.NonNull;

import com.winning.unifystorage_core.HandlerAdapter;
import com.winning.unifystorage_core.UStorage;
import com.winning.unifystorage_core.Utils.CommonUtil;
import com.winning.unifystorage_core.annotations.FIND;
import com.winning.unifystorage_core.annotations.Model;
import com.winning.unifystorage_core.annotations.Param;
import com.winning.unifystorage_core.exception.ErrorParamsException;
import com.winning.unifystorage_core.model.DbResult;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

import io.realm.Realm;
import io.realm.RealmList;
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
    public static HandlerAdapter parseAnnotations(Annotation[] annotations, Class<? extends RealmObject> table){
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
        UStorage.realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm realm) {

                Class<?> rawType = CommonUtil.getRawType(parameterTypes[0]);
                if (RealmObject[].class.isAssignableFrom(rawType) && rawType.isArray()){

                    RealmList realmList = new RealmList( Arrays.asList((RealmObject[]) args[0]));
                    realmList.deleteAllFromRealm();
                    dbResult.setCount(realmList.size());
                } else if (RealmObject.class.isAssignableFrom(rawType)){
                    RealmObject realmObject = (RealmObject) args[0];
                    realmObject.deleteFromRealm();
                    dbResult.setCount(1);
                } else if (List.class.isAssignableFrom(rawType)){
                    RealmList realmList = new RealmList(args[0]);
                    realmList.deleteAllFromRealm();
                    dbResult.setCount(realmList.size());
                }else {
                    throw new ErrorParamsException("save method parameter is invalid,please check your code");
                }
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {

                dbResult.setResultCallback(true,null);
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                dbResult.setResultCallback(false,error);
            }
        });
        return dbResult;
    }

    private void deleteByQuery(Object[] args,Type[] parameterTypes){

        try{
            RealmQuery<? extends RealmObject> query = UStorage.realm.where(this.table);
            RealmQuery<? extends RealmObject> whereFilteredQuery = whereFilter(query, args , parameterTypes);

            if (!CommonUtil.isEmptyStr(orderBy)){
                whereFilteredQuery.sort(orderBy);
            }

            if (0 != limit){
                whereFilteredQuery.limit(limit);
            }

            if (!CommonUtil.isEmptyStr(distinct)){
                whereFilteredQuery.distinct(distinct);
            }

            RealmResults result = whereFilteredQuery.findAllAsync();
            boolean success = result.deleteAllFromRealm();

            dbResult.setCount(result.size());
            dbResult.setResultCallback(success,null);
        }catch (Exception e){
            e.printStackTrace();
            dbResult.setResultCallback(false,e.getCause());
        }
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
        throw new ErrorParamsException("save method parameter is invalid,please check your code");
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

    private RealmQuery<? extends RealmObject> whereFilter(RealmQuery<? extends RealmObject> query,Object[] args, Type[] parameterTypes){
        linkCondition.clear();
        if (!CommonUtil.isEmptyStr(where)){

            Pattern linkPattern = Pattern.compile(AND_OR);
            Matcher linkMatcher = linkPattern.matcher(where);

            while (linkMatcher.find()){
                linkCondition.add(linkMatcher.group());
            }

            //说明有复合条件
            if (linkCondition.size() > 0){
                String[] whereArray = where.split(AND_OR);
                if (args.length != whereArray.length || parameterTypes.length != whereArray.length){
                    throw new IllegalArgumentException("parameter size is not equal to ?");
                }

                for (int i = 0;i < whereArray.length;i ++){
                    String whereCondition = whereArray[i];
                    Object parameter = args[i];
                    Type parameterType = parameterTypes[i];
                    //构造查询条件
                    buildWhereCondition(query, whereCondition, parameter, parameterType);

                    if (linkCondition.size() - 1 >= i){
                        String condition = linkCondition.get(i);
                        if ("and".equalsIgnoreCase(condition)){
                            query.and();
                        }else {
                            query.or();
                        }
                    }
                }
            }else {//说明是单一条件
                buildWhereCondition(query, where, args[0], parameterTypes[0]);
            }
        }

        return query;
    }

    private void buildWhereCondition(@Nonnull RealmQuery<? extends RealmObject> query, @Nonnull String whereCondition,
                                     @Nonnull Object parameter, @Nonnull Type parameterType) {
        Class<?> rawType = CommonUtil.getRawType(parameterType);
        for (int j = 0; j < patternArray.length; j ++){
            Pattern pattern = Pattern.compile(patternArray[j][0]);
            Matcher matcher = pattern.matcher(whereCondition);
            if (matcher.matches()){
                String[] array = whereCondition.split(patternArray[j][1]);
                if (parameterType == String.class){
                    if ("=".equalsIgnoreCase(patternArray[j][1])){
                        query.equalTo(array[0].trim(),(String) parameter);
                    }else if ("contains".equalsIgnoreCase(patternArray[j][1])){
                        query.contains(array[0].trim(),(String) parameter);
                    }else if("like".equalsIgnoreCase(patternArray[j][1])){
                        query.like(array[0].trim(),(String) parameter);
                    }else if ("notnull".equalsIgnoreCase(patternArray[j][1])){
                        query.isNotNull(array[0].trim());
                    }else if ("null".equalsIgnoreCase(patternArray[j][1])){
                        query.isNull(array[0].trim());
                    }
                }else if(parameterType == Integer.class){
                    if ("=".equalsIgnoreCase(patternArray[j][1])){
                        query.equalTo(array[0].trim(),(int)parameter);
                    }else if (">".equalsIgnoreCase(patternArray[j][1])){
                        query.greaterThan(array[0].trim(),(int) parameter);
                    }else if ("<".equalsIgnoreCase(patternArray[j][1])){
                        query.lessThan(array[0].trim(),(int) parameter);
                    }else if (">=".equalsIgnoreCase(patternArray[j][1])){
                        query.greaterThanOrEqualTo(array[0].trim(),(int) parameter);
                    }else if ("<=".equalsIgnoreCase(patternArray[j][1])){
                        query.lessThanOrEqualTo(array[0].trim(),(int) parameter);
                    }
                }else if (parameterType == Date.class){
                    if ("=".equalsIgnoreCase(patternArray[j][1])){
                        query.equalTo(array[0].trim(),(Date) parameter);
                    }else if (">".equalsIgnoreCase(patternArray[j][1])){
                        query.greaterThan(array[0].trim(),(Date) parameter);
                    }else if ("<".equalsIgnoreCase(patternArray[j][1])){
                        query.lessThan(array[0].trim(),(Date) parameter);
                    }else if (">=".equalsIgnoreCase(patternArray[j][1])){
                        query.greaterThanOrEqualTo(array[0].trim(),(Date) parameter);
                    }else if ("<=".equalsIgnoreCase(patternArray[j][1])){
                        query.lessThanOrEqualTo(array[0].trim(),(Date) parameter);
                    }
                }else if (List.class.isAssignableFrom(rawType)){
                    if ("in".equalsIgnoreCase(patternArray[j][1])){
                        query.in(array[0].trim(), (String[]) ((List)parameter).toArray(new String[0]));
                    }
                }
            }
        }
    }

}
