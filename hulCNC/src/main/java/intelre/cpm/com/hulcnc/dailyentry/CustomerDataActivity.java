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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cpm.com.hulcnc.R;
import intelre.cpm.com.hulcnc.Database.HUL_CNC_DB;
import intelre.cpm.com.hulcnc.constant.CommonString;
import intelre.cpm.com.hulcnc.gettersetter.SampledGetterSetter;
import intelre.cpm.com.hulcnc.gsonGetterSetter.BrandMaster;

public class CustomerDataActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout lay_customer_name;
    private Spinner sp_present, sp_customer_sale;
    private EditText customer_name, mobile_no;
    private Button btn_brand, btn_add;
    FloatingActionButton save_fab;
    String[] string_present = {"Select", "YES", "NO"};
    String string_present_cd, customerData_cd;
    HUL_CNC_DB db;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor = null;
    String store_cd, visit_date, user_type, username, Error_Message, region_id, destributor_id, training_time;
    Toolbar toolbar;
    boolean value = true;
    ArrayList<SampledGetterSetter> insertedDataList = new ArrayList<>();
    MyAdapter adapter;
    RecyclerView sample_list;
    ArrayList<BrandMaster> samplingChecklistData = new ArrayList<>();
    boolean sampleaddflag = false;
    Dialog dialog1;
    private Button cancel, save;
    private RecyclerView lv_customer_list;
    ArrayList<BrandMaster> feedback_ques = new ArrayList<>();
    RouteAdapter feedback_question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_data);
        getviewUI();
        setSppinerData();

    }

    private void getviewUI() {

        toolbar = findViewById(R.id.toolbar);
        sp_present = findViewById(R.id.sp_present);
        sp_customer_sale = findViewById(R.id.sp_customer_sale);
        customer_name = findViewById(R.id.customer_name);
        mobile_no = findViewById(R.id.mobile_no);
        sample_list = findViewById(R.id.sample_list);
        btn_brand = findViewById(R.id.btn_brand);
        btn_add = findViewById(R.id.btn_add);
        save_fab = findViewById(R.id.save_fab);
        lay_customer_name = findViewById(R.id.lay_customer_name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        Intent intent = getIntent();
        //  stock_category_cd = intent.getStringExtra(CommonString.KEY__STOCK_CATEGORY_CD);
        store_cd = preferences.getString(CommonString.KEY_STORE_CD, null);
        username = preferences.getString(CommonString.KEY_USERNAME, null);
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        user_type = preferences.getString(CommonString.KEY_USER_TYPE, null);
        training_time = preferences.getString(CommonString.KEY_FLAG_TRAINING_TIME, null);

        region_id = preferences.getString(CommonString.KEY_REGION_ID, null);
        destributor_id = preferences.getString(CommonString.KEY_DESTRIBUTOR_ID, null);
        getSupportActionBar().setTitle("Customer Data -" + visit_date);
        btn_brand.setOnClickListener(this);
        btn_add.setOnClickListener(this);
        save_fab.setOnClickListener(this);
        db = new HUL_CNC_DB(this);
        db.open();

    }

    private void setSppinerData() {

        ArrayAdapter present = new ArrayAdapter(this, android.R.layout.simple_spinner_item, string_present);
        present.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_present.setAdapter(present);

        ArrayAdapter customerData = new ArrayAdapter(this, android.R.layout.simple_spinner_item, string_present);
        customerData.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_present.setAdapter(customerData);

        sp_present.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    string_present_cd = String.valueOf(parent.getSelectedItemPosition());
                    //  lay_customer_name.setVisibility(View.VISIBLE);

                    //  visiColoersGetterSetter.setPresent_name(string_present_cd);
                } else {
                    //visiColoersGetterSetter.setPresent_name("");
                }

                if (position == 0) {

                } else if (position == 1) {
                    lay_customer_name.setVisibility(View.VISIBLE);
                } else if (position == 2) {
                    lay_customer_name.setVisibility(View.GONE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        sp_customer_sale.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    customerData_cd = String.valueOf(parent.getSelectedItemPosition());

                    //  visiColoersGetterSetter.setPresent_name(customerData_cd);
                } else {
                    //  visiColoersGetterSetter.setPresent_name("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btn_brand:
                popup();
                break;

            case R.id.save_fab:
                if (insertedDataList.size() > 0) {
                    if (sampleaddflag) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(CustomerDataActivity.this);
                        builder1.setMessage("Are you sure you want to save data ?")
                                .setCancelable(false)
                                .setPositiveButton("Yes",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                db.open();
                                                //   db.insertSampledData(jcpGetset, username, insertedDataList);
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


            case R.id.btn_add:
                if (validation()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CustomerDataActivity.this);
                    builder.setMessage("Are you sure you want to add ?")
                            .setCancelable(false)
                            .setPositiveButton("Yes",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            db.open();
                                            SampledGetterSetter sampledG = new SampledGetterSetter();

                                            sampledG.setCustomerData_cd(string_present_cd);
                                            sampledG.setCustomerSales_cd(customerData_cd);
                                            sampledG.setMobile(customer_name.getText().toString().replaceAll("[(!@#$%^&*?)]", ""));
                                            sampledG.setName(mobile_no.getText().toString().replaceAll("[(!@#$%^&*?)]", ""));

                                            sampledG.setSamplingChecklistData(samplingChecklistData);
                                            insertedDataList.add(sampledG);
                                            adapter = new MyAdapter(CustomerDataActivity.this, insertedDataList);
                                            sample_list.setAdapter(adapter);
                                            sample_list.setLayoutManager(new LinearLayoutManager(CustomerDataActivity.this));
                                            clearingData();

                                            sp_present.setSelection(0);
                                            sp_customer_sale.setSelection(0);
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

                break;
        }
    }

    public boolean validation() {
        value = true;
        if (sp_present.getSelectedItemPosition() == 0) {
            value = false;
            showMessage("Please Select Customer data");
        } else if (customer_name.getText().toString().equalsIgnoreCase("")) {
            value = false;
            showMessage("Please enter consumer name.");
        } else if (mobile_no.getText().toString().equalsIgnoreCase("")) {
            value = false;
            showMessage("Please enter mobile number.");
        } else if (mobile_no.getText().toString().length() < 10) {
            value = false;
            showMessage("Mobile number should be 10 digits only.");
        } else if (sp_customer_sale.getSelectedItemPosition() == 0) {
            value = false;
            showMessage("Please Select Customer sales");

        } else if (sp_customer_sale.getSelectedItemPosition() == 2) {
            value = false;
            if (isValid(samplingChecklistData)){

            }else {
                showMessage("Please fill At Least one Brand Liens Sold");
            }
           // isValid(samplingChecklistData);
        }


        feedback_question.notifyDataSetChanged();
        return value;
    }

    /*
        private boolean isValid(List<BrandMaster> possaledata) {
            boolean flag = false;
            for (int i = 0; i < possaledata.size(); i++) {
                String posqty = possaledata.get(i).getStock_liens();

                if (posqty.equals("")) {
                    showMessage("Please all Brand Liens Sold");
                    flag = true;
                    break;
                }
            }
            return flag;
        }
    */
    public boolean isValid(List<BrandMaster> possaledata) {
        boolean flag = false;
        for (int i = 0; i < possaledata.size(); i++) {
            String posqty = possaledata.get(i).getStock_liens();
            if (posqty != null && !posqty.equalsIgnoreCase("") && !posqty.equalsIgnoreCase("0")) {
               // showMessage("Please all Brand Liens Sold");
                flag = true;
                break;

            }

           /* if (posqty.equals("")) {
                showMessage("Please all Brand Liens Sold");
                flag = true;
                break;
            }*/

            if (flag) {
                break;
            }

        }

        return flag;
    }

    public void showMessage(String message) {
        Snackbar.make(btn_add, message, Snackbar.LENGTH_SHORT).show();
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(CustomerDataActivity.this);
                        builder.setMessage("Are you sure you want to delete the data ?")
                                .setCancelable(false)
                                .setPositiveButton("Yes",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int id) {
                                                insertedlist_Data.remove(position);
                                                if (insertedlist_Data.size() > 0) {
                                                    MyAdapter adapter = new MyAdapter(CustomerDataActivity.this, insertedlist_Data);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(CustomerDataActivity.this);
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
                                                    MyAdapter adapter = new MyAdapter(CustomerDataActivity.this, insertedlist_Data);
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

    private void clearingData() {

        customer_name.setText("");
        mobile_no.setText("");

        setDefaultdata();
    }

    private void setDefaultdata() {
        db.open();
        samplingChecklistData = db.getCustomer_data();
        feedback_question = new RouteAdapter(CustomerDataActivity.this, samplingChecklistData);
        lv_customer_list.setAdapter(feedback_question);
        lv_customer_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    private void popup() {

        dialog1 = new Dialog(CustomerDataActivity.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.dialog_customer_data);
        save = (Button) dialog1.findViewById(R.id.save);
        cancel = (Button) dialog1.findViewById(R.id.cancel);
        lv_customer_list = (RecyclerView) dialog1.findViewById(R.id.lv_customer_list);
        dialog1.setCancelable(false);
        dialog1.setCanceledOnTouchOutside(true);
        db.open();
        feedback_ques = db.getInsertCustomerPoData();

        if (feedback_ques.size() > 0) {
            feedback_question = new RouteAdapter(getApplicationContext(), feedback_ques);
            lv_customer_list.setAdapter(feedback_question);
            lv_customer_list.setLayoutManager(new LinearLayoutManager(CustomerDataActivity.this));
        } else {
            feedback_ques = db.getCustomer_data();
            if (feedback_ques.size() > 0) {
                feedback_question = new RouteAdapter(getApplicationContext(), feedback_ques);
                lv_customer_list.setAdapter(feedback_question);
                lv_customer_list.setLayoutManager(new LinearLayoutManager(this));

            }
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(CustomerDataActivity.this);
                builder.setMessage("Do you want to save the data ")
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                       /* long id1 = db.InsertFeedbackData(feedback_ques, visit_date, store_cd, key_id);
                                        if (id1 > 0) {
                                            Snackbar.make(save, "Data saved successfully", Snackbar.LENGTH_SHORT).show();
                                            dialog.cancel();
                                            dialog1.dismiss();

                                        } else {
                                            Snackbar.make(save, "Data not saved", Snackbar.LENGTH_SHORT).show();
                                        }
*/
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        dialog1.dismiss();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        dialog1.show();
    }

    public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.MyViewHolder> {
        private LayoutInflater inflator;
        List<BrandMaster> data = Collections.emptyList();

        public RouteAdapter(Context context, List<BrandMaster> data) {
            inflator = LayoutInflater.from(context);
            this.data = data;
        }

        @Override
        public RouteAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            View view = inflator.inflate(R.layout.dialog_list_, parent, false);
            RouteAdapter.MyViewHolder holder = new RouteAdapter.MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final RouteAdapter.MyViewHolder viewHolder, final int position) {

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


}
