package com.winning.unifystorage;

import com.winning.unifystorage.model.Fake;
import com.winning.unifystorage.model.User;
import com.winning.unifystorage_core.Utils.Constants;
import com.winning.unifystorage_core.annotations.DB;
import com.winning.unifystorage_core.annotations.DELETE;
import com.winning.unifystorage_core.annotations.FIND;
import com.winning.unifystorage_core.annotations.GETJSON;
import com.winning.unifystorage_core.annotations.JSON;
import com.winning.unifystorage_core.annotations.SAVE;
import com.winning.unifystorage_core.annotations.SAVEORUPDATE;
import com.winning.unifystorage_core.annotations.UPDATE;
import com.winning.unifystorage_core.model.DbResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * 2018/11/29
 * Created by SharkChao
 * 827623353@qq.com
 * https://github.com/sharkchao
 */
public interface ApiDataBase {

    @DB(table = User.class)
    @SAVE
    DbResult saveUser(User user);

    @DB(table = User.class)
    @SAVE(type = Constants.JSON_OBJECT)
    DbResult saveUsersByJsonObject(String jsonObject);

    @DB(table = User.class)
    @SAVE(type = Constants.JSON_ARRAY)
    DbResult saveUsersByJsonArray(String jsonArray);

    @DB(table = User.class)
    @SAVEORUPDATE(type = Constants.JSON_ARRAY)
    DbResult saveOrUpdateUsersByJsonArray(String jsonArray);

    @DB(table = User.class)
    @SAVEORUPDATE(type = Constants.JSON_OBJECT)
    DbResult saveOrUpdateUsersByJsonObject(String jsonObject);

    @DB(table = User.class)
    @SAVE
    DbResult saveUsersByList(List<User> user);

    @DB(table = User.class)
    @SAVE
    DbResult saveUsersByArray(User[] user);

    @DB(table = User.class)
    @SAVE
    DbResult saveFake(Fake fake);


    @DB(table = User.class)
    @SAVEORUPDATE
    DbResult saveOrUpdateUser(User user);

    @DB(table = User.class)
    @SAVEORUPDATE
    DbResult saveOrUpdateUsersByList(List<User> user);

    @DB(table = User.class)
    @SAVEORUPDATE
    DbResult saveOrUpdateUsersByArray(User[] user);

    @DB(table = User.class)
    @FIND
    DbResult<User> findAll();

    @DB(table = User.class)
    @FIND(where = "name = ? and (age > ? or sex = ?)",limit = 10,orderBy = "age desc")
    DbResult<User> findUser(String name, int age, String sex);

    @DB(table = User.class)
    @FIND(where = "name in ?",limit = 10)
    DbResult<User> findUsers(List<String> users);

    @DB(table = User.class)
    @DELETE(where = "name = ?")
    DbResult deleteUsersByQuery();

    @DB(table = User.class)
    @FIND(where = "name in ?")
    DbResult<User> findUserByIn(List<String> users);

    @DB(table = User.class)
    @FIND(where = "name contains ?",distinct = "name")
    DbResult<User> findUserByContains(String name);

    @DB(table = User.class)
    @FIND(where = "name like ? and age > ?",distinct = "name")
    DbResult<User> findUserByLike(String name, int age);

    @DB(table = User.class)
    @FIND(where = "? notnull",limit = 2)
    DbResult<User> findUserByNotNull(String name);

    @DB(table = User.class)
    @UPDATE(where = "name = ?",set = "name = ? and age = ?")
    DbResult updateUsersByQuery(String oldName,String newName,String age);


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

    @GET("/getUser")
    Call<User> getUser();

    @GET("/getUserList")
    Call<List<User>> getUserList();
}
