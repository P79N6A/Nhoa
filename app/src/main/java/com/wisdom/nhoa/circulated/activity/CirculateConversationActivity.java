package com.wisdom.nhoa.circulated.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.request.BaseRequest;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.base.BaseActivity;
import com.wisdom.nhoa.circulated.adapter.CirculateConversationAdapter;
import com.wisdom.nhoa.circulated.helper.PopWindowHelper;
import com.wisdom.nhoa.circulated.model.CirculateConversationModel;
import com.wisdom.nhoa.circulated.model.CirculatedListModel;
import com.wisdom.nhoa.util.FileUtils;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.U;
import com.wisdom.nhoa.util.http_util.HttpUtil;
import com.wisdom.nhoa.util.http_util.callback.BaseModel;
import com.wisdom.nhoa.util.http_util.callback.JsonCallback;
import com.wisdom.nhoa.widget.WaterMarkText;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import static com.wisdom.nhoa.ConstantString.REQUEST_CAMERA;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.circulated.activity
 * @class describe： 公文传阅对话页面
 * @time 2018/3/26 14:42
 * @change
 */

public class CirculateConversationActivity extends BaseActivity {
    @BindView(R.id.circulated_conversation_listview)
    ListView circulatedConversationListview;
    @BindView(R.id.circulated_conversation_linearLayout)
    LinearLayout circulatedConversationLinearLayout;
    @BindView(R.id.circulated_tv_upload)
    TextView circulated_tv_upload;
    private ProgressDialog progressDialog;
    @BindView(R.id.head_parent)
    RelativeLayout head_parent;
    private CirculatedListModel circulatedListModel;
    private String mFilePath;
    public static final String TAG = CirculateConversationActivity.class.getSimpleName();
    int width1;//水印的左边距
    int height1;//水印的高度
    private MyReceiver receiver;

    @Override
    public void initViews() {
        addWaterMarkView(SharedPreferenceUtil.getUserInfo(this).getUser_name());
        mFilePath = Environment.getExternalStorageDirectory().getPath();// 获取SD卡路径
        //FIXME 临时文件夹
        mFilePath = mFilePath + System.currentTimeMillis() + ".png";// 指定路径

        Log.i(TAG, "mFilePath: " + mFilePath);
        if (getIntent() != null) {
            circulatedListModel = (CirculatedListModel) getIntent().getExtras().getSerializable("data");
            String title = circulatedListModel.getGroup_name() + " ("
                    + circulatedListModel.getMembercount() + ")";
            setTitle(title);
            getAndSetData();
        }
        setRightIcon(R.mipmap.icon_settings);
        //接收广播关闭页面
        receiver = new MyReceiver();
        this.registerReceiver(receiver, new IntentFilter(ConstantString.BROADCAST_GROUP_FINISH));
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
        setContentView(R.layout.circulate_activity_conversation);
    }

