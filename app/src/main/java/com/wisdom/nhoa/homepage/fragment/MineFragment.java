package com.wisdom.nhoa.homepage.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.android.pushservice.PushManager;
import com.wisdom.nhoa.AppApplication;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.base.ActivityManager;
import com.wisdom.nhoa.homepage.activity.HomePageActivity;
import com.wisdom.nhoa.homepage.model.MsgListModel;
import com.wisdom.nhoa.mine.activity.AboutActivity;
import com.wisdom.nhoa.mine.activity.ChangePasswordActivity;
import com.wisdom.nhoa.mine.activity.ChangePhoneActivity;
import com.wisdom.nhoa.mine.activity.LoginActivity;
import com.wisdom.nhoa.util.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * author：
 * date:
 */

public class MineFragment extends Fragment {
    @BindView(R.id.comm_head_title)
    TextView commHeadTitle;
    @BindView(R.id.head_back_iv)
    ImageView headBackIv;
    @BindView(R.id.mine_head_image)
    ImageView mineHeadImage;
    @BindView(R.id.mine_department_name)
    TextView mineDepartmentName;
    @BindView(R.id.mine_rl_update_phone)
    RelativeLayout mineRlUpdatePhone;
    @BindView(R.id.mine_rl_update_psw)
    RelativeLayout mineRlUpdatePsw;
    @BindView(R.id.mine_rl_about)
    RelativeLayout mineRlAbout;
    @BindView(R.id.mine_department)
    TextView mine_department;
    @BindView(R.id.mine_simple_name)
    TextView mine_job;
    @BindView(R.id.mine_role_name)
    TextView mine_role_name;
    Unbinder unbinder;
    private Intent mintent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homepage_fragment_mine, container, false);
        unbinder = ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    /**
     * 初始化页面内相关控件
     */
    private void initViews() {
        headBackIv.setVisibility(View.GONE);
        commHeadTitle.setText(R.string.mine_title);
        mineDepartmentName.setText(SharedPreferenceUtil.getUserInfo(getContext()).getUser_name());
        mine_department.setText(SharedPreferenceUtil.getUserInfo(getContext()).getDepartment_name());
        mine_job.setText(SharedPreferenceUtil.getUserInfo(getContext()).getSimple_name());
        if (!SharedPreferenceUtil.getUserInfo(getContext()).getRole_name().contains("暂无")) {
            mine_role_name.setText(SharedPreferenceUtil.getUserInfo(getContext()).getRole_name());
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    /**
     * 页面内点击事件
     *
     * @param view
     */
    @OnClick({R.id.mine_rl_update_phone, R.id.mine_rl_update_psw, R.id.mine_rl_about, R.id.mine_btn_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mine_rl_update_phone: {
                //修改手机
                startActivity(new Intent(getActivity(), ChangePhoneActivity.class));
            }
            break;
            case R.id.mine_rl_update_psw: {
                //修改密码
                startActivity(new Intent(getActivity(), ChangePasswordActivity.class));
            }
            break;
            case R.id.mine_rl_about: {
                //关于
                startActivity(new Intent(getActivity(), AboutActivity.class));
            }
            break;
            case R.id.mine_btn_logout: {

                final Dialog alertDialog = new AlertDialog.Builder(getActivity()).setTitle("退出").setMessage("确认退出账号并注销？")
                        .setPositiveButton("确认退出", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
//                                AppApplication.getDaosession().getDao(MsgListModel.class).deleteAll();
                                //发送
//                                Intent intent = new Intent();
//                                intent.setAction(HomePageActivity.UPDATAMSGCOUNTS);
//                                getActivity().sendBroadcast(intent);
                                //退出按钮点击事件
                                //存储用户信息到sp文件
                                NotificationManager   notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                                notificationManager.cancelAll();
                                SharedPreferenceUtil.getConfig(getContext()).clearSharePrefernence();
                                //断开百度推送服务器的推送
                                PushManager.stopWork(getContext());
                                if (SharedPreferenceUtil.getUserInfo(AppApplication.getInstance()) != null) {
                                    List<String> tags = new ArrayList<>();
                                    tags.add(SharedPreferenceUtil.getUserInfo(AppApplication.getInstance()).getUid());
                                    PushManager.delTags(AppApplication.getInstance(), tags);
                                }
                                startActivity(new Intent(getContext(), LoginActivity.class));
                                ActivityManager.getActivityManagerInstance().clearAllActivity();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).create();
                alertDialog.show();

            }
            break;
        }
    }
}
