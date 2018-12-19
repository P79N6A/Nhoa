package com.wisdom.nhoa.approval.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.approval.adapter.ChooseCopyToAdapter;
import com.wisdom.nhoa.approval.model.CopyToSelectedModel;
import com.wisdom.nhoa.approval.model.OrganizationModel;
import com.wisdom.nhoa.base.BaseActivity;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.ToastUtil;
import com.wisdom.nhoa.util.U;
import com.wisdom.nhoa.util.http_util.HttpUtil;
import com.wisdom.nhoa.util.http_util.callback.BaseModel;
import com.wisdom.nhoa.util.http_util.callback.JsonCallback;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by hanxuefeng on 2018/7/24.
 * 新版本选择抄送对象的页面
 */

public class ChooseCopyToActivity extends BaseActivity {
    @BindView(R.id.lv_participant)
    ListView lvParticipant;
    @BindView(R.id.bt_confirm)
    Button btConfirm;
    private List<Map<Integer, String>> ischeck;
    private List<OrganizationModel> organizationModelList;
    private CopyToSelectedModel copyToSelectedModel;//用来判断用户之前勾选的是什么

    @Override
    public void setlayoutIds() {
        setContentView(R.layout.activity_choose_copy_to_new);
    }

    @Override
    public void initViews() {
        setTitle(R.string.copy_to);
        if (getIntent() != null) {
            copyToSelectedModel = (CopyToSelectedModel) getIntent().getExtras().getSerializable("data");
            if (copyToSelectedModel != null) {
//                回传数据源不为空证明是用户已经选择过，想进行勾选结果的修改操作。
                int totalCount = 0;
                List<Map<Integer, String>> checkState = copyToSelectedModel.getIscheck();
                for (int i = 0; i < checkState.size(); i++) {
                    for (int j = 0; j < checkState.get(i).size(); j++) {
                        if ("true".equals(checkState.get(i).get(j))) {
                            totalCount++;
                        }
                    }
                }
                btConfirm.setText("确定（" + totalCount + "）");
            }

        }
        getDataList();
    }

    /**
     * 通过网络，获取数据源
     */
    private void getDataList() {
        U.showLoadingDialog(this);
        HttpParams httpParams = new HttpParams();
        httpParams.put("appkey", ConstantString.APP_KEY);
        httpParams.put("access_token", SharedPreferenceUtil.getUserInfo(this).getAccess_token());
        HttpUtil.httpGet(ConstantUrl.GET_PEOPLE_TREE_URL, httpParams, new JsonCallback<BaseModel<List<OrganizationModel>>>() {
            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                U.closeLoadingDialog();
            }

            @Override
            public void onSuccess(final BaseModel<List<OrganizationModel>> listBaseModel, Call call, Response response) {
                U.closeLoadingDialog();
                if (listBaseModel.results.size() > 0) {
                    organizationModelList = listBaseModel.results;
                    ChooseCopyToAdapter chooseCopyToAdapter = new ChooseCopyToAdapter(ChooseCopyToActivity.this, listBaseModel.results, copyToSelectedModel);
                    chooseCopyToAdapter.setOnChooseCountChangeListener(new ChooseCopyToAdapter.OnChooseCountChangeListener() {
                        @Override
                        public void OnChooseCountChange(int groupPosition, int chooseCount, int itemChooseCount, List<Map<Integer, String>> ischeck) {
                            TextView tv_num = lvParticipant.getChildAt(groupPosition).findViewById(R.id.tv_choose_num);
                            tv_num.setText(itemChooseCount + "/");
                            btConfirm.setText("确定（" + chooseCount + "）");
                            ChooseCopyToActivity.this.ischeck = ischeck;
                        }
                    });
                    lvParticipant.setAdapter(chooseCopyToAdapter);
                } else {
                    ToastUtil.showToast("暂无数据");
                }
            }
        });
    }


    /**
     * 页面下方的确定按钮点击事件
     */
    @OnClick(R.id.bt_confirm)
    public void onViewClicked() {
        CopyToSelectedModel model = new CopyToSelectedModel();
        String ids = "";
        String names = "";
        if (organizationModelList != null && ischeck != null) {
            //遍历列表，找到用户选中的抄送人，然后进行拼接。
            for (int i = 0; i < ischeck.size(); i++) {
                for (int j = 0; j < organizationModelList.get(i).getChiled().size(); j++) {
                    if ("true".equals(ischeck.get(i).get(j))) {
                        ids += organizationModelList.get(i).getChiled().get(j).getId() + ",";
                        names += organizationModelList.get(i).getChiled().get(j).getName() + ",";
                    }
                }

            }
            model.setId(ids.substring(0, ids.length() - 1));
            model.setIscheck(ischeck);
            model.setName(names.substring(0, names.length() - 1));
            //封装数据，作为回显使用
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", model);
            Intent intent = new Intent();
            intent.putExtras(bundle);
            setResult(ConstantString.COPY_TO_RESULT_CODE, intent);
            ChooseCopyToActivity.this.finish();
        } else {
            ToastUtil.showToast("暂无选中项");
        }
    }
}
