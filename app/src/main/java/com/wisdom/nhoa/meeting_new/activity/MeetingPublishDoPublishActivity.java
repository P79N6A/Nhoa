package com.wisdom.nhoa.meeting_new.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.request.BaseRequest;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.base.BaseActivity;
import com.wisdom.nhoa.circulated.helper.PopWindowHelper;
import com.wisdom.nhoa.meeting_new.model.ApplyMeetingRoomDetailModel;
import com.wisdom.nhoa.meeting_new.model.FileUploadSuccessModel;
import com.wisdom.nhoa.meeting_new.model.MeetingListModel;
import com.wisdom.nhoa.meeting_new.model.SimpleResponseBooleanModel;
import com.wisdom.nhoa.meeting_new.model.TopicModel;
import com.wisdom.nhoa.util.FileUtils;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.StrUtils;
import com.wisdom.nhoa.util.ToastUtil;
import com.wisdom.nhoa.util.http_util.HttpUtil;
import com.wisdom.nhoa.util.http_util.callback.BaseModel;
import com.wisdom.nhoa.util.http_util.callback.JsonCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import static com.wisdom.nhoa.ConstantString.REQUEST_CAMERA;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.meeting_new.activity
 * @class describe：发布会议填写信息界面
 * @time 2018/10/24 16:04
 * @change
 */
