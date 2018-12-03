package com.winning.unifystorage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.winning.unifystorage_core.model.DbResult;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    private Button btnInsert;
    private Button btnFind;
    private ApiDataBase mApiDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnInsert = findViewById(R.id.btnInsert);
        btnFind = findViewById(R.id.btnFind);

        mApiDataBase = ApiServiceModule.getInstance().provideApiService(ApiDataBase.class);


        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              insert1();
            }
        });
        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                mApiDataBase.findUser("sharkchao", 10)
//                .registerDbFindCallBack(new DbResult.DbFindCallBack() {
//                    @Override
//                    public void onFirstFindResult(RealmResults realmResults) {
//                        realmResults.size();
//                    }
//                    @Override
//                    public void onChange(RealmResults realmResults) {
//
//                    }
//                });

                List<String> users = new ArrayList<>();
                users.add("yuzhijun");
                users.add("sharkchao");

                mApiDataBase.findUsers(users).registerDbFindCallBack(new DbResult.DbFindCallBack() {
                    @Override
                    public void onFirstFindResult(RealmResults realmResults) {
                        realmResults.size();
                    }

                    @Override
                    public void onChange(RealmResults realmResults) {

                    }
                });
            }
        });
    }

    private void insert1(){
        User user = new User();
        user.setAge("20");
        user.setName("sharkchao");
        user.setSex("男");
        mApiDataBase.saveUser(user).registerCallback(new DbResult.DbResultCallback() {
            @Override
            public void onSuccess(int count) {
                Toast.makeText(MainActivity.this, "成功!"+count, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(Throwable error) {
                Toast.makeText(MainActivity.this, "失败"+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void insert2(){
        List<User>list = new ArrayList<>();

        User user1 = new User();
        user1.setAge("20");
        user1.setName("小明");
        user1.setSex("男");

        User user2 = new User();
        user2.setAge("15");
        user2.setName("小红");
        user2.setSex("女");

        list.add(user1);
        list.add(user2);
        mApiDataBase.saveUsersByList(list).registerCallback(new DbResult.DbResultCallback() {
            @Override
            public void onSuccess(int count) {
                Toast.makeText(MainActivity.this, "成功!"+count, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(Throwable error) {
                Toast.makeText(MainActivity.this, "失败"+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void insert3(){
        User[]users = new User[2];

        User user1 = new User();
        user1.setAge("20");
        user1.setName("小绿");
        user1.setSex("男");

        User user2 = new User();
        user2.setAge("15");
        user2.setName("小白");
        user2.setSex("女");

        users[0] = user1;
        users[1] = user2;
        mApiDataBase.saveUsersByArray(users).registerCallback(new DbResult.DbResultCallback() {
            @Override
            public void onSuccess(int count) {
                Toast.makeText(MainActivity.this, "成功!"+count, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(Throwable error) {
                Toast.makeText(MainActivity.this, "失败"+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveFake(){
        mApiDataBase.saveFake(new Fake()).registerCallback(new DbResult.DbResultCallback() {
            @Override
            public void onSuccess(int count) {
                Toast.makeText(MainActivity.this, "成功!"+count, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(Throwable error) {
                Toast.makeText(MainActivity.this, "失败"+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
