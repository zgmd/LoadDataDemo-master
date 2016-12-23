package com.jason.loaddatademo.server;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.jason.loaddatademo.GifActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;


/**
 * Created by WenYan on 2016/5/25.
 * <p/>
 * 自定义接收器
 * <p/>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */

public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "MyReceiver";
    private static String type = "";
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
//
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...
        }

        else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//            processCustomMessage(context, bundle);
        }

        else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");

            context.startActivity(new Intent(context, GifActivity.class));
            if(type.equals("1")){
                //打开自s定义的Activity,需要跳转的页面。(来电提醒)
//                Intent intent2 = new Intent(context, SimpleBackActivity.class);
//                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//                intent2.putExtra(SimpleBackActivity.KEY_IS_LOGIN_NEEDED, true);
//                intent2.putExtra(SimpleBackActivity.KEY_PAGE, SimplePage.WakeUpCall);
//                context.startActivity(intent2);
                return;
            }else if(type.equals("2")){
                //余额查询界面。
//                Intent intent2 = new Intent(context, SimpleBackActivity.class);
//                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//                intent2.putExtra(SimpleBackActivity.KEY_IS_LOGIN_NEEDED, true);
//                intent2.putExtra("extra", bundle);
//                intent2.putExtra(SimpleBackActivity.KEY_PAGE, SimplePage.MyHomeYuEQuery);
//                context.startActivity(intent2);
                return;
            }else if(type.equals("3")){
                //余量查询界面。
//                Intent intent2 = new Intent(context, SimpleBackActivity.class);
//                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//                intent2.putExtra(SimpleBackActivity.KEY_IS_LOGIN_NEEDED, true);
//                intent2.putExtra("extra", bundle);
//                intent2.putExtra(SimpleBackActivity.KEY_PAGE, SimplePage.MyHomeYuLiangQuery);
//                context.startActivity(intent2);
                return;
            }

            //打开自s定义的Activity,需要跳转的页面。
//            Intent intent1 = new Intent(context, SimpleBackActivity.class);
//            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//            intent1.putExtra(SimpleBackActivity.KEY_IS_LOGIN_NEEDED, true);
//            intent1.putExtra(SimpleBackActivity.KEY_PAGE, SimplePage.WakeUpCall);
//            context.startActivity(intent1);

//            Intent intent3 = new Intent(context, SimpleBackActivity.class);
//            intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//            intent3.putExtra(SimpleBackActivity.KEY_IS_LOGIN_NEEDED, true);
//            intent3.putExtra(SimpleBackActivity.KEY_PAGE, SimplePage.MyHomeYuEQuery);
//            intent3.

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                        type = json.optString(myKey);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

}