public class MeetingPublishDoPublishActivity extends BaseActivity {
    @BindView(R.id.et_meeting_theme)
    EditText etMeetingTheme;
    @BindView(R.id.et_convoke_department)
    EditText etConvokeDepartment;
    @BindView(R.id.et_compere)
    EditText etCompere;
    @BindView(R.id.et_meeting_room)
    EditText etMeetingRoom;
    @BindView(R.id.et_Participant)
    TextView etParticipant;
    @BindView(R.id.et_start_time)
    EditText etStartTime;
    @BindView(R.id.et_end_time)
    EditText etEndTime;
    @BindView(R.id.et_current_state)
    EditText etCurrentState;
    @BindView(R.id.ll_add_subject)
    LinearLayout llAddSubject;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.et_meeting_contect_person)
    EditText etMeetingContectPerson;
    @BindView(R.id.et_meeting_contect_person_phone)
    EditText etMeetingContectPersonPhone;
    @BindView(R.id.et_meeting_record_person)
    EditText etMeetingRecordPerson;
    @BindView(R.id.et_remark)
    EditText etRemark;
    //选择参会人员的回调参数
    private String memberid = "";
    private String username = "";
    //选择议题 参会人员的回调参数
    private String topicMemberid = "";
    private String topicUsername = "";
    private ProgressDialog progressDialog;
    private String mFilePath;
    public static final String TAG = MeetingPublishDoPublishActivity.class.getSimpleName();
    private File photoFile;
    private MeetingListModel meetingListModel;
    private ApplyMeetingRoomDetailModel applyMeetingRoomDetailModel;
    private String tag = "";//判定是哪个界面跳转过来的数据
    private Bundle choosedParticpantBundle = null;//选择完参会人员后，返回的参会人员model，提供给选择议题人员时候使用
    private EditText et_temp;//设置显示议题 参与人员 的临时控件
    private TextView tv_file_name;//设置显示议题 附件 的临时控件
    private List<TopicModel> topicModelList = new ArrayList<>();//存储议题的model

    @Override
    public void setlayoutIds() {
        setContentView(R.layout.activity_meeting_publish_do_publish);
    }

    @Override
    public void initViews() {
        setTitle(R.string.meeting_publish_title);
        mFilePath = Environment.getExternalStorageDirectory().getPath();// 获取SD卡路径
        mFilePath = mFilePath + System.currentTimeMillis() + ".png";// 指定路径
        if (getIntent() != null) {
            tag = getIntent().getStringExtra("tag");
            switch (tag) {
                case ConstantString.MEETING_PUBLISH_TAG_MEETING_LIST: {
//                    由会议列表跳转到发布会议 获取上级界面信息，填充到表单页面
                    meetingListModel = (MeetingListModel) getIntent().getExtras().getSerializable("data");
                    if (meetingListModel != null) {
                        etMeetingTheme.setText(meetingListModel.getTheme());
                        etMeetingTheme.setFocusable(false);
                        etStartTime.setText(meetingListModel.getStaTime());
                        etStartTime.setFocusable(false);
                        etEndTime.setText(meetingListModel.getEndTime());
                        etEndTime.setFocusable(false);
                        etMeetingRoom.setText(meetingListModel.getBdrmName());
                        etMeetingRoom.setFocusable(false);
                    }
                }
                break;
                case ConstantString.MEETING_PUBLISH_TAG_MEETING_ROOM_LIST: {
//                    由会议室申请列表跳转到发布会议
                    applyMeetingRoomDetailModel = (ApplyMeetingRoomDetailModel) getIntent().getExtras().getSerializable("data");
                    if (applyMeetingRoomDetailModel != null) {
                        etMeetingTheme.setText(applyMeetingRoomDetailModel.getTheme());
                        etMeetingTheme.setFocusable(false);
                        etStartTime.setText(applyMeetingRoomDetailModel.getStaTime());
                        etStartTime.setFocusable(false);
                        etEndTime.setText(applyMeetingRoomDetailModel.getEndTime());
                        etEndTime.setFocusable(false);
                        etMeetingRoom.setText(applyMeetingRoomDetailModel.getBdrmName());
                        etMeetingRoom.setFocusable(false);
                    }
                }
                break;
            }
        }
    }


    /**
     * 添加议题布局
     */
    @OnClick(R.id.ll_add_subject)
    public void onLlAddSubjectClicked() {
        //创建一个议题数据模型，并添加到模型列表中
        final TopicModel topicModel = new TopicModel();
        topicModelList.add(topicModel);
        //构建议题布局
        View view = LayoutInflater.from(this).inflate(R.layout.item_meeting_publish_add_subjet, null, false);
        TextView textView = view.findViewById(R.id.tv_layout_title);
        final TextView tv_file = view.findViewById(R.id.tv_file);
        TextView tv_add_attach = view.findViewById(R.id.tv_add_attach);
        final EditText etTopicSubject = view.findViewById(R.id.et_meeting_theme);
        final EditText et_topic_person = view.findViewById(R.id.et_topic_person);
        et_topic_person.setHint("请选择参与议题人员");
        llContent.addView(view, llContent.getChildCount() - 1);
        textView.setText("议题" + StrUtils.toChinese((llContent.getChildCount() - 2) + ""));
        //设置添加文件按钮点击事件
        tv_add_attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_file_name = tv_file;
                new PopWindowHelper(MeetingPublishDoPublishActivity.this).showUploadPop(MeetingPublishDoPublishActivity.this, R.id.bt_publish_meeting);
            }
        });
        //设置选择议题人员点击事件
        et_topic_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!StrUtils.isEdtTxtEmpty(etTopicSubject)) {
                    //将议题名称添加到数据模型
                    String topicSubject = StrUtils.getEdtTxtContent(etTopicSubject);
                    topicModelList.get(llContent.getChildCount() - 3).setIssueName(topicSubject);
                    //判断会议参与人员是否已经选择完毕，然后进行 选择议题参与人员
                    if (choosedParticpantBundle != null) {
                        Intent intent = new Intent(MeetingPublishDoPublishActivity.this
                                , TopicParticipantActivity.class);
                        intent.putExtras(choosedParticpantBundle);
                        startActivityForResult(intent, 0x888);
                        //将当前控件设置到变量，回调时候可以用来设置显示的值
                        et_temp = et_topic_person;
                    } else {
                        ToastUtil.showToast("请先选择参与会议人员");
                    }
                } else {
                    ToastUtil.showToast("议题名称不能为空");
                }
            }
        });


    }

    /**
     * 选择参会人员
     */
    @OnClick(R.id.et_Participant)
    public void onViewClicked() {
        startActivityForResult(new Intent(this, ParticipantActivity.class), 777);
    }

    /**
     * 发布会议
     */
    @OnClick(R.id.bt_publish_meeting)
    public void onBtApplyMeetingRoomClicked() {
        if (checkValidate()) {
            new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("确定发布此会议？")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            publishMeeting();
                        }
                    }).create().show();
        }
    }

    /**
     * 执行发布会议方法
     */
    private void publishMeeting() {
        //构建参数
        HttpParams params = new HttpParams();
        if (topicModelList.size() > 0) {
//            String topic = new Gson().toJson(topicModelList).toString();
            String topic = "";
            for (int j = 0; j < topicModelList.size(); j++) {
                String temp = new Gson().toJson(topicModelList.get(j)).toString();
                topic += temp + ";";
            }
            if (!"".equals(topic)) {
                topic = topic.substring(0, topic.length() - 1);
            }
            Log.i(TAG, "issueMsg: " + topic);
            params.put("issueMsg", topic);

        }
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(this).getAccess_token());
        params.put("meetingtitle", StrUtils.getEdtTxtContent(etMeetingTheme));
        params.put("memberid", memberid);
        params.put("starttime", StrUtils.getEdtTxtContent(etStartTime));
        params.put("endtime", StrUtils.getEdtTxtContent(etEndTime));
        params.put("meetingcontent", StrUtils.getEdtTxtContent(etRemark));
        params.put("bdrmApplyId", meetingListModel.getId());
        params.put("master", StrUtils.getEdtTxtContent(etCompere));
        params.put("linkman", StrUtils.getEdtTxtContent(etMeetingContectPerson));
        params.put("linkTel", StrUtils.getEdtTxtContent(etMeetingContectPersonPhone));
        params.put("logMan", StrUtils.getEdtTxtContent(etMeetingRecordPerson));
        HttpUtil.httpPost(ConstantUrl.PUBLISH_MEETING_URL, params, new JsonCallback<BaseModel<SimpleResponseBooleanModel>>() {
            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                ToastUtil.showToast(e.getMessage());
            }

            @Override
            public void onSuccess(BaseModel<SimpleResponseBooleanModel> modelBaseModel, Call call, Response response) {
                if (modelBaseModel.results.isResult()) {
                    //提交成功
                    setResult(ConstantString.REQUEST_CODE_REFRESH_DATA);
                    ToastUtil.showToast("发布成功");
                    MeetingPublishDoPublishActivity.this.finish();
                } else {
                    //提交失败
                    ToastUtil.showToast("发布失败，请重试");
                }
            }
        });
    }

    /**
     * 检查界面信息是否完整
     * social
     *
     * @return
     */
    private Boolean checkValidate() {
        Boolean isChecked = true;
        if (StrUtils.isEdtTxtEmpty(etCompere)) {
            ToastUtil.showToast(R.string.meeting_publish_compere_hint);
            isChecked = false;
        } else if ("".equals(memberid)) {
            ToastUtil.showToast(R.string.index_meeting_participant_title);
            isChecked = false;
        } else if (StrUtils.isEdtTxtEmpty(etMeetingContectPerson)) {
            ToastUtil.showToast(R.string.meeting_publish_contect_hint);
            isChecked = false;
        } else if (StrUtils.isEdtTxtEmpty(etMeetingContectPersonPhone)) {
            ToastUtil.showToast(R.string.meeting_publish_contect_phone_hint);
            isChecked = false;
        } else if (StrUtils.isEdtTxtEmpty(etMeetingRecordPerson)) {
            ToastUtil.showToast(R.string.meeting_publish_record_hint);
            isChecked = false;
        }
        return isChecked;
    }

    /**
     * 界面回调方法
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 777) {
            //选择参会人员的回调方法
            if (data != null) {
                memberid = data.getStringExtra("memberid");
                username = data.getStringExtra("username");
                choosedParticpantBundle = data.getExtras();
                etParticipant.setText(username);
            }
        } else if (requestCode == 0x888) {
            //选择参加议题人的回调方法
            if (data != null) {
                topicMemberid = data.getStringExtra("memberid");
                topicUsername = data.getStringExtra("username");
                et_temp.setText(topicUsername);
                //将接口返回值封装进数据模型
                topicModelList.get(llContent.getChildCount() - 3).setIssuePersons(topicMemberid);
            }
        } else {
            //选择附件
            photoFile = new File(ConstantString.PIC_LOCATE);
            switch (requestCode) {
                case ConstantString.ALBUM_SELECT_CODE: {//相册选择
                    if (resultCode == RESULT_OK) {
                        if (data != null) {
                            // Get the Uri of the selected file
                            Uri uri = data.getData();
                            Uri uri2 = Uri.parse(Uri.encode(uri.toString()));
                            String path = FileUtils.getPath(this, uri);
                            if (!mFilePath.equals("")) {
                                uploadFiles(path);
                            }
                        } else if ("".equals(ConstantString.PIC_LOCATE)) {
                            if (!mFilePath.equals("")) {
                                uploadFiles(ConstantString.PIC_LOCATE);
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
                                uploadFiles(path);
                            }
                        }
                    }
                }
                break;
                case REQUEST_CAMERA: {//相机拍照选择
                    if (!"".equals(ConstantString.PIC_LOCATE)
                            && photoFile.exists()
                            ) {
                        uploadFiles(ConstantString.PIC_LOCATE);
                    }
                }
                break;
                default: {

                }
                break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 上传文件到服务器端
     *
     * @param picLocate
     */
    private void uploadFiles(String picLocate) {
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(this).getAccess_token());
        params.put("sign_pic", new File(picLocate));
        Log.i(TAG, "appkey: " + ConstantString.APP_KEY);
        Log.i(TAG, "access_token: " + SharedPreferenceUtil.getUserInfo(this).getAccess_token());
        HttpUtil.uploadFiles(ConstantUrl.UPLOAD_FILE_URL, params, new StringCallback() {
            @Override
            public void onBefore(BaseRequest request) {
                super.onBefore(request);
                progressDialog = new ProgressDialog(MeetingPublishDoPublishActivity.this);
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
                Log.i(TAG, "返回的服务器图片地址：" + s);
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                FileUploadSuccessModel fileUploadSuccessModel = new Gson().fromJson(
                        s, FileUploadSuccessModel.class
                );
                if (fileUploadSuccessModel != null) {
                    tv_file_name.setText(fileUploadSuccessModel.getResults().getFileName());
                    //将接口返回值封装进数据模型
                    topicModelList.get(llContent.getChildCount() - 3).setIssueFileUrl(
                            fileUploadSuccessModel.getResults().getFileUrl()
                    );
                }
            }

            @Override
            public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                super.upProgress(currentSize, totalSize, progress, networkSpeed);
                progressDialog.setMax(100);
                progressDialog.setProgress((int) (progress * 100));
            }
        });
    }


}
