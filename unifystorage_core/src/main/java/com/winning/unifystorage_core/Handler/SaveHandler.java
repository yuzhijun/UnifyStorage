package com.winning.unifystorage_core.Handler;

import com.winning.unifystorage_core.HandlerAdapter;
import com.winning.unifystorage_core.UStorage;
import com.winning.unifystorage_core.annotations.SAVE;
import com.winning.unifystorage_core.exception.ErrorParamsException;
import com.winning.unifystorage_core.model.DbResult;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
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
        if (checkIfValid(args,parameterTypes,parameterAnnotationsArray)){
            UStorage.realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    if (parameterTypes[0] instanceof  RealmObject && parameterTypes[0].getClass().isArray()){
                        List<RealmObject> realmObjects = realm.copyToRealm(Arrays.asList((RealmObject[]) args[0]));
                        result.setCount(realmObjects.size());
                    } else if (parameterTypes[0] instanceof  RealmObject){
                         realm.copyToRealm(((RealmObject) args[0]));
                        result.setCount(1);
                    } else if (parameterTypes[0] instanceof ParameterizedType){
                        ParameterizedType type = (ParameterizedType) parameterTypes[0];
                        if (type.getRawType() instanceof List && type.getActualTypeArguments()[0] instanceof RealmObject){
                            List<RealmObject> realmObjects = realm.copyToRealm((List<RealmObject>) args[0]);
                            result.setCount(realmObjects.size());
                        }
                    }
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    result.setResult(true,null);
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                    result.setResult(false,error);
                }
            });
        }
        return result;
    }

    /**
     * 验证参数是否合法
     * @param args
     * @param parameterTypes
     * @param parameterAnnotationsArray
     * @return
     */
    private boolean checkIfValid(Object[] args, Type[] parameterTypes, Annotation[][] parameterAnnotationsArray){
        if (parameterAnnotationsArray.length == 1
                && parameterAnnotationsArray[0].length == 1
                && args.length == 1
                && parameterAnnotationsArray[0][0].annotationType().equals(SAVE.class)
                && parameterTypes[0] instanceof RealmObject){
            return true;
        }
        throw new ErrorParamsException("save method params not valid");
    }
}
