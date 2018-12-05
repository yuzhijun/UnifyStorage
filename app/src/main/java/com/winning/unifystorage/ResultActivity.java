package com.winning.unifystorage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.winning.unifystorage.adapter.ResultAdapter;
import com.winning.unifystorage.data.UserData;
import com.winning.unifystorage.model.User;
import com.winning.unifystorage.view.RecycleViewDivider;
import com.winning.unifystorage_core.model.DbResult;

import io.realm.RealmResults;

/**
 * 2018/12/5
 * Created by SharkChao
 * 827623353@qq.com
 * https://github.com/sharkchao
 */
public class ResultActivity extends AppCompatActivity {

    private RecyclerView mRvList;
    private ResultAdapter mAdapter;
    private ApiDataBase mApiDataBase;

    private static final String IS_FIND_TAG = "is_find";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        mRvList = findViewById(R.id.rvList);
        mAdapter = new ResultAdapter(R.layout.activity_result_item);
        mRvList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mRvList.addItemDecoration(new RecycleViewDivider(this,LinearLayoutManager.VERTICAL));
        mRvList.setAdapter(mAdapter);

        mApiDataBase = ApiServiceModule.getInstance().provideApiService(ApiDataBase.class);
        boolean isFind = getIntent().getBooleanExtra(IS_FIND_TAG, false);
        if (isFind){
            getDataByQuery();
        }else {
            getAllData();
        }
    }

    private void getDataByQuery() {
        RealmResults<User> users = UserData.getmResults();
        mAdapter.setNewData(users);
    }

    private void getAllData() {
        mApiDataBase.findAll().registerDbFindCallBack(new DbResult.DbFindCallBack<User>() {
            @Override
            public void onFirstFindResult(RealmResults<User> realmResults) {
               mAdapter.setNewData(realmResults);
            }

            @Override
            public void onChange(RealmResults<User> realmResults) {

            }
        });
    }

    public static void startResultActivity(Context context,boolean isFind){
        Intent intent = new Intent(context,ResultActivity.class);
        intent.putExtra(IS_FIND_TAG,isFind);
        context.startActivity(intent);
    }


}
