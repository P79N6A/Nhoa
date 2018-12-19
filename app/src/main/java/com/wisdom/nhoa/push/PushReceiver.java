package com.wisdom.nhoa.push;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.baidu.android.pushservice.PushMessageReceiver;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.wisdom.nhoa.AppApplication;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.homepage.activity.HomePageActivity;
import com.wisdom.nhoa.homepage.model.MsgListModel;
import com.wisdom.nhoa.push.activity.PushDetailActivity;
import com.wisdom.nhoa.util.ServiceHelper;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.dbHelper.DbHelperCustom;
import com.wisdom.nhoa.util.http_util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

import static com.loopj.android.http.AsyncHttpClient.log;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.push
 * @class describe：推送接收信息的类
 * @time 2018/1/31 9:39
 * @change
 */

public class PushReceiver extends PushMessageReceiver {
    private onReceiveServerMsgListener MsgListener;

    public void setOnReceiveServerMsgListener(PushReceiver.onReceiveServerMsgListener MsgListener) {
        this.MsgListener = MsgListener;
    }

    MsgCountEvent msgCountEvent = new MsgCountEvent("0");
    /**
     * TAG to Log
     */
    public static final String TAG = PushReceiver.class
            .getSimpleName();

    /**
     * 调用PushManager.startWork后，sdk将对push
     * server发起绑定请求，这个过程是异步的。绑定请求的结果通过onBind返回。 如果您需要用单播推送，需要把这里获取的channel
     * id和user id上传到应用server中，再调用server接口用channel id和user id给单个手机或者用户推送。
     *
     * @param context   BroadcastReceiver的执行Context
     * @param errorCode 绑定接口返回值，0 - 成功
     * @param appid     应用id。errorCode非0时为null
     * @param userId    应用user id。errorCode非0时为null
     * @param channelId 应用channel id。errorCode非0时为null
     * @param requestId 向服务端发起的请求id。在追查问题时有用；
     * @return none
     */
    @Override
    public void onBind(Context context, int errorCode, String appid,
                       String userId, String channelId, String requestId) {
        Log.i(TAG, "###############################onBind: ###############################");
        //        Log.i(TAG, "onBind: 调起推送接口");
//        /**
//         * 提交数据到接口
//         */
//        HttpParams paramas = new HttpParams();
//        paramas.put("userId", ConstantString.USERID);//用户id
//        paramas.put("channelId", channelId);
//        paramas.put("systemType", "android");
//        HttpUtil.httpGet(ConstantUrl.PUSH, paramas, new StringCallback() {
//            @Override
//            public void onSuccess(String s, Call call, Response response) {
//                Log.i(TAG, "onSuccess: " + s);
//            }
//
//            @Override
//            public void onError(Call call, Response response, Exception e) {
//                super.onError(call, response, e);
//                Log.i(TAG, "error: ");
//            }
//        });
        //移动设备更新百度云推送链路信息
        HttpParams paramas1 = new HttpParams();
        Log.i(TAG, "channelId******: " + channelId);
        Log.i(TAG, "systemType******: " + "android");
        Log.i(TAG, "appkey******: " + ConstantString.APP_KEY);
        Log.i(TAG, "access_token******: " + SharedPreferenceUtil.getUserInfo(context).getAccess_token());
        paramas1.put("channelId", channelId);
        paramas1.put("systemType", "android");
        paramas1.put("appkey", ConstantString.APP_KEY);
        paramas1.put("access_token", SharedPreferenceUtil.getUserInfo(context).getAccess_token());
        HttpUtil.httpGet(ConstantUrl.DATA_CHANGE_URL, paramas1, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                Log.i(TAG, "error1: ");
            }

            @Override
            public void onSuccess(String s, Call call, Response response) {
                Log.i(TAG, "onSuccess1: " + s);
            }
        });

        Log.i(TAG, "onBind errorCode: " + errorCode);
        Log.i(TAG, "onBind appid: " + appid);
        Log.i(TAG, "onBind userId: " + userId);
        Log.i(TAG, "onBind channelId: " + channelId);
        Log.i(TAG, "onBind requestId: " + requestId);
