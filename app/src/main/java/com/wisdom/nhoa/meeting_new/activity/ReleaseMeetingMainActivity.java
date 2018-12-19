package com.wisdom.nhoa.meeting_new.activity;

import android.content.Intent;
import android.os.Bundle;
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
import com.wisdom.nhoa.meeting_new.adapter.MeetingPublishListAdapter;
import com.wisdom.nhoa.meeting_new.model.MeetingListModel;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.ToastUtil;
import com.wisdom.nhoa.util.UserPermissionHelper;
import com.wisdom.nhoa.util.http_util.HttpUtil;
import com.wisdom.nhoa.util.http_util.callback.BaseModel;
import com.wisdom.nhoa.util.http_util.callback.JsonCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import static com.wisdom.nhoa.ConstantString.PERMISSION_MEETING_PUBLISH;
import static com.wisdom.nhoa.ConstantString.PERMISSION_MEETING_ROOM_APPLY;

/**
 * 发布会议列表界面
 */
public class ReleaseMeetingMainActivity extends BaseActivity implements MeetingPublishListAdapter.OnChildItemClickedListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.tv_no_data)
    TextView tv_no_data;
    MeetingPublishListAdapter adapter;

    public static final String TAG = ReleaseMeetingMainActivity.class.getSimpleName();

    @Override
    public void setlayoutIds() {
        setContentView(R.layout.activity_release_meeting_main);
    }

    @Override
    public void initViews() {
        setTitle(R.string.meeting_publish_meeting);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getData();
            }
        });
        refreshLayout.autoRefresh();
    }


    /**
     * 取得服务器数据适配到界面
     */
    private void getData() {
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(this).getAccess_token());
        HttpUtil.httpGet(ConstantUrl.GET_MEETING_PUBLISH_LIST, params, new JsonCallback<BaseModel<List<MeetingListModel>>>() {
            @Override
            public void onSuccess(BaseModel<List<MeetingListModel>> model, Call call, Response response) {
                refreshLayout.finishRefresh();
                if (model.results.size() > 0) {
                    //有数据项
                    tv_no_data.setVisibility(View.GONE);
                    refreshLayout.setVisibility(View.VISIBLE);
                    adapter = new MeetingPublishListAdapter(model.results, ReleaseMeetingMainActivity.this);
                    adapter.setListener(ReleaseMeetingMainActivity.this);
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
     * 界面列表的子项点击事件
     *
     * @param recyclerView
     * @param pos
     * @param view
     * @param model
     */
    @Override
    public void onChildItemClick(RecyclerView recyclerView, int pos, View view, MeetingListModel model) {
        if (ConstantString.MEETING_PUBLISH_STATUS_PUBLISHED
                .equals(model.getStatus())) {
            //已发布状态，根据数据中是否存在meetId来判断是否能跳转详情页面
            String meetId = model.getMeetingid();
            if (meetId != null && !"".equals(meetId)) {
                Intent intent = new Intent(this, MeetingNewDetailActivity.class);
                intent.putExtra("data", model.getId());
                startActivity(intent);
            } else {
                //暂无会议相关信息，跳转到会议室详情信息
                Intent intent = new Intent(this, MyCheckedMeetingRoomDetailActivity.class);
                intent.putExtra("data", model.getId());
                startActivity(intent);
            }
        } else if (ConstantString.MEETING_PUBLISH_STATUS_PASS
                .equals(model.getStatus())) {
            // 审核通过状态，跳转发布会议填写表单界面
            if (UserPermissionHelper.getUserPermissionStatus(this, PERMISSION_MEETING_PUBLISH)) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", model);
                Intent intent = new Intent(ReleaseMeetingMainActivity.this, MeetingPublishDoPublishActivity.class);
                intent.putExtra("tag", ConstantString.MEETING_PUBLISH_TAG_MEETING_LIST);
                intent.putExtras(bundle);
                startActivityForResult(intent, ConstantString.REQUEST_CODE_REFRESH_DATA);
            }
        } else {
            ToastUtil.showToast("暂无该状态数据信息");
        }
    }


    /**
     * 刷新界面的回调方法
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstantString.REQUEST_CODE_REFRESH_DATA
                || resultCode == ConstantString.REQUEST_CODE_REFRESH_DATA) {
            refreshLayout.autoRefresh();
        }
    }


    /**
     * 申请会议室按钮点击事件
     */
    @OnClick(R.id.bt_apply_meeting_room)
    public void onViewClicked() {
        if (UserPermissionHelper.getUserPermissionStatus(this, PERMISSION_MEETING_ROOM_APPLY)) {
            startActivity(new Intent(this, ApplyMeetingRoomApplyActivity.class));
        }
    }
}
