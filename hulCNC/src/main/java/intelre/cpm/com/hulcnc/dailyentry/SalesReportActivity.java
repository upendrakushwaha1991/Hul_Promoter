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
import android.view.MenuItem;
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
import intelre.cpm.com.hulcnc.gettersetter.SaleReportsGetterSetter;
import intelre.cpm.com.hulcnc.gettersetter.SalesEntryGetterSetter;
import intelre.cpm.com.hulcnc.gsonGetterSetter.SalesReport;

public class SalesReportActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    SharedPreferences preferences;
    private String date;
    private HUL_CNC_DB database;
    private Context context;
    private String userId;
    private ValueAdapter adapter;
    private ArrayList<SaleReportsGetterSetter> report_list = new ArrayList<>();
    String store_cd, user_type, username, Error_Message, region_id, destributor_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_report);
        declaration();


    }

    private void declaration() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        store_cd = preferences.getString(CommonString.KEY_STORE_CD, null);
        date = preferences.getString(CommonString.KEY_DATE, null);
        region_id = preferences.getString(CommonString.KEY_REGION_ID, null);
        destributor_id = preferences.getString(CommonString.KEY_DESTRIBUTOR_ID, null);
        userId = preferences.getString(CommonString.KEY_USERNAME, null);

        recyclerView = (RecyclerView) findViewById(R.id.drawer_layout_recycle);
        context = this;
        getSupportActionBar().setTitle("Sales Report- " + date);
        database = new HUL_CNC_DB(context);
        database.open();
        report_list = database.getReportData(store_cd);
        if (report_list.size() > 0) {
            adapter = new ValueAdapter(getApplicationContext(), report_list);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
    }

    public class ValueAdapter extends RecyclerView.Adapter<ValueAdapter.MyViewHolder> {
        private LayoutInflater inflator;
        List<SaleReportsGetterSetter> data = Collections.emptyList();

        public ValueAdapter(Context context, List<SaleReportsGetterSetter> data) {
            inflator = LayoutInflater.from(context);
            this.data = data;
        }

        @Override
        public ValueAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflator.inflate(R.layout.reportlist, parent, false);
            return new ValueAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ValueAdapter.MyViewHolder viewHolder, final int position) {
            final SaleReportsGetterSetter current = data.get(position);

            viewHolder.txt.setText(current.getSku());
            viewHolder.value.setText(current.getValue());
            viewHolder.mtd.setText(current.getMtd());
            viewHolder.ftd.setText(current.getFtd());
            viewHolder.total.setText(current.getTotal());

        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView txt, value, mtd, ftd, total;


            public MyViewHolder(View itemView) {
                super(itemView);
                txt = itemView.findViewById(R.id.text);
                value = itemView.findViewById(R.id.value);
                mtd = itemView.findViewById(R.id.mtd);
                ftd = itemView.findViewById(R.id.ftd);
                total = itemView.findViewById(R.id.total);

            }
        }

    }

    /*@Override
    protected void onResume() {
        super.onResume();
        database.open();
        category_list = database.getSelectCategoryData(region_id,destributor_id);
        if (category_list.size() > 0) {
            adapter = new ValueAdapter(SalesReportActivity.this, category_list);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }

    }

*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
