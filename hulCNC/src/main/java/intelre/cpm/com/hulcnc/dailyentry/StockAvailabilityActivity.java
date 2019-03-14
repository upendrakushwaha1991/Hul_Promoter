package intelre.cpm.com.hulcnc.dailyentry;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cpm.com.hulcnc.R;
import intelre.cpm.com.hulcnc.Database.HUL_CNC_DB;
import intelre.cpm.com.hulcnc.gettersetter.StockAvailabilityGetterSetter;

import intelre.cpm.com.hulcnc.constant.CommonString;

public class StockAvailabilityActivity extends AppCompatActivity {
    HUL_CNC_DB db;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor = null;
    String store_cd, visit_date, user_type, username, Error_Message, region_id, destributor_id;
    ExpandableListView lvExp_audit;
    FloatingActionButton storeAudit_fab;
    List<StockAvailabilityGetterSetter> listDataHeader;
    List<StockAvailabilityGetterSetter> questionList;
    HashMap<StockAvailabilityGetterSetter, List<StockAvailabilityGetterSetter>> listDataChild;
    ExpandableListAdapter listAdapter;
    boolean checkflag = true;
    ArrayList<Integer> checkHeaderArray = new ArrayList<>();
    private String stock_category_cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_audit);
        getviewUI();
        storeAudit_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lvExp_audit.clearFocus();
                lvExp_audit.invalidateViews();
                listAdapter.notifyDataSetChanged();
                if (validateData(listDataChild, listDataHeader)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(StockAvailabilityActivity.this);
                    builder.setTitle("Parinaam").setMessage(R.string.alertsaveData);
                    builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            db.open();
                            db.insertStockData(store_cd, listDataChild, listDataHeader,stock_category_cd);
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
        });

    }

    private void getviewUI() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        region_id = preferences.getString(CommonString.KEY_REGION_ID, null);
        destributor_id = preferences.getString(CommonString.KEY_DESTRIBUTOR_ID, null);
        getSupportActionBar().setTitle("Stock Availability -" + visit_date);
        db = new HUL_CNC_DB(this);
        db.open();
        prepareListData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        lvExp_audit.setAdapter(listAdapter);
        for (int i = 0; i < listAdapter.getGroupCount(); i++){
            lvExp_audit.expandGroup(i);
        }
    }

    private void prepareListData() {
        db.open();
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
        listDataHeader = db.getHeaderStockData(region_id, destributor_id,stock_category_cd);
        if (listDataHeader.size() > 0) {
            for (int i = 0; i < listDataHeader.size(); i++) {

                questionList = db.getStockInsertedData(store_cd, listDataHeader.get(i).getBrand_id());
                if (questionList.size() > 0) {
                    storeAudit_fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.edit_txt));
                } else {
                    questionList = db.getStockchildData(region_id, destributor_id, listDataHeader.get(i).getBrand_id());
                }
                listDataChild.put(listDataHeader.get(i), questionList); // Header, Child data
            }
        }
    }

    public class ExpandableListAdapter extends BaseExpandableListAdapter {
        private Context _context;
        private List<StockAvailabilityGetterSetter> _listDataHeader;
        private HashMap<StockAvailabilityGetterSetter, List<StockAvailabilityGetterSetter>> _listDataChild;

        public ExpandableListAdapter(Context context, List<StockAvailabilityGetterSetter> listDataHeader,
                                     HashMap<StockAvailabilityGetterSetter, List<StockAvailabilityGetterSetter>> listChildData) {
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

            final StockAvailabilityGetterSetter childText = (StockAvailabilityGetterSetter) getChild(groupPosition, childPosition);
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
            txtListChild.setText(childText.getSku());

            final ArrayList<StockAvailabilityGetterSetter> reason_list = db.getastockAnswerData();

            holder.audit_spin.setAdapter(new ReasonSpinnerAdapter(_context, R.layout.spinner_text_view, reason_list));

            for (int i = 0; i < reason_list.size(); i++) {
                if (reason_list.get(i).getAnswerId().toString().equals(_listDataChild.get(listDataHeader.get(groupPosition))
                        .get(childPosition).getCurrectanswerCd().toString())) {
                    holder.audit_spin.setSelection(i);
                    break;
                }
            }
            final ViewHolder finalHolder = holder;
            holder.audit_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    if (pos != -1) {
                        StockAvailabilityGetterSetter ans = reason_list.get(pos);
                        _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setCurrectanswerCd(ans.getAnswerId().toString());
                        _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setCurrectanswer(ans.getAnswer().toString());

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            if (!checkflag) {
                boolean tempflag = false;
                if (_listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getCurrectanswerCd().equals("-1")) {
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
            final StockAvailabilityGetterSetter headerTitle = (StockAvailabilityGetterSetter) getGroup(groupPosition);
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


    boolean validateData(HashMap<StockAvailabilityGetterSetter, List<StockAvailabilityGetterSetter>> listDataChild2, List<StockAvailabilityGetterSetter> listDataHeader2) {
        checkflag = true;
        checkHeaderArray.clear();
        for (int i = 0; i < listDataHeader2.size(); i++) {
            for (int j = 0; j < listDataChild2.get(listDataHeader2.get(i)).size(); j++) {
                String spinValue = listDataChild2.get(listDataHeader2.get(i)).get(j).getCurrectanswerCd();
                if (spinValue.equals("-1")) {
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


    public class ReasonSpinnerAdapter extends ArrayAdapter<StockAvailabilityGetterSetter> {
        List<StockAvailabilityGetterSetter> list;
        Context context;
        int resourceId;

        public ReasonSpinnerAdapter(Context context, int resourceId, ArrayList<StockAvailabilityGetterSetter> list) {
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
            StockAvailabilityGetterSetter cm = list.get(position);
            TextView txt_spinner = (TextView) view.findViewById(R.id.txt_sp_text);
            txt_spinner.setText(list.get(position).getAnswer());

            return view;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            LayoutInflater inflater = getLayoutInflater();
            view = inflater.inflate(resourceId, parent, false);
            StockAvailabilityGetterSetter cm = list.get(position);
            TextView txt_spinner = (TextView) view.findViewById(R.id.txt_sp_text);
            txt_spinner.setText(cm.getAnswer());

            return view;
        }

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(StockAvailabilityActivity.this);
        builder.setMessage(CommonString.ONBACK_ALERT_MESSAGE)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                        StockAvailabilityActivity.this.finish();
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
            AlertDialog.Builder builder = new AlertDialog.Builder(StockAvailabilityActivity.this);
            builder.setMessage(CommonString.ONBACK_ALERT_MESSAGE)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                            StockAvailabilityActivity.this.finish();
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
