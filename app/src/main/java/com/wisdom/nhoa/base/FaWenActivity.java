package com.wisdom.nhoa.base;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.wisdom.nhoa.ConstantString;
import com.wisdom.nhoa.ConstantUrl;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.widget.paint.WriteDilogListener;
import com.wisdom.nhoa.widget.paint.WritePadDialog;
import com.wisdom.nhoa.widget.qrcode.CaptureActivity;
import com.wisdom.nhoa.util.U;
import com.wisdom.nhoa.util.UtilCustom;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import okhttp3.Call;
import okhttp3.Response;

import static android.webkit.WebSettings.LOAD_DEFAULT;

public class FaWenActivity extends AppCompatActivity {
    private WebView webview;
    private WritePadDialog mWritePadDialog;
    private Bitmap mSignBitmap;
    private String insid;
    private String flag[] = null;

    private ImageView img_saoma;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fa_wen);
        webview = (WebView) findViewById(R.id.webview);
        img_saoma = (ImageView) findViewById(R.id.img_saoma);
        img_saoma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(FaWenActivity.this, CaptureActivity.class), 1);
            }
        });
        initView(ConstantUrl.BASE_URL);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void initView(String url) {
//        bt_blue_back = (Button)findViewById(R.id.bt_blue_back);
        webview = (WebView) findViewById(R.id.webview);
//        tv_title= (TextView) findViewById(R.id.comm_head_title);
//        bt_blue_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

        U.showLoadingDialog(this);
        WebSettings webSettings = webview.getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setCacheMode(LOAD_DEFAULT);
        webSettings.setJavaScriptEnabled(true);//允许网页使用js
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webview.loadUrl(url);
        //webview.loadUrl("http://www.hrbga.gov.cn/hbgwx/dbb_queryLargeActivitiesSecurity");
//        webview.setWebChromeClient(new WebChromeClient(){
//            @Override
//            public void onReceivedTitle(WebView view, String title) {
//                super.onReceivedTitle(view, title);
//                tv_title.setText(title);
//            }
//        });
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                Log.e("123post","111");
                return super.shouldInterceptRequest(view, request);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //设定加载结束的操作
                U.closeLoadingDialog();

            }

        });

        webview.setWebChromeClient(new WebChromeClient() {//允许有alert弹出框
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                //webTitle = title;
            }

            /**
             * 处理alert弹出框
             */
            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
//                Log.i("log", "onJsAlert:" + message + webTitle + result);
//                //对alert的简单封装
                final String urlmessage = message;
                flag = urlmessage.split("_");
                if (flag[0].equals("checkword")) {
                    Log.e("urlmessage", urlmessage);

                    mWritePadDialog = new WritePadDialog(FaWenActivity.this, new WriteDilogListener.WriteDialogListener() {
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
                }else{
                    result.confirm();
                }
                //checkword
                return true;
            }
        });
    }

    //获取系统时间改成字符串
    public static String getStrTime() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        String date = sDateFormat.format(new java.util.Date());
        return date;
    }

    //创建签名文件
    private void createSignFile(final JsResult result) {
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
                    Toast.makeText(FaWenActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(FaWenActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                        mWritePadDialog.setBtText("确定");
                    } else {
                        Toast.makeText(FaWenActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
//                        result.cancel();
                    }
//                    if (mWritePadDialog != null) {
//                        mWritePadDialog.dismiss();
//                    }
                }
            });
        }
    }
}
