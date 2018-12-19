package com.wisdom.nhoa.widget.qrcode;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.wisdom.nhoa.R;

/**
 * author：
 * date:
 */

public class MeetingOverDialog extends DialogFragment {

    public interface OverMettingListener {
        void onOverMetting();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setTitle(R.string.index_meeting_over_meeting);
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        OverMettingListener listener = (OverMettingListener) getActivity();
                        listener.onOverMetting();
                    }
                }).setNegativeButton("取消", null);
        return builder.create();
    }
}
