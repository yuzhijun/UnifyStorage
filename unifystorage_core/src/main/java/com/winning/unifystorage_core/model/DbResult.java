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
    private DbFindCallBack<T> mDbFindCallBack;
    private boolean isSuccess;
    private Throwable mThrowable;
    private boolean hasObserver;

    public DbResult(){
    }

    public DbResult(T result){
        this.result = result;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public interface DbResultCallback{
        void onSuccess(int count);
        void onError(Throwable error);
    }

    public interface DbFindCallBack<T>{
        void onFirstFindResult(RealmResults<T> realmResults);
        void onChange(RealmResults<T> realmResults);
    }

    public void registerDbFindCallBack(DbFindCallBack<T> dbFindCallBack){
        this.mDbFindCallBack = dbFindCallBack;
    }

    public void setDbFindCallBack(RealmResults realmResults,OrderedCollectionChangeSet changeSet){
        if (null != mDbFindCallBack){
            if (OrderedCollectionChangeSet.State.INITIAL == changeSet.getState()){
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
                mResultCallback.onSuccess(count);
            }else if (throwable != null){
                mResultCallback.onError(throwable);
            }
        }else {
            hasObserver = false;
        }
    }
}
