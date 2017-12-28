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
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import intelre.cpm.com.intelre.Database.INTEL_RE_DB;
import intelre.cpm.com.intelre.R;
import intelre.cpm.com.intelre.constant.CommonString;
import intelre.cpm.com.intelre.gsonGetterSetter.JourneyPlan;
import intelre.cpm.com.intelre.gsonGetterSetter.PosmMaster;

public class SoftMerchVisibilityActivity extends AppCompatActivity {
    INTEL_RE_DB db;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor = null;
    String store_cd, visit_date, user_type, username, Error_Message;
    ExpandableListView lvExp_softmerch;
    FloatingActionButton fab;
    List<PosmMaster> listDataHeader;
    List<PosmMaster> questionList;
    HashMap<PosmMaster, List<PosmMaster>> listDataChild;
    ExpandableListAdapter listAdapter;
    static int grp_position = -1;
    static int child_position = -1;
    String _pathforcheck, _path, img1 = "";
    boolean checkflag = true;
    ArrayList<Integer> checkHeaderArray = new ArrayList<>();
    ArrayList<JourneyPlan> specificStoreDATA = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soft_merch_visibility);
        auditUI();
        //save audit data
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lvExp_softmerch.clearFocus();
                if (validateData(listDataChild, listDataHeader)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SoftMerchVisibilityActivity.this);
                    builder.setTitle("Parinaam").setMessage(R.string.alertsaveData);
                    builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            db.open();
                            db.insertVisibilitySoftMerchData(store_cd, listDataChild, listDataHeader);
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
                    listAdapter.notifyDataSetChanged();
                    Snackbar.make(lvExp_softmerch, Error_Message, Snackbar.LENGTH_LONG).show();
                }

            }
        });
    }

    private void auditUI() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lvExp_softmerch = findViewById(R.id.lvExp_softmerch);
        fab = findViewById(R.id.fab);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        store_cd = preferences.getString(CommonString.KEY_STORE_CD, null);
        username = preferences.getString(CommonString.KEY_USERNAME, null);
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        user_type = preferences.getString(CommonString.KEY_USER_TYPE, null);
        getSupportActionBar().setTitle("Visibility-Soft Merch-" + visit_date);
        db = new INTEL_RE_DB(this);
        db.open();
        prepareListData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        // setting list adapter
        lvExp_softmerch.setAdapter(listAdapter);

        lvExp_softmerch.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

            }

            @Override
            public void onScrollStateChanged(AbsListView arg0, int arg1) {
                lvExp_softmerch.invalidateViews();

                InputMethodManager inputManager = (InputMethodManager) getApplicationContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getCurrentFocus() != null) {
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    getCurrentFocus().clearFocus();
                }
            }

        });

        // Listview Group click listener
        lvExp_softmerch.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_LONG).show();
                return false;
            }
        });

        // Listview Group expanded listener
        lvExp_softmerch.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                InputMethodManager inputManager = (InputMethodManager) getApplicationContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getCurrentFocus() != null) {
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    getCurrentFocus().clearFocus();
                }
            }
        });

        // Listview Group collasped listener
        lvExp_softmerch.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                InputMethodManager inputManager = (InputMethodManager) getApplicationContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getCurrentFocus() != null) {
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    getCurrentFocus().clearFocus();
                }


            }
        });

        // Listview on child click listener
        lvExp_softmerch.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                return false;
            }
        });

    }

    private void prepareListData() {
        db.open();
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
        specificStoreDATA = new ArrayList<>();
        specificStoreDATA = db.getSpecificStoreData(store_cd);
        listDataHeader = db.getSofMerchPosmHeaderData(specificStoreDATA.get(0).getRegionId(),
                specificStoreDATA.get(0).getClassificationId(),
                specificStoreDATA.get(0).getStoreTypeId());
        if (listDataHeader.size() > 0) {
            for (int i = 0; i < listDataHeader.size(); i++) {
                questionList = db.getVisibilitySoftMerchInsertedData(store_cd, listDataHeader.get(i).getPosmTypeId());
                if (questionList.size() > 0) {
                    fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.edit_txt));
                } else {
                    questionList = db.getVisibilitySoftMerchChildData(listDataHeader.get(i).getPosmTypeId());
                }
                listDataChild.put(listDataHeader.get(i), questionList); // Header, Child data
            }
        }
    }


    public class ExpandableListAdapter extends BaseExpandableListAdapter {
        private Context _context;
        private List<PosmMaster> _listDataHeader;
        private HashMap<PosmMaster, List<PosmMaster>> _listDataChild;

        public ExpandableListAdapter(Context context, List<PosmMaster> listDataHeader,
                                     HashMap<PosmMaster, List<PosmMaster>> listChildData) {
            this._context = context;
            this._listDataHeader = listDataHeader;
            this._listDataChild = listChildData;
        }

        @Override
        public Object getChild(int groupPosition, int childPosititon) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                    .get(childPosititon);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild,
                                 View convertView, ViewGroup parent) {

            final PosmMaster childText = (PosmMaster) getChild(groupPosition, childPosition);
            ViewHolder holder = null;
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_item_parent_visibility_soft, null);
                holder = new ViewHolder();
                holder.cardView = convertView.findViewById(R.id.card_view);
                holder.nDeploymentSoft_edt = convertView.findViewById(R.id.nDeploymentSoft_edt);
                holder.visibility_softame = convertView.findViewById(R.id.visibility_softame);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            TextView txtListChild = convertView.findViewById(R.id.lblListItem_v_soft);
            txtListChild.setText(childText.getPosm().toString());


            holder.nDeploymentSoft_edt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    final EditText Caption = (EditText) v;
                    String value1 = Caption.getText().toString().replaceFirst("^0+(?!$)", "");
                    if (value1.equals("")) {
                        _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setDeployment_Value("");
                    } else {
                        _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setDeployment_Value(value1);
                    }
                }

            });


            holder.nDeploymentSoft_edt.setText(_listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getDeployment_Value());

            holder.visibility_softame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lvExp_softmerch.clearFocus();
                    grp_position = groupPosition;
                    child_position = childPosition;
                    _pathforcheck = store_cd + "_" +
                            childText.getPosmId() + "_SOFTMERCHIMG_" + visit_date.replace("/", "") + "_"
                            + getCurrentTime().replace(":", "") + ".jpg";
                    _path = CommonString.FILE_PATH + _pathforcheck;
                    startCameraActivity();
                }
            });


            if (!img1.equalsIgnoreCase("")) {
                if (childPosition == child_position && groupPosition == grp_position) {
                    _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setSoft_merchIMG(img1);
                    img1 = "";
                }
            }

            if (_listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getSoft_merchIMG() != null &&
                    !_listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getSoft_merchIMG().equals("")) {
                holder.visibility_softame.setImageResource(R.mipmap.camera_green);
            } else {
                holder.visibility_softame.setImageResource(R.mipmap.camera_orange);
            }


            if (!checkflag) {
                boolean tempflag = false;
                if (_listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getSoft_merchIMG().equals("")) {
                    tempflag = true;
                } else if (_listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getDeployment_Value().equals("")) {
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
            final PosmMaster headerTitle = (PosmMaster) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_group_storesoftmerch, null);
            }
            TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader_soft);
            lblListHeader.setText(headerTitle.getPosmType().toString());
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
        EditText nDeploymentSoft_edt;
        CardView cardView;
        ImageView visibility_softame;

    }


    boolean validateData(HashMap<PosmMaster, List<PosmMaster>> listDataChild2, List<PosmMaster> listDataHeader2) {
        boolean flag = true;
        checkHeaderArray.clear();
        for (int i = 0; i < listDataHeader2.size(); i++) {
            for (int j = 0; j < listDataChild2.get(listDataHeader2.get(i)).size(); j++) {
                String deploymentValue = listDataChild2.get(listDataHeader2.get(i)).get(j).getDeployment_Value();
                String soft_merchIMG = listDataChild2.get(listDataHeader2.get(i)).get(j).getSoft_merchIMG();
                if (deploymentValue.equals("")) {
                    flag = false;
                    checkflag = false;
                    Error_Message = CommonString.KEY_FOR_DEPLOYMENT;
                    break;
                } else if (soft_merchIMG.equals("")) {
                    checkflag = false;
                    flag = false;
                    Error_Message = CommonString.KEY_FOR_CAMERA_C;
                    break;
                } else {
                    checkflag = true;
                    flag = true;
                }
            }
            if (checkflag == false) {
                if (!checkHeaderArray.contains(i)) {
                    checkHeaderArray.add(i);
                }
                break;
            }

        }
        listAdapter.notifyDataSetChanged();
        return checkflag;
    }


    public String getCurrentTime() {
        Calendar m_cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String cdate = formatter.format(m_cal.getTime());
        return cdate;
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
                        img1 = _pathforcheck;
                        lvExp_softmerch.invalidateViews();
                        _pathforcheck = "";
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
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

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SoftMerchVisibilityActivity.this);
        builder.setMessage(CommonString.ONBACK_ALERT_MESSAGE)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SoftMerchVisibilityActivity.this);
            builder.setMessage(CommonString.ONBACK_ALERT_MESSAGE)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
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
        return super.onOptionsItemSelected(item);
    }

}
