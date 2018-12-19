package com.wisdom.nhoa.contacts.helper;

import android.content.Context;
import android.util.Log;

import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.http_util.HttpUtil;

import okhttp3.Call;
import okhttp3.Response;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.contacts.helper
 * @class describe：
 * @time 2018/3/30 10:16
 * @change
 */

public class ContactsHelper {
    public static final String TAG=ContactsHelper.class.getSimpleName();
    /**
     * 保存打过电话的联系人
     * @param userId
     * @param stringCallback
     */
    public static void  saveContactPerson(Context context,String userId, StringCallback stringCallback){
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(context).getAccess_token());
        params.put("userid", userId);
        HttpUtil.httpGet(ConstantUrl.SAVE_DAIL_CONTRACT, params, stringCallback);
    }
}
