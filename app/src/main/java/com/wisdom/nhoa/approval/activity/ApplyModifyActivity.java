package com.wisdom.nhoa.approval.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.approval.adapter.LeaveTypeAdapter;
import com.wisdom.nhoa.approval.custom.PeopleTreeWindowWheel;
import com.wisdom.nhoa.approval.helper.FIlePopWindowHelper;
import com.wisdom.nhoa.approval.model.CopyToSelectedModel;
import com.wisdom.nhoa.approval.model.OrganizationModel;
import com.wisdom.nhoa.base.BaseActivity;
import com.wisdom.nhoa.util.DateUtilCustom;
import com.wisdom.nhoa.util.FileUtils;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.ToastUtil;
import com.wisdom.nhoa.util.U;
import com.wisdom.nhoa.util.http_util.HttpUtil;
import com.wisdom.nhoa.widget.CustomDatePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Response;

import static com.wisdom.nhoa.ConstantString.REQUEST_CAMERA;

/**
 * 类 {类名称}   添加请假
 *
 * @author lxd
 * @date 2018/7/13
 */
public class ApplyModifyActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    @BindView(R.id.tv_leave_type)
    TextView tvLeaveType;
    @BindView(R.id.tv_leave_days)
    EditText tvLeaveDays;
    @BindView(R.id.tvleave_reason)
    EditText tvleaveReason;
    @BindView(R.id.tv_approver)
    TextView tvApprover;
    @BindView(R.id.tv_remark)
    EditText tvRemark;
    @BindView(R.id.tv_accessory)
    TextView tvAccessory;
    @BindView(R.id.approval_ll_top_items)
    LinearLayout approvalLlTopItems;
    @BindView(R.id.comm_head_title)
    TextView commHeadTitle;
    @BindView(R.id.bt_apply_for)
    Button btApplyFor;
    @BindView(R.id.bt_select_file)
    Button btSelectFile;
    @BindView(R.id.tv_filename)
    TextView tv_filename;
    @BindView(R.id.tv_copy_to)
    TextView tv_copy_to;

    private OrganizationModel.ChiledBean chiledBean;
    @BindView(R.id.rl_parent)
    RelativeLayout rl_parent;

    private CustomDatePicker customDatePicker;
    private String currentTime = "";
    private int currentmark = 0;
    private String currentchosetime = "";
    private LeaveTypeAdapter adapter;
    JSONArray typeresult = new JSONArray();
    private String typeId = "";

    private List<Map<Integer, String>> ischeck;
    private String reminder = "";
    private CopyToSelectedModel copyToSelectedModel;
    public static final String TAG = ApplyModifyActivity.class.getSimpleName();

    private String mFilePath;
    private File updatefile;
    private File photoFile;
    private List<String> list=new ArrayList<>();
    @Override
    public void initViews() {
        setTitle(R.string.leave_apply_of_leave);
        //获取系统时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        currentchosetime = df.format(System.currentTimeMillis());
        customDatePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) {
                currentTime = time;
                if (currentmark == 0) {
                    tvStartTime.setText(time);
                } else {
                    tvEndTime.setText(time);
                }
//                //自动设置天数，不用手动输入
//                try {
//                    int days = DateUtilCustom.getDayLength(tvStartTime.getText().toString()
//                            , tvEndTime.getText().toString());
//                    tvLeaveDays.setText(days + "");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                try {
                    List<String> listtime=getDate(tvStartTime.getText().toString().substring(0,10),tvEndTime.getText().toString().substring(0,10));//遍历日期
                    int j =0;
                    for (int i=0;i<listtime.size();i++){
                        if (!isWeekend(listtime.get(i))){
                            j++;
                        }

                    }
                    tvLeaveDays.setText( ""+j);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
        }, "2000-01-01 00:00", "2050-01-01 00:00");
        mFilePath = Environment.getExternalStorageDirectory().getPath();// 获取SD卡路径
        //FIXME 临时文件夹
        mFilePath = mFilePath + System.currentTimeMillis() + ".png";// 指定路径
        customDatePicker.showSpecificTime(true); // 显示时和分
        customDatePicker.setIsLoop(true); // 允许循环滚动
        tvStartTime.setText(currentchosetime+" "+"08:00");
        tvEndTime.setText(currentchosetime+" "+"17:00");
        tvStartTime.setOnClickListener(this);
        tvEndTime.setOnClickListener(this);
        btApplyFor.setOnClickListener(this);
        tvLeaveType.setOnClickListener(this);
        tvApprover.setOnClickListener(this);
        tv_copy_to.setOnClickListener(this);
        btSelectFile.setOnClickListener(this);
        tvLeaveDays.setFocusable(false);
        tvLeaveDays.setText( "1");
    }

    @Override
    public void setlayoutIds() {
        setContentView(R.layout.activity_apply_modify);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_start_time: {//选择开始时间
                currentmark = 0;
                customDatePicker.show(tvStartTime.getText().toString());
            }
            break;
            case R.id.tv_end_time: {//选择结束时间
                currentmark = 1;
                customDatePicker.show(tvEndTime.getText().toString());
            }
            break;
            case R.id.bt_apply_for:
                //申请休假
                if (isEmpty()) {
                    addLeave();
                }
                break;
            case R.id.tv_copy_to: {
//                选择抄送人
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", copyToSelectedModel);
                Intent intent = new Intent(this, ChooseCopyToActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, ConstantString.COPY_TO_REQUEST_CODE);
            }
            break;
            case R.id.tv_leave_type:
                //获取请假类型
                getLeaveType();
                break;
            case R.id.tv_approver: {
                //选择申请人弹出
                WindowManager wm1 = this.getWindowManager();
                int height = wm1.getDefaultDisplay().getHeight();
                double statusBarHeight = Math.ceil(20 * this.getResources().getDisplayMetrics().density);
                //计算完毕后剪掉多余高度留下的结果
                int heightLeft = height / 2;
                PeopleTreeWindowWheel popWindowWheel = new PeopleTreeWindowWheel(this);
                popWindowWheel.setHeight(heightLeft);
                popWindowWheel.showAtLocation(rl_parent, Gravity.BOTTOM, 0, 0);
                popWindowWheel.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        backgroundAlpha(1f);
                    }
                });
                popWindowWheel.setListener(new PeopleTreeWindowWheel.onChildItemClikedListener() {
                    @Override
                    public void onChildItemClicked(OrganizationModel.ChiledBean childBean) {
                        ApplyModifyActivity.this.chiledBean = childBean;
                        tvApprover.setText(childBean.getName());
                    }
                });
                backgroundAlpha(0.5f);
            }
            break;
            //选择文件
            case R.id.bt_select_file:
                new FIlePopWindowHelper(this).showUploadPop(this);
                break;
        }
    }
    //遍历周期间隔并且拆分出日期
    private List<String> getDate(String beginDate, String endDate) throws ParseException {
        if (list!=null){
            list.clear();
        }
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        java.util.Calendar  calendar= java.util.Calendar.getInstance();
        calendar.setTime(sdf.parse(beginDate));
        for (long d =calendar.getTimeInMillis();d<=sdf.parse(endDate).getTime();d=get_add_1(calendar)){
            list.add(sdf.format(d));
        }
        return list;
    }

    private long get_add_1(java.util.Calendar c) {
        c.set(java.util.Calendar.DAY_OF_MONTH, c.get(java.util.Calendar.DAY_OF_MONTH)+1);
        return c.getTimeInMillis();
    }

    private static boolean isWeekend(String date) throws ParseException{
        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
        Date  date1=sf.parse(date);
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date1);
        if (calendar.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY||calendar.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY){
            return true;
        }
       return false;
    }
    //判断空值条件
    private boolean isEmpty() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        //将字符串形式的时间转化为Date类型的时间
        long starttime = 0;
        long endtime = 0;
        try {
            starttime = sdf.parse(tvStartTime.getText().toString()).getTime();
            endtime = sdf.parse(tvEndTime.getText().toString()).getTime();
            Log.e("天数比较", "starttime: " + starttime + "----endtime" + endtime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (tvStartTime.getText().toString().equals("")) {
            ToastUtil.showToast("起始时间不能为空");
            return false;
        } else if (tvEndTime.getText().toString().equals("")) {
            ToastUtil.showToast("结束时间不能为空");
            return false;
        } else if (starttime > endtime) {
            ToastUtil.showToast("起始时间不能大于结束时间");
            return false;
        } else if (typeId.equals("")) {
            ToastUtil.showToast("休假类型不能为空");
            return false;
        } else if (tvLeaveDays.getText().toString().equals("")) {
            ToastUtil.showToast("休假天数不能为空");
            return false;
        } else if (tvleaveReason.getText().toString().equals("")) {
            ToastUtil.showToast("休假理由不能为空");
            return false;
        } else if (chiledBean == null) {
            ToastUtil.showToast("审批人不能为空");
            return false;
        } else {
            return true;
        }
    }

    //申请请假
    public void addLeave() {
        U.showLoadingDialog(this);
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(this).getAccess_token());
        params.put("typeid", typeId);
        params.put("reminder", reminder);
        params.put("starttime", tvStartTime.getText().toString());
        params.put("endtime", tvEndTime.getText().toString());
        params.put("leaveday", tvLeaveDays.getText().toString());
        params.put("reason", tvleaveReason.getText().toString());
        params.put("auditor", chiledBean.getId());
        params.put("remark", tvRemark.getText().toString());
        if (updatefile != null) {
            params.put("file", updatefile);
        }
        HttpUtil.httpPostWithMultipart(ConstantUrl.ADD_LEAVE, params, new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                Log.e("请求成功", s);
                try {
                    U.closeLoadingDialog();
                    JSONObject jsonObject = new JSONObject(s);
                    String error_code = jsonObject.getString("error_code");
                    if (error_code.equals("0")) {
                        ToastUtil.showToast("申请成功");
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    //获取请假类型
    private void getLeaveType() {
        U.showLoadingDialog(this);
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(this).getAccess_token());
        HttpUtil.httpGet(ConstantUrl.LEAVE_TYPE, params, new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                try {
                    U.closeLoadingDialog();
                    JSONObject jsonObject = new JSONObject(s);
                    String error_code = jsonObject.getString("error_code");
                    if (error_code.equals("0")) {
                        typeresult = jsonObject.getJSONArray("results");
                        dialogLeaveType(typeresult);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
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
                tv_filename.setText(names1[names1.length - 1]);
            } else if (requestCode == REQUEST_CAMERA
                    && !"".equals(ConstantString.PIC_LOCATE)
                    && photoFile.exists()) {
                //当用户选择的是拍照时候的回调，向列表中插入正在上传的布局
                String[] names2 = ConstantString.PIC_LOCATE.split("/");
                tv_filename.setText(names2[names2.length - 1]);
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
//                选择抄送人的回调
                if (data != null) {
                    copyToSelectedModel = (CopyToSelectedModel) data.getExtras().getSerializable("data");
                    ischeck = copyToSelectedModel.getIscheck();
                    reminder = copyToSelectedModel.getId();
                    if (!"".equals(copyToSelectedModel.getName())) {
                        tv_copy_to.setText(copyToSelectedModel.getName());
                    }
                }
            }
            break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    // 选择dialog
    private void dialogLeaveType(final JSONArray array) {
        final Dialog dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        // 填充对话框的布局
        View inflate = LayoutInflater.from(this).inflate(R.layout.layout_date_year, null);
        // 初始化控件
        ListView listYear = (ListView) inflate.findViewById(R.id.listYear);
        TextView tv_title = (TextView) inflate.findViewById(R.id.tv_title);
        tv_title.setText("请选择请假类型");
        adapter = new LeaveTypeAdapter(this, array);
        listYear.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listYear.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    JSONObject typeObject = array.getJSONObject(position);
                    typeId = typeObject.getString("id");
                    tvLeaveType.setText(typeObject.getString("name"));
                    dialog.cancel();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Button btnCancel = (Button) inflate.findViewById(R.id.btnYearCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        // 将布局设置给Dialog
        dialog.setContentView(inflate);
        // 获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        // 设置Dialog从窗体底部弹出
        //dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0); // 消除边距
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 设置宽度充满屏幕
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        dialogWindow.setAttributes(lp);
        // lp.y = 20;// 设置Dialog距离底部的距离
        // 将属性设置给窗体
        dialog.show();// 显示对话框
    }

    private void backgroundAlpha(float f) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = f;
        getWindow().setAttributes(lp);
    }
}
