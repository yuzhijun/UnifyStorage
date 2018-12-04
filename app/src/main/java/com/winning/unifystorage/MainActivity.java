package com.winning.unifystorage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.winning.unifystorage.model.Fake;
import com.winning.unifystorage.model.User;
import com.winning.unifystorage_core.model.DbResult;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button saveUserByObject;
    private Button saveUsersByList;
    private Button saveUsersByArray;
    private Button saveOrUpdateObject;
    private Button saveOrUpdateUsersByList;
    private Button saveOrUpdateUsersByArray;
    private Button deleteUsersByResult;
    private Button deleteUsersByQuery;
    private Button updateUsersByResult;
    private Button updateUsersByQuery;
    private Button btnFindAll;
    private Button btnFindUser;
    private Button btnFindUserByIn;
    private Button btnFindUserByContains;
    private Button btnFindUserByLike;
    private Button btnFindUserByNotNull;
    private ApiDataBase mApiDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        saveUserByObject = findViewById(R.id.saveUserByObject);
        saveUsersByList = findViewById(R.id.saveUsersByList);
        saveUsersByArray = findViewById(R.id.saveUsersByArray);
        saveOrUpdateObject = findViewById(R.id.saveOrUpdateObject);
        saveOrUpdateUsersByList = findViewById(R.id.saveOrUpdateUsersByList);
        saveOrUpdateUsersByArray = findViewById(R.id.saveOrUpdateUsersByArray);

        btnFindAll = findViewById(R.id.btnFindAll);
        btnFindUser = findViewById(R.id.btnFindUser);
        btnFindUserByIn = findViewById(R.id.btnFindUserByIn);
        btnFindUserByContains = findViewById(R.id.btnFindUserByContains);
        btnFindUserByLike = findViewById(R.id.btnFindUserByLike);
        btnFindUserByNotNull = findViewById(R.id.btnFindUserByNotNull);

        deleteUsersByResult = findViewById(R.id.deleteUsersByResult);
        deleteUsersByQuery = findViewById(R.id.deleteUsersByQuery);

        updateUsersByResult = findViewById(R.id.updateUsersByResult);
        updateUsersByQuery = findViewById(R.id.updateUsersByQuery);

        saveUserByObject.setOnClickListener(this);
        saveUsersByArray.setOnClickListener(this);
        saveUsersByList.setOnClickListener(this);
        saveOrUpdateObject.setOnClickListener(this);
        saveOrUpdateUsersByArray.setOnClickListener(this);
        saveOrUpdateUsersByList.setOnClickListener(this);
        deleteUsersByResult.setOnClickListener(this);
        deleteUsersByQuery.setOnClickListener(this);
        updateUsersByResult.setOnClickListener(this);
        updateUsersByQuery.setOnClickListener(this);

        mApiDataBase = ApiServiceModule.getInstance().provideApiService(ApiDataBase.class);


        btnFindAll.setOnClickListener(view -> mApiDataBase.findAll().registerDbFindCallBack(new DbResult.DbFindCallBack() {
            @Override
            public void onFirstFindResult(RealmResults realmResults) {
                realmResults.size();
            }

            @Override
            public void onChange(RealmResults realmResults) {
            }
        }));

        btnFindUser.setOnClickListener(view -> mApiDataBase.findUser("sharkchao", 10)
        .registerDbFindCallBack(new DbResult.DbFindCallBack<User>() {
            @Override
            public void onFirstFindResult(RealmResults<User> realmResults) {

            }

            @Override
            public void onChange(RealmResults<User> realmResults) {

            }
        }));

        btnFindUserByIn.setOnClickListener(view -> {
            List<String> users = new ArrayList<>();
            users.add("yuzhijun");
            users.add("sharkchao");

            mApiDataBase.findUserByIn(users).registerDbFindCallBack(new DbResult.DbFindCallBack() {
                @Override
                public void onFirstFindResult(RealmResults realmResults) {
                    Toast.makeText(MainActivity.this, "realmResult"+realmResults.size(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onChange(RealmResults realmResults) {
                    Toast.makeText(MainActivity.this, "realmResult"+realmResults.size(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnFindUserByContains.setOnClickListener(view -> mApiDataBase.findUserByContains("shark").registerDbFindCallBack(new DbResult.DbFindCallBack() {
            @Override
            public void onFirstFindResult(RealmResults realmResults) {
                realmResults.size();
            }

            @Override
            public void onChange(RealmResults realmResults) {

            }
        }));

        btnFindUserByLike.setOnClickListener(view -> mApiDataBase.findUserByLike("sha*", 10).registerDbFindCallBack(new DbResult.DbFindCallBack() {
               @Override
               public void onFirstFindResult(RealmResults realmResults) {
                   realmResults.size();
               }

               @Override
               public void onChange(RealmResults realmResults) {

               }
           }));

        btnFindUserByNotNull.setOnClickListener(view -> mApiDataBase.findUserByNotNull("name").registerDbFindCallBack(new DbResult.DbFindCallBack() {
            @Override
            public void onFirstFindResult(RealmResults realmResults) {
                realmResults.size();
            }

            @Override
            public void onChange(RealmResults realmResults) {

            }
        }));




    }

    private void saveUserByObject(){
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
    private void saveUsersByList(){
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
    private void saveUsersByArray(){
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
    private void saveOrUpdateObject(){
        User user = new User();
        user.setId("d07ea3e1-2f75-4363-91c3-697570c882e6");
        user.setAge("20");
        user.setName("小熊");
        user.setSex("男");
        mApiDataBase.saveOrUpdateUser(user).registerCallback(new DbResult.DbResultCallback() {

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
    private void saveOrUpdateUsersByList(){
        List<User>list = new ArrayList<>();

        User user1 = new User();
        user1.setAge("22");
        user1.setName("小明");
        user1.setSex("男");
        user1.setId("b1d92a7f-6201-4d5f-be37-4fb33283ce44");

        User user2 = new User();
        user2.setAge("18");
        user2.setName("小红");
        user2.setSex("女");
        user2.setId("79be8e66-e1d0-4386-96c8-9e71a20ecd42");

        list.add(user1);
        list.add(user2);
        mApiDataBase.saveOrUpdateUsersByList(list).registerCallback(new DbResult.DbResultCallback() {

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
    private void saveOrUpdateUsersByArray(){
        User[]users = new User[2];

        User user1 = new User();
        user1.setAge("22");
        user1.setName("小绿");
        user1.setSex("男");
        user1.setId("56ea25d8-ad50-4422-aa47-63c6e4901c3b");


        User user2 = new User();
        user2.setAge("18");
        user2.setName("小白");
        user2.setSex("女");
        user2.setId("6b6ea802-d499-4ba7-9ba5-62de4fc5abc8");

        users[0] = user1;
        users[1] = user2;
        mApiDataBase.saveOrUpdateUsersByArray(users).registerCallback(new DbResult.DbResultCallback() {

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

    private void deleteUsersByResult(){
        List<String> users = new ArrayList<>();
        users.add("yuzhijun");
        users.add("sharkchao");

        mApiDataBase.findUserByIn(users).registerDbFindCallBack(new DbResult.DbFindCallBack() {
            @Override
            public void onFirstFindResult(RealmResults realmResults) {
                Toast.makeText(MainActivity.this, "realmResult"+realmResults.size(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChange(RealmResults realmResults) {
                Toast.makeText(MainActivity.this, "realmResult"+realmResults.size(), Toast.LENGTH_SHORT).show();
                if (realmResults.size() > 0){
                    realmResults.getRealm().executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realmResults.deleteAllFromRealm();
                        }
                    });

                }
            }
        });
    }

    private void deleteUsersByQuery(){

        mApiDataBase.deleteUsersByQuery().registerCallback(new DbResult.DbResultCallback() {

            @Override
            public void onSuccess(int count) {
                Toast.makeText(MainActivity.this, "deleteUsersByQuery成功!"+count, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable error) {
                Toast.makeText(MainActivity.this, "deleteUsersByQuery失败"+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updateUsersByQuery(){

        mApiDataBase.updateUsersByQuery("sharkchao","小红","100").registerCallback(new DbResult.DbResultCallback() {

            @Override
            public void onSuccess(int count) {
                Toast.makeText(MainActivity.this, "updateUsersByQuery成功!"+count, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable error) {
                Toast.makeText(MainActivity.this, "updateUsersByQuery失败"+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updateUsersByResult(){
        List<String> users = new ArrayList<>();
        users.add("yuzhijun");
        users.add("sharkchao");

        mApiDataBase.findUserByIn(users).registerDbFindCallBack(new DbResult.DbFindCallBack<User>() {
            @Override
            public void onFirstFindResult(RealmResults<User> realmResults) {
                Toast.makeText(MainActivity.this, "onFirstFindResult", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChange(RealmResults<User> realmResults) {
                if (realmResults.size() > 0){
                    realmResults.getRealm().executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realmResults.get(0).setName("刘超");
                        }
                    });

                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.saveUserByObject:
                saveUserByObject();
                break;
            case R.id.saveUsersByList:
                saveUsersByList();
                break;
            case R.id.saveUsersByArray:
                saveUsersByArray();
                break;
            case R.id.saveOrUpdateObject:
                saveOrUpdateObject();
                break;
            case R.id.saveOrUpdateUsersByList:
                saveOrUpdateUsersByList();
                break;
            case R.id.saveOrUpdateUsersByArray:
                saveOrUpdateUsersByArray();
                break;
            case R.id.deleteUsersByResult:
                deleteUsersByResult();
                break;
            case R.id.deleteUsersByQuery:
                deleteUsersByQuery();
                break;
            case R.id.updateUsersByResult:
                updateUsersByResult();
                break;
            case R.id.updateUsersByQuery:
                updateUsersByQuery();
                break;
        }
    }
}
