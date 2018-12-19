package com.wisdom.nhoa.contacts.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.base.BaseActivity;
import com.wisdom.nhoa.contacts.adapter.SearchResultAdpater;
import com.wisdom.nhoa.contacts.adapter.StaffListAdapter;
import com.wisdom.nhoa.contacts.helper.ContactsHelper;
import com.wisdom.nhoa.contacts.model.SearchResultModel;
import com.wisdom.nhoa.contacts.model.StaffListModel;
import com.wisdom.nhoa.homepage.adapter.LatelyPersonAdapter;
import com.wisdom.nhoa.homepage.fragment.ContactFragment;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.ToastUtil;
import com.wisdom.nhoa.util.U;
import com.wisdom.nhoa.util.http_util.HttpUtil;
import com.wisdom.nhoa.util.http_util.callback.BaseModel;
import com.wisdom.nhoa.util.http_util.callback.JsonCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Response;

import static com.lzy.okgo.OkGo.getContext;

/**
 * 搜索页面
 */
public class SearchContactsActivity extends BaseActivity {
    @BindView(R.id.et_contact_search)
    EditText contact_search;
    @BindView(R.id.tv_search_list)
    ListView search_list;
    @BindView(R.id.tv_hint)
    TextView tv_hint;
    private String[] permissions = {Manifest.permission.CALL_PHONE};
    public static final String TAG = StaffListActivity.class.getSimpleName();
    private String tel;
    List<SearchResultModel> mdata=new ArrayList<>();
    SearchResultAdpater searchResultAdpater;
    @Override
    public void initViews() {
        setTitle("通讯录检索");
        contact_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String contact_searchS=contact_search.getText().toString();
                if(TextUtils.isEmpty(contact_searchS)){
                    return;
                }
                getAndSetData(contact_searchS);
            }
        });
        searchResultAdpater= new SearchResultAdpater(SearchContactsActivity.this, mdata);
        search_list.setAdapter(searchResultAdpater);
        search_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tel="tel:" + mdata.get(position).getPhone();
                saveDailPerson(mdata.get(position).getUserid());
                //申请拨打电话权限
                ActivityCompat.requestPermissions(SearchContactsActivity.this, permissions, 321);
            }
        });
    }

    @Override
    public void setlayoutIds() {
        setContentView(R.layout.activity_search_contacts);
    }
    /**
     * 访问接口，设置数据到界面
     */
    public void getAndSetData(String usename) {
        U.showLoadingDialog(this);
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("username", usename);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(this).getAccess_token());
        HttpUtil.httpGet(ConstantUrl.GETCONTACTLIST, params, new JsonCallback<BaseModel<List<SearchResultModel>>>() {

            @Override
            public void onSuccess(final BaseModel<List<SearchResultModel>> listBaseModel, Call call, Response response) {
                U.closeLoadingDialog();



                if (listBaseModel.results.size() == 0) {
                    tv_hint.setVisibility(View.VISIBLE);
                    List<SearchResultModel> infos = new ArrayList<SearchResultModel>();
                    mdata.clear();
                    mdata.addAll(infos);
                    searchResultAdpater.notifyDataSetChanged();
                } else {
                    tv_hint.setVisibility(View.GONE);
                       mdata.clear();
                        List<SearchResultModel> infos = new ArrayList<SearchResultModel>();
                        for (SearchResultModel userInfo : listBaseModel.results) {
                                infos.add(userInfo);
                        }
                        mdata.addAll(infos);
                        searchResultAdpater.notifyDataSetChanged();

                }

            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                U.closeLoadingDialog();
            }
        });

    }
    /**
     * 申请打电话权限回调
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "onRequestPermissionsResult: "+tel);

            Intent intent = new Intent(Intent.ACTION_CALL);
            Uri data = Uri.parse(tel);
            intent.setData(data);
            startActivity(intent);
        } else {
            ToastUtil.showToast(R.string.permission_fail_hint);
        }
    }
    private void saveDailPerson(String userId) {
        if (!"".equals(userId)) {
            ContactsHelper.saveContactPerson(this, userId, new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    Log.i(TAG, "Error: " + "保存打电话接口成功");
                }
            });
        } else {
            Log.e(TAG, "Error: " + "保存打电话接口，人员Id空");
        }
    }


}
