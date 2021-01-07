package com.example.wastedfoodteam.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.example.wastedfoodteam.R;

public class LoadingDialog {
    final Activity activity;
    final AlertDialog dialog;

    public LoadingDialog(Activity myActivity) {
        activity = myActivity;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_dialog, null));
        builder.setCancelable(true);
        dialog = builder.create();
    }

    public void startLoadingDialog() {
        if (!dialog.isShowing())
            dialog.show();
    }

    public void dismissDialog() {
        dialog.dismiss();
    }
}
