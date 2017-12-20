package intelre.cpm.com.intelre.dailyentry;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import intelre.cpm.com.intelre.Database.INTALMerDB;
import intelre.cpm.com.intelre.R;
import intelre.cpm.com.intelre.constant.CommonString;
import intelre.cpm.com.intelre.gettersetter.RspGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.BrandMaster;
import intelre.cpm.com.intelre.gsonGetterSetter.JourneyPlan;
import intelre.cpm.com.intelre.gsonGetterSetter.RspDetailGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.StoreCategoryMaster;

public class RspDetailActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Context context;
    private SharedPreferences preferences;
    Toolbar toolbar;
    EditText edit_espname, edit_emailid, edit_phoneno;
    Spinner sp_brand, sp_registered;
    ArrayList<BrandMaster> list = new ArrayList<BrandMaster>();
    INTALMerDB db;
    private ArrayAdapter<CharSequence> city_adapter;
    String brand_name, brand_id;
    String[] irep_registered = {"Select", "YES", "NO"};
    //RspGetterSetter rspGetterSetter;
    StoreCategoryMaster storeCategoryMaster;

    String spinner_irep, spinner_irepregisterd;
    String str_rspname, str_emailid, str_phoneno;
    private String date;
    private ArrayList<JourneyPlan> storelist = new ArrayList<>();
    FloatingActionButton fab;
    ArrayList<StoreCategoryMaster> categorymaster = new ArrayList<StoreCategoryMaster>();
    //  StoreCategoryMaster storeCategoryMaster;
    String mode;
    String flag;
    String key_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rsp_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db = new INTALMerDB(this);
        db.open();

        declaration();
        //   setdata();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validation()) {
                    rspDetailsSaveData();
                }

            }
        });
    }

    void declaration() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;
        edit_espname = (EditText) findViewById(R.id.edit_espname);
        edit_emailid = (EditText) findViewById(R.id.edit_emailid);
        edit_phoneno = (EditText) findViewById(R.id.edit_phoneno);
        sp_brand = (Spinner) findViewById(R.id.sp_brand);
        sp_registered = (Spinner) findViewById(R.id.sp_registered);
        context = this;

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        date = preferences.getString(CommonString.KEY_DATE, null);
        toolbar.setTitle(getString(R.string.main_menu_activity_name) + " - " + date);
        getSupportActionBar().setTitle(getString(R.string.main_menu_activity_name) + " \n- " + date);

        storeCategoryMaster = (StoreCategoryMaster) getIntent().getSerializableExtra(CommonString.KEY_OBJECT);


        mode = getIntent().getStringExtra(CommonString.KEY_MODE);
        flag = getIntent().getStringExtra(CommonString.KEY_FLAG);


        /*rspGetterSetter = new RspGetterSetter();*/
        storeCategoryMaster = new StoreCategoryMaster();

        list = db.getBrandMasterData();

        ArrayAdapter aa3 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, irep_registered);
        aa3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_registered.setAdapter(aa3);


        city_adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
        city_adapter.add("Select Brand");
        for (int i = 0; i < list.size(); i++) {
            city_adapter.add(list.get(i).getBrand());
        }
        sp_brand.setAdapter(city_adapter);
        city_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_brand.setOnItemSelectedListener(this);
        sp_registered.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.sp_brand:
                if (position != 0) {
                    brand_name = list.get(position - 1).getBrand();
                    brand_id = list.get(position - 1).getBrandId().toString();
                    break;
                } else {
                    brand_id = "0";
                    brand_name = "";
                }
            case R.id.sp_registered:
                if (position != 0) {

                    spinner_irepregisterd = String.valueOf(parent.getSelectedItemPosition());
                   /* storeCategoryMaster.setIrepregistered(spinner_irep);*/
                    storeCategoryMaster.setIREPStatus(Boolean.valueOf(spinner_irep));
                } else {
                    storeCategoryMaster.setIREPStatus(Boolean.valueOf(""));
                }

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void rspDetailsSaveData() {
        str_rspname = edit_espname.getText().toString().replaceAll("[&^<>{}'$]", " ");
        str_emailid = edit_emailid.getText().toString().replaceAll("[&^<>{}'$]", " ");
        str_phoneno = edit_phoneno.getText().toString().replaceAll("[&^<>{}'$]", " ");

        storeCategoryMaster.setRspName(str_rspname);
        storeCategoryMaster.setEmail(str_emailid);
        storeCategoryMaster.setMobile(str_phoneno);
        storeCategoryMaster.setBrandId(Integer.valueOf(brand_id));
        storeCategoryMaster.setIREPStatus(Boolean.valueOf(spinner_irepregisterd));
        storeCategoryMaster.setRspId(Integer.valueOf("0"));

        String flag = "A";
        int rspid=0;
        long id = db.InsertRspDetailData(storeCategoryMaster, storelist.get(0).getStoreId(), date);
        if (id > 0) {
            Snackbar.make(fab, "Data saved successfully", Snackbar.LENGTH_SHORT).show();
            finish();
        } else {
            Snackbar.make(fab, "Data not saved", Snackbar.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        db.open();
        storelist = db.getStoreData(date);
        categorymaster=db.getRspDetailData(storelist.get(0).getStoreId());
    }

    public boolean validation() {

        boolean value = true;
        if (edit_espname.getText().toString().isEmpty()) {
            value = false;
            showMessage("Please Enter RSP Name");
        } else if (edit_emailid.getText().toString().isEmpty()) {
            value = false;
            showMessage("Please Enter Email Id");
        } else if (edit_phoneno.getText().toString().isEmpty()) {
            value = false;
            showMessage("Please Enter Phone No");
        } else if (sp_brand.getSelectedItemPosition() == 0) {
            value = false;
            showMessage("Please Select Brand");
        } else if (sp_registered.getSelectedItemPosition() == 0) {
            value = false;
            showMessage("Please Select IRP Registered");
        } else {

            value = true;
        }
        return value;

    }

    public void showMessage(String message) {

        Snackbar.make(fab, message, Snackbar.LENGTH_SHORT).show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == android.R.id.home) {

            // NavUtils.navigateUpFromSameTask(this);
            finish();


            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);

        }

        return super.onOptionsItemSelected(item);
    }

    private void setdata() {

        edit_espname.setText(storeCategoryMaster.getRspName());
        edit_emailid.setText(storeCategoryMaster.getEmail());
        edit_phoneno.setText(storeCategoryMaster.getMobile());

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getBrandId() == storeCategoryMaster.getBrandId()) {
                sp_brand.setSelection(i);
                break;
            }
        }

        for (int i = 0; i < irep_registered.length; i++) {
            if (irep_registered[i].equalsIgnoreCase("YES")) {
                sp_registered.setSelection(i);
                break;
            }
            if (irep_registered[i].equalsIgnoreCase("No")) {
                sp_registered.setSelection(i);
                break;
            }
        }


    }
}
