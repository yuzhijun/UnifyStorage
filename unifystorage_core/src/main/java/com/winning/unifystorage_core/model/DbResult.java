package com.winning.unifystorage_core.model;

import io.realm.OrderedCollectionChangeSet;
import io.realm.RealmResults;

/**
 * 2018/11/30
 * Created by SharkChao
 * 827623353@qq.com
 * https://github.com/sharkchao
 */
public class DbResult<T>{
    private T result;
    private int count;
    private DbResultCallback mResultCallback;
    private DbFindCallBack mDbFindCallBack;
    private boolean isSuccess;
    private Throwable mThrowable;
    private boolean hasObserver;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public interface DbResultCallback<T>{
        void onSuccess(int count,T result);
        void onError(Throwable error);
    }

    public interface DbFindCallBack{
        void onFirstFindResult(RealmResults realmResults);
        void onChange(RealmResults realmResults);
    }

    public void registerDbFindCallBack(DbFindCallBack dbFindCallBack){
        this.mDbFindCallBack = dbFindCallBack;
    }

    public void setDbFindCallBack(RealmResults realmResults,OrderedCollectionChangeSet changeSet){
        if (null != mDbFindCallBack){
            if (null == changeSet){
                mDbFindCallBack.onFirstFindResult(realmResults);
            }else {
                mDbFindCallBack.onChange(realmResults);
            }
        }
    }

    public void registerCallback(DbResultCallback callback){
        mResultCallback = callback;
        hasObserver = true;
        setResultCallback(isSuccess,mThrowable);
    }

     public void setResultCallback(boolean success,Throwable throwable){
        isSuccess = success;
        mThrowable = throwable;
        if (mResultCallback != null){
            hasObserver = true;
            if (isSuccess){
                mResultCallback.onSuccess(count,result);
            }else if (throwable != null){
                mResultCallback.onError(throwable);
            }
        }else {
            hasObserver = false;
        }
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
