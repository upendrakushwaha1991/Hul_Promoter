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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cpm.com.hulcnc.R;
import intelre.cpm.com.hulcnc.Database.HUL_CNC_DB;
import intelre.cpm.com.hulcnc.gettersetter.SalesEntryGetterSetter;
import intelre.cpm.com.hulcnc.gettersetter.SalesEntryGetterSetter;

import intelre.cpm.com.hulcnc.constant.CommonString;
import intelre.cpm.com.hulcnc.gettersetter.SearchSalesGetterSetter;
import intelre.cpm.com.hulcnc.gettersetter.SearchStoreDataGetterSetter;

public class SalesEntryActivity extends AppCompatActivity {

    HUL_CNC_DB db;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor = null;
    String store_cd, visit_date, user_type, username, Error_Message, region_id, destributor_id, category_cd;
    ExpandableListView lvExp_audit;
    FloatingActionButton storeAudit_fab;
    List<SalesEntryGetterSetter> listDataHeader;
    List<SalesEntryGetterSetter> questionList;
    HashMap<SalesEntryGetterSetter, List<SalesEntryGetterSetter>> listDataChild;
    ExpandableListAdapter listAdapter;
    boolean checkflag = true;
    ArrayList<Integer> checkHeaderArray = new ArrayList<>();
    String _customer_name,_card_no,_customer_id,_mobile_no;
    SearchSalesGetterSetter searchSalesGetterSetter;
    SearchStoreDataGetterSetter AddstoreObject;
    String key_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_entry);

        getviewUI();

        storeAudit_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lvExp_audit.clearFocus();
                lvExp_audit.invalidateViews();
                listAdapter.notifyDataSetChanged();
                if (validateData(listDataChild, listDataHeader)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SalesEntryActivity.this);
                    builder.setTitle("Parinaam").setMessage(R.string.alertsaveData);
                    builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            db.open();
                            db.insertSalesStockData(store_cd,category_cd, listDataChild, listDataHeader);
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
                    Snackbar.make(lvExp_audit, "Please fill At Least one data and value greater then zero", Snackbar.LENGTH_SHORT).show();
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

        key_id = getIntent().getStringExtra(CommonString.KEY_ID);
        AddstoreObject = (SearchStoreDataGetterSetter) getIntent().getSerializableExtra(CommonString.KEY_ADD_STORE_OBJECT);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        store_cd = preferences.getString(CommonString.KEY_STORE_CD, null);
        username = preferences.getString(CommonString.KEY_USERNAME, null);
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        user_type = preferences.getString(CommonString.KEY_USER_TYPE, null);

        Intent intent = getIntent();
        category_cd = intent.getStringExtra(CommonString.KEY_CATEGORY_CD);
        region_id = preferences.getString(CommonString.KEY_REGION_ID, null);
        destributor_id = preferences.getString(CommonString.KEY_DESTRIBUTOR_ID, null);

        _customer_name = preferences.getString(CommonString.KEY_CUSTOMER_NAME, null);
        _card_no = preferences.getString(CommonString.KEY_CARD_NO, null);
        _customer_id = preferences.getString(CommonString.KEY_CUSTOMER_ID, null);
        _mobile_no = preferences.getString(CommonString.KEY_PHONENO, null);

        getSupportActionBar().setTitle("Sales Entry -" + visit_date);
        db = new HUL_CNC_DB(this);
        db.open();
        searchSalesGetterSetter=new SearchSalesGetterSetter();
        searchSalesGetterSetter.setCustomerName(_customer_name);
        searchSalesGetterSetter.setCard_no(_card_no);

        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        lvExp_audit.setAdapter(listAdapter);
        for (int i = 0; i < listAdapter.getGroupCount(); i++){
            lvExp_audit.expandGroup(i);
        }

        lvExp_audit.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lvExp_audit.invalidate();

                int lastItem = firstVisibleItem + visibleItemCount;

                if (firstVisibleItem == 0) {
                    storeAudit_fab.show();//.setVisibility(View.VISIBLE);
                } else if (lastItem == totalItemCount) {
                    storeAudit_fab.hide();//setVisibility(View.INVISIBLE);
                } else {
                    storeAudit_fab.show();//setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView arg0, int arg1) {

                InputMethodManager inputManager = (InputMethodManager) getApplicationContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getCurrentFocus() != null) {
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    getCurrentFocus().clearFocus();
                }
                lvExp_audit.invalidateViews();
            }
        });

        // Listview Group click listener
        lvExp_audit.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                return false;
            }
        });

        // Listview Group expanded listener
        lvExp_audit.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                InputMethodManager inputManager = (InputMethodManager) getApplicationContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getWindow().getCurrentFocus() != null) {
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    getCurrentFocus().clearFocus();
                }
            }
        });

        // Listview Group collasped listener
        lvExp_audit.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {

                InputMethodManager inputManager = (InputMethodManager) getApplicationContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                if (getWindow().getCurrentFocus() != null) {
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    getCurrentFocus().clearFocus();
                }
            }
        });

        // Listview on child click listener
        lvExp_audit.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

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
        listDataHeader = db.getHeaderSalesData(region_id, destributor_id, category_cd);
        if (listDataHeader.size() > 0) {
            for (int i = 0; i < listDataHeader.size(); i++) {

                questionList = db.getSalesStockInsertedData(store_cd, listDataHeader.get(i).getBrand_id());
                if (questionList.size() > 0) {
                    storeAudit_fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.edit_txt));
                } else {
                    questionList = db.getSalesStockchildData(region_id, destributor_id, listDataHeader.get(i).getBrand_id());
                }
                listDataChild.put(listDataHeader.get(i), questionList); // Header, Child data
            }
        }
    }

    public class ExpandableListAdapter extends BaseExpandableListAdapter {
        private Context _context;
        private List<SalesEntryGetterSetter> _listDataHeader;
        private HashMap<SalesEntryGetterSetter, List<SalesEntryGetterSetter>> _listDataChild;

        public ExpandableListAdapter(Context context, List<SalesEntryGetterSetter> listDataHeader,
                                     HashMap<SalesEntryGetterSetter, List<SalesEntryGetterSetter>> listChildData) {
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

            final SalesEntryGetterSetter childText = (SalesEntryGetterSetter) getChild(groupPosition, childPosition);
            ViewHolder holder = null;
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_item_parent_salesentry, null);
                holder = new ViewHolder();
                holder.cardView = convertView.findViewById(R.id.card_view);
                holder.ed_Stock = convertView.findViewById(R.id.ed_Stock);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            TextView txtListChild = convertView.findViewById(R.id.lblListItem);
            txtListChild.setText(childText.getSku());
            final ViewHolder finalHolder = holder;

            holder.ed_Stock.setOnFocusChangeListener(new View.OnFocusChangeListener() {

                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    final EditText Caption = (EditText) v;
                    String value1 = Caption.getText().toString().replaceFirst("^0+(?!$)", "");
                    if (value1.equals("")) {
                        _listDataChild.get(listDataHeader.get(groupPosition))
                                .get(childPosition).setStock("");
                    } else {
                        _listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).setStock(value1);
                    }

                }
            });
            holder.ed_Stock.setText(childText.getStock());

            if (!checkflag) {
                boolean tempflag = false;
                if (_listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getStock().equals("")) {
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
            final SalesEntryGetterSetter headerTitle = (SalesEntryGetterSetter) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_group_salesentry, null);
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
        EditText ed_Stock;
        CardView cardView;
    }


    boolean validateData(HashMap<SalesEntryGetterSetter, List<SalesEntryGetterSetter>> listDataChild2,
                         List<SalesEntryGetterSetter> listDataHeader2) {
        boolean flag = false;
        checkHeaderArray.clear();
        loop1:
        for (int i = 0; i < listDataHeader2.size(); i++) {

            for (int j = 0; j < listDataChild2.get(listDataHeader.get(i)).size(); j++) {
                String stock = listDataChild.get(listDataHeader.get(i)).get(j).getStock();
                if (stock != null && !stock.equalsIgnoreCase("") && !stock.equalsIgnoreCase("0")) {
                    flag = true;
                    break;
                }

            }
            if (flag) {
                break;
            }
        }
        if (flag) {
            return checkflag = true;
        } else {

            return checkflag = false;
        }

    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SalesEntryActivity.this);
        builder.setMessage(CommonString.ONBACK_ALERT_MESSAGE)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                        SalesEntryActivity.this.finish();
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
            AlertDialog.Builder builder = new AlertDialog.Builder(SalesEntryActivity.this);
            builder.setMessage(CommonString.ONBACK_ALERT_MESSAGE)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
                            SalesEntryActivity.this.finish();
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
    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

}
