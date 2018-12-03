package com.winning.unifystorage;

import com.winning.unifystorage.model.Fake;
import com.winning.unifystorage.model.User;
import com.winning.unifystorage_core.annotations.DB;
import com.winning.unifystorage_core.annotations.DELETE;
import com.winning.unifystorage_core.annotations.FIND;
import com.winning.unifystorage_core.annotations.GETJSON;
import com.winning.unifystorage_core.annotations.JSON;
import com.winning.unifystorage_core.annotations.Model;
import com.winning.unifystorage_core.annotations.SAVE;
import com.winning.unifystorage_core.annotations.SAVEORUPDATE;
import com.winning.unifystorage_core.model.DbResult;

import java.util.List;

/**
 * 2018/11/29
 * Created by SharkChao
 * 827623353@qq.com
 * https://github.com/sharkchao
 */
public interface ApiDataBase {

    @DB(table = User.class)
    @SAVE
    DbResult saveUser(@Model User user);

    @DB(table = User.class)
    @SAVE
    DbResult saveUsersByList(@Model List<User> user);

    @DB(table = User.class)
    @SAVE
    DbResult saveUsersByArray(@Model User[] user);

    @DB(table = User.class)
    @SAVE
    DbResult saveFake(@Model Fake fake);


    @DB(table = User.class)
    @SAVEORUPDATE
    DbResult saveOrUpdateUser(@Model User user);

    @DB(table = User.class)
    @SAVEORUPDATE
    DbResult saveOrUpdateUsersByList(@Model List<User> user);

    @DB(table = User.class)
    @SAVEORUPDATE
    DbResult saveOrUpdateUsersByArray(@Model User[] user);

    @DB(table = User.class)
    @FIND
    DbResult findAll();

    @DB(table = User.class)
    @FIND(where = "name = ? and age > ?",limit = 10,orderBy = "age")
    DbResult findUser(String name, int age);

    @DB(table = User.class)
    @FIND(where = "name in ?",limit = 10)
    DbResult findUsers(List<String> users);

    @DB(table = User.class)
    @DELETE
    DbResult deleteUsersByQuery();

    @DB(table = User.class)
    @DELETE
    DbResult deleteUsersByObject(@Model User user);

    @DB(table = User.class)
    @DELETE
    DbResult deleteUsersByArray(@Model User[] user);

    @DB(table = User.class)
    @DELETE
    DbResult deleteUsersByList(@Model List<User> user);

    @DB(table = User.class)
    @FIND(where = "name in ?")
    DbResult findUserByIn(List<String> users);

    @DB(table = User.class)
    @FIND(where = "name contains ?",distinct = "name")
    DbResult findUserByContains(String name);

    @DB(table = User.class)
    @FIND(where = "name like ? and age > ?",distinct = "name")
    DbResult findUserByLike(String name, int age);

    @DB(table = User.class)
    @FIND(where = "? notnull",limit = 2)
    DbResult findUserByNotNull(String name);

    @JSON(key = "JSON_KEY",convert = User.class)
    boolean saveJson(User user);

    @JSON(key = "JSON_ARRAY",convert = User.class)
    boolean saveJsonArray(List<User> users);

    @JSON(key = "JSON_STR")
    boolean saveStr(String str);

    @JSON(key = "JSON_INT")
    boolean saveInt(int ints);

    @JSON(key = "JSON_BOOL")
    boolean saveBool(boolean booleans);

    @GETJSON(key = "JSON_KEY",convert=User.class)
    DbResult<User> getJson();

    @GETJSON(key = "JSON_ARRAY",convert = User[].class)
    DbResult<List<User>> getJsonArray();

    @GETJSON(key = "JSON_STR")
    DbResult<String> getStr();

    @GETJSON(key="JSON_BOOL",convert = Boolean.class)
    DbResult<Boolean> getBool();

    @GETJSON(key = "JSON_INT",convert = Integer.class)
    DbResult<Integer> getInt();
}
