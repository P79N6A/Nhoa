package com.wisdom.nhoa.homepage.callback;

import com.wisdom.nhoa.meeting_new.model.ParticpantModel;

import java.util.HashMap;
import java.util.List;

public interface ParticpantCallBack {
    void getdata(List<ParticpantModel> listdata,List<HashMap<String, Object>> list);
}
