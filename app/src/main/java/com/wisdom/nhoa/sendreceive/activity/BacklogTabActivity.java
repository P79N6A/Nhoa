package com.wisdom.nhoa.sendreceive.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import com.wisdom.nhoa.homepage.adapter.BacklogTabAdapter;
import com.wisdom.nhoa.sendreceive.model.SendManageModel;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.U;
import com.wisdom.nhoa.util.http_util.HttpUtil;
import com.wisdom.nhoa.util.http_util.callback.BaseModel;
import com.wisdom.nhoa.util.http_util.callback.JsonCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Response;

public class BacklogTabActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
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
    @BindView(R.id.tv_no_data)
    TextView tv_no_data;
    private String fileId;
    private List<SendManageModel> list;
    BacklogTabAdapter adapter;

    private int issend = 0;//0是发文1是收文

    @Override
    public void initViews() {
        setTitle("待办列表");
        if (getIntent() != null) {
            fileId = getIntent().getStringExtra("data");
        }
        rgTab.setOnCheckedChangeListener(this);
        rbRead.setChecked(true);
        lvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                if (0 == issend) {
                    intent = new Intent(BacklogTabActivity.this, SendIssueTodoDetailActivity.class);
                    intent.putExtra("insid", list.get(position).getInsid());
                    intent.putExtra("doctitle", list.get(position).getDoctitle());
                    intent.putExtra("docnumber", list.get(position).getDocnumber());
                } else {
                    intent = new Intent(BacklogTabActivity.this, ReceiveIssueTodoDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", list.get(position));
                    intent.putExtras(bundle);
                }

                startActivityForResult(intent, ConstantString.REQUEST_CODE_REFRESH_DATA);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ConstantString.REQUEST_CODE_REFRESH_DATA) {
            if (0 == issend) {
                getsend();
            } else {
                getReceive();
            }
        }
    }

    @Override
    public void setlayoutIds() {
        setContentView(R.layout.activity_backlog_tab);
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_read: {
                issend = 0;
                getsend();
                ll1.setBackgroundColor(getResources().getColor(R.color.color_647aea));
                ll2.setBackgroundColor(getResources().getColor(R.color.white));
            }
            break;
            case R.id.rb_un_read: {
                getReceive();
                issend = 1;
                ll2.setBackgroundColor(getResources().getColor(R.color.color_647aea));
                ll1.setBackgroundColor(getResources().getColor(R.color.white));
            }
            break;
            default: {
            }
        }
    }


    /**
     * 获得发文数据
     */

    private void getsend() {
        U.showLoadingDialog(this);
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(BacklogTabActivity.this).getAccess_token());
        params.put("listtype", ConstantString.SEND_RECEIVE_LIST_TYPE_SEND);
        params.put("doctype", ConstantString.SEND_RECEIVE_DOC_TYPE_SEND);
        params.put("pagenum", 1);
        HttpUtil.httpGet(ConstantUrl.RECEIVE_SEND_ISSUE_MANAGMENT, params, new JsonCallback<BaseModel<List<SendManageModel>>>() {
            @Override
            public void onSuccess(BaseModel<List<SendManageModel>> listBaseModel, Call call, Response response) {
                U.closeLoadingDialog();
                list = new ArrayList<>();
                list = listBaseModel.results;
                if (list.size() == 0) {
//                    DialogUtil.showConfirmDialog(BacklogActivity.this
//                            , "暂无事项", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    BacklogActivity.this.finish();
//                                }
//                            });
                    tv_no_data.setVisibility(View.VISIBLE);
                    lvData.setVisibility(View.GONE);
                } else {
                    tv_no_data.setVisibility(View.GONE);
                    lvData.setVisibility(View.VISIBLE);
                    adapter = new BacklogTabAdapter(BacklogTabActivity.this, list);
                    lvData.setAdapter(adapter);
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                U.closeLoadingDialog();
            }
        });
    }

    //获得收文数据
    private void getReceive() {
        // U.showLoadingDialog(this);
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(this).getAccess_token());
        params.put("listtype", ConstantString.SEND_RECEIVE_LIST_TYPE_SEND);
        params.put("doctype", ConstantString.SEND_RECEIVE_DOC_TYPE_RECEIVE);
        params.put("pagenum", "1");
        HttpUtil.httpGet(ConstantUrl.RECEIVE_SEND_ISSUE_MANAGMENT, params, new JsonCallback<BaseModel<List<SendManageModel>>>() {
            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                U.closeLoadingDialog();
            }

            @Override
            public void onSuccess(BaseModel<List<SendManageModel>> listBaseModel, Call call, Response response) {
                U.closeLoadingDialog();
                if (listBaseModel.results.size() == 0) {
//                    DialogUtil.showConfirmDialog(BacklogActivity.this
//                            , "暂无事项", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    BacklogActivity.this.finish();
//                                }
//                            });
                    tv_no_data.setVisibility(View.VISIBLE);
                    lvData.setVisibility(View.GONE);
                } else {
                    tv_no_data.setVisibility(View.GONE);
                    lvData.setVisibility(View.VISIBLE);
                    list = new ArrayList<>();
                    list = listBaseModel.results;
                    adapter = new BacklogTabAdapter(BacklogTabActivity.this, list);
//                    adapter.setRecevice(recrvicelist);
                    lvData.setAdapter(adapter);
                }
            }
        });
    }
}