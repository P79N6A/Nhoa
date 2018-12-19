package com.wisdom.nhoa.homepage.activity;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.base.BaseActivity;
import com.wisdom.nhoa.circulated.activity.FilePreviewActivity;
import com.wisdom.nhoa.homepage.adapter.ToBeReadAdapter;
import com.wisdom.nhoa.homepage.model.ToBeReadModel;
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
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.homepage.activity
 * @class describe： 首页待阅读
 * @time 2018/4/4 10:59
 * @change
 */

public class ToBeReadActivity extends BaseActivity {
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.tv_preview_hint)
    TextView tvPreviewHint;

    private ToBeReadAdapter adapter;

    @Override
    public void initViews() {
        setTitle("待阅列表");
        getAndSetData();
    }


    @Override
    public void setlayoutIds() {
        setContentView(R.layout.homepage_activity_tobe_read);
    }


    /**
     * 获取数据并设置界面
     */
    private void getAndSetData() {
        U.showLoadingDialog(this);
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(this).getAccess_token());
        params.put("pagenum", "1");
        HttpUtil.httpGet(ConstantUrl.TOBE_READ_URL, params, new JsonCallback<BaseModel<List<ToBeReadModel>>>() {
            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                U.closeLoadingDialog();
                tvPreviewHint.setVisibility(View.VISIBLE);
                recycler.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(BaseModel<List<ToBeReadModel>> listBaseModel, Call call, Response response) {
                U.closeLoadingDialog();
                if (listBaseModel.results.size() > 0) {
                    tvPreviewHint.setVisibility(View.GONE);
                    recycler.setVisibility(View.VISIBLE);
                    adapter = new ToBeReadAdapter(ToBeReadActivity.this, listBaseModel.results);
                    recycler.setAdapter(adapter);
                    adapter.setOnToBeReadItemClickListener(new ToBeReadAdapter.OnToBeReadItemClickListener() {
                        @Override
                        public void onToBeReadItemClicked(RecyclerView recyclerView, int pos, View clickedView, ToBeReadModel toBeReadModel) {
//                            Intent mIntent = new Intent(ToBeReadActivity.this, ToBeReadDetailedActivity.class);
//                            mIntent.putExtra("notice_id", toBeReadModel.getNoticeid());
//                            startActivityForResult(mIntent, ConstantString.REQUEST_CODE_REFRESH_DATA);
                            Log.e("待阅预览路径", ConstantUrl.BASE_URL + toBeReadModel.getDocUrl());
                            FilePreviewActivity.show(context
                                    , ConstantUrl.BASE_URL + toBeReadModel.getDocUrl().replaceAll(" ", "")
                                    , toBeReadModel.getNoticeid());
                        }
                    });
                } else {
                    tvPreviewHint.setVisibility(View.VISIBLE);
                    recycler.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstantString.REQUEST_CODE_REFRESH_DATA) {
            //刷新界面数据
            getAndSetData();
        }
    }
}
