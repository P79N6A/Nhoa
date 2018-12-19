package com.wisdom.nhoa.sendreceive.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.base.BaseActivity;
import com.wisdom.nhoa.base.BroadCastManager;
import com.wisdom.nhoa.sendreceive.adapter.ApprovalFragmentAdapter;
import com.wisdom.nhoa.sendreceive.fragment.AttachFragment;
import com.wisdom.nhoa.sendreceive.fragment.FileContentFragment;
import com.wisdom.nhoa.sendreceive.fragment.FileTodoListFragment;
import com.wisdom.nhoa.sendreceive.model.ToDoModel;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.ToastUtil;
import com.wisdom.nhoa.util.U;
import com.wisdom.nhoa.util.http_util.HttpUtil;
import com.wisdom.nhoa.util.http_util.callback.BaseModel;
import com.wisdom.nhoa.util.http_util.callback.JsonCallback;
import com.wisdom.nhoa.widget.AlterIssueDialog;
import com.wisdom.nhoa.widget.AutoMesureViewPager;
import com.wisdom.nhoa.widget.RefuseIssueDialog;
import com.wisdom.nhoa.widget.ScrollViewWithScrollListener;
import com.wisdom.nhoa.widget.paint.WritePadDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.approval.activity
 * @class describe：
 * @time 2018/3/12 15:23
 * @change
 */


public class SendIssueTodoDetailActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, ScrollViewWithScrollListener.ScrollViewListener, ViewPager.OnPageChangeListener {

    public static final String TAG = SendIssueTodoDetailActivity.class.getSimpleName();
    @BindView(R.id.approval_sv)
    ScrollViewWithScrollListener approvalSv;
    @BindView(R.id.approval_rb_1)
    RadioButton approvalRb1;
    @BindView(R.id.approval_rb_2)
    RadioButton approvalRb2;
    @BindView(R.id.approval_rb_3)
    RadioButton approvalRb3;
    @BindView(R.id.approval_rb_1_top)
    RadioButton approvalRb1Top;
    @BindView(R.id.approval_rb_2_top)
    RadioButton approvalRb2Top;
    @BindView(R.id.approval_rb_3_top)
    RadioButton approvalRb3Top;
    @BindView(R.id.approval_rg)
    RadioGroup approvalRg;
    @BindView(R.id.approval_rg_top)
    RadioGroup approvalRgTop;
    @BindView(R.id.approval_rb_ll_1)
    LinearLayout approvalRbLl1;
    @BindView(R.id.approval_rb_ll_2)
    LinearLayout approvalRbLl2;
    @BindView(R.id.approval_rb_ll_3)
    LinearLayout approvalRbLl3;
    @BindView(R.id.approval_rb_ll_2_top)
    LinearLayout approvalRbLl2Top;
    @BindView(R.id.approval_rb_ll_3_top)
    LinearLayout approvalRbLl3Top;
    @BindView(R.id.approval_ll_collection_top)
    LinearLayout approvalLlCollectionTop;
    @BindView(R.id.approval_rb_ll_1_top)
    LinearLayout approvalRbLl1Top;
    @BindView(R.id.approval_ll_top_items)
    LinearLayout llTopItems;
    @BindView(R.id.approval_viewpager)
    AutoMesureViewPager approvalViewpager;
    @BindView(R.id.tv_todo_file_num)
    TextView tvTodoFileNum;
    @BindView(R.id.tv_todo_file_title)
    TextView tvTodoFileTitle;
    @BindView(R.id.tv_todo_file_process)
    TextView tvTodoFileProcess;
    @BindView(R.id.tv_todo_file_author)
    TextView tvTodoFileAuthor;
    @BindView(R.id.tv_todo_file_date)
    TextView tvTodoFileDate;
    @BindView(R.id.approval_tv_todo_send)
    TextView approval_tv_todo_send;
//    @BindView(R.id.approval_ll_todo_menu)
//    LinearLayout approvalLlTodoMenu;

    private String insid = "";
    private String docnumber = "";
    private String doctitle = "";

    private ToDoModel toDoModel;

    // 文件处理
    private FileTodoListFragment fileTodoListFragment = new FileTodoListFragment();
    // 公文正文
    private FileContentFragment fileContentFragment = new FileContentFragment();
    // 附件材料
    private AttachFragment attachFragment = new AttachFragment();
    private List<Fragment> fragmentList = new ArrayList<>();

