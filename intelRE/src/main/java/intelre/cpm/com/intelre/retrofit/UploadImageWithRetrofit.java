package intelre.cpm.com.intelre.retrofit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import intelre.cpm.com.intelre.Database.INTALMerDB;
import intelre.cpm.com.intelre.constant.AlertandMessages;
import intelre.cpm.com.intelre.constant.CommonString;
import intelre.cpm.com.intelre.gettersetter.ReferenceVariablesForDownloadActivity;
import intelre.cpm.com.intelre.gsonGetterSetter.AuditQuestionGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.BrandMasterGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.CategoryMasterGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.JCPGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.MappingPermanentPosmGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.MappingSoftPosmGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.NonWorkingReasonGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.PosmMasterGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.RspDetailGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.SkuMasterGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.TableStructure;
import intelre.cpm.com.intelre.gsonGetterSetter.TableStructureGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.TrainingTopicGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.TrainingTypeGetterSetter;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by upendrak on 15-12-2017.
 */

public class UploadImageWithRetrofit extends ReferenceVariablesForDownloadActivity {
    boolean isvalid;
    RequestBody body1;
    private Retrofit adapter;
    Context context;
    public static int uploadedFiles = 0;
    public int listSize = 0;
    int status = 0;
    INTALMerDB db;
    ProgressDialog pd;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String _UserId, date, app_ver;
    String[] jj;
    boolean statusUpdated = true;
    int from;

    public UploadImageWithRetrofit(Context context) {
        this.context = context;
    }

