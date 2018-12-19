package com.wisdom.nhoa.sendreceive.activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.callback.StringCallback;
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
import com.wisdom.nhoa.sendreceive.model.SendManageModel;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
 * @class describe：收文 我发起的详情页
 * @time 2018/3/12 15:23
 * @change
 */


public class ReceiveIssueSponsorDetailActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, ScrollViewWithScrollListener.ScrollViewListener, ViewPager.OnPageChangeListener {

    public static final String TAG = ReceiveIssueSponsorDetailActivity.class.getSimpleName();
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
        @BindView(R.id.approval_ll_todo_menu)
    LinearLayout approvalLlTodoMenu;
    private WritePadDialog mWritePadDialog;
    private ToDoModel toDoModel;
    private String insid = "";
    private String docnumber = "";
    private String doctitle = "";
    private String form_id = "";
    private ApprovalFragmentAdapter approvalFragmentAdapter;
    // 文件处理
    private FileTodoListFragment fileTodoListFragment = new FileTodoListFragment();
    // 公文正文
    private FileContentFragment fileContentFragment = new FileContentFragment();
    // 附件材料
    private AttachFragment attachFragment = new AttachFragment();
    private List<Fragment> fragmentList = new ArrayList<>();
    private SendManageModel sendManageModel;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void initViews() {
        approvalLlTodoMenu.setVisibility(View.GONE);
        setTitle(R.string.sendreceive_receive_sponsor);
        toDoModel = new ToDoModel();
        if (getIntent() != null) {
            sendManageModel = (SendManageModel) getIntent().getExtras().getSerializable("data");
            insid = sendManageModel.getInsid();
            doctitle = sendManageModel.getDoctitle();
            docnumber = sendManageModel.getDocnumber();
        }

        getToDoDetails();
        // 初始化fragment切换部分
        fragmentList.add(fileTodoListFragment);
        fragmentList.add(fileContentFragment);
        fragmentList.add(attachFragment);
        FragmentManager fragmentManager = getSupportFragmentManager();
        approvalFragmentAdapter = new ApprovalFragmentAdapter(fragmentList, fragmentManager);
        approvalViewpager.setOffscreenPageLimit(fragmentList.size());
        approvalViewpager.setAdapter(approvalFragmentAdapter);
        approvalViewpager.setOnPageChangeListener(this);
        //
        approvalSv.setScrollViewListener(this);
        approvalRg.setOnCheckedChangeListener(this);
        approvalRgTop.setOnCheckedChangeListener(this);
        approvalRb1.setChecked(true);

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
            }
            break;
        }

    }

    //页面滚动切换事件
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

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
            case R.id.approval_tv_todo_reject: {
                // 驳回
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

    private void getToDoDetails() {
        U.showLoadingDialog(this);
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(this).getAccess_token());
        params.put("insid", insid);
        HttpUtil.httpGet(ConstantUrl.BACKLOGDETAILS, params, new JsonCallback<BaseModel<ToDoModel>>() {
            @Override
            public void onSuccess(BaseModel<ToDoModel> listBaseModel, Call call, Response response) {
                tvTodoFileNum.setText(docnumber);
                tvTodoFileTitle.setText(doctitle);
                toDoModel = listBaseModel.results;
                tvTodoFileProcess.setText(toDoModel.getProcessName());
                tvTodoFileAuthor.setText(toDoModel.getDrafter());
                tvTodoFileDate.setText(toDoModel.getCreateTime());

                form_id = ConstantString.FORM_ID;
                //发送广播，通知Fragment insId参数
                Intent intent = new Intent();
                intent.putExtra("insid", sendManageModel.getInsid());
                intent.putExtra("processkey", listBaseModel.results.getProcessKey());
                intent.putExtra("nodeid", listBaseModel.results.getNodeId());
                intent.setAction(ConstantString.BROADCAST_INSID_TAG);
                BroadCastManager.getInstance().sendBroadCast(ReceiveIssueSponsorDetailActivity.this, intent);
                if (ConstantString.FILE_TYPE_UNDER_DO.equals(listBaseModel.results.getNodeType())) {
                    //    在办
                    approval_tv_todo_send.setText(R.string.approval_approval_todo_send);
                } else {
                    //    办结
                    approval_tv_todo_send.setText(R.string.approval_approval_todo_finish);
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
     * 创建签名文件
     */
    private void createSignFile(Bitmap mSignBitmap) {
        ByteArrayOutputStream baos = null;
        FileOutputStream fos = null;
        String path = null;
        File file = null;
        try {
            // TODO: 2017/11/3 图片地址写死了***************************
//            if (ConstantString.filelist != null) {
//                path = Environment.getExternalStorageDirectory() + File.separator + ConstantString.filelist.get(0).getSign_name() + ".jpg";
//            } else {
            path = Environment.getExternalStorageDirectory() + "/oaSign/" + System.currentTimeMillis() + ".png";
//            }
            Log.i(TAG, "图片路径：" + path);
            file = new File(path);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdir();
            }
            fos = new FileOutputStream(file);
            baos = new ByteArrayOutputStream();

            //如果设置成
//            mSignBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            // 图片的背景都是黑色的
            mSignBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            if (b != null) {
                fos.write(b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (file != null) {
            HttpParams params1 = new HttpParams();
            params1.put("appkey", ConstantString.APP_KEY);
            params1.put("insid", insid);
            params1.put("form_id", ConstantString.FORM_ID);
            params1.put("access_token", SharedPreferenceUtil.getUserInfo(this).getAccess_token());
            params1.put("sign_pic", file);

            Log.i(TAG, "sign_pic: " + file.getAbsolutePath());
            Log.i(TAG, "appkey: " + ConstantString.APP_KEY);
            Log.i(TAG, "insid: " + insid);
            Log.i(TAG, "form_id: " + ConstantString.FORM_ID);
            Log.i(TAG, "access_token: " + SharedPreferenceUtil.getUserInfo(this).getAccess_token());
//            上传文件到服务器的逻辑
            HttpUtil.uploadFiles(ConstantUrl.FILEHANDINGSHEET_SIGN, params1, new StringCallback() {
                @Override
                public void onError(Call call, Response response, Exception e) {
                    super.onError(call, response, e);
                    Toast.makeText(ReceiveIssueSponsorDetailActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "onError: " + e);
                    if (mWritePadDialog != null) {
                        mWritePadDialog.dismiss();
                    }
                }

                @Override
                public void onSuccess(String s, Call call, Response response) {
                    Log.e(TAG, "onSuccess: " + s);
                    String code = "";
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        code = jsonObject.getString("error_code");
                        if ("0".equals(code)) {
                            Toast.makeText(ReceiveIssueSponsorDetailActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                            // 签名成功后的处理
                            Intent intentRefresh = new Intent();
                            intentRefresh.setAction(ConstantString.BROADCAST_REFRESH_TAG);
                            BroadCastManager.getInstance().sendBroadCast(ReceiveIssueSponsorDetailActivity.this, intentRefresh);
                        } else {
                            Toast.makeText(ReceiveIssueSponsorDetailActivity.this, jsonObject.getString("error_msg"), Toast.LENGTH_SHORT).show();
                        }
                        if (mWritePadDialog != null) {
                            mWritePadDialog.dismiss();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }


}