    private MyReceiver finishreceiver;//关闭activity广播
    private WritePadDialog mWritePadDialog;//手写板
    private Boolean hasComplete = false;//判断该事项是否有办结按钮，true：有

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void initViews() {
        setTitle(R.string.approval_approval_todo_title);
        approval_tv_todo_send.setText(R.string.approval_approval_todo_send);
        if (getIntent() != null) {
            insid = getIntent().getStringExtra("insid");
            doctitle = getIntent().getStringExtra("doctitle");
            docnumber = getIntent().getStringExtra("docnumber");
        }
        //接收广播关闭页面
        finishreceiver = new MyReceiver();
        this.registerReceiver(finishreceiver, new IntentFilter(ConstantString.BROADCAST_ACTIVITY_FINISH));
        toDoModel = new ToDoModel();
        getToDoDetails();
        // 初始化fragment切换部分
        fragmentList.add(fileTodoListFragment);
        fragmentList.add(fileContentFragment);
        fragmentList.add(attachFragment);
        FragmentManager fragmentManager = getSupportFragmentManager();
        approvalViewpager.setOffscreenPageLimit(fragmentList.size());
        approvalViewpager.setAdapter(new ApprovalFragmentAdapter(fragmentList, fragmentManager));
        approvalViewpager.setOnPageChangeListener(this);
        //
        approvalSv.setScrollViewListener(this);
        approvalRg.setOnCheckedChangeListener(this);
        approvalRgTop.setOnCheckedChangeListener(this);
        approvalRb1.setChecked(true);

    }


    private class MyReceiver extends BroadcastReceiver {
        /**
         * 接数据参数的Receiver
         *
         * @param context
         * @param intent
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    }

    @Override
    public void setlayoutIds() {
        setContentView(R.layout.approval_activity_approval_todo);
    }


    /**
     * 页面单选按钮组切换点击事件
     *
     * @param group
     * @param checkedId
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.approval_rb_1_top:
            case R.id.approval_rb_1: {
                //文件处理单
                changeLinearUnderRb(approvalRbLl1, approvalRbLl2, approvalRbLl3);
                changeLinearUnderRb(approvalRbLl1Top, approvalRbLl2Top, approvalRbLl3Top);
                setRadioButtonChecked(approvalRb1, approvalRb1Top);
                approvalViewpager.setCurrentItem(0);
            }
            break;
            case R.id.approval_rb_2_top:
            case R.id.approval_rb_2: {
                //公文正文
                changeLinearUnderRb(approvalRbLl2, approvalRbLl1, approvalRbLl3);
                changeLinearUnderRb(approvalRbLl2Top, approvalRbLl1Top, approvalRbLl3Top);
                setRadioButtonChecked(approvalRb2, approvalRb2Top);
                approvalViewpager.setCurrentItem(1);
            }
            break;
            case R.id.approval_rb_3_top:
            case R.id.approval_rb_3: {
                //附件材料
                changeLinearUnderRb(approvalRbLl3, approvalRbLl2, approvalRbLl1);
                changeLinearUnderRb(approvalRbLl3Top, approvalRbLl2Top, approvalRbLl1Top);
                setRadioButtonChecked(approvalRb3, approvalRb3Top);
                approvalViewpager.setCurrentItem(2);

                //像Fragment发送数据
                Intent intent1 = new Intent();
                intent1.setAction("com.sendrecevice.fragment.AttachFragment");
                intent1.putExtra("insid", insid);
                sendBroadcast(intent1);
            }
            break;
        }

    }

    //页面滚动切换事件
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁广播
        unregisterReceiver(finishreceiver);
    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0: {
//                approvalSv.scrollTo(0, 0);
                changeLinearUnderRb(approvalRbLl1, approvalRbLl2, approvalRbLl3);
                changeLinearUnderRb(approvalRbLl1Top, approvalRbLl2Top, approvalRbLl3Top);
                setRadioButtonChecked(approvalRb1, approvalRb1Top);
            }
            break;
            case 1: {
                changeLinearUnderRb(approvalRbLl2, approvalRbLl1, approvalRbLl3);
                changeLinearUnderRb(approvalRbLl2Top, approvalRbLl1Top, approvalRbLl3Top);
                setRadioButtonChecked(approvalRb2, approvalRb2Top);
//                approvalSv.scrollTo(0, 0);
            }
            break;
            case 2: {
                changeLinearUnderRb(approvalRbLl3, approvalRbLl2, approvalRbLl1);
                changeLinearUnderRb(approvalRbLl3Top, approvalRbLl2Top, approvalRbLl1Top);
                setRadioButtonChecked(approvalRb3, approvalRb3Top);
//                approvalSv.scrollTo(0, 0);
            }
            break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 页面内点击事件
     *
     * @param view
     */
    @OnClick({R.id.approval_tv_todo_reject, R.id.approval_tv_todo_alter, R.id.approval_tv_todo_sign, R.id.approval_tv_todo_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.approval_tv_todo_reject: {//驳回
                RefuseIssueDialog refuseIssueDialog = new RefuseIssueDialog();
                Bundle bundle = new Bundle();
                bundle.putString("insid", insid);
                refuseIssueDialog.setArguments(bundle);
                showDialogFragment(refuseIssueDialog);
            }
            break;
            case R.id.approval_tv_todo_alter: {
                // 修改
                AlterIssueDialog alterIssueDialog = new AlterIssueDialog();
                Bundle bundle = new Bundle();
                bundle.putString("insid", insid);
                alterIssueDialog.setArguments(bundle);
                showDialogFragment(alterIssueDialog);
            }
            break;
            case R.id.approval_tv_todo_sign: {
                // 签字 手写板
                if ("".equals(ConstantString.FORM_ID)) {
                    ToastUtil.showToast("此文件暂不需要签字");
                } else {
                    Intent intent = new Intent(this, WritePadActivity.class);
                    intent.putExtra("data", insid);
                    startActivity(intent);
                }
            }
            break;
            case R.id.approval_tv_todo_send: {
                //发送
                send();
            }
            break;
        }
    }


