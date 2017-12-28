package intelre.cpm.com.intelre;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import intelre.cpm.com.intelre.Get_IMEI_number.ImeiNumberClass;
import intelre.cpm.com.intelre.autoupdate.AutoUpdateActivity;
import intelre.cpm.com.intelre.constant.AlertandMessages;
import intelre.cpm.com.intelre.constant.CommonString;
import intelre.cpm.com.intelre.gettersetter.LoginGsonGetterSetter;
import intelre.cpm.com.intelre.gpsenable.LocationEnableCommon;
import intelre.cpm.com.intelre.retrofit.PostApi;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IntelLoginActivty extends AppCompatActivity {
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
            loading = ProgressDialog.show(IntelLoginActivty.this, "Processing", "Please wait...", false, false);
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

            if (!imei1.equals("") && !imei2.equals("")) {
                jsonObject.put("IMEINumber1", imei1);
                jsonObject.put("IMEINumber2", imei2);
            } else if (!imei1.equals("") || imei2.equals("")) {
                jsonObject.put("IMEINumber1", imei1);
                jsonObject.put("IMEINumber2", "0");
            } else {
                jsonObject.put("IMEINumber1", "0");
                jsonObject.put("IMEINumber2", "0");
            }

            String jsonString = jsonObject.toString();
            try {
                final String[] data_global = {""};
                RequestBody jsonData = RequestBody.create(MediaType.parse("application/json"), jsonString);
                adapter = new Retrofit.Builder().baseUrl(CommonString.URL)
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
                                if (data.equalsIgnoreCase(CommonString.MESSAGE_SOCKETEXCEPTION)) {
                                    loading.dismiss();
                                    AlertandMessages.showAlertlogin(IntelLoginActivty.this,
                                            "Check Your Internet Connection");

                                } else if (data.equalsIgnoreCase(CommonString.KEY_FAILURE)) {
                                    AlertandMessages.showAlertlogin(IntelLoginActivty.this,
                                            CommonString.KEY_FAILURE + " Please try again");
                                    loading.dismiss();
                                } else {
                                    Gson gson = new Gson();
                                    LoginGsonGetterSetter userObject = gson.fromJson(data, LoginGsonGetterSetter.class);
                                    // PUT IN PREFERENCES
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
                                    //date is changed for previous day data
                                    //editor.putString(CommonString.KEY_DATE, "11/22/2017");
                                    editor.commit();

                                    //Download Todays Questions
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("Username", userid);
                                    jsonObject.put("Downloadtype", "Today_Question");
                                    String jsonString = jsonObject.toString();
                                    final String[] question_data_global = {""};
                                    RequestBody questionjsonData = RequestBody.create(MediaType.parse("application/json"),
                                            jsonString);
                                    adapter = new Retrofit.Builder().baseUrl(CommonString.URL)
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
                                                    data = data.substring(1, data.length() - 1).
                                                            replace("\\", "");

                                                    if (data.equalsIgnoreCase(CommonString.MESSAGE_SOCKETEXCEPTION)) {
                                                        loading.dismiss();
                                                        AlertandMessages.showAlertlogin(IntelLoginActivty.this,
                                                                "Check Your Internet Connection");
                                                    } else if (data.equalsIgnoreCase(CommonString.KEY_FAILURE)) {
                                                        AlertandMessages.showAlertlogin(IntelLoginActivty.this,
                                                                CommonString.KEY_FAILURE + " Please try again");
                                                        loading.dismiss();
                                                    } else {
                                                        Gson gs = new Gson();
                                                        final LoginGsonGetterSetter userques = gs.fromJson(data, LoginGsonGetterSetter.class);
                                                        userques.getTodayQuestion().size();
                                                        if (preferences.getString(CommonString.KEY_VERSION, "").equals(Integer.toString(versionCode))) {
                                                            loading.dismiss();
                                                            final String visit_date = preferences.getString(CommonString.KEY_DATE, "");
                                                            if (userques.getTodayQuestion().size() > 0 && userques.getTodayQuestion().get(0).getStatus().equals("N") &&
                                                                    !preferences.getBoolean(CommonString.KEY_IS_QUIZ_DONE + visit_date, false)) {
                                                                for (int i = 0; i < userques.getTodayQuestion().size(); i++) {
                                                                    if (userques.getTodayQuestion().get(i).getRightAnswer().toString().equalsIgnoreCase("true")) {
                                                                        right_answer = userques.getTodayQuestion().get(i).getAnswer();
                                                                        rigth_answer_cd = userques.getTodayQuestion().get(i).getAnswerId().toString();
                                                                        break;
                                                                    }
                                                                }
                                                                final AnswerData answerData = new AnswerData();
                                                                final Dialog customD = new Dialog(IntelLoginActivty.this);
                                                                customD.setTitle("Todays Question");
                                                                customD.setCancelable(false);
                                                                customD.setContentView(R.layout.show_answer_layout);
                                                                customD.setContentView(R.layout.todays_question_layout);
                                                                ((TextView) customD.findViewById(R.id.tv_qns)).setText(userques.getTodayQuestion().get(0).getQuestion());
                                                                Button btnsubmit = (Button) customD.findViewById(R.id.btnsubmit);
                                                                final TextView txt_timer = (TextView) customD.findViewById(R.id.txt_timer);
                                                                RadioGroup radioGroup = (RadioGroup) customD.findViewById(R.id.radiogrp);
                                                                new CountDownTimer(30000, 1000) {
                                                                    public void onTick(long millisUntilFinished) {
                                                                        txt_timer.setText("seconds remaining: " + millisUntilFinished / 1000);
                                                                        //here you can have your logic to set text to edittext
                                                                    }

                                                                    public void onFinish() {
                                                                        if (answerData.getAnswer_id() == null || answerData.getAnswer_id().equals("")) {
                                                                            txt_timer.setText("done!");
                                                                            customD.cancel();
                                                                            String ansisright = "";
                                                                            ansisright = "Your Time is over";
                                                                            final Dialog ans_dialog = new Dialog(IntelLoginActivty.this);
                                                                            ans_dialog.setTitle("Answer");
                                                                            ans_dialog.setCancelable(false);
                                                                            //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                                                            ans_dialog.setContentView(R.layout.show_answer_layout);
                                                                            ((TextView) ans_dialog.findViewById(R.id.tv_ans)).setText(ansisright);
                                                                            Button btnok = (Button) ans_dialog.findViewById(R.id.btnsubmit);
                                                                            btnok.setOnClickListener(new View.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(View v) {
                                                                                    answerData.setQuestion_id(userques.getTodayQuestion().get(0).getQuestionId().toString());
                                                                                    answerData.setUsername(userid);
                                                                                    answerData.setVisit_date(visit_date);
                                                                                    if ((checkNetIsAvailable())) {
                                                                                        ans_dialog.cancel();
                                                                                        try {
                                                                                            JSONArray answerDetaills = new JSONArray();
                                                                                            JSONObject object = new JSONObject();

                                                                                            //region Deviation_journeyplan Data
                                                                                            object.put("ANSWER_ID", "0");
                                                                                            object.put("QUESTION_ID", answerData.getQuestion_id());
                                                                                            object.put("VISIT_DATE", answerData.getVisit_date());
                                                                                            object.put("USER_NAME", answerData.getUsername());
                                                                                            answerDetaills.put(object);

                                                                                            object = new JSONObject();
                                                                                            object.put("MID", "0");
                                                                                            object.put("Keys", "TODAY_ANSWER");
                                                                                            object.put("JsonData", answerDetaills.toString());
                                                                                            object.put("UserId", userid);

                                                                                            String jsonString = object.toString();
                                                                                            if (jsonString != null && !jsonString.equalsIgnoreCase("")) {

                                                                                                loading.setMessage("Uploading answer data..");
                                                                                                RequestBody jsonData = RequestBody.create(MediaType.parse("application/json"), jsonString);
                                                                                                adapter = new Retrofit.Builder().baseUrl(CommonString.URL).
                                                                                                        addConverterFactory(GsonConverterFactory.create()).build();
                                                                                                PostApi api = adapter.create(PostApi.class);
                                                                                                Call<ResponseBody> call = api.getUploadJsonDetail(jsonData);
                                                                                                call.enqueue(new Callback<ResponseBody>() {
                                                                                                    @Override
                                                                                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                                                                        ResponseBody responseBody = response.body();
                                                                                                        String data = null;
                                                                                                        if (responseBody != null && response.isSuccessful()) {
                                                                                                            try {
                                                                                                                data = response.body().string();
                                                                                                                if (data.equalsIgnoreCase("")) {
                                                                                                                } else {
                                                                                                                    data = data.substring(1, data.length() - 1).replace("\\", "");
                                                                                                                    data_global[0] = data;
                                                                                                                    if (data.contains("Success")) {
                                                                                                                        String visit_date = preferences.getString(CommonString.KEY_DATE, null);
                                                                                                                        editor = preferences.edit();
                                                                                                                        editor.putBoolean(CommonString.KEY_IS_QUIZ_DONE + visit_date, true);
                                                                                                                        editor.commit();
                                                                                                                        Intent intent = new Intent(getBaseContext(), MainMenuActivity.class);
                                                                                                                        startActivity(intent);
                                                                                                                        finish();
                                                                                                                    } else {
                                                                                                                        editor = preferences.edit();
                                                                                                                        editor.putString(CommonString.KEY_QUESTION_CD +
                                                                                                                                visit_date, qns_cd);
                                                                                                                        editor.putString(CommonString.KEY_ANSWER_CD +
                                                                                                                                visit_date, ans_cd);
                                                                                                                        editor.commit();
                                                                                                                        Intent intent = new Intent(getBaseContext(), MainMenuActivity.class);
                                                                                                                        startActivity(intent);
                                                                                                                        finish();
                                                                                                                    }
                                                                                                                }

                                                                                                            } catch (Exception e) {
                                                                                                                loading.dismiss();
                                                                                                                AlertandMessages.showAlertlogin(IntelLoginActivty.this,
                                                                                                                        e.getLocalizedMessage().toString());
                                                                                                            }
                                                                                                        } else {
                                                                                                            loading.dismiss();
                                                                                                            AlertandMessages.showAlertlogin(IntelLoginActivty.this, "Check Your Internet Connection");

                                                                                                        }
                                                                                                    }

                                                                                                    @Override
                                                                                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                                                                                        if (t instanceof SocketTimeoutException) {
                                                                                                            AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_INTERNET_NOT_AVALABLE, true);
                                                                                                        } else if (t instanceof IOException) {
                                                                                                            AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_INTERNET_NOT_AVALABLE, true);
                                                                                                        } else if (t instanceof SocketException) {
                                                                                                            AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_INTERNET_NOT_AVALABLE, true);
                                                                                                        } else {
                                                                                                            AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_INTERNET_NOT_AVALABLE, true);
                                                                                                        }

                                                                                                    }
                                                                                                });

                                                                                            }
                                                                                            ans_dialog.cancel();
                                                                                        } catch (JSONException e) {
                                                                                            e.printStackTrace();
                                                                                        }
                                                                                    } else {
                                                                                        showToast("No internet connection");
                                                                                    }
                                                                                }
                                                                            });
                                                                            ans_dialog.show();
                                                                        }
                                                                    }
                                                                }.start();

                                                                for (int i = 0; i < userques.getTodayQuestion().size(); i++) {
                                                                    RadioButton rdbtn = new RadioButton(IntelLoginActivty.this);
                                                                    rdbtn.setId(i);
                                                                    rdbtn.setText(userques.getTodayQuestion().get(i).getAnswer());
                                                                    radioGroup.addView(rdbtn);
                                                                }

                                                                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                                                    @Override
                                                                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                                                                        answerData.setAnswer_id(userques.getTodayQuestion().get(checkedId).getAnswerId().toString());
                                                                        answerData.setRight_answer(userques.getTodayQuestion().get(checkedId).getRightAnswer().toString());
                                                                    }
                                                                });

                                                                btnsubmit.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View v) {
                                                                        if (answerData.getAnswer_id() == null || answerData.getAnswer_id().equals("")) {
                                                                            Snackbar.make(museridSignInButton, "First select an answer", Snackbar.LENGTH_SHORT).show();
                                                                        } else {
                                                                            customD.cancel();
                                                                            String ansisright = "";
                                                                            if (answerData.getRight_answer().equalsIgnoreCase("true")) {
                                                                                ansisright = "Your Answer Is Right!";
                                                                            } else {
                                                                                ansisright = "Your Answer is Wrong! Right Answer Is :- " + right_answer;
                                                                            }
                                                                            final Dialog ans_dialog = new Dialog(IntelLoginActivty.this);
                                                                            ans_dialog.setTitle("Answer");
                                                                            ans_dialog.setCancelable(false);
                                                                            ans_dialog.setContentView(R.layout.show_answer_layout);
                                                                            ((TextView) ans_dialog.findViewById(R.id.tv_ans)).setText(ansisright);
                                                                            Button btnok = (Button) ans_dialog.findViewById(R.id.btnsubmit);
                                                                            btnok.setOnClickListener(new View.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(View v) {
                                                                                    answerData.setQuestion_id(userques.getTodayQuestion().get(0).getQuestionId().toString());
                                                                                    answerData.setUsername(userid);
                                                                                    answerData.setVisit_date(visit_date);
                                                                                    if (checkNetIsAvailable()) {
                                                                                        try {
                                                                                            JSONArray answerDetaills = new JSONArray();
                                                                                            JSONObject object = new JSONObject();

                                                                                            //region Deviation_journeyplan Data
                                                                                            object.put("ANSWER_ID", answerData.getAnswer_id());
                                                                                            object.put("QUESTION_ID", answerData.getQuestion_id());
                                                                                            object.put("VISIT_DATE", answerData.getVisit_date());
                                                                                            object.put("USER_NAME", answerData.getUsername());
                                                                                            answerDetaills.put(object);

                                                                                            object = new JSONObject();
                                                                                            object.put("MID", "0");
                                                                                            object.put("Keys", "TODAY_ANSWER");
                                                                                            object.put("JsonData", answerDetaills.toString());
                                                                                            object.put("UserId", userid);

                                                                                            String jsonString = object.toString();
                                                                                            if (jsonString != null && !jsonString.equalsIgnoreCase("")) {

                                                                                                loading.setMessage("Uploading answer data..");
                                                                                                RequestBody jsonData = RequestBody.create(MediaType.parse("application/json"), jsonString);
                                                                                                adapter = new Retrofit.Builder().baseUrl(CommonString.URL).
                                                                                                        addConverterFactory(GsonConverterFactory.create()).build();
                                                                                                PostApi api = adapter.create(PostApi.class);
                                                                                                Call<ResponseBody> call = api.getUploadJsonDetail(jsonData);
                                                                                                call.enqueue(new Callback<ResponseBody>() {
                                                                                                    @Override
                                                                                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                                                                        ResponseBody responseBody = response.body();
                                                                                                        String data = null;
                                                                                                        if (responseBody != null && response.isSuccessful()) {
                                                                                                            try {
                                                                                                                data = response.body().string();
                                                                                                                // if (data.equalsIgnoreCase("")) {
                                                                                                                // data = data.substring(1, data.length() - 1).replace("\\", "");
                                                                                                                //  data_global[0] = data;
                                                                                                                if (data.contains("Success")) {
                                                                                                                    loading.dismiss();
                                                                                                                    String visit_date = preferences.getString(CommonString.KEY_DATE, null);
                                                                                                                    editor = preferences.edit();
                                                                                                                    editor.putBoolean(CommonString.KEY_IS_QUIZ_DONE + visit_date, true);
                                                                                                                    editor.commit();
                                                                                                                    Intent intent = new Intent(getBaseContext(), MainMenuActivity.class);
                                                                                                                    startActivity(intent);
                                                                                                                    finish();
                                                                                                                } else {
                                                                                                                    loading.dismiss();
                                                                                                                    editor = preferences.edit();
                                                                                                                    editor.putString(CommonString.KEY_QUESTION_CD + visit_date, qns_cd);
                                                                                                                    editor.putString(CommonString.KEY_ANSWER_CD + visit_date, ans_cd);
                                                                                                                    editor.commit();
                                                                                                                    Intent intent = new Intent(getBaseContext(), MainMenuActivity.class);
                                                                                                                    startActivity(intent);
                                                                                                                    finish();
                                                                                                                }


                                                                                                            } catch (Exception e) {
                                                                                                                loading.dismiss();
                                                                                                                AlertandMessages.showAlertlogin(IntelLoginActivty.this,
                                                                                                                        e.getLocalizedMessage().toString());
                                                                                                            }
                                                                                                        } /*else {
                                                                                                            loading.dismiss();
                                                                                                            AlertandMessages.showAlertlogin(IntelLoginActivty.this, "Check Your Internet Connection");

                                                                                                        }*/
                                                                                                    }

                                                                                                    @Override
                                                                                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                                                                        loading.dismiss();
                                                                                                        if (t instanceof SocketTimeoutException) {
                                                                                                            AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_INTERNET_NOT_AVALABLE, true);
                                                                                                        } else if (t instanceof IOException) {
                                                                                                            AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_INTERNET_NOT_AVALABLE, true);
                                                                                                        } else if (t instanceof SocketException) {
                                                                                                            AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_INTERNET_NOT_AVALABLE, true);
                                                                                                        } else {
                                                                                                            AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_INTERNET_NOT_AVALABLE, true);
                                                                                                        }

                                                                                                    }
                                                                                                });

                                                                                            }
                                                                                            ans_dialog.cancel();
                                                                                        } catch (JSONException e) {
                                                                                            loading.dismiss();
                                                                                            e.printStackTrace();
                                                                                        }
                                                                                    } else {
                                                                                        showToast("No internet connection");
                                                                                    }
                                                                                }
                                                                            });
                                                                            ans_dialog.show();
                                                                        }
                                                                    }
                                                                });
                                                                customD.show();
                                                            } else {
                                                                Intent intent = new Intent(getBaseContext(), MainMenuActivity.class);
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                        } else {
                                                            Intent intent = new Intent(getBaseContext(), AutoUpdateActivity.class);
                                                            intent.putExtra(CommonString.KEY_PATH, preferences.getString(CommonString.KEY_PATH, ""));
                                                            startActivity(intent);
                                                            finish();
                                                        }


                                                    }
                                                } catch (Exception e) {
                                                    loading.dismiss();
                                                    AlertandMessages.showAlertlogin(IntelLoginActivty.this, e.getLocalizedMessage().toString());
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                            loading.dismiss();
                                            if (t instanceof SocketTimeoutException || t instanceof IOException || t instanceof Exception) {
                                                AlertandMessages.showAlertlogin(IntelLoginActivty.this, t.getLocalizedMessage().toString());
                                            }else {
                                                AlertandMessages.showAlertlogin(IntelLoginActivty.this, t.getLocalizedMessage().toString());

                                            }
                                        }
                                    });
                                }

                            } catch (Exception e) {
                                loading.dismiss();
                                e.printStackTrace();
                                AlertandMessages.showAlertlogin(IntelLoginActivty.this, e.getLocalizedMessage().toString());

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        loading.dismiss();
                        if (t instanceof SocketTimeoutException || t instanceof IOException || t instanceof Exception) {
                            AlertandMessages.showAlertlogin(IntelLoginActivty.this,
                                    CommonString.MESSAGE_INTERNET_NOT_AVALABLE);
                        } else {
                            AlertandMessages.showAlertlogin(IntelLoginActivty.this, t.getLocalizedMessage().toString());

                        }
                    }
                });

            } catch (Exception e) {
                loading.dismiss();
                e.printStackTrace();
                AlertandMessages.showAlertlogin(IntelLoginActivty.this, e.getLocalizedMessage().toString());
            }

        } catch (PackageManager.NameNotFoundException e) {
            loading.dismiss();
            AlertandMessages.showAlertlogin(IntelLoginActivty.this, e.getLocalizedMessage().toString());

        } catch (JSONException e) {
            loading.dismiss();
            AlertandMessages.showAlertlogin(IntelLoginActivty.this, e.getLocalizedMessage().toString());
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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_PHONE_STATE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            imeiNumbers = imei.getDeviceImei();
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
        tv_version = (TextView) findViewById(R.id.tv_version_code);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        museridView = (AutoCompleteTextView) findViewById(R.id.userid);
        mPasswordView = (EditText) findViewById(R.id.password);
        museridView.setText("test");
        mPasswordView.setText("gsk@123");
        museridSignInButton = (Button) findViewById(R.id.user_login_button);
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
        tv_version.setText("Version - " + app_ver);
        imei = new ImeiNumberClass(context);
        locationEnableCommon = new LocationEnableCommon();
        locationEnableCommon.checkgpsEnableDevice(this);
        imei = new ImeiNumberClass(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE},
                    PERMISSIONS_REQUEST_READ_PHONE_STATE);
        } else {
            imeiNumbers = imei.getDeviceImei();
            if (imeiNumbers.length == 2) {
                imei1 = imeiNumbers[0];
                imei2 = imeiNumbers[1];
            } else {
                imei1 = imeiNumbers[0];
                imei2 = "";
            }

        }
        // Create a Folder for Images
        File file = new File(Environment.getExternalStorageDirectory(),
                "IntelRE_Images");
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


}
