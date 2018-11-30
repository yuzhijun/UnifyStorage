package com.winning.unifystorage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.winning.unifystorage_core.model.DbResult;

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
        });
        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mApiDataBase.findUser("sharkchao", 10);
            }
        });
    }
}
