package intelre.cpm.com.hulcnc.dailyentry;

import android.content.Context;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cpm.com.hulcnc.R;
import intelre.cpm.com.hulcnc.Database.HUL_CNC_DB;
import intelre.cpm.com.hulcnc.constant.CommonString;
import intelre.cpm.com.hulcnc.gettersetter.SalesEntryGetterSetter;

public class CategoryList extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    SharedPreferences preferences;
    private String date;
    private HUL_CNC_DB database;
    private Context context;
    private String userId,customer_name,card_no,customer_id;
    private ValueAdapter adapter;
    private ArrayList<SalesEntryGetterSetter> category_list = new ArrayList<>();
    String store_cd, user_type, username, Error_Message, region_id, destributor_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        declaration();

        category_list = database.getSelectCategoryData(region_id,destributor_id);
        if (category_list.size() > 0) {
            adapter = new ValueAdapter(CategoryList.this, category_list);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }
    private void declaration() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        setSupportActionBar(toolbar);
    //    getSupportActionBar().setHomeButtonEnabled(true);
      //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        store_cd = preferences.getString(CommonString.KEY_STORE_CD, null);
        date = preferences.getString(CommonString.KEY_DATE, null);
        region_id = preferences.getString(CommonString.KEY_REGION_ID, null);
        destributor_id = preferences.getString(CommonString.KEY_DESTRIBUTOR_ID, null);
        userId = preferences.getString(CommonString.KEY_USERNAME, null);

        customer_name = preferences.getString(CommonString.KEY_CUSTOMER_NAME, null);
        card_no = preferences.getString(CommonString.KEY_CARD_NO, null);


        recyclerView = (RecyclerView) findViewById(R.id.drawer_layout_recycle);
        context = this;
        getSupportActionBar().setTitle("Category List- " + date);
        database = new HUL_CNC_DB(this);
        database.open();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }

    public class ValueAdapter extends RecyclerView.Adapter<ValueAdapter.MyViewHolder> {
        private LayoutInflater inflator;
        List<SalesEntryGetterSetter> data = Collections.emptyList();

        public ValueAdapter(Context context, List<SalesEntryGetterSetter> data) {
            inflator = LayoutInflater.from(context);
            this.data = data;
        }

        @Override
        public ValueAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflator.inflate(R.layout.selectcategorylist, parent, false);
            return new ValueAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ValueAdapter.MyViewHolder viewHolder, final int position) {
            final SalesEntryGetterSetter current = data.get(position);

            database.open();
            if (database.getStockCategoryIdDoneData(store_cd,current.getCategory_id()).size()>0){
                viewHolder.chkbtn.setBackgroundResource(R.mipmap.tick);
            }

            viewHolder.txt.setText(current.getCategory());
            viewHolder.relativelayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(CategoryList.this, StockAvailabilityActivity.class);
                    intent.putExtra(CommonString.KEY__STOCK_CATEGORY_CD, current.getCategory_id().toString());
                    startActivity(intent);
                    overridePendingTransition(R.anim.activity_in, R.anim.activity_out);

                }
            });

        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView txt;
            ImageView chkbtn;
            RelativeLayout relativelayout;

            public MyViewHolder(View itemView) {
                super(itemView);
                txt = itemView.findViewById(R.id.category_name);
                chkbtn = itemView.findViewById(R.id.done);
                relativelayout = itemView.findViewById(R.id.storenamelistview_layout);

            }
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
        database.open();
        category_list = database.getSelectCategoryData(region_id,destributor_id);
        if (category_list.size() > 0) {
            adapter = new ValueAdapter(CategoryList.this, category_list);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

    }

}
