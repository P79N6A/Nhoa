package com.wisdom.nhoa.supervision.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.approval.helper.FIlePopWindowHelper;
import com.wisdom.nhoa.approval.model.CopyToSelectedModel;
import com.wisdom.nhoa.base.BaseActivity;
import com.wisdom.nhoa.util.FileUtils;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.ToastUtil;
import com.wisdom.nhoa.util.U;
import com.wisdom.nhoa.util.http_util.HttpUtil;
import com.wisdom.nhoa.widget.CustomDatePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import static com.wisdom.nhoa.ConstantString.REQUEST_CAMERA;

public class AddSupervisionActivity extends BaseActivity {
    @BindView(R.id.et_supervision_name)
    EditText et_supervision_name;
    @BindView(R.id.tv_begin_time)
    TextView tv_begin_time;
    @BindView(R.id.tv_end_time)
    TextView tv_end_time;
    @BindView(R.id.tv_supervised)
    TextView tv_supervised;
    @BindView(R.id.et_supervision_content)
    EditText et_supervision_content;
    @BindView(R.id.tv_attachment)
    TextView tv_attachment;
    @BindView(R.id.bt_submit_supervision)
    Button bt_submit_supervision;
    private CustomDatePicker customDatePicker;
    private int currentmark = 0;
    private Context mContext;
    private String mFilePath;
    private File updatefile;
    private File photoFile;

    private List<Map<Integer, String>> ischeck;
    private String reminder = "";
    private CopyToSelectedModel copyToSelectedModel;

    @Override
    public void initViews() {
        mContext = this;
        setTitle("发起督办");
        initDataPick();
        mFilePath = Environment.getExternalStorageDirectory().getPath();// 获取SD卡路径
        mFilePath = mFilePath + System.currentTimeMillis() + ".png";// 指定路径
    }

    @Override
    public void setlayoutIds() {
        setContentView(R.layout.activity_add_supervision);
    }

    @OnClick({R.id.tv_begin_time, R.id.tv_end_time, R.id.tv_attachment, R.id.tv_supervised, R.id.bt_submit_supervision})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_begin_time:
                currentmark = 0;
                customDatePicker.show(tv_begin_time.getText().toString());
                break;
            case R.id.tv_end_time:
                currentmark = 1;
                customDatePicker.show(tv_begin_time.getText().toString());
                break;
            case R.id.tv_attachment://添加附件
                new FIlePopWindowHelper(mContext).showUploadPop(mContext);
                break;
            case R.id.tv_supervised://添加督办人
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", copyToSelectedModel);
                Intent intent = new Intent(this, ChooseSupervisionActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, ConstantString.COPY_TO_REQUEST_CODE);
                break;
            case R.id.bt_submit_supervision://
                addSupervision();
                break;
            default:
                break;

        }

    }

    private void addSupervision() {
        if (et_supervision_name.getText().toString().equals("")) {
            ToastUtil.showToast("督办名称不能为空");
            return;
        }

        if (tv_supervised.getText().toString().equals("")) {
            ToastUtil.showToast("被督办人不能为空");
            return;
        }
        submitSupervision();
    }

    private void initDataPick() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());

        customDatePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                if (currentmark == 0) {
                    tv_begin_time.setText(time);
                } else {
                    tv_end_time.setText(time);
                }
            }
        }, "1990-01-01 00:00", "2100-01-01 00:00");
        customDatePicker.showSpecificTime(true); // 显示时和分
        customDatePicker.setIsLoop(true); // 允许循环滚动
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date curDate = new Date(System.currentTimeMillis());
        Date onehourDate = new Date(System.currentTimeMillis() + 60 * 60 * 1000);
        tv_begin_time.setText(formatter.format(curDate));
        tv_end_time.setText(formatter.format(onehourDate));
    }

    /**
     * 选择上传附件文件后的回调方法
     * 调起相关选择器的事件详见mydeclare.helper.FIlePopWindowHelper
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode != ConstantString.COPY_TO_REQUEST_CODE) {
            photoFile = new File(ConstantString.PIC_LOCATE);
            if (requestCode != REQUEST_CAMERA
                    && requestCode != ConstantString.MANAGMENT_REQUEST_CODE
                    && data != null) {
                //当用户选择的是相册或者文件浏览器时候的回调，向列表中插入正在上传的布局
                String[] names1 = data.getData().getPath().split("/");
                tv_attachment.setText(names1[names1.length - 1]);
            } else if (requestCode == REQUEST_CAMERA
                    && !"".equals(ConstantString.PIC_LOCATE)
                    && photoFile.exists()) {
                //当用户选择的是拍照时候的回调，向列表中插入正在上传的布局
                String[] names2 = ConstantString.PIC_LOCATE.split("/");
                tv_attachment.setText(names2[names2.length - 1]);
            }
        }
        switch (requestCode) {
            case ConstantString.ALBUM_SELECT_CODE: {//相册选择
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        // Get the Uri of the selected file
                        Uri uri = data.getData();
                        Uri uri2 = Uri.parse(Uri.encode(uri.toString()));
                        String path = FileUtils.getPath(this, uri);
                        if (!mFilePath.equals("")) {
                            updatefile = new File(path);
                            Log.e("文件路径", path);
                        }
                    } else if ("".equals(ConstantString.PIC_LOCATE)) {
                        if (!mFilePath.equals("")) {

                        }
                    }
                }
            }
            break;
            case ConstantString.FILE_SELECT_CODE: {//文件选择器选择
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        // Get the Uri of the selected file
                        Uri uri = data.getData();
                        String path = FileUtils.getPathByUri4kitkat(this, uri);
                        if (!mFilePath.equals("")) {
                            updatefile = new File(path);
                        }
                    }
                }
            }
            break;
            case REQUEST_CAMERA: {//相机拍照选择
                if (!"".equals(ConstantString.PIC_LOCATE)
                        && photoFile.exists()
                        ) {
                    updatefile = new File(ConstantString.PIC_LOCATE);
                }
            }
            break;
            default: {


            }
            break;
            case ConstantString.COPY_TO_REQUEST_CODE: {
//                选择反馈人的回调
                if (data != null) {
                    copyToSelectedModel = (CopyToSelectedModel) data.getExtras().getSerializable("data");
                    ischeck = copyToSelectedModel.getIscheck();
                    reminder = copyToSelectedModel.getId();
                    if (!"".equals(copyToSelectedModel.getName())) {
                        tv_supervised.setText(copyToSelectedModel.getName());
                    }
                }
            }
            break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //提交督办
    public void submitSupervision() {
        U.showLoadingDialog(this);
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(mContext).getAccess_token());
        params.put("name", et_supervision_name.getText().toString());
        params.put("begintime", tv_begin_time.getText().toString());
        params.put("endtime", tv_end_time.getText().toString());
        params.put("superintendent", reminder);
        if (!et_supervision_content.getText().toString().equals("")) {
            params.put("content", et_supervision_content.getText().toString());
        }
        if (updatefile != null) {
            params.put("file", updatefile);
        }
        HttpUtil.httpPostWithMultipart(ConstantUrl.ADD_SUPERVISION, params, new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                Log.e("请求成功", s);
                try {
                    U.closeLoadingDialog();
                    JSONObject jsonObject = new JSONObject(s);
                    String error_code = jsonObject.getString("error_code");
                    if (error_code.equals("0")) {
                        ToastUtil.showToast("添加成功");
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

}
