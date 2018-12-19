package com.wisdom.nhoa.approval.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ExpandableListView;

import com.lzy.okgo.model.HttpParams;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.approval.adapter.BacklogELAdapter;
import com.wisdom.nhoa.base.BaseActivity;
import com.wisdom.nhoa.sendreceive.activity.SendIssueTodoDetailActivity;
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

/**
 * 首页上面的代办按钮
 * 收文代办和发文代办
 */
public class BacklogActivity extends BaseActivity {

    @BindView(R.id.el_backlog_missive)
    ExpandableListView elBacklogMissive;
    private List<SendManageModel> sendlist = new ArrayList<>();
    BacklogELAdapter adapter;
    List<SendManageModel> recrvicelist = new ArrayList();

    @Override
    public void initViews() {
        setTitle("待办");
        adapter = new BacklogELAdapter(BacklogActivity.this);
        getsend();
        getReceive();

        elBacklogMissive.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Intent intent = new Intent(BacklogActivity.this, SendIssueTodoDetailActivity.class);
                if (groupPosition == 0) {
                    intent.putExtra("insid", sendlist.get(childPosition).getInsid());
                    intent.putExtra("doctitle", sendlist.get(childPosition).getDoctitle());
                    intent.putExtra("docnumber", sendlist.get(childPosition).getDocnumber());
                } else {
                    intent.putExtra("insid", recrvicelist.get(childPosition).getInsid());
                    intent.putExtra("doctitle", recrvicelist.get(childPosition).getDoctitle());
                    intent.putExtra("docnumber", recrvicelist.get(childPosition).getDocnumber());
                }
                // TODO: 2018/3/12
                startActivity(intent);
                return true;
            }
        });
    }

    @Override
    public void setlayoutIds() {
        setContentView(R.layout.activity_backlog);
    }


    /**
     * 获得发文数据
     */

    private void getsend() {
        U.showLoadingDialog(this);
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(BacklogActivity.this).getAccess_token());
        params.put("listtype", ConstantString.SEND_RECEIVE_LIST_TYPE_SEND);
        params.put("doctype", ConstantString.SEND_RECEIVE_DOC_TYPE_SEND);
        params.put("pagenum", 1);
        HttpUtil.httpGet(ConstantUrl.RECEIVE_SEND_ISSUE_MANAGMENT, params, new JsonCallback<BaseModel<List<SendManageModel>>>() {
            @Override
            public void onSuccess(BaseModel<List<SendManageModel>> listBaseModel, Call call, Response response) {
                U.closeLoadingDialog();
                sendlist = listBaseModel.results;
                if (sendlist.size() == 0) {
//                    DialogUtil.showConfirmDialog(BacklogActivity.this
//                            , "暂无事项", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    BacklogActivity.this.finish();
//                                }
//                            });
                } else {
                    adapter.setSend(sendlist);
                    elBacklogMissive.setAdapter(adapter);
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
                } else {
                    recrvicelist = listBaseModel.results;
                    adapter.setRecevice(recrvicelist);
                    elBacklogMissive.setAdapter(adapter);
                }
            }
        });
    }


}
