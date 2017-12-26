package intelre.cpm.com.intelre.dailyentry;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
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
import intelre.cpm.com.intelre.delegates.NavMenuItemGetterSetter;

public class StoreEntryActivity extends AppCompatActivity {
    INTEL_RE_DB db;
    ValueAdapter adapter;
    RecyclerView recyclerView;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor = null;
    String store_cd, visit_date, user_type, username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_entry);
        uivalidate();
        db = new INTEL_RE_DB(this);
    }

    private void uivalidate() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.drawer_layout_recycle_store);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        store_cd = preferences.getString(CommonString.KEY_STORE_CD, null);
        username = preferences.getString(CommonString.KEY_USERNAME, null);
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        user_type = preferences.getString(CommonString.KEY_USER_TYPE, null);
        getSupportActionBar().setTitle("Store Entry -" + visit_date);
    }

    @Override
    protected void onResume() {
        super.onResume();
        db.open();
        adapter = new ValueAdapter(getApplicationContext(), getdata());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
    }

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

    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
        this.finish();
    }

    public class ValueAdapter extends RecyclerView.Adapter<ValueAdapter.MyViewHolder> {
        private LayoutInflater inflator;
        List<NavMenuItemGetterSetter> data = Collections.emptyList();

        public ValueAdapter(Context context, List<NavMenuItemGetterSetter> data) {
            inflator = LayoutInflater.from(context);
            this.data = data;
        }

        @Override
        public ValueAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            View view = inflator.inflate(R.layout.custom_row, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final ValueAdapter.MyViewHolder viewHolder, final int position) {
            final NavMenuItemGetterSetter current = data.get(position);
            viewHolder.icon.setImageResource(current.getIconImg());
            viewHolder.icon_txtname.setText(current.getIconName());
            viewHolder.icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (current.getIconImg() == R.drawable.store_audit || current.getIconImg() == R.drawable.store_audit_done) {
                        if (db.getStoreAuditHeaderData().size() > 0) {
                            Intent in7 = new Intent(StoreEntryActivity.this, StoreAuditActivity.class);
                            startActivity(in7);
                            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                        } else {
                            Snackbar.make(recyclerView, "Store audit data not found", Snackbar.LENGTH_LONG).show();
                        }

                    }
                    if (current.getIconImg() == R.drawable.visibility || current.getIconImg() == R.drawable.visibility_done) {
                        startActivity(new Intent(StoreEntryActivity.this, IntelVisibilityMenu.class));
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);

                    }

                    if (current.getIconImg() == R.drawable.market_info || current.getIconImg() == R.drawable.market_info_done) {
                        startActivity(new Intent(StoreEntryActivity.this, MarketInfoActivity.class));
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);

                    }

                    if (current.getIconImg() == R.drawable.shopper_mktg_tool || current.getIconImg() == R.drawable.shopper_mktg_tool_done) {
                        startActivity(new Intent(StoreEntryActivity.this, ShoperMToolMenu.class));
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);

                    }


                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView icon;
            TextView icon_txtname;

            public MyViewHolder(View itemView) {
                super(itemView);
                icon = (ImageView) itemView.findViewById(R.id.list_icon);
                icon_txtname = (TextView) itemView.findViewById(R.id.icon_txtname);
            }
        }

    }

    public List<NavMenuItemGetterSetter> getdata() {
        List<NavMenuItemGetterSetter> data = new ArrayList<>();
        int rspDetail = 0, storeAudit=0, training, visibility, shoperMKTTool, marketInfo;

        // if (db.getRspDetailData(store_cd).size() > 0) {
        // if (db.isSRSPDetailFilled(store_cd)) {
        //   rspDetail = R.drawable.rsp_detail_done;
        //   } else {
        rspDetail = R.drawable.rsp_detail;
        //  }
        //  }


        if (db.getStoreAuditHeaderData().size() > 0) {
            if (db.isStoreAuditFilled(store_cd)) {
                storeAudit = R.drawable.store_audit_done;
            } else {
                storeAudit = R.drawable.store_audit;
            }
        }


        // if (db.isMiddayDataFilled(store_cd)) {
        // training = R.drawable.training_done;
        //  } else {
        training = R.drawable.training;
        //  }

        if (db.isVisibilitySoftMerchFilled(store_cd) && db.isVisibilitySPMerchFilled(store_cd)) {
            visibility = R.drawable.visibility_done;
        } else {
            visibility = R.drawable.visibility;
        }


        if (db.isRXTFilled(store_cd) && db.isIPOSFilled(store_cd)) {
            shoperMKTTool = R.drawable.shopper_mktg_tool_done;
        } else {
            shoperMKTTool = R.drawable.shopper_mktg_tool;
        }

        if (db.isMarketInfoFilled(store_cd)) {
            marketInfo = R.drawable.market_info_done;
        } else {
            marketInfo = R.drawable.market_info;
        }


        int img[] = {rspDetail, storeAudit, training, visibility, shoperMKTTool, marketInfo};
        String name[] = {"RSP Detail", "Store Audit", "Training", "Visibility", "Shoper MKT Tool", "Market Info"};
        for (int i = 0; i < img.length; i++) {
            NavMenuItemGetterSetter recData = new NavMenuItemGetterSetter();
            recData.setIconImg(img[i]);
            recData.setIconName(name[i]);
            data.add(recData);
        }

        return data;
    }


}
