package com.wisdom.nhoa.homepage.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.base.BaseActivity;
import com.wisdom.nhoa.homepage.adapter.NoticeAdapter;
import com.wisdom.nhoa.homepage.model.NoticeModel;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.http_util.HttpUtil;
import com.wisdom.nhoa.util.http_util.callback.BaseModel;
import com.wisdom.nhoa.util.http_util.callback.JsonCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.homepage.activity
 * @class describe：公告页面
 * @time 2018/3/25 14:21
 * @change
 */

public class NoticeActivity extends BaseActivity {
    @BindView(R.id.notice_listview)
    ListView noticeListview;
    public static final String TAG = NoticeActivity.class.getSimpleName();
    @BindView(R.id.tv_hint)
    TextView tvHint;

    private Intent mIntent;

    @Override
    public void initViews() {
        getAndSetData();
        setTitle(R.string.notice_detailed_title);

    }


    @Override
    public void setlayoutIds() {
        setContentView(R.layout.notice_activity_notice);
    }

    /**
     * 请求接口，获得公告数据并更新界面
     */
    private void getAndSetData() {
        HttpParams params = new HttpParams();
        params.put("userid", SharedPreferenceUtil.getUserInfo(this).getUid());
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(this).getAccess_token());
        Log.i(TAG, "getAndSetData: " + SharedPreferenceUtil.getUserInfo(this).toString());
        HttpUtil.httpGet(ConstantUrl.NOTICE_URL, params, new JsonCallback<BaseModel<List<NoticeModel>>>() {
            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                tvHint.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(final BaseModel<List<NoticeModel>> listBaseModel, Call call, Response response) {
                if (listBaseModel.results != null) {
                    if (listBaseModel.results.size() == 0) {
                        tvHint.setVisibility(View.VISIBLE);
                    } else {
                        tvHint.setVisibility(View.GONE);
                    }
                }else{
                    tvHint.setVisibility(View.VISIBLE);
                }
                noticeListview.setAdapter(new NoticeAdapter(NoticeActivity.this, listBaseModel.results));
                noticeListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mIntent = new Intent(NoticeActivity.this, NoticeDetailedActivity.class);
                        mIntent.putExtra("notice_id", listBaseModel.results.get(position).getNoticeid());
                        startActivity(mIntent);
                    }
                });
            }
        });


    }


}
