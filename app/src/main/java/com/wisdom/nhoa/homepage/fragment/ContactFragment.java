package com.wisdom.nhoa.homepage.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.contacts.activity.DepartmentListActivity;
import com.wisdom.nhoa.contacts.activity.SearchContactsActivity;
import com.wisdom.nhoa.contacts.helper.ContactsHelper;
import com.wisdom.nhoa.homepage.adapter.LatelyPersonAdapter;
import com.wisdom.nhoa.homepage.model.LatelyPersonModel;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.ToastUtil;
import com.wisdom.nhoa.util.http_util.HttpUtil;
import com.wisdom.nhoa.util.http_util.callback.BaseModel;
import com.wisdom.nhoa.util.http_util.callback.JsonCallback;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * author：
 * date:
 */

public class ContactFragment extends Fragment implements View.OnClickListener {
    View view;
    private LinearLayout ll_search;
    private ListView lv_contact;
    private TextView comm_head_title;
    private TextView tv_hint;
    private TextView tv_orgnization;
    private ImageView iv_back;
    private String[] permissions = {Manifest.permission.CALL_PHONE};
    private String tel = "";
    private String userId = "";
    private TextView tv_next;
    public static final String TAG = ContactFragment.class.getSimpleName();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.homepage_fragment_contact, container, false);
        tv_hint = view.findViewById(R.id.tv_hint);
        tv_next = view.findViewById(R.id.tv_next);
        initView();
        return view;
    }

    private void initView() {
        iv_back = view.findViewById(R.id.head_back_iv);
        iv_back.setVisibility(View.GONE);
        ll_search = view.findViewById(R.id.ll_search);
        ll_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), SearchContactsActivity.class), ConstantString.CONTRACT_REFRESH);
            }
        });
        lv_contact = view.findViewById(R.id.lv_contact);
        comm_head_title = view.findViewById(R.id.comm_head_title);
        tv_orgnization = view.findViewById(R.id.tv_orgnization);
        comm_head_title.setText("通讯录");
        tv_orgnization.setText(SharedPreferenceUtil.getUserInfo(getContext()).getOrganization());
        tv_next.setOnClickListener(this);
        getAndSetLatelyPerson();

    }


    /**
     * 获得常用联系人列表
     */
    private void getAndSetLatelyPerson() {
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(getContext()).getAccess_token());
        HttpUtil.httpGet(ConstantUrl.GET_LATELY_PERSON, params, new JsonCallback<BaseModel<List<LatelyPersonModel>>>() {

            @Override
            public void onSuccess(final BaseModel<List<LatelyPersonModel>> listData, Call call, Response response) {
                if (listData.results.size() == 0) {
                    tv_hint.setVisibility(View.VISIBLE);
                } else {
                    tv_hint.setVisibility(View.GONE);
                    lv_contact.setAdapter(new LatelyPersonAdapter(getContext(), listData.results));
                    lv_contact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                            Log.i(TAG, "listViewSize: " + listData.results.size());
                            Log.i(TAG, "pos: " + pos);
                            tel = listData.results.get(pos).getPhone();
                            userId = listData.results.get(pos).getUserid();
                            //申请拨打电话权限
                            ContactFragment.this.requestPermissions(permissions, 321);
                        }
                    });
                }
            }
        });
    }


    /**
     * 申请打电话权限回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "onRequestPermissionsResult: " + tel);
            if (!"".equals(tel)) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                Uri data = Uri.parse("tel:" + tel);
                intent.setData(data);
                saveDailPerson();
                startActivity(intent);
            } else {
                ToastUtil.showToast("该联系人暂无电话");
            }
        } else {
            ToastUtil.showToast(R.string.permission_fail_hint);
        }
    }

    /**
     * 保存打过电话的联系人
     */
    private void saveDailPerson() {
        if (!"".equals(userId)) {
            ContactsHelper.saveContactPerson(getContext(), userId, new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    Log.i(TAG, "Error: " + "保存打电话接口成功");
                }
            });
        } else {
            Log.e(TAG, "Error: " + "保存打电话接口，人员Id空");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_next: {
                //组织架构点击事件
                startActivityForResult(new Intent(getContext(), DepartmentListActivity.class), ConstantString.CONTRACT_REFRESH);
            }
            break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getAndSetLatelyPerson();
    }
}