    /**
     * 针对ScrollView滚动的监听事件
     *
     * @param v
     * @param scrollX
     * @param scrollY
     * @param oldScrollX
     * @param oldScrollY
     */
    @Override
    public void onScrollChanged(ScrollViewWithScrollListener v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        Rect scrollRect = new Rect();
        approvalSv.getHitRect(scrollRect);
        if (approvalRg.getLocalVisibleRect(scrollRect)) {
            //子控件在可视范围内
            approvalLlCollectionTop.setVisibility(View.GONE);
        } else {
            //子控件完全不在可视范围内
            approvalLlCollectionTop.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 将传入的radioButton全部选中
     *
     * @param radioButton
     */
    private void setRadioButtonChecked(RadioButton... radioButton) {
        for (int i = 0; i < radioButton.length; i++) {
            if (!radioButton[i].isChecked()) {
                radioButton[i].setChecked(true);
            }
        }
    }


    /**
     * 根据选中的单选按钮对按钮下的条文颜色进行切换
     * 将第一个参数布局设置成选中状态，其它两个布局设置成灰色
     *
     * @param ll1
     * @param ll2
     * @param ll3
     */
    private void changeLinearUnderRb(LinearLayout ll1, LinearLayout ll2, LinearLayout ll3) {
        ll1.setBackgroundColor(getResources().getColor(R.color.color_647aea));
        ll2.setBackgroundColor(getResources().getColor(R.color.grey_line));
        ll3.setBackgroundColor(getResources().getColor(R.color.grey_line));
    }

    /**
     * 弹出dialogFragment的方法
     *
     * @param dialogFragment
     */
    private void showDialogFragment(DialogFragment dialogFragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        dialogFragment.show(ft, "df");
    }

    //公文详情数据
    private void getToDoDetails() {
        U.showLoadingDialog(this);
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(this).getAccess_token());
        params.put("insid", insid);
        HttpUtil.httpGet(ConstantUrl.BACKLOGDETAILS, params, new JsonCallback<BaseModel<ToDoModel>>() {
            @Override
            public void onSuccess(BaseModel<ToDoModel> meetingSignModelBaseModel, Call call, Response response) {
                tvTodoFileNum.setText(docnumber);
                tvTodoFileTitle.setText(doctitle);
                toDoModel = meetingSignModelBaseModel.results;
                tvTodoFileProcess.setText(toDoModel.getProcessName());
                tvTodoFileAuthor.setText(toDoModel.getDrafter());
                tvTodoFileDate.setText(toDoModel.getCreateTime());
                //像Fragment发送数据
                Intent intent = new Intent();
                intent.putExtra("insid", insid);
                intent.putExtra("processkey", meetingSignModelBaseModel.results.getProcessKey());
                intent.putExtra("nodeid", meetingSignModelBaseModel.results.getNodeId());
                intent.setAction(ConstantString.BROADCAST_INSID_TAG);
                BroadCastManager.getInstance().sendBroadCast(SendIssueTodoDetailActivity.this, intent);
                if (ConstantString.FILE_TYPE_COMPLETE.equals(meetingSignModelBaseModel.results.getNodeType())) {
                    hasComplete = true;
                }
                U.closeLoadingDialog();
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                U.closeLoadingDialog();
            }
        });
    }

    /**
     * 发送
     */
    private void send() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", toDoModel);
        Intent intent = new Intent(this, SendToNextActivity.class);
        intent.putExtra("insid", insid);
        intent.putExtra("doctype", ConstantString.SEND_RECEIVE_DOC_TYPE_SEND);
        intent.putExtra("fieldjson", fileTodoListFragment.getUserJson());
        intent.putExtra("hasComplete", hasComplete);
        intent.putExtras(bundle);
        startActivity(intent);
    }


}
