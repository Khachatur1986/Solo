package com.khach.feed.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

public final class DialogUtil {

    public interface CallBack {
        void doAction(int selectedOptionIndex);
    }

    public interface CallBackAddContact {
        void doAction(String name, String phone, String phoneType);
    }

    public static void showMessageOKCancel(Activity activity, String message, final CallBack callBack) {
        new AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callBack.doAction(which);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        callBack.doAction(which);
                    }
                })
                .create()
                .show();
    }

    public static void showDialogWithItems(Context context, String title, CharSequence[] options, final CallBack callBack) {
        /**
         * alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
         alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
         */
        AlertDialog.Builder alertDialogBuilder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alertDialogBuilder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
//            alertDialogBuilder = new AlertDialog.Builder(context, R.style.MyDialogTheme);
        } else {
            alertDialogBuilder = new AlertDialog.Builder(context);
        }

        alertDialogBuilder.setTitle(title)
//                .setMessage("Are you sure you want to delete this entry?")
//                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        // continue with delete
//                    }
//                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert);

        alertDialogBuilder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callBack.doAction(which);
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public static void showYesNoCancelDialog(Context context, String title, String message, int resourceIconId, final CallBack callBack) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // set the title of the Alert Dialog
        if (!TextUtils.isEmpty(title)) {
            alertDialogBuilder.setTitle(title);
        }

        // set dialog message
        if (!TextUtils.isEmpty(message)) {
            alertDialogBuilder.setMessage(message);
        }
        alertDialogBuilder
//                .setCancelable(false)
                .setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // what to do if YES is tapped
                                callBack.doAction(id);
                                dialog.dismiss();
                            }
                        });

        alertDialogBuilder.setNeutralButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // code to do on CANCEL tapped
                        callBack.doAction(id);
                        dialog.cancel();
                    }
                });

        alertDialogBuilder.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // code to do on NO tapped
                        callBack.doAction(id);
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }
}
