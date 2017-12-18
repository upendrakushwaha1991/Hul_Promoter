package intelre.cpm.com.intelre.constant;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

import intelre.cpm.com.intelre.R;

/**
 * Created by jeevanp on 14-12-2017.
 */

public class AlertandMessages {
    public static void showAlert(final Activity activity, String str, final Boolean activityFinish) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Parinaam");
        builder.setMessage(str).setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (activityFinish) {
                            activity.finish();
                        } else {
                            dialog.dismiss();
                        }

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void showToastMsg(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    public static void showSnackbarMsg(Context context, String message) {
        Snackbar.make(((Activity) context).getCurrentFocus(), message, Snackbar.LENGTH_SHORT).show();
    }
}
