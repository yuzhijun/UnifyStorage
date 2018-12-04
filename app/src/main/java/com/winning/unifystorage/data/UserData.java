package com.winning.unifystorage.data;

import io.realm.RealmResults;

/**
 * 2018/12/3
 * Created by SharkChao
 * 827623353@qq.com
 * https://github.com/sharkchao
 */
public class UserData {
    private static RealmResults realmResults;


    public static RealmResults getRealmResults() {
        return realmResults;
    }

    public static void setRealmResults(RealmResults realmResults) {
        UserData.realmResults = realmResults;
    }
}
