package com.khach.feed.util;

import android.content.Context;
import android.support.v7.app.AlertDialog;

public final class ViewUtil {
    public static void showMessageDialog(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
