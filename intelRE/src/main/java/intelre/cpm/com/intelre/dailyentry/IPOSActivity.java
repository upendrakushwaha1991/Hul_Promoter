package intelre.cpm.com.intelre.dailyentry;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import intelre.cpm.com.intelre.Database.INTEL_RE_DB;
import intelre.cpm.com.intelre.R;
import intelre.cpm.com.intelre.constant.CommonString;
import intelre.cpm.com.intelre.gsonGetterSetter.SkuMaster;

public class IPOSActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String sku_cd = "", sku = "";
    String store_cd, visit_date, user_type, username, Error_Message;
    INTEL_RE_DB db;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor = null;
    EditText number_TXT, machine_onEDT, ipos_EDT;
    ImageView ipos_img;
    RecyclerView recycle_ipos;
    FloatingActionButton fab, btn_add;
    Spinner spin_sku;
    String _pathforcheck, _path, img1 = "";
    boolean checkflag = true;
    ArrayAdapter<CharSequence> adapter;
    MyAdapter myAdapter;
    ArrayList<SkuMaster> skumasterList = new ArrayList<>();
    ArrayList<SkuMaster> secCompleteIPOSDATA = new ArrayList<>();
    boolean addflag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipos);
        iposUIDATA();
        fab = findViewById(R.id.fab);
        btn_add = findViewById(R.id.btn_add);
        validateDATA();
        setDataToListView();
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    if (duplicateValue(sku_cd)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(IPOSActivity.this);
                        builder.setTitle("Parinaam").setMessage("Do you want to add data");
                        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                addflag = true;
                                SkuMaster info = new SkuMaster();
                                info.setSku(sku);
                                info.setSkuId(Integer.valueOf(sku_cd));
                                info.setNumber(number_TXT.getText().toString());
                                info.setMachine_on(machine_onEDT.getText().toString());
                                info.setIpos(ipos_EDT.getText().toString());
                                info.setIpos_img(img1);
                                secCompleteIPOSDATA.add(info);
                                myAdapter = new MyAdapter(IPOSActivity.this, secCompleteIPOSDATA);
                                recycle_ipos.setAdapter(myAdapter);
                                recycle_ipos.setLayoutManager(new LinearLayoutManager(IPOSActivity.this));
                                myAdapter.notifyDataSetChanged();
                                Snackbar.make(btn_add, "Data has been added", Snackbar.LENGTH_SHORT).show();
                                ipos_img.setImageResource(R.mipmap.camera_orange);
                                spin_sku.setSelection(0);
                                number_TXT.setText("");
                                machine_onEDT.setText("");
                                ipos_EDT.setText("");
                                img1 = "";


                            }
                        });
                        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.show();
                    } else {
                        Snackbar.make(btn_add, "This " + sku + " alredy exist. Please select another ", Snackbar.LENGTH_LONG).show();

                    }

                } else {
                    Snackbar.make(btn_add, Error_Message, Snackbar.LENGTH_LONG).show();
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (secCompleteIPOSDATA.size() > 0 && addflag) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(IPOSActivity.this);
                    builder.setTitle("Parinaam").setMessage("Do you want to save data");
                    builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            db.open();
                            db.insertIPOSData(store_cd, visit_date, secCompleteIPOSDATA);
                            Snackbar.make(btn_add, "Data has been saved", Snackbar.LENGTH_SHORT).show();
                            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                            IPOSActivity.this.finish();
                        }
                    });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();

                        }
                    });
                    builder.show();
                } else {
                    Snackbar.make(fab, "Please add first", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private void iposUIDATA() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //edittext
        number_TXT = findViewById(R.id.number_TXT);
        machine_onEDT = findViewById(R.id.machine_onEDT);
        ipos_EDT = findViewById(R.id.ipos_EDT);
        spin_sku = findViewById(R.id.spin_sku);
        ipos_img = findViewById(R.id.ipos_img);

        recycle_ipos = findViewById(R.id.recycle_ipos);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        store_cd = preferences.getString(CommonString.KEY_STORE_CD, null);
        username = preferences.getString(CommonString.KEY_USERNAME, null);
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        user_type = preferences.getString(CommonString.KEY_USER_TYPE, null);
        getSupportActionBar().setTitle("IPOS - " + visit_date);
        db = new INTEL_RE_DB(this);
        db.open();
    }

    private void validateDATA() {
        db.open();
        skumasterList = db.getSkuMasterData();
        if (skumasterList.size() > 0) {
            adapter = new ArrayAdapter<>(IPOSActivity.this, R.layout.spinner_custom_item);
            adapter.add("Select Sku");
            for (int i = 0; i < skumasterList.size(); i++) {
                adapter.add(skumasterList.get(i).getSku());
            }
            adapter.setDropDownViewResource(R.layout.spinner_custom_item);
            spin_sku.setAdapter(adapter);
            spin_sku.setOnItemSelectedListener(IPOSActivity.this);


        }

        ipos_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _pathforcheck = store_cd + "_" + sku_cd + "_IPOSING_" + visit_date.replace("/", "") +
                        "_" + getCurrentTime().replace(":", "") + ".jpg";
                _path = CommonString.FILE_PATH + _pathforcheck;
                startCameraActivity();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        switch (adapterView.getId()) {
            case R.id.spin_sku:
                if (position != 0) {
                    sku = skumasterList.get(position - 1).getSku();
                    sku_cd = skumasterList.get(position - 1).getSkuId().toString();
                } else {
                    sku_cd = "";
                    sku = "";
                }
                break;
        }

    }

    private boolean validate() {
        boolean status = true;
        if (spin_sku.getSelectedItem().toString().contains("Select Sku")) {
            Error_Message = "Please Select Sku";
            status = false;
        } else if (number_TXT.getText().toString().equals("")) {
            Error_Message = "Please enter IPOS Number";
            status = false;
        } else if (machine_onEDT.getText().toString().equals("")) {
            Error_Message = "Please enter IPOS Machine On";
            status = false;
        } else if (ipos_EDT.getText().toString().equals("")) {
            Error_Message = "Please enter IPOS ";
            status = false;
        } else if (img1.equals("")) {
            Error_Message = "Please click IPOS image";
            status = false;
        }
        return status;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public String getCurrentTime() {
        Calendar m_cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String cdate = formatter.format(m_cal.getTime());
        return cdate;
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                IPOSActivity.this);
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            AlertDialog.Builder builder = new AlertDialog.Builder(IPOSActivity.this);
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

        return super.onOptionsItemSelected(item);
    }

    protected void startCameraActivity() {
        try {
            Log.i("MakeMachine", "startCameraActivity()");
            File file = new File(_path);
            Uri outputFileUri = Uri.fromFile(file);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            startActivityForResult(intent, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.i("MakeMachine", "resultCode: " + resultCode);
        switch (resultCode) {
            case 0:
                Log.i("MakeMachine", "User cancelled");
                break;

            case -1:


                if (_pathforcheck != null && !_pathforcheck.equals("")) {
                    if (new File(CommonString.FILE_PATH + _pathforcheck).exists()) {
                        ipos_img.setImageResource(R.mipmap.camera_green);
                        img1 = _pathforcheck;
                        _pathforcheck = "";
                    }
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    public void setDataToListView() {
        try {
            secCompleteIPOSDATA = db.getiposinseteddata(store_cd);
            if (secCompleteIPOSDATA.size() > 0) {
                Collections.reverse(secCompleteIPOSDATA);
                myAdapter = new MyAdapter(this, secCompleteIPOSDATA);
                recycle_ipos.setAdapter(myAdapter);
                recycle_ipos.setLayoutManager(new LinearLayoutManager(IPOSActivity.this));
                adapter.notifyDataSetChanged();
            }

        } catch (Exception e) {
            Log.d("Exception when fetching", e.toString());
        }
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private LayoutInflater inflator;
        Context context;
        ArrayList<SkuMaster> insertedlist_Data;

        MyAdapter(Context context, ArrayList<SkuMaster> insertedlist_Data) {
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
            View view = inflator.inflate(R.layout.secondary_adapter_ipos, parent, false);
            MyAdapter.MyViewHolder holder = new MyAdapter.MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyAdapter.MyViewHolder holder, final int position) {
            holder.status.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (insertedlist_Data.get(position).getKey_id() == null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(IPOSActivity.this);
                        builder.setMessage("Are you sure you want to Delete")
                                .setCancelable(false)
                                .setPositiveButton("Yes",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int id) {
                                                insertedlist_Data.remove(position);
                                                notifyDataSetChanged();
                                                if (insertedlist_Data.size() > 0) {
                                                    MyAdapter adapter = new MyAdapter(IPOSActivity.this, insertedlist_Data);
                                                    recycle_ipos.setAdapter(adapter);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(IPOSActivity.this);
                        builder.setMessage("Are you sure you want to Delete")
                                .setCancelable(false)
                                .setPositiveButton("Yes",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int id) {
                                                String listid = insertedlist_Data.get(position).getKey_id();
                                                db.remove_ipos(listid);
                                                insertedlist_Data.remove(position);
                                                notifyDataSetChanged();
                                                if (insertedlist_Data.size() > 0) {
                                                    MyAdapter adapter = new MyAdapter(IPOSActivity.this,
                                                            insertedlist_Data);
                                                    recycle_ipos.setAdapter(adapter);
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

            holder.ipos_sku.setText(insertedlist_Data.get(position).getSku());
            holder.ipos_no.setText(insertedlist_Data.get(position).getNumber());
            holder.ipos_on.setText(insertedlist_Data.get(position).getMachine_on());
            holder.ipos_i.setText(insertedlist_Data.get(position).getIpos());
            holder.ipos_sku.setId(position);
            holder.ipos_no.setId(position);
            holder.ipos_on.setId(position);
            holder.ipos_i.setId(position);
            holder.status.setId(position);
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView ipos_sku, ipos_no, ipos_on, ipos_i;
            ImageView status;

            public MyViewHolder(View convertView) {
                super(convertView);
                ipos_sku = convertView.findViewById(R.id.ipos_sku);
                ipos_no = convertView.findViewById(R.id.ipos_no);
                ipos_on = convertView.findViewById(R.id.ipos_on);
                ipos_i = convertView.findViewById(R.id.ipos_i);
                status = convertView.findViewById(R.id.imgDelRow);
            }
        }
    }

    private boolean duplicateValue(String sku_cd) {
        boolean status = true;
        if (secCompleteIPOSDATA.size() > 0) {
            for (int i = 0; i < secCompleteIPOSDATA.size(); i++) {
                if (secCompleteIPOSDATA.get(i).getSkuId().toString().equals(sku_cd)) {
                    status = false;
                    break;

                }
            }
        }
        return status;
    }

}
