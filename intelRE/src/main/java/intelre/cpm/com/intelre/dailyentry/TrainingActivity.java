package intelre.cpm.com.intelre.dailyentry;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.Button;
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
import intelre.cpm.com.intelre.constant.AlertandMessages;
import intelre.cpm.com.intelre.constant.CommonString;
import intelre.cpm.com.intelre.gettersetter.TrainingGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.BrandMaster;
import intelre.cpm.com.intelre.gsonGetterSetter.StoreCategoryMaster;
import intelre.cpm.com.intelre.gsonGetterSetter.WindowChecklist;
import intelre.cpm.com.intelre.gsonGetterSetter.WindowMaster;

public class TrainingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    String  intime,_pathforcheck, _path, str, image1 = "";
    String visit_date, userId, user_type, store_cd;
    Spinner sp_rsp,sp_trainingtype,sp_topic;
    ImageView img_photoMar;
    Button btn_add;
    RecyclerView marketinteligence_list;
    private SharedPreferences preferences;
    Toolbar toolbar;
    INTEL_RE_DB db;
    private Context context;
    String date;
    ArrayList<StoreCategoryMaster> rsplist = new ArrayList<>();
    ArrayList<WindowMaster> trainingtypelist = new ArrayList<WindowMaster>();
    ArrayList<WindowChecklist> trainingtopiclist = new ArrayList<WindowChecklist>();
    private ArrayAdapter<CharSequence> rsp_adapter;
    private ArrayAdapter<CharSequence> training_adapter;
    private ArrayAdapter<CharSequence> trainingtopic_adapter;
    String rsp_name,rsp_id,training_name,training_nameid,training_topic,training_topicid;
    // TrainingGetterSetter trainingGetterSetter;
    ArrayList<TrainingGetterSetter> inserteslistData = new ArrayList<>();
    MyAdapter adapter;
    boolean sampleaddflag = false;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        db = new INTEL_RE_DB(this);
        db.open();
        declaration();
        setDataToListView();

    }

    void declaration() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = this;
        sp_rsp = (Spinner) findViewById(R.id.sp_rsp);
        sp_trainingtype = (Spinner) findViewById(R.id.sp_trainingtype);
        sp_topic = (Spinner) findViewById(R.id.sp_topic);
        img_photoMar = (ImageView) findViewById(R.id.img_photoMar);
        btn_add = (Button) findViewById(R.id.btn_add);
        marketinteligence_list = (RecyclerView) findViewById(R.id.marketinteligence_list);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        str = CommonString.FILE_PATH;
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        userId = preferences.getString(CommonString.KEY_USERNAME, null);
        user_type = preferences.getString(CommonString.KEY_USER_TYPE, null);
        store_cd = preferences.getString(CommonString.KEY_STORE_CD, null);
        date = preferences.getString(CommonString.KEY_DATE, null);
        getSupportActionBar().setTitle(getString(R.string.title_activity_training) + " \n- " + date);

        db.open();
        rsplist = db.getRspDetailData(store_cd);
        trainingtypelist=db.getTrainingTypeData();
        /*trainingtopiclist=db.getTrainingTopicData();*/

        sp_rsp.setOnItemSelectedListener(this);
        sp_trainingtype.setOnItemSelectedListener(this);
        sp_topic.setOnItemSelectedListener(this);
        img_photoMar.setOnClickListener(this);
        fab.setOnClickListener(this);
        btn_add.setOnClickListener(this);


        rsp_adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
        rsp_adapter.add("-Select RSP-");

        for (int i = 0; i < rsplist.size(); i++) {
            rsp_adapter.add(rsplist.get(i).getRspName());

        }
        sp_rsp.setAdapter(rsp_adapter);
        rsp_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        training_adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
        training_adapter.add("-Select Training Type-");
        for (int i = 0; i < trainingtypelist.size(); i++) {
            training_adapter.add(trainingtypelist.get(i).getTrainingType());

        }
        sp_trainingtype.setAdapter(training_adapter);
        training_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        trainingtopic_adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
        trainingtopic_adapter.add("-Select Training Topic-");
        /*for (int i = 0; i < trainingtopiclist.size(); i++) {
            trainingtopic_adapter.add(trainingtopiclist.get(i).getTopic());

        }

        sp_topic.setAdapter(trainingtopic_adapter);
        trainingtopic_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);*/
        sp_topic.setAdapter(trainingtopic_adapter);
        trainingtopic_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.sp_rsp:
                if (position != 0) {
                    rsp_name = rsplist.get(position - 1).getRspName();
                    rsp_id = rsplist.get(position - 1).getRspId().toString();
                    break;
                } else {
                    rsp_id = "0";
                    rsp_name = "";
                }
            case R.id.sp_trainingtype:
                trainingtopiclist.clear();
                trainingtopic_adapter.clear();
                 trainingtopic_adapter.add("-Select Training Topic-");
                if (position != 0) {
                    training_name = trainingtypelist.get(position - 1).getTrainingType();
                    training_nameid = trainingtypelist.get(position - 1).getTrainingTypeId().toString();

                    trainingtopiclist=db.getTrainingTopicData(training_nameid);
                    for (int i = 0; i < trainingtopiclist.size(); i++) {
                        trainingtopic_adapter.add(trainingtopiclist.get(i).getTopic());

                    }

                    sp_topic.setAdapter(trainingtopic_adapter);
                    trainingtopic_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    break;
                } else {
                    training_nameid = "0";
                    training_name = "";
                }
            case R.id.sp_topic:

                if (position != 0) {
                    training_topic = trainingtopiclist.get(position - 1).getTopic();
                    training_topicid = trainingtopiclist.get(position - 1).getTopicId().toString();

                    break;

                } else {
                    training_topicid = "0";
                    training_topic = "";
                }


        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        switch (id) {
            case R.id.img_photoMar:
                _pathforcheck = store_cd +"_"+training_nameid+ "_TRAININGIMG_" + visit_date.replace("/", "") + getCurrentTime().replace(":", "") + ".jpg";
                _path = CommonString.FILE_PATH + _pathforcheck;
                intime = getCurrentTime();
                startCameraActivity();

            case R.id.btn_add:
                //validationDuplication
                if (validation()) {
                    if (validationDuplication()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(TrainingActivity.this);
                        builder.setMessage("Are you sure you want to add")
                                .setCancelable(false)
                                .setPositiveButton("Yes",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                db.open();
                                                TrainingGetterSetter trainingGetterSetter = new TrainingGetterSetter();
                                                trainingGetterSetter.setRspname_cd(rsp_id);
                                                trainingGetterSetter.setRspname(rsp_name);
                                                trainingGetterSetter.setTrainingtype_cd(training_nameid);
                                                trainingGetterSetter.setTrainingtype(training_name);
                                                trainingGetterSetter.setTopic_cd(training_topicid);
                                                trainingGetterSetter.setTopic(training_topic);
                                                trainingGetterSetter.setPhoto(image1);

                                                //marketIntelligenceG.setExists(true);
                                                inserteslistData.add(trainingGetterSetter);
                                                adapter = new MyAdapter(TrainingActivity.this, inserteslistData);
                                                marketinteligence_list.setAdapter(adapter);
                                                marketinteligence_list.setLayoutManager(new LinearLayoutManager(TrainingActivity.this));
                                                adapter.notifyDataSetChanged();

                                                sp_rsp.setSelection(0);
                                                sp_trainingtype.setSelection(0);
                                                sp_topic.setSelection(0);

                                                image1 = "";
                                                sampleaddflag = true;
                                                img_photoMar.setImageResource(R.mipmap.camera_green);
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
                    } else {
                        Snackbar.make(btn_add, "Already added ", Snackbar.LENGTH_SHORT).show();
                    }
                }

                break;
            case R.id.fab:
                if (inserteslistData.size() > 0) {
                    if (sampleaddflag) {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(TrainingActivity.this);
                        builder1.setMessage("Are you sure you want to save data")
                                .setCancelable(false)
                                .setPositiveButton("Yes",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                db.open();
                                                db.insertTrainingData(store_cd, user_type, visit_date, inserteslistData);
                                                TrainingActivity.this.finish();
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
                        Snackbar.make(btn_add, "Please add data", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(btn_add, "Please add data", Snackbar.LENGTH_SHORT).show();
                }
                break;
        }
    }
    public String getCurrentTime() {
        Calendar m_cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String cdate = formatter.format(m_cal.getTime());
        return cdate;
    }
    protected void startCameraActivity() {

        try {
            Log.i("MakeMachine", "startCameraActivity()");
            File file = new File(_path);
            Uri outputFileUri = Uri.fromFile(file);

            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            startActivityForResult(intent, 0);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    @SuppressWarnings("deprecation")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("MakeMachine", "resultCode: " + resultCode);
        switch (resultCode) {
            case 0:
                Log.i("MakeMachine", "User cancelled");
                break;

            case -1:
                if (_pathforcheck != null && !_pathforcheck.equals("")) {
                    if (new File(str + _pathforcheck).exists()) {
                        img_photoMar.setImageResource(R.mipmap.camera_green);
                        image1 = _pathforcheck;
                    }
                    _pathforcheck = "";
                }
                break;
        }

    }

    public void showMessage(String message) {
        Snackbar.make(btn_add, message, Snackbar.LENGTH_SHORT).show();
    }

    public boolean validation() {
        boolean value = true;
        if (sp_rsp.getSelectedItem().toString().equalsIgnoreCase("-Select RSP-")) {
            value = false;
            showMessage("Please Select RSP Dropdown");

        } else if (sp_trainingtype.getSelectedItem().toString().equalsIgnoreCase("-Select Training Type-")) {
            value = false;
            showMessage("Please Select Training Type Dropdown");
        }else if (sp_topic.getSelectedItem().toString().equalsIgnoreCase("-Select Training Topic-")) {
            value = false;
            showMessage("Please Training Topic Dropdown");
        } else if (image1.equals("")) {
            value = false;
            showMessage("Please Capture Photo");
        }

        return value;
    }

    public void setDataToListView() {
        try {
            inserteslistData = db.getinsertedTrainingData(store_cd, visit_date);
            if (inserteslistData.size() > 0) {
                // save_fab.setText("Update");
                Collections.reverse(inserteslistData);
                adapter = new MyAdapter(this, inserteslistData);
                marketinteligence_list.setAdapter(adapter);
                marketinteligence_list.setLayoutManager(new LinearLayoutManager(TrainingActivity.this));
                adapter.notifyDataSetChanged();
            }

        } catch (Exception e) {
            Log.d("Exception when fetching", e.toString());
        }
    }


    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private LayoutInflater inflator;
        Context context;
        ArrayList<TrainingGetterSetter> insertedlist_Data;

        MyAdapter(Context context, ArrayList<TrainingGetterSetter> insertedlist_Data) {
            inflator = LayoutInflater.from(context);
            this.context = context;
            this.insertedlist_Data = insertedlist_Data;

        }

        @Override
        public int getItemCount() {
            return insertedlist_Data.size();

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // View view = inflator.inflate(R.layout.secondary_adapter, parent, false);
            View view = inflator.inflate(R.layout.secondary_training_adapter, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            holder.remove.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (insertedlist_Data.get(position).getKey_id() == null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(TrainingActivity.this);
                        builder.setMessage("Are you sure you want to Delete")
                                .setCancelable(false)
                                .setPositiveButton("Yes",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int id) {
                                                insertedlist_Data.remove(position);
                                                if (insertedlist_Data.size() > 0) {
                                                    MyAdapter adapter = new MyAdapter(TrainingActivity.this, insertedlist_Data);
                                                    marketinteligence_list.setAdapter(adapter);
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(TrainingActivity.this);
                        builder.setMessage("Are you sure you want to Delete")
                                .setCancelable(false)
                                .setPositiveButton("Yes",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int id) {
                                                String listid = insertedlist_Data.get(position).getKey_id();
                                                db.remove_training(listid);
                                                insertedlist_Data.remove(position);
                                                if (insertedlist_Data.size() > 0) {
                                                    MyAdapter adapter = new MyAdapter(TrainingActivity.this, insertedlist_Data);
                                                    marketinteligence_list.setAdapter(adapter);
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

            holder.txt_comp.setText(insertedlist_Data.get(position).getRspname());
            holder.txt_cate.setText(insertedlist_Data.get(position).getTopic());
            holder.txt_pro.setText(insertedlist_Data.get(position).getTrainingtype());

            holder.txt_comp.setId(position);
            holder.txt_cate.setId(position);
            holder.txt_pro.setId(position);
            holder.remove.setId(position);
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView txt_comp, txt_cate, txt_pro;
            ImageView remove;

            public MyViewHolder(View convertView) {
                super(convertView);
                txt_comp = (TextView) convertView.findViewById(R.id.txt_comp);
                txt_pro = (TextView) convertView.findViewById(R.id.txt_pro);
                txt_cate = (TextView) convertView.findViewById(R.id.txt_cate);
                remove = (ImageView) convertView.findViewById(R.id.imgDelRow);
            }
        }
    }

    public boolean validationDuplication() {
        boolean value = true;
        if (inserteslistData.size() > 0) {
            for (int i = 0; i < inserteslistData.size(); i++) {
                if (inserteslistData.get(i).getRspname_cd().equals(rsp_name) &&
                        inserteslistData.get(i).getTrainingtype_cd().equals(training_name) &&
                        inserteslistData.get(i).getTopic_cd().equals(training_topic)) {
                    value = false;
                    break;
                }
            }
        }

        return value;
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

    @Override
    public void onBackPressed() {
        new AlertandMessages().backpressedAlert((Activity) context);
    }

}
