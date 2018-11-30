package com.winning.unifystorage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.winning.unifystorage_core.model.DbResult;

import java.util.ArrayList;
import java.util.List;

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
                List<User> users = new ArrayList<>();
                User user = new User();
                user.setId(1);
                user.setAge("20");
                user.setName("sharkchao");
                user.setSex("男");
                users.add(user);
                mApiDataBase.saveUser(users).registerCallback(new DbResult.DbResultCallback() {
                    @Override
                    public void onSuccess(int count) {

                    }

                    @Override
                    public void onError(Throwable error) {

                    }
                });
            }
        });
        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mApiDataBase.findUser("sharkchao", 10);
            }
        });
    }
}
