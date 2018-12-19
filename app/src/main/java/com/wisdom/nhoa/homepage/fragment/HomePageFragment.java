package com.wisdom.nhoa.homepage.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.approval.activity.ApprovalActivity;
import com.wisdom.nhoa.approval.activity.ApprovalListActivity;
import com.wisdom.nhoa.circulated.activity.DocumentCirculatedListActivity;
import com.wisdom.nhoa.homepage.activity.DraftActivity;
import com.wisdom.nhoa.homepage.activity.NoticeActivity;
import com.wisdom.nhoa.homepage.activity.ScheduleActivity;
import com.wisdom.nhoa.homepage.activity.ToBeReadActivity;
import com.wisdom.nhoa.homepage.model.HomePageStaticModel;
import com.wisdom.nhoa.meeting_new.activity.MeetingMainActivity;
import com.wisdom.nhoa.mine.model.LoginModel;
import com.wisdom.nhoa.sendreceive.activity.BacklogTabActivity;
import com.wisdom.nhoa.sendreceive.activity.ReceiveIssuedManagmentActivity;
import com.wisdom.nhoa.sendreceive.activity.SendManagementActivity;
import com.wisdom.nhoa.supervision.activity.SupervisionActivity;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.http_util.HttpUtil;
import com.wisdom.nhoa.util.http_util.callback.BaseModel;
import com.wisdom.nhoa.util.http_util.callback.JsonCallback;
import com.wisdom.nhoa.widget.DcTextViewRunNumber;
import com.wisdom.nhoa.widget.qrcode.CaptureActivity;

import okhttp3.Call;
import okhttp3.Response;

import static android.app.Activity.RESULT_FIRST_USER;

/**
 * @author LiXiaoDong
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.approval.activity
 * @class describe：
 * @time 2018-3-13
 * @change
 */

public class HomePageFragment extends Fragment implements View.OnClickListener {
    View view;
    public static final String TAG = HomePageFragment.class.getSimpleName();
    private LinearLayout ll_issued_management;
    private LinearLayout ll_incoming_message_management;
    private LinearLayout ll_announcement;
    private LinearLayout ll_documents_issuing;
    private LinearLayout ll_meeting;
    private LinearLayout ll_approval;
    private LinearLayout index_ll_draft;
    private LinearLayout ll_schedule;
    private LinearLayout index_ll_tocheck;
    private LinearLayout index_ll_todo;
    private LinearLayout index_ll_toread;
    private LinearLayout ll_watch;
    private DcTextViewRunNumber homepage_static_tv_todo;
    private DcTextViewRunNumber homepage_static_tv_tocheck;
    private DcTextViewRunNumber homepage_static_tv_toread;
    private DcTextViewRunNumber homepage_static_tv_draft;
    private TextView index_tv_title;

