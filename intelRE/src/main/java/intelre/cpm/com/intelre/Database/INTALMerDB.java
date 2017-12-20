package intelre.cpm.com.intelre.Database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import intelre.cpm.com.intelre.constant.CommonString;
import intelre.cpm.com.intelre.gettersetter.RspGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.AuditQuestion;
import intelre.cpm.com.intelre.gsonGetterSetter.AuditQuestionGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.BrandMaster;
import intelre.cpm.com.intelre.gsonGetterSetter.BrandMasterGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.CategoryMaster;
import intelre.cpm.com.intelre.gsonGetterSetter.CategoryMasterGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.JCPGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.JourneyPlan;
import intelre.cpm.com.intelre.gsonGetterSetter.MappingPermanentPosm;
import intelre.cpm.com.intelre.gsonGetterSetter.MappingPermanentPosmGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.MappingSoftPosm;
import intelre.cpm.com.intelre.gsonGetterSetter.MappingSoftPosmGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.NonWorkingReason;
import intelre.cpm.com.intelre.gsonGetterSetter.NonWorkingReasonGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.PosmMaster;
import intelre.cpm.com.intelre.gsonGetterSetter.PosmMasterGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.RspDetailGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.SkuMaster;
import intelre.cpm.com.intelre.gsonGetterSetter.SkuMasterGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.StoreCategoryMaster;
import intelre.cpm.com.intelre.gsonGetterSetter.TrainingTopicGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.TrainingTypeGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.WindowChecklist;
import intelre.cpm.com.intelre.gsonGetterSetter.WindowMaster;

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
            db.execSQL(CommonString.CREATE_TABLE_RSPDETAILS);


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

    public boolean insertPosmMaster(PosmMasterGetterSetter posmmaster) {
        db.delete("Posm_Master", null, null);
        List<PosmMaster> list = posmmaster.getPosmMaster();
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
                values.put("Image_Allow", data.get(i).getImageAllow());
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

    public boolean insertRspDetailnData(RspDetailGetterSetter rspDetail) {
        db.delete("Rsp_Detail", null, null);
        ContentValues values = new ContentValues();
        List<StoreCategoryMaster> data = rspDetail.getStoreCategoryMaster();
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
                values.put("Brand_Id", data.get(i).getBrandId());
                values.put("IREP_Status", data.get(i).getIREPStatus());

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

    //upendra_18_12_2017 Training_Type
    public boolean insertTrainingTypeData(TrainingTypeGetterSetter trainingType) {
        db.delete("Training_Type", null, null);
        ContentValues values = new ContentValues();
        List<WindowMaster> data = trainingType.getWindowMaster();
        try {
            if (data.size() == 0) {
                return false;
            }

            for (int i = 0; i < data.size(); i++) {

                values.put("Training_Type", data.get(i).getTrainingType());
                values.put("Training_Type_Id", data.get(i).getTrainingTypeId());


                long id = db.insert("Training_Type", null, values);
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


    public boolean insertTrainingTopicData(TrainingTopicGetterSetter TrainingTopic) {
        db.delete("Training_Topic", null, null);
        ContentValues values = new ContentValues();
        List<WindowChecklist> data = TrainingTopic.getWindowChecklist();
        try {
            if (data.size() == 0) {
                return false;
            }

            for (int i = 0; i < data.size(); i++) {

                values.put("Topic", data.get(i).getTopic());
                values.put("Topic_Id", data.get(i).getTrainingTypeId());
                values.put("Training_Type_Id", data.get(i).getTrainingTypeId());


                long id = db.insert("Training_Topic", null, values);
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

    public boolean insertMappingSoftPosmData(MappingSoftPosmGetterSetter MappingSoft) {
        db.delete("Mapping_Soft_Posm", null, null);
        ContentValues values = new ContentValues();
        List<MappingSoftPosm> data = MappingSoft.getMappingSoftPosm();
        try {
            if (data.size() == 0) {
                return false;
            }

            for (int i = 0; i < data.size(); i++) {

                values.put("Classification_Id", data.get(i).getClassificationId());
                values.put("Posm_Id", data.get(i).getPosmId());
                values.put("Region_Id", data.get(i).getRegionId());
                values.put("Store_Type_Id", data.get(i).getStoreTypeId());


                long id = db.insert("Mapping_Soft_Posm", null, values);
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


    public boolean insertMappingPermanentPosmData(MappingPermanentPosmGetterSetter MappingPermanent) {
        db.delete("Mapping_Permanent_Posm", null, null);
        ContentValues values = new ContentValues();
        List<MappingPermanentPosm> data = MappingPermanent.getMappingPermanentPosm();
        try {
            if (data.size() == 0) {
                return false;
            }

            for (int i = 0; i < data.size(); i++) {

                values.put("Posm_Id", data.get(i).getPosmId());
                values.put("Store_Id", data.get(i).getStoreId());

                long id = db.insert("Mapping_Permanent_Posm", null, values);
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


    public boolean insertCategoryMasterData(CategoryMasterGetterSetter CategoryMaster) {
        db.delete("Category_Master", null, null);
        ContentValues values = new ContentValues();
        List<CategoryMaster> data = CategoryMaster.getCategoryMaster();
        try {
            if (data.size() == 0) {
                return false;
            }

            for (int i = 0; i < data.size(); i++) {

                values.put("Category", data.get(i).getCategory());
                values.put("Category_Id", data.get(i).getCategoryId());
                values.put("Category_Sequence", data.get(i).getCategorySequence());


                long id = db.insert("Category_Master", null, values);
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

    public boolean insertBrandMasterData(BrandMasterGetterSetter BrandMaster) {
        db.delete("Brand_Master", null, null);
        ContentValues values = new ContentValues();
        List<BrandMaster> data = BrandMaster.getBrandMaster();
        try {
            if (data.size() == 0) {
                return false;
            }

            for (int i = 0; i < data.size(); i++) {

                values.put("Brand", data.get(i).getBrand());
                values.put("Brand_Id", data.get(i).getBrandId());
                values.put("Brand_Sequence", data.get(i).getBrandSequence());
                values.put("Category_Id", data.get(i).getCategoryId());


                long id = db.insert("Brand_Master", null, values);
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

    public boolean insertSkuMasterData(SkuMasterGetterSetter BrandMaster) {
        db.delete("Sku_Master", null, null);
        ContentValues values = new ContentValues();
        List<SkuMaster> data = BrandMaster.getSkuMaster();
        try {
            if (data.size() == 0) {
                return false;
            }

            for (int i = 0; i < data.size(); i++) {

                values.put("Brand_Id", data.get(i).getBrandId());
                values.put("Sku", data.get(i).getSku());
                values.put("Sku_Id", data.get(i).getSkuId());
                values.put("Sku_Sequence", data.get(i).getSkuSequence());

                long id = db.insert("Sku_Master", null, values);
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


    public ArrayList<JourneyPlan> getStoreData(String date) {
        ArrayList<JourneyPlan> list = new ArrayList<JourneyPlan>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT  * FROM Journey_Plan  " + "WHERE Visit_Date ='" + date + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    JourneyPlan sb = new JourneyPlan();
                    sb.setStoreId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_Id"))));
                    sb.setVisitDate((dbcursor.getString(dbcursor.getColumnIndexOrThrow("Visit_Date"))));
                    sb.setStoreName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_Name")));
                    sb.setAddress1(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Address1")));
                    sb.setAddress2((dbcursor.getString(dbcursor.getColumnIndexOrThrow("Address2"))));
                    sb.setLandmark(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Landmark")));
                    sb.setPincode(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Pincode")));
                    sb.setContactPerson(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Contact_Person")));
                    sb.setContactNo(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Contact_No")));
                    sb.setCity(dbcursor.getString(dbcursor.getColumnIndexOrThrow("City")));
                    sb.setStoreType(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_Type")));
                    sb.setStoreCategory(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_Category")));
                    sb.setClassification(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Classification")));
                    sb.setRegionId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Region_Id"))));
                    sb.setStoreTypeId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_Type_Id"))));
                    sb.setClassificationId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Classification_Id"))));
                    sb.setStoreCategoryId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_Category_Id"))));
                    sb.setReasonId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Reason_Id"))));
                    sb.setUploadStatus(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Upload_Status")));
                    sb.setGeoTag(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Geo_Tag")));
                    sb.setDistributorId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Distributor_Id"))));
                    sb.setCityId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("City_Id"))));
                    sb.setVisibilityLocation1(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Visibility_Location1")));
                    sb.setVisibilityLocation2(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Visibility_Location2")));
                    sb.setVisibilityLocation3(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Visibility_Location3")));
                    sb.setDimension1(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Dimension1")));
                    sb.setDimension2(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Dimension2")));
                    sb.setDimension3(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Dimension3")));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception get JCP!", e.toString());
            return list;
        }


        return list;
    }


    public ArrayList<NonWorkingReason> getNonWorkingData() {

        ArrayList<NonWorkingReason> list = new ArrayList<NonWorkingReason>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * FROM Non_Working_Reason", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    NonWorkingReason sb = new NonWorkingReason();

                    sb.setReasonId(Integer.valueOf(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Reason_Id"))));
                    sb.setReason(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Reason")));
                    sb.setEntryAllow("1".equalsIgnoreCase(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Entry_Allow"))));
                    sb.setImageAllow("1".equalsIgnoreCase(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Image_Allow"))));
                    sb.setGPSMandatory("1".equalsIgnoreCase(dbcursor.getString(dbcursor.getColumnIndexOrThrow("GPS_Mandatory"))));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {

            return list;
        }


        return list;
    }


    public ArrayList<StoreCategoryMaster> getRspDetailData(Integer storeId) {

        ArrayList<StoreCategoryMaster> list = new ArrayList<StoreCategoryMaster>();
        Cursor dbcursor = null;
        try {
            //  dbcursor = db.rawQuery("SELECT * FROM Rsp_Detail", null);
            dbcursor = db.rawQuery("SELECT  * FROM Rsp_Detail  " + "WHERE Store_Id ='" + storeId + "'", null);


            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    StoreCategoryMaster sb = new StoreCategoryMaster();

                    sb.setRspId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Rsp_Id"))));
                    sb.setRspName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Rsp_Name")));
                    sb.setEmail(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Email")));
                    sb.setMobile(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Mobile")));
                    sb.setStoreId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_Id"))));
                    sb.setBrandId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Brand_Id"))));
                    sb.setIREPStatus(Boolean.getBoolean(dbcursor.getString(dbcursor.getColumnIndexOrThrow("IREP_Status"))));


                    list.add(sb);

                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {

            return list;
        }


        return list;
    }


    public ArrayList<BrandMaster> getBrandMasterData() {

        ArrayList<BrandMaster> list = new ArrayList<BrandMaster>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * FROM Brand_Master", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    BrandMaster sb = new BrandMaster();

                    sb.setBrandId(Integer.valueOf(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Brand_Id"))));
                    sb.setBrand(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Brand")));
                    sb.setCategoryId(Integer.valueOf(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Category_Id"))));
                    sb.setBrandSequence(Integer.valueOf(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Brand_Sequence"))));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {

            return list;
        }


        return list;
    }


    /*
        public long InsertRspDetailData(RspGetterSetter data, Integer storeid, String visitdate) {
          //  db.delete(CommonString.TABLE_INSERT_RSPDETAILS, CommonString.KEY_STORE_ID + "='" +storeid  + "'", null);
            ContentValues values = new ContentValues();
            long id=0;

            try {
                values.put(CommonString.KEY_STORE_ID, storeid);
                values.put(CommonString.KEY_VISITDATE, visitdate);
                values.put(CommonString.KEY_EMAILID , data.getEmailid());
                values.put(CommonString.KEY_PHONENO, data.getPhoneno());
                values.put(CommonString.KEY_BRAND   , data.getBrand());
                values.put(CommonString.KEY_IREP_REGISTERED , data.getIrepregistered());
                id= db.insert(CommonString.TABLE_INSERT_RSPDETAILS, null, values);

                if (id > 0) {
                    return id;
                } else {
                    return 0;
                }
            } catch (Exception ex) {
                Log.d("Database Exception ", ex.toString());
                return 0;
            }

        }
    */
    public long InsertRspDetailData(StoreCategoryMaster data, Integer storeid, String visitdate) {
         // db.delete(CommonString.TABLE_INSERT_RSPDETAILS, CommonString.KEY_STORE_ID + "='" +storeid  + "'"+ storeid + "'", null);
        db.delete(CommonString.TABLE_INSERT_RSPDETAILS, CommonString.KEY_STORE_ID + "='" +storeid  + "'", null);
        ContentValues values = new ContentValues();
        long id = 0;

        try {
            values.put(CommonString.COMMON_ID, data.getKey_id());
            values.put(CommonString.KEY_STORE_ID, storeid);
            values.put(CommonString.KEY_VISITDATE, visitdate);
            values.put(CommonString.KEY_RSPID, data.getRspId());
            values.put(CommonString.KEY_FLAG, data.getFlag());
            values.put(CommonString.KEY_RSPNAME, data.getRspName());
            values.put(CommonString.KEY_EMAILID, data.getEmail());
            values.put(CommonString.KEY_PHONENO, data.getMobile());
            values.put(CommonString.KEY_BRAND, data.getBrandId());
            values.put(CommonString.KEY_IREP_REGISTERED, data.getIREPStatus());
            id = db.insert(CommonString.TABLE_INSERT_RSPDETAILS, null, values);

            if (id > 0) {
                return id;
            } else {
                return 0;
            }
        } catch (Exception ex) {
            Log.d("Database Exception ", ex.toString());
            return 0;
        }

    }


    public ArrayList<StoreCategoryMaster> getRspDetailinsertData(Integer storeId) {

        ArrayList<StoreCategoryMaster> list = new ArrayList<StoreCategoryMaster>();
        Cursor dbcursor = null;
        try {
            //  dbcursor = db.rawQuery("SELECT * FROM Rsp_Detail", null);
            dbcursor = db.rawQuery("SELECT  * FROM DR_RSPDETAILS  " + "WHERE Store_Id ='" + storeId + "'", null);


            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    StoreCategoryMaster sb = new StoreCategoryMaster();

                    sb.setRspId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Rsp_Id"))));
                    sb.setRspName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Rsp_Name")));
                    sb.setEmail(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Email")));
                    sb.setMobile(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Mobile")));
                    sb.setStoreId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_Id"))));
                    sb.setBrandId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Brand_Id"))));
                    sb.setIREPStatus(Boolean.getBoolean(dbcursor.getString(dbcursor.getColumnIndexOrThrow("IREP_Status"))));


                    list.add(sb);

                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {

            return list;
        }


        return list;
    }

}
