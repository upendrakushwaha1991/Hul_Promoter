package intelre.cpm.com.hulcnc.dailyentry;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import cpm.com.hulcnc.R;
import intelre.cpm.com.hulcnc.Database.HUL_CNC_DB;
import intelre.cpm.com.hulcnc.constant.CommonString;
import intelre.cpm.com.hulcnc.gettersetter.SampledGetterSetter;
import intelre.cpm.com.hulcnc.gsonGetterSetter.BrandMaster;

public class NewCustomerActivity extends AppCompatActivity implements View.OnClickListener {
    CheckBox market_checkbox;
    LinearLayout layout_parentM, rl_content;
    String visit_date, username;
    private SharedPreferences preferences;
    HUL_CNC_DB db;
    EditText sample_mobile, sample_name;
    LinearLayout rl_img, rl_feedback;
    NestedScrollView scroll;
    Button btn_add, save_fab;
    Spinner categorysample_spin, skusample_spin;
    EditText sample_feedback_txt;
    ImageView img_sampled;
    RecyclerView sample_list;
    ToggleButton toogle_sampleV;
    MyAdapter adapter;
    ArrayList<BrandMaster> samplingChecklistData = new ArrayList<>();
    ArrayList<SampledGetterSetter> insertedDataList = new ArrayList<>();
    boolean sampleaddflag = false;
    RecyclerView lv_customer_list;
    DailogAdapater dailogAdapterData;
    boolean  value = true;
    String store_cd, user_type, training_time, region_id, destributor_id;
    Spinner sp_customer_sale;
    Button btn_brand;
    String[] string_present = {"Select", "YES", "NO"};
    String customerData_cd;
    Dialog dialog1;
    Button save, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_customer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        db = new HUL_CNC_DB(getApplicationContext());
        db.open();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        store_cd = preferences.getString(CommonString.KEY_STORE_CD, null);
        username = preferences.getString(CommonString.KEY_USERNAME, null);
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        user_type = preferences.getString(CommonString.KEY_USER_TYPE, null);
        training_time = preferences.getString(CommonString.KEY_FLAG_TRAINING_TIME, null);

        region_id = preferences.getString(CommonString.KEY_REGION_ID, null);
        destributor_id = preferences.getString(CommonString.KEY_DESTRIBUTOR_ID, null);
        getSupportActionBar().setTitle("Customer Data -" + visit_date);

        scroll = (NestedScrollView) findViewById(R.id.scroll);
        btn_add = (Button) findViewById(R.id.btn_add);
        save_fab = (Button) findViewById(R.id.save_fab);
        rl_img = (LinearLayout) findViewById(R.id.rl_img);
        rl_feedback = (LinearLayout) findViewById(R.id.rl_feedback);
        categorysample_spin = (Spinner) findViewById(R.id.categorysample_spin);
        skusample_spin = (Spinner) findViewById(R.id.skusample_spin);
        sample_feedback_txt = (EditText) findViewById(R.id.sample_feedback_txt);
        img_sampled = (ImageView) findViewById(R.id.img_sampled);
        sample_list = (RecyclerView) findViewById(R.id.sample_list);
        toogle_sampleV = (ToggleButton) findViewById(R.id.toogle_sampleV);
        sample_mobile = (EditText) findViewById(R.id.sample_mobile);
        sample_name = (EditText) findViewById(R.id.sample_name);
        sp_customer_sale = (Spinner) findViewById(R.id.sp_customer_sale);
        market_checkbox = (CheckBox) findViewById(R.id.market_checkbox);
        layout_parentM = (LinearLayout) findViewById(R.id.layout_parentM);
        rl_content = (LinearLayout) findViewById(R.id.rl_content);
        btn_brand = (Button) findViewById(R.id.btn_brand);
        setTitle("Consumer Connect - " + visit_date);

        dialog1 = new Dialog(NewCustomerActivity.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.dialog_customer_data);

        lv_customer_list = (RecyclerView) dialog1.findViewById(R.id.lv_customer_list);


