package intelre.cpm.com.hulcnc;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cpm.com.hulcnc.R;
import intelre.cpm.com.hulcnc.autoupdate.AutoUpdateActivity;
import intelre.cpm.com.hulcnc.constant.AlertandMessages;
import intelre.cpm.com.hulcnc.constant.CommonString;
import intelre.cpm.com.hulcnc.gettersetter.NoticeBoardGetterSetter;
import intelre.cpm.com.hulcnc.retrofit.PostApi;
import intelre.cpm.com.hulcnc.Get_IMEI_number.ImeiNumberClass;
import intelre.cpm.com.hulcnc.gettersetter.LoginGsonGetterSetter;
import intelre.cpm.com.hulcnc.gpsenable.LocationEnableCommon;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivty extends AppCompatActivity {
    private TextView tv_version;
    private String app_ver;
    private SharedPreferences preferences = null;
    private SharedPreferences.Editor editor = null;
    private final String lat = "0.0";
    private final String lon = "0.0";
    // UI references.
    private AutoCompleteTextView museridView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Context context;
    private String userid;
    private String password;
    private int versionCode;
    private String[] imeiNumbers;
    private int i = 0;
    private static final int PERMISSIONS_REQUEST_READ_PHONE_STATE = 999;
    LocationEnableCommon locationEnableCommon;
    private static final int REQUEST_LOCATION = 1;
    private Button museridSignInButton;
    private ImeiNumberClass imei;
    private String manufacturer;
    private String model;
    private String os_version;
    private Retrofit adapter;
    String imei1 = "", imei2 = "", status;
    ProgressDialog loading;
    String right_answer, rigth_answer_cd = "", qns_cd, ans_cd;

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 10;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 11;
    private static final int MY_PERMISSIONS_REQUEST_STORAGE_READ = 12;
    private static final int MY_PERMISSIONS_REQUEST_STORAGE_WRITE = 14;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private static final int PERMISSION_ALL = 99;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_intel_login_activty);
        Ui_declaration();
        getDeviceName();

    }


    private void attemptLogin() {
        // Reset errors.
        museridView.setError(null);
        mPasswordView.setError(null);
        // Store values at the time of the login attempt.
        userid = museridView.getText().toString().trim();
        password = mPasswordView.getText().toString().trim();
        boolean cancel = false;
        View focusView = null;
        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        // Check for a valid userid address.
        if (TextUtils.isEmpty(userid)) {
            museridView.setError(getString(R.string.error_field_required));
            focusView = museridView;
            cancel = true;
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else if (!isuseridValid(userid)) {
            Snackbar.make(museridView, getString(R.string.error_incorrect_username), Snackbar.LENGTH_SHORT).show();
        } else if (!isPasswordValid(password)) {
            Snackbar.make(museridView, getString(R.string.error_incorrect_password), Snackbar.LENGTH_SHORT).show();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            if (checkNetIsAvailable())
                if (locationEnableCommon.checkgpsEnableDevice(this)) {
                    AttempLogin();
                }
        }
    }

    private boolean isuseridValid(String userid) {
        //TODO: Replace this with your own logic
        boolean flag = true;
        String u_id = preferences.getString(CommonString.KEY_USERNAME, "");
        if (!u_id.equals("") && !userid.equalsIgnoreCase(u_id)) {
            flag = false;
        }
        return flag;
    }

    private void AttempLogin() {
        try {
            loading = ProgressDialog.show(LoginActivty.this, "Processing", "Please wait...", false, false);
            versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Userid", userid);
            jsonObject.put("Password", password);
            jsonObject.put("Intime", getCurrentTime());
            jsonObject.put("Latitude", lat);
            jsonObject.put("Longitude", lon);
            jsonObject.put("Appversion", app_ver);
            jsonObject.put("Attmode", "0");
            jsonObject.put("Networkstatus", "0");
            jsonObject.put("Manufacturer", manufacturer);
            jsonObject.put("ModelNumber", model);
            jsonObject.put("OSVersion", os_version);

            /*if (!imei1.equals("") && !imei2.equals("")) {
                jsonObject.put("IMEINumber1", imei1);
                jsonObject.put("IMEINumber2", imei2);
            } else if (!imei1.equals("") || imei2.equals("")) {
                jsonObject.put("IMEINumber1", imei1);
                jsonObject.put("IMEINumber2", "0");
            } else {
                jsonObject.put("IMEINumber1", "0");
                jsonObject.put("IMEINumber2", "0");
            }*/
           /* if (imeiNumbers.length > 0) {
                jsonObject.put("IMEINumber1", imeiNumbers[0]);
                if (imeiNumbers.length > 1) {
                    jsonObject.put("IMEINumber2", imeiNumbers[1]);
                } else {
                    jsonObject.put("IMEINumber2", "0");
                }
            } else {
                jsonObject.put("IMEINumber1", "0");
                jsonObject.put("IMEINumber2", "0");
            }*/

            if (imeiNumbers!=null && imeiNumbers.length > 0) {
                if (imeiNumbers[0] == null) {
                    jsonObject.put("IMEINumber1", "0");
                } else {
                    jsonObject.put("IMEINumber1", imeiNumbers[0]);
                }
                if (imeiNumbers!=null && imeiNumbers.length > 1) {
                    if (imeiNumbers[1] == null) {
                        jsonObject.put("IMEINumber2", "0");
                    } else {
                        jsonObject.put("IMEINumber2", imeiNumbers[1]);
                    }
                } else {
                    jsonObject.put("IMEINumber2", "0");
                }
            } else {
                jsonObject.put("IMEINumber1", "0");
                jsonObject.put("IMEINumber2", "0");
            }


            String jsonString = jsonObject.toString();
            try {
                final String[] data_global = {""};
                final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .readTimeout(20, TimeUnit.SECONDS)
                        .writeTimeout(20, TimeUnit.SECONDS)
                        .connectTimeout(20, TimeUnit.SECONDS)
                        .build();

                RequestBody jsonData = RequestBody.create(MediaType.parse("application/json"), jsonString);
                adapter = new Retrofit.Builder().baseUrl(CommonString.URL).client(okHttpClient)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                PostApi api = adapter.create(PostApi.class);
                Call<ResponseBody> call = api.getLogindetail(jsonData);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        ResponseBody responseBody = response.body();
                        String data = null;
                        if (responseBody != null && response.isSuccessful()) {
                            try {
                                data = response.body().string();
                                data = data.substring(1, data.length() - 1).replace("\\", "");
                                if (data.contains("Changed")) {
                                    loading.dismiss();
                                    AlertandMessages.showAlertlogin(LoginActivty.this, CommonString.MESSAGE_CHANGED);
                                } else if (data.contains("No data")) {
                                    loading.dismiss();
                                    AlertandMessages.showAlertlogin(LoginActivty.this, CommonString.MESSAGE_LOGIN_NO_DATA);

                                } else if (data.equalsIgnoreCase(CommonString.KEY_FAILURE)) {
                                    AlertandMessages.showAlertlogin(LoginActivty.this, CommonString.KEY_FAILURE + " Please try again");
                                    loading.dismiss();
                                } else {
                                    Gson gson = new Gson();
                                    LoginGsonGetterSetter userObject = gson.fromJson(data, LoginGsonGetterSetter.class);
                                    // PUT IN PREFERENCES
                                    Crashlytics.setUserIdentifier(userid);
                                    editor.putString(CommonString.KEY_USERNAME, userid);
                                    editor.putString(CommonString.KEY_PASSWORD, password);
                                    editor.putString(CommonString.KEY_VERSION, String.valueOf(userObject.getResult().get(0).getAppVersion()));
                                    editor.putString(CommonString.KEY_PATH, userObject.getResult().get(0).getAppPath());
                                    editor.putString(CommonString.KEY_DATE, userObject.getResult().get(0).getCurrentdate());
                                    Date initDate = new SimpleDateFormat("MM/dd/yyyy").parse(userObject.getResult().get(0).getCurrentdate());
                                    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
                                    String parsedDate = formatter.format(initDate);
                                    editor.putString(CommonString.KEY_USER_TYPE, userObject.getResult().get(0).getRightname());
                                    editor.putString(CommonString.KEY_YYYYMMDD_DATE, parsedDate);

                                    //editor.putString(CommonString.KEY_DATE, "11/22/2017");
                                    editor.commit();
                                    JSONObject jsonObjectnotice = new JSONObject();
                                    jsonObjectnotice.put("Username", userid);
                                    jsonObjectnotice.put("Downloadtype", "Notice_Board");
                                    String jsonStringnotice = jsonObjectnotice.toString();
                                    final String[] question_data_globalnotice = {""};
                                    RequestBody questionjsonDatanotice = RequestBody.create(MediaType.parse("application/json"),
                                            jsonStringnotice);
                                    adapter = new Retrofit.Builder().baseUrl(CommonString.URL).client(okHttpClient)
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .build();
                                    PostApi api1notice = adapter.create(PostApi.class);
                                    Call<ResponseBody> callquestnotice = api1notice.getDownloadAllUSINGLOGIN(questionjsonDatanotice);
                                    callquestnotice.enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                            ResponseBody responseBody = response.body();
                                            String data = null;
                                            if   (responseBody != null && response.isSuccessful()) {
                                                try {
                                                    data = response.body().string();
                                                    data = data.substring(1, data.length() - 1).replace("\\", "");
                                                    if (data.equalsIgnoreCase(CommonString.MESSAGE_SOCKETEXCEPTION)) {
                                                        loading.dismiss();
                                                        AlertandMessages.showAlertlogin(LoginActivty.this, "Check Your Internet Connection");
                                                    } else if (data.contains("No Data")) {
                                                        loading.dismiss();
                                                        Intent intent = new Intent(getBaseContext(), MainMenuActivity.class);
                                                        startActivity(intent);
                                                        LoginActivty.this.finish();
                                                    } else {
                                                        Gson gs = new Gson();
                                                        final NoticeBoardGetterSetter noticeBoardGetterSetter = gs.fromJson(data.toString().trim(), NoticeBoardGetterSetter.class);
                                                        editor.putString(CommonString.KEY_NOTICE_BOARD_LINK, noticeBoardGetterSetter.getNoticeBoard().get(0).getNOTICEBOARD());
                                                        editor.commit();
                                                        //Download Todays Questions
                                                        JSONObject jsonObject = new JSONObject();
                                                        jsonObject.put("Username", userid);
                                                        jsonObject.put("Downloadtype", "Today_Question");
                                                        String jsonString = jsonObject.toString();
                                                        final String[] question_data_global = {""};
                                                        RequestBody questionjsonData = RequestBody.create(MediaType.parse("application/json"),
                                                                jsonString);
                                                        adapter = new Retrofit.Builder().baseUrl(CommonString.URL).client(okHttpClient)
                                                                .addConverterFactory(GsonConverterFactory.create())
                                                                .build();
                                                        PostApi api1 = adapter.create(PostApi.class);
                                                        Call<ResponseBody> callquest = api1.getDownloadAllUSINGLOGIN(questionjsonData);
                                                        callquest.enqueue(new Callback<ResponseBody>() {
                                                            @Override
                                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                                ResponseBody responseBody = response.body();
                                                                String data = null;
                                                                if (responseBody != null && response.isSuccessful()) {
                                                                    try {
                                                                        data = response.body().string();
                                                                        data = data.substring(1, data.length() - 1).replace("\\", "");
                                                                        if (data.equalsIgnoreCase(CommonString.MESSAGE_SOCKETEXCEPTION)) {
                                                                            loading.dismiss();
                                                                            AlertandMessages.showAlertlogin(LoginActivty.this, "Check Your Internet Connection");
                                                                        } else if (data.contains("No Data")) {
                                                                            loading.dismiss();
                                                                            Intent intent = new Intent(getBaseContext(), MainMenuActivity.class);
                                                                            startActivity(intent);
                                                                            LoginActivty.this.finish();
                                                                        } else {
                                                                            Gson gs = new Gson();
                                                                            final LoginGsonGetterSetter userques = gs.fromJson(data.toString().trim(), LoginGsonGetterSetter.class);
                                                                            if (preferences.getString(CommonString.KEY_VERSION, "").equals(Integer.toString(versionCode))) {
                                                                                loading.dismiss();
                                                                                final String visit_date = preferences.getString(CommonString.KEY_DATE, "");
                                                                                Intent intent = new Intent(getBaseContext(), MainMenuActivity.class);
                                                                                startActivity(intent);
                                                                                finish();

                                                                            } else {
                                                                                Intent intent = new Intent(getBaseContext(), AutoUpdateActivity.class);
                                                                                intent.putExtra(CommonString.KEY_PATH, preferences.getString(CommonString.KEY_PATH, ""));
                                                                                startActivity(intent);
                                                                                finish();
                                                                            }


                                                                        }
                                                                    } catch (Exception e) {
                                                                        loading.dismiss();
                                                                        AlertandMessages.showAlertlogin(LoginActivty.this, CommonString.MESSAGE_NO_RESPONSE_SERVER + "(" + e.toString() + ")");
                                                                    }
                                                                }
                                                            }

                                                            @Override
                                                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                                loading.dismiss();
                                                                if (t instanceof SocketTimeoutException || t instanceof IOException || t instanceof Exception) {
                                                                    AlertandMessages.showAlertlogin(LoginActivty.this,
                                                                            CommonString.MESSAGE_INTERNET_NOT_AVALABLE + "(" + t.getMessage().toString() + ")");
                                                                } else {
                                                                    AlertandMessages.showAlertlogin(LoginActivty.this,
                                                                            CommonString.MESSAGE_INTERNET_NOT_AVALABLE + "(" + t.getMessage().toString() + ")");
                                                                }
                                                            }
                                                        });

                                                    }
                                                } catch (Exception e) {
                                                    loading.dismiss();
                                                    AlertandMessages.showAlertlogin(LoginActivty.this, CommonString.MESSAGE_NO_RESPONSE_SERVER + "(" + e.toString() + ")");
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                            loading.dismiss();
                                            if (t instanceof SocketTimeoutException || t instanceof IOException || t instanceof Exception) {
                                                AlertandMessages.showAlertlogin(LoginActivty.this,
                                                        CommonString.MESSAGE_INTERNET_NOT_AVALABLE + "(" + t.getMessage().toString() + ")");
                                            } else {
                                                AlertandMessages.showAlertlogin(LoginActivty.this,
                                                        CommonString.MESSAGE_INTERNET_NOT_AVALABLE + "(" + t.getMessage().toString() + ")");
                                            }
                                        }
                                    });



                                }

                            } catch (Exception e) {
                                loading.dismiss();
                                e.printStackTrace();
                                AlertandMessages.showAlertlogin(LoginActivty.this, CommonString.MESSAGE_SOCKETEXCEPTION + "(" + e.toString() + ")");


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        loading.dismiss();
                        if (t instanceof SocketTimeoutException || t instanceof IOException || t instanceof Exception) {
                            AlertandMessages.showAlertlogin(LoginActivty.this,
                                    CommonString.MESSAGE_INTERNET_NOT_AVALABLE + "(" + t.getMessage().toString() + ")");
                        } else {
                            AlertandMessages.showAlertlogin(LoginActivty.this, CommonString.MESSAGE_SOCKETEXCEPTION);

                        }
                    }
                });

            } catch (Exception e) {
                loading.dismiss();
                e.printStackTrace();
                AlertandMessages.showAlertlogin(LoginActivty.this, CommonString.MESSAGE_SOCKETEXCEPTION + "(" + e.toString() + ")");
            }

        } catch (PackageManager.NameNotFoundException e) {
            loading.dismiss();
            AlertandMessages.showAlertlogin(LoginActivty.this, CommonString.MESSAGE_SOCKETEXCEPTION + "(" + e.toString() + ")");

        } catch (JSONException e) {
            loading.dismiss();
            AlertandMessages.showAlertlogin(LoginActivty.this, CommonString.MESSAGE_SOCKETEXCEPTION + "(" + e.toString() + ")");
        }
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        boolean flag = true;
        String pw = preferences.getString(CommonString.KEY_PASSWORD, "");
        if (!pw.equals("") && !password.equals(pw)) {
            flag = false;
        }
        return flag;
    }

    public void getDeviceName() {
        manufacturer = Build.MANUFACTURER;
        model = Build.MODEL;
        os_version = Build.VERSION.RELEASE;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_CANCELED: {
                        finish();
                    }
                    default: {
                        break;
                    }
                }
                break;
        }

    }

    private void showToast(String message) {
        Snackbar.make(museridSignInButton, message, Snackbar.LENGTH_LONG).show();
    }

    public String getCurrentTime() {
        Calendar m_cal = Calendar.getInstance();
        String intime = m_cal.get(Calendar.HOUR_OF_DAY) + ":" + m_cal.get(Calendar.MINUTE) + ":" + m_cal.get(Calendar.SECOND);
        return intime;
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);

    }



  /* @Override
   public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

       checkAppPermission(Manifest.permission.CAMERA, MY_PERMISSIONS_REQUEST_CAMERA);

       if (requestCode == PERMISSIONS_REQUEST_READ_PHONE_STATE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
           imeiNumbers = imei.getDeviceImei();
       }

       if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getApplicationContext(),
               android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
               ContextCompat.checkSelfPermission(getApplicationContext(),
                       android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
           return;
       }

   }*/
  @Override
  public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
      android.util.Log.d("", "Permission callback called-------");
      switch (requestCode) {
          case PERMISSION_ALL: {
              Map<String, Integer> perms = new HashMap<>();
              // Initialize the map with both permissions
              perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
              perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
              perms.put(Manifest.permission.ACCESS_NETWORK_STATE, PackageManager.PERMISSION_GRANTED);
              perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
              perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
              perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
              perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
              // Fill with actual results from user
              if (grantResults.length > 0) {
                  for (int i = 0; i < permissions.length; i++)
                      perms.put(permissions[i], grantResults[i]);
                  // Check for both permissions
                  if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                          && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                          && perms.get(Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED
                          && perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                          && perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                          && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                          && perms.get(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {

                       imeiNumbers = imei.getDeviceImei();
                      // Create a Folder for Images
                     /* File file = new File(Environment.getExternalStorageDirectory(), ".Himalaya_BA_Images");
                      if (!file.isDirectory()) {
                          file.mkdir();
                      }*/
                      android.util.Log.d("", "sms & location services permission granted");
                      // process the normal flow
                      //else any one or both the permissions are not granted
                  } else {
                      Log.d("", "Some permissions are not granted ask again ");
                      //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                      //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                      if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                              ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) ||
                              ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_NETWORK_STATE) ||
                              ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION) ||
                              ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) ||
                              ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE) ||
                              ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
                          showDialogOK("Location,Photos,media,file,manage phone calls and Camera Services Permission required for this app",
                                  new DialogInterface.OnClickListener() {
                                      @Override
                                      public void onClick(DialogInterface dialog, int which) {
                                          switch (which) {
                                              case DialogInterface.BUTTON_POSITIVE:
                                                  checkAndRequestPermissions();
                                                  break;
                                              case DialogInterface.BUTTON_NEGATIVE:
                                                  // proceed with logic by disabling the related features or quit the app.
                                                  Intent startMain = new Intent(Intent.ACTION_MAIN);
                                                  startMain.addCategory(Intent.CATEGORY_HOME);
                                                  startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                                  startActivity(startMain);
                                                  break;
                                          }
                                      }
                                  });
                      }
                      //permission is denied (and never ask again is  checked)
                      //shouldShowRequestPermissionRationale will return false
                      else {
                          Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG).show();
                          //                            //proceed with logic by disabling the related features or quit the app.
                      }
                  }
              }
          }
      }

  }



    class AnswerData {
        public String question_id, answer_id, username, visit_date, right_answer;

        public String getQuestion_id() {
            return question_id;
        }

        public void setQuestion_id(String question_id) {
            this.question_id = question_id;
        }

        public String getAnswer_id() {
            return answer_id;
        }

        public void setAnswer_id(String answer_id) {
            this.answer_id = answer_id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getVisit_date() {
            return visit_date;
        }

        public void setVisit_date(String visit_date) {
            this.visit_date = visit_date;
        }

        public String getRight_answer() {
            return right_answer;
        }

        public void setRight_answer(String right_answer) {
            this.right_answer = right_answer;
        }
    }


    private void Ui_declaration() {
        context = this;
        tv_version = findViewById(R.id.tv_version_code);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        museridView = findViewById(R.id.userid);
        mPasswordView = findViewById(R.id.password);
       museridView.setText("test");
        mPasswordView.setText("gsk@123");
       // museridView.setText("testpromo");
       // museridView.setText("sunny.s");
       // mPasswordView.setText("cpm123");


    /*    museridView.setText("mandava.s");
        mPasswordView.setText("cpm123");*/
        checkAndRequestPermissions();
        museridSignInButton = findViewById(R.id.user_login_button);
        museridSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkNetIsAvailable()) {
                    attemptLogin();
                } else {
                    AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_INTERNET_NOT_AVALABLE, false);
                }
            }
        });
        try {
            app_ver = String.valueOf(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);

        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tv_version.setText("Version " + app_ver);
        imei = new ImeiNumberClass(context);
        locationEnableCommon = new LocationEnableCommon();
        locationEnableCommon.checkgpsEnableDevice(this);
        imei = new ImeiNumberClass(this);
        imeiNumbers = imei.getDeviceImei();
        if (imeiNumbers.length == 2) {
            imei1 = imeiNumbers[0];
            imei2 = imeiNumbers[1];
        } else {
            imei1 = imeiNumbers[0];
            imei2 = "";
        }
        // Create a Folder for Images
        File file = new File(Environment.getExternalStorageDirectory(), ".Hulcnc_Images");
        if (!file.isDirectory()) {
            file.mkdir();
        }
    }

    public boolean checkNetIsAvailable() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            // we are connected to a network
            connected = true;
        }

        return connected;
    }
    void checkAppPermission(String permission, int requestCode) {

        boolean permission_flag = false;
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(LoginActivty.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivty.this,
                    permission)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                showOnPermissiondenied(Manifest.permission.CAMERA, MY_PERMISSIONS_REQUEST_CAMERA, 1);
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(LoginActivty.this,
                        new String[]{permission},
                        requestCode);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            if (requestCode == MY_PERMISSIONS_REQUEST_CAMERA) {
                checkAppPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, MY_PERMISSIONS_REQUEST_STORAGE_WRITE);
            } else if (requestCode == MY_PERMISSIONS_REQUEST_STORAGE_WRITE) {
                checkAppPermission(Manifest.permission.READ_EXTERNAL_STORAGE, MY_PERMISSIONS_REQUEST_STORAGE_READ);
            } else if (requestCode == MY_PERMISSIONS_REQUEST_STORAGE_READ) {
                checkAppPermission(Manifest.permission.ACCESS_FINE_LOCATION, MY_PERMISSIONS_REQUEST_LOCATION);
            } else {

                // Create a Folder for Images

                File file = new File(Environment.getExternalStorageDirectory(), ".GSK_MT_ORANGE_IMAGES");
                if (!file.isDirectory()) {
                    file.mkdir();
                }
                File file_planogram = new File(Environment.getExternalStorageDirectory(), "GSK_MT_ORANGE_Planogram_Images");
                if (!file_planogram.isDirectory()) {
                    file_planogram.mkdir();
                }

               /* if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getApplicationContext(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(getApplicationContext(),
                                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }*/

             /*   if (checkPlayServices()) {
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
                }*/
                //attemptLogin();
            }

        }
    }    void showOnPermissiondenied(final String permissionsRequired, final int request_code, final int check) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivty.this);
        builder.setTitle("Need Multiple Permissions");
        builder.setMessage("This app needs Camera, Storage and Location permissions.");
        builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                if (check == 0) {
                    checkAppPermission(permissionsRequired, request_code);
                } else {
                    ActivityCompat.requestPermissions(LoginActivty.this,
                            new String[]{permissionsRequired},
                            request_code);
                }

            }
        });
       /* builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
            }
        });*/
        builder.show();
    }


    private boolean checkAndRequestPermissions() {

        int permissionwrite_storage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int CAMERA = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int ACCESS_NETWORK_STATE = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE);
        int ACCESS_COARSE_LOCATION = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int READ_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int READ_PHONE_STATE = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (permissionwrite_storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (CAMERA != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }

        if (ACCESS_NETWORK_STATE != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_NETWORK_STATE);
        }
        if (ACCESS_COARSE_LOCATION != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (READ_EXTERNAL_STORAGE != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (READ_PHONE_STATE != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), PERMISSION_ALL);
            return false;
        }
        return true;
    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(this).setMessage(message).setPositiveButton("OK", okListener).setNegativeButton("Cancel", okListener).create().show();
    }


}
