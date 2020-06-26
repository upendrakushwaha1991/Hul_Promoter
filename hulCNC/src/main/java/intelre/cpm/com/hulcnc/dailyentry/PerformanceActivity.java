package intelre.cpm.com.hulcnc.dailyentry;

import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cpm.com.hulcnc.R;
import intelre.cpm.com.hulcnc.Database.HUL_CNC_DB;
import intelre.cpm.com.hulcnc.constant.CommonString;

import intelre.cpm.com.hulcnc.gsonGetterSetter.StorewiseFocusSalesReport;
import intelre.cpm.com.hulcnc.gsonGetterSetter.StorewiseSalesReport;

public class PerformanceActivity extends AppCompatActivity {
    private RecyclerView lv_route, lv_focus;
    HUL_CNC_DB db;
    String store_cd, visit_date;
    ArrayList<StorewiseSalesReport> list = new ArrayList<>();
    ArrayList<StorewiseFocusSalesReport> list_focus = new ArrayList<>();
    Toolbar toolbar;
    LinearLayout linearLayout, no_data_lay;
    private SharedPreferences preferences;
    RouteAdapter routeAdapter;
    FocusAdapter focusAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lv_route = (RecyclerView) findViewById(R.id.lv_routewise);
        lv_focus = (RecyclerView) findViewById(R.id.lv_focus);

        linearLayout = findViewById(R.id.ll_layout);
        no_data_lay = findViewById(R.id.no_data_lay);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        store_cd = preferences.getString(CommonString.KEY_STORE_CD, null);
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        setTitle("Performance - " + visit_date);
        db = new HUL_CNC_DB(getApplicationContext());
        db.open();

        list = db.getPerformanceDataStorewiseReport();
        list_focus = db.getStorePerformanceDataStorewiseFocusReport();
        if (list.size() > 0) {
            routeAdapter = new RouteAdapter(getApplicationContext(), list);
            lv_route.setAdapter(routeAdapter);
            lv_route.setLayoutManager(new LinearLayoutManager(this));
            linearLayout.setVisibility(View.VISIBLE);
            no_data_lay.setVisibility(View.GONE);
        } else {
            linearLayout.setVisibility(View.GONE);
            no_data_lay.setVisibility(View.VISIBLE);
        }
        if (list_focus.size() > 0) {
            focusAdapter = new FocusAdapter(getApplicationContext(), list_focus);
            lv_focus.setAdapter(focusAdapter);
            lv_focus.setLayoutManager(new LinearLayoutManager(this));
            linearLayout.setVisibility(View.VISIBLE);
            no_data_lay.setVisibility(View.GONE);
        } else {
            linearLayout.setVisibility(View.GONE);
            no_data_lay.setVisibility(View.VISIBLE);
        }



    }

    public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.MyViewHolder> {
        private LayoutInflater inflator;
        List<StorewiseSalesReport> data = Collections.emptyList();

        public RouteAdapter(Context context, List<StorewiseSalesReport> data) {
            inflator = LayoutInflater.from(context);
            this.data = data;
        }

        @Override
        public RouteAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            View view = inflator.inflate(R.layout.route_item, parent, false);
            RouteAdapter.MyViewHolder holder = new RouteAdapter.MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final RouteAdapter.MyViewHolder viewHolder, final int position) {

            final StorewiseSalesReport current = data.get(position);

            viewHolder.sno.setText(String.valueOf(current.getSrno()));
            viewHolder.storeid.setText(String.valueOf(current.getStoreId()));
            viewHolder.achivemend.setText(String.valueOf(current.getAchivement()));
            viewHolder.timeperiod.setText(String.valueOf(current.getTimePeriod()));
            viewHolder.target.setText(String.valueOf(current.getTarget()));
            viewHolder.achiv.setText(String.valueOf(current.getAchPer()));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView sno, storeid, achivemend, timeperiod, target, achiv;

            public MyViewHolder(View itemView) {
                super(itemView);
                sno = (TextView) itemView.findViewById(R.id.sno);
                storeid = (TextView) itemView.findViewById(R.id.storeid);
                achivemend = (TextView) itemView.findViewById(R.id.achivemend);
                timeperiod = (TextView) itemView.findViewById(R.id.timeperiod);
                target = (TextView) itemView.findViewById(R.id.target);
                achiv = (TextView) itemView.findViewById(R.id.achiv);


            }
        }
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class FocusAdapter extends RecyclerView.Adapter<FocusAdapter.MyViewHolderFocus> {
        private LayoutInflater inflator;
        List<StorewiseFocusSalesReport> data = Collections.emptyList();

        public FocusAdapter(Context context, List<StorewiseFocusSalesReport> data) {
            inflator = LayoutInflater.from(context);
            this.data = data;
        }

        @Override
        public FocusAdapter.MyViewHolderFocus onCreateViewHolder(ViewGroup parent, int i) {
            View view = inflator.inflate(R.layout.focus_item, parent, false);
            FocusAdapter.MyViewHolderFocus holder = new FocusAdapter.MyViewHolderFocus(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final FocusAdapter.MyViewHolderFocus viewHolder, final int position) {

            final StorewiseFocusSalesReport current = data.get(position);

            viewHolder.sno.setText(String.valueOf(current.getSrno()));
            viewHolder.storeid.setText(String.valueOf(current.getStoreId()));
            viewHolder.achivemend.setText(String.valueOf(current.getAchivement()));
            viewHolder.timeperiod.setText(String.valueOf(current.getTimePeriod()));
            viewHolder.target.setText(String.valueOf(current.getTarget()));
            viewHolder.achiv.setText(String.valueOf(current.getAchPer()));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyViewHolderFocus extends RecyclerView.ViewHolder {
            TextView sno, storeid, achivemend, timeperiod, target, achiv;

            public MyViewHolderFocus(View itemView) {
                super(itemView);
                sno = (TextView) itemView.findViewById(R.id.sno);
                storeid = (TextView) itemView.findViewById(R.id.storeid);
                achivemend = (TextView) itemView.findViewById(R.id.achivemend);
                timeperiod = (TextView) itemView.findViewById(R.id.timeperiod);
                target = (TextView) itemView.findViewById(R.id.target);
                achiv = (TextView) itemView.findViewById(R.id.achiv);


            }
        }
    }


}
