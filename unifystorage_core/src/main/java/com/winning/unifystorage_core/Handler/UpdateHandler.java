package com.winning.unifystorage_core.Handler;

import com.winning.unifystorage_core.HandlerAdapter;
import com.winning.unifystorage_core.UStorage;
import com.winning.unifystorage_core.Utils.CommonUtil;
import com.winning.unifystorage_core.Utils.FindConditionUtil;
import com.winning.unifystorage_core.annotations.UPDATE;
import com.winning.unifystorage_core.exception.ErrorParamsException;
import com.winning.unifystorage_core.model.DbResult;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import static com.winning.unifystorage_core.Utils.Constants.EQUAL_TO;

/**
 * 用于封装查询操作的类
 * @author yuzhijun 2018/11/30
 * */
public class UpdateHandler extends HandlerAdapter {
    private Class<? extends RealmObject> table;
    private String set;
    private String orderBy;
    private String where;
    private String distinct;
    private int limit;
    private boolean eager;
    private DbResult dbResult;

    private UpdateHandler(Annotation[] annotations, Class<? extends RealmObject> table){
        this.table = table;
        buildField(annotations);
    }

    private void buildField(Annotation[] annotations) {
        if (null != annotations){
            for (Annotation annotation : annotations){
                if (annotation instanceof UPDATE){
                    UPDATE update = (UPDATE) annotation;
                    this.orderBy = update.orderBy();
                    this.where = update.where();
                    this.distinct = update.distinct();
                    this.limit = update.limit();
                    this.eager = update.eager();
                    this.set = update.set();
                }
            }
        }
    }

    public static HandlerAdapter parseAnnotations(Annotation[] annotations, Class<? extends RealmObject> table){
        return new UpdateHandler(annotations, table);
    }

    @SuppressWarnings("unchecked")
    @Override
    public DbResult invoke(Object[] args, final Type[] parameterTypes, Annotation[][] parameterAnnotationsArray) {
        dbResult = new DbResult();
       if (checkIfValidSet()){
           updateDataFromSet(args,parameterTypes);
       }
        return dbResult;
    }

    private void updateDataFromSet(final Object[] args, final Type[] parameterTypes){
        try{
            RealmQuery<? extends RealmObject> query = UStorage.realm.where(this.table);
            RealmQuery<? extends RealmObject> whereFilteredQuery = FindConditionUtil.setFilter(set ,where, query, args , parameterTypes);

            RealmQuery<? extends RealmObject> otherFilteredQuery = FindConditionUtil.otherFilter(whereFilteredQuery, orderBy, limit, distinct);

            final RealmResults result = otherFilteredQuery.findAllAsync();
            result.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults>() {
                @Override
                public void onChange(RealmResults realmResults, OrderedCollectionChangeSet changeSet) {
                    if (OrderedCollectionChangeSet.State.INITIAL == changeSet.getState()){
                        if (realmResults.size() > 0){
                            updateDataByTransAcion(realmResults,parameterTypes);
                        }else {
                            dbResult.setCount(0);
                            dbResult.setResultCallback(false,new Throwable("No data was selected"));
                        }

                    }

                }
            });
        }catch (Exception e){
            e.printStackTrace();
            dbResult.setCount(0);
            dbResult.setResultCallback(false,e);
        }
    }

    private void updateDataByTransAcion(final RealmResults realmResults, final Type[] parameterTypes){
        UStorage.realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                try {
                    Map<String, Object> setMap = FindConditionUtil.getSetMap();
                    Iterator<String> iterator = setMap.keySet().iterator();
                    dbResult.setCount(setMap.size() == 0 ? 0 : realmResults.size());
                    for (int i = 0;i < realmResults.size();i++){
                        Object instance = realmResults.get(i);
                        for (int j = 0; iterator.hasNext(); j++){
                            String next = iterator.next();
                            Class<?> rawType = CommonUtil.getRawType(getSetParameterTypes(parameterTypes)[j]);
                            instance.getClass().getMethod("set" + next,rawType).invoke( instance,setMap.get(next));
                        }
                    }
                    dbResult.setResultCallback(setMap.size() != 0,new Throwable("update annotation is invalid,please check your code"));
                } catch (Exception e) {
                    e.printStackTrace();
                    dbResult.setCount(0);
                    dbResult.setResultCallback(false,e.getCause());
                }
            }
        });

    }
    private Type[] getSetParameterTypes(Type[] types){
        if (!CommonUtil.isEmptyStr(set)){
            // 根据指定的字符构建正则
            Pattern pattern = Pattern.compile(EQUAL_TO);
            // 构建字符串和正则的匹配
            Matcher matcher = pattern.matcher(set);
            int count = 0;
            // 循环依次往下匹配
            while (matcher.find()){ // 如果匹配,则数量+1
                count++;
            }
            Type[] setTypes = new Type[count];
            for (int i = 0;i < count;i++){
                setTypes[count - i - 1] = types[types.length - i - 1];
            }
            return setTypes;
        }
        throw new ErrorParamsException("update method annotation is invalid,please check your code");
    }
    /**
     * 验证通过条件来更新表数据  参数是否合法
     * @return
     */
    private boolean checkIfValidSet(){
        if (CommonUtil.isEmptyStr(set)){
            throw new ErrorParamsException("update method annotation is invalid,please check your code");
        }
        return true;
    }
}
