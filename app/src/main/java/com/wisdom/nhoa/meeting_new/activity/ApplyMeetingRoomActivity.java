package com.wisdom.nhoa.meeting_new.activity;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.base.BaseActivity;
import com.wisdom.nhoa.meeting_new.adapter.MeetingRoomApplyListAdapter;
import com.wisdom.nhoa.meeting_new.model.ApplyMeetingRoomListModel;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.UserPermissionHelper;
import com.wisdom.nhoa.util.http_util.HttpUtil;
import com.wisdom.nhoa.util.http_util.callback.BaseModel;
import com.wisdom.nhoa.util.http_util.callback.JsonCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import static com.wisdom.nhoa.ConstantString.PERMISSION_MEETING_ROOM_APPLY;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.meeting_new.activity
 * @class describe：申请会议室界面
 * @time 2018/10/23 9:45
 * @change
 */
public class ApplyMeetingRoomActivity extends BaseActivity implements MeetingRoomApplyListAdapter.OnChildItemClickedListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.tv_no_data)
    TextView tv_no_data;

    MeetingRoomApplyListAdapter adapter;

    @Override
    public void setlayoutIds() {
        setContentView(R.layout.activity_apply_meeting_room);
    }

    @Override
    public void initViews() {
        setTitle(R.string.meeting_apply_meeting_room);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getData();
            }
        });
        refreshLayout.autoRefresh();
    }


    /**
     * 申请会议室操作
     */
    @OnClick(R.id.bt_apply_meeting_room)
    public void onViewClicked() {
        if (UserPermissionHelper.getUserPermissionStatus(this, PERMISSION_MEETING_ROOM_APPLY)) {
            startActivity(new Intent(this, ApplyMeetingRoomApplyActivity.class));
        }
    }

    /**
     * 取得服务器数据适配到界面
     */
    private void getData() {
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(this).getAccess_token());
        HttpUtil.httpGet(ConstantUrl.APPLY_MEETING_ROOM_LIST_URL, params, new JsonCallback<BaseModel<List<ApplyMeetingRoomListModel>>>() {
            @Override
            public void onSuccess(BaseModel<List<ApplyMeetingRoomListModel>> model, Call call, Response response) {
                refreshLayout.finishRefresh();
                if (model.results.size() > 0) {
                    //有数据项
                    tv_no_data.setVisibility(View.GONE);
                    refreshLayout.setVisibility(View.VISIBLE);
                    adapter = new MeetingRoomApplyListAdapter(model.results, ApplyMeetingRoomActivity.this);
                    adapter.setListener(ApplyMeetingRoomActivity.this);
                    recyclerView.setAdapter(adapter);
                } else {
                    tv_no_data.setVisibility(View.VISIBLE);
                    refreshLayout.setVisibility(View.GONE);
                }
            }


            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                refreshLayout.finishRefresh();
                tv_no_data.setVisibility(View.VISIBLE);
            }


        });
    }


    /**
     * 列表子项点击事件
     *
     * @param recyclerView
     * @param pos
     * @param view
     * @param model
     */
    @Override
    public void onChildItemClick(RecyclerView recyclerView, int pos, View view, ApplyMeetingRoomListModel model) {
        Intent intent = new Intent(this, ApplyMeetingRoomDetailActivity.class);
        intent.putExtra("data", model.getId());
        startActivityForResult(intent, ConstantString.REQUEST_CODE_REFRESH_DATA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ConstantString.REQUEST_CODE_REFRESH_DATA) {
            refreshLayout.autoRefresh();

        }
    }
}
