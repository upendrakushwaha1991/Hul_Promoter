package intelre.cpm.com.intelre.Database;


import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;

import intelre.cpm.com.intelre.gsonGetterSetter.AuditQuestion;
import intelre.cpm.com.intelre.gsonGetterSetter.AuditQuestionGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.JCPGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.JourneyPlan;
import intelre.cpm.com.intelre.gsonGetterSetter.NonWorkingReason;
import intelre.cpm.com.intelre.gsonGetterSetter.NonWorkingReasonGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.PosmMaster;
import intelre.cpm.com.intelre.gsonGetterSetter.PosmMasterGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.RspDetailGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.StoreCategoryMaster;

/**
 * Created by upendrak on 15-12-2017.
 */

public class INTALMerDB extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "INTEL_MER_DB";
    public static final int DATABASE_VERSION = 13;
    private SQLiteDatabase db;
    Context context;

    public INTALMerDB(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public void open() {
        try {
            db = this.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
           // db.execSQL(CommonString.CREATE_TABLE_PRIMARY_BAY_RANGE);


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // db.execSQL("DROP TABLE IF EXISTS " + TableBean.getJourneyPlan());
    }

    public int createtable(String sqltext) {
        try {
            db.execSQL(sqltext);
            return 1;
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }


    public boolean insertJCPData(JCPGetterSetter data) {
        db.delete("Journey_Plan", null, null);
        List<JourneyPlan> jcpList = data.getJourneyPlan();

        ContentValues values = new ContentValues();
        try {
            if (jcpList.size() == 0) {
                return false;
            }

            for (int i = 0; i < jcpList.size(); i++) {

                values.put("Store_Id", jcpList.get(i).getStoreId());
                values.put("Visit_Date", jcpList.get(i).getVisitDate());
                values.put("Store_Name", jcpList.get(i).getStoreName());
                values.put("Address1", jcpList.get(i).getAddress1());
                values.put("Address2", jcpList.get(i).getAddress2());
                values.put("Landmark", jcpList.get(i).getLandmark());
                values.put("Pincode", jcpList.get(i).getPincode());
                values.put("Contact_Person", jcpList.get(i).getContactPerson());
                values.put("Contact_No", jcpList.get(i).getContactNo());
                values.put("City", jcpList.get(i).getCity());
                values.put("Store_Type", jcpList.get(i).getStoreType());
                values.put("Store_Category", jcpList.get(i).getStoreCategory());
                values.put("Classification", jcpList.get(i).getClassification());
                values.put("Store_Type_Id", jcpList.get(i).getStoreTypeId());
                values.put("Classification_Id", jcpList.get(i).getClassificationId());
                values.put("Store_Category_Id", jcpList.get(i).getStoreCategoryId());
                values.put("Reason_Id", jcpList.get(i).getReasonId());
                values.put("Upload_Status", jcpList.get(i).getUploadStatus());
                values.put("Geo_Tag", jcpList.get(i).getGeoTag());
                values.put("Distributor_Id", jcpList.get(i).getDistributorId());
                values.put("City_Id", jcpList.get(i).getCityId());
                values.put("Visibility_Location1", jcpList.get(i).getVisibilityLocation1());
                values.put("Visibility_Location2", jcpList.get(i).getVisibilityLocation2());
                values.put("Visibility_Location3", jcpList.get(i).getVisibilityLocation3());
                values.put("Dimension1", jcpList.get(i).getDimension1());
                values.put("Dimension2", jcpList.get(i).getDimension2());
                values.put("Dimension3", jcpList.get(i).getDimension3());
                values.put("Store_Type_Id", jcpList.get(i).getStoreTypeId());
                values.put("Classification_Id", jcpList.get(i).getClassificationId());
                values.put("City_Id", jcpList.get(i).getCityId());
                values.put("Region_id", jcpList.get(i).getRegionId());



                long id = db.insert("Journey_Plan", null, values);
                if (id == -1) {
                    throw new Exception();
                }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d("Exception in Jcp", ex.toString());
            return false;
        }
    }

    public boolean insertNonWorkingData(NonWorkingReasonGetterSetter nonWorkingdata) {
        db.delete("Non_Working_Reason", null, null);
        ContentValues values = new ContentValues();
        List<NonWorkingReason> data = nonWorkingdata.getNonWorkingReason();
        try {
            if (data.size() == 0) {
                return false;
            }

            for (int i = 0; i < data.size(); i++) {

                values.put("Reason_Id", data.get(i).getReasonId());
                values.put("Reason", data.get(i).getReason());
                values.put("Entry_Allow", data.get(i).getEntryAllow());
                values.put("Image_Allow", data.get(i).getImageAllow());
                values.put("GPS_Mandatory", data.get(i).getGPSMandatory());

                long id = db.insert("Non_Working_Reason", null, values);
                if (id == -1) {
                    throw new Exception();
                }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d("Database Exception  ", ex.toString());
            return false;
        }
    }

    public boolean insertPosmMaster(PosmMasterGetterSetter data) {
        db.delete("Posm_Master", null, null);
        List<PosmMaster> list = data.getPosmMaster();
        ContentValues values = new ContentValues();
        try {
            if (list.size() == 0) {
                return false;
            }
            for (int i = 0; i < list.size(); i++) {
                values.put("Posm_Id", list.get(i).getPosmId());
                values.put("Posm", list.get(i).getPosm());
                values.put("Posm_Type", list.get(i).getPosmType());
                values.put("Posm_Type_Id", list.get(i).getPosmTypeId());
                long id = db.insert("Posm_Master", null, values);
                if (id == -1) {
                    throw new Exception();
                }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d("Exception ", " in Posm_Master " + ex.toString());
            return false;
        }
    }



    public boolean insertAuditQuestionData(AuditQuestionGetterSetter auditdata) {
        db.delete("Audit_Question", null, null);
        ContentValues values = new ContentValues();
        List<AuditQuestion> data = auditdata.getAuditQuestion();
        try {
            if (data.size() == 0) {
                return false;
            }

            for (int i = 0; i < data.size(); i++) {

                values.put("Question_Id", data.get(i).getQuestionId());
                values.put("Question", data.get(i).getQuestion());
                values.put("Answer", data.get(i).getAnswer());
                values.put("Answer_Id", data.get(i).getAnswerId());
                values.put("Question_Category", data.get(i).getQuestionCategory());
                values.put("Question_Category_Id", data.get(i).getQuestionCategoryId());
                values.put("Question_Id", data.get(i).getQuestionId());


                long id = db.insert("Audit_Question", null, values);
                if (id == -1) {
                    throw new Exception();
                }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d("Database Exception  ", ex.toString());
            return false;
        }
    }

    public boolean insertRspDetailnData(RspDetailGetterSetter auditdata) {
        db.delete("Rsp_Detail", null, null);
        ContentValues values = new ContentValues();
        List<StoreCategoryMaster> data = auditdata.getStoreCategoryMaster();
        try {
            if (data.size() == 0) {
                return false;
            }

            for (int i = 0; i < data.size(); i++) {

                values.put("Email", data.get(i).getEmail());
                values.put("Mobile", data.get(i).getMobile());
                values.put("Rsp_Id", data.get(i).getRspId());
                values.put("Rsp_Name", data.get(i).getRspName());
                values.put("Store_Id", data.get(i).getStoreId());

                long id = db.insert("Rsp_Detail", null, values);
                if (id == -1) {
                    throw new Exception();
                }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d("Database Exception  ", ex.toString());
            return false;
        }
    }

}
