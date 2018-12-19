package com.wisdom.nhoa.homepage.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.base.BaseActivity;
import com.wisdom.nhoa.homepage.adapter.DraftListAdapter;
import com.wisdom.nhoa.homepage.model.DraftListModel;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.http_util.HttpUtil;
import com.wisdom.nhoa.util.http_util.callback.BaseModel;
import com.wisdom.nhoa.util.http_util.callback.JsonCallback;

import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Response;

public class DraftActivity extends BaseActivity {
    @BindView(R.id.rv_draf_list)
    RecyclerView draf_recycle;
    @BindView(R.id.tv_no_data)
    TextView tv_no_data;
    DraftListAdapter adapter;

    @Override
    public void initViews() {
        setTitle("草稿箱");
        initView();
        getData();
    }

    private void initView() {
        draf_recycle.setLayoutManager(new LinearLayoutManager(this));

    }

    private void getData() {
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(this).getAccess_token());
        params.put("pagenum", 1);
        HttpUtil.httpGet(ConstantUrl.GET_DRAFTLIST, params, new JsonCallback<BaseModel<List<DraftListModel>>>() {
            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);

            }

            @Override
            public void onSuccess(BaseModel<List<DraftListModel>> listBaseModel, Call call, Response response) {

                if (listBaseModel.results.size() > 0) {

                tv_no_data.setVisibility(View.GONE);
                adapter = new DraftListAdapter(listBaseModel.results);
                adapter.setOnItemClickListener(new DraftListAdapter.OnClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }
                });
                draf_recycle.setAdapter(adapter);
                } else {
                    tv_no_data.setVisibility(View.VISIBLE);
                    draf_recycle.setVisibility(View.GONE);
                }


            }


        });


    }

    @Override
    public void setlayoutIds() {
        setContentView(R.layout.activity_draf);
    }


}
