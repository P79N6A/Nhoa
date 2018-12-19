package com.wisdom.nhoa.supervision.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.base.BaseActivity;
import com.wisdom.nhoa.supervision.model.SupervisionModel;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.StrUtils;
import com.wisdom.nhoa.util.ToastUtil;
import com.wisdom.nhoa.util.U;
import com.wisdom.nhoa.util.http_util.HttpUtil;
import com.wisdom.nhoa.widget.CustomDatePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.supervision.activity
 * @class describe：添加反馈信息界面
 * @time 2018/7/26 14:50
 * @change
 */
public class AddFeedBackActivity extends BaseActivity {
    @BindView(R.id.head_parent)
    RelativeLayout headParent;
    @BindView(R.id.tv_value_name)
    TextView tvValueName;
    @BindView(R.id.tv_value_percent)
    EditText tvValuePercent;
    @BindView(R.id.tv_value_start_time)
    TextView tvValueStartTime;
    @BindView(R.id.tv_value_end_time)
    TextView tvValueEndTime;
    @BindView(R.id.tv_value_content)
    EditText tvValueContent;
    @BindView(R.id.tv_confirm)
    TextView tvFeedBack;
    private String supervisionid;
    private String addendumid;
    private CustomDatePicker customDatePicker;
    private SupervisionModel model = null;
    private String currentchosetime = "";
    private String currentTime = "";
    private int currentmark = 0;
    private int position;

    @Override
    public void initViews() {
        setTitle(R.string.superversion_add_feed_back_title);
        if (getIntent() != null) {
            model = (SupervisionModel) getIntent().getExtras().getSerializable("data");
            position = getIntent().getIntExtra("position", 0);
            tvValueName.setText(model.getName());
//            tvValuePercent.setText(model.getPercentage());
        } else {
            ToastUtil.showToast("获取数据失败，请重试");
        }
        initDataPick();
    }

    @Override
    public void setlayoutIds() {
        setContentView(R.layout.activity_add_feed_back);
    }


    @OnClick({R.id.tv_value_start_time, R.id.tv_value_end_time, R.id.tv_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_value_start_time: {
                //开始时间选择
                currentmark = 0;
                customDatePicker.show(tvValueStartTime.getText().toString());
            }
            break;
            case R.id.tv_value_end_time: {
                //结束时间选择
                currentmark = 1;
                customDatePicker.show(tvValueEndTime.getText().toString());
            }
            break;
            case R.id.tv_confirm: {
//                确定按钮
                if (isChecked()) {
                    submitData();
                }
            }
            break;
        }
    }


    private void initDataPick() {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
//        String now = sdf.format(new Date());
        customDatePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                currentTime = time;
                if (currentmark == 0) {
                    tvValueStartTime.setText(time);
                } else {
                    tvValueEndTime.setText(time);
                }
            }
        }, "1990-01-01 00:00", "2100-01-01 00:00");
        customDatePicker.showSpecificTime(true); // 显示时和分
        customDatePicker.setIsLoop(true); // 允许循环滚动
        customDatePicker.showSpecificTime(true); // 显示时和分
        customDatePicker.setIsLoop(true); // 允许循环滚动
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date curDate = new Date(System.currentTimeMillis());
        Date onehourDate = new Date(System.currentTimeMillis() + 60 * 60 * 1000);
        tvValueStartTime.setText(formatter.format(curDate));
        tvValueEndTime.setText(formatter.format(onehourDate));

    }

    /**
     * 验证界面空值设置
     *
     * @return
     */
    private Boolean isChecked() {
        Boolean isChecked = true;
        if (StrUtils.isEdtTxtEmpty(tvValuePercent)) {
            ToastUtil.showToast(R.string.superversion_add_feed_back_hint1);
            isChecked = false;
        } else if (Integer.parseInt(StrUtils.getEdtTxtContent(tvValuePercent)) > 100) {
            ToastUtil.showToast(R.string.superversion_add_feed_back_hint3);
            isChecked = false;
        } else if (StrUtils.isEdtTxtEmpty(tvValueContent)) {
            ToastUtil.showToast(R.string.superversion_add_feed_back_hint2);
            isChecked = false;
        }
        return isChecked;
    }

    /**
     * 提交数据到服务器接口
     */
    private void submitData() {
        HttpParams params = new HttpParams();
        params.put("access_token", SharedPreferenceUtil.getUserInfo(this).getAccess_token());
        params.put("appkey", ConstantString.APP_KEY);
        params.put("supervisionid", model.getChild().get(position).getSupervisionid());
        params.put("addendumid", model.getChild().get(position).getAddendumid());
        params.put("userpercentage", StrUtils.getEdtTxtContent(tvValuePercent));
        params.put("begintime", tvValueStartTime.getText().toString());
        params.put("endtime", tvValueEndTime.getText().toString());
        params.put("content", StrUtils.getEdtTxtContent(tvValueContent));
        U.showLoadingDialog(this);
        HttpUtil.httpGet(ConstantUrl.ADD_FEED_BACK_INFO_URL, params, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                U.closeLoadingDialog();
                ToastUtil.showToast("提交失败，请重试");
            }

            @Override
            public void onSuccess(String s, Call call, Response response) {
                U.closeLoadingDialog();
                try {
                    JSONObject jsonObject=new JSONObject(s);
                    if (jsonObject.getInt("error_code")==0){
                        ToastUtil.showToast("提交成功");
                        AddFeedBackActivity.this.finish();
                    }else{
                        ToastUtil.showToast("提交失败，请重试");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });




    }
}
