package intelre.cpm.com.intelre.dailyentry;

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
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import intelre.cpm.com.intelre.Database.INTALMerDB;
import intelre.cpm.com.intelre.R;
import intelre.cpm.com.intelre.constant.CommonString;
import intelre.cpm.com.intelre.gsonGetterSetter.JourneyPlan;
import intelre.cpm.com.intelre.gsonGetterSetter.StoreCategoryMaster;

public class RspListActivity extends AppCompatActivity {
    FloatingActionButton fab;
    Toolbar toolbar;
    private Context context;
    private ArrayList<JourneyPlan> storelist = new ArrayList<>();
    private RecyclerView lv_rsp_detail;
    String store_cd;
    private SharedPreferences preferences;
    respListAdapter respListAdapter;
    ArrayList<StoreCategoryMaster> list = new ArrayList<>();
    ArrayList<StoreCategoryMaster> added_list = new ArrayList<>();

    INTALMerDB db;
    private String date;
    String key_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rsp_list);
        declaration();
        lv_rsp_detail = (RecyclerView) findViewById(R.id.lv_rsp);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        db.open();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(RspListActivity.this, RspDetailActivity.class);
                in.putExtra(CommonString.KEY_MODE, CommonString.KEY_FROM_ADD_STORE);
                in.putExtra(CommonString.KEY_ID, key_id);
                startActivity(in);
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
            }
        });
    }

    public class respListAdapter extends RecyclerView.Adapter<respListAdapter.MyViewHolder> {
        private LayoutInflater inflator;
        List<StoreCategoryMaster> data = Collections.emptyList();

        public respListAdapter(Context context, List<StoreCategoryMaster> data) {
            inflator = LayoutInflater.from(context);
            this.data = data;
        }

        @Override
        public respListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            View view = inflator.inflate(R.layout.rsplist, parent, false);
            respListAdapter.MyViewHolder holder = new respListAdapter.MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final respListAdapter.MyViewHolder viewHolder, final int position) {

            final StoreCategoryMaster current = data.get(position);

            viewHolder.rsplist.setText(String.valueOf(current.getRspName()));
           /* viewHolder.tvpss.setText(String.valueOf(current.getIncentive()));
            viewHolder.tvmerchandise.setText(String.valueOf(current.getYear()));*/
            viewHolder.rsplist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(RspListActivity.this, RspDetailActivity.class);
                    i.putExtra(CommonString.KEY_OBJECT, current);
                    i.putExtra(CommonString.KEY_FLAG, "A");
                    startActivity(i);

                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView rsplist, tvpss;

            public MyViewHolder(View itemView) {
                super(itemView);
                rsplist = (TextView) itemView.findViewById(R.id.rsplist);
                tvpss = (TextView) itemView.findViewById(R.id.tvpss);

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        storelist = db.getStoreData(date);
       /* list=db.getRspDetailData(storelist.get(0).getStoreId());*/


        list = db.getRspDetailData(storelist.get(0).getStoreId());
        added_list = db.getRspDetailinsertData(storelist.get(0).getStoreId());

        if (added_list.size() > 0) {
            for (int i = 0; i < added_list.size(); i++) {
                if(added_list.get(i).getFlag().equalsIgnoreCase("")){

                }
            }

        }
        respListAdapter = new respListAdapter(getApplicationContext(), list);
        lv_rsp_detail.setAdapter(respListAdapter);
        lv_rsp_detail.setLayoutManager(new LinearLayoutManager(this));

    }

    void declaration() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = this;
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        date = preferences.getString(CommonString.KEY_DATE, null);
        toolbar.setTitle(getString(R.string.main_menu_activity_name) + " - " + date);
        //getSupportActionBar().setTitle(getString(R.string.main_menu_activity_name) + " \n- " + date);
        db = new INTALMerDB(RspListActivity.this);
        db.open();
    }

}
