package com.winning.unifystorage_core.model;

/**
 * 2018/11/30
 * Created by SharkChao
 * 827623353@qq.com
 * https://github.com/sharkchao
 */
public class DbResult {
    private int count;
    private DbResultCallback mResultCallback;
    private boolean isSuccess;
    private Throwable mThrowable;
    private boolean hasObserver;

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
