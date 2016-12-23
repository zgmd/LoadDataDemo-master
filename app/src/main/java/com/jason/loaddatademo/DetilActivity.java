package com.jason.loaddatademo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jason.loaddatademo.widget.MyImageView;
import com.jason.loaddatademo.widget.ZoomImageView;

public class DetilActivity extends AppCompatActivity {

    ZoomImageView ziv_image;
    MyImageView my_image;
    String url ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detil);
        ziv_image = (ZoomImageView) findViewById(R.id.ziv_image);
        my_image = (MyImageView) findViewById(R.id.my_image);
        url = getIntent().getStringExtra("url");
        initPicture();
    }

    private void initPicture() {
        Glide.with(DetilActivity.this)
                .load(url)
                .placeholder(R.mipmap.load_image_bg)
                .into(ziv_image);
    }
}
