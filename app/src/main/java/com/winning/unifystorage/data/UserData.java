package com.winning.unifystorage.data;

import com.winning.unifystorage.model.User;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;

/**
 * 2018/12/3
 * Created by SharkChao
 * 827623353@qq.com
 * https://github.com/sharkchao
 */
public class UserData {
    private static User user;
    private static List<User>userList = new ArrayList<>();
    private static User[] userArray;
    private static RealmResults realmResults;

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        UserData.user = user;
    }

    public static List<User> getUserList() {
        return userList;
    }

    public static void setUserList(List<User> userList) {
        UserData.userList = userList;
    }

    public static User[] getUserArray() {
        return userArray;
    }

    public static void setUserArray(User[] userArray) {
        UserData.userArray = userArray;
    }

    public static RealmResults getRealmResults() {
        return realmResults;
    }

    public static void setRealmResults(RealmResults realmResults) {
        UserData.realmResults = realmResults;
    }
}
