package com.winning.unifystorage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.winning.unifystorage.adapter.ResultAdapter;
import com.winning.unifystorage.view.RecycleViewDivider;

/**
 * 2018/12/5
 * Created by SharkChao
 * 827623353@qq.com
 * https://github.com/sharkchao
 */
public class ResultActivity extends AppCompatActivity {

    private RecyclerView mRvList;
    private ResultAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        mRvList = findViewById(R.id.rvList);
        mAdapter = new ResultAdapter(R.layout.activity_result_item);
        mRvList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mRvList.addItemDecoration(new RecycleViewDivider(this,LinearLayoutManager.VERTICAL));
        mRvList.setAdapter(mAdapter);
    }


}
