package com.winning.unifystorage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

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
                User user = new User();
                user.setAge("10");
                user.setName("sharkchao");
                user.setSex("男");
                mApiDataBase.saveUser(user);
            }
        });
        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mApiDataBase.findUser("yuzhijun", 10);
            }
        });
    }
}
