package intelre.cpm.com.intelre.dailyentry;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
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
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
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
import intelre.cpm.com.intelre.gsonGetterSetter.MappingPermanentPosm;
import intelre.cpm.com.intelre.gsonGetterSetter.PosmMaster;

public class VisibilitySpermanentActivity extends AppCompatActivity {
    INTEL_RE_DB db;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor = null;
    String store_cd, visit_date, user_type, username, Error_Message;
    ExpandableListView lvExp_semi_p_merch;
    FloatingActionButton fab;
    List<MappingPermanentPosm> listDataHeader;
    List<MappingPermanentPosm> questionList;
    HashMap<MappingPermanentPosm, List<MappingPermanentPosm>> listDataChild;
    ExpandableListAdapter listAdapter;
    static int grp_position = -1;
    static int child_position = -1;
    String _pathforcheck, _path, img1 = "", img2 = "", img3 = "";
    boolean checkflag = true;
    ArrayList<Integer> checkHeaderArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visibility_samipermanent);
        auditUI();
        //save audit data
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lvExp_semi_p_merch.clearFocus();
                if (validateData(listDataChild, listDataHeader)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(VisibilitySpermanentActivity.this);
                    builder.setTitle("Parinaam").setMessage(R.string.alertsaveData);
                    builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            db.open();
                            db.insertVisibilitySemiParmanetMerchData(store_cd, listDataChild, listDataHeader);
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
                    Snackbar.make(lvExp_semi_p_merch, Error_Message, Snackbar.LENGTH_LONG).show();
                }

            }
        });
    }

    private void auditUI() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lvExp_semi_p_merch = findViewById(R.id.lvExp_semi_p_merch);
        fab = findViewById(R.id.fab);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        store_cd = preferences.getString(CommonString.KEY_STORE_CD, null);
        username = preferences.getString(CommonString.KEY_USERNAME, null);
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        user_type = preferences.getString(CommonString.KEY_USER_TYPE, null);
        getSupportActionBar().setTitle("Visibility-SP Merch -" + visit_date);
        db = new INTEL_RE_DB(this);
        db.open();
        prepareListData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        // setting list adapter
        lvExp_semi_p_merch.setAdapter(listAdapter);

        lvExp_semi_p_merch.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

            }

            @Override
            public void onScrollStateChanged(AbsListView arg0, int arg1) {
                lvExp_semi_p_merch.invalidateViews();

                InputMethodManager inputManager = (InputMethodManager) getApplicationContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getCurrentFocus() != null) {
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    getCurrentFocus().clearFocus();
                }
            }

        });

        // Listview Group click listener
        lvExp_semi_p_merch.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

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
        lvExp_semi_p_merch.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

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
        lvExp_semi_p_merch.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

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
        lvExp_semi_p_merch.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

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
        listDataHeader = db.getPemanentMerchPosmHeaderData(Integer.parseInt(store_cd));
        if (listDataHeader.size() > 0) {
            for (int i = 0; i < listDataHeader.size(); i++) {
                questionList = db.getVisibilitySemiPermanetMerchInsertedData(store_cd, listDataHeader.get(i).getPosmTypeId());
                if (questionList.size() > 0) {
                    fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.edit_txt));
                } else {
                    questionList = db.getVisibilitySemiPermanetMerchChildData(listDataHeader.get(i).getPosmTypeId());
                }
                listDataChild.put(listDataHeader.get(i), questionList); // Header, Child data
            }
        }
    }


    public class ExpandableListAdapter extends BaseExpandableListAdapter {
        private Context _context;
        private List<MappingPermanentPosm> _listDataHeader;
        private HashMap<MappingPermanentPosm, List<MappingPermanentPosm>> _listDataChild;

        public ExpandableListAdapter(Context context, List<MappingPermanentPosm> listDataHeader,
                                     HashMap<MappingPermanentPosm, List<MappingPermanentPosm>> listChildData) {
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

            final MappingPermanentPosm childText = (MappingPermanentPosm) getChild(groupPosition, childPosition);
            ViewHolder holder = null;
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_item_parent_visibility_semip, null);
                holder = new ViewHolder();
                holder.cardView = convertView.findViewById(R.id.card_view);
                holder.previous_Dep_edt = convertView.findViewById(R.id.previous_Dep_edt);
                holder.new_Dep_edt = convertView.findViewById(R.id.new_Dep_edt);

                holder.permanent_img1 = convertView.findViewById(R.id.permanent_img1);
                holder.permanent_img2 = convertView.findViewById(R.id.permanent_img2);
                holder.permanent_img3 = convertView.findViewById(R.id.permanent_img3);
                holder.perm_previous_txt = convertView.findViewById(R.id.perm_previous_txt);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            //non changeable value
            TextView txtListChild = convertView.findViewById(R.id.lblListItem_v_soft);
            txtListChild.setText(childText.getPosm().toString());
            holder.perm_previous_txt.setText(childText.getPrev_Qty().toString());
            holder.perm_previous_txt.setBackgroundColor(Color.GRAY);
            if (childText.getPrev_Qty().toString().equals("0")) {
                holder.previous_Dep_edt.setEnabled(false);
                holder.previous_Dep_edt.setBackgroundColor(Color.GRAY);
                _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setPreV_dValue("0");
                holder.previous_Dep_edt.setText(_listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getPreV_dValue());
            } else {
                holder.previous_Dep_edt.setEnabled(true);
            }
            holder.previous_Dep_edt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    final EditText Caption = (EditText) v;
                    String value1 = Caption.getText().toString().replaceFirst("^0+(?!$)", "");
                    if (value1.equals("")) {
                        _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setPreV_dValue("");
                    } else {
                        _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setPreV_dValue(value1);
                    }
                }

            });
            holder.previous_Dep_edt.setText(_listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getPreV_dValue());

            holder.new_Dep_edt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    final EditText Caption = (EditText) v;
                    String value1 = Caption.getText().toString().replaceFirst("^0+(?!$)", "");
                    if (value1.equals("")) {
                        _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setNewDeploymnt_Value("");
                    } else {
                        _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setNewDeploymnt_Value(value1);
                    }
                }

            });


            holder.new_Dep_edt.setText(_listDataChild.get(listDataHeader.get(groupPosition))
                    .get(childPosition).getNewDeploymnt_Value());


            holder.permanent_img1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lvExp_semi_p_merch.clearFocus();
                    grp_position = groupPosition;
                    child_position = childPosition;
                    _pathforcheck = store_cd + "_" +
                            childText.getPosmId() + "_SEMIPMERCHIMG_ONE_" + visit_date.replace("/", "") + "_"
                            + getCurrentTime().replace(":", "") + ".jpg";
                    _path = CommonString.FILE_PATH + _pathforcheck;
                    startCameraActivity(0);
                }
            });

            holder.permanent_img2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lvExp_semi_p_merch.clearFocus();
                    grp_position = groupPosition;
                    child_position = childPosition;
                    _pathforcheck = store_cd + "_" +
                            childText.getPosmId() + "_SEMIPMERCHIMG_TWO_" + visit_date.replace("/", "") + "_"
                            + getCurrentTime().replace(":", "") + ".jpg";
                    _path = CommonString.FILE_PATH + _pathforcheck;
                    startCameraActivity(1);
                }
            });

            holder.permanent_img3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lvExp_semi_p_merch.clearFocus();
                    grp_position = groupPosition;
                    child_position = childPosition;
                    _pathforcheck = store_cd + "_" +
                            childText.getPosmId() + "_SEMIPMERCHIMG_THREE_" + visit_date.replace("/", "") + "_"
                            + getCurrentTime().replace(":", "") + ".jpg";
                    _path = CommonString.FILE_PATH + _pathforcheck;
                    startCameraActivity(1);
                }
            });


            if (!img1.equals("")) {
                if (childPosition == child_position && groupPosition == grp_position) {
                    _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setsPermanetIMG_1(img1);
                    img1 = "";
                }
            }

            if (!img2.equals("")) {
                if (childPosition == child_position && groupPosition == grp_position) {
                    _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setsPermanetIMG_2(img2);
                    img2 = "";
                }
            }

            if (!img3.equals("")) {
                if (childPosition == child_position && groupPosition == grp_position) {
                    _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setsPermanetIMG_3(img3);
                    img3 = "";
                }
            }


            if (_listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getsPermanetIMG_1() != null &&
                    !_listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getsPermanetIMG_1().equals("")) {
                holder.permanent_img1.setImageResource(R.mipmap.camera_green);
            } else {
                holder.permanent_img1.setImageResource(R.mipmap.camera_orange);
            }

            if (_listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getsPermanetIMG_2() != null &&
                    !_listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getsPermanetIMG_2().equals("")) {
                holder.permanent_img2.setImageResource(R.mipmap.camera_green);
            } else {
                holder.permanent_img2.setImageResource(R.mipmap.camera_orange);
            }

            if (_listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getsPermanetIMG_3() != null &&
                    !_listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getsPermanetIMG_3().equals("")) {
                holder.permanent_img3.setImageResource(R.mipmap.camera_green);
            } else {
                holder.permanent_img3.setImageResource(R.mipmap.camera_orange);
            }


            if (!checkflag) {
                boolean tempflag = false;
                if (!holder.perm_previous_txt.getText().toString().equals("0")){
                    if (holder.previous_Dep_edt.getText().toString().equals("")){
                        tempflag = true;
                    }
                }else if (!holder.new_Dep_edt.getText().toString().equals("")){
                    if (childText.getsPermanetIMG_1().equals("") ||childText.getsPermanetIMG_2().equals("")
                            ||childText.getsPermanetIMG_3().equals("")) {
                        tempflag = true;
                    }
                }else  if (!childText.getsPermanetIMG_1().equals("") || !childText.getsPermanetIMG_2().equals("")
                        || !childText.getsPermanetIMG_3().equals("")) {
                    if (holder.new_Dep_edt.getText().toString().equals("")){
                        tempflag = true;
                    }
                }

               /* if (holder.previous_Dep_edt.getText().toString().equals("") &&
                        holder.new_Dep_edt.getText().toString().equals("")) {
                    holder.previous_Dep_edt.setHintTextColor(getResources().getColor(R.color.red));
                    holder.new_Dep_edt.setHintTextColor(getResources().getColor(R.color.red));
                    tempflag = true;
                } else if (holder.previous_Dep_edt.getText().toString().equals("")) {
                    holder.previous_Dep_edt.setHintTextColor(getResources().getColor(R.color.red));
                    tempflag = true;
                } else if (holder.new_Dep_edt.getText().toString().equals("")) {
                    holder.new_Dep_edt.setHintTextColor(getResources().getColor(R.color.red));
                    tempflag = true;
                } else if (childText.getsPermanetIMG_1().equals("") && childText.getsPermanetIMG_2().equals("")
                        && childText.getsPermanetIMG_3().equals("")) {
                    tempflag = true;
                }
*/
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
            final MappingPermanentPosm headerTitle = (MappingPermanentPosm) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_group_storesoftmerch, null);
            }
            TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader_soft);
            lblListHeader.setTypeface(null, Typeface.BOLD);
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
        EditText previous_Dep_edt, new_Dep_edt;
        CardView cardView;
        ImageView permanent_img1, permanent_img2, permanent_img3;
        TextView perm_previous_txt;

    }


    boolean validateData(HashMap<MappingPermanentPosm, List<MappingPermanentPosm>> listDataChild2,
                         List<MappingPermanentPosm> listDataHeader2) {
        boolean flag = true;
        checkHeaderArray.clear();
        for (int i = 0; i < listDataHeader2.size(); i++) {
            for (int j = 0; j < listDataChild2.get(listDataHeader2.get(i)).size(); j++) {
                String prevValue = listDataChild2.get(listDataHeader2.get(i)).get(j).getPrev_Qty().toString();
                String previousdeploymentValue = listDataChild2.get(listDataHeader2.get(i)).get(j).getPreV_dValue();
                String NdeploymentValue = listDataChild2.get(listDataHeader2.get(i)).get(j).getNewDeploymnt_Value();
                String posm = listDataChild2.get(listDataHeader2.get(i)).get(j).getPosm();
                String smIMG1 = listDataChild2.get(listDataHeader2.get(i)).get(j).getsPermanetIMG_1();
                String smIMG2 = listDataChild2.get(listDataHeader2.get(i)).get(j).getsPermanetIMG_2();
                String smIMG3 = listDataChild2.get(listDataHeader2.get(i)).get(j).getsPermanetIMG_3();
                if (!prevValue.equals("0")) {
                    if (previousdeploymentValue.equals("")) {
                        flag = false;
                        checkflag = false;
                        Error_Message = CommonString.KEY_FOR_OLD_DEPLOYMENT + " of " + posm;
                        break;
                    }
                } else if (!NdeploymentValue.equals("")) {
                    if (smIMG1.equals("")) {
                        checkflag = false;
                        flag = false;
                        Error_Message = CommonString.KEY_FOR_CAMERA_C_ALL + " of " + posm;
                        break;
                    } else if (smIMG2.equals("")) {
                        checkflag = false;
                        flag = false;
                        Error_Message = CommonString.KEY_FOR_CAMERA_C_ALL + " of " + posm;
                        break;

                    } else if (smIMG3.equals("")) {
                        checkflag = false;
                        flag = false;
                        Error_Message = CommonString.KEY_FOR_CAMERA_C_ALL + " of " + posm;
                        break;
                    }
                } else if (!smIMG1.equals("") || !smIMG2.equals("") || !smIMG3.equals("")) {
                    if (NdeploymentValue.equals("")){
                        flag = false;
                        checkflag = false;
                        Error_Message = CommonString.KEY_FOR_DEPLOYMENT + " of " + posm;
                        break;
                    }
                   /* checkflag = false;
                    flag = false;
                    Error_Message = CommonString.KEY_FOR_CAMERA_C_ALL + " of " + posm;
                    break;*/
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
        switch (requestCode) {
            case 0:
                if (resultCode == -1) {
                    if (_pathforcheck != null && !_pathforcheck.equals("")) {
                        if (new File(CommonString.FILE_PATH + _pathforcheck).exists()) {
                            img1 = _pathforcheck;
                            lvExp_semi_p_merch.invalidateViews();
                            _pathforcheck = "";
                        }
                    }
                } else {
                    Log.i("MakeMachine", "User cancelled");
                    _pathforcheck = "";
                }
                break;

            case 1:
                if (resultCode == -1) {
                    if (_pathforcheck != null && !_pathforcheck.equals("")) {
                        if (new File(CommonString.FILE_PATH + _pathforcheck).exists()) {
                            if (_pathforcheck.contains("THREE")) {
                                img3 = _pathforcheck;
                            } else {
                                img2 = _pathforcheck;
                            }
                            lvExp_semi_p_merch.invalidateViews();
                            _pathforcheck = "";
                        }
                    }
                } else {
                    Log.i("MakeMachine", "User cancelled");
                    _pathforcheck = "";
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /*  @Override
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
                          lvExp_semi_p_merch.invalidateViews();
                          _pathforcheck = "";
                      }
                  }
                  break;
          }
          super.onActivityResult(requestCode, resultCode, data);
      }
  */
    protected void startCameraActivity(int position) {
        try {
            Log.i("MakeMachine", "startCameraActivity()");
            File file = new File(_path);
            Uri outputFileUri = Uri.fromFile(file);
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            startActivityForResult(intent, position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(VisibilitySpermanentActivity.this);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(VisibilitySpermanentActivity.this);
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
