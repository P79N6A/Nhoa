package com.wisdom.nhoa.meeting_new.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.lzy.okgo.model.HttpParams;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.base.BaseActivity;
import com.wisdom.nhoa.homepage.adapter.ParticpantAdapter;
import com.wisdom.nhoa.meeting_new.model.ParticpantModel;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.U;
import com.wisdom.nhoa.util.http_util.HttpUtil;
import com.wisdom.nhoa.util.http_util.callback.BaseModel;
import com.wisdom.nhoa.util.http_util.callback.JsonCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author lxd
 * @ProjectName project： 选择参会人员
 * @class package：
 * @class describe：ParticipantActivity
 * @time 17:30
 * @change
 */
public class ParticipantActivity extends BaseActivity {

    @BindView(R.id.lv_participant)
    ListView lvParticipant;
    @BindView(R.id.bt_create_meeting)
    Button btCreateMeeting;

    private ParticpantAdapter adapter;
    private String memberid = "";
    private String username = "";

    private String TAG = "partic";
    public List<ParticpantModel> particpantModelList = new ArrayList<>();

    @Override
    public void initViews() {
        setTitle(R.string.index_meeting_participant_title);
        lvParticipant.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ParticpantAdapter.ParticpantHolder holder = (ParticpantAdapter.ParticpantHolder) view.getTag();
                if (ParticpantAdapter.ischeck.get(position).equals("true")) {
                    ParticpantAdapter.ischeck.put(position, "false");
                    holder.iv_checkbox.setImageDrawable(getResources().getDrawable(R.mipmap.checkbox));
                } else {
                    ParticpantAdapter.ischeck.put(position, "true");
                    holder.iv_checkbox.setImageDrawable(getResources().getDrawable(R.mipmap.checkbox_pre));
                }
            }
        });
        btCreateMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<ParticpantModel> listOfParticpantModel = new ArrayList<>();
                for (int i = 0; i < particpantModelList.size(); i++) {
                    if (ParticpantAdapter.ischeck.get(i).equals("true")) {
                        memberid = memberid + particpantModelList.get(i).getUser_id() + ",";
                        username = username + particpantModelList.get(i).getUser_name() + ",";
                        listOfParticpantModel.add(particpantModelList.get(i));
                    }

                }
                if (!"".equals(memberid)) {
                    memberid = memberid.substring(0, memberid.length() - 1);
                    username = username.substring(0, username.length() - 1);
                }

                //数据是使用Intent返回
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", listOfParticpantModel);
                //把返回数据存入Intent
                intent.putExtras(bundle);
                intent.putExtra("memberid", memberid);
                intent.putExtra("username", username);
                //设置返回数据
                ParticipantActivity.this.setResult(RESULT_OK, intent);
                //关闭Activity
                ParticipantActivity.this.finish();

            }
        });
        getdata();
    }

    @Override
    public void setlayoutIds() {
        setContentView(R.layout.activity_participant);
    }

    private void getdata() {
        U.showLoadingDialog(this);
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(ParticipantActivity.this).getAccess_token());
        HttpUtil.httpGet(ConstantUrl.MANAGE_USER, params, new JsonCallback<BaseModel<List<ParticpantModel>>>() {
            @Override
            public void onSuccess(BaseModel<List<ParticpantModel>> listBaseModel, Call call, Response response) {
                U.closeLoadingDialog();
                particpantModelList = listBaseModel.results;
                adapter = new ParticpantAdapter(ParticipantActivity.this, listBaseModel.results);
                lvParticipant.setAdapter(adapter);
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                U.closeLoadingDialog();
            }
        });
    }
}