    public UploadImageWithRetrofit(Context context, INTALMerDB db, ProgressDialog pd, int from) {
        this.context = context;
        this.db = db;
        this.pd = pd;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
        this.from = from;
       /* _UserId = preferences.getString(CommonString.KEY_USERNAME, "");
        date = preferences.getString(CommonString.KEY_DATE, null);
        try {
            app_ver = String.valueOf(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
        db.open();
    }


    public void downloadDataUniversalWithoutWait(final ArrayList<String> jsonStringList, final ArrayList<String> KeyNames, int downloadindex, int type) {
        status = 0;
        isvalid = false;
        final String[] data_global = {""};
        String jsonString = "", KeyName = "";
        int jsonIndex = 0;

        if (jsonStringList.size() > 0) {

            jsonString = jsonStringList.get(downloadindex);
            KeyName = KeyNames.get(downloadindex);
            jsonIndex = downloadindex;

            pd.setMessage("Downloading (" + downloadindex + "/" + listSize + ") \n" + KeyName + "");
            RequestBody jsonData = RequestBody.create(MediaType.parse("application/json"), jsonString);
            adapter = new Retrofit.Builder().baseUrl(CommonString.URL).addConverterFactory(GsonConverterFactory.create()).build();
            PostApi api = adapter.create(PostApi.class);
            Call<String> call = api.getDownloadAll(jsonData);
            final int[] finalJsonIndex = {jsonIndex};
            final String finalKeyName = KeyName;


            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String responseBody = response.body();
                    String data = null;
                    if (responseBody != null && response.isSuccessful()) {
                        try {
                            data = response.body();
                            if (data.equalsIgnoreCase("")) {
                                data_global[0] = "";

                            } else {
                                //data = data.substring(1, data.length() - 1).replace("\\", "");
                                data_global[0] = data;
                                if (finalKeyName.equalsIgnoreCase("Table_Structure")) {

                                    editor.putInt(CommonString.KEY_DOWNLOAD_INDEX, finalJsonIndex[0]);
                                    editor.apply();
                                    tableStructureObj = new Gson().fromJson(data, TableStructureGetterSetter.class);
                                    String isAllTableCreated = createTable(tableStructureObj);
                                    if (isAllTableCreated != CommonString.KEY_SUCCESS) {
                                        pd.dismiss();
                                        AlertandMessages.showAlert((Activity) context, isAllTableCreated + " not created", true);
                                    }
                                } else {
                                    editor.putInt(CommonString.KEY_DOWNLOAD_INDEX, finalJsonIndex[0]);
                                    editor.apply();
                                    switch (finalKeyName) {
                                        case "Journey_Plan":
                                            jcpObject = new Gson().fromJson(data, JCPGetterSetter.class);
                                            if (jcpObject != null && !db.insertJCPData(jcpObject)) {
                                                pd.dismiss();
                                                AlertandMessages.showSnackbarMsg(context, "JCP data not saved");
                                            }
                                            break;
                                        case "Non_Working_Reason":
                                            nonWorkingObj = new Gson().fromJson(data, NonWorkingReasonGetterSetter.class);
                                            if (nonWorkingObj != null && !db.insertNonWorkingData(nonWorkingObj)) {
                                                pd.dismiss();
                                                AlertandMessages.showSnackbarMsg(context, "Non Working Reason not saved");
                                            }
                                            break;
                                        case "Posm_Master":
                                            posmMObject = new Gson().fromJson(data, PosmMasterGetterSetter.class);
                                            if (posmMObject != null && !db.insertPosmMaster(posmMObject)) {
                                                pd.dismiss();
                                                AlertandMessages.showSnackbarMsg(context, "Posm Master not saved");
                                            }
                                            break;
                                        case "Rsp_Detail":
                                            rspDetailObject = new Gson().fromJson(data, RspDetailGetterSetter.class);
                                            if (rspDetailObject != null && !db.insertRspDetailnData(rspDetailObject)) {
                                                pd.dismiss();
                                                AlertandMessages.showSnackbarMsg(context, "Rsp Detail not saved");
                                            }
                                            break;
                                        case "Audit_Question":
                                            auditQuestionObject = new Gson().fromJson(data, AuditQuestionGetterSetter.class);
                                            if (auditQuestionObject != null && !db.insertAuditQuestionData(auditQuestionObject)) {
                                                pd.dismiss();
                                                AlertandMessages.showSnackbarMsg(context, "Audit Question not saved");
                                            }
                                            break;

                                        //TODAY
                                        case "Training_Type":
                                            trainingTypeObject = new Gson().fromJson(data, TrainingTypeGetterSetter.class);
                                            if (trainingTypeObject != null && !db.insertTrainingTypeData(trainingTypeObject)) {
                                                pd.dismiss();
                                                AlertandMessages.showSnackbarMsg(context, "Training_Type not saved");
                                            }
                                            break;
                                        case "Training_Topic":
                                            trainingTopiceObject = new Gson().fromJson(data, TrainingTopicGetterSetter.class);
                                            if (trainingTopiceObject != null && !db.insertTrainingTopicData(trainingTopiceObject)) {
                                                pd.dismiss();
                                                AlertandMessages.showSnackbarMsg(context, "Training Topic not saved");
                                            }
                                            break;
                                        case "Mapping_Soft_Posm":
                                            mappingSoftPosmObject = new Gson().fromJson(data, MappingSoftPosmGetterSetter.class);
                                            if (mappingSoftPosmObject != null && !db.insertMappingSoftPosmData(mappingSoftPosmObject)) {
                                                pd.dismiss();
                                                AlertandMessages.showSnackbarMsg(context, "Mapping Soft Posm not saved");
                                            }
                                            break;
                                        case "Mapping_Permanent_Posm":
                                            mappingPermanentPosmObject = new Gson().fromJson(data, MappingPermanentPosmGetterSetter.class);
                                            if (mappingPermanentPosmObject != null && !db.insertMappingPermanentPosmData(mappingPermanentPosmObject)) {
                                                pd.dismiss();
                                                AlertandMessages.showSnackbarMsg(context, "Mapping Permanent Posm not saved");
                                            }
                                            break;

                                        case "Category_Master":
                                            categoryMasterObject = new Gson().fromJson(data, CategoryMasterGetterSetter.class);
                                            if (categoryMasterObject != null && !db.insertCategoryMasterData(categoryMasterObject)) {
                                                pd.dismiss();
                                                AlertandMessages.showSnackbarMsg(context, "Mapping Permanent Posm not saved");
                                            }
                                            break;
                                            case "Brand_Master":
                                                brandMasterObject = new Gson().fromJson(data, BrandMasterGetterSetter.class);
                                            if (brandMasterObject != null && !db.insertBrandMasterData(brandMasterObject)) {
                                                pd.dismiss();
                                                AlertandMessages.showSnackbarMsg(context, "Mapping Permanent Posm not saved");
                                            }
                                            break;
                                        case "Sku_Master":
                                            skuMasterObject = new Gson().fromJson(data, SkuMasterGetterSetter.class);
                                            if (skuMasterObject != null && !db.insertSkuMasterData(skuMasterObject)) {
                                                pd.dismiss();
                                                AlertandMessages.showSnackbarMsg(context, "Mapping Permanent Posm not saved");
                                            }
                                            break;


                                        case "Documents_Data":
                                            //documentsDataGetterSetter =  new Gson().fromJson(data, DocumentsDataGetterSetter.class);
                                            //if (documentsDataGetterSetter != null && !db.insertDocumentData(documentsDataGetterSetter)) {
                                            //pd.dismiss();
                                            //    AlertandMessages.showSnackbarMsg(context, "Document Data not saved");
                                            //}
                                            break;
                                    }
                                }
                            }
                            // jsonStringList.remove(finalJsonIndex);
                            // KeyNames.remove(finalJsonIndex);
                            finalJsonIndex[0]++;
                            if (finalJsonIndex[0] != KeyNames.size()) {
                                editor.putInt(CommonString.KEY_DOWNLOAD_INDEX, finalJsonIndex[0]);
                                editor.apply();
                                downloadDataUniversalWithoutWait(jsonStringList, KeyNames, finalJsonIndex[0], CommonString.DOWNLOAD_ALL_SERVICE);
                            } else {
                                editor.putInt(CommonString.KEY_DOWNLOAD_INDEX, 0);
                                editor.apply();
                                //pd.dismiss();
                                //AlertandMessages.showAlert((Activity) context, "All data downloaded Successfully", true);
                                //downloadImages();
                                pd.setMessage("Downloading Images");
                                new DownloadImageTask().execute();
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                            editor.putInt(CommonString.KEY_DOWNLOAD_INDEX, finalJsonIndex[0]);
                            editor.apply();
                            pd.dismiss();
                            AlertandMessages.showAlert((Activity) context, "Error in downloading Data at " + finalKeyName, true);
                        }
                    } else {
                        editor.putInt(CommonString.KEY_DOWNLOAD_INDEX, finalJsonIndex[0]);
                        editor.apply();
                        pd.dismiss();
                        AlertandMessages.showAlert((Activity) context, "Error in downloading Data at " + finalKeyName, true);

                    }

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    isvalid = true;
                    pd.dismiss();
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


        } else {
            editor.putInt(CommonString.KEY_DOWNLOAD_INDEX, 0);
            editor.apply();
            // pd.dismiss();
            // AlertandMessages.showAlert((Activity) context, "All data downloaded Successfully", true);
            pd.setMessage("Downloading Images");
            new DownloadImageTask().execute();


        }
    }

    class DownloadImageTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {

            try {
                downloadImages();
                return CommonString.KEY_SUCCESS;
            } catch (FileNotFoundException ex) {
                return CommonString.KEY_FAILURE;
            } catch (IOException ex) {
                return CommonString.KEY_FAILURE;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equalsIgnoreCase(CommonString.KEY_SUCCESS)) {
                pd.dismiss();
                AlertandMessages.showAlert((Activity) context, "All data downloaded Successfully", true);
            } else {
                pd.dismiss();
                AlertandMessages.showAlert((Activity) context, "Error in downloading", true);
            }

        }

    }

    void downloadImages() throws IOException, FileNotFoundException {
    } /*{
        //region JCP Image Download
        if (jcpObject != null) {

            for (int i = 0; i < jcpObject.getJourneyPlan().size(); i++) {

                String image_name = jcpObject.getJourneyPlan().get(i).getImageName();
                if (image_name != null && !image_name.equalsIgnoreCase("NA")
                        && !image_name.equalsIgnoreCase("")) {
                    URL url = new URL(jcpObject.getJourneyPlan().get(i).getImagePath() + image_name);
                    HttpURLConnection c = (HttpURLConnection) url.openConnection();
                    c.setRequestMethod("GET");
                    c.getResponseCode();
                    c.connect();

                    if (c.getResponseCode() == 200) {
                        int length = c.getContentLength();
                        String size = new DecimalFormat("##.##")
                                .format((double) ((double) length / 1024))
                                + " KB";

                               *//* String PATH = Environment
                                        .getExternalStorageDirectory()
                                        + "/GT_GSK_Images/";*//*
                        File file = new File(CommonString.FILE_PATH_Downloaded);
                        file.mkdirs();

                        if (!new File(CommonString.FILE_PATH_Downloaded
                                + image_name).exists()
                                && !size.equalsIgnoreCase("0 KB")) {

                            jj = image_name.split("\\/");
                            image_name = jj[jj.length - 1];

                            File outputFile = new File(file,
                                    image_name);
                            FileOutputStream fos = null;

                            fos = new FileOutputStream(outputFile);
                            InputStream is1 = (InputStream) c.getInputStream();
                            int bytes = 0;
                            byte[] buffer = new byte[1024];
                            int len1 = 0;

                            while ((len1 = is1.read(buffer)) != -1) {

                                bytes = (bytes + len1);

                                // data.value = (int) ((double) (((double)
                                // bytes) / length) * 100);

                                fos.write(buffer, 0, len1);

                            }

                            fos.close();
                            is1.close();

                        }
                    }
                }
            }

        }
        //endregion

        //region Category Images
        if (categoryObject != null) {

            for (int i = 0; i < categoryObject.getCategoryMaster().size(); i++) {

                String image_name = categoryObject.getCategoryMaster().get(i).getIcon();
                if (image_name != null && !image_name.equalsIgnoreCase("NA")
                        && !image_name.equalsIgnoreCase("")) {
                    URL url = new URL(categoryObject.getCategoryMaster().get(i).getImagePath() + image_name);
                    HttpURLConnection c = (HttpURLConnection) url.openConnection();
                    c.setRequestMethod("GET");
                    c.getResponseCode();
                    c.connect();

                    if (c.getResponseCode() == 200) {

                        int length = c.getContentLength();

                        String size = new DecimalFormat("##.##")
                                .format((double) ((double) length / 1024))
                                + " KB";

                               *//* String PATH = Environment
                                        .getExternalStorageDirectory()
                                        + "/GT_GSK_Images/";*//*
                        File file = new File(CommonString.FILE_PATH_Downloaded);
                        file.mkdirs();

                        if (!new File(CommonString.FILE_PATH_Downloaded
                                + image_name).exists()
                                && !size.equalsIgnoreCase("0 KB")) {

                            jj = image_name.split("\\/");
                            image_name = jj[jj.length - 1];

                            File outputFile = new File(file,
                                    image_name);
                            FileOutputStream fos = new FileOutputStream(
                                    outputFile);
                            InputStream is1 = (InputStream) c
                                    .getInputStream();

                            int bytes = 0;
                            byte[] buffer = new byte[1024];
                            int len1 = 0;

                            while ((len1 = is1.read(buffer)) != -1) {

                                bytes = (bytes + len1);

                                // data.value = (int) ((double) (((double)
                                // bytes) / length) * 100);

                                fos.write(buffer, 0, len1);

                            }

                            fos.close();
                            is1.close();

                        }
                    }
                }


                String image_name2 = categoryObject.getCategoryMaster().get(i).getIconDone();
                if (image_name2 != null && !image_name2.equalsIgnoreCase("NA")
                        && !image_name2.equalsIgnoreCase("")) {
                    URL url = new URL(categoryObject.getCategoryMaster().get(i).getImagePath() + image_name2);
                    HttpURLConnection c = (HttpURLConnection) url.openConnection();
                    c.setRequestMethod("GET");
                    c.getResponseCode();
                    c.connect();

                    if (c.getResponseCode() == 200) {

                        int length = c.getContentLength();

                        String size = new DecimalFormat("##.##")
                                .format((double) ((double) length / 1024))
                                + " KB";

                                *//*String PATH = Environment
                                        .getExternalStorageDirectory()
                                        + "/GT_GSK_Images/";*//*
                        File file = new File(CommonString.FILE_PATH_Downloaded);
                        file.mkdirs();

                        if (!new File(CommonString.FILE_PATH_Downloaded
                                + image_name2).exists()
                                && !size.equalsIgnoreCase("0 KB")) {

                            jj = image_name2.split("\\/");
                            image_name2 = jj[jj.length - 1];

                            File outputFile = new File(file,
                                    image_name2);
                            FileOutputStream fos = new FileOutputStream(
                                    outputFile);
                            InputStream is1 = (InputStream) c
                                    .getInputStream();

                            int bytes = 0;
                            byte[] buffer = new byte[1024];
                            int len1 = 0;

                            while ((len1 = is1.read(buffer)) != -1) {

                                bytes = (bytes + len1);

                                // data.value = (int) ((double) (((double)
                                // bytes) / length) * 100);

                                fos.write(buffer, 0, len1);
                            }
                            fos.close();
                            is1.close();
                        }
                    }
                }
            }

        }
        //endregion

        //region mapping window Images
        if (mappingWObject != null) {

            for (int i = 0; i < mappingWObject.getMappingWindow().size(); i++) {

                String image_name = mappingWObject.getMappingWindow().get(i).getPlanogramImage();
                if (image_name != null && !image_name.equalsIgnoreCase("NA")
                        && !image_name.equalsIgnoreCase("")) {
                    URL url = new URL(mappingWObject.getMappingWindow().get(i).getImagePath() + image_name);
                    HttpURLConnection c = (HttpURLConnection) url.openConnection();
                    c.setRequestMethod("GET");
                    c.getResponseCode();
                    c.connect();

                    if (c.getResponseCode() == 200) {
                        int length = c.getContentLength();
                        String size = new DecimalFormat("##.##")
                                .format((double) ((double) length / 1024))
                                + " KB";

                        String PATH = Environment
                                .getExternalStorageDirectory()
                                + "/GT_GSK_Images/";
                        File file = new File(CommonString.FILE_PATH_Downloaded);
                        file.mkdirs();

                        if (!new File(CommonString.FILE_PATH_Downloaded
                                + image_name).exists()
                                && !size.equalsIgnoreCase("0 KB")) {

                            jj = image_name.split("\\/");
                            image_name = jj[jj.length - 1];

                            File outputFile = new File(file,
                                    image_name);
                            FileOutputStream fos = new FileOutputStream(
                                    outputFile);
                            InputStream is1 = (InputStream) c
                                    .getInputStream();

                            int bytes = 0;
                            byte[] buffer = new byte[1024];
                            int len1 = 0;

                            while ((len1 = is1.read(buffer)) != -1) {

                                bytes = (bytes + len1);

                                // data.value = (int) ((double) (((double)
                                // bytes) / length) * 100);

                                fos.write(buffer, 0, len1);

                            }

                            fos.close();
                            is1.close();

                        }
                    }
                }
            }

        }
        //endregion

    }*/

    String createTable(TableStructureGetterSetter tableGetSet) {
        List<TableStructure> tableList = tableGetSet.getTableStructure();
        for (int i = 0; i < tableList.size(); i++) {
            String table = tableList.get(i).getSqlText();
            if (db.createtable(table) == 0) {
                return table;
            }
        }
        return CommonString.KEY_SUCCESS;
    }


}
