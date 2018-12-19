package com.wisdom.nhoa.homepage.activity;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.base.BaseActivity;
import com.wisdom.nhoa.homepage.model.NoticeDetailedModel;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.http_util.HttpUtil;
import com.wisdom.nhoa.util.http_util.callback.BaseModel;
import com.wisdom.nhoa.util.http_util.callback.JsonCallback;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 公告详情页面
 */
public class NoticeDetailedActivity extends BaseActivity {
    @BindView(R.id.notice_d_title)
    TextView notice_d_title;
    @BindView(R.id.notice_d_department)
    TextView notice_d_department;
    @BindView(R.id.notice_d_content)
    TextView notice_d_content;

    @Override
    public void initViews() {
        setTitle(R.string.notice_detailed_title);
        getDetialedC();
    }

    private void getDetialedC() {



        String notice_id=getIntent().getStringExtra("notice_id");
        HttpParams params = new HttpParams();
        params.put("noticeid", notice_id);
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(this).getAccess_token());
        HttpUtil.httpGet(ConstantUrl.NOTICE_DETIALED_URL, params, new JsonCallback<BaseModel< NoticeDetailedModel>>() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSuccess(BaseModel<NoticeDetailedModel> noticeBaseModel, Call call, Response response) {
                Log.v("notice_detailed",noticeBaseModel.toString());
                CharSequence charSequence = null;
                if (noticeBaseModel.results.getNoticecontent()!=null){
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    charSequence = Html.fromHtml(noticeBaseModel.results.getNoticecontent(),Html.FROM_HTML_MODE_LEGACY);
                } else {
                    charSequence = Html.fromHtml(noticeBaseModel.results.getNoticecontent());
                }}
                notice_d_title.setText(""+noticeBaseModel.results.getNoticetitle());
                notice_d_department.setText(""+noticeBaseModel.results.getNoticedep());
                notice_d_content.setText(""+charSequence);

            }
        });
    }

    @Override
    public void setlayoutIds() {
        setContentView(R.layout.activity_notice_detailed);
    }
}
