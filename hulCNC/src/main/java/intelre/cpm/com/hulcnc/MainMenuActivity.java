package intelre.cpm.com.hulcnc;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import cpm.com.hulcnc.R;
import intelre.cpm.com.hulcnc.Database.HUL_CNC_DB;
import intelre.cpm.com.hulcnc.constant.AlertandMessages;
import intelre.cpm.com.hulcnc.constant.CommonString;
import intelre.cpm.com.hulcnc.dailyentry.JourneyPlanSearchActivity;
import intelre.cpm.com.hulcnc.dailyentry.PerformanceActivity;
import intelre.cpm.com.hulcnc.dailyentry.StoreListActivity;
import intelre.cpm.com.hulcnc.download.DownloadActivity;
import intelre.cpm.com.hulcnc.upload.PreviousDataUploadActivity;
import intelre.cpm.com.hulcnc.upload.UploadDataActivity;
import intelre.cpm.com.hulcnc.GeoTag.GeoTagStoreList;
import intelre.cpm.com.hulcnc.chunkfile.ChunkFileClass;
import intelre.cpm.com.hulcnc.dailyentry.ServiceActivity;
import intelre.cpm.com.hulcnc.delegates.CoverageBean;
import intelre.cpm.com.hulcnc.gsonGetterSetter.JourneyPlan;

//import retrofit.Response;


