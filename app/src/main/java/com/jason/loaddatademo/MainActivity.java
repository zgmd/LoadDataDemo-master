package com.jason.loaddatademo;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jason.loaddatademo.entity.WelfareEntity;
import com.jason.loaddatademo.server.WelfareServer;

import java.util.ArrayList;

import cn.jpush.android.api.JPushInterface;

public class MainActivity extends BasePagingActivity_pictrue<WelfareEntity> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        JPushInterface.setAlias(this, "a", null);
        getSupportActionBar().hide();
        SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.list);
        startGetData(mRecyclerView, mSwipeRefreshLayout, new BaseQuickAdapter<WelfareEntity>(R.layout.item_welfare,new ArrayList()){
            @Override
            protected void convert(final BaseViewHolder baseViewHolder, final WelfareEntity welfareEntity) {
                Glide.with(MainActivity.this)
                        .load(welfareEntity.getUrl())
                        .placeholder(R.mipmap.load_image_bg)
                        .into((ImageView) baseViewHolder.getView(R.id.iv));
                baseViewHolder.convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this,"点击了第"+baseViewHolder.getLayoutPosition()+"个",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this,DetilActivity.class);
                        intent.putExtra("url",welfareEntity.getUrl());
                        startActivity(intent);
                    }
                });
            }
        },new WelfareServer());

    }
}