        btn_add.setOnClickListener(this);
        save_fab.setOnClickListener(this);
        img_sampled.setOnClickListener(this);
        toogle_sampleV.setOnClickListener(this);
        market_checkbox.setOnClickListener(this);
        btn_brand.setOnClickListener(this);
        GETALLDATA();
        samplingChecklistData = db.getCustomer_data();
        setDataToListView();
        if (insertedDataList.size() > 0 && !insertedDataList.get(0).isExists()) {
            market_checkbox.setChecked(false);
            sample_list.setVisibility(View.GONE);
            layout_parentM.setVisibility(View.GONE);
            rl_content.setVisibility(View.GONE);
        } else {
            sample_list.setVisibility(View.VISIBLE);
            layout_parentM.setVisibility(View.VISIBLE);
            rl_content.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.market_checkbox:
                if (market_checkbox.isChecked()) {
                    if (insertedDataList.size() > 0 && !insertedDataList.get(0).isExists()) {
                        insertedDataList.clear();
                        db.removealSamplingData(store_cd);
                        save_fab.setText("Save");
                    }
                    sample_list.setVisibility(View.VISIBLE);
                    layout_parentM.setVisibility(View.VISIBLE);
                    rl_content.setVisibility(View.VISIBLE);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(NewCustomerActivity.this);
                    builder.setTitle("Parinaam").setMessage(R.string.messageM);
                    builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sample_list.setVisibility(View.GONE);
                            layout_parentM.setVisibility(View.GONE);
                            rl_content.setVisibility(View.GONE);
                            clearingData();
                            db.open();
                            if (insertedDataList.size() > 0) {
                                insertedDataList.clear();
                                db.removealSamplingData(store_cd);
                            }
                            SampledGetterSetter marketIntelligenceG = new SampledGetterSetter();

                            marketIntelligenceG.setMobile("");
                            marketIntelligenceG.setName("");
                            marketIntelligenceG.setExists(false);

                            insertedDataList.add(marketIntelligenceG);
                            sampleaddflag = true;
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            market_checkbox.setChecked(true);
                        }
                    });
                    builder.show();

                }
                break;

            case R.id.btn_brand:
                popup();

                break;
            case R.id.btn_add:
                if (market_checkbox.isChecked()) {
                    if (validation()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(NewCustomerActivity.this);
                        builder.setMessage("Are you sure you want to add ?")
                                .setCancelable(false)
                                .setPositiveButton("Yes",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                db.open();
                                                SampledGetterSetter sampledG = new SampledGetterSetter();
                                                sampledG.setMobile(sample_mobile.getText().toString().replaceAll("[(!@#$%^&*?)]", ""));
                                                sampledG.setName(sample_name.getText().toString().replaceAll("[(!@#$%^&*?)]", ""));
                                                sampledG.setCustomerSales_cd(customerData_cd);

                                                sampledG.setExists(true);
                                                sampledG.setSamplingChecklistData(samplingChecklistData);
                                                insertedDataList.add(sampledG);
                                                adapter = new MyAdapter(NewCustomerActivity.this, insertedDataList);
                                                sample_list.setAdapter(adapter);
                                                sample_list.setLayoutManager(new LinearLayoutManager(NewCustomerActivity.this));
                                                clearingData();

                                                sample_feedback_txt.setHint("Feedback");
                                                categorysample_spin.setSelection(0);
                                                skusample_spin.setSelection(0);
                                                sp_customer_sale.setSelection(0);
                                                toogle_sampleV.setChecked(false);
                                                rl_img.setVisibility(View.GONE);
                                                adapter.notifyDataSetChanged();

                                                sampleaddflag = true;
                                                Snackbar.make(btn_add, "Data has been added", Snackbar.LENGTH_SHORT).show();
                                            }
                                        })
                                .setNegativeButton("No",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int id) {
                                                dialog.cancel();
                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }
                break;
            case R.id.save_fab:
                if (insertedDataList.size() > 0) {
                    if (sampleaddflag) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(NewCustomerActivity.this);
                        builder1.setMessage("Are you sure you want to save data ?")
                                .setCancelable(false)
                                .setPositiveButton("Yes",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                db.open();
                                                db.insertSampledData(store_cd, visit_date, username, insertedDataList);
                                                finish();
                                                sampleaddflag = false;
                                                Snackbar.make(btn_add, "Data has been saved", Snackbar.LENGTH_SHORT).show();
                                            }
                                        })
                                .setNegativeButton("No",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int id) {
                                                dialog.cancel();
                                            }
                                        });
                        AlertDialog alert1 = builder1.create();
                        alert1.show();
                    } else {
                        Snackbar.make(btn_add, "Please add consumer connect data", Snackbar.LENGTH_SHORT).show();
                    }

                } else {
                    Snackbar.make(btn_add, "Please add consumer connect data", Snackbar.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void clearingData() {
        sample_feedback_txt.setText("");
        sample_mobile.setText("");
        sample_name.setText("");
        setDefaultdata();
    }

    public void GETALLDATA() {

        db.open();
        btn_brand.setBackgroundResource(R.color.light_gray);

        ArrayAdapter present = new ArrayAdapter(this, android.R.layout.simple_spinner_item, string_present);
        present.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_customer_sale.setAdapter(present);

        sp_customer_sale.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final SampledGetterSetter sampledG = new SampledGetterSetter();
                if (position != 0) {
                    customerData_cd = String.valueOf(parent.getSelectedItemPosition());

                } else {
                    customerData_cd="";
                }

                if (position == 0) {

                    btn_brand.setEnabled(false);
                    btn_brand.setBackgroundResource(R.color.light_gray);
                    /*ArrayList<BrandMaster> samplingChecklistData = new ArrayList<>();
                    sampledG.setSamplingChecklistData(samplingChecklistData);*/
                    samplingChecklistData.clear();
                } else if (position == 1) {
                    btn_brand.setEnabled(true);
                    btn_brand.setBackgroundResource(R.color.colorPrimary);
                } else if (position == 2) {
                    btn_brand.setEnabled(false);
                    /*ArrayList<BrandMaster> samplingChecklistData = new ArrayList<>();
                    sampledG.setSamplingChecklistData(samplingChecklistData);*/
                    samplingChecklistData.clear();
                    btn_brand.setBackgroundResource(R.color.light_gray);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        setDefaultdata();
    }

    private void setDefaultdata() {
        db.open();
        samplingChecklistData = db.getCustomer_data();

        dailogAdapterData =new DailogAdapater(NewCustomerActivity.this, samplingChecklistData);
        lv_customer_list.setAdapter(dailogAdapterData);
        lv_customer_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    public void showMessage(String message) {
        Snackbar.make(btn_add, message, Snackbar.LENGTH_SHORT).show();
    }
    public void showMessagepopup(String message) {
        Snackbar.make(save, message, Snackbar.LENGTH_SHORT).show();
    }


    public boolean validation() {
        value = true;

        if (sample_name.getText().toString().equalsIgnoreCase("")) {
            value = false;
            showMessage("Please enter consumer name.");
        } else if (sample_mobile.getText().toString().equalsIgnoreCase("")) {
            value = false;
            showMessage("Please enter mobile number.");
        } else if (sample_mobile.getText().toString().length() < 10) {
            value = false;
            showMessage("Mobile number should be 10 digits only.");
        } else if (sp_customer_sale.getSelectedItemPosition() == 0) {
            value = false;
            showMessage("Please Select Customer sales");
        } else if (sp_customer_sale.getSelectedItemPosition() == 1) {
            value = false;
            if (isValid(samplingChecklistData)) {
                value = true;
            }else {
                showMessage("Please fill At Least one Brand Liens Sold");
            }
        }

        ///    brandDataListtion.notifyDataSetChanged();
        return value;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(CommonString.ONBACK_ALERT_MESSAGE)
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog, int id) {
                                    overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                                    finish();
                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(CommonString.ONBACK_ALERT_MESSAGE)
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                                finish();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private LayoutInflater inflator;
        Context context;
        ArrayList<SampledGetterSetter> insertedlist_Data;

        MyAdapter(Context context, ArrayList<SampledGetterSetter> insertedlist_Data) {
            inflator = LayoutInflater.from(context);
            this.context = context;
            this.insertedlist_Data = insertedlist_Data;
        }

        @Override
        public int getItemCount() {
            return insertedlist_Data.size();
        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflator.inflate(R.layout.customer_adapter, parent, false);
            MyAdapter.MyViewHolder holder = new MyAdapter.MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyAdapter.MyViewHolder holder, final int position) {
            holder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (insertedlist_Data.get(position).getKey_id() == null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(NewCustomerActivity.this);
                        builder.setMessage("Are you sure you want to delete the data ?")
                                .setCancelable(false)
                                .setPositiveButton("Yes",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int id) {
                                                insertedlist_Data.remove(position);
                                                if (insertedlist_Data.size() > 0) {
                                                    MyAdapter adapter = new MyAdapter(NewCustomerActivity.this, insertedlist_Data);
                                                    sample_list.setAdapter(adapter);
                                                    adapter.notifyDataSetChanged();
                                                }
                                                notifyDataSetChanged();
                                            }
                                        })
                                .setNegativeButton("No",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int id) {
                                                dialog.cancel();
                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(NewCustomerActivity.this);
                        builder.setMessage("Are you sure you want to delete the data?")
                                .setCancelable(false)
                                .setPositiveButton("Yes",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int id) {
                                                String listid = insertedlist_Data.get(position).getKey_id();
                                                db.removesampledata(listid);
                                                insertedlist_Data.remove(position);
                                                if (insertedlist_Data.size() > 0) {
                                                    MyAdapter adapter = new MyAdapter(NewCustomerActivity.this, insertedlist_Data);
                                                    sample_list.setAdapter(adapter);
                                                    adapter.notifyDataSetChanged();
                                                }
                                                notifyDataSetChanged();
                                            }
                                        })
                                .setNegativeButton("No",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int id) {
                                                dialog.cancel();
                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }


                }
            });

            holder.txt_cat.setText(insertedlist_Data.get(position).getName());
            holder.txt_sku.setText(insertedlist_Data.get(position).getMobile());
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView txt_cat, txt_sku, txt_samp;
            ImageView remove;

            public MyViewHolder(View convertView) {
                super(convertView);
                txt_cat = (TextView) convertView.findViewById(R.id.txt_cat);
                txt_sku = (TextView) convertView.findViewById(R.id.txt_sku);
                txt_samp = (TextView) convertView.findViewById(R.id.txt_samp);
                remove = (ImageView) convertView.findViewById(R.id.imgDelRow);
            }
        }
    }

    public class DailogAdapater extends RecyclerView.Adapter<DailogAdapater.MyViewHolder> {
        private LayoutInflater inflator;
        List<BrandMaster> data = Collections.emptyList();

        public DailogAdapater(Context context, List<BrandMaster> data) {
            inflator = LayoutInflater.from(context);
            this.data = data;
        }

        @Override
        public DailogAdapater.MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            View view = inflator.inflate(R.layout.dialog_list_, parent, false);
            DailogAdapater.MyViewHolder holder = new DailogAdapater.MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final DailogAdapater.MyViewHolder viewHolder, final int position) {

            final BrandMaster current = data.get(position);

            viewHolder.brand.setText(current.getBrand());

            viewHolder.edt_brand.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {

                    if (!hasFocus) {
                        final EditText Caption = (EditText) v;
                        String value1 = Caption.getText().toString().replaceFirst("^0+(?!$)", "");
                        if (value1.equals("")) {
                            current.setStock_liens("");
                        } else {
                            current.setStock_liens(value1);
                        }

                    }
                }
            });
            viewHolder.edt_brand.setText(current.getStock_liens());
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView brand;
            EditText edt_brand;

            public MyViewHolder(View itemView) {
                super(itemView);
                brand = (TextView) itemView.findViewById(R.id.brand);
                edt_brand = (EditText) itemView.findViewById(R.id.edt_brand);
            }
        }
    }

   /* private boolean isValid(List<BrandMaster> possaledata) {
        boolean flag = true;
        for (int i = 0; i < possaledata.size(); i++) {
            String posqty = possaledata.get(i).getStock_liens();

            if (posqty.equals("")) {
                showMessage("Please all fill No of lines purchsed.");
                flag = false;
                break;
            }
        }
        return flag;
    }*/
   private boolean isValid(List<BrandMaster> possaledata) {
       boolean flag = true;
       for (int i = 0; i < possaledata.size(); i++) {
           String posqty = possaledata.get(i).getStock_liens();
           if (posqty != null && !posqty.equalsIgnoreCase("") && !posqty.equalsIgnoreCase("0")) {
               flag = true;
               break;

           }

           if (flag) {
               break;
           }

       }
       return flag;
   }


