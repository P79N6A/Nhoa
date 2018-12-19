package com.wisdom.nhoa.util;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.wisdom.nhoa.util.http_util.HttpUtil;

/**
 * Created by hanxuefeng on 2017/11/2.
 */

public class UtilCustom {
    /**
     * 上传文件的相关方法
     *
     * @param url
     * @param params
     * @param callback
     */
    public static void uploadFiles(String url, HttpParams params, StringCallback callback) {
        OkGo.post(url)
                .params(params)
                .isMultipart(true)
                .cacheMode(CacheMode.DEFAULT)
                .execute(callback);
    }


}
