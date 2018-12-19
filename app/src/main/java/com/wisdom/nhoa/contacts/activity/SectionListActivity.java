package com.wisdom.nhoa.contacts.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.base.BaseActivity;
import com.wisdom.nhoa.contacts.adapter.DepartmentListAdapter;
import com.wisdom.nhoa.contacts.model.DepartmentListModel;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.U;
import com.wisdom.nhoa.util.http_util.HttpUtil;
import com.wisdom.nhoa.util.http_util.callback.BaseModel;
import com.wisdom.nhoa.util.http_util.callback.JsonCallback;

import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.contacts.activity
 * @class describe：部门列表页
 * @time 2018/3/25 15:34
 * @change
 */

public class SectionListActivity extends BaseActivity {
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.tv_hint)
    TextView tv_hint;
    @BindView(R.id.ll_department)
    LinearLayout llDepartment;
    @BindView(R.id.lv_department)
    ListView lvDepartment;
    private DepartmentListModel departmentListModel;
    private String content;

    @Override
    public void initViews() {
        setTitle(R.string.tab_addressbook);
        if (getIntent() != null) {
            departmentListModel = (DepartmentListModel) getIntent().getExtras().getSerializable("data");
            llSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(SectionListActivity.this, SearchContactsActivity.class));
                }
            });
            TextView tv_department = new TextView(this);
            content = SharedPreferenceUtil.getUserInfo(this).getOrganization() + ">" + departmentListModel.getDepname();
            int posColor = SharedPreferenceUtil.getUserInfo(this).getOrganization().length() + 1;
//            StrUtils.changeTheKeyTextColor2(SectionListActivity.this
//                    , content
//                    , 0
//                    , posColor);
            tv_department.setTextSize(18);
            tv_department.setTextColor(getResources().getColor(R.color.department_color));
            tv_department.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                    , ViewGroup.LayoutParams.WRAP_CONTENT));
            tv_department.setText(content);
            llDepartment.addView(tv_department);
            getAndSetData(departmentListModel.getDepid());
        }
    }

    @Override
    public void setlayoutIds() {
        setContentView(R.layout.activity_department);
    }


    /**
     * 访问接口，设置数据到界面
     */
    public void getAndSetData(String depid) {
        U.showLoadingDialog(this);
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("depid", depid);
        params.put("access_token"
                , SharedPreferenceUtil.getUserInfo(this).getAccess_token());
        HttpUtil.httpGet(ConstantUrl.SECTION_LIST_URL, params, new JsonCallback<BaseModel<List<DepartmentListModel>>>() {

            @Override
            public void onSuccess(final BaseModel<List<DepartmentListModel>> listBaseModel, Call call, Response response) {
                U.closeLoadingDialog();
                if (listBaseModel.results.size() > 0) {
                    tv_hint.setVisibility(View.GONE);
                    lvDepartment.setVisibility(View.VISIBLE);
                    lvDepartment.setAdapter(new DepartmentListAdapter(SectionListActivity.this, listBaseModel.results));
                    lvDepartment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            DepartmentListModel model = listBaseModel.results.get(position);
                            Intent intent = new Intent(SectionListActivity.this, StaffListActivity.class);
                            Bundle bundle = new Bundle();
                            intent.putExtra("content", content);
                            bundle.putSerializable("data", model);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                } else {
                    tv_hint.setVisibility(View.VISIBLE);
                    lvDepartment.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                U.closeLoadingDialog();
                tv_hint.setVisibility(View.VISIBLE);
                lvDepartment.setVisibility(View.GONE);
            }
        });

    }

}
