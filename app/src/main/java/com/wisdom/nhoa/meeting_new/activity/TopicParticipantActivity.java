package com.wisdom.nhoa.meeting_new.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.wisdom.nhoa.R;
import com.wisdom.nhoa.base.BaseActivity;
import com.wisdom.nhoa.homepage.adapter.ParticpantAdapter;
import com.wisdom.nhoa.meeting_new.model.ParticpantModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author lxd
 * @ProjectName project：
 * @class package：
 * @class describe：从参会人员中选择参加议题的人员
 * @time 17:30
 * @change
 */
public class TopicParticipantActivity extends BaseActivity {

    @BindView(R.id.lv_participant)
    ListView lvParticipant;
    @BindView(R.id.bt_create_meeting)
    Button btCreateMeeting;

    private ParticpantAdapter adapter;
    private String memberid = "";
    private String username = "";

    private String TAG = "partic";
    public List<ParticpantModel> particpantModelList = new ArrayList<>();

    @Override
    public void initViews() {
        setTitle(R.string.index_meeting_participant_title);
        lvParticipant.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ParticpantAdapter.ParticpantHolder holder = (ParticpantAdapter.ParticpantHolder) view.getTag();
                if (ParticpantAdapter.ischeck.get(position).equals("true")) {
                    ParticpantAdapter.ischeck.put(position, "false");
                    holder.iv_checkbox.setImageDrawable(getResources().getDrawable(R.mipmap.checkbox));
                } else {
                    ParticpantAdapter.ischeck.put(position, "true");
                    holder.iv_checkbox.setImageDrawable(getResources().getDrawable(R.mipmap.checkbox_pre));
                }
            }
        });
        btCreateMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < particpantModelList.size(); i++) {
                    if (ParticpantAdapter.ischeck.get(i).equals("true")) {
                        memberid = memberid + particpantModelList.get(i).getUser_id() + ",";
                        username = username + particpantModelList.get(i).getUser_name() + ",";
                    }

                }
                if (!"".equals(memberid)) {
                    memberid = memberid.substring(0, memberid.length() - 1);
                    username = username.substring(0, username.length() - 1);
                }

                //数据是使用Intent返回
                Intent intent = new Intent();
                //把返回数据存入Intent
                intent.putExtra("memberid", memberid);
                intent.putExtra("username", username);
                TopicParticipantActivity.this.setResult(RESULT_OK, intent);
                //关闭Activity
                TopicParticipantActivity.this.finish();

            }
        });
        getdata();
    }

    @Override
    public void setlayoutIds() {
        setContentView(R.layout.activity_participant);
    }

    private void getdata() {
        if (getIntent() != null) {
            ArrayList<ParticpantModel> listdata = new ArrayList<>();
            listdata = (ArrayList<ParticpantModel>) getIntent().getExtras().getSerializable("data");
            particpantModelList = listdata;
            adapter = new ParticpantAdapter(TopicParticipantActivity.this, listdata);
            lvParticipant.setAdapter(adapter);
        }
    }
}
