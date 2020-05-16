package intelre.cpm.com.hulcnc.dailyentry;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import intelre.cpm.com.hulcnc.Database.HUL_CNC_DB;
import intelre.cpm.com.hulcnc.download.DownloadActivity;
import intelre.cpm.com.hulcnc.retrofit.PostApi;
import intelre.cpm.com.hulcnc.upload.PreviousDataUploadActivity;
import cpm.com.hulcnc.R;
import intelre.cpm.com.hulcnc.constant.AlertandMessages;
import intelre.cpm.com.hulcnc.constant.CommonString;
import intelre.cpm.com.hulcnc.delegates.CoverageBean;
import intelre.cpm.com.hulcnc.gpsenable.LocationEnableCommon;
import intelre.cpm.com.hulcnc.gsonGetterSetter.JourneyPlan;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class StoreListActivity extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private Context context;
    private String userId, destributor_id;
    private ArrayList<CoverageBean> coverage = new ArrayList<>();
    private ArrayList<JourneyPlan> storelist = new ArrayList<>();
    private String date;
    private HUL_CNC_DB database;
    private ValueAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayout linearlay;
    private Dialog dialog;
    private boolean result_flag = false;
    private FloatingActionButton fab;
    private double lat = 0.0;
    private double lon = 0.0;
    SharedPreferences preferences;
    private GoogleApiClient mGoogleApiClient;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private SharedPreferences.Editor editor = null;
    private LocationRequest mLocationRequest;
    LocationEnableCommon locationEnableCommon;
    private static final String TAG = StoreimageActivity.class.getSimpleName();
    int downloadIndex;
    ProgressDialog loading;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storelistfablayout);
        declaration();
        if (checkPlayServices()) {
            // Building the GoogleApi client
            buildGoogleApiClient();
            createLocationRequest();
        }
        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Download data
                if (checkNetIsAvailable()) {
                    if (database.isCoverageDataFilled(date)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(StoreListActivity.this);
                        builder.setTitle("Parinaam");
                        builder.setMessage("Please Upload Previous Data First")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(StoreListActivity.this, PreviousDataUploadActivity.class);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {
                        try {
                            database.open();
                            database.deletePreviousUploadedData(date);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Intent startDownload = new Intent(StoreListActivity.this, DownloadActivity.class);
                        startActivity(startDownload);
                        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                        finish();
                    }
                } else {
                    Snackbar.make(recyclerView, "No Network Available", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                }
            }

        });
    }

    public boolean checkNetIsAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }


    protected void onResume() {
        super.onResume();
       /* database.open();
        storelist = database.getStoreData(date);
        downloadIndex = preferences.getInt(CommonString.KEY_DOWNLOAD_INDEX, 0);
        if (storelist.size() > 0 && downloadIndex == 0) {
            adapter = new ValueAdapter(StoreListActivity.this, storelist);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            linearlay.setVisibility(View.GONE);
            fab.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
*/
        setListData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // NavUtils.navigateUpFromSameTask(this);
            finish();
            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }


    public class ValueAdapter extends RecyclerView.Adapter<ValueAdapter.MyViewHolder> {
        private LayoutInflater inflator;
        List<JourneyPlan> data = Collections.emptyList();

        public ValueAdapter(Context context, List<JourneyPlan> data) {
            inflator = LayoutInflater.from(context);
            this.data = data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            View view = inflator.inflate(R.layout.storeviewlist, parent, false);
            return new MyViewHolder(view);
        }

        @SuppressWarnings("deprecation")
        @Override
        public void onBindViewHolder(final MyViewHolder viewHolder, final int position) {
            final JourneyPlan current = data.get(position);
            viewHolder.chkbtn.setBackgroundResource(R.mipmap.checkout);
            // viewHolder.txt.setText(current.getStoreName() + " - " + current.getClassification());
            viewHolder.txt.setText(current.getStoreName());
            viewHolder.address.setText(current.getAddress1());
            if (current.getUploadStatus().equalsIgnoreCase(CommonString.KEY_U)) {
                viewHolder.imageview.setVisibility(View.VISIBLE);
                viewHolder.imageview.setBackgroundResource(R.drawable.tick_u);
                viewHolder.chkbtn.setVisibility(View.INVISIBLE);
                viewHolder.Cardbtn.setCardBackgroundColor(getResources().getColor(R.color.colorOrange));
            } else if (current.getUploadStatus().equalsIgnoreCase(CommonString.KEY_D)) {
                viewHolder.imageview.setVisibility(View.VISIBLE);
                viewHolder.imageview.setBackgroundResource(R.drawable.tick_d);
                viewHolder.chkbtn.setVisibility(View.INVISIBLE);
                viewHolder.Cardbtn.setCardBackgroundColor(getResources().getColor(R.color.colorOrange));
            } else if (current.getUploadStatus().equalsIgnoreCase(CommonString.KEY_P)) {
                viewHolder.imageview.setVisibility(View.VISIBLE);
                viewHolder.imageview.setBackgroundResource(R.drawable.tick_p);
                viewHolder.chkbtn.setVisibility(View.INVISIBLE);
                viewHolder.Cardbtn.setCardBackgroundColor(getResources().getColor(R.color.colorOrange));
            } else if (current.getUploadStatus().equalsIgnoreCase(CommonString.KEY_C)) {
                viewHolder.imageview.setVisibility(View.VISIBLE);
                viewHolder.imageview.setBackgroundResource(R.mipmap.tick);
                viewHolder.chkbtn.setVisibility(View.INVISIBLE);
                viewHolder.Cardbtn.setCardBackgroundColor(getResources().getColor(R.color.colorOrange));
            } else if (current.getUploadStatus().equalsIgnoreCase(CommonString.KEY_CHECK_IN)) {
              /*  viewHolder.chkbtn.setVisibility(View.VISIBLE);
                viewHolder.imageview.setVisibility(View.INVISIBLE);*/

                //usk
                if (chekDataforCheckout(current.getStoreId().toString(), current.getRegionId().toString(), current.getQuizOpen())) {
                    viewHolder.chkbtn.setVisibility(View.VISIBLE);
                    viewHolder.imageview.setVisibility(View.INVISIBLE);
                } else {
                    viewHolder.imageview.setVisibility(View.INVISIBLE);
                    viewHolder.chkbtn.setVisibility(View.INVISIBLE);
                    viewHolder.Cardbtn.setCardBackgroundColor(getResources().getColor(R.color.green));
                }
                //usk end

            } else if (current.getUploadStatus().equalsIgnoreCase(CommonString.STORE_STATUS_LEAVE)) {
                viewHolder.imageview.setVisibility(View.VISIBLE);
                viewHolder.imageview.setBackgroundResource(R.drawable.leave_tick);
            } else {
                viewHolder.Cardbtn.setCardBackgroundColor(getResources().getColor(R.color.colorOrange));
                viewHolder.imageview.setVisibility(View.VISIBLE);
                viewHolder.chkbtn.setVisibility(View.INVISIBLE);
            }

            viewHolder.relativelayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int store_id = current.getStoreId();
                    if (current.getUploadStatus().equalsIgnoreCase(CommonString.KEY_U)) {
                        Snackbar.make(v, R.string.title_store_list_activity_store_already_done, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    } else if (current.getUploadStatus().equalsIgnoreCase(CommonString.KEY_D)) {
                        Snackbar.make(v, R.string.title_store_list_activity_store_data_uploaded, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    } else if (current.getUploadStatus().equalsIgnoreCase(CommonString.KEY_C)) {
                        Snackbar.make(v, R.string.title_store_list_activity_store_already_checkout, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    } else if (current.getUploadStatus().equalsIgnoreCase(CommonString.KEY_P)) {
                        Snackbar.make(v, R.string.title_store_list_activity_store_again_uploaded, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    } else if (current.getUploadStatus().equalsIgnoreCase(CommonString.STORE_STATUS_LEAVE)) {
                        Snackbar.make(v, R.string.title_store_list_activity_already_store_closed, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    } else {
                        boolean entry_flag = true;
                        boolean showdialog = true;
                        for (int j = 0; j < storelist.size(); j++) {
                            if (storelist.get(j).getUploadStatus().equalsIgnoreCase(CommonString.KEY_CHECK_IN) ||
                                    storelist.get(j).getUploadStatus().equalsIgnoreCase(CommonString.KEY_VALID)) {
                                showdialog = false;
                                if (store_id != storelist.get(j).getStoreId()) {
                                    entry_flag = false;
                                    break;
                                } else {
                                    break;
                                }
                            }
                        }
                        if (entry_flag) {
                            showMyDialog(current, showdialog);
                        } else {
                            Snackbar.make(v, R.string.title_store_list_checkout_current, Snackbar.LENGTH_SHORT).setAction("Action", null).show();

                        }

                    }
                }
            });
            viewHolder.chkbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(StoreListActivity.this);
                    builder.setMessage(R.string.wantcheckout)
                            .setCancelable(false)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    if (CheckNetAvailability()) {
                                        Intent intent = new Intent(StoreListActivity.this, CheckoutActivty.class);
                                        intent.putExtra(CommonString.KEY_STORE_CD, current.getStoreId().toString());
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);

                                    } else {
                                        Snackbar.make(recyclerView, R.string.nonetwork, Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                                    }

                                }
                            })
                            .setNegativeButton(R.string.closed, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
        }

        @SuppressWarnings("deprecation")
        public boolean CheckNetAvailability() {
            boolean connected = false;
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                    .getState() == NetworkInfo.State.CONNECTED
                    || connectivityManager.getNetworkInfo(
                    ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                // we are connected to a network
                connected = true;
            }
            return connected;
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView txt, address;
            RelativeLayout relativelayout;
            ImageView imageview;
            Button chkbtn;
            CardView Cardbtn;

            public MyViewHolder(View itemView) {
                super(itemView);
                txt = itemView.findViewById(R.id.storelistviewxml_storename);
                address = itemView.findViewById(R.id.storelistviewxml_storeaddress);
                relativelayout = itemView.findViewById(R.id.storenamelistview_layout);
                imageview = itemView.findViewById(R.id.storelistviewxml_storeico);
                chkbtn = itemView.findViewById(R.id.chkout);
                Cardbtn = itemView.findViewById(R.id.card_view);
            }
        }

    }


    void showMyDialog(final JourneyPlan current, final boolean isVisitLater) {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogbox);
        RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.radiogrpvisit);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.yes) {
                    if (!current.getGeoTag().equalsIgnoreCase(CommonString.KEY_Y)) {
                        dialog.dismiss();
                        Snackbar.make(recyclerView, R.string.title_store_list_geo_tag, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    } else {
                        editor = preferences.edit();
                        editor.putString(CommonString.KEY_STOREVISITED_STATUS, "Yes");
                        editor.putString(CommonString.KEY_STORE_NAME, current.getStoreName());
                        editor.putString(CommonString.KEY_STORE_CD, current.getStoreId().toString());
                        editor.putString(CommonString.KEY_REGION_ID, current.getRegionId().toString());
                        editor.putString(CommonString.KEY_DESTRIBUTOR_ID, current.getDistributorId().toString());
                        editor.putString(CommonString.KEY_FLAG_QUIZ, current.getQuizOpen());
                        editor.putString(CommonString.KEY_FLAG_TRAINING_TIME, current.getTimePeriod().toString());

                        editor.commit();
                        dialog.cancel();
                        ArrayList<CoverageBean> specdata;
                        specdata = database.getSpecificCoverageData(current.getVisitDate(), current.getStoreId().toString());
                        if (specdata.size() == 0 && !isVisitLater) {
                            Intent in = new Intent(StoreListActivity.this, StoreimageActivity.class);
                            startActivity(in);
                            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                        } else if (!isVisitLater) {
                            //   Intent in = new Intent(StoreListActivity.this, StoreProfileActivity.class);
                            Intent in = new Intent(StoreListActivity.this, StoreEntryActivity.class);
                            startActivity(in);
                            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                        } else {
                            Intent in = new Intent(StoreListActivity.this, StoreimageActivity.class);
                            startActivity(in);
                            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                        }
                    }
                } else if (checkedId == R.id.no) {
                    dialog.cancel();
                    if (current.getUploadStatus().equals(CommonString.KEY_CHECK_IN) ||
                            current.getUploadStatus().equals(CommonString.KEY_VALID)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(StoreListActivity.this);
                        builder.setMessage(CommonString.DATA_DELETE_ALERT_MESSAGE)
                                .setCancelable(false)
                                .setPositiveButton("Yes",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int id) {
                                                try {
                                                    deletecoverageData(StoreListActivity.this, current);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        })
                                .setNegativeButton("No",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int id) {
                                                dialog.cancel();
                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {
                        UpdateData(current.getStoreId().toString(), current.getVisitDate());
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(CommonString.KEY_STORE_CD, current.getStoreId().toString());
                        editor.commit();
                        Intent in = new Intent(StoreListActivity.this, NonWorkingReasonActivity.class);
                        startActivity(in);
                        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                    }
                }
            }

        });
        dialog.show();
    }

    public void UpdateData(String storeCd, String visit_date) {
        database.open();
        database.deleteSpecificStoreData(storeCd);
        database.updateJaurneyPlanSpecificStoreStatus(storeCd, visit_date, "N");
    }


    private void declaration() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        date = preferences.getString(CommonString.KEY_DATE, null);
        linearlay = (LinearLayout) findViewById(R.id.no_data_lay);
        recyclerView = (RecyclerView) findViewById(R.id.drawer_layout_recycle);
        userId = preferences.getString(CommonString.KEY_USERNAME, null);
        destributor_id = preferences.getString(CommonString.KEY_DESTRIBUTOR_ID, null);
        context = this;
        getSupportActionBar().setTitle("Store List - " + date);
        locationEnableCommon = new LocationEnableCommon();
        locationEnableCommon.checkgpsEnableDevice(this);
        database = new HUL_CNC_DB(this);
        database.open();
        setListData();
    }


    @SuppressWarnings("deprecation")
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                finish();
            }
            return false;
        }
        return true;
    }

    private synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        int UPDATE_INTERVAL = 500;
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        int FATEST_INTERVAL = 100;
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        int DISPLACEMENT = 5;
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (mLastLocation != null) {
                lat = mLastLocation.getLatitude();
                lon = mLastLocation.getLongitude();

            }
        }

        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }


    protected void onStart() {
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        //client.connect();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }

    }


    /*
        private boolean chekDataforCheckout(String store_cd, String region_id, String quiz_open) {
            boolean status = false;
            database.open();

            if (database.getSelectCategoryData(region_id,destributor_id).size()>0){
                if (database.isStoreAuditFilled(store_cd)) {
                    status = true;
                }else {
                    status = false;
                }
            }else {
                status = true;
            }

            if (status){
                if (quiz_open.equals("Y")) {
                    if (database.getHeaderQuizData().size()>0){
                        if (database.isQuizFilled(store_cd)) {
                            status = true;
                        }else {
                            status = false;
                        }
                    }else {
                        status = true;
                    }
                }else {
                    status = true;
                }

            }

            if (status){
                if (database.getSelectCategoryData(region_id,destributor_id).size()>0){
                    if (database.isSaleFilled(store_cd)) {
                        status = true;
                    }else {
                        status = false;
                    }
                }else {
                    status = true;
                }

            }


            return status;
        }
    */
    private boolean chekDataforCheckout(String store_cd, String region_id, String quiz_open) {
        boolean status = false;
        database.open();


            if (database.isStoreAuditFilled(store_cd)) {
                status = true;
            } else {
                status = false;
            }


        if (status) {
            if (quiz_open.equals("Y")) {
                if (database.getHeaderQuizData().size() > 0) {
                    if (database.isQuizFilled(store_cd)) {
                        status = true;
                    } else {
                        status = false;
                    }
                } else {
                    status = true;
                }
            } else {
                status = true;
            }

        }

        if (status) {

                if (database.isSaleFilled(store_cd)) {
                    status = true;
                } else {
                    status = false;
                }

        }
        if (status) {

            if (database.isCustomerDataFilled(store_cd)) {
                status = true;
            } else {
                status = false;
            }


        }

        return status;
    }

    private void deletecoverageData(final Context context, final JourneyPlan current) {
        try {
            loading = ProgressDialog.show(StoreListActivity.this, "Processing", "Please wait...",
                    false, false);
            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .build();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("StoreId", current.getStoreId().toString());
            jsonObject.put("VisitDate", current.getVisitDate());
            jsonObject.put("UserId", userId);
            String jsonString2 = jsonObject.toString();
            RequestBody jsonData = RequestBody.create(MediaType.parse("application/json"), jsonString2.toString());
            Retrofit adapter = new Retrofit.Builder().baseUrl(CommonString.URL).client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            PostApi api = adapter.create(PostApi.class);
            retrofit2.Call<ResponseBody> call = api.deleteCoverageData(jsonData);
            call.enqueue(new retrofit2.Callback<ResponseBody>() {
                @Override
                public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    ResponseBody responseBody = response.body();
                    String data = null;
                    if (responseBody != null && response.isSuccessful()) {
                        try {
                            data = response.body().string();
                            if (data.contains(CommonString.KEY_SUCCESS)) {
                                UpdateData(current.getStoreId().toString(), current.getVisitDate());
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString(CommonString.KEY_STORE_CD, current.getStoreId().toString());
                                editor.commit();
                                Intent intent = new Intent((Activity) context, NonWorkingReasonActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                                loading.dismiss();
                            } else {
                                loading.dismiss();
                                AlertandMessages.showAlertlogin((Activity) context, data.toString());

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            loading.dismiss();
                            AlertandMessages.showAlertlogin((Activity) context, e.toString());
                        }
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                    loading.dismiss();
                    if (t instanceof SocketTimeoutException || t instanceof IOException || t instanceof Exception) {
                        AlertandMessages.showAlertlogin((Activity) context, getResources().getString(R.string.nonetwork));
                    } else {
                        AlertandMessages.showAlertlogin((Activity) context, getResources().getString(R.string.nonetwork));
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            loading.dismiss();
            AlertandMessages.showAlertlogin((Activity) context, getResources().getString(R.string.nonetwork));
        }
    }

    void setListData() {
        database.open();
        storelist = database.getStoreData(date);
        downloadIndex = preferences.getInt(CommonString.KEY_DOWNLOAD_INDEX, 0);
        if (storelist.size() > 0 && downloadIndex == 0) {
            adapter = new ValueAdapter(StoreListActivity.this, storelist);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            linearlay.setVisibility(View.GONE);
            fab.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }


}

