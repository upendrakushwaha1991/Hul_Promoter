package intelre.cpm.com.intelre.download;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ProgressBar;
import android.widget.TextView;


import org.json.JSONObject;

import java.util.ArrayList;

import intelre.cpm.com.intelre.Database.INTEL_RE_DB;
import intelre.cpm.com.intelre.R;
import intelre.cpm.com.intelre.constant.CommonString;
import intelre.cpm.com.intelre.retrofit.UploadImageWithRetrofit;

public class DownloadActivity extends AppCompatActivity {

    public int listSize = 0;
    Data data;
    int eventType;
    INTEL_RE_DB db;
    String userId, date;
    private Dialog dialog;
    private ProgressBar pb;
    private TextView percentage, message;
    private SharedPreferences preferences = null;
    Toolbar toolbar;
    String str;
    boolean ResultFlag = true;

   //TableStructureGetterSetter tableStructureObj;
   /* JCPGetterSetter jcpObject;
    NonWorkingReasonGetterSetter nonWorkingObj;
    NonWorkingSubReasonGetterSetter nonWorkingSubObj;
    SkuMasterGetterSetter skuObject;
    CategoryMasterGetterSetter categoryObject;
    BrandGroupMasterGetterSetter brandGObject;
    BrandMasterGetterSetter brandMObject;
    WindowMasterGetterSetter windowMObject;
    WindowChecklistGetterSetter windowCObject;
    PosmMasterGetterSetter posmMObject;
    WindowLocationGetterSetter windowLObject;
    MappingWindowGetterSetter mappingWObject;
    MappingStockGetterSetter mappingSObject;
    MappingPosmGetterSetter mappingPObject;
    MappingSelfServiceCategoryGetterSetter mappingSSCategoryObject;
    MappingBrandGroupGetterSetter mappingBrandGroupObject;
    PrimaryBayMasterGetterSetter primaryBayMasterObject;
    DisplayMasterGetterSetter displayMasterObject;
    DisplayTypeMasterGetterSetter displayTypeMasterObject;
    DisplayTermMasterGetterSetter displayTermMasterObject;
    MappingSelfserviceCategoryDisplayGetterSetter mappingCDObject;
    PromoTypeMasterGetterSetter promoObject;
    DeviationJourneyPlanGetterSetter deviationObject;
    TopupCityGetterSetter topUpObject;
    MydbDistributorGetterSetter distributerObject;
    MappingTopupStoreGetterSetter mapTopUpObject;
    MappingMydbPosmGetterSetter mapPosmObject;
    MappingPrimaryCategoryImagesGetterSetter mapPCIObject;
    MappingPosmMonthlyPriorityGetterSetter mapPMPObject;
    MappingMappingPosmMustHaveGetterSetter mapPMHObject;
    MappingMappingPosmActivityGetterSetter mapPAObject;
    MyPerformanceGetterSetter myPerformanceObject;
    MyPerformanceRoutewiseGetterSetter myPRObject;
    StoreLayoutMasterGetterSetter storeLayoutObject;
    StoreSizeMasterGetterSetter storeSizeObject;
    StoreClassificationMasterGetterSetter storeClassMasterObject;
    StoreTypeMasterGetterSetter storeTypeMasterObject;
    TopupDistributorGetterSetter topupDistributorObject;
    StoreCategoryMasterGetterSetter storeCategoryMasterObject;
    MappingMydbPosmMustHaveGetterSetter mappingMydbPosmMustHaveGetterSetter;
    MappingMydbPosmMonthlyPriorityGetterSetter mappingMydbPosmMonthlyPriorityGetterSetter;
    MappingMydbPosmActivityGetterSetter mappingMydbPosmActivityGetterSetter;
    MappingMydbPosmOthersGetterSetter mappingMydbPosmOthersGetterSetter;
    KycMasterGetterSetter kycMasterGetterSetter;
    DocumentsDataGetterSetter documentsDataGetterSetter;*/
    String[] jj;
    Context context;
    int downloadindex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;
        db = new INTEL_RE_DB(context);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
       /* userId = preferences.getString(CommonString.KEY_USERNAME, null);
        date = preferences.getString(CommonString.KEY_DATE, "");*/
        downloadindex = preferences.getInt(CommonString.KEY_DOWNLOAD_INDEX, 0);
        getSupportActionBar().setTitle("Download - " + date);
        toolbar.setTitle("Download - " + date);
        //new UploadTask(context).execute();
        UploadDataTask();

    }
    public void UploadDataTask() {

        try {
            ArrayList<String> keysList = new ArrayList<>();
            ArrayList<String> jsonList = new ArrayList<>();
            ArrayList<String> KeyNames = new ArrayList<>();
            KeyNames.clear();
            keysList.clear();

            keysList.add("Table_Structure");
            keysList.add("Journey_Plan");
            keysList.add("Non_Working_Reason");
            keysList.add("Posm_Master");
            keysList.add("Rsp_Detail");
            keysList.add("Audit_Question");

            keysList.add("Training_Type");
            keysList.add("Training_Topic");
            keysList.add("Mapping_Soft_Posm");
            keysList.add("Mapping_Permanent_Posm");


           /* keysList.add("non_working_sub_reason");
            keysList.add("Sku_Master");
            keysList.add("Category_Master");
            keysList.add("Brand_Group_Master");
            keysList.add("Brand_Master");
            keysList.add("Window_Master");
            keysList.add("Window_Checklist");
            keysList.add("Window_Location");
            keysList.add("Mapping_Window");
            keysList.add("Mapping_Stock");
            keysList.add("Mapping_Selfservice_Brand_Group");
            keysList.add("Primary_Bay_Master");
            keysList.add("Mapping_Selfservice_Category");
            keysList.add("Display_Master");
            keysList.add("Display_Type_Master");
            keysList.add("Display_Term_Master");
            keysList.add("Mapping_Selfservice_Category_Display");
            keysList.add("Promo_Type_Master");
            keysList.add("Deviation_Journey_Plan");
            keysList.add("Topup_City");
            keysList.add("Mydb_Distributor");
            keysList.add("Mapping_Posm_MonthlyPriority");
            keysList.add("Mapping_Posm_MustHave");
            keysList.add("Mapping_Posm_Activity");
            keysList.add("Mapping_Posm");
            keysList.add("My_performance_Mer");
            keysList.add("My_performance_Routewise_Mer");
            keysList.add("Store_Layout_Master");
            keysList.add("Store_Size_Master");
            keysList.add("Store_Classification_Master");
            keysList.add("Store_Type_Master");
            keysList.add("Topup_Distributor");
            keysList.add("Mapping_Primary_Category_Images");
            keysList.add("Store_Category_Master");
            keysList.add("Mapping_Mydb_Posm_MustHave");
            keysList.add("Mapping_Mydb_Posm_MonthlyPriority");
            keysList.add("Mapping_Mydb_Posm_Activity");
            keysList.add("Mapping_Mydb_Posm_Others");
            keysList.add("Kyc_Master");*/
            //keysList.add("Documents_Data");

            if (keysList.size() > 0) {
                for (int i = 0; i < keysList.size(); i++) {

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("Downloadtype", keysList.get(i));
                    jsonObject.put("Username", "test");
                    jsonList.add(jsonObject.toString());
                    KeyNames.add(keysList.get(i));
                }

                if (jsonList.size() > 0) {
                    ProgressDialog pd = new ProgressDialog(context);
                    pd.setCancelable(false);
                    pd.setMessage("Downloading Data" + "(" + "/" + ")");
                    pd.show();
                    UploadImageWithRetrofit downloadData = new UploadImageWithRetrofit(context, db, pd, CommonString.TAG_FROM_CURRENT);
                    downloadData.listSize = jsonList.size();
                    downloadData.downloadDataUniversalWithoutWait(jsonList, KeyNames, downloadindex, CommonString.DOWNLOAD_ALL_SERVICE);
                }


            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    class Data {
        int value;
        String name;
    }
}
