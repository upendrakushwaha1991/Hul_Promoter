package intelre.cpm.com.hulcnc.Database;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import intelre.cpm.com.hulcnc.constant.CommonString;
import intelre.cpm.com.hulcnc.gettersetter.NoSaleGetterSetter;
import intelre.cpm.com.hulcnc.gettersetter.QuizGetterSetter;
import intelre.cpm.com.hulcnc.gettersetter.QuizQuestion;
import intelre.cpm.com.hulcnc.gettersetter.QuizQuestionGetterSetter;
import intelre.cpm.com.hulcnc.gettersetter.SaleReportsGetterSetter;
import intelre.cpm.com.hulcnc.gettersetter.SalesEntryGetterSetter;
import intelre.cpm.com.hulcnc.gettersetter.SampledGetterSetter;
import intelre.cpm.com.hulcnc.gettersetter.SearchSalesGetterSetter;
import intelre.cpm.com.hulcnc.gettersetter.SearchStoreDataGetterSetter;
import intelre.cpm.com.hulcnc.gettersetter.StockAvailabilityGetterSetter;
import intelre.cpm.com.hulcnc.gettersetter.TrainingGetterSetter;
import intelre.cpm.com.hulcnc.gettersetter.TrainingQuizGetterSetter;
import intelre.cpm.com.hulcnc.gsonGetterSetter.AuditQuestion;
import intelre.cpm.com.hulcnc.gsonGetterSetter.InfoTypeMaster;
import intelre.cpm.com.hulcnc.gsonGetterSetter.MappingStock;
import intelre.cpm.com.hulcnc.gsonGetterSetter.MappingStockSetterGetter;
import intelre.cpm.com.hulcnc.gsonGetterSetter.SalesReport;
import intelre.cpm.com.hulcnc.gsonGetterSetter.SalesReportGetterSetter;
import intelre.cpm.com.hulcnc.gsonGetterSetter.StoreCategoryMaster;

import intelre.cpm.com.hulcnc.gettersetter.GeotaggingBeans;

import intelre.cpm.com.hulcnc.delegates.CoverageBean;
import intelre.cpm.com.hulcnc.gettersetter.StoreProfileGetterSetter;
import intelre.cpm.com.hulcnc.gsonGetterSetter.AuditQuestionGetterSetter;
import intelre.cpm.com.hulcnc.gsonGetterSetter.BrandMaster;
import intelre.cpm.com.hulcnc.gsonGetterSetter.BrandMasterGetterSetter;
import intelre.cpm.com.hulcnc.gsonGetterSetter.CategoryMasterGetterSetter;
import intelre.cpm.com.hulcnc.gsonGetterSetter.InfoTypeMasterGetterSetter;
import intelre.cpm.com.hulcnc.gsonGetterSetter.JCPGetterSetter;
import intelre.cpm.com.hulcnc.gsonGetterSetter.JourneyPlan;
import intelre.cpm.com.hulcnc.gsonGetterSetter.MappingPermanentPosm;
import intelre.cpm.com.hulcnc.gsonGetterSetter.MappingPermanentPosmGetterSetter;
import intelre.cpm.com.hulcnc.gsonGetterSetter.MappingSoftPosm;
import intelre.cpm.com.hulcnc.gsonGetterSetter.MappingSoftPosmGetterSetter;
import intelre.cpm.com.hulcnc.gsonGetterSetter.NonWorkingReason;
import intelre.cpm.com.hulcnc.gsonGetterSetter.NonWorkingReasonGetterSetter;
import intelre.cpm.com.hulcnc.gsonGetterSetter.PosmMaster;
import intelre.cpm.com.hulcnc.gsonGetterSetter.PosmMasterGetterSetter;
import intelre.cpm.com.hulcnc.gsonGetterSetter.RspDetailGetterSetter;
import intelre.cpm.com.hulcnc.gsonGetterSetter.SkuMaster;
import intelre.cpm.com.hulcnc.gsonGetterSetter.SkuMasterGetterSetter;
import intelre.cpm.com.hulcnc.gsonGetterSetter.StorewiseFocusSalesReport;
import intelre.cpm.com.hulcnc.gsonGetterSetter.StorewiseFocusSalesReportGetterSetter;
import intelre.cpm.com.hulcnc.gsonGetterSetter.StorewiseSalesReport;
import intelre.cpm.com.hulcnc.gsonGetterSetter.StorewiseSalesReportGetterSetter;
import intelre.cpm.com.hulcnc.gsonGetterSetter.SubCategoryMasteSetterGetter;
import intelre.cpm.com.hulcnc.gsonGetterSetter.SubCategoryMaster;
import intelre.cpm.com.hulcnc.gsonGetterSetter.TrainingTopicGetterSetter;
import intelre.cpm.com.hulcnc.gsonGetterSetter.TrainingType;
import intelre.cpm.com.hulcnc.gsonGetterSetter.TrainingTypeGetterSetter;
import intelre.cpm.com.hulcnc.gsonGetterSetter.TrainingTypeQuizGetterSetter;
import intelre.cpm.com.hulcnc.gsonGetterSetter.WindowChecklist;
import intelre.cpm.com.hulcnc.gsonGetterSetter.WindowMaster;

/**
 * /**
 * Created by upendra on 20-7-2018.
 */

