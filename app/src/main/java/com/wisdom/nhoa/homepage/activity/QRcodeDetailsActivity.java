package com.wisdom.nhoa.homepage.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wisdom.nhoa.R;
import com.wisdom.nhoa.base.BaseActivity;
import com.wisdom.nhoa.util.FileUtils;
import com.wisdom.nhoa.util.PicUtil;
import com.wisdom.nhoa.util.ToastUtil;
import com.wisdom.nhoa.widget.SavePicDialog;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

public class QRcodeDetailsActivity extends BaseActivity implements View.OnLongClickListener {

    private static final int SELECT_PIC_BY_PICK_PHOTO =10201 ;
    @BindView(R.id.img_QRcode)
    ImageView imgQRcode;
    @BindView(R.id.tv_metting_topic)
    TextView tv_metting_topic;
    @BindView(R.id.rl_qr_code)
    RelativeLayout rl_qr_code;
    @BindView(R.id.rl_open)
    RelativeLayout rl_open;
    @BindView(R.id.tv_hint)
    TextView tv_hint;

    private File QRFile;//二维码文件
    private String url;//二维码保存在本地的地址

    private String mettingTitle = "";
    public static final String TAG = QRcodeDetailsActivity.class.getSimpleName();

    @Override
    public void initViews() {
        setTitle("二维码");
        String qrcodeUrl = getIntent().getStringExtra("qrcodeUrl");
        mettingTitle = getIntent().getStringExtra("mettingTitle");
        tv_metting_topic.setText("会议名:" + mettingTitle);
        Log.i(TAG, "qrcodeUrl2: " + qrcodeUrl);
        if (qrcodeUrl != null) {
            Glide.with(this).load(qrcodeUrl).into(imgQRcode);
        } else {
            ToastUtil.showToast("二维码图片加载失败，请重试");
        }
        rl_qr_code.setLongClickable(true);
        rl_qr_code.setOnLongClickListener(this);

    }

    @Override
    public void setlayoutIds() {
        setContentView(R.layout.activity_qrcode_details);
    }

    /**
     * 二维码长按保存方法
     *
     * @param v
     * @return
     */
    @Override
    public boolean onLongClick(View v) {
        SavePicDialog savePicDialog = new SavePicDialog(this,R.style.dialog);
        savePicDialog.setOnDialogDismissListener(new SavePicDialog.OnDialogDismissListener() {
            @Override
            public void onDialogDismiss() {
                PicUtil.saveMypic(PicUtil.makeView2Bitmap(rl_qr_code), new PicUtil.OnPicSaveSuccessListener() {
                    @Override
                    public void onPicSaveFail(Exception e) {
                        rl_open.setVisibility(View.GONE);
                    }

                    @Override
                    public void onPicSaveSuccess(File file, String filePath, String folderPath) {
                        QRFile = file;
                        QRcodeDetailsActivity.this.url = folderPath;
                        rl_open.setVisibility(View.VISIBLE);
                        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
                        tv_hint.setText("二维码保存成功，地址为:" + folderPath);
                    }
                },context);
            }
        });
//        showDialogFragment(savePicDialog);
        savePicDialog.show();
        return true;
    }

    /**
     * 弹出dialogFragment的方法
     *
     * @param dialogFragment
     */
    private void showDialogFragment(DialogFragment dialogFragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        dialogFragment.show(ft, "df1");
    }


    /**
     * 页面最下面点击打开相册预览
     */
    @OnClick(R.id.btn_open)
    public void onViewClicked() {
        if (QRFile != null) {
                if (Build.VERSION.SDK_INT >= 24) {//如果大于等于7.0使用FileProvider
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri uriForFile = FileProvider.getUriForFile(this, "com.wisdom.nhoa.myprovider", QRFile);
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uriForFile);
                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setDataAndType(uriForFile,"image/*");

                    startActivity(intent);
                } else {
                    FileUtils.openPhoto(this, QRFile);
                }
            } else {
                ToastUtil.showToast("文件不存在");
            }
        }
    }


