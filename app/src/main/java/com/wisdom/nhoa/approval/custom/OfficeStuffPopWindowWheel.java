package com.wisdom.nhoa.approval.custom;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.lzy.okgo.model.HttpParams;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.approval.adapter.PopWheelStuffNameAdapter;
import com.wisdom.nhoa.approval.adapter.PopWheelStuffSubjectAdapter;
import com.wisdom.nhoa.approval.model.OfficeStuffModel;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.ToastUtil;
import com.wisdom.nhoa.util.U;
import com.wisdom.nhoa.util.http_util.HttpUtil;
import com.wisdom.nhoa.util.http_util.callback.BaseModel;
import com.wisdom.nhoa.util.http_util.callback.JsonCallback;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.approval.custom
 * @class describe：自定义办公用品选择弹出,滚轮形式的
 * @time 2018/7/13 17:13
 * @change
 */
public class OfficeStuffPopWindowWheel extends PopupWindow {
    Context mContext;
    private ListView lvStuffSubject;
    private ListView lvStuffName;
    private LayoutInflater mInflater;
    private View mContentView;
    private onChildItemClikedListener listener;

    public void setListener(onChildItemClikedListener listener) {
        this.listener = listener;
    }

    public OfficeStuffPopWindowWheel(Context context) {
        super(context);
        this.mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView = mInflater.inflate(R.layout.pop_wheel_office_stuff_selection, null);
        lvStuffSubject = mContentView.findViewById(R.id.lv_stuff_subject);
        lvStuffName = mContentView.findViewById(R.id.lv_stuff_name);
        //设置View
        setContentView(mContentView);
        //设置宽与高
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);


        /**
         * 设置进出动画
         */
//        FIXME 设置进出动画
        setAnimationStyle(R.style.PopupWindowAnimation);


        /**
         * 设置背景只有设置了这个才可以点击外边和BACK消失
         */
        setBackgroundDrawable(new ColorDrawable());


        /**
         * 设置可以获取集点
         */
        setFocusable(true);

        /**
         * 设置点击外边可以消失
         */
        setOutsideTouchable(true);

        /**
         *设置可以触摸
         */
        setTouchable(true);


        /**
         * 设置点击外部可以消失
         */

        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                /**
                 * 判断是不是点击了外部
                 */
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    return true;
                }
                //不是点击外部
                return false;
            }
        });


        /**
         * 初始化View与监听器
         */
        initView();

        initListener();
    }


    /**
     * 初始化View
     */
    private void initView() {
        getDataList();
    }

    /**
     * 初始化监听
     */
    private void initListener() {

    }

    /**
     * 通过网络，获取数据源
     */
    private void getDataList() {
        U.showLoadingDialog(mContext);
        HttpParams httpParams = new HttpParams();
        httpParams.put("appkey", ConstantString.APP_KEY);
        httpParams.put("access_token", SharedPreferenceUtil.getUserInfo(mContext).getAccess_token());
        HttpUtil.httpGet(ConstantUrl.GET_OFFICE_STUFF_LIST, httpParams, new JsonCallback<BaseModel<List<OfficeStuffModel>>>() {
            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                U.closeLoadingDialog();
            }

            @Override
            public void onSuccess(final BaseModel<List<OfficeStuffModel>> listBaseModel, Call call, Response response) {
                U.closeLoadingDialog();
                if (listBaseModel.results.size() > 0) {
                    final PopWheelStuffSubjectAdapter popWheelStuffSubjectAdapter = new PopWheelStuffSubjectAdapter(mContext, listBaseModel.results);
                    lvStuffSubject.setAdapter(popWheelStuffSubjectAdapter);
                    lvStuffSubject.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            popWheelStuffSubjectAdapter.setSelectedPosition(position);
                            popWheelStuffSubjectAdapter.notifyDataSetInvalidated();
                            final List<OfficeStuffModel.ChildBean> listChildBean = listBaseModel.results.get(position).getChild();
                            lvStuffName.setAdapter(new PopWheelStuffNameAdapter(mContext, listChildBean));
                            lvStuffName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    if (listener != null) {
                                        listener.onChildItemClicked(listChildBean.get(position));
                                        OfficeStuffPopWindowWheel.this.dismiss();
                                    }
                                }
                            });
                        }
                    });
                    lvStuffSubject.performItemClick(lvStuffSubject.getChildAt(0), 0, lvStuffSubject.getItemIdAtPosition(0));
                } else {
                    ToastUtil.showToast("暂无数据");
                }
            }
        });
    }

    public interface onChildItemClikedListener {
        void onChildItemClicked(OfficeStuffModel.ChildBean childBean);
    }
}