public class HUL_CNC_DB extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "HUL_CNC5";
    public static final int DATABASE_VERSION = 2;
    private SQLiteDatabase db;
    Context context;

    public HUL_CNC_DB(Context context) {

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
            db.execSQL(CommonString.CREATE_TABLE_COVERAGE_DATA);
            db.execSQL(CommonString.CREATE_TABLE_STORE_PROFILE_DATA);
            db.execSQL(CommonString.CREATE_TABLE_STOCK_OPENINGHEADER_DATA);
            db.execSQL(CommonString.CREATE_TABLE_SALES_STOCK_OPENINGHEADER_DATA);
            db.execSQL(CommonString.CREATE_TABLE_STORE_STOCK_DATA);
            db.execSQL(CommonString.CREATE_TABLE_STORE_SALES_STOCK_DATA);
            db.execSQL(CommonString.CREATE_TABLE_STORE_GEOTAGGING);
            db.execSQL(CommonString.CREATE_TABLE_SEARCH_SALES_ENTRY_DATA);
            db.execSQL(CommonString.CREATE_TABLE_NO_SALE);
            db.execSQL(CommonString.CREATE_TABLE_SEARCH_STORE);
            db.execSQL(CommonString.CREATE_TABLE_QUIZ_OPENINGHEADER_DATA);
            db.execSQL(CommonString.CREATE_TABLE_STORE_QUIZ_DATA);
            db.execSQL(CommonString.CREATE_TABLE_INSERT_EXPENSEIMAGE_DATA);
            db.execSQL(CommonString.CREATE_TABLE_INSERT_CUSTOMER_DATA);
            db.execSQL(CommonString.CREATE_TABLE_INSERT_CUSTOMER_POPUP_DATA);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void deleteSpecificStoreData(String storeid) {
        db.delete(CommonString.TABLE_COVERAGE_DATA, CommonString.KEY_STORE_ID + "='" + storeid + "'", null);
        //   db.delete(CommonString.TABLE_STORE_PROFILE_DATA, CommonString.KEY_STORE_ID + "='" + storeid + "'", null);
        db.delete(CommonString.TABLE_INSERT_STOCK_OPENINGHEADER_DATA, CommonString.KEY_STORE_CD + "='" + storeid + "'", null);
        db.delete(CommonString.TABLE_INSERT_SALES_STOCK_OPENINGHEADER_DATA, CommonString.KEY_STORE_CD + "='" + storeid + "'", null);
        db.delete(CommonString.TABLE_STORE_STOCK_DATA, CommonString.KEY_STORE_CD + "='" + storeid + "'", null);
        db.delete(CommonString.TABLE_STORE_SALES_STOCK_DATA, CommonString.KEY_STORE_CD + "='" + storeid + "'", null);
        db.delete(CommonString.TABLE_SEARCH_SALES_ENTRY_DATA, CommonString.KEY_STORE_ID + "='" + storeid + "'", null);
        db.delete(CommonString.TABLE_NO_SALE, CommonString.KEY_STORE_ID + "='" + storeid + "'", null);
        db.delete(CommonString.TABLE_SEARCH_STORE_DATA, CommonString.KEY_STORE_ID + "='" + storeid + "'", null);
        db.delete(CommonString.TABLE_INSERT_QUIZ_OPENINGHEADER_DATA, CommonString.KEY_STORE_CD + "='" + storeid + "'", null);
        db.delete(CommonString.TABLE_STORE_QUIZ_DATA, CommonString.KEY_STORE_CD + "='" + storeid + "'", null);
        db.delete(CommonString.TABLE_INSERT_CUSTOMER_POPUP_DATA, CommonString.KEY_STORE_ID + "='" + storeid + "'", null);
        db.delete(CommonString.TABLE_INSERT_CUSTOMER_DATA, CommonString.KEY_STORE_ID + "='" + storeid + "'", null);

        db.delete(CommonString.TABLE_INSERT_QUIZ_DATA, CommonString.KEY_STORE_ID + "='" + storeid + "'", null);



    }


    public void deleteAllTables() {
        db.delete(CommonString.TABLE_COVERAGE_DATA, null, null);
        //  db.delete(CommonString.TABLE_STORE_PROFILE_DATA, null, null);
        db.delete(CommonString.TABLE_INSERT_STOCK_OPENINGHEADER_DATA, null, null);
        db.delete(CommonString.TABLE_INSERT_SALES_STOCK_OPENINGHEADER_DATA, null, null);
        db.delete(CommonString.TABLE_STORE_STOCK_DATA, null, null);
        db.delete(CommonString.TABLE_STORE_SALES_STOCK_DATA, null, null);
        db.delete(CommonString.TABLE_SEARCH_SALES_ENTRY_DATA, null, null);
        db.delete(CommonString.TABLE_NO_SALE, null, null);
        db.delete(CommonString.TABLE_SEARCH_STORE_DATA, null, null);
        db.delete(CommonString.TABLE_INSERT_QUIZ_OPENINGHEADER_DATA, null, null);
        db.delete(CommonString.TABLE_STORE_QUIZ_DATA, null, null);
        db.delete(CommonString.TABLE_INSERT_CUSTOMER_POPUP_DATA, null, null);
        db.delete(CommonString.TABLE_INSERT_CUSTOMER_DATA, null, null);
        db.delete(CommonString.TABLE_INSERT_QUIZ_DATA, null, null);


    }

    public void deletePreviousUploadedData(String visit_date) {
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT  * from COVERAGE_DATA where VISIT_DATE < '" + visit_date + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                int icount = dbcursor.getCount();
                dbcursor.close();
                if (icount > 0) {
                    db.delete(CommonString.TABLE_COVERAGE_DATA, null, null);
                    db.delete(CommonString.TABLE_STORE_PROFILE_DATA, null, null);
                    db.delete(CommonString.TABLE_INSERT_STOCK_OPENINGHEADER_DATA, null, null);
                    db.delete(CommonString.TABLE_INSERT_SALES_STOCK_OPENINGHEADER_DATA, null, null);
                    db.delete(CommonString.TABLE_STORE_STOCK_DATA, null, null);
                    db.delete(CommonString.TABLE_STORE_SALES_STOCK_DATA, null, null);
                    db.delete(CommonString.TABLE_SEARCH_SALES_ENTRY_DATA, null, null);
                    db.delete(CommonString.TABLE_NO_SALE, null, null);
                    db.delete(CommonString.TABLE_SEARCH_STORE_DATA, null, null);
                    db.delete(CommonString.TABLE_INSERT_QUIZ_DATA, null, null);
                    db.delete(CommonString.TABLE_INSERT_QUIZ_OPENINGHEADER_DATA, null, null);
                    db.delete(CommonString.TABLE_STORE_QUIZ_DATA, null, null);
                }
                dbcursor.close();
            }

        } catch (Exception e) {
            Log.d("Exception when fetching", e.toString());

        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
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
                values.put("Store_Type_Id", jcpList.get(i).getStoreTypeId());
                values.put("Reason_Id", jcpList.get(i).getReasonId());
                values.put("Upload_Status", jcpList.get(i).getUploadStatus());
                values.put("Geo_Tag", jcpList.get(i).getGeoTag());
                values.put("Distributor_Id", jcpList.get(i).getDistributorId());
                values.put("City_Id", jcpList.get(i).getCityId());
                values.put("Region_id", jcpList.get(i).getRegionId());
                values.put("Quiz_Open", jcpList.get(i).getQuizOpen());
                values.put("Time_Period", jcpList.get(i).getTimePeriod());

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


    public boolean insertCategoryMasterData(CategoryMasterGetterSetter CategoryMaster) {
        db.delete("Category_Master", null, null);
        ContentValues values = new ContentValues();
        List<intelre.cpm.com.hulcnc.gsonGetterSetter.CategoryMaster> data = CategoryMaster.getCategoryMaster();
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
        List<intelre.cpm.com.hulcnc.gsonGetterSetter.BrandMaster> data = BrandMaster.getBrandMaster();
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
                values.put("MRP", data.get(i).getMrp());

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


    public boolean insertMappingStockData(MappingStockSetterGetter infotype) {
        db.delete("Mapping_Stock", null, null);
        ContentValues values = new ContentValues();
        List<MappingStock> data = infotype.getMappingStock();
        try {
            if (data.size() == 0) {
                return false;
            }
            for (int i = 0; i < data.size(); i++) {
                values.put("Sku_Id", data.get(i).getSkuId());
                values.put("Region_Id", data.get(i).getRegionId());
                values.put("Distributor_Id", data.get(i).getDistributorId());
                long id = db.insert("Mapping_Stock", null, values);
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

    public boolean insertQuizData(QuizQuestionGetterSetter infotype) {
        db.delete("Quiz_Question", null, null);
        ContentValues values = new ContentValues();
        List<QuizQuestion> data = infotype.getQuizQuestion();
        try {
            if (data.size() == 0) {
                return false;
            }
            for (int i = 0; i < data.size(); i++) {
                values.put("Answer", data.get(i).getAnswer());
                values.put("Answer_Id", data.get(i).getAnswerId());
                values.put("Brand", data.get(i).getBrand());
                values.put("Brand_Id", data.get(i).getBrandId());
                values.put("Question", data.get(i).getQuestion());
                values.put("Question_Category", data.get(i).getQuestionCategory());
                values.put("Question_Category_Id", data.get(i).getQuestionCategoryId());
                values.put("Question_Id", data.get(i).getQuestionId());
                values.put("Right_Answer", data.get(i).getRightAnswer());
                long id = db.insert("Quiz_Question", null, values);
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

    public boolean insertSalesReportData(SalesReportGetterSetter infotype) {
        db.delete("Sales_Report", null, null);
        ContentValues values = new ContentValues();
        List<SalesReport> data = infotype.getSalesReport();
        try {
            if (data.size() == 0) {
                return false;
            }
            for (int i = 0; i < data.size(); i++) {
                values.put("MTD", data.get(i).getMTD());
                values.put("Sale_Type", data.get(i).getSaleType());
                values.put("Sku_Id", data.get(i).getSkuId());
                values.put("Store_Id", data.get(i).getStoreId());

                long id = db.insert("Sales_Report", null, values);
                if (id == -1) {
                    throw new Exception();
                }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean insertTrainingTypeQuizData(TrainingTypeQuizGetterSetter infotype) {
        db.delete("Training_Type", null, null);
        ContentValues values = new ContentValues();
        List<TrainingType> data = infotype.getTrainingType();
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
            return false;
        }
    }

    public boolean insertStoreWiseData(StorewiseSalesReportGetterSetter infotype) {
        db.delete("Storewise_Sales_Report", null, null);
        ContentValues values = new ContentValues();
        List<StorewiseSalesReport> data = infotype.getStorewiseSalesReport();
        try {
            if (data.size() == 0) {
                return false;
            }
            for (int i = 0; i < data.size(); i++) {
                values.put("Achivement", data.get(i).getAchivement());
                values.put("Srno", data.get(i).getSrno());
                values.put("Store_Id", data.get(i).getStoreId());
                values.put("Time_Period", data.get(i).getTimePeriod());
                values.put("Target", data.get(i).getTarget());
                values.put("Ach_Per", data.get(i).getAchPer());


                long id = db.insert("Storewise_Sales_Report", null, values);
                if (id == -1) {
                    throw new Exception();
                }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean insertStorefocusWiseData(StorewiseFocusSalesReportGetterSetter infotype) {
        db.delete("Storewise_Focus_Sales_Report", null, null);
        ContentValues values = new ContentValues();
        List<StorewiseFocusSalesReport> data = infotype.getStorewiseFocusSalesReport();
        try {
            if (data.size() == 0) {
                return false;
            }
            for (int i = 0; i < data.size(); i++) {
                values.put("Achivement", data.get(i).getAchivement());
                values.put("Srno", data.get(i).getSrno());
                values.put("Store_Id", data.get(i).getStoreId());
                values.put("Time_Period", data.get(i).getTimePeriod());
                values.put("Target", data.get(i).getTarget());
                values.put("Ach_Per", data.get(i).getAchPer());


                long id = db.insert("Storewise_Focus_Sales_Report", null, values);
                if (id == -1) {
                    throw new Exception();
                }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
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
                    sb.setRegionId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Region_Id"))));
                    sb.setStoreTypeId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_Type_Id"))));
                    sb.setReasonId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Reason_Id"))));
                    sb.setUploadStatus(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Upload_Status")));
                    sb.setGeoTag(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Geo_Tag")));
                    sb.setDistributorId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Distributor_Id"))));
                    sb.setCityId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("City_Id"))));
                    sb.setQuizOpen(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Quiz_Open")));
                    sb.setTimePeriod(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Time_Period"))));

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

    @SuppressLint("LongLogTag")
    public boolean isCoverageDataFilled(String visit_date) {
        boolean filled = false;
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * FROM COVERAGE_DATA " + "where " + CommonString.KEY_VISIT_DATE + "<>'" + visit_date + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                int icount = dbcursor.getCount();
                dbcursor.close();
                if (icount > 0) {
                    filled = true;
                } else {
                    filled = false;
                }

            }

        } catch (Exception e) {
            Log.d("Exception when fetching Records!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());
            return filled;
        }

        return filled;
    }

    @SuppressLint("LongLogTag")
    public long InsertCoverageData(CoverageBean data) {
        db.delete(CommonString.TABLE_COVERAGE_DATA, "STORE_ID" + "='" + data.getStoreId() + "' AND VISIT_DATE='" + data.getVisitDate() + "'", null);
        ContentValues values = new ContentValues();
        long l = 0;
        try {
            values.put(CommonString.KEY_STORE_ID, data.getStoreId());
            values.put(CommonString.KEY_USER_ID, data.getUserId());
            values.put(CommonString.KEY_VISIT_DATE, data.getVisitDate());
            values.put(CommonString.KEY_LATITUDE, data.getLatitude());
            values.put(CommonString.KEY_LONGITUDE, data.getLongitude());
            values.put(CommonString.KEY_IMAGE, data.getImage());
            values.put(CommonString.KEY_COVERAGE_REMARK, data.getRemark());
            values.put(CommonString.KEY_REASON_ID, data.getReasonid());
            values.put(CommonString.KEY_REASON, data.getReason());
            values.put(CommonString.KEY_CHECKOUT_IMAGE, data.getCkeckout_image());
            l = db.insert(CommonString.TABLE_COVERAGE_DATA, null, values);
        } catch (Exception ex) {
            Log.d("Database Exception while Insert Closes Data ", ex.toString());
        }
        return l;
    }

    @SuppressLint("LongLogTag")
    public ArrayList<CoverageBean> getSpecificCoverageData(String visitdate, String store_cd) {
        ArrayList<CoverageBean> list = new ArrayList<CoverageBean>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT  * from " + CommonString.TABLE_COVERAGE_DATA + " where " + CommonString.KEY_VISIT_DATE + "='" + visitdate + "' AND " +
                    CommonString.KEY_STORE_ID + "='" + store_cd + "'", null);


            if (dbcursor != null) {

                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    CoverageBean sb = new CoverageBean();
                    sb.setStoreId(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_STORE_ID)));
                    sb.setUserId(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_USER_ID)));
                    sb.setVisitDate(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_VISIT_DATE)));
                    sb.setLatitude(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_LATITUDE)));
                    sb.setLongitude(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_LONGITUDE)));
                    sb.setImage(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_IMAGE)));
                    sb.setReason(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_REASON)));
                    sb.setReasonid(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_REASON_ID)));
                    sb.setMID(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_ID))));
                    sb.setCkeckout_image(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_CHECKOUT_IMAGE)));

                    // sb.setRemark(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_COVERAGE_REMARK)));
                    String value = dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_COVERAGE_REMARK));
                    if (value == null) {
                        sb.setRemark("");
                    } else {
                        sb.setRemark(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_COVERAGE_REMARK)));
                    }
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception when fetching Coverage Data!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());

        }

        return list;

    }

    public long updateJaurneyPlanSpecificStoreStatus(String storeid, String visit_date, String status) {
        long l = 0;
        try {
            ContentValues values = new ContentValues();
            values.put("Upload_Status", status);
            l = db.update("Journey_Plan", values, " Store_Id ='" + storeid + "' AND Visit_Date ='" + visit_date + "'", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return l;
    }

    public ArrayList<JourneyPlan> getSpecificStoreData(String store_cd) {
        ArrayList<JourneyPlan> list = new ArrayList<JourneyPlan>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT  * from Journey_Plan  " + "where Store_Id ='" + store_cd + "'", null);

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
                    sb.setRegionId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Region_Id"))));
                    sb.setStoreTypeId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_Type_Id"))));
                    sb.setReasonId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Reason_Id"))));
                    sb.setUploadStatus(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Upload_Status")));
                    sb.setGeoTag(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Geo_Tag")));
                    sb.setDistributorId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Distributor_Id"))));
                    sb.setCityId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("City_Id"))));
                    sb.setQuizOpen(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Quiz_Open")));
                    sb.setTimePeriod(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Time_Period"))));

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


    public long insertStoreProfileData(
            String user_id, String storeid, String visit_date, StoreProfileGetterSetter save_listDataHeader) {
        db.delete(CommonString.TABLE_STORE_PROFILE_DATA, " STORE_ID" + "='" + storeid + "' AND VISIT_DATE='" + visit_date + "'", null);
        ContentValues values = new ContentValues();
        long l = 0;
        try {
            db.beginTransaction();
            if (!save_listDataHeader.getProfileStoreName().isEmpty()) {
                values.put(CommonString.KEY_STORE_ID, storeid);
                values.put("USER_ID", user_id);
                values.put(CommonString.KEY_VISIT_DATE, visit_date);
                values.put(CommonString.KEY_STORE_PROFILE_STORE_NAME, save_listDataHeader.getProfileStoreName());
                values.put(CommonString.KEY_STORE_PROFILE_STORE_ADDRESS1, save_listDataHeader.getProfileAddress1());
                values.put(CommonString.KEY_STORE_PROFILE_CITY, save_listDataHeader.getProfileCity());
                values.put(CommonString.KEY_STORE_PROFILE_OWNER_NAME, save_listDataHeader.getProfileOwner());
                values.put(CommonString.KEY_STORE_PROFILE_CONTACT_NO, save_listDataHeader.getProfileContact());
                values.put(CommonString.KEY_STORE_PROFILE_DOB, save_listDataHeader.getProfileDOB());
                values.put(CommonString.KEY_STORE_PROFILE_DOA, save_listDataHeader.getProfileDOA());
                values.put(CommonString.KEY_STORE_PROFILE_VISIBILITY_LOCATION1, save_listDataHeader.getProfileVisibilityLocation1());
                values.put(CommonString.KEY_STORE_PROFILE_VISIBILITY_LOCATION2, save_listDataHeader.getProfileVisibilityLocation2());
                values.put(CommonString.KEY_STORE_PROFILE_VISIBILITY_LOCATION3, save_listDataHeader.getProfileVisibilityLocation3());
                values.put(CommonString.KEY_STORE_PROFILE_DIMENTION1, save_listDataHeader.getProfileDimension1());
                values.put(CommonString.KEY_STORE_PROFILE_DIMENTION2, save_listDataHeader.getProfileDimension2());
                values.put(CommonString.KEY_STORE_PROFILE_DIMENTION3, save_listDataHeader.getProfileDimension3());

                l = db.insert(CommonString.TABLE_STORE_PROFILE_DATA, null, values);
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception ex) {
            Log.d("Database Exception", " while Insert Header Data " + ex.toString());
        }
        return l;
    }

    public StoreProfileGetterSetter getStoreProfileData(String store_cd, String visit_date) {
        StoreProfileGetterSetter sb = new StoreProfileGetterSetter();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * FROM STORE_PROFILE_DATA WHERE STORE_ID ='" + store_cd + "' AND VISIT_DATE='" + visit_date + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    sb.setProfileStoreName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("PROFILE_STORE_NAME")));
                    sb.setProfileAddress1((dbcursor.getString(dbcursor.getColumnIndexOrThrow("PROFILE_STORE_ADDRESS_1"))));
                    sb.setProfileCity(dbcursor.getString(dbcursor.getColumnIndexOrThrow("PROFILE_STORE_CITY")));
                    sb.setProfileOwner(dbcursor.getString(dbcursor.getColumnIndexOrThrow("STORE_PROFILE_OWNER")));
                    sb.setProfileContact((dbcursor.getString(dbcursor.getColumnIndexOrThrow("STORE_PROFILE_CONTACT"))));
                    sb.setProfileDOB(dbcursor.getString(dbcursor.getColumnIndexOrThrow("DOB")));
                    sb.setProfileDOA(dbcursor.getString(dbcursor.getColumnIndexOrThrow("DOA")));
                    sb.setProfileVisibilityLocation1(dbcursor.getString(dbcursor.getColumnIndexOrThrow("VISIBILITY_LOCATION1")));
                    sb.setProfileVisibilityLocation2(dbcursor.getString(dbcursor.getColumnIndexOrThrow("VISIBILITY_LOCATION2")));
                    sb.setProfileVisibilityLocation3(dbcursor.getString(dbcursor.getColumnIndexOrThrow("VISIBILITY_LOCATION3")));
                    sb.setProfileDimension1(dbcursor.getString(dbcursor.getColumnIndexOrThrow("DIMENTION1")));
                    sb.setProfileDimension2(dbcursor.getString(dbcursor.getColumnIndexOrThrow("DIMENTION2")));
                    sb.setProfileDimension3(dbcursor.getString(dbcursor.getColumnIndexOrThrow("DIMENTION3")));
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return sb;
            }

        } catch (Exception e) {
            Log.d("Exception get JCP!", e.toString());
            return sb;
        }

        return sb;
    }


    public long InsertRspDetailData(StoreCategoryMaster data, String storeid, String visitdate) {
        db.delete(CommonString.TABLE_INSERT_RSPDETAILS, CommonString.KEY_ID + "='" + data.getKey_id() + "'", null);
        ContentValues values = new ContentValues();
        long id = 0;
        try {
            values.put(CommonString.KEY_STORE_ID, storeid);
            values.put(CommonString.KEY_VISITDATE, visitdate);
            values.put(CommonString.KEY_UNIQUE_ID, data.getRsp_uniqueID());
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

    public ArrayList<StockAvailabilityGetterSetter> getStoreAuditHeaderData() {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<StockAvailabilityGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT  DISTINCT Question_Category , Question_Category_Id from STOCK_OPENINGHEADER_DATA ", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    StockAvailabilityGetterSetter sb = new StockAvailabilityGetterSetter();
                    sb.setBrand(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Question_Category")));
                    sb.setBrand_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Question_Category_Id")));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching ", "opening stock---------------------->Stop<-----------");
        return list;
    }


    public ArrayList<StockAvailabilityGetterSetter> getStockchildData(String region_id, String destributor_id, String brand_id) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<StockAvailabilityGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {

            dbcursor = db.rawQuery("Select distinct sk.Sku_Id, sk.Sku from Mapping_Stock m inner join Sku_Master sk on m.Sku_Id = sk.Sku_Id " +
                    " inner join Brand_Master br on sk.Brand_Id = br.Brand_Id " +
                    " where m.Region_Id = '" + region_id + "' and m.Distributor_Id = '" + destributor_id + "' and br.Brand_Id = '" + brand_id + "' order by sk.Sku", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    StockAvailabilityGetterSetter sb = new StockAvailabilityGetterSetter();
                    sb.setSku_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Sku_Id")));
                    sb.setSku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Sku")));
                    sb.setCurrectanswer("");
                    sb.setCurrectanswerCd("-1");

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching ", "opening stock---------------------->Stop<-----------");
        return list;
    }


    public ArrayList<StockAvailabilityGetterSetter> getastockAnswerData() {
        ArrayList<StockAvailabilityGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("Select -1 as Answer_Id, 'Select' as Answer union Select 1 as Answer_Id, 'Yes' as Answer union Select 0 as Answer_Id, 'No' as Answer", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    StockAvailabilityGetterSetter df = new StockAvailabilityGetterSetter();
                    df.setAnswerId(Integer.valueOf(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Answer_Id"))));
                    df.setAnswer(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Answer")));

                    list.add(df);
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

    public ArrayList<TrainingType> getTrainingData() {
        ArrayList<TrainingType> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("Select * from Training_Type", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    TrainingType df = new TrainingType();
                    df.setTrainingTypeId(Integer.valueOf(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Training_Type_Id"))));
                    df.setTrainingType(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Training_Type")));

                    list.add(df);
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


    public void insertStockData(String storeid,
                                HashMap<StockAvailabilityGetterSetter,
                                        List<StockAvailabilityGetterSetter>> data, List<StockAvailabilityGetterSetter> save_listDataHeader, String category_id) {


        db.delete(CommonString.TABLE_INSERT_STOCK_OPENINGHEADER_DATA, " STORE_CD='" + storeid + "' AND CATEGORY_ID=" + category_id + "", null);
        db.delete(CommonString.TABLE_STORE_STOCK_DATA, " STORE_CD='" + storeid + "' AND CATEGORY_ID=" + category_id + "", null);

        ContentValues values = new ContentValues();
        ContentValues values1 = new ContentValues();

        try {
            db.beginTransaction();
            for (int i = 0; i < save_listDataHeader.size(); i++) {
                values.put("STORE_CD", storeid);
                values1.put("CATEGORY_ID", category_id);
                values.put("BRAND_CD", save_listDataHeader.get(i).getBrand_id());
                values.put("BRAND", save_listDataHeader.get(i).getBrand());
                long l = db.insert(CommonString.TABLE_INSERT_STOCK_OPENINGHEADER_DATA, null, values);
                for (int j = 0; j < data.get(save_listDataHeader.get(i)).size(); j++) {
                    values1.put("Common_Id", (int) l);
                    values1.put("STORE_CD", storeid);
                    values1.put("CATEGORY_ID", category_id);
                    values1.put("BRAND_CD", save_listDataHeader.get(i).getBrand_id());
                    values1.put("BRAND", save_listDataHeader.get(i).getBrand());
                    values1.put("SKU", data.get(save_listDataHeader.get(i)).get(j).getSku());
                    values1.put("SKU_CD", data.get(save_listDataHeader.get(i)).get(j).getSku_id());
                    values1.put("CURRECT_ANSWER", data.get(save_listDataHeader.get(i)).get(j).getCurrectanswer());
                    values1.put("ANSWER_CD", Integer.parseInt(data.get(save_listDataHeader.get(i)).get(j).getCurrectanswerCd()));


                    db.insert(CommonString.TABLE_STORE_STOCK_DATA, null, values1);
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception ex) {
            Log.d("Database Exception", " while Insert Posm Master Data " + ex.toString());
        }
    }

    public ArrayList<StockAvailabilityGetterSetter> getStockInsertedData(String store_cd, String brand_id) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<StockAvailabilityGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT * FROM STORE_STOCK_DATA WHERE STORE_CD ='" + store_cd + "' AND BRAND_CD=" + brand_id + "", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    StockAvailabilityGetterSetter sb = new StockAvailabilityGetterSetter();
                    sb.setSku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU")));
                    sb.setSku_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_CD")));
                    sb.setCurrectanswer(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CURRECT_ANSWER")));
                    sb.setCurrectanswerCd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ANSWER_CD")));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching ", "opening stock---------------------->Stop<-----------");
        return list;
    }


    public ArrayList<StockAvailabilityGetterSetter> getHeaderStockData(String Region_Id, String Distributor_Id, String category_id) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<StockAvailabilityGetterSetter> list = new ArrayList<StockAvailabilityGetterSetter>();
        Cursor dbcursor = null;

        try {

            dbcursor = db.rawQuery("Select distinct br.Brand_Id, br.Brand from Mapping_Stock m inner join Sku_Master sk on m.Sku_Id = sk.Sku_Id " +
                    " inner join Brand_Master br on sk.Brand_Id = br.Brand_Id " +
                    " where m.Region_Id = '" + Region_Id + "' and m.Distributor_Id = '" + Distributor_Id + "' and br.Category_Id = '" + category_id + "'", null);


            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    StockAvailabilityGetterSetter sb = new StockAvailabilityGetterSetter();
                    sb.setBrand_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Brand_Id")));
                    sb.setBrand(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Brand")));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception", " when fetching Header!!!!!!!!!!! " + e.toString());
            return list;
        }
        Log.d("Fetching ", "Header stock---------------------->Stop<-----------");
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

    public ArrayList<PosmMaster> getSofMerchPosmHeaderData(int region_cd, int classification_cd, int store_type_cd) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<PosmMaster> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT DISTINCT PM.Posm_Type_Id ,PM.Posm_Type FROM Mapping_Soft_Posm MP INNER JOIN Posm_Master PM ON " +
                    "MP.Posm_Id=PM.Posm_Id WHERE MP.Region_Id=" + region_cd +
                    " AND MP.Classification_Id=" + classification_cd + " AND MP.Store_Type_Id=" + store_type_cd + " ", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    PosmMaster sb = new PosmMaster();
                    sb.setPosmTypeId(Integer.valueOf(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Posm_Type_Id"))));
                    sb.setPosmType(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Posm_Type")));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching ", "opening stock---------------------->Stop<-----------");
        return list;
    }


    public ArrayList<MappingPermanentPosm> getPemanentMerchPosmHeaderData(int store_cd) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<MappingPermanentPosm> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("select DISTINCT  PM.Posm_Type_Id ,PM.Posm_Type FROM " +
                    "Mapping_Permanent_Posm MP INNER JOIN Posm_Master PM ON " +
                    "MP.Posm_Id=PM.Posm_Id WHERE MP.Store_Id=" + store_cd + " ", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    MappingPermanentPosm sb = new MappingPermanentPosm();
                    sb.setPosmTypeId(Integer.valueOf(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Posm_Type_Id"))));
                    sb.setPosmType(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Posm_Type")));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching ", "opening stock---------------------->Stop<-----------");
        return list;
    }


    public ArrayList<BrandMaster> getbranddataformarketinfo() {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<BrandMaster> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT br.Brand_Id, br.Brand, ca.Category,ca.Category_Id FROM Brand_Master br INNER JOIN Category_Master ca on br.Category_Id = ca.Category_id\n" +
                    "where br.Brand_Id <> 1", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    BrandMaster sb = new BrandMaster();
                    sb.setBrand(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Brand")));
                    sb.setBrandId(Integer.valueOf(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Brand_Id"))));
                    sb.setCategory(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Category")));
                    sb.setCategoryId(Integer.valueOf(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Category_Id"))));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching ", "opening stock---------------------->Stop<-----------");
        return list;
    }

    public ArrayList<SkuMaster> getSkuMasterData() {
        ArrayList<SkuMaster> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT  * FROM Sku_Master", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    SkuMaster sb = new SkuMaster();
                    sb.setSkuId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Sku_Id"))));
                    sb.setSku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Sku")));
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

    @SuppressLint("LongLogTag")
    public boolean isRXTFilled(String storeId) {
        boolean filled = false;

        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT RXT_SKU_CD FROM RXT_DATA WHERE STORE_CD= '" + storeId + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    if (dbcursor.getString(dbcursor.getColumnIndexOrThrow("RXT_SKU_CD")).equals("")) {
                        filled = false;
                        break;
                    } else {
                        filled = true;
                    }

                    dbcursor.moveToNext();
                }
                dbcursor.close();
            }

        } catch (Exception e) {
            Log.d("Exception when fetching Records!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());
            return filled;
        }

        return filled;


    }

    @SuppressLint("LongLogTag")
    public boolean isIPOSFilled(String storeId) {
        boolean filled = false;

        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT SKU_CD FROM IPOS_DATA WHERE STORE_CD= '" + storeId + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    if (dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_CD")).equals("")) {
                        filled = false;
                        break;
                    } else {
                        filled = true;
                    }

                    dbcursor.moveToNext();
                }
                dbcursor.close();
            }

        } catch (Exception e) {
            Log.d("Exception when fetching Records!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());
            return filled;
        }

        return filled;


    }

    @SuppressLint("LongLogTag")
    // isIPOSFilled
    public boolean ismappingSoftPosm(String storeId) {
        boolean filled = false;
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT Posm_Id FROM Mapping_Soft_Posm WHERE STORE_CD= '" + storeId + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    if (dbcursor.getString(dbcursor.getColumnIndexOrThrow("Posm_Id")).equals("")) {
                        filled = false;
                        break;
                    } else {
                        filled = true;
                    }

                    dbcursor.moveToNext();
                }
                dbcursor.close();
            }

        } catch (Exception e) {
            Log.d("Exception when fetching Records!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());
            return filled;
        }

        return filled;


    }

    @SuppressLint("LongLogTag")
    public boolean isMarketInfoFilled(String storeId) {
        boolean filled = false;

        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT BRAND_CD FROM MARKETINFO_DATA WHERE STORE_CD= '" + storeId + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    if (dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND_CD")).equals("")) {
                        filled = false;
                        break;
                    } else {
                        filled = true;
                    }

                    dbcursor.moveToNext();
                }
                dbcursor.close();
            }

        } catch (Exception e) {
            Log.d("Exception when fetching Records!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());
            return filled;
        }

        return filled;


    }

    @SuppressLint("LongLogTag")
    public boolean isVisibilitySoftMerchFilled(String storeId) {
        boolean filled = false;

        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT VISIBILITY_POSM_CD FROM VISIBILITYSOFT_MERCH_DATA WHERE STORE_CD= '" + storeId + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    if (dbcursor.getString(dbcursor.getColumnIndexOrThrow("VISIBILITY_POSM_CD")).equals("")) {
                        filled = false;
                        break;
                    } else {
                        filled = true;
                    }

                    dbcursor.moveToNext();
                }
                dbcursor.close();
            }

        } catch (Exception e) {
            Log.d("Exception when fetching Records!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());
            return filled;
        }

        return filled;


    }

    @SuppressLint("LongLogTag")
    public boolean isVisibilitySPMerchFilled(String storeId) {
        boolean filled = false;

        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT SP_VISIBILITY_POSM_CD FROM VISIBILITYSEMI_PERMANENT_MERCH_DATA WHERE STORE_CD= '" + storeId + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    if (dbcursor.getString(dbcursor.getColumnIndexOrThrow("SP_VISIBILITY_POSM_CD")).equals("")) {
                        filled = false;
                        break;
                    } else {
                        filled = true;
                    }
                    dbcursor.moveToNext();
                }
                dbcursor.close();
            }

        } catch (Exception e) {
            Log.d("Exception when fetching Records!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());
            return filled;
        }

        return filled;
    }

    @SuppressLint("LongLogTag")
    public boolean isStoreAuditFilled(String storeId) {
        boolean filled = false;

        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT ANSWER_CD FROM STORE_STOCK_DATA WHERE STORE_CD= '" + storeId + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    if (dbcursor.getString(dbcursor.getColumnIndexOrThrow("ANSWER_CD")).equals("")) {
                        filled = false;
                        break;
                    } else {
                        filled = true;
                    }
                    dbcursor.moveToNext();
                }
                dbcursor.close();
            }

        } catch (Exception e) {
            Log.d("Exception when fetching Records!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());
            return filled;
        }

        return filled;
    }

    public long updateJaurneyPlanStatus(String storeid, String visit_date, String status) {
        long l = 0;
        try {
            ContentValues values = new ContentValues();
            values.put("Upload_Status", status);
            l = db.update("Journey_Plan", values, " Store_Id ='" + storeid + "' AND Visit_Date ='" + visit_date + "'", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return l;
    }

    public long updateCoverageCheckoutIMG(String storeid, String visit_date, String checkout_img) {
        long l = 0;
        try {
            ContentValues values = new ContentValues();
            values.put(CommonString.KEY_CHECKOUT_IMAGE, checkout_img);
            l = db.update("COVERAGE_DATA", values, " STORE_ID ='" + storeid + "' AND VISIT_DATE ='" + visit_date + "'", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return l;
    }

    @SuppressLint("LongLogTag")
    public ArrayList<NonWorkingReason> getNonWorkingDataByFlag(boolean flag) {
        Log.d("FetchingAssetdata--------------->Start<------------",
                "------------------");
        ArrayList<NonWorkingReason> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * FROM Non_Working_Reason", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    if (flag) {
                        NonWorkingReason sb = new NonWorkingReason();
                        String entry_allow_fortest = dbcursor.getString(dbcursor.getColumnIndexOrThrow("Entry_Allow"));
                        if (entry_allow_fortest.equals("1")) {
                            sb.setReasonId(Integer.valueOf(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Reason_Id"))));
                            sb.setReason(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Reason")));
                            String entry_allow = dbcursor.getString(dbcursor.getColumnIndexOrThrow("Entry_Allow"));
                            if (entry_allow.equals("1")) {
                                sb.setEntryAllow(true);
                            } else {
                                sb.setEntryAllow(false);
                            }
                            String image_allow = dbcursor.getString(dbcursor.getColumnIndexOrThrow("Image_Allow"));
                            if (image_allow.equals("1")) {
                                sb.setImageAllow(true);
                            } else {
                                sb.setImageAllow(false);
                            }
                            String gps_mendtry = dbcursor.getString(dbcursor.getColumnIndexOrThrow("GPS_Mandatory"));
                            if (gps_mendtry.equals("1")) {
                                sb.setGPSMandatory(true);
                            } else {
                                sb.setGPSMandatory(false);
                            }

                            list.add(sb);
                        }


                    } else {
                        NonWorkingReason sb = new NonWorkingReason();
                        sb.setReasonId(Integer.valueOf(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Reason_Id"))));
                        sb.setReason(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Reason")));
                        String entry_allow = dbcursor.getString(dbcursor.getColumnIndexOrThrow("Entry_Allow"));
                        if (entry_allow.equals("1")) {
                            sb.setEntryAllow(true);
                        } else {
                            sb.setEntryAllow(false);
                        }
                        String image_allow = dbcursor.getString(dbcursor.getColumnIndexOrThrow("Image_Allow"));
                        if (image_allow.equals("1")) {
                            sb.setImageAllow(true);
                        } else {
                            sb.setImageAllow(false);
                        }
                        String gps_mendtry = dbcursor.getString(dbcursor.getColumnIndexOrThrow("GPS_Mandatory"));
                        if (gps_mendtry.equals("1")) {
                            sb.setGPSMandatory(true);
                        } else {
                            sb.setGPSMandatory(false);
                        }

                        list.add(sb);
                    }
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception when fetching Non working!!!!!!!!!!!",
                    e.toString());
            return list;
        }

        Log.d("Fetching non working data---------------------->Stop<-----------",
                "-------------------");
        return list;
    }


    @SuppressLint("LongLogTag")
    public ArrayList<CoverageBean> getCoverageData(String visitdate) {
        ArrayList<CoverageBean> list = new ArrayList<CoverageBean>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * FROM " + CommonString.TABLE_COVERAGE_DATA +
                    " WHERE " + CommonString.KEY_VISIT_DATE + "='" + visitdate + "'", null);


            if (dbcursor != null) {

                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    CoverageBean sb = new CoverageBean();
                    sb.setStoreId(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_STORE_ID)));
                    sb.setUserId(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_USER_ID)));
                    sb.setVisitDate(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_VISIT_DATE)));
                    sb.setLatitude(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_LATITUDE)));
                    sb.setLongitude(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_LONGITUDE)));
                    sb.setImage(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_IMAGE)));
                    sb.setReason(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_REASON)));
                    sb.setReasonid(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_REASON_ID)));
                    sb.setMID(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_ID))));
                    sb.setCkeckout_image(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_CHECKOUT_IMAGE)));
                    sb.setRemark(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_COVERAGE_REMARK)));
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception when fetching Coverage Data!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());

        }

        return list;

    }

    public ArrayList<StockAvailabilityGetterSetter> getStockData(String store_cd) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<StockAvailabilityGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT * FROM STORE_STOCK_DATA WHERE STORE_CD ='" + store_cd + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    StockAvailabilityGetterSetter sb = new StockAvailabilityGetterSetter();
                    sb.setSku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU")));
                    sb.setSku_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_CD")));
                    sb.setCurrectanswerCd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ANSWER_CD")));
                    sb.setCategory_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY_ID")));


                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching ", "opening stock---------------------->Stop<-----------");
        return list;
    }


    public ArrayList<JourneyPlan> getSpecificStoreDatawithdate(String visit_date, String store_cd) {
        ArrayList<JourneyPlan> list = new ArrayList<JourneyPlan>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * from Journey_Plan  " +
                    "where Visit_Date ='" + visit_date + "' AND Store_Id='" + store_cd + "'", null);
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
                    sb.setRegionId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Region_Id"))));
                    sb.setStoreTypeId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_Type_Id"))));
                    sb.setReasonId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Reason_Id"))));
                    sb.setUploadStatus(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Upload_Status")));
                    sb.setGeoTag(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Geo_Tag")));
                    sb.setDistributorId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Distributor_Id"))));
                    sb.setCityId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("City_Id"))));
                    sb.setTimePeriod(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Time_Period"))));

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

    @SuppressLint("LongLogTag")
    public ArrayList<CoverageBean> getcoverageDataPrevious(String visitdate) {
        ArrayList<CoverageBean> list = new ArrayList<CoverageBean>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT  * from " +
                    CommonString.TABLE_COVERAGE_DATA + " where " + CommonString.KEY_VISIT_DATE + "<>'" + visitdate + "'", null);


            if (dbcursor != null) {

                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    CoverageBean sb = new CoverageBean();
                    sb.setStoreId(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_STORE_ID)));
                    sb.setUserId(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_USER_ID)));
                    sb.setVisitDate(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_VISIT_DATE)));
                    sb.setLatitude(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_LATITUDE)));
                    sb.setLongitude(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_LONGITUDE)));
                    sb.setImage(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_IMAGE)));
                    sb.setReason(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_REASON)));
                    sb.setReasonid(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_REASON_ID)));
                    sb.setMID(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_ID))));
                    sb.setCkeckout_image(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_CHECKOUT_IMAGE)));
                    //  sb.setRemark(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_COVERAGE_REMARK)));
                    String value = dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_COVERAGE_REMARK));
                    if (value == null) {
                        sb.setRemark("");
                    } else {
                        sb.setRemark(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_COVERAGE_REMARK)));
                    }
                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception when fetching Coverage Data!!!!!!!!!!!!!!!!!!!!!",
                    e.toString());

        }

        return list;

    }

    public JourneyPlan getSpecificStoreDataPrevious(String date, String store_id) {
        JourneyPlan sb = new JourneyPlan();
        Cursor dbcursor = null;

        try {

            dbcursor = db.rawQuery("SELECT * from Journey_Plan  " +
                    "where Visit_Date <> '" + date + "' AND Store_Id='" + store_id + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
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
                    sb.setRegionId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Region_Id"))));
                    sb.setStoreTypeId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_Type_Id"))));
                    sb.setReasonId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Reason_Id"))));
                    sb.setUploadStatus(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Upload_Status")));
                    sb.setGeoTag(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Geo_Tag")));
                    sb.setDistributorId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Distributor_Id"))));
                    sb.setCityId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("City_Id"))));
                    sb.setQuizOpen(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Quiz_Open")));
                    sb.setTimePeriod(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Time_Period"))));
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return sb;
            }

        } catch (Exception e) {
            Log.d("Exception get JCP!", e.toString());
            return sb;
        }

        return sb;
    }


    public void updateStatus(String id, String status) {
        ContentValues values = new ContentValues();
        try {
            values.put("GEO_TAG", status);
            db.update("Journey_Plan", values, CommonString.KEY_STORE_ID + "='" + id + "'", null);
        } catch (Exception ex) {
        }
    }

    public long InsertSTOREgeotag(String storeid, double lat, double longitude, String path, String status) {

        db.delete(CommonString.TABLE_STORE_GEOTAGGING, CommonString.KEY_STORE_ID + "='" + storeid + "'", null);
        ContentValues values = new ContentValues();
        try {
            values.put("STORE_ID", storeid);
            values.put("LATITUDE", Double.toString(lat));
            values.put("LONGITUDE", Double.toString(longitude));
            values.put("FRONT_IMAGE", path);
            values.put("GEO_TAG", status);
            values.put("STATUS", status);

            return db.insert(CommonString.TABLE_STORE_GEOTAGGING, null, values);

        } catch (Exception ex) {
            Log.d("Database Exception ", ex.toString());
            return 0;
        }
    }

    public ArrayList<GeotaggingBeans> getinsertGeotaggingData(String storeid, String status) {
        ArrayList<GeotaggingBeans> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("Select * from " + CommonString.TABLE_STORE_GEOTAGGING + "" +
                    " where " + CommonString.KEY_STORE_ID + " ='" + storeid + "' and " + CommonString.KEY_STATUS + " = '" + status + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    GeotaggingBeans geoTag = new GeotaggingBeans();
                    geoTag.setStoreid(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_STORE_ID)));
                    geoTag.setLatitude(Double.parseDouble(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_LATITUDE))));
                    geoTag.setLongitude(Double.parseDouble(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_LONGITUDE))));
                    geoTag.setImage(dbcursor.getString(dbcursor.getColumnIndexOrThrow("FRONT_IMAGE")));
                    list.add(geoTag);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }

        } catch (Exception e) {
            Log.d("Exception Brands",
                    e.toString());
            return list;
        }
        return list;

    }

    public long updateInsertedGeoTagStatus(String id, String status) {
        ContentValues values = new ContentValues();
        try {
            values.put("GEO_TAG", status);
            values.put("STATUS", status);
            return db.update(CommonString.TABLE_STORE_GEOTAGGING, values, CommonString.KEY_STORE_ID + "='" + id + "'", null);
        } catch (Exception ex) {
            return 0;
        }
    }

    public ArrayList<StockAvailabilityGetterSetter> getStockDone(String store_cd) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<StockAvailabilityGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT * FROM STORE_STOCK_DATA WHERE STORE_CD ='" + store_cd + "' ", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    StockAvailabilityGetterSetter sb = new StockAvailabilityGetterSetter();
                    sb.setSku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU")));
                    sb.setSku_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_CD")));
                    sb.setCurrectanswerCd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ANSWER_CD")));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching ", "opening stock---------------------->Stop<-----------");
        return list;
    }


    public ArrayList<SalesEntryGetterSetter> getSelectCategoryData(String Region_Id, String Distributor_Id) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<SalesEntryGetterSetter> list = new ArrayList<SalesEntryGetterSetter>();
        Cursor dbcursor = null;

        try {

            dbcursor = db.rawQuery("Select distinct cm.Category_Id, cm.Category from Mapping_Stock m inner join Sku_Master sk on m.Sku_Id = sk.Sku_Id " +
                    " inner join Brand_Master br on sk.Brand_Id = br.Brand_Id " +
                    " inner join Category_Master cm on br.Category_Id = cm.Category_Id  " +
                    " where m.Region_Id = '" + Region_Id + "' and m.Distributor_Id = '" + Distributor_Id + "'", null);


            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    SalesEntryGetterSetter sb = new SalesEntryGetterSetter();
                    sb.setCategory_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Category_Id")));
                    sb.setCategory(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Category")));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception", " when fetching Header!!!!!!!!!!! " + e.toString());
            return list;
        }
        Log.d("Fetching ", "Header stock---------------------->Stop<-----------");
        return list;
    }

    public ArrayList<SalesEntryGetterSetter> getHeaderSalesData(String Region_Id, String Distributor_Id, String Category_Id) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<SalesEntryGetterSetter> list = new ArrayList<SalesEntryGetterSetter>();
        Cursor dbcursor = null;

        try {

            dbcursor = db.rawQuery("Select distinct br.Brand_Id, br.Brand from Mapping_Stock m inner join Sku_Master sk on m.Sku_Id = sk.Sku_Id " +
                    " inner join Brand_Master br on sk.Brand_Id = br.Brand_Id " +
                    " where m.Region_Id = '" + Region_Id + "' and m.Distributor_Id = '" + Distributor_Id + "' and br.Category_Id = '" + Category_Id + "'", null);


            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    SalesEntryGetterSetter sb = new SalesEntryGetterSetter();
                    sb.setBrand_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Brand_Id")));
                    sb.setBrand(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Brand")));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception", " when fetching Header!!!!!!!!!!! " + e.toString());
            return list;
        }
        Log.d("Fetching ", "Header stock---------------------->Stop<-----------");
        return list;
    }

    public ArrayList<SalesEntryGetterSetter> getSalesStockchildData(String region_id, String destributor_id, String brand_id) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<SalesEntryGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {

            dbcursor = db.rawQuery("Select distinct sk.Sku_Id, sk.Sku from Mapping_Stock m inner join Sku_Master sk on m.Sku_Id = sk.Sku_Id " +
                    " inner join Brand_Master br on sk.Brand_Id = br.Brand_Id " +
                    " where m.Region_Id = '" + region_id + "' and m.Distributor_Id = '" + destributor_id + "' and br.Brand_Id = '" + brand_id + "'order by sk.Sku", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    SalesEntryGetterSetter sb = new SalesEntryGetterSetter();
                    sb.setSku_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Sku_Id")));
                    sb.setSku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Sku")));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching ", "opening stock---------------------->Stop<-----------");
        return list;
    }

    public ArrayList<SalesEntryGetterSetter> getSalesStockInsertedData(String store_cd, String brand_id) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<SalesEntryGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            //dbcursor = db.rawQuery("SELECT * FROM STORE_SALES_STOCK_DATA WHERE STORE_CD ='" + store_cd + "' AND BRAND_CD=" + brand_id + " AND COMMONID=" + key_id + "", null);
            dbcursor = db.rawQuery("SELECT * FROM STORE_SALES_STOCK_DATA WHERE STORE_CD ='" + store_cd + "' AND BRAND_CD=" + brand_id + "", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    SalesEntryGetterSetter sb = new SalesEntryGetterSetter();
                    sb.setSku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU")));
                    sb.setSku_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_CD")));
                    sb.setStock(dbcursor.getString(dbcursor.getColumnIndexOrThrow("STOCK")));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching ", "opening stock---------------------->Stop<-----------");
        return list;
    }

    public void insertSalesStockData(String storeid, String category_cd,
                                     HashMap<SalesEntryGetterSetter,
                                             List<SalesEntryGetterSetter>> data, List<SalesEntryGetterSetter> save_listDataHeader) {
        db.delete(CommonString.TABLE_INSERT_SALES_STOCK_OPENINGHEADER_DATA, " STORE_CD='" + storeid + "' AND CATEGORY_ID=" + category_cd + "", null);
        db.delete(CommonString.TABLE_STORE_SALES_STOCK_DATA, " STORE_CD='" + storeid + "' AND CATEGORY_ID=" + category_cd + "", null);

        ContentValues values = new ContentValues();
        ContentValues values1 = new ContentValues();

        try {
            db.beginTransaction();
            for (int i = 0; i < save_listDataHeader.size(); i++) {
                values.put("STORE_CD", storeid);
                values.put("BRAND_CD", save_listDataHeader.get(i).getBrand_id());
                values.put("BRAND", save_listDataHeader.get(i).getBrand());
                values.put("CATEGORY_ID", category_cd);
                long l = db.insert(CommonString.TABLE_INSERT_SALES_STOCK_OPENINGHEADER_DATA, null, values);
                for (int j = 0; j < data.get(save_listDataHeader.get(i)).size(); j++) {
                    values1.put("Common_Id", (int) l);
                    values1.put("STORE_CD", storeid);
                    values1.put("BRAND_CD", save_listDataHeader.get(i).getBrand_id());
                    values1.put("BRAND", save_listDataHeader.get(i).getBrand());
                    values1.put("CATEGORY_ID", category_cd);
                    values1.put("SKU", data.get(save_listDataHeader.get(i)).get(j).getSku());
                    values1.put("SKU_CD", data.get(save_listDataHeader.get(i)).get(j).getSku_id());

                    if (!data.get(save_listDataHeader.get(i)).get(j).getStock().equals("")) {
                        values1.put("STOCK", Integer.parseInt(data.get(save_listDataHeader.get(i)).get(j).getStock()));
                    } else {
                        values1.put("STOCK", "0");
                    }
                    db.insert(CommonString.TABLE_STORE_SALES_STOCK_DATA, null, values1);
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception ex) {
            Log.d("Database Exception", " while Insert Posm Master Data " + ex.toString());
        }
    }

    public ArrayList<SalesEntryGetterSetter> getCategoryIdDoneData(String store_cd, String category_id) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<SalesEntryGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            //   dbcursor = db.rawQuery("SELECT * FROM SALES_STOCK_OPENINGHEADER_DATA WHERE STORE_CD ='" + store_cd + "' AND CATEGORY_ID=" + category_id + " AND COMMONID=" + key_id + "", null);
            dbcursor = db.rawQuery("SELECT * FROM SALES_STOCK_OPENINGHEADER_DATA WHERE STORE_CD ='" + store_cd + "' AND CATEGORY_ID=" + category_id + "", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    SalesEntryGetterSetter sb = new SalesEntryGetterSetter();
                    sb.setCategory_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY_ID")));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching ", "opening stock---------------------->Stop<-----------");
        return list;
    }


/*
    public ArrayList<SalesEntryGetterSetter> getSalesStockUploadData(String store_cd) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<SalesEntryGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT * FROM STORE_SALES_STOCK_DATA WHERE STORE_CD ='" + store_cd + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    SalesEntryGetterSetter sb = new SalesEntryGetterSetter();
                    sb.setSku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU")));
                    sb.setSku_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_CD")));
                    sb.setStock(dbcursor.getString(dbcursor.getColumnIndexOrThrow("STOCK")));
                    sb.setCategory_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY_ID")));
                    sb.setCustomer_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CUSTOMER_ID")));
                    sb.setCustomer_name(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CUSTOMER_NAME")));
                    sb.setCard_no(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CARD_NO")));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching ", "opening stock---------------------->Stop<-----------");
        return list;
    }
*/

    public ArrayList<SalesEntryGetterSetter> getSalesStockUploadData_key_id(String store_cd, String key_id) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<SalesEntryGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT * FROM STORE_SALES_STOCK_DATA WHERE STORE_CD =" + store_cd + "  AND COMMONID=" + key_id + "", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    SalesEntryGetterSetter sb = new SalesEntryGetterSetter();
                    sb.setSku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU")));
                    sb.setSku_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_CD")));
                    sb.setStock(dbcursor.getString(dbcursor.getColumnIndexOrThrow("STOCK")));
                    sb.setCategory_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY_ID")));
                    sb.setCustomer_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CUSTOMER_ID")));
                    sb.setCustomer_name(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CUSTOMER_NAME")));
                    sb.setCard_no(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CARD_NO")));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching ", "opening stock---------------------->Stop<-----------");
        return list;
    }

    public long insertSearchSalesData(String storeid, String visit_date, SearchSalesGetterSetter searchSalesGetterSetter) {
        db.delete(CommonString.TABLE_SEARCH_SALES_ENTRY_DATA, " STORE_ID" + "='" + storeid + "' AND VISIT_DATE='" + visit_date + "'", null);
        ContentValues values = new ContentValues();
        long l = 0;
        try {
            db.beginTransaction();
            values.put(CommonString.KEY_STORE_ID, storeid);
            values.put(CommonString.KEY_VISIT_DATE, visit_date);
            values.put(CommonString.KEY_CUSTOMER_NAME, searchSalesGetterSetter.getCustomerName());
            values.put(CommonString.KEY_CARD_NO, searchSalesGetterSetter.getCard_no());
            values.put(CommonString.KEY_PRICE, searchSalesGetterSetter.getPrice());

            l = db.insert(CommonString.TABLE_SEARCH_SALES_ENTRY_DATA, null, values);
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception ex) {
            Log.d("Database Exception", " while Insert Header Data " + ex.toString());
        }
        return l;
    }


    public SearchSalesGetterSetter getStoreDetailsData(String store_id) {

        SearchSalesGetterSetter sb = new SearchSalesGetterSetter();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT * FROM " + CommonString.TABLE_SEARCH_SALES_ENTRY_DATA + " WHERE " + CommonString.KEY_STORE_ID + " = " + store_id, null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    sb.setCustomerName(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_CUSTOMER_NAME)));
                    sb.setCard_no(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_CARD_NO)));
                    sb.setPrice(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_PRICE)));
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return sb;
            }

        } catch (Exception e) {
            return sb;
        }
        return sb;
    }


    public SearchSalesGetterSetter getRetailerListData(String store_id) {

        SearchSalesGetterSetter sb = new SearchSalesGetterSetter();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("select  SUM(S.STOCK) AS SALE_QTY, SUM(S.STOCK*SK.MRP) AS AMOUNT from STORE_SALES_STOCK_DATA S " +
                    "INNER JOIN SKU_MASTER SK ON S.SKU_CD = SK.SKU_ID WHERE S.STORE_CD = " + store_id + "", null);


            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    sb.setSale_qty(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SALE_QTY")));
                    sb.setAmmount(dbcursor.getString(dbcursor.getColumnIndexOrThrow("AMOUNT")));
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return sb;
            }

        } catch (Exception e) {
            return sb;
        }
        return sb;
    }

    @SuppressLint("LongLogTag")
    public boolean isSaleFilled(String storeId) {
        boolean filled = false;
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT STOCK FROM STORE_SALES_STOCK_DATA WHERE STORE_CD= '" + storeId + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    if (dbcursor.getString(dbcursor.getColumnIndexOrThrow("STOCK")).equals("")) {
                        filled = false;
                        break;
                    } else {
                        filled = true;
                    }
                    dbcursor.moveToNext();
                }
                dbcursor.close();
            }

        } catch (Exception e) {
            return filled;
        }
        return filled;
    }


    public long InsertNoSaleData(String store_cd, String visit_date, String no_sale) {
        db.delete(CommonString.TABLE_NO_SALE, "STORE_ID" + "='" + store_cd + "' AND VISIT_DATE='" + visit_date + "'", null);
        ContentValues values = new ContentValues();
        long l = 0;
        try {
            values.put(CommonString.KEY_STORE_ID, store_cd);
            values.put(CommonString.KEY_VISIT_DATE, visit_date);
            values.put(CommonString.KEY_NO_SALE, no_sale);
            l = db.insert(CommonString.TABLE_NO_SALE, null, values);
        } catch (Exception ex) {
            Log.d("Database Exception", ex.toString());
        }
        return l;
    }

    public ArrayList<NoSaleGetterSetter> getNosaleData(String visitdate) {
        ArrayList<NoSaleGetterSetter> list = new ArrayList<NoSaleGetterSetter>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * FROM " + CommonString.TABLE_NO_SALE + " WHERE " + CommonString.KEY_VISIT_DATE + "='" + visitdate + "'", null);

            if (dbcursor != null) {

                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    NoSaleGetterSetter sb = new NoSaleGetterSetter();
                    sb.setStore_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_STORE_ID)));
                    sb.setVisit_date(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_VISIT_DATE)));
                    sb.setNo_sale(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_NO_SALE)));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception when No sale ", e.toString());

        }

        return list;

    }

    public ArrayList<NoSaleGetterSetter> getNoSaleUploadData(String store_cd) {
        ArrayList<NoSaleGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT * FROM NO_SALE_DATA WHERE STORE_ID ='" + store_cd + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    NoSaleGetterSetter sb = new NoSaleGetterSetter();
                    sb.setStore_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_STORE_ID)));
                    sb.setVisit_date(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_VISIT_DATE)));
                    sb.setNo_sale(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_NO_SALE)));

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

    public boolean isNoSaleDataFilled(String storeId) {
        boolean filled = false;
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT NO_SALE FROM NO_SALE_DATA " + "WHERE STORE_ID= '" + storeId + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    if (dbcursor.getString(dbcursor.getColumnIndexOrThrow("NO_SALE")).equals("")) {
                        filled = false;
                        break;
                    } else {
                        filled = true;
                    }
                    dbcursor.moveToNext();
                }
                dbcursor.close();
            }

        } catch (Exception e) {
            Log.d("Exception when fetching", e.toString());
            return filled;
        }

        return filled;


    }

    public ArrayList<SalesEntryGetterSetter> getStockCategoryIdDoneData(String store_cd, String category_id) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<SalesEntryGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT * FROM STORE_STOCK_DATA WHERE STORE_CD ='" + store_cd + "' AND CATEGORY_ID=" + category_id + "", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    SalesEntryGetterSetter sb = new SalesEntryGetterSetter();
                    sb.setCategory_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("" + "ANSWER_CD")));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching ", "opening stock---------------------->Stop<-----------");
        return list;
    }


    public long insertStoreData(SearchStoreDataGetterSetter data, String store_cd, String saveStatus) {

        ContentValues values = new ContentValues();
        long key;

        try {

            values.put(CommonString.KEY_PHONENO, data.getMobile());
            values.put(CommonString.KEY_CARD_NO, data.getCard_no());
            values.put(CommonString.KEY_VISIT_DATE, data.getVisit_date());
            values.put(CommonString.KEY_SAVE_DATA_STATUS, saveStatus);
            values.put(CommonString.KEY_USERNAME, data.getCustomerName());
            values.put(CommonString.KEY_STORE_ID, store_cd);
            key = db.insert(CommonString.TABLE_SEARCH_STORE_DATA, null, values);

        } catch (Exception ex) {
            Log.d("DB Excep in Insert", ex.toString());
            return 0;
        }
        return key;
    }


    public void updateSaveDataStatusMccain(String key_id, String status) {
        try {
            ContentValues values = new ContentValues();
            values.put(CommonString.KEY_SAVE_DATA_STATUS, status);

            //Store Data
            db.update(CommonString.TABLE_SEARCH_STORE_DATA, values,
                    CommonString.KEY_ID + "=" + key_id, null);


            db.update(CommonString.TABLE_INSERT_SALES_STOCK_OPENINGHEADER_DATA, values,
                    "COMMONID =" + key_id, null);

            db.update(CommonString.TABLE_STORE_SALES_STOCK_DATA, values,
                    "COMMONID =" + key_id, null);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void deleteUnSaveData(String saveDataStatus) {
        try {
            db.delete(CommonString.TABLE_SEARCH_STORE_DATA,
                    CommonString.KEY_SAVE_DATA_STATUS + "<>'" + saveDataStatus + "'", null);

            db.delete(CommonString.TABLE_INSERT_SALES_STOCK_OPENINGHEADER_DATA,
                    CommonString.KEY_SAVE_DATA_STATUS + "<>'" + saveDataStatus + "'", null);

            db.delete(CommonString.TABLE_STORE_SALES_STOCK_DATA,
                    CommonString.KEY_SAVE_DATA_STATUS + "<>'" + saveDataStatus + "'", null);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public ArrayList<SearchStoreDataGetterSetter> getStoreSavedData(String saveDataStatus, String store_cd) {
        Log.d("FetchStoreType>Start<--", "----");
        ArrayList<SearchStoreDataGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT * from SEARCH_STORE_DATA where " + CommonString.KEY_SAVE_DATA_STATUS + " ='" + saveDataStatus + "' AND STORE_ID ='" + store_cd + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    SearchStoreDataGetterSetter df = new SearchStoreDataGetterSetter();

                    df.setKey_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_ID)));
                    df.setCard_no(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_CARD_NO)));
                    df.setMobile(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_PHONENO)));
                    df.setVisit_date(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_VISIT_DATE)));
                    df.setSAVE_DATA_STATUS(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_SAVE_DATA_STATUS)));
                    df.setUser_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_USERNAME)));
                    list.add(df);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Ex fetching Posm!", e.toString());
            return list;
        }

        Log.d("Fetcstore data->Stop<-", "-");
        return list;
    }

    public SearchStoreDataGetterSetter getStoreSavedData1(String store_cd, String key_id) {
        Log.d("FetchStoreType>Start<--", "----");
        Cursor dbcursor = null;
        SearchStoreDataGetterSetter df = null;

        try {

            dbcursor = db.rawQuery("select ST.ID,ST.USERNAME, ST.CARD_NO, SUM(S.STOCK) AS SALE_QTY, SUM(S.STOCK*SK.MRP) AS AMOUNT from STORE_SALES_STOCK_DATA S " +
                    "INNER JOIN SKU_MASTER SK ON S.SKU_CD = SK.SKU_ID " +
                    "INNER JOIN SEARCH_STORE_DATA ST ON S.COMMONID = ST.ID " +
                    " WHERE S.STORE_CD = " + store_cd + " AND COMMONID=" + key_id + "", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    df = new SearchStoreDataGetterSetter();
                    df.setKey_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_ID)));
                    df.setCard_no(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CARD_NO")));
                    df.setUser_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("USERNAME")));
                    df.setSale_qty(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SALE_QTY")));
                    df.setAmmount(dbcursor.getString(dbcursor.getColumnIndexOrThrow("AMOUNT")));

                    dbcursor.moveToNext();
                }
                dbcursor.close();

            }
            return df;
        } catch (Exception e) {
            Log.d("Ex fetching Posm!", e.toString());
            return df;
        }


    }


    public ArrayList<SearchStoreDataGetterSetter> getSearchStoreDataUpload() {
        Log.d("FetchStoreType>Start<--", "----");
        ArrayList<SearchStoreDataGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT * from STORE_DATA ", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    SearchStoreDataGetterSetter df = new SearchStoreDataGetterSetter();


                    df.setKey_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_ID)));
                    df.setCard_no(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_CARD_NO)));
                    df.setMobile(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_PHONENO)));
                    df.setVisit_date(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_VISIT_DATE)));
                    df.setSAVE_DATA_STATUS(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_SAVE_DATA_STATUS)));
                    df.setUser_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_USERNAME)));
                    list.add(df);
                    dbcursor.moveToNext();

                    list.add(df);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Ex fetching Posm!", e.toString());
            return list;
        }

        Log.d("Fetcstore data->Stop<-", "-");
        return list;
    }

    public ArrayList<SalesEntryGetterSetter> getSalesStockUploadKey_idData(String store_cd, String key_id) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<SalesEntryGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT * FROM STORE_SALES_STOCK_DATA WHERE STORE_CD ='" + store_cd + "' AND COMMONID=" + key_id + "", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    SalesEntryGetterSetter sb = new SalesEntryGetterSetter();
                    sb.setSku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU")));
                    sb.setSku_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_CD")));
                    sb.setStock(dbcursor.getString(dbcursor.getColumnIndexOrThrow("STOCK")));
                    sb.setCategory_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY_ID")));
                    sb.setCustomer_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CUSTOMER_ID")));

                    list.add(sb);
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return list;
            }
        } catch (Exception e) {
            Log.d("Exception ", "when fetching opening stock!!!!!!!!!!!" + e.toString());
            return list;
        }

        Log.d("Fetching ", "opening stock---------------------->Stop<-----------");
        return list;
    }

    public void deleteSpecificSalesMData(String common_id) {
        db.delete(CommonString.TABLE_INSERT_SALES_STOCK_OPENINGHEADER_DATA, CommonString.COMMONID + "='" + common_id + "'", null);
        db.delete(CommonString.KEY_SAVE_DATA_STATUS, CommonString.COMMONID + "='" + common_id + "'", null);


    }

    public ArrayList<SearchStoreDataGetterSetter> getStoreSavedDataforUpload(String store_cd) {
        Log.d("FetchStoreType>Start<--", "----");
        Cursor dbcursor = null;
        ArrayList<SearchStoreDataGetterSetter> list = new ArrayList<>();

        try {

            dbcursor = db.rawQuery("SELECT * from SEARCH_STORE_DATA WHERE STORE_ID ='" + store_cd + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    SearchStoreDataGetterSetter df = new SearchStoreDataGetterSetter();
                    df.setKey_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_ID)));
                    df.setCard_no(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CARD_NO")));
                    df.setUser_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("USERNAME")));
                    list.add(df);
                    dbcursor.moveToNext();
                }
                dbcursor.close();

            }
            return list;
        } catch (Exception e) {
            Log.d("Ex fetching Posm!", e.toString());
            return list;
        }


    }


    public ArrayList<SalesEntryGetterSetter> getSalesDone(String store_cd) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<SalesEntryGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT * FROM STORE_SALES_STOCK_DATA WHERE STORE_CD ='" + store_cd + "' ", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    SalesEntryGetterSetter sb = new SalesEntryGetterSetter();
                    sb.setSku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU")));
                    sb.setSku_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_CD")));
                    sb.setStock(dbcursor.getString(dbcursor.getColumnIndexOrThrow("STOCK")));

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


    public boolean isSalesFilled(String storeId) {
        boolean filled = false;

        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT STOCK FROM STORE_SALES_STOCK_DATA WHERE STORE_CD= '" + storeId + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    if (dbcursor.getString(dbcursor.getColumnIndexOrThrow("STOCK")).equals("")) {
                        filled = false;
                        break;
                    } else {
                        filled = true;
                    }
                    dbcursor.moveToNext();
                }
                dbcursor.close();
            }

        } catch (Exception e) {
            return filled;
        }
        return filled;
    }

    public boolean isQuizFilled(String storeId) {
        boolean filled = false;

        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT QUESTION_ID FROM STORE_QUIZ_DATA WHERE STORE_CD= '" + storeId + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    if (dbcursor.getString(dbcursor.getColumnIndexOrThrow("QUESTION_ID")).equals("")) {
                        filled = false;
                        break;
                    } else {
                        filled = true;
                    }
                    dbcursor.moveToNext();
                }
                dbcursor.close();
            }

        } catch (Exception e) {
            return filled;
        }
        return filled;
    }


    public void insertQuizData(String storeid,
                               HashMap<QuizGetterSetter,
                                       List<QuizGetterSetter>> data, List<QuizGetterSetter> save_listDataHeader) {

        db.delete(CommonString.TABLE_INSERT_QUIZ_OPENINGHEADER_DATA, CommonString.KEY_STORE_CD + "='" + storeid + "'", null);
        db.delete(CommonString.TABLE_STORE_QUIZ_DATA, CommonString.KEY_STORE_CD + "='" + storeid + "'", null);
        ContentValues values = new ContentValues();
        ContentValues values1 = new ContentValues();

        try {
            db.beginTransaction();
            for (int i = 0; i < save_listDataHeader.size(); i++) {
                values.put("STORE_CD", storeid);
                values.put("BRAND_CD", save_listDataHeader.get(i).getBrand_id());
                values.put("BRAND", save_listDataHeader.get(i).getBrand());
                long l = db.insert(CommonString.TABLE_INSERT_QUIZ_OPENINGHEADER_DATA, null, values);
                for (int j = 0; j < data.get(save_listDataHeader.get(i)).size(); j++) {
                    values1.put("Common_Id", (int) l);
                    values1.put("STORE_CD", storeid);
                    values1.put("BRAND_CD", save_listDataHeader.get(i).getBrand_id());
                    values1.put("BRAND", save_listDataHeader.get(i).getBrand());
                    values1.put("QUESTION", data.get(save_listDataHeader.get(i)).get(j).getQuestion());
                    values1.put("QUESTION_ID", data.get(save_listDataHeader.get(i)).get(j).getQuestion_id());
                    values1.put("CURRECT_ANSWER", data.get(save_listDataHeader.get(i)).get(j).getCurrectanswer());
                    // values1.put("ANSWER_CD", Integer.parseInt(data.get(save_listDataHeader.get(i)).get(j).getCurrectanswerCd()));
                    values1.put("ANSWER_CD", data.get(save_listDataHeader.get(i)).get(j).getCurrectanswerCd());
                    values1.put("RIGHT_ANSWER", String.valueOf(data.get(save_listDataHeader.get(i)).get(j).getRight_Answer()));

                    db.insert(CommonString.TABLE_STORE_QUIZ_DATA, null, values1);
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception ex) {
            Log.d("Database Exception", " while Insert Posm Master Data " + ex.toString());
        }
    }

    public ArrayList<QuizGetterSetter> getHeaderQuizData() {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<QuizGetterSetter> list = new ArrayList<QuizGetterSetter>();
        Cursor dbcursor = null;

        try {

            dbcursor = db.rawQuery("select DISTINCT  Q.Brand_Id, Q.Brand FROM Quiz_Question Q ", null);


            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    QuizGetterSetter sb = new QuizGetterSetter();
                    sb.setBrand_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Brand_Id")));
                    sb.setBrand(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Brand")));

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

    public ArrayList<QuizGetterSetter> getQuizInsertedData(String store_cd, String brand_id) {
        ArrayList<QuizGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT * FROM STORE_QUIZ_DATA WHERE STORE_CD ='" + store_cd + "' AND BRAND_CD=" + brand_id + "", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    QuizGetterSetter sb = new QuizGetterSetter();
                    sb.setQuestion(dbcursor.getString(dbcursor.getColumnIndexOrThrow("QUESTION")));
                    sb.setQuestion_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("QUESTION_ID")));
                    sb.setCurrectanswer(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CURRECT_ANSWER")));
                    sb.setCurrectanswerCd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ANSWER_CD")));
                    sb.setRight_Answer(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("RIGHT_ANSWER")));


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

    public ArrayList<QuizGetterSetter> getQuizchildData(String brand_id) {
        ArrayList<QuizGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("select DISTINCT  Q.Question_Id, Q.Question FROM Quiz_Question Q " +
                    " where Q.Brand_Id = '" + brand_id + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    QuizGetterSetter sb = new QuizGetterSetter();
                    sb.setQuestion_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Question_Id")));
                    sb.setQuestion(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Question")));
                    sb.setCurrectanswer("");
                    sb.setCurrectanswerCd("-1");

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


    public ArrayList<QuizGetterSetter> getQuizAnswerData(String question_id, String select) {
        ArrayList<QuizGetterSetter> list = new ArrayList<>();

        QuizGetterSetter sb1 = new QuizGetterSetter();
        sb1.setAnswerId(0);
        sb1.setRight_Answer(0);
        sb1.setAnswer(select);
        list.add(0, sb1);
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("select DISTINCT  Q.Answer_Id, Q.Answer, Q.Right_Answer FROM Quiz_Question Q " +
                    " where Question_Id = " + question_id + "", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    QuizGetterSetter df = new QuizGetterSetter();
                    df.setAnswerId(Integer.valueOf(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Answer_Id"))));
                    df.setAnswer(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Answer")));
                    df.setRight_Answer(Integer.valueOf(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Right_Answer"))));
                    df.setCurrectanswerCd("-1");

                    list.add(df);
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


    public ArrayList<QuizGetterSetter> getQuizUploadData(String store_cd) {
        ArrayList<QuizGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            // dbcursor = db.rawQuery("SELECT * FROM STORE_QUIZ_DATA WHERE STORE_CD ='" + store_cd + "'", null);
            dbcursor = db.rawQuery("SELECT * FROM STORE_QUIZ_DATA WHERE STORE_CD ='" + store_cd + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    QuizGetterSetter sb = new QuizGetterSetter();
                    sb.setQuestion(dbcursor.getString(dbcursor.getColumnIndexOrThrow("QUESTION")));
                    sb.setQuestion_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("QUESTION_ID")));
                    sb.setCurrectanswer(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CURRECT_ANSWER")));
                    sb.setCurrectanswerCd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ANSWER_CD")));
                    sb.setRight_Answer(dbcursor.getInt(dbcursor.getColumnIndexOrThrow("RIGHT_ANSWER")));
                    sb.setBrand_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND_CD")));

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

    public ArrayList<SalesEntryGetterSetter> getSalesStockUploadData_(String store_cd) {
        Log.d("Fetching", "Storedata--------------->Start<------------");
        ArrayList<SalesEntryGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT * FROM STORE_SALES_STOCK_DATA WHERE STORE_CD ='" + store_cd + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    SalesEntryGetterSetter sb = new SalesEntryGetterSetter();
                    sb.setSku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU")));
                    sb.setSku_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_CD")));
                    sb.setStock(dbcursor.getString(dbcursor.getColumnIndexOrThrow("STOCK")));
                    sb.setCategory_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CATEGORY_ID")));

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


    public ArrayList<SaleReportsGetterSetter> getReportData(String store_cd) {
        ArrayList<SaleReportsGetterSetter> list = new ArrayList<SaleReportsGetterSetter>();
        Cursor dbcursor = null;

        try {

            dbcursor = db.rawQuery("select t1.Sku_Id as Sku_Id, t1.sku as Sku,  t1.Sale_Type as Sale_Type,  t1.MTD as MTD,  t2.FTD as FTD, (t1.MTD+ t2.FTD) as Total  from " +
                    " (select * from Sales_Report a inner join sku_master sk on a.sku_id = sk.sku_id) as  t1 " +
                    " Inner join " +
                    " (Select * from " +
                    " (Select  'Volume' as Sale_Type,  d.Store_cd, d.Sku_Cd, sk.Sku,  sum(Stock) as FTD  from STORE_SALES_STOCK_DATA d  Inner join Sku_Master sk on d.Sku_cd = sk.Sku_Id  Where  d.Store_Cd ='" + store_cd + "' " +
                    " group by d.Sku_cd, d.Store_cd, sk.Sku " +
                    " union " +
                    " Select  'Value' as Sale_Type, d.Store_cd,  d.Sku_Cd,  sk.Sku, sum(d.Stock*sk.MRP) as FTD  from STORE_SALES_STOCK_DATA d Inner join Sku_Master sk " +
                    " on  d.Sku_Cd = sk.Sku_Id Where  Store_Cd ='" + store_cd + "'" +
                    " group by d.Sku_cd, d.Store_Cd, sk.Sku) as t1) as t2 " +
                    " on t1.Store_Id = t2.Store_cd " +
                    " and t1.Sku_Id = t2.Sku_cd " +
                    " and t1.Sale_Type = t2.Sale_Type ", null);


            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    SaleReportsGetterSetter sb = new SaleReportsGetterSetter();
                    sb.setSku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Sku")));
                    sb.setMtd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("MTD")));
                    sb.setTotal(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Total")));
                    sb.setFtd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("FTD")));
                    sb.setValue(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Sale_Type")));

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


    public ArrayList<SaleReportsGetterSetter> getHeaderSalesReportData(String store_id) {

        ArrayList<SaleReportsGetterSetter> list = new ArrayList<SaleReportsGetterSetter>();
        Cursor dbcursor = null;

        try {

            dbcursor = db.rawQuery(" Select distinct SK.Sku_Id,SK.Sku from Sales_Report SR " +
                    " INNER JOIN SKU_MASTER SK ON SR.Sku_Id = SK.Sku_Id " +
                    " where SR.Store_Id = '" + store_id + "'", null);


            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    SaleReportsGetterSetter sb = new SaleReportsGetterSetter();
                    sb.setSku_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Sku_Id")));
                    sb.setSku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Sku")));

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

    public ArrayList<SaleReportsGetterSetter> getHeaderSalesInsertReportData(String store_id) {

        ArrayList<SaleReportsGetterSetter> list = new ArrayList<SaleReportsGetterSetter>();
        Cursor dbcursor = null;

        try {

            dbcursor = db.rawQuery(" Select distinct SK.SKU_CD,SK.SKU from STORE_SALES_STOCK_DATA SK " +
                    " where SK.STORE_CD = '" + store_id + "'", null);


            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    SaleReportsGetterSetter sb = new SaleReportsGetterSetter();
                    sb.setSku_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU_CD")));
                    sb.setSku(dbcursor.getString(dbcursor.getColumnIndexOrThrow("SKU")));

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


    public ArrayList<SaleReportsGetterSetter> getSalesReportTotalData(String store_cd, String sku_id) {
        ArrayList<SaleReportsGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {

            dbcursor = db.rawQuery("select t1.Sku_Id as Sku_Id, t1.sku as Sku,  t1.Sale_Type as Sale_Type,  t1.MTD as MTD,  t2.FTD as FTD, (t1.MTD+ t2.FTD) as Total  from " +
                    " (select * from Sales_Report a inner join sku_master sk on a.sku_id = sk.sku_id) as  t1 " +
                    " Inner join " +
                    " (Select * from " +
                    " (Select  'Volume' as Sale_Type,  d.Store_cd, d.Sku_Cd, sk.Sku,  sum(Stock) as FTD  from STORE_SALES_STOCK_DATA d  Inner join Sku_Master sk on d.Sku_cd = sk.Sku_Id  Where  d.Store_Cd ='" + store_cd + "' " +
                    " group by d.Sku_cd, d.Store_cd, sk.Sku " +
                    " union " +
                    " Select  'Value' as Sale_Type, d.Store_cd,  d.Sku_Cd,  sk.Sku, sum(d.Stock*sk.MRP) as FTD  from STORE_SALES_STOCK_DATA d Inner join Sku_Master sk " +
                    " on  d.Sku_Cd = sk.Sku_Id Where  Store_Cd ='" + store_cd + "'" +
                    " group by d.Sku_cd, d.Store_Cd, sk.Sku) as t1) as t2 " +
                    " on t1.Store_Id = t2.Store_cd " +
                    " and t1.Sku_Id = t2.Sku_cd " +
                    " and t1.Sale_Type = t2.Sale_Type and t1.Sku_id  ='" + sku_id + "'", null);


            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    SaleReportsGetterSetter sb = new SaleReportsGetterSetter();
                    sb.setValue(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Sale_Type")));
                    sb.setMtd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("MTD")));
                    sb.setFtd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("FTD")));
                    sb.setTotal(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Total")));

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

    public ArrayList<SaleReportsGetterSetter> getSalesReportInsertTotalData(String store_cd, String sku_id) {
        ArrayList<SaleReportsGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;

        try {

            dbcursor = db.rawQuery("Select sku_Id, Sku, Sale_Type, 0.0 as MTD, FTD, FTD as Total  from " +
                    " (Select sk.Sku_Id,  sk.Sku, 'Value' as Sale_Type,  sum(ifnull(d.Stock,0)*sk.MRP) as FTD from STORE_SALES_STOCK_DATA d inner join Sku_Master sk on d.sku_cd = sk.sku_Id " +
                    " where d.Store_cd ='" + store_cd + "' and sk.Sku_Id ='" + sku_id + "' group by sk.Sku, sk.Sku_Id " +
                    " union " +
                    " Select sk.Sku_Id, sk.Sku,  'Volume' as Sale_Type, sum(ifnull(d.Stock,0)) as FTD from STORE_SALES_STOCK_DATA d inner join Sku_Master sk on d.sku_cd = sk.sku_Id " +
                    " where d.Store_cd ='" + store_cd + "' and sk.Sku_Id ='" + sku_id + "' group by sk.Sku, sk.Sku_Id) as t", null);


           /* Select sku_Id, Sku, Sale_Type, 0.0 as MTD, FTD, FTD as Total  from
                    (Select sk.Sku_Id,  sk.Sku, 'Value' as Sale_Type,  sum(ifnull(d.Stock,0)*sk.MRP) as FTD from STORE_SALES_STOCK_DATA d inner join Sku_Master sk on d.sku_cd = sk.sku_Id
                            where d.Store_cd = 9 and sk.Sku_Id = 168 group by sk.Sku, sk.Sku_Id
                            Union
                            Select sk.Sku_Id, sk.Sku,  'Volume' as Sale_Type, sum(ifnull(d.Stock,0)) as FTD from STORE_SALES_STOCK_DATA d inner join Sku_Master sk on d.sku_cd = sk.sku_Id
                            where d.Store_cd = 9 and sk.Sku_Id = 168 group by sk.Sku, sk.Sku_Id) as t*/


            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    SaleReportsGetterSetter sb = new SaleReportsGetterSetter();
                    sb.setValue(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Sale_Type")));
                    sb.setMtd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("MTD")));
                    sb.setFtd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("FTD")));
                    sb.setTotal(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Total")));

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


    public SaleReportsGetterSetter getSaleData1() {
        Cursor dbcursor = null;
        SaleReportsGetterSetter sb = new SaleReportsGetterSetter();
        try {
            dbcursor = db.rawQuery(" Select a.Store_cd, b.MTD,a.FTD, a.FTD+b.MTD as Total from" +
                    "(select store_cd, SUM(STOCK) as FTD from  STORE_SALES_STOCK_DATA Group By Store_Cd) a inner join" +
                    "(Select store_id as store_cd, sum(MTD) as MTD from Sales_Report where lower(Sale_Type) = 'volume' group by Store_id) b on a.Store_cd=b.Store_cd", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    sb.setMtd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("MTD")));
                    sb.setFtd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("FTD")));
                    sb.setTotal(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Total")));
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return sb;
            }

        } catch (Exception e) {
            Log.d("Exception get JCP!", e.toString());
            return sb;
        }
        return sb;
    }


    public SaleReportsGetterSetter getSaleData2() {
        Cursor dbcursor = null;
        SaleReportsGetterSetter sb = new SaleReportsGetterSetter();
        try {
            dbcursor = db.rawQuery("Select a.Store_cd, b.ValueMTD,a.ValueFTD, a.ValueFTD+b.ValueMTD as TotalValue from " +
                    "(Select Store_cd, SUM(ValueMRP) as ValueFTD from " +
                    "(select store_cd,  SUM(STOCK)* MRP as ValueMRP from  STORE_SALES_STOCK_DATA sd inner join sku_master sm on sd.SKU_CD=sm.SKU_ID Group By sd.Store_Cd, sd.SKU_CD)) a" +
                    " inner join (Select store_id as store_cd, sum(MTD) as ValueMTD from Sales_Report where lower(Sale_Type)= 'value' group by Store_id) b on a.Store_cd=b.Store_cd", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    sb.setMtd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ValueMTD")));
                    sb.setFtd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("ValueFTD")));
                    sb.setTotal(dbcursor.getString(dbcursor.getColumnIndexOrThrow("TotalValue")));
                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return sb;
            }

        } catch (Exception e) {
            Log.d("Exception get JCP!", e.toString());
            return sb;
        }
        return sb;
    }

    public long insertTrainingQuizData(String user_name, String visit_date, String store_id, String trainingData, String trainingId, String traing) {
        db.delete(CommonString.TABLE_INSERT_QUIZ_DATA, "STORE_ID='" + store_id + "'", null);
        long l = 0;
        ContentValues values = new ContentValues();
        try {
            values.put("STORE_ID", store_id);
            values.put("VISIT_DATE", visit_date);
            values.put("TRAINING_DATE", trainingData);
            values.put("TRAINING_ID", trainingId);
            values.put("TRAINING", traing);
            values.put("USER_ID", user_name);

            l = db.insert(CommonString.TABLE_INSERT_QUIZ_DATA, null, values);

        } catch (Exception ex) {
            Log.d("Database Exception ", ex.toString());
        }
        return l;
    }


    public TrainingQuizGetterSetter getTraingTypeData(String storeId) {
        TrainingQuizGetterSetter sb = new TrainingQuizGetterSetter();
        Cursor dbcursor = null;
        try {

            dbcursor = db.rawQuery("Select * from " + CommonString.TABLE_INSERT_QUIZ_DATA + "" + " where " + CommonString.KEY_STORE_ID + " = '" + storeId + "'", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    sb.setStoreId(dbcursor.getString(dbcursor.getColumnIndexOrThrow("STORE_ID")));
                    sb.setTraining(dbcursor.getString(dbcursor.getColumnIndexOrThrow("TRAINING")));
                    sb.setTrainingId(dbcursor.getString(dbcursor.getColumnIndexOrThrow("TRAINING_ID")));
                    sb.setTrainingDate(dbcursor.getString(dbcursor.getColumnIndexOrThrow("TRAINING_DATE")));
                    sb.setUser_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("USER_ID")));
                    sb.setVisit_date(dbcursor.getString(dbcursor.getColumnIndexOrThrow("VISIT_DATE")));

                    dbcursor.moveToNext();
                }
                dbcursor.close();
                return sb;
            }

        } catch (Exception e) {

            return sb;
        }

        return sb;
    }


    public ArrayList<StorewiseSalesReport> getPerformanceDataStorewiseReport() {
        ArrayList<StorewiseSalesReport> list = new ArrayList<StorewiseSalesReport>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * FROM Storewise_Sales_Report order by Srno", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    StorewiseSalesReport sb = new StorewiseSalesReport();
                    sb.setSrno(Integer.valueOf(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Srno"))));
                    sb.setStoreId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_Id"))));
                    sb.setAchivement(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Achivement"))));
                    sb.setTimePeriod(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Time_Period")));
                    sb.setAchPer(Double.valueOf(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Ach_Per"))));
                    sb.setTarget(Double.valueOf(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Target"))));

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

    public ArrayList<StorewiseFocusSalesReport> getStorePerformanceDataStorewiseFocusReport() {
        ArrayList<StorewiseFocusSalesReport> list = new ArrayList<StorewiseFocusSalesReport>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("SELECT * FROM Storewise_Focus_Sales_Report order by Srno", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    StorewiseFocusSalesReport sb = new StorewiseFocusSalesReport();
                    sb.setSrno(Integer.valueOf(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Srno"))));
                    sb.setStoreId(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Store_Id"))));
                    sb.setAchivement(Integer.parseInt(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Achivement"))));
                    sb.setTimePeriod(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Time_Period")));
                    sb.setAchPer(Double.valueOf(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Ach_Per"))));
                    sb.setTarget(Double.valueOf(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Target"))));

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

    public ArrayList<BrandMaster> getCustomer_data() {

        ArrayList<BrandMaster> list = new ArrayList<BrandMaster>();
        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT * from Brand_Master", null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    BrandMaster pgs = new BrandMaster();

                    pgs.setBrand(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Brand")));
                    pgs.setBrandId(Integer.valueOf(dbcursor.getString(dbcursor.getColumnIndexOrThrow("Brand_Id"))));
                    list.add(pgs);
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

    public ArrayList<BrandMaster> getInsertCustomerPoData() {
        ArrayList<BrandMaster> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {

            //  dbcursor = db.rawQuery("SELECT * FROM " + CommonString.TABLE_INSERT_CUSTOMER_POP + " WHERE " + CommonString.KEY_COMMON_ID + " = " + key_id, null);
            //  dbcursor = db.rawQuery("SELECT * FROM " + CommonString.TABLE_INSERT_CUSTOMER_POP + " WHERE " + CommonString.KEY_COMMON_ID + " = " + key_id, null);

            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    BrandMaster sb = new BrandMaster();

                    sb.setBrand(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_BRAND_NAME)));
                    sb.setBrandId(Integer.valueOf(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_BRAND_ID))));
                    sb.setStock_liens(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_CUSTOMER_QTY)));
                    sb.setKey_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow(CommonString.KEY_COMMON_ID)));

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

    public long InsertFeedbackData(ArrayList<BrandMaster> data, String date, String store_id, String key_id) {

        db.delete(CommonString.TABLE_INSERT_CUSTOMER_POP, CommonString.KEY_STORE_ID + "='" + store_id + "' and " + CommonString.KEY_COMMON_ID + " = '" + key_id + "'", null);

        ContentValues values = new ContentValues();
        long id = 0;
        try {
            for (int i = 0; i < data.size(); i++) {
                values.put(CommonString.KEY_COMMON_ID, key_id);
                values.put(CommonString.KEY_BRAND_ID, data.get(i).getBrandId());
                values.put(CommonString.KEY_BRAND, data.get(i).getBrand());
                values.put(CommonString.KEY_VISIT_DATE, date);
                values.put(CommonString.KEY_CUSTOMER_QTY, data.get(i).getStock_liens());

                id = db.insert(CommonString.TABLE_INSERT_CUSTOMER_POP, null, values);
            }

            return id;
        } catch (Exception ex) {
            return -1;
        }

    }

    public void removesampledata(String user_id) {
        db.execSQL("DELETE FROM CUSTOMER_DATA WHERE KEY_ID = '" + user_id + "'");
        db.execSQL("DELETE FROM CUSTOMER_POPUP_DATA WHERE KEY_ID = '" + user_id + "'");
    }

    public void removealSamplingData(String store_id) {
        db.delete(CommonString.TABLE_INSERT_CUSTOMER_DATA, CommonString.KEY_STORE_ID + "='" + store_id + "'", null);
        db.delete(CommonString.TABLE_INSERT_CUSTOMER_POPUP_DATA, CommonString.KEY_STORE_ID + "='" + store_id + "'", null);
    }

    public long insertSampledData(String store_id, String visit_Date, String user_name, ArrayList<SampledGetterSetter> list) {
        db.delete(CommonString.TABLE_INSERT_CUSTOMER_DATA, "STORE_ID" + "='" + store_id + "'AND VISIT_DATE='" + visit_Date + "'", null);
        db.delete(CommonString.TABLE_INSERT_CUSTOMER_POPUP_DATA, "STORE_ID" + "='" + store_id + "'", null);
        long l = 0, l2 = 0;

        ContentValues values = new ContentValues();
        ContentValues values2 = new ContentValues();
        try {
            for (int i = 0; i < list.size(); i++) {

                values.put("STORE_ID", store_id);
                values.put("USER_ID", user_name);
                values.put("VISIT_DATE", visit_Date);
                values.put("MOBILE", list.get(i).getMobile());
                values.put("NAME", list.get(i).getName());
                values.put("CUSTOMER_SALES_CD", list.get(i).getCustomerSales_cd());
                values.put("CHECKBOX", list.get(i).isExists());

                l = db.insert(CommonString.TABLE_INSERT_CUSTOMER_DATA, null, values);
                for (int j = 0; j < list.get(i).getSamplingChecklistData().size(); j++) {
                    if (!list.get(i).getSamplingChecklistData().get(j).getStock_liens().equalsIgnoreCase("")) {
                        values2.put("STORE_ID", store_id);
                        values2.put("COMMON_ID", l);
                        values2.put("BRAND_ID", list.get(i).getSamplingChecklistData().get(j).getBrandId());

                        if (!list.get(i).getSamplingChecklistData().get(j).getStock_liens().equals("")) {
                            values2.put("QTY", list.get(i).getSamplingChecklistData().get(j).getStock_liens());
                        } else {
                            values2.put("QTY", "0");
                        }

                        l2 = db.insert(CommonString.TABLE_INSERT_CUSTOMER_POPUP_DATA, null, values2);
                    }
                }
            }
        } catch (Exception ex) {

        }
        return l2;
    }


    public ArrayList<SampledGetterSetter> getinsertedsampledData(String store_cd, String visit_date) {

        ArrayList<SampledGetterSetter> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("Select * from CUSTOMER_DATA where STORE_ID='" + store_cd + "'AND VISIT_DATE='" + visit_date + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    SampledGetterSetter sb = new SampledGetterSetter();



                    sb.setMobile(dbcursor.getString(dbcursor.getColumnIndexOrThrow("MOBILE")));
                    sb.setName(dbcursor.getString(dbcursor.getColumnIndexOrThrow("NAME")));
                    sb.setKey_id(dbcursor.getString(dbcursor.getColumnIndexOrThrow("KEY_ID")));
                    sb.setCustomerSales_cd(dbcursor.getString(dbcursor.getColumnIndexOrThrow("CUSTOMER_SALES_CD")));
                    String sales=dbcursor.getString(dbcursor.getColumnIndexOrThrow("CUSTOMER_SALES_CD"));
                    if (sales.equals("1")){
                        sb.setCustomerSales_cd("1");
                    }else {
                        sb.setCustomerSales_cd("0");
                    }
                    String value = dbcursor.getString(dbcursor.getColumnIndexOrThrow("CHECKBOX"));
                    if (value.equals("0")) {
                        sb.setExists(false);
                    } else {
                        sb.setExists(true);
                    }
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


    public ArrayList<BrandMaster> getInsertedSamplingData(String storeId, String key_id) {

        ArrayList<BrandMaster> list = new ArrayList<>();
        Cursor dbcursor = null;
        try {
            dbcursor = db.rawQuery("Select * from CUSTOMER_POPUP_DATA where STORE_ID='" + storeId + "' and COMMON_ID = '" + key_id + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                while (!dbcursor.isAfterLast()) {
                    BrandMaster sb = new BrandMaster();
                    sb.setBrandId(Integer.valueOf(dbcursor.getString(dbcursor.getColumnIndexOrThrow("BRAND_ID"))));
                    sb.setStock_liens(dbcursor.getString(dbcursor.getColumnIndexOrThrow("QTY")));

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

    public boolean isCustomerDataFilled(String storeId) {
        boolean filled = false;

        Cursor dbcursor = null;

        try {
            dbcursor = db.rawQuery("SELECT * FROM CUSTOMER_DATA WHERE STORE_ID= '" + storeId + "'", null);
            if (dbcursor != null) {
                dbcursor.moveToFirst();
                int icount = dbcursor.getInt(0);
                dbcursor.close();
                if (icount > 0) {
                    filled = true;
                } else {
                    filled = false;
                }

            }

        } catch (Exception e) {

            return filled;
        }

        return filled;
    }

}
