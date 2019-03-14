package intelre.cpm.com.hulcnc.dailyentry;

import android.app.Activity;
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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cpm.com.hulcnc.R;
import intelre.cpm.com.hulcnc.Database.HUL_CNC_DB;
import intelre.cpm.com.hulcnc.constant.CommonString;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import intelre.cpm.com.hulcnc.gettersetter.SearchStoreDataGetterSetter;
import intelre.cpm.com.hulcnc.gsonGetterSetter.Result;
import intelre.cpm.com.hulcnc.retrofit.PostApi;
import intelre.cpm.com.hulcnc.constant.AlertandMessages;
import intelre.cpm.com.hulcnc.gsonGetterSetter.JourneyPlanSearch;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CustomerInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private View view;
    private LinearLayout search_lin;
    private CardView search_card;
    private TextView customer_name, card_no, customer_category, last_purchase_date, last_purchase_details;
    private EditText phone_number, card_number;
    private Button search_phone_number, search_card_number;
    private RecyclerView drawer_layout_recycle_store;
    FloatingActionButton fab, fab_sav;
    HUL_CNC_DB db;
    private Toolbar toolbar;
    private SharedPreferences preferences = null;
    String userId, visit_date,store_cd;
    Context context;
    ProgressDialog loading;
    private Retrofit adapter;
    boolean uploadflag = false;
    ArrayList<Result> storelist = new ArrayList<Result>();
    private String phone_num = "", card_num = "";
    private SharedPreferences.Editor editor = null;
    ValueAdapter adapter1;
    SearchStoreDataGetterSetter searchStoreDataGetterSetter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_info);
        declaration();

    }

    private void declaration() {

        view = (View) findViewById(R.id.view);
        search_lin = (LinearLayout) findViewById(R.id.search_lin);
        search_card = (CardView) findViewById(R.id.search_card);
        customer_name = (TextView) findViewById(R.id.customer_name);
        card_no = (TextView) findViewById(R.id.card_no);
        customer_category = (TextView) findViewById(R.id.customer_category);
        last_purchase_date = (TextView) findViewById(R.id.last_purchase_date);
        last_purchase_details = (TextView) findViewById(R.id.last_purchase_details);
        phone_number = (EditText) findViewById(R.id.phone_number);
        card_number = (EditText) findViewById(R.id.card_number);
        search_phone_number = (Button) findViewById(R.id.search_phone_number);
        search_card_number = (Button) findViewById(R.id.search_card_number);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab_sav = (FloatingActionButton) findViewById(R.id.fab_sav);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawer_layout_recycle_store = (RecyclerView) findViewById(R.id.drawer_layout_recycle_store);
        context = this;
        db = new HUL_CNC_DB(context);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        userId = preferences.getString(CommonString.KEY_USERNAME, null);
        visit_date = preferences.getString(CommonString.KEY_DATE, "");
        store_cd = preferences.getString(CommonString.KEY_STORE_CD, null);
        getSupportActionBar().setTitle("Customer Info - " + visit_date);
        search_phone_number.setOnClickListener(this);
        search_card_number.setOnClickListener(this);
        fab.setOnClickListener(this);
        fab_sav.setOnClickListener(this);
        searchStoreDataGetterSetter=new SearchStoreDataGetterSetter();

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.search_phone_number:
                if (checkNetIsAvailable()) {
                    if (validation_phonenumber()) {
                        uploadSearchList();
                    }
                } else {
                    Snackbar.make(fab, "No internet connection !", Snackbar.LENGTH_SHORT).show();
                }
                break;
            case R.id.search_card_number:

                if (checkNetIsAvailable()) {
                    if (validation_cardnumber()) {
                        uploadSearchList();
                    }
                } else {
                    Snackbar.make(fab, "No internet connection !", Snackbar.LENGTH_SHORT).show();
                }
                break;
            case R.id.fab:
                Intent in = new Intent(CustomerInfoActivity.this, AddCustomerInfoActivity.class);
                startActivity(in);
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                CustomerInfoActivity.this.finish();
                break;

            case R.id.fab_sav:

               saveSearchData();

                break;
        }
    }

    public boolean validation_phonenumber() {
        boolean value = true;
        phone_num = phone_number.getText().toString();
        if (phone_num.length() != 10) {
            value = false;
            showMessage("Please fill 10 digit mobile Number.");
        } else {

            value = true;
        }
        return value;
    }

    public boolean validation_cardnumber() {

        boolean value = true;
        card_num = card_number.getText().toString();
        if (card_num.isEmpty()) {
            value = false;
            showMessage("Please fill Card Number.");
        } else {

            value = true;
        }
        return value;
    }

    public void showMessage(String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(CustomerInfoActivity.this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                       // CustomerInfoActivity.this.finish();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();


    }

    private void uploadSearchList() {
        try {
            loading = ProgressDialog.show(CustomerInfoActivity.this, "Processing", "Please wait...", false, false);
            uploadflag = false;

            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .build();
            JSONObject obj = new JSONObject();
            obj = new JSONObject();
            obj.put("CardNo", card_num);
            obj.put("MobileNo", phone_num);
            obj.put("Username", userId);
            String jsonString = obj.toString();

            RequestBody jsonData = RequestBody.create(MediaType.parse("application/json"), jsonString);
            adapter = new Retrofit.Builder().baseUrl(CommonString.URL).client(okHttpClient).
                    addConverterFactory(GsonConverterFactory.create()).build();
            PostApi api = adapter.create(PostApi.class);

            Call<String> call = api.getSearchcustomer(jsonData);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String responseBody = response.body().toString();
                    String data = null;
                    if (responseBody != null && response.isSuccessful()) {
                        try {
                            storelist.clear();
                            if (!responseBody.contains("No Data")) {
                                JSONObject jsnobject = new JSONObject(responseBody);
                                JSONArray jsonArray = jsnobject.getJSONArray("Result");
                                //   storelist.clear();
                                if (jsonArray.length() == 1 && jsonArray.getJSONObject(0).has("Status") && jsonArray.getJSONObject(0).get("Status").toString().equalsIgnoreCase("No Data Found")) {
                                    AlertandMessages.showAlertlogin(CustomerInfoActivity.this, CommonString.MESSAGE_NO_SEARCH);
                                    fab.setVisibility(View.VISIBLE);
                                    fab_sav.setVisibility(View.INVISIBLE);
                                    search_lin.setVisibility(View.INVISIBLE);
                                    search_card.setVisibility(View.INVISIBLE);
                                    view.setVisibility(View.INVISIBLE);
                                } else {

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        Result jps = new Result();
                                        JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                        jps.setCustomerId(Integer.parseInt(jsonObject2.getString("Customer_Id")));
                                        jps.setCustomerName((jsonObject2.getString("Customer_Name")));
                                        jps.setRetailerStoreName((jsonObject2.getString("Retailer_Store_Name")));
                                        jps.setCardNo((jsonObject2.getString("Card_No")));
                                        jps.setMobile((jsonObject2.getString("Mobile")));
                                        jps.setLastPurchaseDate((jsonObject2.getString("Last_Purchase_Date")));
                                        jps.setLastPurchaseDetail(jsonObject2.getString("Last_Purchase_Detail"));
                                        jps.setSkuId(Integer.parseInt(jsonObject2.getString("Sku_Id")));
                                        jps.setQty(Integer.parseInt(jsonObject2.getString("Qty")));
                                        jps.setAmount(Integer.parseInt(jsonObject2.getString("Amount")));

                                        storelist.add(jps);

                                    }

                                    fab_sav.setVisibility(View.VISIBLE);
                                    fab_sav.setImageDrawable(ContextCompat.getDrawable(CustomerInfoActivity.this, R.drawable.right_arrow));
                                    fab.setVisibility(View.INVISIBLE);
                                    search_lin.setVisibility(View.VISIBLE);
                                    search_card.setVisibility(View.VISIBLE);
                                    view.setVisibility(View.VISIBLE);

                                    customer_name.setText(storelist.get(0).getCustomerName());
                                    card_no.setText(storelist.get(0).getCardNo());
                                    customer_category.setText(String.valueOf(storelist.get(0).getCustomerId()));
                                    last_purchase_date.setText(String.valueOf(storelist.get(0).getLastPurchaseDate()));

                                    adapter1 = new ValueAdapter(getApplicationContext(), storelist);
                                    drawer_layout_recycle_store.setAdapter(adapter1);
                                    drawer_layout_recycle_store.setLayoutManager(new LinearLayoutManager(context));
                                    loading.dismiss();
                                }

                            } else {
                                throw new java.lang.Exception();
                            }
                         } catch (Exception e) {
                            e.printStackTrace();
                            AlertandMessages.showAlertlogin(CustomerInfoActivity.this, CommonString.MESSAGE_NO_SEARCH);
                            fab.setVisibility(View.VISIBLE);
                            fab_sav.setVisibility(View.GONE);
                            search_lin.setVisibility(View.INVISIBLE);
                            search_card.setVisibility(View.INVISIBLE);
                            view.setVisibility(View.INVISIBLE);
                            loading.dismiss();

                        }
                    } else {
                        loading.dismiss();
                        AlertandMessages.showAlertlogin(CustomerInfoActivity.this, "Server Not Responding. Please Try Again");

                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    if (t instanceof SocketTimeoutException || t instanceof IOException || t instanceof SocketException) {
                        AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_INTERNET_NOT_AVALABLE
                                + "(" + t.getMessage().toString() + ")", true);
                        loading.dismiss();
                    } else {
                        AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_INTERNET_NOT_AVALABLE, true);
                        loading.dismiss();
                    }

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            AlertandMessages.showAlertlogin(CustomerInfoActivity.this, e.getLocalizedMessage().toString());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            AlertDialog.Builder builder = new AlertDialog.Builder(CustomerInfoActivity.this);
            builder.setMessage(CommonString.ONBACK_ALERT_MESSAGE)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                            CustomerInfoActivity.this.finish();
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


    public class ValueAdapter extends RecyclerView.Adapter<ValueAdapter.MyViewHolder> {

        private LayoutInflater inflator;
        List<Result> data = Collections.emptyList();

        public ValueAdapter(Context context, List<Result> data) {
            inflator = LayoutInflater.from(context);
            this.data = data;
        }

        @Override
        public ValueAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            View view = inflator.inflate(R.layout.search_list, parent, false);
            ValueAdapter.MyViewHolder holder = new ValueAdapter.MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final ValueAdapter.MyViewHolder viewHolder, final int position) {
            final Result current = data.get(position);
             viewHolder.sku.setText(current.getLastPurchaseDetail());
            viewHolder.qty.setText(String.valueOf(current.getQty()));
            viewHolder.amount.setText(String.valueOf(current.getAmount()));

        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView sku, qty, amount;

            public MyViewHolder(View itemView) {
                super(itemView);
                sku = (TextView) itemView.findViewById(R.id.sku);
                qty = (TextView) itemView.findViewById(R.id.qty);
                amount = (TextView) itemView.findViewById(R.id.amount);
            }
        }
    }

    private boolean checkNetIsAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public  void  saveSearchData(){

        searchStoreDataGetterSetter.setMobile(storelist.get(0).getMobile());
        searchStoreDataGetterSetter.setCard_no(storelist.get(0).getCardNo());
        searchStoreDataGetterSetter.setCustomerName(storelist.get(0).getCustomerName());
        searchStoreDataGetterSetter.setVisit_date(visit_date);
        db.open();
       long key = db.insertStoreData(searchStoreDataGetterSetter,store_cd, "No");

       if (key>0){

           Intent in1 = new Intent(CustomerInfoActivity.this, SelectCategoryActivity.class);
           editor.putString(CommonString.KEY_CUSTOMER_NAME, storelist.get(0).getCustomerName());
           editor.putString(CommonString.KEY_CARD_NO, storelist.get(0).getCardNo());
           editor.putString(CommonString.KEY_CUSTOMER_ID, String.valueOf(storelist.get(0).getCustomerId()));
           editor.putString(CommonString.KEY_PHONENO, String.valueOf(storelist.get(0).getMobile()));
           editor.commit();

           in1.putExtra(CommonString.KEY_ADD_STORE_OBJECT, searchStoreDataGetterSetter);
           in1.putExtra(CommonString.KEY_ID, key + "");

           startActivity(in1);
           overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
           CustomerInfoActivity.this.finish();
       }


    }

}
