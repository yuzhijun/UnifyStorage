package com.winning.unifystorage_core.Handler;

import android.support.annotation.NonNull;

import com.winning.unifystorage_core.HandlerAdapter;
import com.winning.unifystorage_core.UStorage;
import com.winning.unifystorage_core.Utils.CommonUtil;
import com.winning.unifystorage_core.Utils.Constants;
import com.winning.unifystorage_core.annotations.SAVEORUPDATE;
import com.winning.unifystorage_core.exception.ErrorParamsException;
import com.winning.unifystorage_core.model.DbResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmObject;

public class SaveOrUpdateHandler extends HandlerAdapter {

    private DbResult result;
    private int jsonType;
    private Class<? extends RealmObject> table;
    private SaveOrUpdateHandler(Annotation[] annotations,Class<? extends RealmObject> table){
        this.table = table;
        buildField(annotations);
    }
    private void buildField(Annotation[] annotations) {
        if (null != annotations){
            for (Annotation annotation : annotations){
                if (annotation instanceof SAVEORUPDATE){
                    SAVEORUPDATE save = (SAVEORUPDATE) annotation;
                    this.jsonType = save.type();
                }
            }
        }
    }
    public static HandlerAdapter parseAnnotations(Annotation[] annotations,Class<? extends RealmObject> table){
        return new SaveOrUpdateHandler(annotations,table);
    }

    @Override
    public DbResult invoke(final Object[] args, final Type[] parameterTypes, Annotation[][] parameterAnnotationsArray) {
        result = new DbResult();
        if (checkIfValid(args)){
            UStorage.realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Class<?> rawType = CommonUtil.getRawType(parameterTypes[0]);
                    if (jsonType == Constants.REALM_DATA){//因为realm针对不同类型的存储是不一样的，所以这个地方进行判断
                        saveDataByRealm(realm, rawType, args);
                    }else if (jsonType == Constants.JSON_OBJECT){
                        saveDataByJsonObject(realm, rawType, args);
                    }else if (jsonType == Constants.JSON_ARRAY){
                        saveDatByJsonArray(realm, rawType, args);
                    }
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {

                    result.setResultCallback(true,null);
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                    result.setResultCallback(false,error);
                }
            });
        }
        return result;
    }
    private void saveDatByJsonArray(@NonNull Realm realm, Class<?> rawType, Object[] args) {
        if (String.class.isAssignableFrom(rawType)){
            try {
                JSONArray jsonArray = new JSONArray((String) args[0]);
                realm.createOrUpdateAllFromJson(table,jsonArray);
                result.setCount(jsonArray.length());
            } catch (Exception e) {
                e.printStackTrace();
                result.setCount(0);
                result.setResultCallback(false, e);
            }
        }else {
            result.setCount(0);
            result.setResultCallback(false,new Throwable("save or update method parameter is invalid,please check your code"));
        }
    }
    private void saveDataByJsonObject(@NonNull Realm realm, Class<?> rawType, Object[] args) {
        if (String.class.isAssignableFrom(rawType)){
            try {
                JSONObject jsonObject = new JSONObject((String) args[0]);
                realm.createOrUpdateObjectFromJson(table,jsonObject);
                result.setCount(1);
            } catch (Exception e) {
                e.printStackTrace();
                result.setCount(0);
                result.setResultCallback(false, e);
            }
        }else {
            result.setCount(0);
            result.setResultCallback(false,new Throwable("save or update method parameter is invalid,please check your code"));
        }
    }
    private void saveDataByRealm(Realm realm, Class<?> rawType, Object[] args) {
        if (RealmObject[].class.isAssignableFrom(rawType) && rawType.isArray()){
            List<RealmObject> realmObjects = realm.copyToRealmOrUpdate(Arrays.asList((RealmObject[]) args[0]));
            result.setCount(realmObjects.size());
        } else if (RealmObject.class.isAssignableFrom(rawType)){
            realm.copyToRealmOrUpdate(((RealmObject) args[0]));
            result.setCount(1);
        } else if (List.class.isAssignableFrom(rawType)){
            List<RealmObject> realmObjects = realm.copyToRealmOrUpdate((List<RealmObject>) args[0]);
            result.setCount(realmObjects.size());
        }else {
            result.setCount(0);
            result.setResultCallback(false,new Throwable("save or update method parameter is invalid,please check your code"));
        }
    }

    /**
     * 验证参数是否合法
     * @param args
     * @return
     */
    private boolean checkIfValid(Object[] args){
        if (args.length == 1){
            return true;
        }
        throw new ErrorParamsException("save or update method parameter is invalid,please check your code");
    }
}
