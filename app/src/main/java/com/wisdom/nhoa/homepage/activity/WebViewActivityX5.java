package com.wisdom.nhoa.homepage.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.request.BaseRequest;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.util.SharedPreferenceUtil;
import com.wisdom.nhoa.widget.fileview.FileDisplayActivity;
import com.wisdom.nhoa.widget.fileview.PreviwPicActivity;
import com.wisdom.nhoa.widget.paint.WriteDilogListener;
import com.wisdom.nhoa.widget.paint.WritePadDialog;
import com.wisdom.nhoa.widget.qrcode.CaptureActivity;
import com.wisdom.nhoa.util.ToastUtil;
import com.wisdom.nhoa.util.U;
import com.wisdom.nhoa.util.UtilCustom;
import com.wisdom.nhoa.util.http_util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import okhttp3.Call;
import okhttp3.Response;

import static android.webkit.WebSettings.LOAD_DEFAULT;


/**
 * @author HanXueFeng
 * @ProjectName project： Zwteaa
 * @class package：com.wisdom.zwteaa.homepage.activity
 * @class describe：
 * @time 2018/2/5 11:44
 * @change
 */

public class WebViewActivityX5 extends Activity {
    private ProgressDialog progressDialog;
    private WebView tencent_webview;
    public static final String TAG = WebViewActivityX5.class.getSimpleName();
    public static final int FILECHOOSER_RESULTCODE = 0x114;
    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private WritePadDialog mWritePadDialog;
    private Bitmap mSignBitmap;
    private String insid;
    private String flag[] = null;
    private String urlname[] = null;
    private ImageView img_saoma;

