package com.wisdom.nhoa.homepage.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.baidu.android.pushservice.PushManager;
import com.noober.menu.FloatMenu;
import com.wisdom.nhoa.AppApplication;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.base.fragment.BaseFragment;
import com.wisdom.nhoa.homepage.activity.HomePageActivity;
import com.wisdom.nhoa.homepage.adapter.MsgListAdapter;
import com.wisdom.nhoa.homepage.model.MsgListModel;
import com.wisdom.nhoa.push.activity.PushDetailActivity;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.dbHelper.DbHelperCustom;
import com.wisdom.nhoa.util.greendao.MsgListModelDao;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * author：zhanglichao
 * date:
 */

public class MsgFragment extends BaseFragment implements MsgListAdapter.OnMsgItemClickListener, MsgListAdapter.OnMsgItemLongClickListener {
    private static final int MSGDETAILREQUEST = 100;
    private RecyclerView recyclerView;
    private MsgListAdapter msgListAdapter;
    @BindView(R.id.tv_no_data)
    TextView tv_no_data;
    List<MsgListModel> listData = new ArrayList<>();
    List<MsgListModel> notifydatalist = new ArrayList<>();
    private Point point = new Point();
    public HomePageActivity.MyOnTouchListener myOnTouchListener = new HomePageActivity.MyOnTouchListener() {
        @Override
        public boolean onTouch(MotionEvent ev) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                point.x = (int) ev.getRawX();
                point.y = (int) ev.getRawY();
            }
            return true;
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.homepage_fragment_msg;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(HomePageActivity.UPDATAMSGCOUNTS);
        getActivity().registerReceiver(receiver, intentFilter);
        ((HomePageActivity) getActivity()).registerMyOnTouchListener(myOnTouchListener);
    }

    @Override
    public void initView() {
        setTitle("消息");
        hideBackIv();
        recyclerView = rootView.findViewById(R.id.fragment_msg_recycler_view);

        //冲数据库直接查询数据加载到列表
//        listData = AppApplication.getDaosession().getMsgListModelDao().queryBuilder().orderDesc(MsgListModelDao.Properties.Id).list();
        listData= DbHelperCustom.queryList(AppApplication.getInstance().GetSqliteDataBase(),getActivity());
        notifydatalist = listData;
        if (notifydatalist.size() == 0) {//出现无数据显示
            tv_no_data.setVisibility(View.VISIBLE);
        }
        msgListAdapter = new MsgListAdapter(getContext(), notifydatalist);
        recyclerView.setAdapter(msgListAdapter);
        msgListAdapter.setOnMsgItemClickListener(this);
        msgListAdapter.setOnMsgItemLongClickListener(this);

    }

    /**
     * 列表子项点击事件
     *
     * @param recyclerView
     * @param pos
     * @param clickedView
     * @param msgListModel
     */
    @Override
    public void onMsgItemClicked(RecyclerView recyclerView, int pos, View clickedView, MsgListModel msgListModel) {
        //清除掉任务栏指定的Notification
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancelAll();


        Intent intent = new Intent(getActivity(), PushDetailActivity.class);
        intent.putExtra("messTypeCode", msgListModel.getMessTypeCode());
        intent.putExtra("dataId", msgListModel.getDataId());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(intent);

    }

    //接受消息广播
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(HomePageActivity.UPDATAMSGCOUNTS)) {
                UpDataMsgList();
            }
        }
    };

    private void UpDataMsgList() {//收到广播刷新列表
//        listData = AppApplication.getDaosession().getMsgListModelDao().queryBuilder().orderDesc(MsgListModelDao.Properties.Id).list();
        listData= DbHelperCustom.queryList(AppApplication.getInstance().GetSqliteDataBase(),getActivity());
        if (notifydatalist != null) {
            notifydatalist.clear();
            notifydatalist.addAll(listData);
        } else {
            notifydatalist.addAll(listData);
        }
        if (notifydatalist.size() != 0) {
            tv_no_data.setVisibility(View.GONE);
        } else {
            if (tv_no_data != null) {
                tv_no_data.setVisibility(View.VISIBLE);
            }
        }
        msgListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
        ((HomePageActivity) getActivity()).unregisterMyOnTouchListener(myOnTouchListener);
    }


    @Override
    public void onMsgItemLongClicked(RecyclerView recyclerView, int pos, View clickedView, final MsgListModel msgListModel) {
//
        final FloatMenu floatMenu = new FloatMenu(getActivity());
        floatMenu.items("删除该条消息");
        floatMenu.setOnItemClickListener(new FloatMenu.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                if (position == 0) {
                    showDiolag(msgListModel);
                    floatMenu.dismiss();
                }
            }
        });
        floatMenu.show(point);
//
    }

    private void showDiolag(final MsgListModel msgListModel) {
        final Dialog alertDialog = new AlertDialog.Builder(getActivity()).setMessage("删除消息后不可恢复！")
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        MsgListModel model = AppApplication.getDaosession().getMsgListModelDao().queryBuilder().where(MsgListModelDao.Properties.DataId.eq(msgListModel.getDataId())).unique();
//                        AppApplication.getDaosession().getMsgListModelDao().delete(model);
                        DbHelperCustom.deleteById(AppApplication.getInstance().GetSqliteDataBase(),getActivity(),msgListModel.getDataId());
                        //发送
                        Intent intent = new Intent();
                        intent.setAction(HomePageActivity.UPDATAMSGCOUNTS);
                        context.sendBroadcast(intent);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create();
        alertDialog.show();
    }


}
