package intelre.cpm.com.hulcnc.upload;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;
import com.squareup.okhttp.MultipartBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import intelre.cpm.com.hulcnc.Database.HUL_CNC_DB;
import cpm.com.hulcnc.R;
import intelre.cpm.com.hulcnc.constant.AlertandMessages;
import intelre.cpm.com.hulcnc.constant.CommonString;
import intelre.cpm.com.hulcnc.delegates.CoverageBean;
import intelre.cpm.com.hulcnc.gettersetter.GeotaggingBeans;
import intelre.cpm.com.hulcnc.gettersetter.NoSaleGetterSetter;
import intelre.cpm.com.hulcnc.gettersetter.QuizGetterSetter;
import intelre.cpm.com.hulcnc.gettersetter.SalesEntryGetterSetter;
import intelre.cpm.com.hulcnc.gettersetter.SearchSalesGetterSetter;
import intelre.cpm.com.hulcnc.gettersetter.SearchStoreDataGetterSetter;
import intelre.cpm.com.hulcnc.gettersetter.StockAvailabilityGetterSetter;
import intelre.cpm.com.hulcnc.gettersetter.StoreProfileGetterSetter;
import intelre.cpm.com.hulcnc.gettersetter.TrainingGetterSetter;
import intelre.cpm.com.hulcnc.gsonGetterSetter.AuditQuestion;
import intelre.cpm.com.hulcnc.gsonGetterSetter.ImageNMResponseGetterSetter;
import intelre.cpm.com.hulcnc.gsonGetterSetter.InfoTypeMaster;
import intelre.cpm.com.hulcnc.gsonGetterSetter.JourneyPlan;
import intelre.cpm.com.hulcnc.gsonGetterSetter.MappingPermanentPosm;
import intelre.cpm.com.hulcnc.gsonGetterSetter.PosmMaster;
import intelre.cpm.com.hulcnc.gsonGetterSetter.SkuMaster;
import intelre.cpm.com.hulcnc.gsonGetterSetter.StoreCategoryMaster;
import intelre.cpm.com.hulcnc.retrofit.PostApi;
import intelre.cpm.com.hulcnc.retrofit.PostApiForUpload;
import intelre.cpm.com.hulcnc.retrofit.StringConverterFactory;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PreviousDataUploadActivity extends AppCompatActivity {
    HUL_CNC_DB db;
    Toolbar toolbar;
    String mid = "0";
    com.squareup.okhttp.RequestBody body1;
    private Retrofit adapter;
    int status = 0, statusforimage = 0;
    Context context;
    private SharedPreferences preferences;
    String userId, visit_date, app_version;
    private ProgressDialog pb;
    ArrayList<CoverageBean> coverageList = new ArrayList<>();
    ArrayList<JourneyPlan> specific_uploadStatus;
    StoreProfileGetterSetter storePGT;
    ProgressDialog loading;
    public static ArrayList<ImageNMResponseGetterSetter> imageresponse = new ArrayList<>();
    public static int uploadedFiles = 0;
    public static int totalFiles = 0;
    int count = 0;
    boolean isvalid;
    SearchSalesGetterSetter searchData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        userId = preferences.getString(CommonString.KEY_USERNAME, null);
        app_version = preferences.getString(CommonString.KEY_VERSION, null);
        context = this;
        db = new HUL_CNC_DB(this);
        db.open();
        isDataValid();
        uploadedFiles = 0;
    }

    void isDataValid() {
        boolean flag_invalid = false;
        String Store_cd = "", previous_date = "";
        JourneyPlan jcp = null;
        ArrayList<CoverageBean> coverage_list = db.getcoverageDataPrevious(visit_date);
        for (int i = 0; i < coverage_list.size(); i++) {
            jcp = db.getSpecificStoreDataPrevious(visit_date, coverage_list.get(i).getStoreId());
            if (jcp.getUploadStatus().equalsIgnoreCase(CommonString.KEY_CHECK_IN)) {
                Store_cd = jcp.getStoreId().toString();
                previous_date = jcp.getVisitDate();
                if (chekDataforCheckout(Store_cd, jcp.getVisitDate())) {
                    flag_invalid = true;
                    break;
                } else {
                    db.open();
                    db.updateJaurneyPlanSpecificStoreStatus(Store_cd, jcp.getVisitDate(), CommonString.KEY_N);
                    db.deleteSpecificStoreData(Store_cd);
                    flag_invalid = false;
                    break;
                }

            }
        }

        if (flag_invalid) {
            db.open();
            ArrayList<CoverageBean> specificDATa = db.getSpecificCoverageData(previous_date, Store_cd);
            if (specificDATa.size() > 0) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("UserId", userId);
                    jsonObject.put("StoreId", specificDATa.get(0).getStoreId());
                    jsonObject.put("Latitude", specificDATa.get(0).getLatitude());
                    jsonObject.put("Longitude", specificDATa.get(0).getLongitude());
                    jsonObject.put("Checkout_Date", visit_date);
                    uploadCoverageIntimeDATA(jsonObject.toString(), specificDATa);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                //start upload
                coverageList = db.getcoverageDataPrevious(visit_date);
                if (coverageList.size() > 0) {
                    pb = new ProgressDialog(context);
                    pb.setCancelable(false);
                    pb.setMessage("Uploading Data");
                    pb.show();
                    uploadDataUsingCoverageRecursive(coverageList, 0);
                }
            }

        }else {
            //start upload
            coverageList = db.getcoverageDataPrevious(visit_date);
            if (coverageList.size() > 0) {
                pb = new ProgressDialog(context);
                pb.setCancelable(false);
                pb.setMessage("Uploading Data");
                pb.show();
                uploadDataUsingCoverageRecursive(coverageList, 0);
            } else {
             //   pb.dismiss();
                AlertandMessages.showAlert((Activity) context, "All data and images upload Successfully.", true);
            }
        }

    }

    public void uploadCoverageIntimeDATA(String jsondata, final ArrayList<CoverageBean> specific_CData) {
        try {
            loading = ProgressDialog.show(PreviousDataUploadActivity.this, "Processing", "Please wait...",
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
            retrofit2.Call<ResponseBody> call = api.getCheckout(jsonData);
            call.enqueue(new retrofit2.Callback<ResponseBody>() {
                @Override
                public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    ResponseBody responseBody = response.body();
                    String data = null;
                    if (responseBody != null && response.isSuccessful()) {
                        try {
                            data = response.body().string();
                            if (!data.equals("0")) {
                                db.open();
                                db.updateCoverageCheckoutIMG(specific_CData.get(0).getStoreId(), specific_CData.get(0).getVisitDate(), "");
                                db.updateJaurneyPlanSpecificStoreStatus(specific_CData.get(0).getStoreId(),
                                        specific_CData.get(0).getVisitDate(), CommonString.KEY_C);
                                loading.dismiss();
                                coverageList = db.getcoverageDataPrevious(visit_date);
                                if (coverageList.size() > 0) {
                                    pb = new ProgressDialog(context);
                                    pb.setCancelable(false);
                                    pb.setMessage("Uploading Data");
                                    pb.show();
                                    uploadDataUsingCoverageRecursive(coverageList, 0);
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            loading.dismiss();
                            AlertandMessages.showAlert(PreviousDataUploadActivity.this, "Check internet conection", true);
                        }
                    }

                }

                @Override
                public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                    loading.dismiss();
                    if (t instanceof SocketTimeoutException || t instanceof IOException || t instanceof Exception) {
                        AlertandMessages.showAlert(PreviousDataUploadActivity.this, "Check internet conection", true);
                    } else {
                        AlertandMessages.showAlert(PreviousDataUploadActivity.this, "Check internet conection", true);

                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            loading.dismiss();
            AlertandMessages.showAlert(PreviousDataUploadActivity.this, "Check internet conection", true);
        }
    }


    //upload previous data
    public void uploadDataUsingCoverageRecursive(ArrayList<CoverageBean> coverageList, int coverageIndex) {
        try {
            ArrayList<String> keyList = new ArrayList<>();
            keyList.clear();
            String store_id = coverageList.get(coverageIndex).getStoreId();
            db.open();
            specific_uploadStatus = db.getSpecificStoreData(store_id);
            String status = null;
            status = specific_uploadStatus.get(0).getUploadStatus();
            pb.setMessage("Uploading store " + (coverageIndex + 1) + "/" + coverageList.size());
            if (!status.equalsIgnoreCase(CommonString.KEY_D)) {
                keyList.add("CoverageDetail_latest");
                keyList.add("STOCK_AVAILABILITY_DATA");
                keyList.add("SALES_STOCK_DATA");
                keyList.add("NO_SALES_DATA");
                keyList.add("GeoTag");
            }

            if (keyList.size() > 0) {
                uploadDataWithoutWait(keyList, 0, coverageList, coverageIndex);
            } else {
                if (++coverageIndex != coverageList.size()) {
                    uploadDataUsingCoverageRecursive(coverageList, coverageIndex);
                } else {

                    pb.setMessage("updoading images");
                    File dir = new File(CommonString.FILE_PATH);
                    if (getFileNames(dir.listFiles()).size() > 0) {
                        totalFiles = getFileNames(dir.listFiles()).size();

                        uploadImage(coverageList.get(0).getVisitDate());
                    } else {
                        pb.setMessage("Updating status");
                        updatestatusforu(coverageList, 0, coverageList.get(0).getVisitDate(), CommonString.KEY_U);
                    }
                }
            }
            //endregion

        } catch (Exception e) {
            e.printStackTrace();
            pb.dismiss();
            AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_SOCKETEXCEPTION, true);
        }
    }

    public void uploadDataWithoutWait(final ArrayList<String> keyList,
                                      final int keyIndex, final ArrayList<CoverageBean> coverageList,
                                      final int coverageIndex) {
        try {
            status = 0;
            final String[] data_global = {""};
            String jsonString = "";
            int type = 0;
            JSONObject jsonObject;
            //region Creating json data
            switch (keyList.get(keyIndex)) {
                case "CoverageDetail_latest":
                    JourneyPlan journeyPlan;
                    //region Coverage Data
                    jsonObject = new JSONObject();
                    jsonObject.put("StoreId", coverageList.get(coverageIndex).getStoreId());
                    jsonObject.put("VisitDate", coverageList.get(coverageIndex).getVisitDate());
                    jsonObject.put("Latitude", coverageList.get(coverageIndex).getLatitude());
                    jsonObject.put("Longitude", coverageList.get(coverageIndex).getLongitude());
                    jsonObject.put("ReasonId", coverageList.get(coverageIndex).getReasonid());
                    jsonObject.put("SubReasonId", "0");
                    jsonObject.put("Remark", "");
                    jsonObject.put("ImageName", coverageList.get(coverageIndex).getImage());
                    jsonObject.put("AppVersion", app_version);
                    jsonObject.put("UploadStatus", CommonString.KEY_P);
                    jsonObject.put("Checkout_Image", coverageList.get(coverageIndex).getCkeckout_image());
                    jsonObject.put("UserId", userId);
                    jsonString = jsonObject.toString();
                    type = CommonString.COVERAGE_DETAIL;
                    //endregion
                    break;

                case "STOCK_AVAILABILITY_DATA":
                    db.open();
                    ArrayList<StockAvailabilityGetterSetter> auditList = db.getStockData(coverageList.get(coverageIndex).getStoreId());
                    if (auditList.size() > 0) {
                        JSONArray promoArray = new JSONArray();
                        for (int j = 0; j < auditList.size(); j++) {
                            JSONObject obj = new JSONObject();

                            obj.put("MID", coverageList.get(coverageIndex).getMID());
                            obj.put("UserId", userId);
                            obj.put("SKU_CD", auditList.get(j).getSku_id());
                            obj.put("PRESENT", auditList.get(j).getCurrectanswerCd());
                            obj.put("CATEGORY_CD", auditList.get(j).getCategory_id());

                            promoArray.put(obj);
                        }
                        jsonObject = new JSONObject();
                        jsonObject.put("MID", coverageList.get(coverageIndex).getMID());
                        jsonObject.put("Keys", "STOCK_AVAILABILITY_DATA");
                        jsonObject.put("JsonData", promoArray.toString());
                        jsonObject.put("UserId", userId);

                        jsonString = jsonObject.toString();
                        type = CommonString.UPLOADJsonDetail;
                    }
                    break;

                case "SALES_STOCK_DATA":
                    db.open();
                    ArrayList<SalesEntryGetterSetter> salesEntry = db.getSalesStockUploadData_(coverageList.get(coverageIndex).getStoreId());
                    if (salesEntry.size() > 0) {
                        JSONArray promoArray = new JSONArray();

                        for (int j = 0; j < salesEntry.size(); j++) {
                            JSONObject obj = new JSONObject();
                            obj.put("MID", coverageList.get(coverageIndex).getMID());
                            obj.put("UserId", userId);
                            obj.put("SKU_CD", salesEntry.get(j).getSku_id());
                            obj.put("SALE", salesEntry.get(j).getStock());
                            obj.put("CATEGORY_CD", salesEntry.get(j).getCategory_id());

                            promoArray.put(obj);
                        }

                        jsonObject = new JSONObject();
                        jsonObject.put("MID", coverageList.get(coverageIndex).getMID());
                        jsonObject.put("Keys", "NEW_SALES_STOCK_DATA");
                        jsonObject.put("JsonData", promoArray.toString());
                        jsonObject.put("UserId", userId);

                        jsonString = jsonObject.toString();
                        type = CommonString.UPLOADJsonDetail;
                    }

                    break;

              /*  case "NO_SALES_DATA":
                    db.open();
                    ArrayList<NoSaleGetterSetter> nosale = db.getNoSaleUploadData(coverageList.get(coverageIndex).getStoreId());
                    if (nosale.size() > 0) {
                        JSONArray promoArray = new JSONArray();
                        for (int j = 0; j < nosale.size(); j++) {
                            JSONObject obj = new JSONObject();
                            obj.put("MID", coverageList.get(coverageIndex).getMID());
                            obj.put("UserId", userId);
                            obj.put("SALES", nosale.get(j).getNo_sale());

                            promoArray.put(obj);
                        }
                        jsonObject = new JSONObject();
                        jsonObject.put("MID", coverageList.get(coverageIndex).getMID());
                        jsonObject.put("Keys", "NO_SALES_DATA");
                        jsonObject.put("JsonData", promoArray.toString());
                        jsonObject.put("UserId", userId);

                        jsonString = jsonObject.toString();
                        type = CommonString.UPLOADJsonDetail;
                    }

                    break;
*/
                case "QUIZ_DATA":
                    db.open();
                    ArrayList<QuizGetterSetter> quiz_data = db.getQuizUploadData(coverageList.get(coverageIndex).getStoreId());
                    if (quiz_data.size() > 0) {
                        JSONArray promoArray = new JSONArray();
                        for (int j = 0; j < quiz_data.size(); j++) {
                            JSONObject obj = new JSONObject();
                            obj.put("MID", coverageList.get(coverageIndex).getMID());
                            obj.put("UserId", userId);
                            obj.put("QUESTION_ID", quiz_data.get(j).getQuestion_id());
                            obj.put("ANSWER_CD", quiz_data.get(j).getCurrectanswerCd());
                            obj.put("RIGHT_ANSWER", quiz_data.get(j).getRight_Answer());
                            obj.put("BRAND_ID", quiz_data.get(j).getBrand_id());
                            obj.put("VisitDate", coverageList.get(coverageIndex).getVisitDate());
                            promoArray.put(obj);
                        }
                        jsonObject = new JSONObject();
                        jsonObject.put("MID", coverageList.get(coverageIndex).getMID());
                        jsonObject.put("Keys", "QUIZ_DATA");
                        jsonObject.put("JsonData", promoArray.toString());
                        jsonObject.put("UserId", userId);

                        jsonString = jsonObject.toString();
                        type = CommonString.UPLOADJsonDetail;
                    }

                    break;

                case "GeoTag":
                    ArrayList<GeotaggingBeans> geotaglist = db.getinsertGeotaggingData(coverageList.get(coverageIndex).
                            getStoreId(), "N");
                    if (geotaglist.size() > 0) {
                        JSONArray topUpArray = new JSONArray();
                        for (int j = 0; j < geotaglist.size(); j++) {
                            JSONObject obj = new JSONObject();
                            obj.put(CommonString.KEY_STORE_ID, geotaglist.get(j).getStoreid());
                            obj.put(CommonString.KEY_VISIT_DATE, visit_date);
                            obj.put(CommonString.KEY_LATITUDE, geotaglist.get(j).getLatitude());
                            obj.put(CommonString.KEY_LONGITUDE, geotaglist.get(j).getLongitude());
                            obj.put("FRONT_IMAGE", geotaglist.get(j).getImage());
                            topUpArray.put(obj);
                        }

                        jsonObject = new JSONObject();
                        jsonObject.put("MID", coverageList.get(coverageIndex).getMID());
                        jsonObject.put("Keys", "GeoTag");
                        jsonObject.put("JsonData", topUpArray.toString());
                        jsonObject.put("UserId", userId);

                        jsonString = jsonObject.toString();
                        type = CommonString.UPLOADJsonDetail;
                    }
                    break;

            }
            //endregion

            final int[] finalJsonIndex = {keyIndex};
            final String finalKeyName = keyList.get(keyIndex);
            if (jsonString != null && !jsonString.equalsIgnoreCase("")) {
                pb.setMessage("Uploading (" + keyIndex + "/" + keyList.size() + ") \n" + keyList.get(keyIndex) + "\n Store uploading " +
                        (coverageIndex + 1) + "/" + coverageList.size());

                final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .readTimeout(20, TimeUnit.SECONDS)
                        .writeTimeout(20, TimeUnit.SECONDS)
                        .connectTimeout(20, TimeUnit.SECONDS)
                        .build();

                RequestBody jsonData = RequestBody.create(MediaType.parse("application/json"), jsonString);
                adapter = new Retrofit.Builder().baseUrl(CommonString.URL).client(okHttpClient)
                        .addConverterFactory(GsonConverterFactory.create()).build();
                PostApi api = adapter.create(PostApi.class);
                Call<ResponseBody> call = null;
                if (type == CommonString.COVERAGE_DETAIL) {
                    call = api.getCoverageDetail(jsonData);
                } else if (type == CommonString.UPLOADJsonDetail) {
                    call = api.getUploadJsonDetail(jsonData);
                }
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        ResponseBody responseBody = response.body();
                        String data = null;
                        if (responseBody != null && response.isSuccessful()) {
                            try {
                                data = response.body().string();
                                if (data.equalsIgnoreCase("")) {
                                    pb.dismiss();
                                    data_global[0] = "";
                                    AlertandMessages.showAlert((Activity) context, "Invalid Data :" +
                                            " problem occured at " + keyList.get(keyIndex), true);
                                } else {
                                    data = data.substring(1, data.length() - 1).replace("\\", "");
                                    data_global[0] = data;
                                    if (finalKeyName.equalsIgnoreCase("CoverageDetail_latest")) {
                                        try {
                                            coverageList.get(coverageIndex).setMID(Integer.parseInt(data_global[0]));
                                            specific_uploadStatus.get(0).setUploadStatus(CommonString.KEY_P);
                                            db.updateJaurneyPlanSpecificStoreStatus(coverageList.get(coverageIndex).getStoreId(),
                                                    coverageList.get(coverageIndex).getVisitDate(),
                                                    CommonString.KEY_P);

                                        } catch (NumberFormatException ex) {
                                            pb.dismiss();
                                            AlertandMessages.showAlert((Activity) context, "Error in Uploading Data at " + finalKeyName, true);
                                        }
                                    } else if (data_global[0].contains(CommonString.KEY_SUCCESS)) {
                                        if (finalKeyName.equalsIgnoreCase("GeoTag")) {
                                            // db.updateInsertedGeoTagStatus(coverageList.get(coverageIndex).getStoreId(), CommonString.KEY_Y);
                                            // db.updateStatus(coverageList.get(coverageIndex).getStoreId(), CommonString.KEY_Y);
                                            // db.updateGeoTagStatusForPjpDevaition(coverageList.get(coverageIndex).getStoreId(), CommonString.KEY_Y);
                                        }
                                    } else {
                                        pb.dismiss();
                                        AlertandMessages.showAlert((Activity) context, "Error in Uploading Data at " + finalKeyName + " : " + data_global[0], true);
                                    }
                                    finalJsonIndex[0]++;
                                    if (finalJsonIndex[0] != keyList.size()) {
                                        uploadDataWithoutWait(keyList, finalJsonIndex[0], coverageList, coverageIndex);
                                    } else {
                                        pb.setMessage("updating status :" + coverageIndex);
                                        //uploading status D for current store from coverageList
                                        specific_uploadStatus.get(0).setUploadStatus(CommonString.KEY_D);
                                        updateStatus(coverageList, coverageIndex, CommonString.KEY_D);
                                    }
                                }

                            } catch (Exception e) {
                                pb.dismiss();
                                AlertandMessages.showAlert((Activity) context, "Error in Uploading Data at " + finalKeyName, true);
                            }
                        } else {
                            pb.dismiss();
                            AlertandMessages.showAlert((Activity) context, "Error in Uploading Data at " + finalKeyName, true);

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        pb.dismiss();
                        AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_INTERNET_NOT_AVALABLE, true);
                    }
                });

            } else {
                finalJsonIndex[0]++;
                if (finalJsonIndex[0] != keyList.size()) {
                    uploadDataWithoutWait(keyList, finalJsonIndex[0], coverageList, coverageIndex);
                } else {
                    pb.setMessage("updating status :" + coverageIndex);
                    //uploading status D for current store from coverageList
                    specific_uploadStatus.get(0).setUploadStatus(CommonString.KEY_D);
                    updateStatus(coverageList, coverageIndex, CommonString.KEY_D);

                }
            }
        } catch (Exception ex) {
            pb.dismiss();
            AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_INTERNET_NOT_AVALABLE + " (" + ex.toString() + " )", true);
        }
    }

    void updateStatus(final ArrayList<CoverageBean> coverageList, final int coverageIndex, final String status) {
        if (coverageList.get(coverageIndex) != null) {
            try {
                final int[] tempcoverageIndex = {coverageIndex};

                final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .readTimeout(20, TimeUnit.SECONDS)
                        .writeTimeout(20, TimeUnit.SECONDS)
                        .connectTimeout(20, TimeUnit.SECONDS)
                        .build();

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("StoreId", coverageList.get(coverageIndex).getStoreId());
                jsonObject.put("VisitDate", coverageList.get(coverageIndex).getVisitDate());
                jsonObject.put("UserId", userId);
                jsonObject.put("Status", status);
                RequestBody jsonData = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
                adapter = new Retrofit.Builder().baseUrl(CommonString.URL).client(okHttpClient)
                        .addConverterFactory(GsonConverterFactory.create()).build();
                PostApi api = adapter.create(PostApi.class);
                Call<ResponseBody> call = api.getCoverageStatusDetail(jsonData);
                pb.setMessage("Uploading store status " + (coverageIndex + 1) + "/" + coverageList.size());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        ResponseBody responseBody = response.body();
                        String data = null;
                        if (responseBody != null && response.isSuccessful()) {
                            try {
                                data = response.body().string();
                                if (data.equalsIgnoreCase("")) {
                                    pb.dismiss();
                                    AlertandMessages.showAlert((Activity) context, "Error in Uploading status at coverage :" + coverageIndex, true);
                                } else {
                                    data = data.substring(1, data.length() - 1).replace("\\", "");
                                    if (data.contains("1")) {
                                        db.open();
                                        db.updateJaurneyPlanSpecificStoreStatus(coverageList.get(coverageIndex).getStoreId(),
                                                coverageList.get(coverageIndex).getVisitDate(), status);
                                        specific_uploadStatus.get(0).setUploadStatus(status);
                                        tempcoverageIndex[0]++;
                                        if (tempcoverageIndex[0] != coverageList.size()) {
                                            uploadDataUsingCoverageRecursive(coverageList, tempcoverageIndex[0]);
                                        } else {
                                            //  uploadimages();
                                            pb.setMessage("uploading images");
                                            File dir = new File(CommonString.FILE_PATH);
                                            if (getFileNames(dir.listFiles()).size() > 0) {
                                                totalFiles = getFileNames(dir.listFiles()).size();
                                                uploadImage(coverageList.get(coverageIndex).getVisitDate());

                                            } else {
                                                db.open();
                                                db.updateJaurneyPlanSpecificStoreStatus(coverageList.get(coverageIndex).getStoreId(), coverageList.get(coverageIndex).getVisitDate(), status);
                                                updateStatus(coverageList, coverageIndex, status);
                                            }
                                        }
                                    } else {
                                        pb.dismiss();
                                        AlertandMessages.showAlert((Activity) context, "Error in Uploading status at coverage :" + coverageIndex, true);
                                    }

                                }
                            } catch (Exception e) {
                                pb.dismiss();
                                AlertandMessages.showAlert((Activity) context, "Error in Uploading status at coverage :" + coverageIndex, true);
                            }
                        } else {
                            pb.dismiss();
                            AlertandMessages.showAlert((Activity) context, "Error in Uploading status at coverage :" + coverageIndex, true);

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        pb.dismiss();
                        AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_INTERNET_NOT_AVALABLE, true);

                    }
                });

            } catch (JSONException ex) {
                pb.dismiss();
                AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_INVALID_JSON + " (" + ex.toString() + " )", true);

            }
        }

    }

    public ArrayList<String> getFileNames(File[] file) {
        ArrayList<String> arrayFiles = new ArrayList<String>();
        if (file.length > 0) {
            for (int i = 0; i < file.length; i++)
                arrayFiles.add(file[i].getName());
        }
        return arrayFiles;
    }


    private void updatestatusforu(final ArrayList<CoverageBean> coverageList, int index, final String visit_date, final String status) {
        try {
            db.open();
            final int[] indexlocal = {index};
            final boolean[] status_u = {false};
            final ArrayList<JourneyPlan> store_data = db.getSpecificStoreData(coverageList.get(index).getStoreId().toString());
            if (store_data.size() > 0) {
                if (store_data.get(indexlocal[0]).getUploadStatus().equalsIgnoreCase(CommonString.KEY_D)) {
                    index++;
                    final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .readTimeout(20, TimeUnit.SECONDS)
                            .writeTimeout(20, TimeUnit.SECONDS)
                            .connectTimeout(20, TimeUnit.SECONDS)
                            .build();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("StoreId", store_data.get(indexlocal[0]).getStoreId());
                    jsonObject.put("VisitDate", visit_date);
                    jsonObject.put("UserId", userId);
                    jsonObject.put("Status", status);
                    RequestBody jsonData = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
                    adapter = new Retrofit.Builder().baseUrl(CommonString.URL).client(okHttpClient)
                            .addConverterFactory(GsonConverterFactory.create()).build();
                    PostApi api = adapter.create(PostApi.class);
                    Call<ResponseBody> call = api.getCoverageStatusDetail(jsonData);
                    pb.setMessage("Uploading store status " + (index) + "/" + coverageList.size());
                    final int finalIndex = index;
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            ResponseBody responseBody = response.body();
                            String data = null;
                            if (responseBody != null && response.isSuccessful()) {
                                try {
                                    data = response.body().string();
                                    if (data.equalsIgnoreCase("")) {
                                        pb.dismiss();
                                        status_u[0] = false;
                                        AlertandMessages.showAlert((Activity) context, "Error in Uploading status at coverage :" + finalIndex, true);
                                    } else {
                                        data = data.substring(1, data.length() - 1).replace("\\", "");
                                        if (data.contains("1")) {
                                            status_u[0] = true;
                                            db.open();
                                            db.updateJaurneyPlanSpecificStoreStatus(store_data.get(indexlocal[0]).getStoreId().toString(),
                                                    store_data.get(indexlocal[0]).getVisitDate(), status);
                                            db.deleteSpecificStoreData(store_data.get(indexlocal[0]).getStoreId().toString());
                                            indexlocal[0]++;
                                            if (indexlocal[0] != coverageList.size()) {
                                                updatestatusforu(coverageList, indexlocal[0], coverageList.get(0).getVisitDate(), CommonString.KEY_U);
                                            } else {
                                                if (status_u[0] == true) {
                                                    pb.dismiss();
                                                    AlertandMessages.showAlert((Activity) context, "All data and images upload Successfully.", true);
                                                }
                                            }
                                        } else {
                                            status_u[0] = false;
                                            pb.dismiss();
                                            AlertandMessages.showAlert((Activity) context, "Error in Uploading status at coverage :" + finalIndex, true);
                                        }

                                    }
                                } catch (Exception e) {
                                    status_u[0] = false;
                                    pb.dismiss();
                                   // AlertandMessages.showAlert((Activity) context, "Error in Uploading status at coverage :" + finalIndex, true);
                                    AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_INTERNET_NOT_AVALABLE, true);
                                }
                            } else {
                                status_u[0] = false;
                                pb.dismiss();
                                AlertandMessages.showAlert((Activity) context, "Error in Uploading status at coverage :" + finalIndex, true);
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            status_u[0] = false;
                            pb.dismiss();
                            AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_INTERNET_NOT_AVALABLE, true);


                        }
                    });
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            pb.dismiss();
            AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_INVALID_JSON + " (" + e.toString() + " )", true);

        }

    }

    public static File saveBitmapToFile(File file) {
        File file2 = file;
        try {
            int inWidth = 0;
            int inHeight = 0;
            InputStream in = new FileInputStream(file2);
            // decode image size (decode metadata only, not the whole image)
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, options);
            in.close();
            in = null;
            // save width and height
            inWidth = options.outWidth;
            inHeight = options.outHeight;
            // decode full image pre-resized
            in = new FileInputStream(file2);
            options = new BitmapFactory.Options();
            // calc rought re-size (this is no exact resize)
            options.inSampleSize = Math.max(inWidth / 800, inHeight / 500);
            // decode full image
            Bitmap roughBitmap = BitmapFactory.decodeStream(in, null, options);

            // calc exact destination size
            Matrix m = new Matrix();
            RectF inRect = new RectF(0, 0, roughBitmap.getWidth(), roughBitmap.getHeight());
            RectF outRect = new RectF(0, 0, 800, 500);
            m.setRectToRect(inRect, outRect, Matrix.ScaleToFit.CENTER);
            float[] values = new float[9];
            m.getValues(values);

            // resize bitmap
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(roughBitmap,
                    (int) (roughBitmap.getWidth() * values[0]), (int) (roughBitmap.getHeight() * values[4]), true);
            // save image
            try {
                FileOutputStream out = new FileOutputStream(file2);
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            } catch (Exception e) {
                Log.e("Image", e.toString(), e);
            }
        } catch (IOException e) {
            Log.e("Image", e.toString(), e);
            return file2;
        }
        return file;
    }

    String getParsedDate(String filename) {
        String testfilename = filename;
        testfilename = testfilename.substring(testfilename.indexOf("-") + 1);
        testfilename = testfilename.substring(0, testfilename.indexOf("-"));
        return testfilename;
    }


    public void UploadImageFileJsonList(final Context context, final String coverageDate) {
        try {
            String filename = null, foldername = null;
            int totalfiles = 0;
            String jsonString;
            File f = new File(CommonString.FILE_PATH);
            File file[] = f.listFiles();
            JSONObject list = new JSONObject();
            filename = "";
            totalfiles = f.listFiles().length;
            if (totalfiles == 0) {
                list.put("[ 0 ]", "no files");
            } else {
                for (int i = 0; i < file.length; i++) {
                    list.put("[" + i + "]", file[i].getName());
                }
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("MID", "0");
            jsonObject.put("Keys", "FileList");
            jsonObject.put("JsonData", list.toString());
            jsonObject.put("UserId", userId);
            jsonString = jsonObject.toString();
            status = 0;
            isvalid = false;

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .build();

            RequestBody jsonData = RequestBody.create(MediaType.parse("application/json"), jsonString);
            adapter = new Retrofit.Builder().baseUrl(CommonString.URL).client(okHttpClient).addConverterFactory(GsonConverterFactory.create()).build();
            PostApi api = adapter.create(PostApi.class);
            Call<JSONObject> observable = api.getUploadJsonDetailForFileList(jsonData);
            observable.enqueue(new Callback<JSONObject>() {
                @Override
                public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                    if (response.code() == 200 && response.isSuccessful() && response.message().equalsIgnoreCase("OK")) {
                        uploadImage(coverageDate);
                        status = 1;
                    } else {
                        status = 0;
                    }
                }

                @Override
                public void onFailure(Call<JSONObject> call, Throwable t) {
                    uploadImage(coverageDate);
                    if (t instanceof IOException || t instanceof SocketTimeoutException || t instanceof SocketException) {
                        status = -1;
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void uploadImage(String coverageDate) {
        pb.setMessage("updoading images");
        File f = new File(CommonString.FILE_PATH);
        File file[] = f.listFiles();
        if (file.length > 0) {
            UploadImageRecursive(context, coverageDate);
        } else {
            pb.setMessage("Updating status");
            updatestatusforu(coverageList, 0, coverageDate, CommonString.KEY_U);
        }
    }

    public void UploadImageRecursive(final Context context, final String covergeDate) {
        try {
            statusforimage = 0;
            int totalfiles = 0;
            String filename = null, foldername = null;
            File f = new File(CommonString.FILE_PATH);
            File file[] = f.listFiles();
            count = file.length;
            if (file.length > 0) {
                filename = "";
                totalfiles = f.listFiles().length;
                pb.setMessage("Uploading images" + "(" + uploadedFiles + "/" + totalFiles + ")");
                for (int i = 0; i < file.length; i++) {
                    if (new File(CommonString.FILE_PATH + file[i].getName()).exists()) {

                        if (file[i].getName().contains("_STOREIMG_") || file[i].getName().contains("_NONWORKING_") || file[i].getName().contains("_STOREC_OUTIMG_")) {
                            foldername = "CoverageImages";

                        } else if (file[i].getName().contains("_GeoTag_")) {
                            foldername = "GeoTagImages";
                        }
                        filename = file[i].getName();
                    }
                    break;
                }


                File originalFile = new File(CommonString.FILE_PATH + filename);
                final File finalFile = saveBitmapToFile(originalFile);
                String date;
                if (filename.contains("-")) {
                    date = getParsedDate(filename);
                } else {
                    date = visit_date.replace("/", "");
                }


                com.squareup.okhttp.OkHttpClient okHttpClient = new com.squareup.okhttp.OkHttpClient();
                okHttpClient.setConnectTimeout(20, TimeUnit.SECONDS);
                okHttpClient.setWriteTimeout(20, TimeUnit.SECONDS);
                okHttpClient.setReadTimeout(20, TimeUnit.SECONDS);

                com.squareup.okhttp.RequestBody photo = com.squareup.okhttp.RequestBody.create(com.squareup.okhttp.MediaType.parse("application/octet-stream"), finalFile);
                body1 = new MultipartBuilder()
                        .type(MultipartBuilder.FORM)
                        .addFormDataPart("file", finalFile.getName(), photo)
                        .addFormDataPart("FolderName", foldername)
                        .addFormDataPart("Path", date)
                        .build();

                retrofit.Retrofit adapter = new retrofit.Retrofit.Builder()
                        .baseUrl(CommonString.URLGORIMAG)
                        .client(okHttpClient)
                        .addConverterFactory(new StringConverterFactory())
                        .build();
                PostApiForUpload api = adapter.create(PostApiForUpload.class);
                retrofit.Call<String> call = api.getUploadImageRetrofitOne(body1);

                call.enqueue(new retrofit.Callback<String>() {
                    @Override
                    public void onResponse(retrofit.Response<String> response) {
                        if (response.code() == 200 && response.message().equals("OK") && response.isSuccess() && response.body().contains("Success")) {
                         //   addimgANResponse(finalFile.toString(), response.body().toString());
                            finalFile.delete();
                            statusforimage = 1;
                            uploadedFiles++;
                        } else {
                            statusforimage = 0;
                        }
                        if (statusforimage == 0) {
                            pb.dismiss();
                            if (!((Activity) context).isFinishing()) {
                                AlertandMessages.showAlert((Activity) context, "Image not uploaded." + "\n" + uploadedFiles + " images uploaded out of " + totalFiles, true);
                            }
                        } else {
                            UploadImageRecursive(context, covergeDate);
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        if (t instanceof IOException || t instanceof SocketTimeoutException || t instanceof SocketException) {
                            statusforimage = -1;
                            pb.dismiss();
                            if (!((Activity) context).isFinishing()) {
                                AlertandMessages.showAlert((Activity) context, "Network Error in upload." + "\n" + uploadedFiles + " images uploaded out of " + totalFiles, true);
                            }
                        }
                    }
                });

            } else {
                if (totalFiles == uploadedFiles) {
                    pb.setMessage("Updating Status");

                    specific_uploadStatus.get(0).setUploadStatus(CommonString.KEY_U);
                    updatestatusforu(coverageList, 0, visit_date, CommonString.KEY_U);
                }
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_INVALID_JSON, true);
        } catch (Exception e) {
            e.printStackTrace();
            if (totalFiles == uploadedFiles) {
                AlertandMessages.showAlert((Activity) context, "All images uploaded but status not updated", true);
            } else {
                AlertandMessages.showAlert((Activity) context, CommonString.KEY_FAILURE + " (" + e.toString() + " )", true);
            }
        }

    }

/*
    private ArrayList<ImageNMResponseGetterSetter> addimgANResponse(String imagename, String img_response) {
        ImageNMResponseGetterSetter getterSetter = new ImageNMResponseGetterSetter();
        getterSetter.setImgNAME(imagename);
        getterSetter.setIMGRESPONSE(img_response);
        imageresponse.add(getterSetter);
        return imageresponse;
    }
*/

    private void uploadIMGREsponse(final boolean flag) {
        try {
            if (imageresponse.size() > 0) {
                final String[] data_global = {""};
                JSONArray rspArray = new JSONArray();
                for (int j = 0; j < imageresponse.size(); j++) {
                    JSONObject obj = new JSONObject();
                    obj.put("IMG_NAME", imageresponse.get(j).getImgNAME());
                    obj.put("IMG_RESPONSE", imageresponse.get(j).getIMGRESPONSE());
                    rspArray.put(obj);
                }

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("MID", "0");
                jsonObject.put("Keys", "ALLIMGNAME_RESPONSE");
                jsonObject.put("JsonData", rspArray.toString());
                jsonObject.put("UserId", userId);
                String jsonString = jsonObject.toString();

                final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .readTimeout(20, TimeUnit.SECONDS)
                        .writeTimeout(20, TimeUnit.SECONDS)
                        .connectTimeout(20, TimeUnit.SECONDS)
                        .build();

                RequestBody jsonData = RequestBody.create(MediaType.parse("application/json"), jsonString);
                adapter = new Retrofit.Builder().baseUrl(CommonString.URL).client(okHttpClient)
                        .addConverterFactory(GsonConverterFactory.create()).build();
                PostApi api = adapter.create(PostApi.class);
                Call<ResponseBody> call = null;
                call = api.getUploadJsonDetail(jsonData);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        ResponseBody responseBody = response.body();
                        String data = null;
                        if (responseBody != null && response.isSuccessful()) {
                            try {
                                data = response.body().string();
                                if (data.equals("")) {
                                    data_global[0] = "";
                                } else {
                                    data = data.substring(1, data.length() - 1).replace("\\", "");
                                    data_global[0] = data;
                                    if (data_global[0].contains(CommonString.KEY_SUCCESS)) {
                                        if (flag) {
                                            imageresponse.clear();
                                            specific_uploadStatus.get(0).setUploadStatus(CommonString.KEY_U);
                                            updatestatusforu(coverageList, 0, visit_date, CommonString.KEY_U);
                                        }

                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        if (t instanceof SocketException || t instanceof IOException || t instanceof Exception) {
                            Toast.makeText(getApplicationContext(), "Error in uploading image response", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean chekDataforCheckout(String dieticianId, String visit_date) {
        boolean status = false;
        db.open();
        if (db.isStoreAuditFilled(dieticianId)) {
            status = true;
        }else {
            status = false;
        }
        if (status){
            if (db.isSaleFilled(dieticianId)) {
                status = true;
            }else {
                status = false;
            }
        }

        return status;
    }


    void uploadSaleStockData(final String storeID, final String mId, final ArrayList<SearchStoreDataGetterSetter> list_key_id, int index) {

        try {
            db.open();
            JSONObject jsoSalesData = new JSONObject();
            String jsonString = null;
            ArrayList<SalesEntryGetterSetter> salesEntry = db.getSalesStockUploadData_key_id(storeID, list_key_id.get(index).getKey_id());
            if (salesEntry.size() > 0) {
                JSONArray promoArray = new JSONArray();

                for (int j = 0; j < salesEntry.size(); j++) {
                    JSONObject obj = new JSONObject();
                    obj.put("MID", mId);
                    obj.put("UserId", userId);
                    obj.put("SKU_CD", salesEntry.get(j).getSku_id());
                    obj.put("SALE", salesEntry.get(j).getStock());
                    obj.put("CATEGORY_CD", salesEntry.get(j).getCategory_id());
                    obj.put("CUSTOMER_ID", salesEntry.get(j).getCustomer_id());

                    promoArray.put(obj);
                }


                JSONObject jsonObject = new JSONObject();
                jsonObject.put("MID", mId);
                jsonObject.put("Keys", "SALES_STOCK_DATA");
                jsonObject.put("JsonData", promoArray.toString());
                jsonObject.put("UserId", userId);

                jsonString = jsonObject.toString();
            }

            final int[] finalIndex = {index};
            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .build();

            if (jsonString != null && !jsonString.equalsIgnoreCase("")) {
                pb.setMessage("Uploading Sales Data");
                RequestBody jsonData = RequestBody.create(MediaType.parse("application/json"), jsonString);
                adapter = new Retrofit.Builder().baseUrl(CommonString.URL).client(okHttpClient)
                        .addConverterFactory(GsonConverterFactory.create()).build();
                PostApi api = adapter.create(PostApi.class);
                Call<ResponseBody> call = null;
                call = api.getUploadJsonDetail(jsonData);

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        ResponseBody responseBody = response.body();
                        String data = null;
                        if (responseBody != null && response.isSuccessful()) {
                            try {
                                data = response.body().string();
                                if (data.equalsIgnoreCase("")) {
                                    pb.dismiss();

                                } else {
                                    data = data.substring(1, data.length() - 1).replace("\\", "");
                                    if (data.contains(CommonString.KEY_SUCCESS)) {
                                        finalIndex[0]++;
                                        if (finalIndex[0] != list_key_id.size()) {
                                            uploadSaleStockData(storeID, mId, list_key_id, finalIndex[0]);
                                        } else {

                                        }
                                    } else {
                                        pb.dismiss();
                                        AlertandMessages.showAlert((Activity) context, "Error in Uploading Data at " + " : SALES_STOCK_DATA", true);
                                    }
                                }

                            } catch (Exception e) {
                                pb.dismiss();
                                AlertandMessages.showAlert((Activity) context, "Error in Uploading Data at SALES_STOCK_DATA", true);
                            }
                        } else {
                            pb.dismiss();
                            AlertandMessages.showAlert((Activity) context, "Error in Uploading Data at SALES_STOCK_DATA", true);

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        pb.dismiss();
                        if (t == null) {
                            AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_INTERNET_NOT_AVALABLE, true);
                        } else {
                            AlertandMessages.showAlert((Activity) context, CommonString.MESSAGE_INTERNET_NOT_AVALABLE + "(" + t.getMessage().toString() + ")", true);

                        }
                    }
                });

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }




}
