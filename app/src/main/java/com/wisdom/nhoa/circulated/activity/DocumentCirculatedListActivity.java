package com.wisdom.nhoa.circulated.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lzy.okgo.model.HttpParams;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.base.BaseActivity;
import com.wisdom.nhoa.circulated.adapter.DocumentCirculatedListAdapter;
import com.wisdom.nhoa.circulated.model.CirculatedListModel;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.U;
import com.wisdom.nhoa.util.http_util.HttpUtil;
import com.wisdom.nhoa.util.http_util.callback.BaseModel;
import com.wisdom.nhoa.util.http_util.callback.JsonCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.circulated
 * @class describe： 公文传阅列表
 * @time 2018/3/25 17:53
 * @change
 */

public class DocumentCirculatedListActivity extends BaseActivity {
    @BindView(R.id.circulated_listview)
    ListView circulatedListview;
    public static final String TAG = DocumentCirculatedListActivity.class.getSimpleName();

    @Override
    public void initViews() {
        getAndSetData();
        setTitle(R.string.circulated_title);
    }


    @Override
    public void setlayoutIds() {
        setContentView(R.layout.circulated_activity_circulated_list);
    }


    /**
     * 创建群的点击事件
     */
    @OnClick(R.id.circulated_tv_create_group)
    public void onViewClicked() {
        startActivityForResult(new Intent(this, CreateGroupActivity.class), ConstantString.REQUEST_CODE_REFRESH_DATA);
    }

    /**
     * 获取数据并设置在界面的方法
     */
    private void getAndSetData() {
        U.showLoadingDialog(this);
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(this).getAccess_token());
        HttpUtil.httpGet(ConstantUrl.DOCUMENT_CIRCLATED_LIST_URL, params, new JsonCallback<BaseModel<List<CirculatedListModel>>>() {
            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                U.closeLoadingDialog();
            }

            @Override
            public void onSuccess(final BaseModel<List<CirculatedListModel>> listBaseModel, Call call, Response response) {
                U.closeLoadingDialog();
                Log.i(TAG, "onSuccess: " + listBaseModel.results.toString());
                circulatedListview.setAdapter(new DocumentCirculatedListAdapter(DocumentCirculatedListActivity.this, listBaseModel.results));
                circulatedListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("data", listBaseModel.results.get(position));
                        Intent intent = new Intent(DocumentCirculatedListActivity.this, CirculateConversationActivity.class);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, ConstantString.REQUEST_CODE_REFRESH_DATA);
                    }
                });
            }
        });
    }

    /**
     * 创建完群返回到此页面后进行刷新页面
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstantString.REQUEST_CODE_REFRESH_DATA) {
            getAndSetData();
        }
    }
}
