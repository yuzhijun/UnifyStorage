package com.winning.unifystorage.data;

import com.winning.unifystorage.model.User;

import io.realm.RealmResults;

/**
 * 2018/12/5
 * Created by SharkChao
 * 827623353@qq.com
 * https://github.com/sharkchao
 */
public class UserData {
    public static RealmResults<User> mResults;

    public static RealmResults<User> getmResults() {
        return mResults;
    }

    public static void setmResults(RealmResults<User> mResults) {
        UserData.mResults = mResults;
    }
}
