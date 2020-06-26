package intelre.cpm.com.hulcnc.constant;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import cpm.com.hulcnc.R;
import io.github.memfis19.annca.Annca;
import io.github.memfis19.annca.internal.configuration.AnncaConfiguration;

/**
 * Created by jeevan on 2/16/2017.
 */

public class CommonFunctions {
    public static String getCurrentTime() {
        Calendar m_cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss:mm");
        String cdate = formatter.format(m_cal.getTime());
        return cdate;

    }

    public static boolean CheckNetAvailability(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }


    public static void startAnncaCameraActivity(final Context context, final String path, Fragment fragment, final boolean showGrid) {
        final AnncaConfiguration.Builder dialogDemo;
        if (fragment == null) {
            dialogDemo = new AnncaConfiguration.Builder((Activity) context, CommonString.CAPTURE_MEDIA);
        } else {
            dialogDemo = new AnncaConfiguration.Builder(fragment, CommonString.CAPTURE_MEDIA);
        }
        dialogDemo.setMediaAction(AnncaConfiguration.MEDIA_ACTION_PHOTO);
        dialogDemo.setMediaResultBehaviour(AnncaConfiguration.PREVIEW);
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.view_horizontal_camera);
        dialog.setCancelable(false);
        if (dialog != null && (!dialog.isShowing())) {
            dialog.show();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }

                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }

                new Annca(dialogDemo.build()).launchCamera(path,showGrid);
            }
        }, 3000);
    }

    public static String setMetadataAtImages(String store_Nm, String store_Id, String type, String userId) {
        String ss = "";
        if (!store_Nm.equals("")) {
            ss = "Store Name : " + store_Nm.toLowerCase().trim() + " | Store Id : " + store_Id + " " + " | Retail Executive Id : " + userId + " | Image Type : " + type;
        } else {
            ss = "Store Id : " + store_Id + " " + " | Retail Executive Id : " + userId + " | Image Type : " + type;
        }

        return ss;
    }

    public static String setmetadataforattendance(String type, String userId) {
        String ss = "Retail Executive Id : " + userId + " | Image Type : " + type;
        return ss;
    }


    public static Bitmap addMetadataAndTimeStampToImage(Context context, final String path, String metadata, String visit_date) {
        Bitmap bmp1 = BitmapFactory.decodeFile(path);
        View view = LayoutInflater.from(context).inflate(R.layout.preview_image, null);
        view.layout(0, 0, bmp1.getWidth(), bmp1.getHeight());
        Bitmap bmp = getViewBitmap(view, bmp1, path, metadata, visit_date);
        try {
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(new File(path)));
            return bmp;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
           // Crashlytics.logException(e);
            return bmp1;
        }
    }

    public static Bitmap getViewBitmap(View view, Bitmap bmp, String path, String metadata, String visit_date) {
        try {
            //Get the dimensions of the view so we can re-layout the view at its current size
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
            String dateTime = sdf.format(Calendar.getInstance().getTime());
            String copm = dateTime;
            ImageView temp_img = (ImageView) view.findViewById(R.id.temp_img);
            //ImageView temp_map = (ImageView) view.findViewById(R.id.temp_map);
            TextView storeM = (TextView) view.findViewById(R.id.storeM);
            int copleteValue = 0;

            try {
                copm = copm.replaceAll("[- ]", " ");
                String[] items = copm.split(":");
                String seconds = items[2];
                int lastIndex;
                lastIndex = Integer.parseInt(seconds);
                int day = Integer.parseInt(visit_date.substring(3, 5));
                int a = Integer.parseInt("10") * Integer.parseInt("40");
                a = a + day;
                lastIndex = lastIndex * 2;
                copleteValue = a + lastIndex;
            } catch (NumberFormatException e) {
                e.printStackTrace();
              //  Crashlytics.logException(e);
            }

            //timestamp on image
            Bitmap dest = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas cs = new Canvas(dest);
            Paint tPaint = new Paint();
            tPaint.setTextSize(20);
            tPaint.setColor(Color.RED);
            tPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            cs.drawBitmap(bmp, 0f, 0f, null);
            float height_ = tPaint.measureText("yY");
            String completeDate = dateTime + "[" + "10" + "] " + String.valueOf(copleteValue);
            cs.drawText(completeDate, 20f, height_ + 15f, tPaint);
            try {
                dest.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(new File(path)));
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
               // Crashlytics.logException(e);
            }

            storeM.setText(metadata + " | Date : " + completeDate);

            bmp = BitmapFactory.decodeFile(path);
            temp_img.setImageBitmap(bmp);
            int width = bmp.getWidth();
            int height = bmp.getHeight();
            int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
            int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
            //Cause the view to re-layout
            view.measure(measuredWidth, measuredHeight);
            view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
            //Create a bitmap backed Canvas to draw the view into
            Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            view.draw(c);
            return b;
        } finally {
        }
    }

    public static boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }
    public static String getCurrentTimeHHMMSS() {
        Calendar m_cal = Calendar.getInstance();
        return m_cal.get(Calendar.HOUR_OF_DAY) + ""
                + m_cal.get(Calendar.MINUTE) + "" + m_cal.get(Calendar.SECOND);
    }
}
