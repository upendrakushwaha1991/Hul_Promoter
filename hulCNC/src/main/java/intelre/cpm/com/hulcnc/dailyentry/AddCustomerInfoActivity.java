package intelre.cpm.com.hulcnc.dailyentry;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;

import cpm.com.hulcnc.R;
import intelre.cpm.com.hulcnc.Database.HUL_CNC_DB;
import intelre.cpm.com.hulcnc.constant.AlertandMessages;
import intelre.cpm.com.hulcnc.constant.CommonString;
import intelre.cpm.com.hulcnc.delegates.AddCustomerInfo;
import intelre.cpm.com.hulcnc.retrofit.PostApi;
import retrofit2.Retrofit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import java.util.concurrent.TimeUnit;

import retrofit2.converter.gson.GsonConverterFactory;

public class AddCustomerInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText customer_number, store_name, card_number, mobile_number;
    private String customer_num_, store_, card_num_, mobile_number_;
    private Button add_retailer_info;
    HUL_CNC_DB db;
    private Toolbar toolbar;
    private SharedPreferences preferences = null;
    String userId, visit_date, store_cd;
    Context context;
    ProgressDialog loading;
    private Retrofit adapter;
    AddCustomerInfo addCustomerInfo;
    AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer_info);
        declaration();

    }

    private void declaration() {
        customer_number = (EditText) findViewById(R.id.customer_number);
        store_name = (EditText) findViewById(R.id.store_name);
        card_number = (EditText) findViewById(R.id.card_number);
        mobile_number = (EditText) findViewById(R.id.mobile_number);
        add_retailer_info = (Button) findViewById(R.id.add_retailer_info);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = this;
        db = new HUL_CNC_DB(context);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = preferences.getString(CommonString.KEY_USERNAME, null);
        visit_date = preferences.getString(CommonString.KEY_DATE, "");
        store_cd = preferences.getString(CommonString.KEY_STORE_CD, null);
        getSupportActionBar().setTitle("Add Customer Info - " + visit_date);
        add_retailer_info.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.add_retailer_info:
                if (validation()) {
                    if (checkNetIsAvailable()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                AddCustomerInfoActivity.this);
                        builder.setMessage("Do you want to save the data ")
                                .setCancelable(false)
                                .setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {

                                                try {
                                                    alert.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                                                    addCustomerInfo = new AddCustomerInfo();
                                                    addCustomerInfo.setStore_id(userId);
                                                    addCustomerInfo.setVisitDate(visit_date);
                                                    addCustomerInfo.setCustomerNmae(customer_num_);
                                                    addCustomerInfo.setRetailerStoreNmae(store_);
                                                    addCustomerInfo.setCardNumber(card_num_);
                                                    addCustomerInfo.setMobileNumber(mobile_number_);

                                                    JSONObject jsonObject = new JSONObject();
                                                    jsonObject.put("CustomerName", addCustomerInfo.getCustomerNmae());
                                                    jsonObject.put("RStoreName", addCustomerInfo.getRetailerStoreNmae());
                                                    jsonObject.put("CardNo", addCustomerInfo.getCardNumber());
                                                    jsonObject.put("MobileNo", addCustomerInfo.getMobileNumber());
                                                    jsonObject.put("UserId", userId);

                                                    uploadAddCustomerDATA(jsonObject.toString());
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog,
                                            int id) {
                                        dialog.cancel();
                                    }
                                });
                        alert = builder.create();
                        alert.show();
                    } else {
                        Snackbar.make(add_retailer_info, "No Network Available", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                    }
                }
                break;


        }

    }

    public boolean validation() {
        boolean value = true;
        customer_num_ = customer_number.getText().toString().replaceAll("[(!@#$%^&*?%')<>\"]", " ").trim();
        store_ = store_name.getText().toString().replaceAll("[(!@#$%^&*?%')<>\"]", " ").trim();
        card_num_ = card_number.getText().toString().replaceAll("[(!@#$%^&*?%')<>\"]", " ").trim();
        mobile_number_ = mobile_number.getText().toString().replaceAll("[(!@#$%^&*?%')<>\"]", " ").trim();
        if (customer_num_.isEmpty()) {
            value = false;
            showMessage("Please fill Cutomer Name");
        } else if (store_.isEmpty()) {
            value = false;
            showMessage("Please fill Store Name");
        } else if (card_num_.isEmpty()) {
            value = false;
            showMessage("Please fill Card Number");
        } else if (mobile_number_.length() != 10) {
            value = false;
            showMessage("Please fill 10 digit mobile number");
        } else {
            value = true;
        }
        return value;
    }

    public void showMessage(String message) {

      //  Snackbar.make(customer_number, message, Snackbar.LENGTH_SHORT).show();
        Toast.makeText(AddCustomerInfoActivity.this,message,Toast.LENGTH_LONG).show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            AlertDialog.Builder builder = new AlertDialog.Builder(AddCustomerInfoActivity.this);
            builder.setMessage(CommonString.ONBACK_ALERT_MESSAGE)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                            AddCustomerInfoActivity.this.finish();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void uploadAddCustomerDATA(String jsondata) {
        try {
            loading = ProgressDialog.show(AddCustomerInfoActivity.this, "Processing", "Please wait...",
                    false, false);
            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .build();

            RequestBody jsonData = RequestBody.create(MediaType.parse("application/json"), jsondata.toString());
            Retrofit adapter = new Retrofit.Builder().baseUrl(CommonString.URL).client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            PostApi api = adapter.create(PostApi.class);
            retrofit2.Call<ResponseBody> call = api.getAddCustomer(jsonData);
            call.enqueue(new retrofit2.Callback<ResponseBody>() {
                @Override
                public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    ResponseBody responseBody = response.body();
                    String data = null;
                    if (responseBody != null && response.isSuccessful()) {
                        try {
                            data = response.body().string();
                            if (data != CommonString.KEY_SUCCESS) {
                                //  if (!data.equals("0")) {
                                Intent intent = new Intent(AddCustomerInfoActivity.this, CustomerInfoActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                                AddCustomerInfoActivity.this.finish();
                                loading.dismiss();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            AlertandMessages.showAlertlogin(AddCustomerInfoActivity.this, CommonString.MESSAGE_INTERNET_NOT_AVALABLE);
                            loading.dismiss();
                        }
                    } else {

                        loading.dismiss();
                        AlertandMessages.showAlertlogin(AddCustomerInfoActivity.this, "Server Not Responding. Please Try Again");

                    }

                }

                @Override
                public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                    loading.dismiss();
                    if (t instanceof SocketTimeoutException || t instanceof IOException || t instanceof Exception) {
                        AlertandMessages.showAlertlogin(AddCustomerInfoActivity.this, t.getMessage().toString());
                    } else {
                        AlertandMessages.showAlertlogin(AddCustomerInfoActivity.this, "Please check internet connection");
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            AlertandMessages.showAlertlogin(AddCustomerInfoActivity.this, e.getLocalizedMessage().toString());
            loading.dismiss();

        }

    }

    public boolean checkNetIsAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }


}
