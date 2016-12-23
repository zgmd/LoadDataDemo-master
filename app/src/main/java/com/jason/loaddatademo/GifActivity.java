package com.jason.loaddatademo;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.github.mzule.fantasyslide.SideBar;
import com.github.mzule.fantasyslide.SimpleFantasyListener;
import com.github.mzule.fantasyslide.Transformer;
import com.jason.loaddatademo.api.GankIo;
import com.jason.loaddatademo.api.GifUrlManger;
import com.jason.loaddatademo.data.RequestDataUtil_GIF;
import com.jason.loaddatademo.entity.GifBean;
import com.jason.loaddatademo.entity.WelfareEntity;
import com.jason.loaddatademo.server.WelfareServer;
import com.jason.loaddatademo.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class GifActivity extends BasePagingActivity<WelfareEntity> {
    private DrawerLayout drawerLayout;
    private String url = GifUrlManger.URL_GIF;//当前的url
    private int page = 1;//页码
    GifBean gifBean;//数据
    private boolean isMore;

    private String[] tag = {
            "http://www.gaoxiaogif.cn/giftags/%E5%91%86%E8%90%8Cgif/",//呆萌
            "http://www.gaoxiaogif.cn/giftags/%E7%88%86%E7%AC%91gif/",//爆笑
            "http://www.gaoxiaogif.cn/giftags/%E4%BA%8C%E8%B4%A7gif/",//二货
            "http://www.gaoxiaogif.cn/giftags/%E5%96%B5%E6%98%9F%E4%BA%BAgif/",//喵星人
            "http://www.gaoxiaogif.cn/giftags/%E6%B1%AA%E6%98%9F%E4%BA%BAgif/",//汪星人
            "http://www.gaoxiaogif.cn/giftags/%E5%A4%B1%E8%AF%AFgif/",//失误
            "http://www.gaoxiaogif.cn/giftags/%E5%8A%A8%E7%89%A9gif/",//动物
            "http://www.gaoxiaogif.cn/giftags/%E9%80%97%E9%80%BCgif/",//逗比
            "http://www.gaoxiaogif.cn/giftags/%E7%86%8A%E5%AD%A9%E5%AD%90gif/",//熊孩子
            "http://www.gaoxiaogif.cn/giftags/%E9%82%AA%E6%81%B6gif/",//邪恶
            "http://www.gaoxiaogif.cn/giftags/%E5%9D%91%E7%88%B9gif/",//坑爹
            "http://www.gaoxiaogif.cn/giftags/%E7%89%9BBgif/",//牛逼
            "http://www.gaoxiaogif.cn/giftags/%E9%80%86%E5%A4%A9gif/",//逆天
    };

    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif);
        initIndicator();
        initContentView();


        //默认加载首页数据
        initData(0, GifUrlManger.URL_GIF);
    }

    /**
     * 中间区域显示
     */
    private void initContentView() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_bar);
        startGetData(mRecyclerView, mSwipeRefreshLayout, new BaseQuickAdapter<GifBean.Data>(R.layout.item_gif, new ArrayList()) {
            @Override
            protected void convert(final BaseViewHolder baseViewHolder, final GifBean.Data data) {
                LogUtils.i("convert");
                TextView tv = (TextView) baseViewHolder.getView(R.id.tv_introduce);
                tv.setText(data.getText());
                Glide.with(GifActivity.this)
                        .load(data.getUrl()).asGif()
//                        .placeholder(R.drawable.loading)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into((ImageView) baseViewHolder.getView(R.id.iv_gif));
//                mQuickAdapter.notifyDataSetChanged();
            }
        }, new WelfareServer());
    }

    /**
     * 设置策划菜单
     */
    private void initIndicator() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final DrawerArrowDrawable indicator = new DrawerArrowDrawable(this);
        indicator.setColor(Color.WHITE);
        getSupportActionBar().setHomeAsUpIndicator(indicator);
        setTransformer();
        // setListener();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (((ViewGroup) drawerView).getChildAt(1).getId() == R.id.leftSideBar) {
                    indicator.setProgress(slideOffset);
                }
            }
        });
    }


    private void setListener() {
        final TextView tipView = (TextView) findViewById(R.id.tipView);
        SideBar leftSideBar = (SideBar) findViewById(R.id.leftSideBar);
        leftSideBar.setFantasyListener(new SimpleFantasyListener() {
            @Override
            public boolean onHover(@Nullable View view) {
                tipView.setVisibility(View.VISIBLE);
                if (view instanceof TextView) {
                    tipView.setText(((TextView) view).getText());
                } else if (view != null && view.getId() == R.id.userInfo) {
                    tipView.setText("个人中心");
                } else {
                    tipView.setText(null);
                }
                return false;
            }

            @Override
            public boolean onSelect(View view) {
                tipView.setVisibility(View.INVISIBLE);
                return false;
            }

            @Override
            public void onCancel() {
                tipView.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void setTransformer() {
        final float spacing = getResources().getDimensionPixelSize(R.dimen.spacing);
        SideBar rightSideBar = (SideBar) findViewById(R.id.rightSideBar);
        rightSideBar.setTransformer(new Transformer() {
            private View lastHoverView;

            @Override
            public void apply(ViewGroup sideBar, View itemView, float touchY, float slideOffset, boolean isLeft) {
                boolean hovered = itemView.isPressed();
                if (hovered && lastHoverView != itemView) {
                    animateIn(itemView);
                    animateOut(lastHoverView);
                    lastHoverView = itemView;
                }
            }

            private void animateOut(View view) {
                if (view == null) {
                    return;
                }
                ObjectAnimator translationX = ObjectAnimator.ofFloat(view, "translationX", -spacing, 0);
                translationX.setDuration(200);
                translationX.start();
            }

            private void animateIn(View view) {
                ObjectAnimator translationX = ObjectAnimator.ofFloat(view, "translationX", 0, -spacing);
                translationX.setDuration(200);
                translationX.start();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        }
        return true;
    }

    public void onClick(View view) {
        if (view instanceof TextView) {
            String title = ((TextView) view).getText().toString();
//            Toast.makeText(this, title, Toast.LENGTH_SHORT).show();

            switch (title) {
                case "首页":
                    isMore = false;
                    url = GifUrlManger.URL_GIF;
                    initData(0, url);
                    break;
                case "爆笑":
                    isMore = false;
                    url = GifUrlManger.URL_GIF + "/baoxiaogif/";
                    initData(0, url);
                    break;
                case "碉堡":
                    isMore = false;
                    url = GifUrlManger.URL_GIF + "/diaobaogif/";
                    initData(0, url);
                    break;
                case "悲剧":
                    isMore = false;
                    url = GifUrlManger.URL_GIF + "/beijugif/";
                    initData(0, url);
                    break;
                case "涨知识":
                    isMore = false;
                    url = GifUrlManger.URL_GIF + "/zhangzhishigif/";
                    initData(0, url);
                    break;
                case "关于":
                    startActivity(new Intent(GifActivity.this,AboutActivity.class));
                    break;
                case "分享":
                    break;
                case "妹子":
                    startActivity(new Intent(GifActivity.this,MainActivity.class));
                    break;
                case "热门标签":
                    showWindow();
                    break;

            }
        } else if (view.getId() == R.id.userInfo) {
//            startActivity(UniversalActivity.newIntent(this, "个人中心"));
            Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 显示热门标签
     */
    private void showWindow() {
        View popupView = getLayoutInflater().inflate(R.layout.layout_popupwindow, null);
        final PopupWindow mPopupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setAnimationStyle(R.style.popwin_anim_style);
        //设置一下两项，才可以实现点击外部，popupWindow消失的功能
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setOutsideTouchable(true);

        mPopupWindow.showAtLocation(
                findViewById(R.id.rl_content),
                Gravity.BOTTOM,
                0, 0
        );

         int currentTag;
        //给每个text标签设置点击事件
        ViewGroup viewGroup = (ViewGroup) popupView.findViewById(R.id.fl_layout);
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            currentTag = i;
            final int finalCurrentTag = currentTag;
            viewGroup.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    currentTag = i;
                    url = tag[finalCurrentTag];
                    mPopupWindow.dismiss();
                    initData(0,url);
                }
            });
        }

    }

    /**
     * 处理数据
     *
     * @param i
     * @param url
     */
    private void initData(int i, String url) {
//        mProgressBar.setVisibility(View.VISIBLE);
        LogUtils.e("请求的地址：" + url);
        mSwipeRefreshLayout.setRefreshing(true);
        RequestDataUtil_GIF.requestForNet(0, url, new Handler() {
            @Override
            public void handleMessage(Message msg) {
//                mSwipeRefreshLayout.setRefreshing(false);
                mSwipeRefreshLayout.setRefreshing(false);
                if (msg.what == 0) {
                    if (msg.getData() != null) {
                        Bundle bundle = msg.getData();
                        gifBean = (GifBean) bundle.getSerializable("data");
                        if (gifBean != null) {
                            //TODO  更换url数据不清空
                            if (isMore && page > 1) {
                                mQuickAdapter.addData(gifBean.getLsitText());
                            } else {
                                mQuickAdapter.setNewData(gifBean.getLsitText());
                            }
                            mQuickAdapter.notifyDataSetChanged();
                            LogUtils.i("传来的数据：" + gifBean.toString());

                        }

                    }


                }
            }
        });
    }

    //刷新回调
    @Override
    public void onRefresh() {
        if (gifBean != null) {
            gifBean.getLsitText().clear();
        }
        isMore = false;
        initData(0, url);
        LogUtils.i("onRefresh----------------");
//        mSwipeRefreshLayout.setRefreshing(false);
    }


    /**
     * 加载更多
     */
    @Override
    public void onLoadMoreRequested() {
        LogUtils.e("onLoadMoreRequested");
        page++;
        String currentUrl = "";
        if (url.equals("http://www.gaoxiaogif.cn/")) {
            currentUrl = url + "gif/" + page + "/";
        } else {
            currentUrl = url + page + "/";
        }
        LogUtils.e(currentUrl);
        isMore = true;
        initData(0, currentUrl);
//        mQuickAdapter.loadComplete();
    }
}

