package intelre.cpm.com.intelre.dailyentry;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import intelre.cpm.com.intelre.Database.INTEL_RE_DB;
import intelre.cpm.com.intelre.R;
import intelre.cpm.com.intelre.constant.CommonString;
import intelre.cpm.com.intelre.delegates.CoverageBean;
import intelre.cpm.com.intelre.gsonGetterSetter.JCPGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.JourneyPlan;
import intelre.cpm.com.intelre.gsonGetterSetter.NonWorkingReason;

public class NonWorkingReasonActivity extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    ArrayList<NonWorkingReason> reasondata = new ArrayList<>();
    private Spinner reasonspinner;
    private INTEL_RE_DB database;
    String reasonname = "", reasonid = "", entry_allow = "", image_allow = "", image, intime = "";
    Button save;
    private ArrayAdapter<CharSequence> reason_adapter;
    protected String _path, str;
    protected String _pathforcheck = "";
    private String image1 = "";
    private SharedPreferences preferences;
    String _UserId, visit_date, store_id;
    protected boolean status = true;
    AlertDialog alert;
    ImageButton camera;
    RelativeLayout rel_cam;
    ArrayList<JourneyPlan> jcp;
    boolean update_flag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_non_working_reason);
        reasonspinner = (Spinner) findViewById(R.id.spinner2);
        camera = (ImageButton) findViewById(R.id.imgcam);
        save = (Button) findViewById(R.id.save);
        rel_cam = (RelativeLayout) findViewById(R.id.relimgcam);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        _UserId = preferences.getString(CommonString.KEY_USERNAME, "");
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        store_id = preferences.getString(CommonString.KEY_STORE_CD, "");
        getSupportActionBar().setTitle("Non Working -" + visit_date);

        database = new INTEL_RE_DB(this);
        database.open();
        str = CommonString.FILE_PATH;
        jcp = database.getStoreData(visit_date);
        if (jcp.size() > 0) {
            try {
                for (int i = 0; i < jcp.size(); i++) {
                    boolean flag = false;
                    if (jcp.get(i).getUploadStatus().equals(CommonString.KEY_U) ||
                            jcp.get(i).getUploadStatus().equals(CommonString.KEY_D)
                            || jcp.get(i).getUploadStatus().equals(CommonString.KEY_P)
                            || jcp.get(i).getUploadStatus().equals(CommonString.STORE_STATUS_LEAVE)) {
                        flag = true;
                        reasondata.clear();
                        reasondata = database.getNonWorkingDataByFlag(flag);
                        break;
                    } else {
                        reasondata = database.getNonWorkingDataByFlag(flag);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        reason_adapter = new ArrayAdapter<>(this, R.layout.spinner_custom_item);
        reason_adapter.add("Select Reason");
        for (int i = 0; i < reasondata.size(); i++) {
            reason_adapter.add(reasondata.get(i).getReason());
        }
        intime = getCurrentTime();
        reasonspinner.setAdapter(reason_adapter);
        reason_adapter.setDropDownViewResource(R.layout.spinner_custom_item);
        reasonspinner.setOnItemSelectedListener(this);
        camera.setOnClickListener(this);
        save.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        if (!update_flag) {
            finish();
            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(NonWorkingReasonActivity.this);
            builder.setMessage(CommonString.ONBACK_ALERT_MESSAGE)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                            dialog.dismiss();
                            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
                               long arg3) {
        // TODO Auto-generated method stub

        switch (arg0.getId()) {
            case R.id.spinner2:
                if (position != 0) {
                    reasonname = reasondata.get(position - 1).getReason();
                    reasonid = reasondata.get(position - 1).getReasonId().toString();
                    entry_allow = reasondata.get(position - 1).getEntryAllow().toString();
                    image_allow = reasondata.get(position - 1).getImageAllow().toString();
                    if (image_allow.equalsIgnoreCase("true")) {
                        rel_cam.setVisibility(View.VISIBLE);
                    } else {
                        rel_cam.setVisibility(View.GONE);
                    }
                } else {
                    reasonname = "";
                    reasonid = "";
                    image_allow = "";
                    entry_allow = "";
                }
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
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
                        camera.setImageDrawable(getResources().getDrawable(R.mipmap.camera_green));
                        image1 = _pathforcheck;
                        _pathforcheck = "";
                    }
                }
                break;
        }
    }

    public boolean imageAllowed() {
        boolean result = true;
        if (image_allow.equalsIgnoreCase("true")) {
            if (image1.equalsIgnoreCase("")) {
                result = false;
            }
        }

        return result;

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v.getId() == R.id.imgcam) {
            _pathforcheck = store_id + "_NONWORKING_" + visit_date.replace("/", "") +
                    "_" + getCurrentTime().replace(":", "") + ".jpg";
            _path = CommonString.FILE_PATH + _pathforcheck;
            startCameraActivity();
        }
        if (v.getId() == R.id.save) {
            if (validatedata()) {
                update_flag = true;
                if (imageAllowed()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(NonWorkingReasonActivity.this);
                    builder.setMessage("Do you want to save the data ")
                            .setCancelable(false)
                            .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog,
                                                int id) {
                                            alert.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                                            if (entry_allow.equals("0")) {
                                                database.deleteAllTables();
                                                for (int i = 0; i < jcp.size(); i++) {
                                                    String stoteid = jcp.get(i).getStoreId().toString();
                                                    CoverageBean cdata = new CoverageBean();
                                                    cdata.setStoreId(stoteid);
                                                    cdata.setVisitDate(visit_date);
                                                    cdata.setUserId(_UserId);
                                                    cdata.setReason(reasonname);
                                                    cdata.setReasonid(reasonid);
                                                    cdata.setLatitude("0.0");
                                                    cdata.setLongitude("0.0");
                                                    cdata.setImage(image1);
                                                    cdata.setCkeckout_image(image1);
                                                    database.InsertCoverageData(cdata);
                                                    database.updateJaurneyPlanSpecificStoreStatus(store_id, visit_date,
                                                            CommonString.STORE_STATUS_LEAVE);
                                                    SharedPreferences.Editor editor = preferences.edit();
                                                    editor.putString(CommonString.KEY_STOREVISITED_STATUS + stoteid, "No");
                                                    editor.putString(CommonString.KEY_STOREVISITED_STATUS, "");
                                                    editor.commit();
                                                }

                                            } else {
                                                CoverageBean cdata = new CoverageBean();
                                                cdata.setStoreId(store_id);
                                                cdata.setVisitDate(visit_date);
                                                cdata.setUserId(_UserId);
                                                cdata.setReason(reasonname);
                                                cdata.setReasonid(reasonid);
                                                cdata.setLatitude("0.0");
                                                cdata.setLongitude("0.0");
                                                cdata.setImage(image1);
                                                cdata.setCkeckout_image(image1);
                                                database.InsertCoverageData(cdata);
                                                database.updateJaurneyPlanSpecificStoreStatus(store_id, visit_date,
                                                        CommonString.STORE_STATUS_LEAVE);
                                            }
                                            finish();
                                        }
                                    })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog,
                                                int id) {
                                            dialog.cancel();
                                        }
                                    });

                    alert = builder.create();
                    alert.show();
                } else {
                    Snackbar.make(reasonspinner,
                            "Please Capture Nonworking Image", Snackbar.LENGTH_SHORT).show();
                }
            } else {
                Snackbar.make(reasonspinner,
                        "Please Select a Reason", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    public boolean validatedata() {
        boolean result = false;
        if (reasonid != null && !reasonid.equalsIgnoreCase("")) {
            result = true;
        }
        return result;
    }


    public String getCurrentTime() {
        Calendar m_cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String cdate = formatter.format(m_cal.getTime());
        return cdate;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (!update_flag) {
                finish();
                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(NonWorkingReasonActivity.this);
                builder.setMessage(CommonString.ONBACK_ALERT_MESSAGE)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                                dialog.dismiss();
                                overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