    /**
     * 请求接口数据，展示页面
     */
    private void getAndSetData() {
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("group_id", circulatedListModel.getGroup_id());
        params.put("access_token", SharedPreferenceUtil.getUserInfo(this).getAccess_token());
        U.showLoadingDialog(this);
        HttpUtil.httpGet(ConstantUrl.CIRCLATED_CONVERSATION, params, new JsonCallback<BaseModel<List<CirculateConversationModel>>>() {
            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                U.closeLoadingDialog();

            }

            @Override
            public void onSuccess(BaseModel<List<CirculateConversationModel>> listBaseModel, Call call, Response response) {
                U.closeLoadingDialog();
                CirculateConversationAdapter circulateConversationAdapter = new CirculateConversationAdapter(
                        CirculateConversationActivity.this
                        , listBaseModel.results
                );
                circulatedConversationListview.setAdapter(circulateConversationAdapter);
                circulatedConversationListview.setSelection(circulatedConversationListview.getAdapter().getCount());

            }
        });

    }

    /**
     * 页面内的点击事件
     *
     * @param view
     */
    @OnClick({R.id.head_right_iv, R.id.circulated_tv_upload})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_right_iv: {
                //右上角群管理
                Intent intent = new Intent(this, GroupManagementActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", circulatedListModel);
                intent.putExtras(bundle);
                startActivityForResult(intent, ConstantString.MANAGMENT_REQUEST_CODE);
            }
            break;
            case R.id.circulated_tv_upload: {
                //页面内上传按钮
                new PopWindowHelper(this).showUploadPop(this, R.id.circulated_conversation_listview);
            }
            break;
        }
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
        File photoFile = new File(ConstantString.PIC_LOCATE);
        if (requestCode == ConstantString.MANAGMENT_REQUEST_CODE) {
            //刷新页面数据
            getAndSetData();
            if (data != null) {
                ConstantString.GROUP_TOTAL_COUNT = data.getStringExtra("data");
                String title = circulatedListModel.getGroup_name() + " ("
                        + ConstantString.GROUP_TOTAL_COUNT + ")";
                setTitle(title);
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
                            uploadFiles(path, circulatedConversationLinearLayout);
                        }
                    } else if ("".equals(ConstantString.PIC_LOCATE)) {
                        if (!mFilePath.equals("")) {
                            uploadFiles(ConstantString.PIC_LOCATE, circulatedConversationLinearLayout);
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
                            uploadFiles(path, circulatedConversationLinearLayout);
                        }
                    }
                }
            }
            break;
            case REQUEST_CAMERA: {//相机拍照选择
                if (!"".equals(ConstantString.PIC_LOCATE)
                        && photoFile.exists()
                        ) {
                    uploadFiles(ConstantString.PIC_LOCATE, circulatedConversationLinearLayout);
                }
            }
            break;
            default: {

            }
            break;
        }
        Log.i(TAG, "onActivityResult: " + data);
        //展示页面上传假布局……
        if (requestCode != REQUEST_CAMERA
                && requestCode != ConstantString.MANAGMENT_REQUEST_CODE
                && data != null) {
            //当用户选择的是相册或者文件浏览器时候的回调，向列表中插入正在上传的布局
            String[] names = data.getData().getPath().split("/");
            insertUploadView(names[names.length - 1]);
        } else if (requestCode == REQUEST_CAMERA
                && !"".equals(ConstantString.PIC_LOCATE)
                && photoFile.exists()) {
            //当用户选择的是拍照时候的回调，向列表中插入正在上传的布局
            String[] names = ConstantString.PIC_LOCATE.split("/");
            insertUploadView(names[names.length - 1]);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 插入上传文件显示的布局
     */
    private void insertUploadView(String fileName) {
        View uploadView = LayoutInflater.from(this).inflate(R.layout.circulate_conversation_upload_item, null, false);
        TextView tv_name = uploadView.findViewById(R.id.circulated_conversation_tv_filename_right);
        TextView tv_time = uploadView.findViewById(R.id.circulated_conversation_tv_time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        tv_time.setText(simpleDateFormat.format(System.currentTimeMillis()));
        tv_name.setText(fileName);
        circulatedConversationLinearLayout.addView(uploadView);
    }



    /**
     * 上传文件到服务器端
     *
     * @param picLocate
     */
    private void uploadFiles(String picLocate, LinearLayout linearLayout) {
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(this).getAccess_token());
        params.put("group_id", circulatedListModel.getGroup_id());
        params.put("sign_pic", new File(picLocate));
        Log.i(TAG, "appkey: " + ConstantString.APP_KEY);
        Log.i(TAG, "access_token: " + SharedPreferenceUtil.getUserInfo(this).getAccess_token());
        Log.i(TAG, "group_id: " + circulatedListModel.getGroup_id());
        HttpUtil.uploadFiles(ConstantUrl.GROUP_UPLOAD_FILE, params, new StringCallback() {
            @Override
            public void onBefore(BaseRequest request) {
                super.onBefore(request);
                progressDialog = new ProgressDialog(CirculateConversationActivity.this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setTitle("正在上传……");
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                Log.i(TAG, "onError: 上传文件错误：" + e);
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onSuccess(String s, Call call, Response response) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                //移除界面假布局，刷新listView
                circulatedConversationLinearLayout.removeViewAt(1);
                getAndSetData();
            }

            @Override
            public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                super.upProgress(currentSize, totalSize, progress, networkSpeed);
                progressDialog.setMax(100);
                progressDialog.setProgress((int) (progress * 100));
            }
        });

    }


    // 获取屏幕的默认分辨率
    public void getDefaultScreenDensity() {
        Display mDisplay = getWindowManager().getDefaultDisplay();
        int width = mDisplay.getWidth();
        int height = mDisplay.getHeight();
        Log.d(TAG, "Screen Default Ratio: [" + width + "x" + height + "]");
        Log.d(TAG, "Screen mDisplay: " + mDisplay);

        width1 = width * 60 / 100;
        height1 = height / 6;
    }

    /**
     * 添加水印
     */
    private void addWaterMarkView(String username) {
        getDefaultScreenDensity();
        View waterMarkView = LayoutInflater.from(this).inflate(R.layout.layout_watermark, null);
        LinearLayout ll_group = waterMarkView.findViewById(R.id.ll_group);
        ll_group.setOrientation(LinearLayout.VERTICAL);
        for (int i = 0; i < 6; i++) {
            WaterMarkText text = new WaterMarkText(this);
            text.setDegree(45);
            LinearLayout.LayoutParams layout1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
            if (0 == i) {
                layout1.setMargins(50, 0, 0, 0);
                text.setTextSize(20);
                text.setText(username);
                text.setLayoutParams(layout1);
                ll_group.addView(text, i, layout1);
            } else if (1 == i) {
                layout1.setMargins(width1, 0, 0, 0);
                text.setTextSize(20);
                text.setText(username);
                text.setLayoutParams(layout1);
                ll_group.addView(text, i, layout1);
            } else if (2 == i) {
                layout1.setMargins(50, 0, 0, 0);
                text.setTextSize(20);
                text.setText(username);
                text.setLayoutParams(layout1);
                ll_group.addView(text, i, layout1);
            } else if (3 == i) {
                layout1.setMargins(width1, 0, 0, 0);
                text.setTextSize(20);
                text.setText(username);
                text.setLayoutParams(layout1);
                ll_group.addView(text, i, layout1);
            } else if (4 == i) {
                text.setTextSize(20);
                layout1.setMargins(50, 0, 0, 0);
                text.setText(username);
                text.setLayoutParams(layout1);
                ll_group.addView(text, i, layout1);
            } else if (5 == i) {
                text.setTextSize(20);
                layout1.setMargins(width1, 0, 0, 0);
                text.setText(username);
                text.setLayoutParams(layout1);
                ll_group.addView(text, i, layout1);
            }
        }
        getRootView().addView(waterMarkView, 0);
    }

    /**
     * 获取根布局DecorView
     *
     * @return
     */
    private ViewGroup getRootView() {
        ViewGroup rootView = (ViewGroup) findViewById(android.R.id.content);
        return rootView;
    }

}
