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
import com.wisdom.nhoa.meeting_new.adapter.MeetingMySponsorListAdapter;
import com.wisdom.nhoa.meeting_new.model.MeetingMySponsorModel;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.http_util.HttpUtil;
import com.wisdom.nhoa.util.http_util.callback.BaseModel;
import com.wisdom.nhoa.util.http_util.callback.JsonCallback;

import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.meeting_new.activity
 * @class describe：会议-我发起的
 * @time 2018/10/23 16:52
 * @change
 */
public class MeetingMySponsorListActivity extends BaseActivity implements MeetingMySponsorListAdapter.OnChildItemClickedListener {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.tv_no_data)
    TextView tv_no_data;
    MeetingMySponsorListAdapter adapter;

    @Override
    public void setlayoutIds() {
        setContentView(R.layout.activity_meeting_my_sponsor);
    }

    @Override
    public void initViews() {
        setTitle(R.string.meeting_my_sponsor);
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
        HttpUtil.httpGet(ConstantUrl.MEETING_MY_SPONSOR_LIST_URL, params, new JsonCallback<BaseModel<List<MeetingMySponsorModel>>>() {
            @Override
            public void onSuccess(BaseModel<List<MeetingMySponsorModel>> model, Call call, Response response) {
                refreshLayout.finishRefresh();
                if (model.results.size() > 0) {
                    //有数据项
                    tv_no_data.setVisibility(View.GONE);
                    refreshLayout.setVisibility(View.VISIBLE);
                    adapter = new MeetingMySponsorListAdapter(model.results, MeetingMySponsorListActivity.this);
                    adapter.setListener(MeetingMySponsorListActivity.this);
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
     * 列表的子项点击事件
     *
     * @param recyclerView
     * @param pos
     * @param view
     * @param model
     */
    @Override
    public void onChildItemClick(RecyclerView recyclerView, int pos, View view, MeetingMySponsorModel model) {
        Intent intent = new Intent(this, MeetingNewDetailActivity.class);
        intent.putExtra("data", model.getId());
        startActivity(intent);

    }
}
