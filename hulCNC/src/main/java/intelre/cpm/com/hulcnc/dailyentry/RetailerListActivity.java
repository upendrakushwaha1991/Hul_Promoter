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
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;

import cpm.com.hulcnc.R;
import intelre.cpm.com.hulcnc.Database.HUL_CNC_DB;
import intelre.cpm.com.hulcnc.constant.CommonString;
import intelre.cpm.com.hulcnc.gettersetter.SearchSalesGetterSetter;
import intelre.cpm.com.hulcnc.gettersetter.SearchStoreDataGetterSetter;

public class RetailerListActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView card_view;
    private LinearLayout lay;
    private TextView customer_name, price;
    FloatingActionMenu fab_mainMenu;
    com.github.clans.fab.FloatingActionButton fab_upload, fab_addstore;
    Toolbar toolbar;
    HUL_CNC_DB db;
    private SharedPreferences preferences = null;
    String userId, visit_date, store_cd;
    Context context;
    private RecyclerView drawer_layout_recycle_store;
    Dialog dialog1;
    Button no_sale, cancel;
    RecyclerView rec_primary_self;
    ArrayList<SearchStoreDataGetterSetter> storedataList = new ArrayList<>();
    ArrayList<SearchStoreDataGetterSetter> storedataList1 = new ArrayList<>();
    MyItemRecyclerViewAdapter myItemRecyclerViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_list);

        getViewId();
    }

    private void getViewId() {
        rec_primary_self = (RecyclerView) findViewById(R.id.retailer_list);
        customer_name = (TextView) findViewById(R.id.customer_name);
        card_view = (CardView) findViewById(R.id.card_view);
        price = (TextView) findViewById(R.id.price);
        lay = (LinearLayout) findViewById(R.id.lay);
        fab_mainMenu = (FloatingActionMenu) findViewById(R.id.fab_menu_orderDelivery);
        fab_upload = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_upload);
        fab_addstore = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_addstore);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer_layout_recycle_store = (RecyclerView) findViewById(R.id.drawer_layout_recycle_store);
        setSupportActionBar(toolbar);
        context = this;
        db = new HUL_CNC_DB(context);
        db.open();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = preferences.getString(CommonString.KEY_USERNAME, null);
        visit_date = preferences.getString(CommonString.KEY_DATE, "");
        store_cd = preferences.getString(CommonString.KEY_STORE_CD, null);
        getSupportActionBar().setTitle("Retailer List - " + visit_date);
        lay.setOnClickListener(this);
        fab_upload.setOnClickListener(this);
        fab_addstore.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.lay:
                Intent lay = new Intent(RetailerListActivity.this, SelectCategoryActivity.class);
                startActivity(lay);
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                break;

            case R.id.fab_upload:
                if (storedataList.size()>0){

                    Snackbar.make(fab_upload, "No Sales Option is not Available If you have Entered Sales for Atleast one Customer ", Snackbar.LENGTH_SHORT).show();
                }else {
                    popup();
                }

                break;
            case R.id.fab_addstore:
                Intent in = new Intent(RetailerListActivity.this, CustomerInfoActivity.class);
                startActivity(in);
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                break;


        }

    }

/*
    @Override
    protected void onResume() {
        super.onResume();
        db.open();
        searchSalesGetterSetter = db.getStoreDetailsData(store_cd);
        SaleAmmountList = db.getRetailerListData(store_cd);

        if (!searchSalesGetterSetter.getCustomerName().isEmpty()) {
            card_view.setVisibility(View.VISIBLE);
            customer_name.setText(searchSalesGetterSetter.getCustomerName() + " (" + searchSalesGetterSetter.getCard_no() + ")");
        }
        if (SaleAmmountList.getSale_qty() != null) {
            card_view.setVisibility(View.VISIBLE);
            price.setText("Value: Rs " + SaleAmmountList.getAmmount());
            //  fab.setVisibility(View.INVISIBLE);
            fab_mainMenu.setVisibility(View.INVISIBLE);
        }

        fab_mainMenu.setVisibility(View.VISIBLE);
    }
*/

    private void popup() {

        dialog1 = new Dialog(RetailerListActivity.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.dialog_sale);
        no_sale = (Button) dialog1.findViewById(R.id.no_sale);
        cancel = (Button) dialog1.findViewById(R.id.cancel);
        dialog1.setCancelable(false);
        dialog1.setCanceledOnTouchOutside(false);


        no_sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.open();
                String no_sale = "NOSALES";
                long id = db.InsertNoSaleData(store_cd, visit_date, no_sale);
                if (id > 0) {
                    Snackbar.make(fab_mainMenu, "Data saved successfully", Snackbar.LENGTH_SHORT).show();
                    dialog1.dismiss();
                    finish();
                    overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                } else {
                    Snackbar.make(fab_mainMenu, "Data not saved", Snackbar.LENGTH_SHORT).show();
                }

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();

            }
        });

        dialog1.show();
    }


    public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {
        private final List<SearchStoreDataGetterSetter> mValues;

        public MyItemRecyclerViewAdapter(List<SearchStoreDataGetterSetter> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_storeviewlist, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.mItem = mValues.get(position);
            holder.txt.setText(mValues.get(position).getUser_id() + " (" + mValues.get(position).getCard_no() + ")");
            holder.tv_city.setText("Value: Rs " + mValues.get(position).getAmmount());

            holder.relativelayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent lay = new Intent(RetailerListActivity.this, SelectCategoryActivity.class);
                    lay.putExtra(CommonString.KEY_ID, mValues.get(position).getKey_id() + "");
                    startActivity(lay);
                    overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                }

            });


        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public  TextView txt, tv_city;
            public  CardView Cardbtn;
            public  RelativeLayout relativelayout;
            public final View mView;

            public SearchStoreDataGetterSetter mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                txt = (TextView) view.findViewById(R.id.storelistviewxml_storename);
                tv_city = (TextView) view.findViewById(R.id.storelistviewxml_storeaddress);
                relativelayout = (RelativeLayout) view.findViewById(R.id.storenamelistview_layout);

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {

            db = new HUL_CNC_DB(getApplicationContext());
            db.open();
            storedataList = db.getStoreSavedData("Yes",store_cd);
            if (storedataList.size() > 0) {
                storedataList1.clear();
                for (int i = 0; i < storedataList.size(); i++) {
                    SearchStoreDataGetterSetter df = db.getStoreSavedData1(store_cd,storedataList.get(i).getKey_id());
                    if (df!=null){
                        storedataList1.add(df);
                    }
                }
            }

            if (storedataList1.size()>0){
                rec_primary_self.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                myItemRecyclerViewAdapter = new MyItemRecyclerViewAdapter(storedataList1);
                rec_primary_self.setAdapter(myItemRecyclerViewAdapter);
                rec_primary_self.setVisibility(View.VISIBLE);
            }

           db.deleteUnSaveData("Yes");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

