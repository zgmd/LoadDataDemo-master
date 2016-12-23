package com.jason.loaddatademo;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.TypedValue;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jason.loaddatademo.server.IPagingService;
import com.jason.loaddatademo.util.LogUtils;

import java.util.List;

import rx.Observer;

/**
 * @author zjh
 * @date 2016/9/18
 */
public class BasePagingActivity_pictrue<T> extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    private static final int PAGE_SIZE = 20;
    private RecyclerView mRecyclerView;
    public BaseQuickAdapter mQuickAdapter;
    private IPagingService<List<T>> mPagingService;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int currentPage;
    private int lastPage;
    public Url_Type currentType;

    public enum Url_Type{
        shouye,baoxiao,diaobao,beiju,zhangzhishi
    }

    private void setSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {
        if (swipeRefreshLayout != null) {
            mSwipeRefreshLayout = swipeRefreshLayout;
            mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
            mSwipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics()));
            mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
            swipeRefreshLayout.setOnRefreshListener(this);
        } else {
            throw new NullPointerException("swipeRefreshLayout not null");
        }
    }

    private void setRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        if (mRecyclerView.getLayoutManager() == null) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//            mRecyclerView.setLayoutManager(new GridLayoutManager(this,4));
//            mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
//            mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        }
    }

    private void setQuickAdapter(BaseQuickAdapter quickAdapter) {
        if (quickAdapter != null) {
            mQuickAdapter = quickAdapter;
            mQuickAdapter.openLoadAnimation();

            mQuickAdapter.openLoadMore(10);
            mQuickAdapter.setOnLoadMoreListener(this);
//            if(mRecyclerView.getScrollState()==RecyclerView.SCROLL_STATE_IDLE && mRecyclerView.isComputingLayout()){
//
//            }


            mRecyclerView.setAdapter(quickAdapter);
        } else {
            throw new NullPointerException("swipeRefreshLayout not null");
        }
    }

    protected void startGetData(RecyclerView recyclerView,SwipeRefreshLayout swipeRefreshLayout,BaseQuickAdapter quickAdapter, IPagingService<List<T>> pagingService){
        mPagingService = pagingService;
        setRecyclerView(recyclerView);
        setSwipeRefreshLayout(swipeRefreshLayout);
        setQuickAdapter(quickAdapter);
        onLoadFirstData();
    }

    @Override
    public void onRefresh() {
        currentPage = 0;
        LogUtils.i("onRefresh()--sur");
        //TODO
        mPagingService.getData(currentPage, PAGE_SIZE, new Observer<List<T>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(BasePagingActivity_pictrue.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                mSwipeRefreshLayout.setRefreshing(false);
                currentPage = lastPage;
            }

            @Override
            public void onNext(List<T> list) {
                if (list == null) return;
//                mQuickAdapter.getData().clear();
                mQuickAdapter.addData(list);
                mQuickAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onLoadMoreRequested() {
        lastPage = currentPage;
        currentPage++;
        //TODO
        mPagingService.getData(currentPage, PAGE_SIZE, new Observer<List<T>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(BasePagingActivity_pictrue.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                currentPage = lastPage;
            }

            @Override
            public void onNext(List<T> list) {
                if ((list != null && list.isEmpty())) {
                    Toast.makeText(BasePagingActivity_pictrue.this,"没有更多数据了",Toast.LENGTH_SHORT).show();
                    mQuickAdapter.addData(list);
                    mQuickAdapter.loadComplete();
                } else {
                    mQuickAdapter.addData(list);
                }
                lastPage = currentPage;
            }
        });
    }

    public void onLoadFirstData(){
        lastPage = currentPage = 1;
        mSwipeRefreshLayout.setRefreshing(true);
        //TODO
        mPagingService.getData(currentPage, PAGE_SIZE, new Observer<List<T>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(BasePagingActivity_pictrue.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onNext(List<T> list) {
                if (list == null) return;
                mQuickAdapter.addData(list);
                mQuickAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
