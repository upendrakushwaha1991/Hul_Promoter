package intelre.cpm.com.intelre;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.UnknownHostException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import intelre.cpm.com.intelre.Database.INTEL_RE_DB;
import intelre.cpm.com.intelre.constant.AlertandMessages;
import intelre.cpm.com.intelre.constant.CommonString;
import intelre.cpm.com.intelre.dailyentry.ServiceActivity;
import intelre.cpm.com.intelre.dailyentry.StoreListActivity;
import intelre.cpm.com.intelre.delegates.CoverageBean;
import intelre.cpm.com.intelre.download.DownloadActivity;
import intelre.cpm.com.intelre.gsonGetterSetter.JourneyPlan;
import intelre.cpm.com.intelre.retrofit.PostApiForFile;

import intelre.cpm.com.intelre.retrofit.StringConverterFactory;
import intelre.cpm.com.intelre.upload.PreviousDataUploadActivity;
import intelre.cpm.com.intelre.upload.UploadDataActivity;
import retrofit.Call;
import retrofit.Callback;
//import retrofit.Response;
import retrofit.Response;
import retrofit.Retrofit;

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
    INTEL_RE_DB db;
    String visit_date, user_type, user_name;
    private ArrayList<JourneyPlan> storelist = new ArrayList<>();
    private ArrayList<CoverageBean> coverageList;
    boolean isvalid = false;
    String result = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        declaration();

        /*String url = preferences.getString(CommonString.KEY_NOTICE_BOARD_LINK, "");
        String user_name = preferences.getString(CommonString.KEY_USERNAME, null);

        webView.setWebViewClient(new MyWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);

        if (!url.equals("")) {

            webView.loadUrl(url);

        }*/

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
        db=new INTEL_RE_DB(this);

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
                                    Intent in = new Intent(getApplicationContext(), DownloadActivity.class);
                                    startActivity(in);
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
                if (storelist.size()>0 && downloadIndex == 0) {
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
            }else {
                Snackbar.make(webView, getResources().getString(R.string.nonetwork), Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }

        } else if (id == R.id.nav_geotag) {
            if (db.getSkuMasterData().size() > 0 && downloadIndex == 0) {

            }


        } else if (id == R.id.nav_exit) {
            ActivityCompat.finishAffinity(this);
            Intent intent = new Intent(getApplicationContext(), IntelLoginActivty.class);
            startActivity(intent);
            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
            finish();
        } else if (id == R.id.nav_services) {
            showExportDialog();
            /*Intent startservice = new Intent(this, ServiceActivity.class);
            startActivity(startservice);
            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);*/
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
        String storestatus="";
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        db.open();
        downloadIndex = preferences.getInt(CommonString.KEY_DOWNLOAD_INDEX, 0);
        coverageList = db.getCoverageData(visit_date);


    }


    private boolean checkNetIsAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {

        }

        return super.onOptionsItemSelected(item);
    }


    private String uploadBackup(final Context context, String file_name, String folder_name) {
        RequestBody body1;
        // result = "";
        isvalid = false;
        final File originalFile = new File(CommonString.BACKUP_FILE_PATH + file_name);
        RequestBody photo = RequestBody.create(MediaType.parse("application/octet-stream"), originalFile);
        body1 = new MultipartBuilder().type(MultipartBuilder.FORM)
                .addFormDataPart("file", originalFile.getName(), photo)
                .addFormDataPart("Foldername", folder_name)
                .build();
        Retrofit adapter = new Retrofit.Builder()
                .baseUrl(CommonString.URL)
                .addConverterFactory(new StringConverterFactory())
                .build();
        PostApiForFile api = adapter.create(PostApiForFile.class);
        Call<String> call = api.getUploadImage(body1);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response) {

                String responseBody = response.body();

                if (responseBody != null && response.isSuccess()) {

                    // if (response.toString() != null) {
                    if (response.body().contains(CommonString.KEY_SUCCESS)) {
                        isvalid = true;
                        result = CommonString.KEY_SUCCESS;
                        originalFile.delete();
                    } else {
                        result = "Servererror!";
                    }
                } else {
                    result = "Servererror!";
                }
            }

            @Override
            public void onFailure(Throwable t) {

                isvalid = true;
                if (t instanceof UnknownHostException) {
                    result = AlertandMessages.MESSAGE_SOCKETEXCEPTION;
                } else {
                    result = AlertandMessages.MESSAGE_SOCKETEXCEPTION;
                }
                Toast.makeText(context, originalFile.getName() + " not uploaded", Toast.LENGTH_SHORT).show();
            }
        });
        return result;
    }

    public void showExportDialog() {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainMenuActivity.this);
        builder1.setMessage(R.string.Areyou_sure_take_backup)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @SuppressWarnings("resource")
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            File file = new File(Environment.getExternalStorageDirectory(), "INTEL_RE_backup");
                            if (!file.isDirectory()) {
                                file.mkdir();
                            }

                            File sd = Environment.getExternalStorageDirectory();
                            File data = Environment.getDataDirectory();

                            if (sd.canWrite()) {
                                long date = System.currentTimeMillis();

                                SimpleDateFormat sdf = new SimpleDateFormat("MMM/dd/yy");
                                String dateString = sdf.format(date);

                                String currentDBPath = "//data//cpm.com.intelre//databases//" + INTEL_RE_DB.DATABASE_NAME;
                                String backupDBPath = "INTEL_RE_backup" + user_name + dateString.replace('/', '_') + getCurrentTime().replace(":", "") + ".db";

                                String path = Environment.getExternalStorageDirectory().getPath() + "/INTEL_RE_backup";

                                File currentDB = new File(data, currentDBPath);
                                File backupDB = new File(path, backupDBPath);
                                //Snackbar.make(rec_store_data, "Database Exported Successfully", Snackbar.LENGTH_SHORT).show();

                                if (currentDB.exists()) {
                                    @SuppressWarnings("resource")
                                    FileChannel src = new FileInputStream(currentDB).getChannel();
                                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                                    dst.transferFrom(src, 0, src.size());
                                    src.close();
                                    dst.close();

                                }

                                   /* File originalFile = new File(path + "/" + backupDBPath).exists()));
                                   *//* RetrofitMethod uploadRetro = new RetrofitMethod(context, "Uploading Backup");
                                    uploadRetro.uploadedFiles = 0;
                                    uploadRetro.UploadBackup(backupDBPath, "DBBackup", path + "/");*//*
                                    Object result = uploadBackup(MainMenuActivity.this, originalFile.getName(), "DBBackup");
                                    if (result.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS)) {

                                    }
                                }*/
                                //usk
                                File dir = new File(CommonString.BACKUP_FILE_PATH);
                                ArrayList<String> list = new ArrayList();
                                list = getFileNames(dir.listFiles());
                                if (list.size() > 0) {
                                    for (int i1 = 0; i1 < list.size(); i1++) {
                                        if (list.get(i1).contains("INTEL_RE_backup")) {
                                            File originalFile = new File(CommonString.BACKUP_FILE_PATH + list.get(i1));
                                            Object result = uploadBackup(MainMenuActivity.this, originalFile.getName(), "DBBackup");
                                            if (result.toString().equalsIgnoreCase(CommonString.KEY_SUCCESS)) {

                                            }
                                        }
                                    }
                                }
                                Snackbar.make(imageView, "Database Exported And Uploaded Successfully", Snackbar.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            AlertandMessages.showAlert((Activity) context, context.getString(R.string.errordatabase_not_exporting), true);
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert1 = builder1.create();
        alert1.show();
    }

    public String getCurrentTime() {

        Calendar m_cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss:mmm");
        String cdate = formatter.format(m_cal.getTime());
        return cdate;

    }

    public ArrayList<String> getFileNames(File[] file) {
        ArrayList<String> arrayFiles = new ArrayList<String>();
        if (file.length > 0) {
            for (int i = 0; i < file.length; i++)
                arrayFiles.add(file[i].getName());
        }
        return arrayFiles;
    }


}
