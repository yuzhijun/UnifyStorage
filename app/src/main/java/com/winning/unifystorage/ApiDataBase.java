package com.winning.unifystorage;

import com.winning.unifystorage.model.Fake;
import com.winning.unifystorage.model.User;
import com.winning.unifystorage_core.annotations.DB;
import com.winning.unifystorage_core.annotations.DELETE;
import com.winning.unifystorage_core.annotations.FIND;
import com.winning.unifystorage_core.annotations.SAVE;
import com.winning.unifystorage_core.annotations.SAVEORUPDATE;
import com.winning.unifystorage_core.annotations.UPDATE;
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
    DbResult saveUser(User user);

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
    DbResult findAll();

    @DB(table = User.class)
    @FIND(where = "name = ? and age > ?",limit = 10,orderBy = "age")
    DbResult<User> findUser(String name, int age);

    @DB(table = User.class)
    @FIND(where = "name in ?",limit = 10)
    DbResult findUsers(List<String> users);

    @DB(table = User.class)
    @DELETE(where = "name = ?")
    DbResult deleteUsersByQuery();

    @DB(table = User.class)
    @FIND(where = "name in ?")
    DbResult<User> findUserByIn(List<String> users);

    @DB(table = User.class)
    @FIND(where = "name contains ?",distinct = "name")
    DbResult findUserByContains(String name);

    @DB(table = User.class)
    @FIND(where = "name like ? and age > ?",distinct = "name")
    DbResult findUserByLike(String name, int age);

    @DB(table = User.class)
    @FIND(where = "? notnull",limit = 2)
    DbResult findUserByNotNull(String name);

    @DB(table = User.class)
    @UPDATE(where = "name = ?",set = "name = ? and age = ?")
    DbResult updateUsersByQuery(String oldName,String newName,String age);

}
