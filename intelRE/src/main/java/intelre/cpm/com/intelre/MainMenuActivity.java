package intelre.cpm.com.intelre;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
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

import intelre.cpm.com.intelre.dailyentry.ServiceActivity;

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
        tv_username.setText("testmer");
        //tv_usertype.setText(user_type);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_route_plan) {

        } else if (id == R.id.nav_download) {

        } else if (id == R.id.nav_upload) {

        } else if (id == R.id.nav_geotag) {

        } else if (id == R.id.nav_exit) {

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
       // date = preferences.getString(CommonString.KEY_DATE, null);
        imageView = (ImageView) findViewById(R.id.img_main);
        webView = (WebView) findViewById(R.id.webview);
        toolbar.setTitle(getString(R.string.main_menu_activity_name) + " - " + date);
       /* getSupportActionBar().setTitle(getString(R.string.main_menu_activity_name) + " \n- " + date);
        db = new GSKGTMerDB(MainActivity.this);
        db.open();*/
    }
    @Override
    protected void onResume() {
        super.onResume();
       /* downloadIndex = preferences.getInt(CommonString.KEY_DOWNLOAD_INDEX, 0);
        coverageList = db.getCoverageData(date);
        storelist = db.getStoreData(date);*/
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

}
