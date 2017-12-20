package intelre.cpm.com.intelre;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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

import java.util.ArrayList;
import intelre.cpm.com.intelre.constant.CommonString;
import intelre.cpm.com.intelre.dailyentry.ServiceActivity;
import intelre.cpm.com.intelre.dailyentry.StoreListActivity;
import intelre.cpm.com.intelre.dailyentry.ServiceActivity;
import intelre.cpm.com.intelre.download.DownloadActivity;

public class MainMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private WebView webView;
    private ImageView imageView;
    private String date;
    private View headerView;
    private String error_msg;
    private Toolbar toolbar;
    private Context context;
    private int downloadIndex;
    private SharedPreferences preferences;

    String visit_date, user_type, user_name;

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
                boolean isDownloadValid = true;
/*
                if (db.isCoveragePreviousDataFilled(date)) {
                    if (isPreviousDataValid()) {
                        isDownloadValid = false;
                    }
                }
*/
                if (isDownloadValid) {
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

                                  /*  Intent in = new Intent(getApplicationContext(), PreviousDataUploadActivity.class);
                                    startActivity(in);*/
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

        } else if (id == R.id.nav_geotag) {

        } else if (id == R.id.nav_exit) {
            startActivity(new Intent(MainMenuActivity.this, IntelLoginActivty.class));
            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);

            finish();
        } else if (id == R.id.nav_services) {

            Intent startservice = new Intent(this, ServiceActivity.class);
            startActivity(startservice);
            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
/*
    private boolean isPreviousDataValid() {

        boolean flag_isvalid = false;

        JourneyPlan jcp;
        //ArrayList<CoverageBean> coverage_list = db.getCoverageData(date);
        ArrayList<CoverageBean> coverage_list = db.getCoverageDataPrevious(date);
        for (int i = 0; i < coverage_list.size(); i++) {

            if (coverage_list.get(i).getFrom().equals(CommonString.TAG_FROM_JCP)) {
                jcp = db.getSpecificStoreDataPrevious(date, coverage_list.get(i).getStoreId());
            } else {
                jcp = db.getSpecificStoreDataDeviationPrevious(date, coverage_list.get(i).getStoreId());
            }

            if (jcp.getUploadStatus().equalsIgnoreCase(CommonString.KEY_CHECK_IN)) {
                if (isValid(jcp.getStoreId()) || isValidForSelf(jcp)) {
                    flag_isvalid = true;
                    break;
                }*/
/* else {
                    db.deleteTableWithStoreID(String.valueOf(jcp.getStoreId()));
                }*//*

            } else if (jcp.getUploadStatus().equalsIgnoreCase(CommonString.KEY_C)
                    || jcp.getUploadStatus().equalsIgnoreCase(CommonString.KEY_P)
                    || jcp.getUploadStatus().equalsIgnoreCase(CommonString.KEY_D)
                    || jcp.getUploadStatus().equalsIgnoreCase(CommonString.STORE_STATUS_LEAVE)
                    ) {
                flag_isvalid = true;
                break;
            }

        }

        return flag_isvalid;
    }
*/


}
