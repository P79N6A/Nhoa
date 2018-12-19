package com.wisdom.nhoa.push.activity;

import android.app.NotificationManager;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.wisdom.nhoa.AppApplication;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.base.BaseActivity;
import com.wisdom.nhoa.circulated.activity.FilePreviewActivity;
import com.wisdom.nhoa.homepage.activity.HomePageActivity;
import com.wisdom.nhoa.homepage.model.MsgListModel;
import com.wisdom.nhoa.push.model.CCSPModel;
import com.wisdom.nhoa.push.model.DBXXModel;
import com.wisdom.nhoa.push.model.GGGLModel;
import com.wisdom.nhoa.push.model.GWCYModel;
import com.wisdom.nhoa.push.model.HYGLModel;
import com.wisdom.nhoa.push.model.XJSPModel;
import com.wisdom.nhoa.push.model.YPSPModel;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.ToastUtil;
import com.wisdom.nhoa.util.U;
import com.wisdom.nhoa.util.dbHelper.DbHelperCustom;
import com.wisdom.nhoa.util.greendao.MsgListModelDao;
import com.wisdom.nhoa.util.http_util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by hanxuefeng on 2018/8/2.
 * 点击推送过来的通知后的详情展示页面
 */

public class PushDetailActivity extends BaseActivity {
    @BindView(R.id.ll_parent)
    LinearLayout llParent;
    private String messTypeCode;
    private String dataId;

    @Override
    public void setlayoutIds() {
        setContentView(R.layout.activity_my_approval_detail);
    }


    @Override
    public void initViews() {
        setTitle("详细");
        if (getIntent() != null) {
            messTypeCode = getIntent().getStringExtra("messTypeCode");
            dataId = getIntent().getStringExtra("dataId");
            if (messTypeCode != null & dataId != null) {
                getData(messTypeCode, dataId);
            }
        }
    }


    /**
     * 循环布局，将数据源动态展现在界面
     *
     * @param key
     * @param value
     */
    private void setShowData(String[] key, String[] value) {
        if (key.length == value.length) {
            for (int i = 0; i < key.length; i++) {
                View itemView = LayoutInflater.from(this).inflate(R.layout.item_my_approval_detail, null, false);
                TextView tv_key = itemView.findViewById(R.id.tv_key);
                TextView tv_value = itemView.findViewById(R.id.tv_value);
                tv_key.setText(key[i]);
                if (!"".equals(value[i])) {
                    tv_value.setText(value[i]);
                } else {
                    tv_value.setText("无");
                }
                llParent.addView(itemView);
            }
        } else {
            ToastUtil.showToast("数据加载失败请重试");
            this.finish();
        }
    }


    /**
     * 访问接口，获得数据
     */
    private void getData(final String messTypeCode, final String dataId) {
        U.showLoadingDialog(this);
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(this).getAccess_token());
        params.put("messTypeCode", messTypeCode);
        params.put("dataId", dataId);
        HttpUtil.httpGet(ConstantUrl.GET_PUSH_MSG_DETAIL_URL, params, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                U.closeDialog();
            }

