package com.wisdom.nhoa.metting.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.lzy.okgo.model.HttpParams;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.base.BaseActivity;
import com.wisdom.nhoa.metting.adapter.MeetingListAdapter;
import com.wisdom.nhoa.metting.model.MeetingListModel;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.U;
import com.wisdom.nhoa.util.http_util.HttpUtil;
import com.wisdom.nhoa.util.http_util.callback.BaseModel;
import com.wisdom.nhoa.util.http_util.callback.JsonCallback;
import com.wisdom.nhoa.widget.qrcode.CaptureActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author lxd
 * @ProjectName project： 会议
 * @class package：
 * @class describe：MeetingActivity
 * @time 17:33
 * @change
 */
public class MeetingActivity extends BaseActivity implements View.OnClickListener {

    private ListView lvMeeting;
    private Button btCreateMeeting;
    private MeetingListAdapter adapter;
    private List<MeetingListModel> listData;
    public static final String TAG = MeetingActivity.class.getSimpleName();


    @Override
    public void initViews() {
        setTitle(R.string.index_meeting_title);
        setRightIcon(R.mipmap.scan);
        lvMeeting = (ListView) findViewById(R.id.lv_meeting);
        btCreateMeeting = (Button) findViewById(R.id.bt_create_meeting);
        btCreateMeeting.setOnClickListener(this);
        getData();
    }


    @Override
    public void setlayoutIds() {
        setContentView(R.layout.activity_meeting);
    }


    private void getData() {
        U.showLoadingDialog(this);
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(MeetingActivity.this).getAccess_token());
        HttpUtil.httpGet(ConstantUrl.MEETING_LIST, params, new JsonCallback<BaseModel<List<MeetingListModel>>>() {
            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                U.closeLoadingDialog();
            }

            @Override
            public void onSuccess(BaseModel<List<MeetingListModel>> listBaseModel, Call call, Response response) {
                U.closeLoadingDialog();
                listData = new ArrayList<>();
                listData = listBaseModel.results;
                adapter = new MeetingListAdapter(MeetingActivity.this, listData);
                lvMeeting.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
        lvMeeting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "onItemClick: " + listData.get(position).toString());
                Intent intent = new Intent(MeetingActivity.this, MeetingDetailsActivity.class);
                intent.putExtra("meetingid", listData.get(position).getMeetingid());
                intent.putExtra("signstate", listData.get(position).getSignstate());
                intent.putExtra("isCreater", listData.get(position).getIscreater());
                intent.putExtra("state", listData.get(position).getState());
                startActivityForResult(intent, 778);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_create_meeting:
                startActivityForResult(new Intent(this, CreateMeetingActivity.class), 778);
                break;
        }
    }

    /**
     * 扫码回调事件
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 778) {
            getData();
        }
        if (requestCode == RESULT_FIRST_USER) {
//            if (data == null) {
//                ToastUtil.showToast("请扫描正确二维码");
//            }else{
            getData();
//            }
        }
    }

    /**
     * 右上角的扫码点击事件
     */
    @OnClick(R.id.head_right_iv)
    public void onViewClicked() {
        Intent openCameraIntent = new Intent(this, CaptureActivity.class);
        startActivityForResult(openCameraIntent, RESULT_FIRST_USER);
    }
}