public class MainMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private WebView webView;
    private ImageView imageView;
    private View headerView;
    private String error_msg;
    private Toolbar toolbar;
    private Context context;
    private int downloadIndex;
    private SharedPreferences preferences;
    HUL_CNC_DB db;
    String visit_date, user_type, user_name;
    private ArrayList<JourneyPlan> storelist = new ArrayList<>();
    private ArrayList<CoverageBean> coverageList;
    ChunkFileClass  chunkFileClass;
    public static final int REQUEST_SELECT_FILE = 100;
    private final static int FILECHOOSER_RESULTCODE = 1;
    WebSettings mWebSettings;
    private ValueCallback uploadMessage;
    private ValueCallback<Uri> mUploadMessage;
    String noticeboard="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        declaration();
       // chunkFileClass=new ChunkFileClass();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        headerView = LayoutInflater.from(this).inflate(R.layout.nav_header_main, navigationView, false);
        TextView tv_username = (TextView) headerView.findViewById(R.id.nav_user_name);
        TextView tv_usertype = (TextView) headerView.findViewById(R.id.nav_user_type);
        tv_username.setText(user_name);
        tv_usertype.setText(user_type);
        navigationView.addHeaderView(headerView);
        navigationView.setNavigationItemSelectedListener(this);
        db = new HUL_CNC_DB(this);
        if (!noticeboard.equals("")) {
            webView.loadUrl(noticeboard);
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_route_plan) {
            startActivity(new Intent(this, StoreListActivity.class));
            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);

        } else if (id == R.id.nav_download) {
            if (checkNetIsAvailable()) {
                if (!db.isCoverageDataFilled(visit_date)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainMenuActivity.this);
                    builder.setTitle("Parinaam");
                    builder.setMessage(getResources().getString(R.string.want_download_data)).setCancelable(false)
                            .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    try {
                                        db.open();
                                        db.deletePreviousUploadedData(visit_date);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    Intent in = new Intent(getApplicationContext(), DownloadActivity.class);
                                    startActivity(in);
                                    overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);

                                }
                            })
                            .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });

                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainMenuActivity.this);
                    builder.setTitle("Parinaam");
                    builder.setMessage(getResources().getString(R.string.previous_data_upload)).setCancelable(false)
                            .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent in = new Intent(getApplicationContext(), PreviousDataUploadActivity.class);
                                    startActivity(in);
                                    overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);

                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }

            } else {
                Snackbar.make(webView, getResources().getString(R.string.nonetwork), Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }

        } else if (id == R.id.nav_upload) {
            db.open();
            if (checkNetIsAvailable()) {
                storelist = db.getStoreData(visit_date);
                if (storelist.size() > 0 && downloadIndex == 0) {
                    if (coverageList.size() == 0) {
                        Snackbar.make(webView, R.string.no_data_for_upload, Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                    } else {
                        if (isStoreCheckedIn()) {
                            if (isValid()) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainMenuActivity.this);
                                builder.setTitle("Parinaam");
                                builder.setMessage(getResources().getString(R.string.want_upload_data)).setCancelable(false)
                                        .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Intent i = new Intent(getBaseContext(), UploadDataActivity.class);
                                                startActivity(i);
                                                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                                            }
                                        })
                                        .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        });

                                AlertDialog alert = builder.create();
                                alert.show();
                            } else {
                                AlertandMessages.showSnackbarMsg(context, "No data for Upload");
                            }
                        } else {
                            Snackbar.make(webView, error_msg, Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                        }
                    }
                } else {
                    Snackbar.make(webView, R.string.title_store_list_download_data, Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                }
            } else {
                Snackbar.make(webView, getResources().getString(R.string.nonetwork), Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }

        } else if (id == R.id.nav_geotag) {
            if (db.getSkuMasterData().size() > 0 && downloadIndex == 0) {
                Intent startDownload = new Intent(this, GeoTagStoreList.class);
                startActivity(startDownload);
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
            }else {
                Snackbar.make(webView, "Download data first", Snackbar.LENGTH_SHORT).setAction("Action", null).show();

            }


        } else if (id == R.id.nav_exit) {
            ActivityCompat.finishAffinity(this);
            Intent intent = new Intent(getApplicationContext(), LoginActivty.class);
            startActivity(intent);
            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
            finish();
        } else if (id == R.id.nav_services) {
            Intent search = new Intent(this, ServiceActivity.class);
            startActivity(search);
            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);

        } else if (id == R.id.nav_store_search) {
         //   Intent search = new Intent(this, JourneyPlanSearchActivity.class);
            Intent search = new Intent(this, PerformanceActivity.class);
            startActivity(search);
            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean isStoreCheckedIn() {
        boolean result_flag = true;
        for (int i = 0; i < coverageList.size(); i++) {
            String status = db.getSpecificStoreDatawithdate(visit_date, coverageList.get(i).getStoreId()).get(0).getUploadStatus();
            if (status != null && (status.equals(CommonString.KEY_CHECK_IN) || status.equals(CommonString.KEY_VALID))) {
                result_flag = false;
                error_msg = getResources().getString(R.string.title_store_list_checkout_current);
                break;
            }
        }

        return result_flag;
    }

    private boolean isValid() {
        boolean flag = false;
        String storestatus = "";
        for (int i = 0; i < coverageList.size(); i++) {
            storestatus = db.getSpecificStoreDatawithdate(visit_date, coverageList.get(i).getStoreId()).get(0).getUploadStatus();
            if (!storestatus.equalsIgnoreCase(CommonString.KEY_U)) {
                if ((storestatus.equalsIgnoreCase(CommonString.KEY_C) || storestatus.equalsIgnoreCase(CommonString.KEY_P) ||
                        storestatus.equalsIgnoreCase(CommonString.STORE_STATUS_LEAVE) ||
                        storestatus.equalsIgnoreCase(CommonString.KEY_D))) {

                    flag = true;
                    break;

                }
            }
        }

        if (!flag)
            error_msg = getResources().getString(R.string.no_data_for_upload);

        return flag;
    }


    void declaration() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        imageView = (ImageView) findViewById(R.id.img_main);
        webView = (WebView) findViewById(R.id.webview);
        visit_date = preferences.getString(CommonString.KEY_DATE, "");
        user_type = preferences.getString(CommonString.KEY_USER_TYPE, null);
        user_name = preferences.getString(CommonString.KEY_USERNAME, null);
        toolbar.setTitle(" Main Menu - " + visit_date);

        //load notice board url
          noticeboard = preferences.getString(CommonString.KEY_NOTICE_BOARD_LINK, "");
        webView.setWebViewClient(new MyWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        if (noticeboard != null && !noticeboard.equals("")) {
            webView.loadUrl(noticeboard);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode == REQUEST_SELECT_FILE) {
                if (uploadMessage == null)
                    return;
                uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
                uploadMessage = null;
            }
        } else if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            // Use MainActivity.RESULT_OK if you're implementing WebView inside Fragment
            // Use RESULT_OK only if you're implementing WebView inside an Activity
            Uri result = intent == null || resultCode != MainMenuActivity.RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        } else
            Toast.makeText(getApplicationContext(), "Failed to Upload Image", Toast.LENGTH_LONG).show();
    }
    private boolean checkNetIsAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {

        }
        return super.onOptionsItemSelected(item);
    }

    public String getCurrentTime() {
        Calendar m_cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss:mmm");
        String cdate = formatter.format(m_cal.getTime());
        return cdate;
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            imageView.setVisibility(View.INVISIBLE);
            webView.setVisibility(View.VISIBLE);
            super.onPageFinished(view, url);
            view.clearCache(true);
        }
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        db.open();
        downloadIndex = preferences.getInt(CommonString.KEY_DOWNLOAD_INDEX, 0);
        coverageList = db.getCoverageData(visit_date);
        webView.setWebViewClient(new MyWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(false);
        webView.clearCache(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.setLongClickable(true);
        mWebSettings = webView.getSettings();

        webView.setWebChromeClient(new WebChromeClient() {
            // For 3.0+ Devices (Start)
            // onActivityResult attached before constructor
            protected void openFileChooser(ValueCallback uploadMsg, String acceptType) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);
            }


            // For Lollipop 5.0+ Devices
            public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                if (uploadMessage != null) {
                    uploadMessage.onReceiveValue(null);
                    uploadMessage = null;
                }

                uploadMessage = filePathCallback;
                Intent intent = fileChooserParams.createIntent();
                try {
                    startActivityForResult(intent, REQUEST_SELECT_FILE);
                } catch (ActivityNotFoundException e) {
                    uploadMessage = null;
                    Toast.makeText(getApplicationContext(), "Cannot Open File Chooser", Toast.LENGTH_LONG).show();
                    return false;
                }

                return true;
            }

            //For Android 4.1 only
            protected void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                mUploadMessage = uploadMsg;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "File Browser"), FILECHOOSER_RESULTCODE);
            }

            protected void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
            }
        });


        webView.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!noticeboard.equals("")) {
                    webView.loadUrl(noticeboard);
                }
            }
        }, 300000);
    }
    //comm

}