    private ImageView iv_scan;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.homepage_fragment_home_page, container, false);
        initView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getAndSetStaticData();//获取首页上的统计数据
    }

    private void initView() {
        ll_issued_management = view.findViewById(R.id.ll_issued_management);
        ll_incoming_message_management = view.findViewById(R.id.ll_incoming_message_management);
        ll_announcement = view.findViewById(R.id.ll_announcement);
        index_ll_toread = view.findViewById(R.id.index_ll_toread);
        ll_documents_issuing = view.findViewById(R.id.ll_documents_issuing);
        ll_meeting = view.findViewById(R.id.ll_meeting);
        ll_approval = view.findViewById(R.id.ll_approval);
        ll_watch = view.findViewById(R.id.ll_watch);
        ll_schedule = view.findViewById(R.id.ll_schedule);
        index_ll_draft = view.findViewById(R.id.index_ll_draft);
        homepage_static_tv_todo = view.findViewById(R.id.homepage_static_tv_todo);
        homepage_static_tv_toread = view.findViewById(R.id.homepage_static_tv_toread);
        homepage_static_tv_tocheck = view.findViewById(R.id.homepage_static_tv_tocheck);
        homepage_static_tv_draft = view.findViewById(R.id.homepage_static_tv_draft);
        index_tv_title = view.findViewById(R.id.index_tv_title);
        iv_scan = view.findViewById(R.id.iv_scan);
        index_ll_tocheck = view.findViewById(R.id.index_ll_tocheck);
        index_ll_todo = view.findViewById(R.id.index_ll_todo);
        index_ll_todo.setOnClickListener(this);
        ll_issued_management.setOnClickListener(this);
        ll_incoming_message_management.setOnClickListener(this);
        ll_announcement.setOnClickListener(this);
        ll_documents_issuing.setOnClickListener(this);
        ll_meeting.setOnClickListener(this);
        ll_watch.setOnClickListener(this);
        ll_approval.setOnClickListener(this);
        ll_schedule.setOnClickListener(this);
        index_ll_draft.setOnClickListener(this);
        index_ll_toread.setOnClickListener(this);
        index_ll_tocheck.setOnClickListener(this);
        iv_scan.setOnClickListener(this);
//        index_tv_title.setText(SharedPreferenceUtil.getUserInfo(getContext()).getOrganization());
        index_tv_title.setText(R.string.app_name);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_watch: {
                //TODO 督办
                startActivity(new Intent(getContext(), SupervisionActivity.class));

            }
            break;
            case R.id.ll_issued_management: {
                //发文管理
                startActivity(new Intent(getContext(), SendManagementActivity.class));
            }
            break;
            case R.id.ll_incoming_message_management: {
                //收文管理
                startActivity(new Intent(getContext(), ReceiveIssuedManagmentActivity.class));
                break;
            }

            case R.id.ll_announcement: {
                //公告
                startActivity(new Intent(getContext(), NoticeActivity.class));
            }
            break;

            case R.id.ll_documents_issuing: {
                //公文传阅
                startActivity(new Intent(getContext(), DocumentCirculatedListActivity.class));
            }
            break;

            case R.id.ll_meeting: {
                //会议
//                startActivity(new Intent(getContext(), MeetingActivity.class));
                //新版本会议入口
                startActivity(new Intent(getContext(), MeetingMainActivity.class));
            }
            break;

            case R.id.ll_schedule: {
                //日程
                startActivity(new Intent(getContext(), ScheduleActivity.class));
            }
            break;

            case R.id.ll_approval: {
                //审批
                startActivity(new Intent(getContext(), ApprovalActivity.class));
            }
            break;


            case R.id.index_ll_todo: {
                //待办
                startActivity(new Intent(getContext(), BacklogTabActivity.class));
            }
            break;
            case R.id.index_ll_tocheck: {
                //待审
                startActivity(new Intent(getContext(), ApprovalListActivity.class));
            }
            break;
            case R.id.index_ll_draft: {
                //草稿
                startActivity(new Intent(getActivity(), DraftActivity.class));
            }
            break;
            case R.id.index_ll_toread: {
                //待阅
                startActivity(new Intent(getContext(), ToBeReadActivity.class));
            }
            break;
            case R.id.iv_scan: {
                //扫码
                Intent openCameraIntent = new Intent(getContext(), CaptureActivity.class);
                startActivityForResult(openCameraIntent, RESULT_FIRST_USER);
            }
            break;
        }
    }

    /**
     * 获取并设置首页上的统计数据
     */
    private void getAndSetStaticData() {
        String token = ((LoginModel) SharedPreferenceUtil
                .getConfig(getContext())
                .getSerializable(ConstantString.USER_INFO))
                .getAccess_token();
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", token);
        HttpUtil.httpGet(ConstantUrl.HOME_PAGE_STATIC_DATA_URL, params, new JsonCallback<BaseModel<HomePageStaticModel>>() {

            @Override
            public void onSuccess(BaseModel<HomePageStaticModel> homePageStaticModelBaseModel, Call call, Response response) {
                HomePageStaticModel model = homePageStaticModelBaseModel.results;
                if (model.getBacklognum() != null) {
                    tvRunNumStartRun(homepage_static_tv_todo, model.getBacklognum());
                }
                if (model.getPendingnum() != null) {
                    tvRunNumStartRun(homepage_static_tv_tocheck, model.getPendingnum());
                }
                if (model.getNoticenum() != null) {
                    tvRunNumStartRun(homepage_static_tv_toread, model.getNoticenum());
                }
                if (model.getDraftnum() != null) {
                    tvRunNumStartRun(homepage_static_tv_draft, model.getDraftnum());
                }
            }
        });
    }

    /**
     * 扫码回调事件
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_FIRST_USER) {
            if (data == null) {
//                ToastUtil.showToast("请扫描正确二维码");
            }
        }
    }

    /**
     * 当数字大于50的时候
     * 开始滚动数字
     *
     * @param textViewRunNumber
     * @param num
     */
    private void tvRunNumStartRun(DcTextViewRunNumber textViewRunNumber, String num) {
        textViewRunNumber.setRunCount(Integer.parseInt(num));
        textViewRunNumber.setShowNum(num, 0);
        textViewRunNumber.startRun();
    }

}
