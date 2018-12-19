package com.wisdom.nhoa.supervision.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.base.BaseActivity;
import com.wisdom.nhoa.supervision.adapter.BeSupervisedPersonFeedBackListAdapter;
import com.wisdom.nhoa.supervision.model.BeSupervisedPersonFeedBackModel;
import com.wisdom.nhoa.supervision.model.SupervisionModel;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.ToastUtil;
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
 * @class package：com.wisdom.nhoa.supervision.activity
 * @class describe：督办/被督办人反馈信息 列表
 * @time 2018/7/26 11:39
 * @change
 */
public class BeSupervisedPersonFeedBackListActivity extends BaseActivity {
    @BindView(R.id.head_parent)
    RelativeLayout headParent;
    @BindView(R.id.lv_feedback_list)
    ListView lvFeedbackList;
    @BindView(R.id.tv_confirm)
    TextView tvFeedBack;
    @BindView(R.id.tv_no_data)
    TextView no_data;
    private SupervisionModel supervisionModel = null;
    int position;
    private BeSupervisedPersonFeedBackListAdapter personFeedBackListAdapter;

    @Override
    public void setlayoutIds() {
        setContentView(R.layout.activity_be_supervised_person_feed_back);
    }

    @Override
    public void initViews() {
        setTitle(R.string.superversion_feed_back_title);
        //从上级页面获取数据
        if (getIntent() != null) {
            position = getIntent().getIntExtra("position", 0);
            supervisionModel = (SupervisionModel) getIntent().getExtras().getSerializable("data");
        }
        //根据数据状态判断反馈信息按钮是否展示出来
        if (supervisionModel != null) {
            //为被督办人，且事件进度小于100时候显示最下方按钮
            if (SharedPreferenceUtil.getUserInfo(this).getUid().equals(supervisionModel.getChild().get(position).getUserid())
                    && Float.parseFloat(supervisionModel.getPercentage()) < 100f
                    ) {
                tvFeedBack.setVisibility(View.VISIBLE);
            } else {
                tvFeedBack.setVisibility(View.GONE);
            }
            //根据数据控制页面控件展示
            if (tvFeedBack.getVisibility() == View.GONE) {
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                lvFeedbackList.setLayoutParams(params);
            }
            getData(supervisionModel.getChild().get(position).getAddendumid()
                    , supervisionModel.getChild().get(position).getUserid());

        } else {
            ToastUtil.showToast("获取数据失败，请重试");
        }
    }


    /**
     * 请求接口，获得页面数据
     */
    private void getData(String addendumid, String userid) {
        U.showLoadingDialog(this);
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(this).getAccess_token());
        params.put("addendumid", addendumid);
        params.put("userid", userid);
        HttpUtil.httpGet(ConstantUrl.BE_SUPERVISED_PERSON_FEED_BACK_LIST, params, new JsonCallback<BaseModel<List<BeSupervisedPersonFeedBackModel>>>() {
            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                U.closeLoadingDialog();
            }

            @Override
            public void onSuccess(BaseModel<List<BeSupervisedPersonFeedBackModel>> listBaseModel, Call call, Response response) {
                U.closeLoadingDialog();
                if (listBaseModel.results.size() > 0) {
                    no_data.setVisibility(View.GONE);
                    personFeedBackListAdapter = new BeSupervisedPersonFeedBackListAdapter(
                            BeSupervisedPersonFeedBackListActivity.this
                            , listBaseModel.results
                    );
                    lvFeedbackList.setAdapter(personFeedBackListAdapter);
                } else {
                    ToastUtil.showToast("暂无数据");
                    no_data.setVisibility(View.VISIBLE);
//                    BeSupervisedPersonFeedBackListActivity.this.finish();
                }
            }
        });
    }

    /**
     * 页面下方的“反馈信息点击按钮”
     */
    @OnClick(R.id.tv_confirm)
    public void onViewClicked() {
        if (supervisionModel != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", supervisionModel);
            Intent intent = new Intent(this, AddFeedBackActivity.class);
            intent.putExtras(bundle);
            intent.putExtra("position", position);
            startActivityForResult(intent, 0x119);
        } else {
            ToastUtil.showToast("获取数据失败，请重试");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x119) {
            //添加完反馈回到此列表页面进行刷新操作
            getData(supervisionModel.getChild().get(position).getAddendumid()
                    , supervisionModel.getChild().get(position).getUserid());
        }
    }
}
