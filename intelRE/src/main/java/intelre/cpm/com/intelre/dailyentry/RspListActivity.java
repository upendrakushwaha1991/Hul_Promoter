package intelre.cpm.com.intelre.dailyentry;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import intelre.cpm.com.intelre.Database.INTEL_RE_DB;
import intelre.cpm.com.intelre.R;
import intelre.cpm.com.intelre.constant.CommonString;
import intelre.cpm.com.intelre.gettersetter.StoreProfileGetterSetter;
import intelre.cpm.com.intelre.gsonGetterSetter.JourneyPlan;
import intelre.cpm.com.intelre.gsonGetterSetter.StoreCategoryMaster;

public class RspListActivity extends AppCompatActivity {
    FloatingActionButton fab;
    TextView store_name;
    Toolbar toolbar;
    private Context context;
    private ArrayList<JourneyPlan> storelist = new ArrayList<>();
    private RecyclerView lv_rsp_detail;
    String store_cd;
    private SharedPreferences preferences;
    respListAdapter respListAdapter;
    ArrayList<StoreCategoryMaster> list = new ArrayList<>();
    ArrayList<StoreCategoryMaster> added_list = new ArrayList<>();

    INTEL_RE_DB db;
    private String date;
    String key_id;
    StoreProfileGetterSetter storePGT;
    String visit_date, userId, user_type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rsp_list);
        declaration();
        lv_rsp_detail = (RecyclerView) findViewById(R.id.lv_rsp);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        db.open();
        storePGT = new StoreProfileGetterSetter();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(RspListActivity.this, RspDetailActivity.class);
                in.putExtra(CommonString.KEY_FLAG, CommonString.KEY_ADD);
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
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            View view = inflator.inflate(R.layout.rsplist, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder viewHolder, final int position) {

            final StoreCategoryMaster current = data.get(position);
            viewHolder.rsplist.setText(String.valueOf(current.getRspName()));
            viewHolder.delete_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(RspListActivity.this);
                    builder.setMessage("Are you sure you want to Delete")
                            .setCancelable(false)
                            .setPositiveButton("Yes",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            if (current.getRspId()==0){
                                                db.deleteRspDetail(current);
                                            }else {
                                                current.setFlag(CommonString.KEY_DELETE);
                                                db.InsertRspDetailData(current, store_cd, date);
                                            }

                                            list.remove(position);
                                            respListAdapter.notifyDataSetChanged();
                                        }
                                    })
                            .setNegativeButton("No",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int id) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();

                }
            });

            viewHolder.rsplist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent i = new Intent(RspListActivity.this, RspDetailActivity.class);
                    i.putExtra(CommonString.KEY_OBJECT, current);
                     if (current.getRspId()==0){
                         i.putExtra(CommonString.KEY_FLAG, CommonString.KEY_ADD);
                     }
                     else {
                         i.putExtra(CommonString.KEY_FLAG, CommonString.KEY_EDIT);
                     }


                    startActivity(i);

                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView rsplist;
            ImageView delete_icon;

            public MyViewHolder(View itemView) {
                super(itemView);
                rsplist = (TextView) itemView.findViewById(R.id.rsplist);
                delete_icon = (ImageView) itemView.findViewById(R.id.delete_icon);

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

       // storelist = db.getStoreData(date);
        storePGT = db.getStoreProfileData(store_cd, visit_date);

        list = db.getRspDetailData(store_cd);
        added_list = db.getRspDetailinsertData(store_cd);

        if (added_list.size() > 0) {
            for (int i = 0; i < added_list.size(); i++) {
                if (added_list.get(i).getFlag().equalsIgnoreCase(CommonString.KEY_ADD)) {
                    list.add(added_list.get(i));
                } else if (added_list.get(i).getFlag().equalsIgnoreCase(CommonString.KEY_EDIT)) {

                   int pos= getrspPosition(added_list.get(i).getRspId());
                    list.set(pos,added_list.get(i));
                }else if (added_list.get(i).getFlag().equalsIgnoreCase(CommonString.KEY_DELETE)){
                    int pos= getrspPosition(added_list.get(i).getRspId());
                    list.remove(pos);
                }
            }

        }
        respListAdapter = new respListAdapter(getApplicationContext(), list);
        lv_rsp_detail.setAdapter(respListAdapter);
        lv_rsp_detail.setLayoutManager(new LinearLayoutManager(this));

    }

    void declaration() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        store_name=(TextView)findViewById(R.id.store_name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        date = preferences.getString(CommonString.KEY_DATE, null);
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        userId = preferences.getString(CommonString.KEY_USERNAME, null);
        user_type = preferences.getString(CommonString.KEY_USER_TYPE, null);
        store_cd = preferences.getString(CommonString.KEY_STORE_CD, null);


        toolbar.setTitle(getString(R.string.main_menu_activity_name) + " - " + date);
        getSupportActionBar().setTitle(getString(R.string.title_activity_rsp_list) + " \n- " + date);
        db = new INTEL_RE_DB(RspListActivity.this);
        db.open();
        storelist = db.getStoreData(date);
      //  storelist = db.getStoreData(date);
        if (storelist.size()>0){
            for (int i = 0; i < storelist.size(); i++) {
                store_name.setText(storelist.get(i).getStoreName());
            }
        }

        //  store_name.setText(storelist.get(0).getStoreName());
    }

    private int getrspPosition(int rsp_id ) {
        int pos=0;
       for (int i=0; list.size()<0; i++){
          if (list.get(i).getRspId()==0){
              pos=i;
              break;
          }
       }
        return pos;
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

        return super.onOptionsItemSelected(item);
    }


}