            @Override
            public void onSuccess(String s, Call call, Response response) {
                U.closeDialog();
                try {
                    if (s != null && !"".equals(s)) {
                        JSONObject jsonObject = new JSONObject(s);
                        if (jsonObject.getInt("error_code") == 0) {
                            //访问成功，并且有数据返回
                            setData(messTypeCode, jsonObject.getJSONObject("results").toString());
                            DbHelperCustom.updata(AppApplication.getInstance().GetSqliteDataBase(),context,messTypeCode,dataId);
                            Intent intent = new Intent();
                            intent.setAction(HomePageActivity.UPDATAMSGCOUNTS);
                             sendBroadcast(intent);
                             NotificationManager notification= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                             notification.cancelAll();
                            //  MsgListModel msgListModel = AppApplication.getDaosession().getMsgListModelDao().queryBuilder().where(MsgListModelDao.Properties.MessTypeCode.eq(messTypeCode), MsgListModelDao.Properties.DataId.eq(dataId)).unique();
//                            if (msgListModel != null) {
//                                if (msgListModel.getIsread() == 0) {
//                                    msgListModel.setIsread(1);
//                                    AppApplication.getDaosession().update(msgListModel);
//                                    //发送
//                                    Intent intent = new Intent();
//                                    intent.setAction(HomePageActivity.UPDATAMSGCOUNTS);
//                                    sendBroadcast(intent);
//                                }
//                            }
                        } else {
                            //访问失败，没有数据返回
                            ToastUtil.showToast("加载失败，请重试");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * @param messTypeCode
     * @param results      根据网络获取的数据进行界面的设置
     */
    private void setData(String messTypeCode, String results) {
        Gson gson = new Gson();
        switch (messTypeCode) {
            case ConstantString.PUSH_DETAIL_TAG_GWCY: {
                //公文传阅
                GWCYModel model = gson.fromJson(results, GWCYModel.class);
                String[] key = {"传阅组名称", "公文标题", "上传时间", "上传人", "公文内容"};
                String[] value = {model.getGroupName(), model.getDocName(), model.getUpTime()
                        , model.getWriter(), "点击预览"};
                setShowData(key, value);
                previewFile(model.getDocUrl(), "");
            }
            break;
            case ConstantString.PUSH_DETAIL_TAG_HYGL: {
                //会议管理
                HYGLModel model = gson.fromJson(results, HYGLModel.class);
                String[] key = {"会议主题", "召开部门", "会议发起人"
                        , "参会人员", "开始时间", "结束时间"
                        , "创建时间", "会议状态", "会议内容"};
                String[] value = {model.getMeetingtitle(), model.getConvokedep(), model.getCompere()
                        , model.getMemberid(), model.getStarttime(), model.getEndtime()
                        , model.getCreatetime(), model.getState(), model.getMeetingcontent()};
                setShowData(key, value);
            }
            break;
            case ConstantString.PUSH_DETAIL_TAG_GGGL: {
                //公告管理
                GGGLModel model = gson.fromJson(results, GGGLModel.class);
                String[] key = {"通知标题", "通知内容", "发布人"
                        , "附件名称", "附件"};
                CharSequence charSequence = null;
                if (model.getContent() != null) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        charSequence = Html.fromHtml(model.getContent(), Html.FROM_HTML_MODE_LEGACY);
                    } else {
                        charSequence = Html.fromHtml(model.getContent());
                    }
                }
                String[] value = {model.getTitle(), charSequence.toString(), model.getUsername(), model.getFilename(), "点击查看"};
                setShowData(key, value);
                previewFile(model.getFileurl(), "");
            }
            break;
            case ConstantString.PUSH_DETAIL_TAG_DBXX: {
                //督办信息
                DBXXModel model = gson.fromJson(results, DBXXModel.class);
                String state;
                switch (model.getStatus()) {
                    case "0": {
                        state = "进行中";
                    }
                    break;
                    case "1": {
                        state = "完成";
                    }
                    break;
                    case "2": {
                        state = "超额完成";
                    }
                    break;
                    default: {
                        state = "无反馈";
                    }
                    break;
                }
                String[] key = {"督办名称", "开始时间", "完成百分比"
                        , "状态", "督办人", "结束时间"
                        , "督办内容", "创建时间", "完成时间"};
                String[] value = {model.getName(), model.getBegintime(), model.getPercentage()
                        , state, model.getUsername(), model.getEndtime()
                        , model.getContent(), model.getCreatetime(), model.getFinishtime()};
                setShowData(key, value);
            }
            break;
            case ConstantString.PUSH_DETAIL_TAG_XJSP: {
//                休假审批
                XJSPModel model = gson.fromJson(results, XJSPModel.class);
                String state;
                switch (model.getStatus()) {
                    case ConstantString.APPROVAL_STATUS_CHECK: {
                        state = "待审核";
                    }
                    break;
                    case ConstantString.APPROVAL_STATUS_PASS: {
                        state = "审核通过";
                    }
                    break;
                    case ConstantString.APPROVAL_STATUS_BACK: {
                        state = "审核退回";
                    }
                    break;
                    default: {
                        state = "无";
                    }
                    break;
                }
                String[] key = {"休假天数", "申请时间", "状态"
                        , "休假类型", "申请人", "开始时间"
                        , "结束时间", "申请事由", "备注信息"
                        , "抄送人", "文件名"};
                String[] value = {model.getDays(), model.getApplytime(), state
                        , model.getTypename(), model.getUsername(), model.getStarttime()
                        , model.getEndtime(), model.getReason(), model.getRemark()
                        , model.getRemindername(), model.getFilename()};
                setShowData(key, value);
                previewFile(model.getFileurl(), "");
            }
            break;
            case ConstantString.PUSH_DETAIL_TAG_CCSP: {
//                出差审批
                CCSPModel model = gson.fromJson(results, CCSPModel.class);
                String state;
                switch (model.getStatus()) {
                    case ConstantString.APPROVAL_STATUS_CHECK: {
                        state = "待审核";
                    }
                    break;
                    case ConstantString.APPROVAL_STATUS_PASS: {
                        state = "审核通过";
                    }
                    break;
                    case ConstantString.APPROVAL_STATUS_BACK: {
                        state = "审核退回";
                    }
                    break;
                    default: {
                        state = "无";
                    }
                    break;
                }
                String[] key = {"出差天数", "申请时间", "状态"
                        , "交通工具", "申请人", "开始时间"
                        , "结束时间", "申请事由", "备注信息"
                        , "抄送人", "出差地点", "文件名"};
                String[] value = {model.getDays(), model.getApplytime(), state
                        , model.getVehicle(), model.getUsername(), model.getStarttime()
                        , model.getEndtime(), model.getReason(), model.getRemark()
                        , model.getRemindername(), model.getAddr(), model.getFilename()};
                setShowData(key, value);
                previewFile(model.getFileurl(), "");
            }
            break;
            case ConstantString.PUSH_DETAIL_TAG_YPSP: {
//                用品审批
                YPSPModel model = gson.fromJson(results, YPSPModel.class);
                String state;
                switch (model.getStatus()) {
                    case ConstantString.APPROVAL_STATUS_CHECK: {
                        state = "待审核";
                    }
                    break;
                    case ConstantString.APPROVAL_STATUS_PASS: {
                        state = "审核通过";
                    }
                    break;
                    case ConstantString.APPROVAL_STATUS_BACK: {
                        state = "审核退回";
                    }
                    break;
                    default: {
                        state = "无";
                    }
                    break;
                }
                String[] key = {"申请时间", "状态", "物品名称"
                        , "用品规格", "申请人", "申请数量"
                        , "申请事由", "备注信息"
                        , "抄送人",};
                String[] value = {model.getApplytime(), state, model.getTypename()
                        , model.getSpecifications(), model.getUsername(), model.getNumbers()
                        , model.getReason(), model.getRemark()
                        , model.getRemindername()};
                setShowData(key, value);
            }
            break;
        }
    }

    /**
     * 预览文件的方法
     */
    private void previewFile(final String url, final String fileId) {
        View view = llParent.getChildAt(llParent.getChildCount() - 1);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilePreviewActivity.show(context
                        , HttpUtil.getAbsolteUrl(url)
                        , fileId);
            }
        });
    }
}
