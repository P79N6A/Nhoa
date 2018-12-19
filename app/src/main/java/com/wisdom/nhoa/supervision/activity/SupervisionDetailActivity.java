package com.wisdom.nhoa.supervision.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.base.BaseActivity;
import com.wisdom.nhoa.supervision.adapter.SupervisionDetialAdapter;
import com.wisdom.nhoa.supervision.model.SupervisionModel;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.ToastUtil;
import com.wisdom.nhoa.util.U;
import com.wisdom.nhoa.util.http_util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class SupervisionDetailActivity extends BaseActivity {
    @BindView(R.id.tv_supervision_name)
    TextView tv_supervision_name;
    @BindView(R.id.tv_begin_time)
    TextView tv_begin_time;
    @BindView(R.id.tv_end_time)
    TextView tv_end_time;
    @BindView(R.id.tv_supervision_content)
    TextView tv_supervision_content;
    @BindView(R.id.rv_supervision_list)
    RecyclerView rv_supervision_list;
    @BindView(R.id.bt_edit_supervision)
    Button bt_edit_supervision;
    @BindView(R.id.head_right_iv)
    ImageView head_right_iv;
    SupervisionModel model;
    private String supervisionid;

    @Override
    public void initViews() {
        context = this;
        setTitle("督办事项");
        head_right_iv.setImageResource(R.mipmap.delete);
        head_right_iv.setVisibility(View.VISIBLE);
        if (getIntent() != null) {
            model = (SupervisionModel) getIntent().getExtras().getSerializable("data");
            supervisionid = getIntent().getStringExtra("supervisionid");
        }
        tv_supervision_name.setText(model.getName());
        tv_begin_time.setText(model.getBegintime());
        tv_end_time.setText(model.getEndtime());
        tv_supervision_content.setText(model.getContent());
        rv_supervision_list.setLayoutManager(new LinearLayoutManager(context));
        rv_supervision_list.setItemAnimator(new DefaultItemAnimator());
        SupervisionDetialAdapter adapter = new SupervisionDetialAdapter(context, model.getChild());
        //跳转传递上一页面的所有数据
        adapter.setItemClickListener(new SupervisionDetialAdapter.onItemClickListener() {
            @Override
            public void onItemClicked(int position, SupervisionModel.ChildBean childBean) {
                Intent intent = new Intent(SupervisionDetailActivity.this, BeSupervisedPersonFeedBackListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", model);
                intent.putExtras(bundle);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
        if ("".equals(model.getStatus())) {
            head_right_iv.setVisibility(View.VISIBLE);
            bt_edit_supervision.setVisibility(View.VISIBLE);
        } else {
            head_right_iv.setVisibility(View.INVISIBLE);
            bt_edit_supervision.setVisibility(View.GONE);
        }
        rv_supervision_list.setAdapter(adapter);
        bt_edit_supervision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //转跳到编辑页面
                Intent intent = new Intent(SupervisionDetailActivity.this, EditSupervisionActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", model);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void setlayoutIds() {
        setContentView(R.layout.activity_supervision_detail);
    }


    /**
     * 页面右上角删除操作
     */
    @OnClick(R.id.head_right_iv)
    public void onViewClicked() {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("确定删除该督办信息？")
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //无反馈信息
                        deleteSupervesioninfo();

                    }
                })
                .create().show();
    }

    /**
     * 执行删除操作的方法
     */
    private void deleteSupervesioninfo() {
        U.showLoadingDialog(this);
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(context).getAccess_token());
        params.put("supervisionid", model.getId());
        HttpUtil.httpGet(ConstantUrl.DELETE_SUPERVERSION_INFO, params, new StringCallback() {
            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                U.closeDialog();
            }

            @Override
            public void onSuccess(String s, Call call, Response response) {
                U.closeDialog();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getInt("error_code") == 0) {
                        ToastUtil.showToast("删除成功");
                        SupervisionDetailActivity.this.finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }
}
