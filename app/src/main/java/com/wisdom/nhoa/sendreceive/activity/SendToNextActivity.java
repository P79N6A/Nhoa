package com.wisdom.nhoa.sendreceive.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.base.BaseActivity;
import com.wisdom.nhoa.base.BroadCastManager;
import com.wisdom.nhoa.sendreceive.adapter.SendIssueNodeAdapter;
import com.wisdom.nhoa.sendreceive.model.SendIssueNodeModel;
import com.wisdom.nhoa.sendreceive.model.ToDoModel;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.ToastUtil;
import com.wisdom.nhoa.util.U;
import com.wisdom.nhoa.util.http_util.HttpUtil;
import com.wisdom.nhoa.util.http_util.callback.BaseModel;
import com.wisdom.nhoa.util.http_util.callback.JsonCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.circulated.activity
 * @class describe：发送下一环节或者办结的界面
 * @time 2018/3/27 16:42
 * @change
 */

public class SendToNextActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    @BindView(R.id.rb_next)
    RadioButton rb_next;
    @BindView(R.id.rb_complete)
    RadioButton rb_complete;
    @BindView(R.id.rg_tab)
    RadioGroup rgTab;
    @BindView(R.id.ll_1)
    LinearLayout ll1;
    @BindView(R.id.ll_2)
    LinearLayout ll2;
    @BindView(R.id.ll_bg)
    LinearLayout ll_bg;
    @BindView(R.id.lv_data)
    ListView lvData;
    @BindView(R.id.btn_complete)
    Button btn_complete;

    private ToDoModel toDoModel;
    private SendIssueNodeAdapter adapter;
    private List<SendIssueNodeModel> models;
    private String fieldjson = "";
    private String insid = "";
    private String doctype = "";
    private Boolean hasComplete = false;
    private MyReceiver receiver;


    @Override
    public void initViews() {
        rgTab.setOnCheckedChangeListener(this);
        rb_complete.setChecked(true);
        rb_next.setChecked(true);
        //接收广播关闭页面
        receiver = new MyReceiver();
        this.registerReceiver(receiver, new IntentFilter(ConstantString.BROADCAST_ACTIVITY_FINISH));
        if (getIntent() != null) {
            toDoModel = (ToDoModel) getIntent().getExtras().getSerializable("data");
            insid = getIntent().getStringExtra("insid");
            doctype = getIntent().getStringExtra("doctype");
            fieldjson = getIntent().getStringExtra("fieldjson");
            hasComplete = getIntent().getBooleanExtra("hasComplete", false);
        }
        if (hasComplete) {
            //含有办结状态
            ll_bg.setVisibility(View.VISIBLE);
            rgTab.setVisibility(View.VISIBLE);
        } else {
            //没有办结状态
            rgTab.setVisibility(View.GONE);
            ll_bg.setVisibility(View.GONE);

        }
        getAndSetData();
    }


    @Override
    public void setlayoutIds() {
        setContentView(R.layout.send_and_received_activity_send_to_next);
    }


    /**
     *
     *
     */
    private void getAndSetData() {
        U.showLoadingDialog(this);
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(SendToNextActivity.this).getAccess_token());
        params.put("insid", insid);
        params.put("nodeid", toDoModel.getNodeId());
        params.put("nodename", toDoModel.getNodeName());
        params.put("processkey", toDoModel.getProcessKey());
        params.put("processtag", toDoModel.getProcessTag());
        params.put("doctype", doctype);
        HttpUtil.httpGet(ConstantUrl.BACKLOGDETAILS_NEXTNODE, params, new JsonCallback<BaseModel<List<SendIssueNodeModel>>>() {
            public void onSuccess(BaseModel<List<SendIssueNodeModel>> listBaseModel, Call call, Response response) {
                models = listBaseModel.results;
                adapter = new SendIssueNodeAdapter(SendToNextActivity.this, models);
                lvData.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                lvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(SendToNextActivity.this, SendGetPersonActivity.class);
                        intent.putExtra("insid", insid);
                        intent.putExtra("usernodeId", models.get(position).getNodeid());
                        intent.putExtra("nodeid", toDoModel.getNodeId());
                        intent.putExtra("nodename", toDoModel.getNodeName());
                        intent.putExtra("processkey", toDoModel.getProcessKey());
                        intent.putExtra("processtag", toDoModel.getProcessTag());
                        intent.putExtra("transitionname", models.get(position).getTransitionname());
                        intent.putExtra("doctype", ConstantString.SEND_RECEIVE_LIST_TYPE_SEND);
                        intent.putExtra("fieldjson", fieldjson);
                        startActivity(intent);
                    }
                });
                U.closeLoadingDialog();
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                U.closeLoadingDialog();
            }
        });
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_next: {
                lvData.setVisibility(View.VISIBLE);
                btn_complete.setVisibility(View.GONE);
                ll1.setBackgroundColor(getResources().getColor(R.color.color_647aea));
                ll2.setBackgroundColor(getResources().getColor(R.color.white));
            }
            break;
            case R.id.rb_complete: {
                lvData.setVisibility(View.GONE);
                btn_complete.setVisibility(View.VISIBLE);
                ll2.setBackgroundColor(getResources().getColor(R.color.color_647aea));
                ll1.setBackgroundColor(getResources().getColor(R.color.white));
            }
            break;
            default: {
            }
        }
    }

    /**
     * 办结按钮点击事件
     */
    @OnClick(R.id.btn_complete)
    public void onViewClicked() {
        finishProcess();
    }

    private class MyReceiver extends BroadcastReceiver {
        /**
         * 接数据参数的Receiver
         *
         * @param context
         * @param intent
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    /**
     * 办结接口
     */
    private void finishProcess() {
        U.showLoadingDialog(this);
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(this).getAccess_token());
        params.put("insid", insid);
        params.put("fieldjson", fieldjson);
        params.put("processkey", toDoModel.getProcessKey());
        params.put("nodeid", toDoModel.getNodeId());
        params.put("doctype", ConstantString.SEND_RECEIVE_DOC_TYPE_SEND);
        HttpUtil.httpGet(ConstantUrl.FILE_COMPLETE_URL, params, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                U.closeLoadingDialog();
                ToastUtil.showToast(e.getMessage());
            }

            @Override
            public void onSuccess(String s, Call call, Response response) {
                try {
                    U.closeLoadingDialog();
                    JSONObject jsonObject = new JSONObject(s);
                    String code = jsonObject.getString("error_code");
                    if ("0".equals(code)) {
                        ToastUtil.showToast("提交成功");
                        SendToNextActivity.this.finish();
                        Intent intent = new Intent();
                        intent.setAction(ConstantString.BROADCAST_ACTIVITY_FINISH);
                        BroadCastManager.getInstance().sendBroadCast(SendToNextActivity.this, intent);
                    } else {
                        ToastUtil.showToast(jsonObject.getString("error_msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

}
