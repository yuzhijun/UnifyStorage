package com.winning.unifystorage;

import com.winning.unifystorage_core.annotations.DB;
import com.winning.unifystorage_core.annotations.FIELD;
import com.winning.unifystorage_core.annotations.SAVE;

/**
 * 2018/11/29
 * Created by SharkChao
 * 827623353@qq.com
 * https://github.com/sharkchao
 */
public interface ApiDataBase {

    @DB(table = User.class)
    @SAVE
    boolean saveUser(@FIELD User user);
}
