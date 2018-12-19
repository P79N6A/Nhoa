package com.wisdom.nhoa.metting.activity;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.base.BaseActivity;
import com.wisdom.nhoa.metting.adapter.MeetingSignUserAdapter;
import com.wisdom.nhoa.metting.model.MeetingSignUserModel;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.U;
import com.wisdom.nhoa.util.http_util.HttpUtil;
import com.wisdom.nhoa.util.http_util.callback.BaseModel;
import com.wisdom.nhoa.util.http_util.callback.JsonCallback;

import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author lxd
 * @ProjectName project： 已签到人员列表
 * @class package：
 * @class describe：MeetingSignUserActivity
 * @time 15:41
 * @change
 */
public class MeetingSignUserActivity extends BaseActivity {

    @BindView(R.id.lv_sign_user)
    ListView lvSignUser;
    private String meetingid = "";
    MeetingSignUserAdapter adapter;
    @BindView(R.id.tv_no_data_hint)
    TextView tv_no_data_hint;

    @Override
    public void initViews() {
        setTitle(R.string.index_meeting_signin_user);
        if (getIntent() != null) {
            meetingid = getIntent().getStringExtra("meetingid");
        }
        if (meetingid != null && !meetingid.equals("")) {
            getdata();
        }

    }

    @Override
    public void setlayoutIds() {
        setContentView(R.layout.activity_meeting_sign_user);
    }

    private void getdata() {
        U.showLoadingDialog(this);
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(this).getAccess_token());
        params.put("meetingid", meetingid);
        HttpUtil.httpGet(ConstantUrl.MEETING_SIGNUSER, params, new JsonCallback<BaseModel<List<MeetingSignUserModel>>>() {
            @Override
            public void onSuccess(BaseModel<List<MeetingSignUserModel>> meetingSignUserModel, Call call, Response response) {
                U.closeLoadingDialog();
                if (meetingSignUserModel.results.size() == 0) {
                    tv_no_data_hint.setVisibility(View.VISIBLE);
                    lvSignUser.setVisibility(View.GONE);
                } else {
                    tv_no_data_hint.setVisibility(View.GONE);
                    lvSignUser.setVisibility(View.VISIBLE);
                    adapter = new MeetingSignUserAdapter(MeetingSignUserActivity.this, meetingSignUserModel.results);
                    lvSignUser.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                U.closeLoadingDialog();
                tv_no_data_hint.setVisibility(View.VISIBLE);
                lvSignUser.setVisibility(View.GONE);
            }
        });
    }

}
