package intelre.cpm.com.intelre.GeoTag;

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
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONObject;

import intelre.cpm.com.intelre.Database.INTEL_RE_DB;
import intelre.cpm.com.intelre.IntelLoginActivty;
import intelre.cpm.com.intelre.MainMenuActivity;
import intelre.cpm.com.intelre.R;
import intelre.cpm.com.intelre.constant.AlertandMessages;
import intelre.cpm.com.intelre.constant.CommonFunctions;
import intelre.cpm.com.intelre.constant.CommonString;
import intelre.cpm.com.intelre.delegates.CoverageBean;
import intelre.cpm.com.intelre.gettersetter.GeotaggingBeans;
import intelre.cpm.com.intelre.gsonGetterSetter.JourneyPlan;
import intelre.cpm.com.intelre.retrofit.PostApi;
import intelre.cpm.com.intelre.retrofit.UploadImageWithRetrofit;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class GeoTaggingActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    String result, errormsg = "";
    private ProgressBar pb;
    private GoogleMap mMap;
    double latitude = 0.0;
    double longitude = 0.0;
    protected String diskpath = "", _path, _pathforcheck, img_str = "", status;
    private Location mLastLocation;
    private LocationManager locmanager = null;
    FloatingActionButton fab, fabcarmabtn;
    SharedPreferences preferences;
    String username, str, storename, visit_date_formatted, visitData, storeid;
    INTEL_RE_DB db;
    LocationManager locationManager;
    Geocoder geocoder;
    boolean enabled;
    boolean uploadflag = false;
    private Dialog dialog;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private static int UPDATE_INTERVAL = 500; // 5 sec
    private static int FATEST_INTERVAL = 100; // 1 sec
    private static int DISPLACEMENT = 5; // 10 meters
    private static final String TAG = GeoTaggingActivity.class.getSimpleName();
    ArrayList<GeotaggingBeans> geotaglist = new ArrayList<>();
    String tag_from = "";
    ArrayList<GeotaggingBeans> geotaglistImage = new ArrayList<GeotaggingBeans>();
    Boolean markerflag = true;
    private GoogleApiClient client;
    Context context;
    Activity activity;
    UploadImageWithRetrofit upload;
    CoverageBean coverageBean;
    ArrayList<CoverageBean> coverageBeanList;
    String app_ver = "0";
    ProgressDialog loading;
    private Retrofit adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_tagging);
        declaration();
        if (checkPlayServices()) {
            // Building the GoogleApi client
            buildGoogleApiClient();
            createLocationRequest();
        }
        locmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        enabled = locmanager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!enabled) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            // Setting Dialog Title
            alertDialog.setTitle(getResources().getString(R.string.gps));
            // Setting Dialog Message
            alertDialog.setMessage(getResources().getString(R.string.gpsebale));
            // Setting Positive "Yes" Button
            alertDialog.setPositiveButton(getResources().getString(R.string.yes),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    });

            // Setting Negative "NO" Button
            alertDialog.setNegativeButton(getResources().getString(R.string.no),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to invoke NO event

                            dialog.cancel();
                        }
                    });

            // Showing Alert Message
            alertDialog.show();

        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!img_str.equalsIgnoreCase("")) {
                    status = "N";

                    //  db.updateStatus(storeid, status);
                    if (db.InsertSTOREgeotag(storeid, latitude, longitude, img_str, status) > 0) {
                        img_str = "";
                        //uploadGeotagData();
                        new GeoTagUpload(GeoTaggingActivity.this).execute();
                    } else {
                        Snackbar.make(fab, "Error in saving Geotag", Snackbar.LENGTH_SHORT).show();
                    }

                } else {
                    Snackbar.make(fab, "Please Take Image", Snackbar.LENGTH_SHORT).show();

                }

            }
        });
        fabcarmabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                _pathforcheck = storeid + "_" + username.replace(".", "") + "_GeoTag-" + visit_date_formatted + "-" + CommonFunctions.getCurrentTimeHHMMSS() + ".jpg";
                _path = CommonString.FILE_PATH + _pathforcheck;
                CommonFunctions.startCameraActivity(activity, _path);

            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.notsuppoted), Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    protected void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (LocationListener) this);
        }
    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (LocationListener) this);
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (mLastLocation != null) {
                latitude = mLastLocation.getLatitude();
                longitude = mLastLocation.getLongitude();

                mMap.setMyLocationEnabled(true);

                if (markerflag) {
                    // Add a marker of latest location and move the camera
                    LatLng latLng = new LatLng(latitude, longitude);
                    mMap.addMarker(new MarkerOptions().position(latLng));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15));


                }

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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }


    protected void onStart() {
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    protected void onStop() {
        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.disconnect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    public String getCurrentTime() {
        Calendar m_cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss:mmm");
        String cdate = formatter.format(m_cal.getTime());
        return cdate;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 0:
                Log.i("MakeMachine", "User cancelled");
                break;
            case -1:
                if (_pathforcheck != null && !_pathforcheck.equals("")) {
                    if (new File(str + _pathforcheck).exists()) {
                        // String storeProfileImg = storeid + "_store_img_" + visitData.replace("/", "") + ".jpg";
                        fabcarmabtn.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.mipmap.camera_green));
                        fabcarmabtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4DB6AC")));
                        img_str = _pathforcheck;
                        _pathforcheck = "";
                        markerflag = false;

                    }
                }

                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("GeoTag Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onLocationChanged(Location location) {

    }


    void declaration() {
        activity = this;
        context = this;
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        username = preferences.getString(CommonString.KEY_USERNAME, null);
        storeid = getIntent().getStringExtra(CommonString.KEY_STORE_ID);
        storename = preferences.getString(CommonString.KEY_STORE_NAME, null);
        visit_date_formatted = preferences.getString(CommonString.KEY_YYYYMMDD_DATE, "");
        visitData = preferences.getString(CommonString.KEY_DATE, "");
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fabcarmabtn = (FloatingActionButton) findViewById(R.id.camrabtn);
        db = new INTEL_RE_DB(context);
        db.open();
        str = CommonString.FILE_PATH;
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        geocoder = new Geocoder(this);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        upload = new UploadImageWithRetrofit(context);
        //  tag_from = getIntent().getStringExtra(CommonString.TAG_FROM);
        try {
            app_ver = String.valueOf(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (tag_from != null && tag_from.equalsIgnoreCase(CommonString.TAG_FROM_NONWORKING)) {
            coverageBean = (CoverageBean) getIntent().getSerializableExtra(CommonString.TAG_OBJECT);
        }
    }


    private void uploadGeotagData() {
        try {
            loading = ProgressDialog.show(GeoTaggingActivity.this, "Processing", "Please wait...", false, false);
            uploadflag = false;
            geotaglist = db.getinsertGeotaggingData(storeid, "N");
            if (geotaglist.size() > 0) {
                JSONArray topUpArray = new JSONArray();
                JSONObject obj = new JSONObject();
                for (int j = 0; j < geotaglist.size(); j++) {
                    obj.put(CommonString.KEY_STORE_ID, geotaglist.get(j).getStoreid());
                    obj.put(CommonString.KEY_VISIT_DATE, "");
                    obj.put(CommonString.KEY_LATITUDE, geotaglist.get(j).getLatitude());
                    obj.put(CommonString.KEY_LONGITUDE, geotaglist.get(j).getLongitude());
                    obj.put("FRONT_IMAGE", geotaglist.get(j).getImage());
                    topUpArray.put(obj);
                }
                obj = new JSONObject();
                obj.put("MID", "0");
                obj.put("Keys", "GeoTag");
                obj.put("JsonData", topUpArray.toString());
                obj.put("UserId", username);
                String jsonString = obj.toString();

                RequestBody jsonData = RequestBody.create(MediaType.parse("application/json"), jsonString);
                adapter = new Retrofit.Builder().baseUrl(CommonString.URL).
                        addConverterFactory(GsonConverterFactory.create()).build();
                PostApi api = adapter.create(PostApi.class);

                Call<JsonObject> call = api.getGeotag(jsonData);
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                       // String responseBody = response.body().toString();
                        String responseBody =response.body().get("UploadJsonDetailResult").toString();

                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        String gfdg = t.toString();

                    }
                });


            }


        } catch (Exception e) {
            e.printStackTrace();
            AlertandMessages.showAlertlogin(GeoTaggingActivity.this,
                    e.getLocalizedMessage().toString());
        }
    }


    public class GeoTagUpload extends AsyncTask<Void, Void, String> {

        private Context context;

        GeoTagUpload(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();

            dialog = new Dialog(context);
            dialog.setContentView(R.layout.custom);
            dialog.setTitle(getResources().getString(R.string.dialog_title));
            dialog.setCancelable(false);
            dialog.show();
            pb = (ProgressBar) dialog.findViewById(R.id.progressBar1);
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                // uploading Geotag
                uploadflag = false;
                geotaglist = db.getinsertGeotaggingData(storeid, "N");
                if (geotaglist.size() > 0) {
                    JSONArray topUpArray = new JSONArray();
                    for (int j = 0; j < geotaglist.size(); j++) {
                        JSONObject obj = new JSONObject();
                        obj.put(CommonString.KEY_STORE_ID, geotaglist.get(j).getStoreid());
                        obj.put(CommonString.KEY_VISIT_DATE, visitData);
                        obj.put(CommonString.KEY_LATITUDE, geotaglist.get(j).getLatitude());
                        obj.put(CommonString.KEY_LONGITUDE, geotaglist.get(j).getLongitude());
                        obj.put("FRONT_IMAGE", geotaglist.get(j).getImage());
                        topUpArray.put(obj);
                    }

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("MID", "0");
                    jsonObject.put("Keys", "GeoTag");
                    jsonObject.put("JsonData", topUpArray.toString());
                    jsonObject.put("UserId", username);

                    String jsonString2 = jsonObject.toString();
                    result = upload.downloadDataUniversal(jsonString2, CommonString.UPLOADJsonDetail);
                    if (result.equalsIgnoreCase(CommonString.MESSAGE_NO_RESPONSE_SERVER)) {
                        uploadflag = false;
                        throw new SocketTimeoutException();
                    } else if (result.equalsIgnoreCase(CommonString.MESSAGE_SOCKETEXCEPTION)) {
                        uploadflag = false;
                        throw new IOException();
                    } else if (result.equalsIgnoreCase(CommonString.MESSAGE_INVALID_JSON)) {
                        uploadflag = false;
                        throw new JsonSyntaxException("Primary_Grid_Image");
                    } else if (result.equalsIgnoreCase(CommonString.KEY_FAILURE)) {
                        uploadflag = false;
                        throw new Exception();
                    } else {
                        uploadflag = true;
                    }
                }

            } catch (SocketException ex) {
                uploadflag = false;
                ex.printStackTrace();
                errormsg = CommonString.MESSAGE_INTERNET_NOT_AVALABLE;
            } catch (IOException ex) {
                uploadflag = false;
                ex.printStackTrace();
                errormsg = CommonString.MESSAGE_INTERNET_NOT_AVALABLE;
            } catch (JsonSyntaxException ex) {
                uploadflag = false;
                ex.printStackTrace();
                errormsg = CommonString.MESSAGE_INVALID_JSON;
            } catch (NumberFormatException e) {
                uploadflag = false;
                errormsg = CommonString.MESSAGE_NUMBER_FORMATE_EXEP;
            } catch (Exception ex) {
                uploadflag = false;
                errormsg = CommonString.MESSAGE_EXCEPTION;
            }

            if (uploadflag) {
                return CommonString.KEY_SUCCESS;
            } else {
                return errormsg;
            }

        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dialog.dismiss();
            if (result.equals(CommonString.KEY_SUCCESS)) {
                //  new GeoTagImageUpload().execute();
                status = "Y";

                db.updateStatus(storeid, status);
                if (db.updateInsertedGeoTagStatus(storeid, status) > 0) {
                    img_str = "";
                    AlertandMessages.showToastMsg(context, "Geotag Saved Successfully");
                    GeoTaggingActivity.this.finish();
                } else {
                    AlertandMessages.showAlert((Activity) context, "Error in updating Geotag status", true);
                }

            } else {
                AlertandMessages.showAlert((Activity) context,
                        getResources().getString(R.string.failure) + " : " + errormsg, true);
                GeoTaggingActivity.this.finish();
            }

        }

    }

}
