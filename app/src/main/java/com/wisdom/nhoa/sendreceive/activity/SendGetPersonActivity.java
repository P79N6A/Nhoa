package com.wisdom.nhoa.sendreceive.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.base.BaseActivity;
import com.wisdom.nhoa.base.BroadCastManager;
import com.wisdom.nhoa.sendreceive.adapter.SendIssuePersonAdapter;
import com.wisdom.nhoa.sendreceive.model.SendIssuePersonModel;
import com.wisdom.nhoa.sendreceive.model.SendIssuePersonPostModel;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.ToastUtil;
import com.wisdom.nhoa.util.U;
import com.wisdom.nhoa.util.http_util.HttpUtil;
import com.wisdom.nhoa.util.http_util.callback.BaseModel;
import com.wisdom.nhoa.util.http_util.callback.JsonCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author lxd
 * @ProjectName project： 选择节点人员
 * @class package：
 * @class describe：SendGetPersonActivity
 * @time 9:06
 * @change
 */
public class SendGetPersonActivity extends BaseActivity {

    @BindView(R.id.lv_person)
    ListView lv_person;
    @BindView(R.id.bt_confirm)
    Button btConfirm;
    private String insid = "";
    private String nodeid = "";//当前节点id
    private String nodename = "";
    private String processkey = "";
    private String processtag = "";
    private String doctype = "";
    private SendIssuePersonAdapter adapter;
    private List<SendIssuePersonModel> models;
    private List<SendIssuePersonPostModel> postmodel = new ArrayList<>();
    ;
    private SendIssuePersonPostModel personModel;
    private String usernodeId = "";//当前办事人id
    private String transitionname = "";
    private String userjson = "";//发送接口传的数据
    private String fieldjson = "";
    private String TAG = SendGetPersonActivity.class.getSimpleName();

    @Override
    public void initViews() {
        if (getIntent() != null) {
            insid = getIntent().getStringExtra("insid");
            nodeid = getIntent().getStringExtra("nodeid");
            nodename = getIntent().getStringExtra("nodename");
            processkey = getIntent().getStringExtra("processkey");
            processtag = getIntent().getStringExtra("processtag");
            doctype = getIntent().getStringExtra("doctype");
            usernodeId = getIntent().getStringExtra("usernodeId");
            transitionname = getIntent().getStringExtra("transitionname");
            fieldjson = getIntent().getStringExtra("fieldjson");
        }

        lv_person.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SendIssuePersonAdapter.PersonViewHolder holder = (SendIssuePersonAdapter.PersonViewHolder) view.getTag();
                for (int i = 0; i < models.size(); i++) {
                    if (position != i) {
                        if (models.get(i).getIsChecked().equals("true")) {
                            models.get(i).setIsChecked("false");
                            adapter.setListData(models);
                            //取消上次选择的按钮
                            adapter.getView(i, lv_person.getChildAt(i), lv_person);
                        }
                    }
                }
                //设置选择的状态并获取数据
                if (models.get(position).getIsChecked().equals("false")) {
                    models.get(position).setIsChecked("true");
                    holder.img_radio.setImageDrawable(context.getResources().getDrawable(R.mipmap.checkbox_pre));
                    personModel = new SendIssuePersonPostModel();
                    personModel.setRoleName(models.get(position).getRole_name());
                    personModel.setNodeId(models.get(position).getNode_id());
                    personModel.setNodeName(models.get(position).getNode_name());
                    personModel.setUserName(models.get(position).getUser_name());
                    personModel.setUserId(models.get(position).getUser_id());
                    if (postmodel.size() > 0) {
                        postmodel.remove(0);
                        postmodel.add(personModel);
                    }else{
                        postmodel.add(personModel);
                    }
                }
                userjson = new Gson().toJson(postmodel);
                Log.i(TAG, "onItemClick: "+userjson);
            }
        });
        getData();
        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!"".equals(userjson)) {
                    submit();
                } else {
                    ToastUtil.showToast("请选择办事人员");
                }
            }
        });
    }

    @Override
    public void setlayoutIds() {
        setContentView(R.layout.activity_send_get_person);
    }

    //获取节点人员接口
    private void getData() {
        U.showLoadingDialog(this);
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(SendGetPersonActivity.this).getAccess_token());
        params.put("insid", insid);
        params.put("nodeid", usernodeId);
        params.put("doctype", doctype);
        HttpUtil.httpGet(ConstantUrl.BACKLOGDETAILS_NODEPERSON, params, new JsonCallback<BaseModel<List<SendIssuePersonModel>>>() {
            public void onSuccess(BaseModel<List<SendIssuePersonModel>> listBaseModel, Call call, Response response) {
                models = listBaseModel.results;
                for (int i = 0; i < models.size(); i++) {
                    models.get(i).setIsChecked("false");
                }
                adapter = new SendIssuePersonAdapter(SendGetPersonActivity.this, models);
                lv_person.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                U.closeLoadingDialog();

            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                U.closeLoadingDialog();
            }
        });
    }

    //提交接口！！！！！
    private void submit() {
        U.showLoadingDialog(this);
        HttpParams params = new HttpParams();
        Log.i("SendGet", "getData: " + fieldjson);
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(SendGetPersonActivity.this).getAccess_token());
        params.put("insid", insid);
        params.put("nodeid", nodeid);
        params.put("nodename", nodename);
        params.put("processkey", processkey);
        params.put("processtag", processtag);
        params.put("doctype", doctype);
        params.put("fieldjson", fieldjson);
        params.put("userjson", userjson);
        params.put("transitiontype", transitionname);
        Log.i(TAG, "processtag: " + processtag);
        Log.i(TAG, "fieldjson: " + fieldjson);
        Log.i(TAG, "userjson: " + userjson);
        Log.i(TAG, "transitiontype: " + transitionname);
//        ToastUtil.showToast(fieldjson);
        HttpUtil.httpGet(ConstantUrl.BACKLOGDETAILS_SUBMIT, params, new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            U.closeDialog();
                            JSONObject jsonObject = new JSONObject(s);
                            String code = jsonObject.getString("error_code");
                            if ("0".equals(code)) {
                                ToastUtil.showToast("发送成功");
                                SendGetPersonActivity.this.finish();
                                Intent intent = new Intent();
                                intent.setAction(ConstantString.BROADCAST_ACTIVITY_FINISH);
                                BroadCastManager.getInstance().sendBroadCast(SendGetPersonActivity.this, intent);
                            } else {
                                ToastUtil.showToast("发送失败");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        U.closeDialog();
                        ToastUtil.showToast("服务器连接失败");
                    }
                }

        );
    }

}
