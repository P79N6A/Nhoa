package com.wisdom.nhoa.util;

import android.content.Context;

import com.lzy.okgo.model.HttpParams;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.homepage.model.UserPermissionModel;
import com.wisdom.nhoa.util.http_util.HttpUtil;
import com.wisdom.nhoa.util.http_util.callback.BaseModel;
import com.wisdom.nhoa.util.http_util.callback.JsonCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.util
 * @class describe：（1）从接口获得用户权限列表，本地化，（2）查询
 * @time 2018/11/14 9:09
 * @change
 */
public class UserPermissionHelper {
    /**
     * 访问接口
     * 获得当前用户权限
     * 本地化用户权限列表
     */
    public static void getUserPermission(final Context context) {
        HttpParams params = new HttpParams();
        params.put("appkey", ConstantString.APP_KEY);
        params.put("access_token", SharedPreferenceUtil.getUserInfo(context).getAccess_token());
        HttpUtil.httpGet(ConstantUrl.GET_USER_PERMISSION_URL, params, new JsonCallback<BaseModel<List<UserPermissionModel>>>() {
            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);

            }

            @Override
            public void onSuccess(BaseModel<List<UserPermissionModel>> listBaseModel, Call call, Response response) {
                //获得用户权限列表，并存进行本地化
                //存储用户权限信息到sp文件
                SharedPreferenceUtil
                        .getConfig(context)
                        .putSerializable(ConstantString.USER_PERMISSION, listBaseModel.results);
            }
        });
    }


    /**
     * 根据权限名称查询指定权限的授予情况
     *
     * @param permissionName 权限名称
     * @return 权限是否可访问  （0 false 不可见，1 true 可见）
     */
    public static Boolean getUserPermissionStatus(Context context, String permissionName) {
        String permissionStatus = "";
        Boolean isAccredit = false;
        List<UserPermissionModel> userPermissionModelList = new ArrayList<>();
        userPermissionModelList = SharedPreferenceUtil.getUserPermission(context);
        if (userPermissionModelList != null) {
            for (int i = 0; i < userPermissionModelList.size(); i++) {
                if (permissionName.equals(userPermissionModelList.get(i).getNode_name())) {
                    //本地权限数据 匹配待验证权限参数 成功，赋值权限结果
                    permissionStatus = userPermissionModelList.get(i).getStatus();
                }
            }
        } else {
            ToastUtil.showToast("获取本地用户权限异常");
        }
        if ("1".equals(permissionStatus)) {
            isAccredit = true;
        }
        if(!isAccredit){
            ToastUtil.showToast("您暂未获得该操作权限");
        }
        return isAccredit;
    }
}
