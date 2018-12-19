package com.wisdom.nhoa.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.approval.model.MessageEvent;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.StrUtils;
import com.wisdom.nhoa.util.ToastUtil;
import com.wisdom.nhoa.util.http_util.HttpUtil;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;

/**
 * @authorzhanglichao
 * @date2018/7/12 15:27
 * 审核是否通过dialog
 * @package_name com.wisdom.nhoa.widget
 */
public class LeaveApproveDialog extends DialogFragment implements View.OnClickListener {
    private Button btn_confirm;
    private Button btn_cancle;
    private EditText et_approval_suggestion;
    private String id;
    private String type;//待审批类型
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.approval_dialog_leave,container,false);
        initViews( view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(getActivity());
    }

    /**
     * 初始化页面内相关控件
     */
    private void initViews(View view) {
        id=getArguments().getString("id");
        type=getArguments().getString("type");
        btn_cancle = view.findViewById(R.id.btn_approval_refuse);
        btn_confirm = view.findViewById(R.id.btn_approval_pass);
        et_approval_suggestion = view.findViewById(R.id.et_approval_suggestion);
        btn_confirm.setOnClickListener(this);
        btn_cancle.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) { //是否通过按钮
            case R.id.btn_approval_refuse: {
                suggestionComit(2);

            }
            break;
            case R.id.btn_approval_pass: {
                suggestionComit(1);
            }
            break;
        }
    }
    /**
     * 提交修改意见到服务器端
     */
    private void suggestionComit(int isPass) {
        String suggestion = StrUtils.getEdtTxtContent(et_approval_suggestion);
        if (StrUtils.isEdtTxtEmpty(et_approval_suggestion)) {
            ToastUtil.showToast("请填写审批意见");
        } else {
            HttpParams params = new HttpParams();
            params.put("appkey", ConstantString.APP_KEY);
            params.put("access_token", SharedPreferenceUtil.getUserInfo(getContext()).getAccess_token());
            params.put("id", id);
            params.put("type", type);
            params.put("explain", suggestion);
            params.put("status", isPass);
            HttpUtil.httpGet(ConstantUrl.SUBMIT_APPROVAL, params, new StringCallback() {
                @Override
                public void onError(Call call, Response response, Exception e) {
                    super.onError(call, response, e);
                    ToastUtil.showToast(e.getMessage());
                }

                @Override
                public void onSuccess(String s, Call call, Response response) {
                    LeaveApproveDialog.this.dismiss();
                    EventBus.getDefault().post(new MessageEvent("1"));
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        String code = jsonObject.getString("error_code");
                        if ("0".equals(code)) {
                            LeaveApproveDialog.this.dismiss();
                            ToastUtil.showToast(R.string.sendreceive_receive_reason_success_hint);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });

        }
    }
}
