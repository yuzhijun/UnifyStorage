package com.winning.unifystorage_core.model;

/**
 * 2018/11/30
 * Created by SharkChao
 * 827623353@qq.com
 * https://github.com/sharkchao
 */
public class DbResult {
    private int count;
    private boolean isSuccess;
    private Throwable mThrowable;
    private DbResultCallback mResultCallback;
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
        if (isSuccess){

        }else {

        }
    }

    public DbResultCallback getResultCallback() {
        return mResultCallback;
    }


    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public void setThrowable(Throwable throwable) {
        mThrowable = throwable;
    }
}
