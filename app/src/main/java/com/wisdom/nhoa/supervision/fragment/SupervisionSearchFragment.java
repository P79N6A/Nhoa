package com.wisdom.nhoa.supervision.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.base.fragment.NoTitleBaseFragment;
import com.wisdom.nhoa.mine.model.LoginModel;
import com.wisdom.nhoa.supervision.activity.SupervisionSearchDetailActivity;
import com.wisdom.nhoa.supervision.adapter.SupervisionAdapter;
import com.wisdom.nhoa.supervision.model.SupervisionModel;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.U;
import com.wisdom.nhoa.util.http_util.HttpUtil;
import com.wisdom.nhoa.util.http_util.callback.BaseModel;
import com.wisdom.nhoa.util.http_util.callback.JsonCallback;
import com.wisdom.nhoa.widget.PullToRefreshLayout;
import com.wisdom.nhoa.widget.PullableListView;

import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Response;


public class SupervisionSearchFragment extends NoTitleBaseFragment {
    private static final int SEARCHREQUESTCODE = 1800;
    @BindView(R.id.ptr_ListView)
    PullableListView ptr_ListView;
    @BindView(R.id.ptr_refresh_view)
    PullToRefreshLayout pull;
    @BindView(R.id.tv_no_data)
    TextView tvNoData;
    @BindView(R.id.tv_error_data)
    TextView tvErrorData;
    private RefreshReceiver refreshReceiver;
    private boolean isfirst = true;
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private SupervisionFragment.OnListFragmentInteractionListener mListener;

    public static SupervisionSearchFragment newInstance() {
        SupervisionSearchFragment fragment = new SupervisionSearchFragment();
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isfirst) {
            getData(pull);
            isfirst = false;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_supervision_search_layout;
    }

    @Override
    public void initView() {
        //注册广播，刷新数据
        refreshReceiver = new RefreshReceiver();
        getContext().registerReceiver(refreshReceiver, new IntentFilter(ConstantString.BROADCAST_REFRESH_SUPERVISION_RIGHT));
        pull.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                getData(pullToRefreshLayout);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

            }
        });
    }

    /**
     * 请求接口获得列表数据
     */
    private void getData(final PullToRefreshLayout pullToRefreshLayout) {
        U.showLoadingDialog(getContext());
        String token = ((LoginModel) SharedPreferenceUtil
                .getConfig(getContext())
                .getSerializable(ConstantString.USER_INFO))
                .getAccess_token();
        HttpParams httpParams = new HttpParams();
        httpParams.put("appkey", ConstantString.APP_KEY);
        httpParams.put("access_token", token);
        HttpUtil.httpGet(ConstantUrl.SUPERVISED_LIST_URL, httpParams, new JsonCallback<BaseModel<List<SupervisionModel>>>() {
            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);
                U.closeLoadingDialog();
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                tvNoData.setVisibility(View.GONE);
                tvErrorData.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(final BaseModel<List<SupervisionModel>> listBaseModel, Call call, Response response) {
                U.closeLoadingDialog();
                if (listBaseModel.results.size() > 0) {
                    ptr_ListView.setAdapter(new SupervisionAdapter(getActivity(), listBaseModel.results));
                    //子项点击事件，跳转到详情页面
                    ptr_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("data", listBaseModel.results.get(position));
                            Intent intent = new Intent(getActivity(), SupervisionSearchDetailActivity.class);
                            intent.putExtras(bundle);
                            startActivityForResult(intent, SEARCHREQUESTCODE);
                        }
                    });
                } else {
                    tvNoData.setVisibility(View.VISIBLE);
                    tvErrorData.setVisibility(View.GONE);
                }
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);

            }
        });


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SEARCHREQUESTCODE) {
            if (resultCode == 0) {
                getData(pull);
            }


        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener1 {

        void onListFragmentInteraction(String item);
    }

    private class RefreshReceiver extends BroadcastReceiver {
        /**
         * 接数据参数的Receiver
         *
         * @param context
         * @param intent
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            getData(pull);
        }
    }
}
