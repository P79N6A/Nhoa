package com.wisdom.nhoa.sendreceive.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.base.BroadCastManager;
import com.wisdom.nhoa.sendreceive.model.FileContentModel;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.util.http_util.HttpUtil;
import com.wisdom.nhoa.util.http_util.callback.BaseModel;
import com.wisdom.nhoa.util.http_util.callback.JsonCallback;
import com.wisdom.nhoa.widget.fileview.LoadFileModel;
import com.wisdom.nhoa.widget.fileview.Md5Tool;
import com.wisdom.nhoa.widget.fileview.SuperFileView2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 公文正文
 */
public class FileContentFragment extends Fragment {

    private View view;
    SuperFileView2 mSuperFileView;
    String filePath;
    public static final String TAG = com.wisdom.nhoa.sendreceive.fragment.FileContentFragment.class.getSimpleName();
    private String insid;
    private MyReceiver receiver;
    private TextView tv_no_data_hint;

    @android.support.annotation.Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @android.support.annotation.Nullable ViewGroup container, @android.support.annotation.Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_file_content, container, false);
        mSuperFileView =view.findViewById(R.id.mSuperFileView);
        tv_no_data_hint = view.findViewById(R.id.tv_no_data_hint);
        receiver = new MyReceiver();
        BroadCastManager.getInstance().registerReceiver(getActivity(), receiver, new IntentFilter(ConstantString.BROADCAST_INSID_TAG));
        return view;
    }

    /**
     * 请求公文正文数据
     */
    private void getData() {
        if (insid != null) {
            HttpParams params = new HttpParams();
            params.put("appkey", ConstantString.APP_KEY);
            params.put("access_token", SharedPreferenceUtil.getUserInfo(getContext()).getAccess_token());
            params.put("insid", insid);
            HttpUtil.httpGet(ConstantUrl.RECEIVE_SEND_ISSUE_FILE_CONTEXT, params, new JsonCallback<BaseModel<FileContentModel>>() {
                @Override
                public void onError(okhttp3.Call call, okhttp3.Response response, Exception e) {
                    super.onError(call, response, e);
                    tv_no_data_hint.setVisibility(View.VISIBLE);
                    mSuperFileView.setVisibility(View.GONE);
                }

                @Override
                public void onSuccess(BaseModel<FileContentModel> fileContentModel, okhttp3.Call call, okhttp3.Response response) {
                    String url = fileContentModel.results.getDoccontext_url();
                    if (ConstantString.FILE_NO_CONTENT_CODE.equals(fileContentModel.error_code)) {
                        //暂时没有文件
                        tv_no_data_hint.setVisibility(View.VISIBLE);
                        mSuperFileView.setVisibility(View.GONE);
                    } else {
                        //有可以预览的文件
                        tv_no_data_hint.setVisibility(View.GONE);
                        mSuperFileView.setVisibility(View.VISIBLE);
                        init(view, url);
                    }
                }
            });

        }
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            insid = intent.getStringExtra("insid");
            getData();
        }
    }

    /**
     * 预览调用这个
     *
     * @param view
     */
    public void init(View view, String fileUrl) {
        mSuperFileView.setOnGetFilePathListener(new SuperFileView2.OnGetFilePathListener() {
            @Override
            public void onGetFilePath(SuperFileView2 mSuperFileView2) {
                getFilePathAndShowFile(mSuperFileView2);
            }
        });

        // TODO: 2018/3/27 网络获取的下载地址
        String path = fileUrl;

        if (!TextUtils.isEmpty(path)) {
            Log.d("123", "文件path:" + path);
            setFilePath(path);
        }
        mSuperFileView.show();

    }


    private void getFilePathAndShowFile(SuperFileView2 mSuperFileView2) {


        if (getFilePath().contains("http") || getFilePath().contains("https")) {//网络地址要先下载

            downLoadFromNet(getFilePath(), mSuperFileView2);

        } else {
            mSuperFileView2.displayFile(new File(getFilePath()));
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("123", "FileDisplayActivity-->onDestroy");
        if (mSuperFileView != null) {
            mSuperFileView.onStopDisplay();
        }
    }


    public void setFilePath(String fileUrl) {
        this.filePath = fileUrl;
    }

    private String getFilePath() {
        return filePath;
    }

    private void downLoadFromNet(final String url, final SuperFileView2 mSuperFileView2) {

        //1.网络下载、存储路径、
        File cacheFile = getCacheFile(url);
        if (cacheFile.exists()) {
            if (cacheFile.length() <= 0) {
                Log.d("123", "删除空文件！！");
                cacheFile.delete();
                return;
            }
        }


        LoadFileModel.loadPdfFile(url, new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("123", "下载文件-->onResponse");
                boolean flag;
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    ResponseBody responseBody = response.body();
                    is = responseBody.byteStream();
                    long total = responseBody.contentLength();

                    File file1 = getCacheDir(url);
                    if (!file1.exists()) {
                        file1.mkdirs();
                        Log.d("123", "创建缓存目录： " + file1.toString());
                    }


                    //fileN : /storage/emulated/0/pdf/kauibao20170821040512.pdf
                    File fileN = getCacheFile(url);//new File(getCacheDir(url), getFileName(url))

                    Log.d("123", "创建缓存文件： " + fileN.toString());
                    if (!fileN.exists()) {
                        boolean mkdir = fileN.createNewFile();
                    }
                    fos = new FileOutputStream(fileN);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        Log.d("123", "写入缓存文件" + fileN.getName() + "进度: " + progress);
                    }
                    fos.flush();
                    Log.d("123", "文件下载成功,准备展示文件。");
                    //2.ACache记录文件的有效期
                    mSuperFileView2.displayFile(fileN);
                } catch (Exception e) {
                    Log.d("123", "文件下载异常 = " + e.toString());
//                    ToastUtil.showToast("文件预览失败");
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("123", "文件下载失败");
                File file = getCacheFile(url);
                if (!file.exists()) {
                    Log.d("123", "删除下载失败文件");
                    file.delete();
                }
            }
        });


    }

    /***
     * 获取缓存目录
     *
     * @param url
     * @return
     */
    private File getCacheDir(String url) {

        return new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/007/");

    }

    /***
     * 绝对路径获取缓存文件
     *
     * @param url
     * @return
     */
    private File getCacheFile(String url) {
        File cacheFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/007/"
                + getFileName(url));
        Log.d("123", "缓存文件 = " + cacheFile.toString());
        return cacheFile;
    }

    /***
     * 根据链接获取文件名（带类型的），具有唯一性
     *
     * @param url
     * @return
     */
    private String getFileName(String url) {
        String fileName = Md5Tool.hashKey(url) + "." + getFileType(url);
        return fileName;
    }

    /***
     * 获取文件类型
     *
     * @param paramString
     * @return
     */
    private String getFileType(String paramString) {
        String str = "";

        if (TextUtils.isEmpty(paramString)) {
            Log.d("123", "paramString---->null");
            return str;
        }
        Log.d("123", "paramString:" + paramString);
        int i = paramString.lastIndexOf('.');
        if (i <= -1) {
            Log.d("123", "i <= -1");
            return str;
        }


        str = paramString.substring(i + 1);
        Log.d("123", "paramString.substring(i + 1)------>" + str);
        return str;
    }

    @Override
    public void onDestroyView() {
        BroadCastManager.getInstance().unregisterReceiver(getActivity(), receiver);
        super.onDestroyView();
    }
}
