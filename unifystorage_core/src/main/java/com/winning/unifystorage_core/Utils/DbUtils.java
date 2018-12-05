package com.winning.unifystorage_core.Utils;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * 2018/12/5
 * Created by SharkChao
 * 827623353@qq.com
 * https://github.com/sharkchao
 */
public class DbUtils {
    public static void deleteAll(final RealmResults realmResults){
        realmResults.getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realmResults.deleteAllFromRealm();
            }
        });
    }
    public static void deleteItem(final RealmObject realmObject){
        realmObject.getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realmObject.deleteFromRealm();
            }
        });
    }
}
