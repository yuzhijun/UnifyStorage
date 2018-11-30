package com.winning.unifystorage;

import com.winning.unifystorage_core.annotations.DB;
import com.winning.unifystorage_core.annotations.FIELD;
import com.winning.unifystorage_core.annotations.FIND;
import com.winning.unifystorage_core.annotations.SAVE;
import com.winning.unifystorage_core.model.DbResult;

import java.util.List;

import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * 2018/11/29
 * Created by SharkChao
 * 827623353@qq.com
 * https://github.com/sharkchao
 */
public interface ApiDataBase {

    @DB(table = User.class)
    @SAVE
    DbResult saveUser(@FIELD List<User> user);

    @DB(table = User.class)
    @FIND(where = "name = ? and age > ?",limit = 10,orderBy = "age")
    RealmResults<? extends RealmObject> findUser(String name, int age);
}
