package intelre.cpm.com.intelre.upload;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import intelre.cpm.com.intelre.Database.INTEL_RE_DB;
import intelre.cpm.com.intelre.MainMenuActivity;
import intelre.cpm.com.intelre.R;
import intelre.cpm.com.intelre.constant.AlertandMessages;
import intelre.cpm.com.intelre.constant.CommonString;
import intelre.cpm.com.intelre.dailyentry.CheckoutActivty;
import intelre.cpm.com.intelre.delegates.CoverageBean;
import intelre.cpm.com.intelre.gettersetter.StoreProfileGetterSetter;
import intelre.cpm.com.intelre.gettersetter.TrainingGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.AuditQuestion;
import intelre.cpm.com.intelre.gsonGetterSetter.InfoTypeMaster;
import intelre.cpm.com.intelre.gsonGetterSetter.JourneyPlan;
import intelre.cpm.com.intelre.gsonGetterSetter.MappingPermanentPosm;
import intelre.cpm.com.intelre.gsonGetterSetter.PosmMaster;
import intelre.cpm.com.intelre.gsonGetterSetter.SkuMaster;
import intelre.cpm.com.intelre.gsonGetterSetter.StoreCategoryMaster;
import intelre.cpm.com.intelre.retrofit.PostApi;
import intelre.cpm.com.intelre.retrofit.StringConverterFactory;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PreviousDataUploadActivity extends AppCompatActivity {
    INTEL_RE_DB db;
    Toolbar toolbar;
    String mid = "0";
    com.squareup.okhttp.RequestBody body1;
    private Retrofit adapter;
    int status = 0;
    Context context;
    private SharedPreferences preferences;
    String userId, visit_date, app_version;
    private ProgressDialog pb;
    ArrayList<CoverageBean> coverageList = new ArrayList<>();
    ArrayList<JourneyPlan> specific_uploadStatus;
    StoreProfileGetterSetter storePGT;
    ProgressDialog loading;

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
        db = new INTEL_RE_DB(this);
        db.open();
        isDataValid();
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
                flag_invalid = true;
                break;
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

        }
    }

    public void uploadCoverageIntimeDATA(String jsondata, final ArrayList<CoverageBean> specific_CData) {
        try {
            loading = ProgressDialog.show(PreviousDataUploadActivity.this, "Processing", "Please wait...",
                    false, false);
            RequestBody jsonData = RequestBody.create(MediaType.parse("application/json"), jsondata.toString());
            Retrofit adapter = new Retrofit.Builder().baseUrl(CommonString.URL)
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
                            AlertandMessages.showAlert(PreviousDataUploadActivity.this,
                                    "Check internet conection", true);
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
                keyList.add("STORE_PROFILE_DATA");
                keyList.add("RSP_DETAILS_DATA");
                keyList.add("TRAINING_DATA");
                keyList.add("STORE_AUDIT_DATA");
                keyList.add("VISIBILITY_SOFT_MERCH_DATA");
                keyList.add("VISIBILITY_SEMIP_MERCH_DATA");
                keyList.add("SHOPER_MKT_IPOS");
                keyList.add("SHOPER_MKT_RXT");
                keyList.add("MARKET_INFO");
            }

            if (keyList.size() > 0) {
                uploadDataWithoutWait(keyList, 0, coverageList, coverageIndex);
            } else {
                if (++coverageIndex != coverageList.size()) {
                    uploadDataUsingCoverageRecursive(coverageList, coverageIndex);
                } else {
                    //  uploadimages();
                    pb.setMessage("updoading images");
                    File dir = new File(CommonString.FILE_PATH);
                    ArrayList<String> list1 = new ArrayList();
                    list1 = getFileNames(dir.listFiles());
                    if (list1.size() > 0) {
                        uploadImageRecursiveWithIndex(0, list1);
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
                case "STORE_PROFILE_DATA":
                    db.open();
                    storePGT = db.getStoreProfileData(coverageList.get(coverageIndex).getStoreId().toString(),
                            coverageList.get(coverageIndex).getVisitDate());
                    if (storePGT.getProfileCity() != null && !storePGT.getProfileCity().equals("")) {
                        JSONArray storeDetail = new JSONArray();
                        jsonObject = new JSONObject();
                        jsonObject.put("MID", coverageList.get(coverageIndex).getMID());
                        jsonObject.put("UserId", userId);
                        jsonObject.put("PROFILE_STORE_NAME", storePGT.getProfileStoreName());
                        jsonObject.put("PROFILE_STORE_ADDRESS_1", storePGT.getProfileAddress1());
                        jsonObject.put("PROFILE_STORE_CITY", storePGT.getProfileCity());
                        jsonObject.put("STORE_PROFILE_OWNER", storePGT.getProfileOwner());
                        jsonObject.put("STORE_PROFILE_CONTACT", storePGT.getProfileContact());
                        jsonObject.put("DOB", storePGT.getProfileDOB());
                        jsonObject.put("DOA", storePGT.getProfileDOA());
                        jsonObject.put("VISIBILITY_LOCATION1", storePGT.getProfileVisibilityLocation1());
                        jsonObject.put("VISIBILITY_LOCATION2", storePGT.getProfileVisibilityLocation2());
                        jsonObject.put("VISIBILITY_LOCATION3", storePGT.getProfileVisibilityLocation3());
                        jsonObject.put("DIMENTION1", storePGT.getProfileDimension1());
                        jsonObject.put("DIMENTION2", storePGT.getProfileDimension2());
                        jsonObject.put("DIMENTION3", storePGT.getProfileDimension3());
                        storeDetail.put(jsonObject);
                        jsonObject = new JSONObject();
                        jsonObject.put("MID", coverageList.get(coverageIndex).getMID());
                        jsonObject.put("Keys", "STORE_PROFILE_DATA");
                        jsonObject.put("JsonData", storeDetail.toString());
                        jsonObject.put("UserId", userId);
                        jsonString = jsonObject.toString();
                        type = CommonString.UPLOADJsonDetail;

                    }
                    //endregion
                    break;
                case "RSP_DETAILS_DATA":
                    //region primary bay data
                    db.open();
                    ArrayList<StoreCategoryMaster> rsp_detailsList = db.getRspDetailinsertData(coverageList.get(coverageIndex).getStoreId().toString());
                    if (rsp_detailsList.size() > 0) {
                        JSONArray rspArray = new JSONArray();
                        for (int j = 0; j < rsp_detailsList.size(); j++) {
                            boolean irep_status = rsp_detailsList.get(j).getIREPStatus();
                            String value = "";
                            if (irep_status) {
                                value = "1";
                            } else {
                                value = "0";
                            }
                            JSONObject obj = new JSONObject();
                            obj.put("MID", coverageList.get(coverageIndex).getMID());
                            obj.put("UserId", userId);
                            obj.put("RSP_NAME", rsp_detailsList.get(j).getRspName());
                            obj.put("RSP_ID", rsp_detailsList.get(j).getRspId().toString());
                            obj.put("EMAIL", rsp_detailsList.get(j).getEmail());
                            obj.put("MOBILE", rsp_detailsList.get(j).getMobile());
                            obj.put("FLAG", rsp_detailsList.get(j).getFlag());
                            obj.put("STORE_CD", rsp_detailsList.get(j).getStoreId().toString());
                            obj.put("IREP_STATUS", value);
                            obj.put("BRAND_ID", rsp_detailsList.get(j).getBrandId().toString());
                            rspArray.put(obj);
                        }
                        jsonObject = new JSONObject();
                        jsonObject.put("MID", coverageList.get(coverageIndex).getMID());
                        jsonObject.put("Keys", "RSP_DETAILS_DATA");
                        jsonObject.put("JsonData", rspArray.toString());
                        jsonObject.put("UserId", userId);
                        jsonString = jsonObject.toString();
                        type = CommonString.UPLOADJsonDetail;
                    }
                    //endregion
                    break;
                case "TRAINING_DATA":
                    //region secondary display data
                    db.open();
                    ArrayList<TrainingGetterSetter> trainingList = db.getinsertedTrainingData(coverageList.get(coverageIndex).getStoreId().toString(),
                            coverageList.get(coverageIndex).getVisitDate());
                    if (trainingList.size() > 0) {
                        JSONArray secArray = new JSONArray();
                        for (int j = 0; j < trainingList.size(); j++) {
                            JSONObject obj = new JSONObject();
                            obj.put("MID", coverageList.get(coverageIndex).getMID());
                            obj.put("UserId", userId);
                            obj.put("RSP_CD", trainingList.get(j).getRspname_cd().toString());
                            obj.put("TRAINING_TYPE_CD", trainingList.get(j).getTrainingtype_cd().toString());
                            obj.put("TOPIC_CD", trainingList.get(j).getTopic_cd().toString());
                            obj.put("TRAINING_PIC", trainingList.get(j).getPhoto());
                            secArray.put(obj);
                        }

                        jsonObject = new JSONObject();
                        jsonObject.put("MID", coverageList.get(coverageIndex).getMID());
                        jsonObject.put("Keys", "TRAINING_DATA");
                        jsonObject.put("JsonData", secArray.toString());
                        jsonObject.put("UserId", userId);
                        jsonString = jsonObject.toString();
                        type = CommonString.UPLOADJsonDetail;
                    }
                    //endregion
                    break;
                case "STORE_AUDIT_DATA":
                    //region Promotion data
                    db.open();
                    ArrayList<AuditQuestion> auditList = db.getStoreAuditData(coverageList.get(coverageIndex).getStoreId());
                    if (auditList.size() > 0) {
                        JSONArray promoArray = new JSONArray();
                        for (int j = 0; j < auditList.size(); j++) {
                            JSONObject obj = new JSONObject();
                            obj.put("MID", coverageList.get(coverageIndex).getMID());
                            obj.put("UserId", userId);
                            obj.put("QUESTION_CD", auditList.get(j).getQuestionId());
                            obj.put("IMAGE_ALLOW", auditList.get(j).getImageAllow());
                            obj.put("ANSWER_CD", auditList.get(j).getCurrectanswerCd());
                            obj.put("AUDIT_IMG", auditList.get(j).getAudit_cam());
                            promoArray.put(obj);
                        }
                        jsonObject = new JSONObject();
                        jsonObject.put("MID", coverageList.get(coverageIndex).getMID());
                        jsonObject.put("Keys", "STORE_AUDIT_DATA");
                        jsonObject.put("JsonData", promoArray.toString());
                        jsonObject.put("UserId", userId);

                        jsonString = jsonObject.toString();
                        type = CommonString.UPLOADJsonDetail;
                    }
                    //endregion
                    break;
                case "VISIBILITY_SOFT_MERCH_DATA":
                    //region Competition data
                    db.open();
                    ArrayList<PosmMaster> softmerch_list = db.getVisibilitySoftMerchData(coverageList.get(coverageIndex).getStoreId());
                    if (softmerch_list.size() > 0) {
                        JSONArray compArray = new JSONArray();
                        for (int j = 0; j < softmerch_list.size(); j++) {
                            JSONObject obj = new JSONObject();
                            obj.put("MID", coverageList.get(coverageIndex).getMID());
                            obj.put("UserId", userId);
                            obj.put("VISIBILITY_POSM_CD", softmerch_list.get(j).getPosmId().toString());
                            obj.put("VISIBILITY_NEWDEPLOYMENT_VALUE", softmerch_list.get(j).getDeployment_Value());
                            obj.put("VISIBILITY_SOFTIMG", softmerch_list.get(j).getSoft_merchIMG());
                            obj.put("POSM_TYPE_CD", softmerch_list.get(j).getPosmTypeId());
                            compArray.put(obj);
                        }

                        jsonObject = new JSONObject();
                        jsonObject.put("MID", coverageList.get(coverageIndex).getMID());
                        jsonObject.put("Keys", "VISIBILITY_SOFT_MERCH_DATA");
                        jsonObject.put("JsonData", compArray.toString());
                        jsonObject.put("UserId", userId);

                        jsonString = jsonObject.toString();
                        type = CommonString.UPLOADJsonDetail;
                    }
                    //endregion
                    break;
                case "VISIBILITY_SEMIP_MERCH_DATA":
                    //region SS_POSM/Touchpoint
                    db.open();
                    List<MappingPermanentPosm> semipermanentMerchList = db.getVisibilitySemiPermanetMerchData(coverageList.get(coverageIndex).getStoreId());
                    if (semipermanentMerchList.size() > 0) {
                        JSONArray posmArray = new JSONArray();
                        for (int j = 0; j < semipermanentMerchList.size(); j++) {
                            JSONObject obj = new JSONObject();
                            obj.put("MID", coverageList.get(coverageIndex).getMID());
                            obj.put("UserId", userId);
                            obj.put("SP_VISIBILITY_POSM_CD", semipermanentMerchList.get(j).getPosmId());
                            obj.put("SP_PREVIOUS_QUANTITY", semipermanentMerchList.get(j).getPrev_Qty());
                            obj.put("SP_PREVIOUS_EDT_VALUE", semipermanentMerchList.get(j).getPreV_dValue());
                            obj.put("SP_VISIBILITY_NEWDEPLOYMENT", semipermanentMerchList.get(j).getNewDeploymnt_Value());
                            obj.put("SP_IMG_1", semipermanentMerchList.get(j).getsPermanetIMG_1());
                            obj.put("SP_IMG_2", semipermanentMerchList.get(j).getsPermanetIMG_2());
                            obj.put("SP_IMG_3", semipermanentMerchList.get(j).getsPermanetIMG_3());

                            posmArray.put(obj);


                        }

                        jsonObject = new JSONObject();
                        jsonObject.put("MID", coverageList.get(coverageIndex).getMID());
                        jsonObject.put("Keys", "VISIBILITY_SEMIP_MERCH_DATA");
                        jsonObject.put("JsonData", posmArray.toString());
                        jsonObject.put("UserId", userId);
                        jsonString = jsonObject.toString();
                        type = CommonString.UPLOADJsonDetail;

                    }
                    //endregion
                    break;
                case "SHOPER_MKT_IPOS":
                    //region Secondary_backwall_image
                    db.open();
                    ArrayList<SkuMaster> ipos_listData = db.getiposinseteddata(coverageList.get(coverageIndex).getStoreId());
                    if (ipos_listData.size() > 0) {
                        JSONArray topUpArray = new JSONArray();
                        for (int j = 0; j < ipos_listData.size(); j++) {
                            JSONObject obj = new JSONObject();
                            obj.put("MID", coverageList.get(coverageIndex).getMID());
                            obj.put("UserId", userId);
                            obj.put("SKU_CD", ipos_listData.get(j).getSkuId().toString());
                            obj.put("NUMBER", ipos_listData.get(j).getNumber());
                            obj.put("MACHINE_ON", ipos_listData.get(j).getMachine_on());
                            obj.put("IPOS", ipos_listData.get(j).getIpos());
                            obj.put("IPOS_IMG", ipos_listData.get(j).getIpos_img());
                            topUpArray.put(obj);
                        }

                        jsonObject = new JSONObject();
                        jsonObject.put("MID", coverageList.get(coverageIndex).getMID());
                        jsonObject.put("Keys", "SHOPER_MKT_IPOS");
                        jsonObject.put("JsonData", topUpArray.toString());
                        jsonObject.put("UserId", userId);

                        jsonString = jsonObject.toString();
                        type = CommonString.UPLOADJsonDetail;
                    }
                    //endregion
                    break;
                case "SHOPER_MKT_RXT":
                    //region primary_image_Grid
                    ArrayList<SkuMaster> secCompleteIPOSDATA = db.getRXTInseteddata(coverageList.get(coverageIndex).getStoreId());
                    if (secCompleteIPOSDATA.size() > 0) {
                        JSONArray topUpArray = new JSONArray();
                        for (int j = 0; j < secCompleteIPOSDATA.size(); j++) {
                            JSONObject obj = new JSONObject();
                            obj.put("MID", coverageList.get(coverageIndex).getMID());
                            obj.put("UserId", userId);
                            obj.put("RXT_SKU_CD", secCompleteIPOSDATA.get(j).getSkuId().toString());
                            obj.put("RXT_NUMBER", secCompleteIPOSDATA.get(j).getNumber());
                            obj.put("RXT_MACHINE_ON", secCompleteIPOSDATA.get(j).getMachine_on());
                            obj.put("RXT", secCompleteIPOSDATA.get(j).getRxt());
                            obj.put("RXT_IMG", secCompleteIPOSDATA.get(j).getRxt_img());
                            obj.put("ENGEGMENT", secCompleteIPOSDATA.get(j).getEngegment());
                            topUpArray.put(obj);
                        }

                        jsonObject = new JSONObject();
                        jsonObject.put("MID", coverageList.get(coverageIndex).getMID());
                        jsonObject.put("Keys", "SHOPER_MKT_RXT");
                        jsonObject.put("JsonData", topUpArray.toString());
                        jsonObject.put("UserId", userId);

                        jsonString = jsonObject.toString();
                        type = CommonString.UPLOADJsonDetail;
                    }
                    //endregion
                    break;
                case "MARKET_INFO":
                    //region Primary_Stock
                    ArrayList<InfoTypeMaster> market_infoList = db.getinfotypeinsetedDATA(coverageList.get(coverageIndex).getStoreId());
                    if (market_infoList.size() > 0) {
                        JSONArray topUpArray = new JSONArray();
                        for (int j = 0; j < market_infoList.size(); j++) {
                            JSONObject obj = new JSONObject();
                            obj.put("MID", coverageList.get(coverageIndex).getMID());
                            obj.put("UserId", userId);
                            obj.put("BRAND_CD", market_infoList.get(j).getBrand_cd());
                            obj.put("TYPE_CD", market_infoList.get(j).getType_cd());
                            obj.put("INFO_TYPE_CD", market_infoList.get(j).getInfoTypeId());
                            obj.put("REMARK", market_infoList.get(j).getRemark());
                            obj.put("MARKET_INFO_IMG", market_infoList.get(j).getMarketinfo_img());
                            topUpArray.put(obj);
                        }

                        jsonObject = new JSONObject();
                        jsonObject.put("MID", coverageList.get(coverageIndex).getMID());
                        jsonObject.put("Keys", "MARKET_INFO");
                        jsonObject.put("JsonData", topUpArray.toString());
                        jsonObject.put("UserId", userId);

                        jsonString = jsonObject.toString();
                        type = CommonString.UPLOADJsonDetail;
                    }
                    //endregion
                    break;


               /* case "GeoTag":
                    //region GeoTag
                    ArrayList<GeotaggingBeans> geotaglist = db.getinsertGeotaggingData(coverageList.get(coverageIndex).getStoreId(), "N");
                    if (geotaglist.size() > 0) {
                        JSONArray topUpArray = new JSONArray();
                        for (int j = 0; j < geotaglist.size(); j++) {
                            JSONObject obj = new JSONObject();
                            obj.put(CommonString.KEY_STORE_ID, geotaglist.get(j).getStoreid());
                            obj.put(CommonString.KEY_VISIT_DATE, date);
                            obj.put(CommonString.KEY_LATITUDE, geotaglist.get(j).getLatitude());
                            obj.put(CommonString.KEY_LONGITUDE, geotaglist.get(j).getLongitude());
                            obj.put("FRONT_IMAGE", geotaglist.get(j).getImage());
                            topUpArray.put(obj);
                        }

                        jsonObject = new JSONObject();
                        jsonObject.put("MID", coverageList.get(coverageIndex).getMID());
                        jsonObject.put("Keys", "GeoTag");
                        jsonObject.put("JsonData", topUpArray.toString());
                        jsonObject.put("UserId", _UserId);

                        jsonString = jsonObject.toString();
                        type = CommonString.UPLOADJsonDetail;
                    }
                    //endregion
                    break;*/

            }
            //endregion

            final int[] finalJsonIndex = {keyIndex};
            final String finalKeyName = keyList.get(keyIndex);
            if (jsonString != null && !jsonString.equalsIgnoreCase("")) {
                pb.setMessage("Uploading (" + keyIndex + "/" + keyList.size() + ") \n" + keyList.get(keyIndex) + "\n Store uploading " +
                        (coverageIndex + 1) + "/" + coverageList.size());
                RequestBody jsonData = RequestBody.create(MediaType.parse("application/json"), jsonString);
                adapter = new Retrofit.Builder().baseUrl(CommonString.URL).addConverterFactory(GsonConverterFactory.create()).build();
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
            AlertandMessages.showAlert((Activity) context, ex.toString(), true);
        }
    }

    void updateStatus(final ArrayList<CoverageBean> coverageList, final int coverageIndex,
                      final String status) {
        if (coverageList.get(coverageIndex) != null) {
            try {
                final int[] tempcoverageIndex = {coverageIndex};
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("StoreId", coverageList.get(coverageIndex).getStoreId());
                jsonObject.put("VisitDate", coverageList.get(coverageIndex).getVisitDate());
                jsonObject.put("UserId", userId);
                jsonObject.put("Status", status);
                RequestBody jsonData = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
                adapter = new Retrofit.Builder().baseUrl(CommonString.URL).addConverterFactory(GsonConverterFactory.create()).build();
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
                                            ArrayList<String> list1 = new ArrayList();
                                            list1 = getFileNames(dir.listFiles());
                                            if (list1.size() > 0) {
                                                uploadImageRecursiveWithIndex(0, list1);
                                            } else {
                                                db.open();
                                                db.updateJaurneyPlanSpecificStoreStatus(coverageList.get(coverageIndex).getStoreId(),
                                                        coverageList.get(coverageIndex).getVisitDate(), status);
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
                AlertandMessages.showAlert((Activity) context, ex.toString(), true);

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

    public void uploadImageRecursiveWithIndex(final int index, final ArrayList<String> filelist) {
        final int[] indexlocal = {index};
        String file = null, foldername = null;
        file = filelist.get(indexlocal[0]);
        //file path\\

        if (new File(CommonString.FILE_PATH + file).exists()) {
            if (file.contains("_STOREIMG_") || file.contains("_NONWORKING_") || file.contains("_STOREC_OUTIMG_")) {
                foldername = "CoverageImages";
            } else if (file.contains("_AUDITIMG_")) {
                foldername = "AuditImages";
            } else if (file.contains("_AUDITIMG_")) {
                foldername = "AuditImages";
            } else if (file.contains("_SOFTMERCHIMG_")) {
                foldername = "SoftPosmImages";
            } else if (file.contains("_SEMIPMERCHIMG_ONE_") || file.contains("_SEMIPMERCHIMG_TWO_") || file.contains("_SEMIPMERCHIMG_THREE_")) {
                foldername = "PermanantPosmImages";
            } else if (file.contains("_IPOSING_")) {
                foldername = "IPOSImages";
            } else if (file.contains("_RXTING_")) {
                foldername = "RXTImages";
            } else if (file.contains("_MARKETINFOING_")) {
                foldername = "MarketInfoImages";
            } else if (file.contains("_TRAININGIMG_")) {
                foldername = "TrainingImages";
            } else if (file.contains("_GeoTag-")) {
                foldername = "GeoTagImages";
            }
        }
        //  data.value = 100;
        pb.setMessage("Uploading (" + foldername + "/" + coverageList.size() + ")");
        pb.setProgress((index * 100 / filelist.size()));
        File originalFile = new File(CommonString.FILE_PATH + file);
        //bitmap
        final File finalFile = saveBitmapToFile(originalFile);
        final com.squareup.okhttp.RequestBody photo = com.squareup.okhttp.RequestBody.create(
                com.squareup.okhttp.MediaType.parse("application/octet-stream"), finalFile);
        com.squareup.okhttp.RequestBody body1 = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addFormDataPart("file", finalFile.getName(), photo)
                .addFormDataPart("FolderName", foldername)
                .build();
        retrofit.Retrofit adapter = new retrofit.Retrofit.Builder()
                .baseUrl(CommonString.URLGORIMAG)
                .addConverterFactory(new StringConverterFactory())
                .build();

        //interface
        PostApi api = adapter.create(PostApi.class);
        retrofit.Call<String> call = api.getUploadImage(body1);
        call.enqueue(new retrofit.Callback<String>() {
            @Override
            public void onResponse(retrofit.Response<String> response) {
                if (response.isSuccess() && response.body().contains("Success")) {
                    finalFile.delete();
                    indexlocal[0]++;
                    if (indexlocal[0] == filelist.size()) {
                        pb.setMessage("Updating Status");
                        specific_uploadStatus.get(0).setUploadStatus(CommonString.KEY_U);
                        updatestatusforu(coverageList, 0, coverageList.get(0).getVisitDate(), CommonString.KEY_U);
                    } else {
                        uploadImageRecursiveWithIndex(indexlocal[0], filelist);
                    }
                } else {
                    pb.dismiss();
                    AlertandMessages.showAlert(PreviousDataUploadActivity.this, finalFile.getName() + " Image Not Uploaded", true);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                pb.dismiss();
                if (t instanceof IOException || t instanceof SocketTimeoutException || t instanceof SocketException) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PreviousDataUploadActivity.this);
                    builder.setTitle("Parinaam");
                    builder.setMessage("Network Error in uploading images")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent startUpload = new Intent(PreviousDataUploadActivity.this, MainMenuActivity.class);
                                    startActivity(startUpload);
                                    PreviousDataUploadActivity.this.finish();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });
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
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("StoreId", store_data.get(indexlocal[0]).getStoreId());
                    jsonObject.put("VisitDate", visit_date);
                    jsonObject.put("UserId", userId);
                    jsonObject.put("Status", status);
                    RequestBody jsonData = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
                    adapter = new Retrofit.Builder().baseUrl(CommonString.URL).addConverterFactory(GsonConverterFactory.create()).build();
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
                                    AlertandMessages.showAlert((Activity) context, "Error in Uploading status at coverage :" + finalIndex, true);
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
            AlertandMessages.showAlert((Activity) context, e.getMessage().toString(), true);

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


}
