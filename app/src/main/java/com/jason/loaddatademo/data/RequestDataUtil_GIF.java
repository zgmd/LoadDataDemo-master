package com.jason.loaddatademo.data;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;


import com.jason.loaddatademo.entity.GifBean;
import com.jason.loaddatademo.util.LogUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by madong on 2016/9/22.
 */
public class RequestDataUtil_GIF {

    public static void requestForNet(final int requestId, final String url, final Handler mHandler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                Bundle bundle = new Bundle();
                message.what = requestId;
                List<GifBean.Data> listDaata = new ArrayList<GifBean.Data>();
                try {
                    Document doc = Jsoup.connect(url).get();
//                    LogUtils.i("请求到的数据：" + doc.toString());

                    //文字
                    Elements selectData1 = doc.select("span.showtxt");
//                    LogUtils.i("请求到的数据：" + selectData1.toString());
//
                    //图片
                    Elements selectData2 = doc.select("img.lazy");
//                    LogUtils.i("请求到的数据：" + selectData2.toString());
                    for (int i = 0; i < selectData2.size(); i++) {
                        String url = selectData2.get(i).select("img").attr("data-original");
                        String text = selectData1.get(i).text();
                        GifBean.Data data = new GifBean.Data(text, url);
                        listDaata.add(i, data);
                        LogUtils.i("请求到的数据：" + "url="+url.toString());
                        LogUtils.i("请求到的数据：" + "text="+text.toString());
                    }
                    GifBean gifBean = new GifBean(listDaata);
                    bundle.putSerializable("data",gifBean);
                } catch (IOException e) {
                    e.printStackTrace();
//                    bundle.putInt(Constant.RESPOND_STATE, Constant.EXCEPTION_CODE);
                    bundle.putString("erro",e.toString());
                }

                message.setData(bundle);
                mHandler.sendMessage(message);
            }
        }).start();
    }
}
