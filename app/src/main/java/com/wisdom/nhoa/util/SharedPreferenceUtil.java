package com.wisdom.nhoa.util;

import android.content.Context;

import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.homepage.model.UserPermissionModel;
import com.wisdom.nhoa.mine.model.LoginModel;

import java.util.List;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.util
 * @class describe：
 * @time 2018/3/25 11:42
 * @change
 */

public class SharedPreferenceUtil {


    public static PrefsConfig getConfig(Context context) {
        return PrefsConfig.getPrefsConfig(new PrefsConfig.Config(
                ConstantString.SHARE_PER_INFO, 0), context);
    }


    /**
     * 返回用户登录后的信息
     *
     * @param context
     * @return
     */
    public static LoginModel getUserInfo(Context context) {
        return ((LoginModel) getConfig(context).getSerializable(ConstantString.USER_INFO));
    }

    /**
     * 返回用户权限信息
     *
     * @param context
     * @return
     */
    public static List<UserPermissionModel> getUserPermission(Context context) {
        return ((List<UserPermissionModel>) getConfig(context).getSerializable(ConstantString.USER_PERMISSION));
    }

}
