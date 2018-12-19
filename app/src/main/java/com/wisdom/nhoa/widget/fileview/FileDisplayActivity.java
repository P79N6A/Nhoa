package com.wisdom.nhoa.widget.fileview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;


import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.R;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 多功能文件预览类
 * 调用 静态方法show 或者 showForResult 可实现预览doc和图片等
 */
public class FileDisplayActivity extends Activity {


    private String TAG = "FileDisplayActivity";
    SuperFileView2 mSuperFileView;

    String filePath;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_display);
        init();
    }


    public void init() {
        mSuperFileView = (SuperFileView2) findViewById(R.id.mSuperFileView);
        mSuperFileView.setOnGetFilePathListener(new SuperFileView2.OnGetFilePathListener() {
            @Override
            public void onGetFilePath(SuperFileView2 mSuperFileView2) {
                getFilePathAndShowFile(mSuperFileView2);
            }
        });

        Intent intent = this.getIntent();
        String path = (String) intent.getSerializableExtra("path");

        if (!TextUtils.isEmpty(path)) {
            Log.d("123", "文件path:" + path);
            setFilePath(path);
        }
        mSuperFileView.show();

    }


    private void getFilePathAndShowFile(SuperFileView2 mSuperFileView2) {


        if (getFilePath().contains("http")||getFilePath().contains("https")) {//网络地址要先下载

            downLoadFromNet(getFilePath(),mSuperFileView2);

        } else {
            mSuperFileView2.displayFile(new File(getFilePath()));
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("123","FileDisplayActivity-->onDestroy");
        if (mSuperFileView != null) {
            mSuperFileView.onStopDisplay();
        }
    }


    public static void show(Context context, String url) {
        Intent intent = new Intent(context, FileDisplayActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("path", url);
        intent.putExtras(bundle);
        context.startActivity(intent);

    }
    public static void showForResult(Context context, String url) {
        Intent intent = new Intent(context, FileDisplayActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("path", url);
        intent.putExtras(bundle);
        ((Activity) context).startActivityForResult(intent, ConstantString.CODE_ACTIVITY_FINISH);

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
                Log.d("123","下载文件-->onResponse");
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
        Log.d("123","paramString:"+paramString);
        int i = paramString.lastIndexOf('.');
        if (i <= -1) {
            Log.d("123","i <= -1");
            return str;
        }


        str = paramString.substring(i + 1);
        Log.d("123","paramString.substring(i + 1)------>"+str);
        return str;
    }


}
