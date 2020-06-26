package intelre.cpm.com.hulcnc.dailyentry;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import cpm.com.hulcnc.R;
import intelre.cpm.com.hulcnc.Database.HUL_CNC_DB;
import intelre.cpm.com.hulcnc.constant.AlertandMessages;
import intelre.cpm.com.hulcnc.constant.CommonString;
import intelre.cpm.com.hulcnc.gettersetter.QuizGetterSetter;
import intelre.cpm.com.hulcnc.gettersetter.StockAvailabilityGetterSetter;
import intelre.cpm.com.hulcnc.gettersetter.TrainingQuizGetterSetter;
import intelre.cpm.com.hulcnc.gsonGetterSetter.TrainingType;
import intelre.cpm.com.hulcnc.retrofit.DownloadAllDatawithRetro;


public class QuizActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    HUL_CNC_DB db;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor = null;
    String store_cd, visit_date, user_type, username, Error_Message, region_id, destributor_id, training_time;
    ExpandableListView lvExp_audit;
    FloatingActionButton storeAudit_fab;
    List<QuizGetterSetter> listDataHeader;
    List<QuizGetterSetter> questionList;
    HashMap<QuizGetterSetter, List<QuizGetterSetter>> listDataChild;
    ExpandableListAdapter listAdapter;
    boolean checkflag = true;
    ArrayList<Integer> checkHeaderArray = new ArrayList<>();
    private String stock_category_cd;
    TextView txt_timer, icon_date, add_date;
    Spinner head_spin;
    ArrayList<TrainingType> trainingData = new ArrayList<>();
    private ArrayAdapter<CharSequence> categoryAdapter;
    //
    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;
    String cdSpinValue, SpinValue;
    DownloadAllDatawithRetro upload;
    String dateset = "";
    CountDownTimer waitTimer;
    int time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        getviewUI();
        getSpinnerData();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        dateset = df.format(Calendar.getInstance().getTime());
        add_date.setText(dateset);

        icon_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(QuizActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                        dateset = day + "/" + (month + 1) + "/" + year;

                        add_date.setText(dateset);

                    }
                }, year, month, dayOfMonth);

                // datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis() - 86400000 * 6);
                try {
                    datePickerDialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        // reverseTimer(600, txt_timer);
        int trainingTym = Integer.parseInt(training_time);
         time = (trainingTym * 40);
        reverseTimer(time, txt_timer);
        // time = (2 * 40);


        storeAudit_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lvExp_audit.clearFocus();
                // lvExp_audit.invalidateViews();
                // listAdapter.notifyDataSetChanged();
                if (validation()) {
                if (validateData(listDataChild, listDataHeader)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(QuizActivity.this);
                    builder.setTitle("Parinaam").setMessage(R.string.alertsaveData);
                    builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if(waitTimer != null) {
                                waitTimer.cancel();
                                waitTimer = null;
                            }
                            db.open();
                            db.insertTrainingQuizData(username, visit_date, store_cd, dateset, cdSpinValue, SpinValue);
                            db.insertQuizData(store_cd, listDataChild, listDataHeader);
                            Intent intent=new Intent(QuizActivity.this,QuizSummaryActivity.class);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                        }
                    });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    builder.show();
                } else {
                    Snackbar.make(lvExp_audit, Error_Message, Snackbar.LENGTH_LONG).show();
                }
            }

            }
        });

    }

    private void getviewUI() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       // getSupportActionBar().setHomeButtonEnabled(true);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        head_spin = findViewById(R.id.head_spin);
        add_date = findViewById(R.id.add_date);
        icon_date = findViewById(R.id.icon_date);
        txt_timer = findViewById(R.id.txt_timer);
        lvExp_audit = findViewById(R.id.lvExp_audit);
        storeAudit_fab = findViewById(R.id.storeAudit_fab);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        Intent intent = getIntent();
        stock_category_cd = intent.getStringExtra(CommonString.KEY__STOCK_CATEGORY_CD);
        store_cd = preferences.getString(CommonString.KEY_STORE_CD, null);
        username = preferences.getString(CommonString.KEY_USERNAME, null);
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        user_type = preferences.getString(CommonString.KEY_USER_TYPE, null);
        training_time = preferences.getString(CommonString.KEY_FLAG_TRAINING_TIME, null);

        region_id = preferences.getString(CommonString.KEY_REGION_ID, null);
        destributor_id = preferences.getString(CommonString.KEY_DESTRIBUTOR_ID, null);
        getSupportActionBar().setTitle("Quiz -" + visit_date);
        db = new HUL_CNC_DB(this);
        db.open();
        lvExp_audit.setVisibility(View.INVISIBLE);

        prepareListData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        lvExp_audit.setAdapter(listAdapter);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            lvExp_audit.expandGroup(i);
        }
    }

    private void prepareListData() {
        db.open();
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
        listDataHeader = db.getHeaderQuizData();
        if (listDataHeader.size() > 0) {
            for (int i = 0; i < listDataHeader.size(); i++) {

                questionList = db.getQuizInsertedData(store_cd, listDataHeader.get(i).getBrand_id());
                if (questionList.size() > 0) {
                    storeAudit_fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.edit_txt));
                } else {
                    questionList = db.getQuizchildData(listDataHeader.get(i).getBrand_id());
                }
                listDataChild.put(listDataHeader.get(i), questionList); // Header, Child data
            }
        }
    }

    public class ExpandableListAdapter extends BaseExpandableListAdapter {
        private Context _context;
        private List<QuizGetterSetter> _listDataHeader;
        private HashMap<QuizGetterSetter, List<QuizGetterSetter>> _listDataChild;

        public ExpandableListAdapter(Context context, List<QuizGetterSetter> listDataHeader,
                                     HashMap<QuizGetterSetter, List<QuizGetterSetter>> listChildData) {
            this._context = context;
            this._listDataHeader = listDataHeader;
            this._listDataChild = listChildData;
        }

        @Override
        public Object getChild(int groupPosition, int childPosititon) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosititon);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild,
                                 View convertView, ViewGroup parent) {

            final QuizGetterSetter childText = (QuizGetterSetter) getChild(groupPosition, childPosition);
            ViewHolder holder = null;
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_item_parent_storeaudit, null);
                holder = new ViewHolder();
                holder.cardView = convertView.findViewById(R.id.card_view);
                holder.audit_spin = convertView.findViewById(R.id.audit_spin);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            TextView txtListChild = convertView.findViewById(R.id.lblListItem);
            txtListChild.setText(childText.getQuestion());
            String select = getString(R.string.select_dropdown);
            final ArrayList<QuizGetterSetter> reason_list = db.getQuizAnswerData(childText.getQuestion_id(), select);

            holder.audit_spin.setAdapter(new ReasonSpinnerAdapter(_context, R.layout.spinner_text_view, reason_list));

            for (int i = 0; i < reason_list.size(); i++) {
                if (reason_list.get(i).getAnswerId().toString().equals(_listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getCurrectanswerCd().toString())) {
                    holder.audit_spin.setSelection(i);
                    break;
                }
            }
            final ViewHolder finalHolder = holder;
            holder.audit_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    if (pos != -1) {
                        QuizGetterSetter ans = reason_list.get(pos);
                        _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setCurrectanswerCd(ans.getAnswerId().toString());
                        _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setCurrectanswer(ans.getAnswer().toString());

                        _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setRight_Answer(Integer.valueOf(ans.getRight_Answer().toString()));

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            if (!checkflag) {
                boolean tempflag = false;
                if (_listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getCurrectanswerCd().equals("0")) {
                    tempflag = true;
                }
                if (tempflag) {
                    holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.red));
                } else {
                    holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.white));
                }
            } else {
                holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.white));
            }

            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this._listDataHeader.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return this._listDataHeader.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            final QuizGetterSetter headerTitle = (QuizGetterSetter) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_group_storeaudit, null);
            }
            TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
            lblListHeader.setText(headerTitle.getBrand());
            if (!checkflag) {
                if (checkHeaderArray.contains(groupPosition)) {
                    lblListHeader.setBackgroundColor(getResources().getColor(R.color.red));
                } else {
                    lblListHeader.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
            } else {
                lblListHeader.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    public class ViewHolder {
        Spinner audit_spin;
        CardView cardView;
    }


    public class ReasonSpinnerAdapter extends ArrayAdapter<QuizGetterSetter> {
        List<QuizGetterSetter> list;
        Context context;
        int resourceId;

        public ReasonSpinnerAdapter(Context context, int resourceId, ArrayList<QuizGetterSetter> list) {
            super(context, resourceId, list);
            this.context = context;
            this.list = list;
            this.resourceId = resourceId;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            LayoutInflater inflater = getLayoutInflater();
            view = inflater.inflate(resourceId, parent, false);
            QuizGetterSetter cm = list.get(position);
            TextView txt_spinner = (TextView) view.findViewById(R.id.txt_sp_text);
            txt_spinner.setText(list.get(position).getAnswer());

            return view;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            LayoutInflater inflater = getLayoutInflater();
            view = inflater.inflate(resourceId, parent, false);
            QuizGetterSetter cm = list.get(position);
            TextView txt_spinner = (TextView) view.findViewById(R.id.txt_sp_text);
            txt_spinner.setText(cm.getAnswer());

            return view;
        }

    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();

    }

    /* @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(QuizActivity.this);
        builder.setMessage(CommonString.ONBACK_ALERT_MESSAGE)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                        QuizActivity.this.finish();
                       *//* if(waitTimer != null) {
                            waitTimer.cancel();
                            waitTimer = null;
                        }*//*
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }*/

    boolean validateData(HashMap<QuizGetterSetter, List<QuizGetterSetter>> listDataChild2, List<QuizGetterSetter> listDataHeader2) {
        checkflag = true;
        checkHeaderArray.clear();
        for (int i = 0; i < listDataHeader2.size(); i++) {
            for (int j = 0; j < listDataChild2.get(listDataHeader2.get(i)).size(); j++) {
                String spinValue = listDataChild2.get(listDataHeader2.get(i)).get(j).getCurrectanswerCd();
                if (spinValue.equals("0")) {
                    checkflag = false;
                    Error_Message = CommonString.KEY_FOR_SPINNER_DROP_DOWN;
                    break;
                } else {
                    checkflag = true;

                }
            }

            if (!checkflag) {
                if (!checkHeaderArray.contains(i)) {
                    checkHeaderArray.add(i);
                }
                break;
            }
        }
        listAdapter.notifyDataSetChanged();
        return checkflag;
    }


    public void reverseTimer(int Seconds, final TextView tv) {

        waitTimer =  new CountDownTimer(Seconds * 1000 + 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);

                int hours = seconds / (60 * 60);
                int tempMint = (seconds - (hours * 60 * 60));
                int minutes = tempMint / 60;
                seconds = tempMint - (minutes * 60);

                tv.setText("START TIME : " + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds));
            }

            public void onFinish() {
                tv.setText("Completed");
                db.open();

                db.insertTrainingQuizData(username, visit_date, store_cd, dateset, cdSpinValue, SpinValue);
                db.insertQuizData(store_cd, listDataChild, listDataHeader);

                AlertandMessages.showToastMsg(QuizActivity.this, "Quiz Saved Successfully");
                finish();

                // new GeoTagUpload(QuizActivity.this).execute();


            }
        }.start();

    }



    public void getSpinnerData() {
        db.open();
        trainingData = db.getTrainingData();
        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        categoryAdapter.add("-Select-");
        for (int i = 0; i < trainingData.size(); i++) {
            categoryAdapter.add(trainingData.get(i).getTrainingType());
        }
        head_spin.setAdapter(categoryAdapter);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        head_spin.setOnItemSelectedListener(this);


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.head_spin:
                if (position != 0) {

                    cdSpinValue = String.valueOf(trainingData.get(position - 1).getTrainingTypeId());
                    SpinValue = trainingData.get(position - 1).getTrainingType();
                    lvExp_audit.setVisibility(View.VISIBLE);
                 //   reverseTimer(time, txt_timer);

                   /* prepareListData();
                    listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
                    lvExp_audit.setAdapter(listAdapter);
                    for (int i = 0; i < listAdapter.getGroupCount(); i++) {
                        lvExp_audit.expandGroup(i);
                    }*/

                   //
                } else {

                    cdSpinValue = "0";
                    SpinValue = "";

                }
                break;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public class GeoTagUpload extends AsyncTask<Void, Void, String> {

        boolean uploadflag;
        String errormsg = "";
        private Context context;
        private Dialog dialog;
        private ProgressBar pb;

        GeoTagUpload(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new Dialog(context);
            dialog.setContentView(R.layout.custom);
            dialog.setTitle(getResources().getString(R.string.dialog_title));
            dialog.setCancelable(false);
            dialog.show();
            pb = (ProgressBar) dialog.findViewById(R.id.progressBar1);
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                // uploading Quiz
                uploadflag = false;
                db.open();

                TrainingQuizGetterSetter traingData = db.getTraingTypeData(store_cd);
                JSONArray topUpArray = new JSONArray();
                JSONObject trainingobj = new JSONObject();
                trainingobj.put("USER_ID", username);
                trainingobj.put(CommonString.KEY_VISIT_DATE, traingData.getVisit_date());
                trainingobj.put("TRAINING_DATE", traingData.getTrainingDate());
                trainingobj.put("TRAINING_ID", traingData.getTrainingId());

                topUpArray.put(trainingobj);


                ArrayList<QuizGetterSetter> quiz_data = db.getQuizUploadData(store_cd);
                if (quiz_data.size() > 0) {
                    JSONArray promoArray = new JSONArray();
                    for (int j = 0; j < quiz_data.size(); j++) {
                        JSONObject obj = new JSONObject();
                        obj.put("MID", "0");
                        obj.put("UserId", username);
                        obj.put("QUESTION_ID", quiz_data.get(j).getQuestion_id());
                        obj.put("ANSWER_CD", quiz_data.get(j).getCurrectanswerCd());
                        obj.put("RIGHT_ANSWER", quiz_data.get(j).getRight_Answer());
                        obj.put("BRAND_ID", quiz_data.get(j).getBrand_id());
                        obj.put("VisitDate", quiz_data.get(j).getVisit_date());
                        promoArray.put(obj);
                    }

                    JSONObject jsonObject4 = new JSONObject();
                    jsonObject4.put("quiz_list_data", promoArray);
                    jsonObject4.put("quiz_training_data", topUpArray);

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("MID", "0");
                    jsonObject.put("Keys", "QUIZ_DATA");
                    // jsonObject.put("JsonData", promoArray.toString());
                    jsonObject.put("JsonData", jsonObject4.toString());
                    jsonObject.put("UserId", username);


                    String jsonString2 = jsonObject.toString();
                    String result = upload.downloadDataUniversal(jsonString2, CommonString.UPLOADJsonDetail);

                    if (result.equalsIgnoreCase(CommonString.MESSAGE_NO_RESPONSE_SERVER)) {
                        uploadflag = false;
                        throw new SocketTimeoutException();
                    } else if (result.equalsIgnoreCase(CommonString.MESSAGE_SOCKETEXCEPTION)) {
                        uploadflag = false;
                        throw new IOException();
                    } else if (result.equalsIgnoreCase(CommonString.MESSAGE_INVALID_JSON)) {
                        uploadflag = false;
                        throw new JsonSyntaxException("Primary_Grid_Image");
                    } else if (result.equalsIgnoreCase(CommonString.KEY_FAILURE)) {
                        uploadflag = false;
                        throw new Exception();
                    } else {
                        uploadflag = true;
                    }
                }

            } catch (SocketException ex) {
                uploadflag = false;
                ex.printStackTrace();
                errormsg = CommonString.MESSAGE_INTERNET_NOT_AVALABLE;
            } catch (IOException ex) {
                uploadflag = false;
                ex.printStackTrace();
                errormsg = CommonString.MESSAGE_INTERNET_NOT_AVALABLE;
            } catch (JsonSyntaxException ex) {
                uploadflag = false;
                ex.printStackTrace();
                errormsg = CommonString.MESSAGE_INVALID_JSON;
            } catch (NumberFormatException e) {
                uploadflag = false;
                errormsg = CommonString.MESSAGE_NUMBER_FORMATE_EXEP;
            } catch (Exception ex) {
                uploadflag = false;
                errormsg = CommonString.MESSAGE_EXCEPTION;
            }

            if (uploadflag) {
                return CommonString.KEY_SUCCESS;
            } else {
                return errormsg;
            }

        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //  dialog.dismiss();
            if (result.equals(CommonString.KEY_SUCCESS) || result.equalsIgnoreCase("")) {

               /* db.open();
                if (db.updateInsertExpenseStatus(campaign_id, status) > 0) {
                    AlertandMessages.showToastMsg(context, "Expense Saved Successfully");
                    finish();
                } else {
                    AlertandMessages.showAlert((android.app.Activity) context, "Error in updating Expense status", true);
                }
                db.updateCStatus(campaign_id, status);*/

                db.open();
                if (db.getQuizUploadData(store_cd).size() > 0) {
                    AlertandMessages.showToastMsg(context, "Expense Saved Successfully");
                    Intent intent = new Intent(QuizActivity.this, StoreListActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_in, R.anim.activity_out);

                    finish();
                } else {
                    AlertandMessages.showAlert((android.app.Activity) context, "Error in updating Quiz status", true);
                }

            } else {
                AlertandMessages.showAlert((Activity) context, getResources().getString(R.string.failure) + " : " + errormsg, true);
                finish();
            }
        }

    }

    public boolean validation() {
        boolean value = true;
        if (head_spin.getSelectedItem().toString().equalsIgnoreCase("-Select-")) {
            value = false;
            showMessage("Please Select Type Of Trainig");
        }

        return value;
    }
    public void showMessage(String message) {
        Snackbar.make(head_spin, message, Snackbar.LENGTH_SHORT).show();
    }

}