    private String downrul = "";
    private TextView tv_meetingtopic;
    private TextView tv_compere;
    private TextView tv_summaryname;
    private TextView tv_applydepart;
    private TextView tv_nameboxfs;
    private TextView tv_resid;
    private TextView tv_starttime;
    private TextView tv_endtime;
    private TextView tv_meetingremark;
    private Button bt_qiandao;
    private JSONObject object;
    private AlertDialog dialog;
    private String applyId;//会议id
    private String sign_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_x5);
        img_saoma = (ImageView) findViewById(R.id.img_saoma);
        img_saoma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(WebViewActivityX5.this, CaptureActivity.class), 1);
            }
        });
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        init(ConstantUrl.BASE_URL);

    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init(String url) {
        U.showLoadingDialog(this);
        // TODO Auto-generated method stub
        tencent_webview = (WebView) findViewById(R.id.tencent_webview);
//TODO***********************************************************
        //        tencent_webview.loadUrl(ConstantUrl.LOGIN_URL);
        tencent_webview.setDownloadListener(new MyWebViewDownLoadListener());
        WebSettings webSettings = tencent_webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        //webSettings.setLoadWithOverviewMode(true);
        // webSettings.setUseWideViewPort(true);将图片调整到适合webview的大小
        // webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        webSettings.setAppCacheEnabled(true);

        webSettings.setCacheMode(LOAD_DEFAULT);
        webSettings.setJavaScriptEnabled(true);//允许网页使用js
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        tencent_webview.loadUrl(url);
        tencent_webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView webView, String s) {
                //设定加载结束的操作
                U.closeLoadingDialog();
                super.onPageFinished(webView, s);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i("拦截url---", "" + url);
                if (url.startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
                view.loadUrl(url);
                return true;
            }
        });
        //TODO  针对上传文件，进行的操作
        tencent_webview.setWebChromeClient(new WebChromeClient() {
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                Log.d(TAG, "openFileChoose(ValueCallback<Uri> uploadMsg)");
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                WebViewActivityX5.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
            }

            public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
                Log.d(TAG, "openFileChoose( ValueCallback uploadMsg, String acceptType )");
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                WebViewActivityX5.this.startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                Log.d(TAG, "openFileChoose(ValueCallback<Uri> uploadMsg, String acceptType, String capture)");
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                WebViewActivityX5.this.startActivityForResult(Intent.createChooser(i, "File Browser"), WebViewActivityX5.FILECHOOSER_RESULTCODE);
            }

            // For Android 5.0+
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                mUploadCallbackAboveL = filePathCallback;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                WebViewActivityX5.this.startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);
                return true;
            }


            /**
             * 处理alert弹出框
             */
            @Override
            public boolean onJsAlert(WebView webView, String s, String message, final com.tencent.smtt.export.external.interfaces.JsResult result) {
//                Log.i("log", "onJsAlert:" + message + webTitle + result);
//                //对alert的简单封装
                final String urlmessage = message;
                Log.i(TAG, "onJsAlert:urlmessage ---" + urlmessage);
                flag = urlmessage.split("_");
                if (flag[0].equals("checkword")) {
                    Log.e("urlmessage", urlmessage);

                    mWritePadDialog = new WritePadDialog(WebViewActivityX5.this, new WriteDilogListener.WriteDialogListener() {
                        @Override
                        public void onPaintDone(Object object) {
                            mSignBitmap = (Bitmap) object;
                            if (mWritePadDialog.getBtText().equals("上传")) {
                                createSignFile(result);
                            } else if (mWritePadDialog.getBtText().equals("确定")) {
                                mWritePadDialog.dismiss();
                                result.confirm();
                            }
                            Log.e("esult.confirm();+---", "确定");
                        }
                    });
                    mWritePadDialog.setListener(new WritePadDialog.CancelOnClickListener() {
                        @Override
                        public void onCancle() {
                            mWritePadDialog.dismiss();
                            result.cancel();
                            Log.e("esult.confirm();+---", "取消");
                        }
                    });
                    // 屏蔽keycode等于84之类的按键，避免按键后导致对话框消息而页面无法再弹出对话框的问题
                    mWritePadDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                        @Override
                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                            Log.v("onJsConfirm", "keyCode==" + keyCode + "event=" + event);
                            return true;
                        }
                    });
                    //mWritePadDialog.setCanceledOnTouchOutside(false);
                    mWritePadDialog.setCancelable(false);
                    mWritePadDialog.showDialog();
                } else if (flag[0].equals("login")) {
                    ConstantString.USERID = flag[1];
                    //初始化百度推送
                    PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, ConstantString.BAIDU_API_KEY);
                    result.confirm();
                } else if (flag[0].equals("groupfile")) {
                    downrul = flag[1];
                    Log.i(TAG, "downrul:--- " + downrul);
                    urlname = urlmessage.split("/");
                    String fileName = urlname[urlname.length - 1];
                    // String fileNameArray[] = fileName.split("\\.");
//                    if (fileNameArray.length > 1) {
//                        String fileLastName = fileNameArray[fileNameArray.length - 1];
                    if (fileName.contains("jpg") ||
                            fileName.contains("png") ||
                            fileName.contains("jpeg") ||
                            fileName.contains("bmp") ||
                            fileName.contains("gif")
                            ) {//等待预览的文件是图片资源
                        Intent intent = new Intent(WebViewActivityX5.this, PreviwPicActivity.class);
                        intent.putExtra("title", fileName);
                        intent.putExtra("url", downrul);
                        startActivity(intent);
                    } else {
                        // TODO: 2018/1/12 其它类型文件
                        FileDisplayActivity.show(WebViewActivityX5.this, downrul);
                    }
                    result.confirm();
                } else if (flag[0].equals("apply")) {
                    downrul = flag[1];
//                    Log.i(TAG, "downrul:--- " + downrul);
                    progressDialog = new ProgressDialog(WebViewActivityX5.this);
                    progressDialog.setTitle("正在下载……");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.setCanceledOnTouchOutside(false);
                    OkGo.get(downrul)
                            .cacheMode(CacheMode.DEFAULT)
                            .execute(new FileCallback() {
                                @Override
                                public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                                    progressDialog.setMax(100);
                                    progressDialog.setProgress((int) (progress * 100));
                                    super.downloadProgress(currentSize, totalSize, progress, networkSpeed);
                                }

                                @Override
                                public void onBefore(BaseRequest request) {
                                    progressDialog.show();
                                    super.onBefore(request);
                                }

                                @Override
                                public void onSuccess(final File file, Call call, Response response) {
                                    progressDialog.dismiss();
                                    ToastUtil.showToast("下载成功,二维码存储在Download文件夹");
                                }
                            });
                    result.confirm();
                }
                return true;
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent data) {
        if (requestCode != FILECHOOSER_RESULTCODE || mUploadCallbackAboveL == null) {
            return;
        }
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
            } else {
                String dataString = data.getDataString();
                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
                if (dataString != null) results = new Uri[]{Uri.parse(dataString)};
            }
        }
        // TODO: 2018/2/7
        mUploadCallbackAboveL.onReceiveValue(results);
        mUploadCallbackAboveL = null;
        return;
    }


    //onActivityResult回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage && null == mUploadCallbackAboveL) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (mUploadCallbackAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            } else if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            }
        } else if (requestCode == 1) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                String url = bundle.getString("result");
                Log.i(TAG, "onActivityResult: ---" + url);
                if (!url.equals("")) {
                    if (!ConstantString.USERID.equals("")) {
                        getSign(url);
                    } else {
                        ToastUtil.showToast("请先登录");
                    }
                }

            }
        }


    }

    private void getSign(String url) {
        HttpParams params = new HttpParams();
        params.put("userId", ConstantString.USERID);
        params.put("appKey","18oa9600ae7d41a489547ad201871fda");
        HttpUtil.httpGet(url, params, new StringCallback() {
            @Override
            public void onSuccess(String s, Call call, Response response) {
                Log.i("签到信息----", s);
                try {
                    JSONObject ob = new JSONObject(s);
                    if(ob.getInt("error_code")==0){
                        object = ob.getJSONObject("results");
                        huiyidialg();
                    }else{
                        ToastUtil.showToast(ob.getString("error_msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && tencent_webview.canGoBack()) {
            tencent_webview.goBack();// 返回前一个页面
            return true;
        } else {
            new AlertDialog.Builder(this).setTitle("提示").setMessage("是否退出程序").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
            return false;
        }

    }

    private class MyWebViewDownLoadListener implements DownloadListener {
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            Log.i(TAG, "附件下载地址: " + url);
            OkGo.get(url)
                    .cacheMode(CacheMode.DEFAULT)
                    .execute(new FileCallback("erweima") {
                        @Override
                        public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                            progressDialog.setMax(100);
                            progressDialog.setProgress((int) (progress * 100));
                            super.downloadProgress(currentSize, totalSize, progress, networkSpeed);
                        }

                        @Override
                        public void onBefore(BaseRequest request) {
                            progressDialog.show();
                            super.onBefore(request);
                        }

                        @Override
                        public void onSuccess(final File file, Call call, Response response) {
                            progressDialog.dismiss();
                            ToastUtil.showToast("下载成功");
                        }
                    });
        }
//            Uri uri = Uri.parse(url);
//            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//            startActivity(intent);

    }


    //获取系统时间改成字符串
    public static String getStrTime() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        String date = sDateFormat.format(new java.util.Date());
        return date;
    }

    //创建签名文件
    private void createSignFile(final com.tencent.smtt.export.external.interfaces.JsResult result) {
        ByteArrayOutputStream baos = null;
        FileOutputStream fos = null;
        String path = null;
        File file = null;
        try {
            // TODO: 2017/11/3 图片地址写死了***************************
//            if (ConstantString.filelist.get(0) != null) {
//                path = Environment.getExternalStorageDirectory() + File.separator + ConstantString.filelist.get(0).getSign_name() + ".jpg";
//            } else {
            Log.e("getStrTime", getStrTime());
            path = Environment.getExternalStorageDirectory() + File.separator + getStrTime() + ".png";
            //}
            Log.e("图片路径", path);
            file = new File(path);
            fos = new FileOutputStream(file);
            baos = new ByteArrayOutputStream();

            //如果设置成
//            mSignBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            // 图片的背景都是黑色的
            mSignBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            if (b != null) {
                fos.write(b);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (file != null) {
            HttpParams params = new HttpParams();
            params.put("file", file);
            params.put("insid", flag[2]);
            params.put("flag", flag[1]);
            //Log.i(TAG, "createSignFile: 开始上传…………");
//            上传文件到服务器的逻辑
            UtilCustom.uploadFiles(ConstantString.UPLOAD_SIGN_FILE, params, new StringCallback() {
                @Override
                public void onError(Call call, Response response, Exception e) {
                    super.onError(call, response, e);
                    Toast.makeText(WebViewActivityX5.this, "上传失败", Toast.LENGTH_SHORT).show();
                    //Log.e(TAG, "onError: " + e);
//                    if (mWritePadDialog != null) {
//                        mWritePadDialog.dismiss();
//                    }
                }

                @Override
                public void onSuccess(String s, Call call, Response response) {
                    //Log.e(TAG, "onSuccess: " + s);
                    Log.e("上传签名图片-----", s);
                    if (s.equals("success")) {
                        Toast.makeText(WebViewActivityX5.this, "上传成功", Toast.LENGTH_SHORT).show();
                        mWritePadDialog.setBtText("确定");
                    } else {
                        Toast.makeText(WebViewActivityX5.this, "上传失败", Toast.LENGTH_SHORT).show();
//                        result.cancel();
                    }
//                    if (mWritePadDialog != null) {
//                        mWritePadDialog.dismiss();
//                    }
                }
            });
        }
    }


    private void huiyidialg() throws JSONException {
        AlertDialog.Builder builder = new AlertDialog.Builder(WebViewActivityX5.this);
        dialog = builder.create();
        View view = View.inflate(WebViewActivityX5.this, R.layout.custom_dialog, null);
        tv_meetingtopic = (TextView) view.findViewById(R.id.tv_meetingtopic);
        tv_compere = (TextView) view.findViewById(R.id.tv_compere);
        tv_summaryname = (TextView) view.findViewById(R.id.tv_summaryname);
        tv_applydepart = (TextView) view.findViewById(R.id.tv_applydepart);
        tv_nameboxfs = (TextView) view.findViewById(R.id.tv_nameboxfs);
        tv_resid = (TextView) view.findViewById(R.id.tv_resid);
        tv_starttime = (TextView) view.findViewById(R.id.tv_starttime);
        tv_endtime = (TextView) view.findViewById(R.id.tv_endtime);
        tv_meetingremark = (TextView) view.findViewById(R.id.tv_meetingremark);
        bt_qiandao = (Button) view.findViewById(R.id.bt_qiandao);
        try {
            applyId = object.getString("applyId");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            tv_meetingtopic.setText(object.getString("meetingtopic"));
            tv_compere.setText(object.getString("compere"));
            tv_summaryname.setText(object.getString("summaryname"));
            tv_applydepart.setText(object.getString("applydepart"));
            tv_nameboxfs.setText(object.getString("nameboxfs"));
            tv_resid.setText(object.getString("resid"));
            tv_starttime.setText(object.getString("starttime"));
            tv_endtime.setText(object.getString("endtime"));
            tv_meetingremark.setText(object.getString("meetingremark"));


        } catch (JSONException e) {
            e.printStackTrace();
        }
        sign_state = object.getString("sign_state");
        if (sign_state.equals("0")) {
            bt_qiandao.setText("签到");
        } else {
            bt_qiandao.setText("已签到");
        }
        bt_qiandao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sign_state.equals("0")) {
                    HttpParams params = new HttpParams();
                    params.put("applyId", applyId);
                    params.put("userId", ConstantString.USERID);
                    HttpUtil.httpGet(ConstantUrl.SIGNIN, params, new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            if (s.equals("success")) {
                                ToastUtil.showToast("签到成功");
                            } else {
                                ToastUtil.showToast("签到失败");
                            }
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);
                            ToastUtil.showToast("签到失败");
                        }

                    });
                }

                dialog.dismiss();
            }
        });
//        TextView title = (TextView) view
//                .findViewById(R.id.title);//设置标题
        //取消或确定按钮监听事件处理
        // 将布局设置给Dialog
        dialog.show();// 显示对话框
        dialog.setContentView(view);
    }
}