/*
    private boolean isValidPopup(List<BrandMaster> possaledata) {
        boolean flag = true;
        for (int i = 0; i < possaledata.size(); i++) {
            String posqty = possaledata.get(i).getStock_liens();

            if (posqty.equals("")) {
                showMessagepopup("Please all fill No of lines purchsed.");
                flag = false;
                break;
            }
        }
        return flag;
    }
*/

    public boolean isValidPopup(List<BrandMaster> possaledata) {
        boolean flag = false;
        for (int i = 0; i < possaledata.size(); i++) {
            String posqty = possaledata.get(i).getStock_liens();
            if (posqty != null && !posqty.equalsIgnoreCase("") && !posqty.equalsIgnoreCase("0")) {
                flag = true;
                break;

            }

            if (flag) {
                break;
            }

        }

        return flag;
    }




    private void popup() {

        dialog1 = new Dialog(NewCustomerActivity.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.dialog_customer_data);
        save = (Button) dialog1.findViewById(R.id.save);
        cancel = (Button) dialog1.findViewById(R.id.cancel);
        lv_customer_list = (RecyclerView) dialog1.findViewById(R.id.lv_customer_list);
        dialog1.setCancelable(false);
        dialog1.setCanceledOnTouchOutside(true);
        db.open();
      ///  samplingChecklistData = db.getInsertCustomerPoData();

        if (samplingChecklistData.size() > 0) {
            dailogAdapterData = new DailogAdapater(getApplicationContext(), samplingChecklistData);
            lv_customer_list.setAdapter(dailogAdapterData);
            lv_customer_list.setLayoutManager(new LinearLayoutManager(NewCustomerActivity.this));
        } else {
            samplingChecklistData = db.getCustomer_data();
            if (samplingChecklistData.size() > 0) {
                dailogAdapterData = new DailogAdapater(getApplicationContext(), samplingChecklistData);
                lv_customer_list.setAdapter(dailogAdapterData);
                lv_customer_list.setLayoutManager(new LinearLayoutManager(this));

           }
        }


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cancelData();

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               final SampledGetterSetter sampledG = new SampledGetterSetter();
                lv_customer_list.clearFocus();
                if (isValidPopup(samplingChecklistData)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(NewCustomerActivity.this);
                    builder.setMessage("Do you want to save the data ")
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            sampledG.setSamplingChecklistData(samplingChecklistData);
                                            dialog.cancel();
                                            dialog1.dismiss();
                                        }
                                    })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            ArrayList<BrandMaster> samplingChecklistData = new ArrayList<>();
                                            sampledG.setSamplingChecklistData(samplingChecklistData);
                                            dialog.cancel();
                                            dialog1.dismiss();
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }else {
                    showMessagepopup("Please fill At Least one Brand Liens Sold");
                }
            }
        });

        dialog1.show();
    }

    public void setDataToListView() {
        try {
            insertedDataList = db.getinsertedsampledData(store_cd, visit_date);
            for (int i = 0; i < insertedDataList.size(); i++) {
                ArrayList<BrandMaster> sampleData = db.getInsertedSamplingData(store_cd, insertedDataList.get(i).getKey_id());
                insertedDataList.get(i).setSamplingChecklistData(sampleData);
            }

            if (insertedDataList.size() > 0) {
                save_fab.setText("Update");
                Collections.reverse(insertedDataList);
                adapter = new MyAdapter(this, insertedDataList);
                sample_list.setAdapter(adapter);
                sample_list.setLayoutManager(new LinearLayoutManager(NewCustomerActivity.this));
                adapter.notifyDataSetChanged();
            }

        } catch (Exception e) {
            Log.d("Exception when fetching", e.toString());
        }
    }
    public  void  cancelData(){
        AlertDialog.Builder builder = new AlertDialog.Builder(NewCustomerActivity.this);
        builder.setTitle("Parinaam").setMessage(R.string.messageM);
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                samplingChecklistData.clear();
                dialog1.dismiss();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        builder.show();
    }

}
