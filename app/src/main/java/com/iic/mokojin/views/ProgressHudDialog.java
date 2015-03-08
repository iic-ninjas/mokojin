package com.iic.mokojin.views;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by yon on 3/5/15.
 */
public class ProgressHudDialog extends ProgressDialog {

    public ProgressHudDialog(Context context, String message) {
        super(context, STYLE_SPINNER);
        if (message != null) {
            setMessage(message);
        }
    }
}