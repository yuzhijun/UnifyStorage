package com.winning.unifystorage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.winning.unifystorage.data.UserData;
import com.winning.unifystorage.model.Cat;
import com.winning.unifystorage.model.Dog;
import com.winning.unifystorage.model.Fake;
import com.winning.unifystorage.model.User;
import com.winning.unifystorage_core.model.DbResult;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button saveUserByObject;
    private Button saveUsersByList;
    private Button saveUsersByArray;
    private Button saveUsersByJsonObject;
    private Button saveUsersByJsonArray;

    private Button saveOrUpdateObject;
    private Button saveOrUpdateUsersByList;
    private Button saveOrUpdateUsersByArray;
    private Button saveOrUpdateUsersByJsonObject;
    private Button saveOrUpdateUsersByJsonArray;
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
    private Button btnSaveJson;
    private Button btnGetJson;
    private Button btnSaveJsonArray;
    private Button btnGetJsonArray;
    private Button btnMockServer;
    private Button btnResult;

    private ApiDataBase mApiDataBase;
    private ApiDataBase mRetrofitApiDataBase;

    private boolean isFind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        saveUserByObject = findViewById(R.id.saveUserByObject);
        saveUsersByList = findViewById(R.id.saveUsersByList);
        saveUsersByArray = findViewById(R.id.saveUsersByArray);
        saveUsersByJsonObject = findViewById(R.id.saveUserByJsonObject);
        saveUsersByJsonArray = findViewById(R.id.saveUserByJsonArray);


        saveOrUpdateObject = findViewById(R.id.saveOrUpdateObject);
        saveOrUpdateUsersByList = findViewById(R.id.saveOrUpdateUsersByList);
        saveOrUpdateUsersByArray = findViewById(R.id.saveOrUpdateUsersByArray);
        saveOrUpdateUsersByJsonObject = findViewById(R.id.saveOrUpdateUsersByJsonObject);
        saveOrUpdateUsersByJsonArray = findViewById(R.id.saveOrUpdateUsersByJsonArray);

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
        btnSaveJson = findViewById(R.id.btnSaveJson);
        btnGetJson = findViewById(R.id.btnGetJson);
        btnSaveJsonArray = findViewById(R.id.btnSaveJsonArray);
        btnGetJsonArray = findViewById(R.id.btnGetJsonArray);
        btnResult = findViewById(R.id.btnResult);
        btnMockServer = findViewById(R.id.btnMockServer);

        saveUserByObject.setOnClickListener(this);
        saveUsersByArray.setOnClickListener(this);
        saveUsersByList.setOnClickListener(this);
        saveUsersByJsonObject.setOnClickListener(this);
        saveUsersByJsonArray.setOnClickListener(this);

        saveOrUpdateObject.setOnClickListener(this);
        saveOrUpdateUsersByArray.setOnClickListener(this);
        saveOrUpdateUsersByList.setOnClickListener(this);
        saveOrUpdateUsersByJsonObject.setOnClickListener(this);
        saveOrUpdateUsersByJsonArray.setOnClickListener(this);


        deleteUsersByResult.setOnClickListener(this);
        deleteUsersByQuery.setOnClickListener(this);
        updateUsersByResult.setOnClickListener(this);
        updateUsersByQuery.setOnClickListener(this);
        btnSaveJson.setOnClickListener(this);
        btnGetJson.setOnClickListener(this);
        btnSaveJsonArray.setOnClickListener(this);
        btnGetJsonArray.setOnClickListener(this);
        btnMockServer.setOnClickListener(this);

        btnFindAll.setOnClickListener(this);
        btnFindUser.setOnClickListener(this);
        btnFindUserByIn.setOnClickListener(this);
        btnFindUserByContains.setOnClickListener(this);
        btnFindUserByLike.setOnClickListener(this);
        btnFindUserByNotNull.setOnClickListener(this);
        btnResult.setOnClickListener(this);


        mApiDataBase = ApiServiceModule.getInstance().provideApiService(ApiDataBase.class);
        mRetrofitApiDataBase  = ApiServiceModule.getInstance().provideRetrofitApiService(this, ApiDataBase.class);
    }

    /**
     * 存储对象，支持一对多
     * 注意：如果数据库中有此条数据，会回调onError
     */
    private void saveUserByObject(){
        User user = new User();
        user.setAge(20);
        user.setName("sharkchao");
        user.setSex("男");

        Dog dog = new Dog();
        dog.setName("小黄");
        dog.setAge(2);
        user.setDog(dog);

        RealmList<Cat> cats = new RealmList<>();
        Cat catA = new Cat();
        catA.setName("白猫");
        catA.setAge(1);

        Cat catB = new Cat();
        catB.setName("黑猫");
        catB.setAge(2);

        cats.add(catA);
        cats.add(catB);

        user.setCats(cats);

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

    /**
     * 支持直接存储json字符串
     * 注意：如果数据库中有此条数据，会回调onError
     */
    private void saveUsersByJsonObject(){
        String json = "{\n" +
                "  \"id\" : 50,\n" +
                "  \"name\" : \"yuzhijun\",\n" +
                "  \"age\" : \"10\",\n" +
                "  \"sex\" : \"男\"\n" +
                "}";
        mApiDataBase.saveUsersByJsonObject(json).registerCallback(new DbResult.DbResultCallback() {

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

    /**
     * 支持存储或更新对象通过json字符串
     * 注意：数据有此条数据则会更新，数据库没有则会插入
     */
    private void saveOrUpdateUsersByJsonObject(){
        String json = "{\n" +
                "  \"id\" : 100,\n" +
                "  \"name\" : \"sharkchao\",\n" +
                "  \"age\" : \"10\",\n" +
                "  \"sex\" : \"男\"\n" +
                "}";

        mApiDataBase.saveOrUpdateUsersByJsonObject(json).registerCallback(new DbResult.DbResultCallback() {

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

    /**
     * 支持存储List集合
     * 注意： List<T>list  T为realmObject类型
     * 注意：如果数据库中有此条数据，会回调onError
     */
    private void saveUsersByList(){
        List<User>list = new ArrayList<>();

        User user1 = new User();
        user1.setAge(20);
        user1.setName("小明");
        user1.setSex("男");

        User user2 = new User();
        user2.setAge(15);
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

    /**
     * 支持存储数组格式
     * 注意：如果数据库中有此条数据，会回调onError
     */
    private void saveUsersByArray(){
        User[]users = new User[2];

        User user1 = new User();
        user1.setAge(20);
        user1.setName("小绿");
        user1.setSex("男");

        User user2 = new User();
        user2.setAge(15);
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

    /**
     * 支持存储json集合字符串
     * 注意：如果数据库中有此条数据，会回调onError
     */
    private void saveUsersByJsonArray(){
    String json = "[{\n" +
            "\t\"id\": \"101\",\n" +
            "\t\"name\": \"sharkchao\",\n" +
            "\t\"age\": \"20\",\n" +
            "\t\"sex\": \"男\"\n" +
            "}, {\n" +
            "\t\"id\": \"102\",\n" +
            "\t\"name\": \"yuzhijun\",\n" +
            "\t\"age\": \"20\",\n" +
            "\t\"sex\": \"男\"\n" +
            "}]";

        mApiDataBase.saveUsersByJsonArray(json).registerCallback(new DbResult.DbResultCallback() {

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

    /**
     * 支持存入或更新集合通过json字符串
     * 注意：数据有此条数据则会更新，数据库没有则会插入
     */
    private void saveOrUpdateUsersByJsonArray(){
        String json = "[{\n" +
                "\t\"id\": \"103\",\n" +
                "\t\"name\": \"sharkchao\",\n" +
                "\t\"age\": \"20\",\n" +
                "\t\"sex\": \"男\"\n" +
                "}, {\n" +
                "\t\"id\": \"104\",\n" +
                "\t\"name\": \"yuzhijun\",\n" +
                "\t\"age\": \"20\",\n" +
                "\t\"sex\": \"男\"\n" +
                "}]";
        mApiDataBase.saveOrUpdateUsersByJsonArray(json).registerCallback(new DbResult.DbResultCallback() {

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

    /**
     * 测试一些非法传参，都会回调到onError()
     */
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

    /**
     * 存储或更新对象
     * 注意：数据有此条数据则会更新，数据库没有则会插入
     */
    private void saveOrUpdateObject(){
        User user = new User();
        user.setId("d07ea3e1-2f75-4363-91c3-697570c882e6");
        user.setAge(20);
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

    /**
     * 存储或更新对象集合
     * 注意：数据有此条数据则会更新，数据库没有则会插入
     */
    private void saveOrUpdateUsersByList(){
        List<User>list = new ArrayList<>();

        User user1 = new User();
        user1.setAge(22);
        user1.setName("小明");
        user1.setSex("男");
        user1.setId("b1d92a7f-6201-4d5f-be37-4fb33283ce44");

        User user2 = new User();
        user2.setAge(18);
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
    /**
     * 存储或更新对象数组
     * 注意：数据有此条数据则会更新，数据库没有则会插入
     */
    private void saveOrUpdateUsersByArray(){
        User[]users = new User[2];

        User user1 = new User();
        user1.setAge(22);
        user1.setName("小绿");
        user1.setSex("男");
        user1.setId("56ea25d8-ad50-4422-aa47-63c6e4901c3b");


        User user2 = new User();
        user2.setAge(18);
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

    /**
     * 删除分为两种，我们推荐用第二种
     * 这是第一种：先查询出来再删除
     */
    private void deleteUsersByResult(){
        List<String> users = new ArrayList<>();
        users.add("yuzhijun");
        users.add("sharkchao");

        mApiDataBase.findUserByIn(users).registerDbFindCallBack(new DbResult.DbFindCallBack<User>() {
            @Override
            public void onFirstFindResult(RealmResults<User> realmResults) {

                if (realmResults.size() > 0){
                    realmResults.getRealm().executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realmResults.deleteAllFromRealm();
                        }
                    });

                }else {
                    Toast.makeText(MainActivity.this, "realmResults为空", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onChange(RealmResults<User> realmResults) {

            }
        });
    }
    /**
     * 删除分为两种，我们推荐用第二种
     * 这是第二种：直接删除
     */
    private void deleteUsersByQuery(){
        mApiDataBase.deleteUsersByQuery("小白").registerCallback(new DbResult.DbResultCallback() {

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

    /**
     * 更新分为两种
     * 这是第一种 ：直接更新数据库对象，如果数据库没有此对象，会回调onError()
     */
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
    /**
     * 更新分为两种
     * 这是第二种 ：先查询出来再更新
     */
    private void updateUsersByResult(){
        List<String> users = new ArrayList<>();
        users.add("yuzhijun");
        users.add("sharkchao");

        mApiDataBase.findUserByIn(users).registerDbFindCallBack(new DbResult.DbFindCallBack<User>() {
            @Override
            public void onFirstFindResult(RealmResults<User> realmResults) {

                if (realmResults.size() > 0){
                    realmResults.getRealm().executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realmResults.get(0).setName("刘超");
                            Toast.makeText(MainActivity.this, "更新成功!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Toast.makeText(MainActivity.this, "realmResults为空", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onChange(RealmResults<User> realmResults) {

            }
        });
    }

    /**
     * 支持直接将对象存储到SharedPreferences文件中
     * 支持多进程访问
     */
    private void saveJson(){
        User user = new User();
        user.setName("yuzhijun");
        user.setAge(18);
        user.setSex("男");
        boolean result =  mApiDataBase.saveJson(user);
    }
    /**
     * 支持从SharedPreferences文件中获取对象
     * 支持多进程访问
     */
    private void getJson(){
        DbResult<User> user = mApiDataBase.getJson();
        if (null != user && null != user.getResult()){
            System.out.println(user.getResult().getName());
        }

    }
    /**
     * 支持直接将对象集合存储到SharedPreferences文件中
     */
    private void saveJsonArray(){
        List<User> users = new ArrayList<>();
        User user = new User();
        user.setName("yuzhijun");
        user.setAge(18);
        user.setSex("男");

        users.add(user);

        boolean result =  mApiDataBase.saveJsonArray(users);
    }
    /**
     * 支持从SharedPreferences文件中获取对象集合
     */
    private void getJsonArray(){
        DbResult<List<User>> users = mApiDataBase.getJsonArray();
        if (null != users && null != users.getResult()){
            System.out.println(users.getResult().get(0).getName());
        }

    }
    /**
     *  支持本地模拟网络访问数据
     */
    private void mockServer(){
        //1
//        Call<User> call =  mRetrofitApiDataBase.getUser();
//        call.enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                User user = response.body();
//                System.out.println(user.getName());
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//
//            }
//        });

        //2
        Call<List<User>> call = mRetrofitApiDataBase.getUserList();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                List<User> users = response.body();
                System.out.println(users.size() + "");
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });
    }

    /**
     * 查询所有
     */
    private void findAll(){
        isFind = true;
        mApiDataBase.findAll().registerDbFindCallBack(new DbResult.DbFindCallBack<User>() {
            @Override
            public void onFirstFindResult(RealmResults<User> realmResults) {
                UserData.setmResults(realmResults);
                Toast.makeText(MainActivity.this, "成功!"+ realmResults.size(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChange(RealmResults<User> realmResults) {

            }
        });
    }

    /**
     * 支持条件查询
     */
    private void findUser(){
        isFind = true;
        mApiDataBase.findUser("sharkchao", 20, "男")
                .registerDbFindCallBack(new DbResult.DbFindCallBack<User>() {
                    @Override
                    public void onFirstFindResult(RealmResults<User> realmResults) {
                        UserData.setmResults(realmResults);
                        Toast.makeText(MainActivity.this, "成功!"+ realmResults.size(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onChange(RealmResults<User> realmResults) {

                    }
                });
    }

    /**
     * 支持通过in从集合中查询
     */
    private void findUserByIn(){
        isFind = true;
        List<String> users = new ArrayList<>();
        users.add("yuzhijun");
        users.add("sharkchao");

        mApiDataBase.findUserByIn(users).registerDbFindCallBack(new DbResult.DbFindCallBack<User>() {
            @Override
            public void onFirstFindResult(RealmResults<User> realmResults) {
                UserData.setmResults(realmResults);
                Toast.makeText(MainActivity.this, "成功!"+ realmResults.size(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChange(RealmResults<User> realmResults) {
                Toast.makeText(MainActivity.this, "变化!"+ realmResults.size(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 支持contains包含查询
     */
    private void findUserByContains(){
        isFind = true;
        mApiDataBase.findUserByContains("shark").registerDbFindCallBack(new DbResult.DbFindCallBack<User>() {
            @Override
            public void onFirstFindResult(RealmResults<User> realmResults) {
                UserData.setmResults(realmResults);
                Toast.makeText(MainActivity.this, "成功!"+ realmResults.size(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChange(RealmResults<User> realmResults) {

            }
        });
    }

    /**
     * 支持like模糊查询
     */
    private void findUserByLike(){
        isFind = true;
        mApiDataBase.findUserByLike("sha*", 10).registerDbFindCallBack(new DbResult.DbFindCallBack<User>() {
            @Override
            public void onFirstFindResult(RealmResults<User> realmResults) {
                UserData.setmResults(realmResults);
                Toast.makeText(MainActivity.this, "成功!"+ realmResults.size(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChange(RealmResults<User> realmResults) {

            }
        });
    }

    /**
     * 支持NotNull非空查询
     */
    private void findUserByNotNull(){
        isFind = true;
        mApiDataBase.findUserByNotNull("name").registerDbFindCallBack(new DbResult.DbFindCallBack<User>() {
            @Override
            public void onFirstFindResult(RealmResults<User> realmResults) {
                UserData.setmResults(realmResults);
                Toast.makeText(MainActivity.this, "成功!"+ realmResults.size(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChange(RealmResults<User> realmResults) {

            }
        });
    }
    private void startResultActivity(){
        ResultActivity.startResultActivity(this,isFind);
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
            case R.id.saveUserByJsonObject:
                saveUsersByJsonObject();
                break;
            case R.id.saveUserByJsonArray:
                saveUsersByJsonArray();
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
            case R.id.saveOrUpdateUsersByJsonObject:
                saveOrUpdateUsersByJsonObject();
                break;
            case R.id.saveOrUpdateUsersByJsonArray:
                saveOrUpdateUsersByJsonArray();
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
            case R.id.btnSaveJson:
                saveJson();
                break;
            case R.id.btnGetJson:
                getJson();
                break;
            case R.id.btnSaveJsonArray:
                saveJsonArray();
                break;
            case R.id.btnGetJsonArray:
                getJsonArray();
                break;
            case R.id.btnMockServer:
                mockServer();
                break;
            case R.id.btnFindAll:
                findAll();
                break;
            case R.id.btnFindUser:
                findUser();
                break;
            case R.id.btnFindUserByIn:
                findUserByIn();
                break;
            case R.id.btnFindUserByContains:
                findUserByContains();
                break;
            case R.id.btnFindUserByLike:
                findUserByLike();
                break;
            case R.id.btnFindUserByNotNull:
                findUserByNotNull();
                break;
            case R.id.btnResult:
                startResultActivity();
                break;
        }
    }
}
