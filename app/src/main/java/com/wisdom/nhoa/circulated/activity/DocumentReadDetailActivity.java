package com.wisdom.nhoa.circulated.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.base.BaseActivity;
import com.wisdom.nhoa.circulated.adapter.DocumentDetailAdapter;
import com.wisdom.nhoa.circulated.model.DocumentDetailModel;
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
 * @class package：com.wisdom.nhoa.circulated.activity
 * @class describe：对话页面的右侧气泡下面的  详情页面
 * @time 2018/3/27 16:42
 * @change
 */

public class DocumentReadDetailActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    @BindView(R.id.rb_read)
    RadioButton rbRead;
    @BindView(R.id.rb_un_read)
    RadioButton rbUnRead;
    @BindView(R.id.rg_tab)
    RadioGroup rgTab;
    @BindView(R.id.ll_1)
    LinearLayout ll1;
    @BindView(R.id.ll_2)
    LinearLayout ll2;
    @BindView(R.id.lv_data)
    ListView lvData;
    @BindView(R.id.tv_hint)
    TextView tv_hint;
    private String fileId;

    @Override
    public void initViews() {
        setTitle("详情");
        if (getIntent() != null) {
            fileId = getIntent().getStringExtra("data");
        }
        rgTab.setOnCheckedChangeListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        rbUnRead.setChecked(true);
        rbRead.setChecked(true);
    }

    @Override
    public void setlayoutIds() {
        setContentView(R.layout.circulated_activity_document_read_detail);
    }


    /**
     * 根据标示查找数据项，并将查找的值设置到界面
     *
     * @param downloadflag
     */
    private void getAndSetData(final String downloadflag) {
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(DocumentReadDetailActivity.this)
                .getAccess_token());
        params.put("file_id", fileId);
        params.put("download_flag", downloadflag);
        HttpUtil.httpGet(ConstantUrl.DOCUMENT_DOWNLOAD_LOG, params, new JsonCallback<BaseModel<List<DocumentDetailModel>>>() {
            @Override
            public void onSuccess(BaseModel<List<DocumentDetailModel>> documentDetailModels, Call call, Response response) {
                if (documentDetailModels.results.size() > 0) {
                    lvData.setVisibility(View.VISIBLE);
                    tv_hint.setVisibility(View.GONE);
                    DocumentDetailAdapter adapter=new DocumentDetailAdapter(DocumentReadDetailActivity.this, documentDetailModels.results);
                    lvData.setAdapter(adapter);
                    if (downloadflag.equals(ConstantString.DOCUMENT_DOWNLOAD_TRUE)) {
                        rbRead.setText("已读(" + documentDetailModels.results.size() + ")");
                    } else {
                        rbUnRead.setText("未读(" + documentDetailModels.results.size() + ")");
                    }
                } else {
                    lvData.setVisibility(View.GONE);
                    tv_hint.setVisibility(View.VISIBLE);
                }


            }
        });
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_read: {

                getAndSetData(ConstantString.DOCUMENT_DOWNLOAD_TRUE);
                ll1.setBackgroundColor(getResources().getColor(R.color.color_647aea));
                ll2.setBackgroundColor(getResources().getColor(R.color.white));
            }
            break;
            case R.id.rb_un_read: {

                getAndSetData(ConstantString.DOCUMENT_DOWNLOAD_FALSE);
                ll2.setBackgroundColor(getResources().getColor(R.color.color_647aea));
                ll1.setBackgroundColor(getResources().getColor(R.color.white));
            }
            break;
            default: {
            }
        }
    }

    /**
     * 清空页面数据防止发生穿位置
     */
    private void setEmptyData() {

    }
}