//        List<String> tags = new ArrayList<>();
        Log.i(TAG, "onBind: UUID:" + SharedPreferenceUtil.getUserInfo(AppApplication.getInstance()).getUid());
//        if (SharedPreferenceUtil.getUserInfo(AppApplication.getInstance()) != null) {
//        Log.i(TAG, "onBind: TAG:" + tags.get(0));
//        tags.add(SharedPreferenceUtil.getUserInfo(AppApplication.getInstance()).getUid());
//        PushManager.setTags(AppApplication.getInstance(), tags);
//        Log.i(TAG, "onBind: TAG:" + tags.get(0));
//        }
        Log.i(TAG, "###############################onBind: ###############################");
    }

    /**
     * 接收透传消息的函数。
     *
     * @param context             上下文
     * @param message             推送的消息
     * @param customContentString 自定义内容,为空或者json字符串
     */
    @Override
    public void onMessage(Context context, String message,
                          String customContentString) {
        String messageString = "透传消息 onMessage=\"" + message + "\" customContentString=" + customContentString;
        Log.d(TAG, " 透传消息:" + messageString);
    }

    /**
     * 接收通知到达的函数。
     *
     * @param context             上下文
     * @param title               推送的通知的标题
     * @param description         推送的通知的描述
     * @param customContentString 自定义内容，为空或者json字符串
     */

    @Override
    public void onNotificationArrived(Context context, String title,
                                      String description, String customContentString) {
        Log.i(TAG, "onNotificationArrived title: " + title);
        Log.i(TAG, "onNotificationArrived description: " + description);
        Log.i(TAG, "onNotificationArrived customContentString: " + customContentString);
        Log.i(TAG, "sendNotification: " + "001");
//        if (MsgListener!=null){
//        MsgListener.onReceiveServerMsg(title, description,customContentString);}
//        Log.i(TAG, "sendNotification: " + "002");
//        Resources resource=context.getResources();
//        String pkgName=context.getPackageName();
//        ToastUtil.showToast(title);
//        CustomPushNotificationBuilder cBuilder = new CustomPushNotificationBuilder(
//                context.getResources().getIdentifier(
//                        "notification_custom_builder", "layout",pkgName ),
//                resource.getIdentifier("notification_icon", "id", pkgName),
//                resource.getIdentifier("notification_title", "id", pkgName),
//                resource.getIdentifier("notification_text", "id", pkgName));
//        cBuilder.setNotificationFlags(Notification.FLAG_AUTO_CANCEL);
//        cBuilder.setNotificationDefaults(Notification.DEFAULT_VIBRATE);
//        cBuilder.setStatusbarIcon(context.getApplicationInfo().icon);
//        cBuilder.setLayoutDrawable(resource.getIdentifier(
//                "simple_notification_icon", "drawable", pkgName));
//        cBuilder.setNotificationSound(Uri.withAppendedPath(
//                MediaStore.Audio.Media.INTERNAL_CONTENT_URI, "6").toString());
//        cBuilder.setNotificationText(description);
//        cBuilder.setNotificationTitle(title);
//        sendNotification( title,  description,context,customContentString);
//        insertSQlite(title,description,customContentString);

        //将每条消息都存入数据库
        DbHelperCustom.insertMessage(title, description, customContentString, AppApplication.getInstance().GetSqliteDataBase(), context);
        //发送
        Intent intent = new Intent();
        intent.setAction(HomePageActivity.UPDATAMSGCOUNTS);
        context.sendBroadcast(intent);
    }

    private void sendNotification(String title, String content, Context context) {
        NotificationManager notifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                //设置通知标题
                .setContentTitle(title)
                //设置通知内容
                .setContentText(content);
        notifyManager.notify(1, builder.build());
        Log.i(TAG, "sendNotification: " + "3");
    }

    /**
     * 插入通知来的数据库每条信息
     *
     * @param title               推送的通知的标题
     * @param content             推送的通知的描述
     * @param customContentString 自定义内容，为空或者json字符串
     */
    private void insertSQlite(String title, String content, String customContentString) {
        MsgListModel msgListModel = new MsgListModel();
        try {
            if (customContentString != null) {
                JSONObject jsonObject = new JSONObject(customContentString);
                if (jsonObject != null) {
                    msgListModel.setMessTypeCode(jsonObject.getString("messTypeCode"));
                    msgListModel.setDataId(jsonObject.getString("dataId"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        msgListModel.setMsgtitle(title);
        msgListModel.setMsgdiscription(content);
        Log.v("insertMsgModel", msgListModel.toString());
        AppApplication.getDaosession().insertOrReplace(msgListModel);
    }


    /**
     * 接收通知点击的函数。
     *
     * @param context             上下文
     * @param title               推送的通知的标题
     * @param description         推送的通知的描述
     * @param customContentString 自定义内容，为空或者json字符串
     */
    @Override
    public void onNotificationClicked(Context context, String title, String description, String customContentString) {
        String messtypeCode = "";
        String dataId = "";
        try {
            if (customContentString != null) {
                JSONObject jsonObject = new JSONObject(customContentString);
                if (jsonObject != null) {
                    messtypeCode = jsonObject.getString("messTypeCode");
                    dataId = jsonObject.getString("dataId");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(context.getApplicationContext(), PushDetailActivity.class);
        intent.putExtra("messTypeCode", messtypeCode);
        intent.putExtra("dataId", dataId);
//        context.getApplicationContext().startActivity(intent);
        ServiceHelper.startActivityWithAppIsRuning(context.getApplicationContext(), intent, messtypeCode, dataId);
    }

    /**
     * setTags() 的回调函数。
     *
     * @param context    上下文
     * @param errorCode  错误码。0表示某些tag已经设置成功；非0表示所有tag的设置均失败。
     * @param sucessTags 设置成功的tag
     * @param failTags   设置失败的tag
     * @param requestId  分配给对云推送的请求的id
     */
    @Override
    public void onSetTags(Context context, int errorCode,
                          List<String> sucessTags, List<String> failTags, String requestId) {
        Log.i(TAG, "onSetTags  errorCode:" + errorCode);


    }

    /**
     * delTags() 的回调函数。
     *
     * @param context    上下文
     * @param errorCode  错误码。0表示某些tag已经删除成功；非0表示所有tag均删除失败。
     * @param sucessTags 成功删除的tag
     * @param failTags   删除失败的tag
     * @param requestId  分配给对云推送的请求的id
     */
    @Override
    public void onDelTags(Context context, int errorCode,
                          List<String> sucessTags, List<String> failTags, String requestId) {
        Log.i(TAG, "onDelTags errorCode: " + errorCode);
    }

    /**
     * listTags() 的回调函数。
     *
     * @param context   上下文
     * @param errorCode 错误码。0表示列举tag成功；非0表示失败。
     * @param tags      当前应用设置的所有tag。
     * @param requestId 分配给对云推送的请求的id
     */
    @Override
    public void onListTags(Context context, int errorCode, List<String> tags,
                           String requestId) {
        Log.i(TAG, "errorCode:" + errorCode);
        Log.i(TAG, "requestId:" + requestId);
        for (int j = 0; j < tags.size(); j++) {
            Log.i(TAG, tags.get(j));
        }
    }

    /**
     * PushManager.stopWork() 的回调函数。
     *
     * @param context   上下文
     * @param errorCode 错误码。0表示从云推送解绑定成功；非0表示失败。
     * @param requestId 分配给对云推送的请求的id
     */
    @Override
    public void onUnbind(Context context, int errorCode, String requestId) {
        Log.i(TAG, "onUnbind: errorCode：" + errorCode);
        Log.i(TAG, "onUnbind: requestId：" + requestId);
    }


    public interface onReceiveServerMsgListener {
        void onReceiveServerMsg(String title, String description, String customContentString);
    }


}
