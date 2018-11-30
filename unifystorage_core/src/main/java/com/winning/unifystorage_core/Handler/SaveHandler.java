package com.winning.unifystorage_core.Handler;

import android.support.annotation.NonNull;

import com.winning.unifystorage_core.HandlerAdapter;
import com.winning.unifystorage_core.UStorage;
import com.winning.unifystorage_core.Utils.CommonUtil;
import com.winning.unifystorage_core.annotations.FIELD;
import com.winning.unifystorage_core.exception.ErrorParamsException;
import com.winning.unifystorage_core.model.DbResult;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmObject;

public class SaveHandler extends HandlerAdapter {

    private DbResult result;
    private SaveHandler(Annotation[] annotations){
    }

    public static HandlerAdapter parseAnnotations(Annotation[] annotations){
        return new SaveHandler(annotations);
    }

    @Override
    public DbResult invoke(final Object[] args, final Type[] parameterTypes, Annotation[][] parameterAnnotationsArray) {
       result = new DbResult();
        if (checkIfValid(args,parameterAnnotationsArray)){
            UStorage.realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(@NonNull Realm realm) {

                    Class<?> rawType = CommonUtil.getRawType(parameterTypes[0]);
                    if (RealmObject[].class.isAssignableFrom(rawType) && rawType.isArray()){
                        List<RealmObject> realmObjects = realm.copyToRealm(Arrays.asList((RealmObject[]) args[0]));
                        result.setCount(realmObjects.size());
                    } else if (RealmObject.class.isAssignableFrom(rawType)){
                        realm.copyToRealm((RealmModel) args[0]);
                        result.setCount(1);
                    } else if (List.class.isAssignableFrom(rawType)){
                        List<RealmObject> realmObjects = realm.copyToRealm((List<RealmObject>) args[0]);
                        result.setCount(realmObjects.size());
                    }else {
                        throw new ErrorParamsException("save method parameter is invalid,please check your code");
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

    /**
     * 验证参数是否合法
     * @param args
     * @param parameterAnnotationsArray
     * @return
     */
    private boolean checkIfValid(Object[] args , Annotation[][] parameterAnnotationsArray){
        if (args.length == 1
                &&parameterAnnotationsArray.length == 1
                && parameterAnnotationsArray[0].length == 1
                && parameterAnnotationsArray[0][0].annotationType() == FIELD.class){
            return true;
        }
        throw new ErrorParamsException("save method parameter is invalid,please check your code");
    }
}
