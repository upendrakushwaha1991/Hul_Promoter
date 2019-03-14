package intelre.cpm.com.hulcnc.dailyentry;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import intelre.cpm.com.hulcnc.Database.HUL_CNC_DB;
import cpm.com.hulcnc.R;
import intelre.cpm.com.hulcnc.constant.CommonString;
import intelre.cpm.com.hulcnc.delegates.NavMenuItemGetterSetter;
import intelre.cpm.com.hulcnc.gettersetter.SearchSalesGetterSetter;
import intelre.cpm.com.hulcnc.gsonGetterSetter.JourneyPlan;

public class StoreEntryActivity extends AppCompatActivity {
    HUL_CNC_DB db;
    ValueAdapter adapter;
    RecyclerView recyclerView;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor = null;
    String store_cd, visit_date, user_type, username;
    ArrayList<JourneyPlan> specificStoreDATA = new ArrayList<>();
    String flag_quiz;
    LinearLayout img_dot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_entry);
        uivalidate();
        db = new HUL_CNC_DB(this);

    }


    private void uivalidate() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        img_dot = findViewById(R.id.img_dot);
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
        flag_quiz = preferences.getString(CommonString.KEY_FLAG_QUIZ, null);


        getSupportActionBar().setTitle("Store Entry -" + visit_date);
        img_dot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent in7 = new Intent(StoreEntryActivity.this, SalesReportActivity.class);
                Intent in7 = new Intent(StoreEntryActivity.this, SalesReportExpendable.class);
                startActivity(in7);
                overridePendingTransition(R.anim.activity_in, R.anim.activity_out);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        db.open();

        specificStoreDATA = db.getSpecificStoreData(store_cd);
        adapter = new ValueAdapter(getApplicationContext(), getdata());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));

        if (db.getSalesDone(store_cd).size() > 0) {
            img_dot.setVisibility(View.VISIBLE);
        }else {
            img_dot.setVisibility(View.INVISIBLE);
        }

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
                        //   Intent in7 = new Intent(StoreEntryActivity.this, StockAvailabilityActivity.class);
                        Intent in7 = new Intent(StoreEntryActivity.this, CategoryList.class);
                        startActivity(in7);
                        overridePendingTransition(R.anim.activity_in, R.anim.activity_out);

                    }

                    if (current.getIconImg() == R.drawable.rsp_detail || current.getIconImg() == R.drawable.rsp_detail_done) {
                        if (!db.isNoSaleDataFilled(store_cd)) {
                            //  Intent in7 = new Intent(StoreEntryActivity.this, RetailerListActivity.class);
                            Intent in7 = new Intent(StoreEntryActivity.this, SelectCategoryActivity.class);
                            startActivity(in7);
                            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                        } else {
                            Snackbar.make(recyclerView, "Data cannot be changed", Snackbar.LENGTH_LONG).show();
                        }

                    }

                    if (current.getIconImg() == R.drawable.quiz || current.getIconImg() == R.drawable.quiz_done) {
                        if (db.getHeaderQuizData().size() > 0) {
                            Intent in7 = new Intent(StoreEntryActivity.this, QuizActivity.class);
                            startActivity(in7);
                            overridePendingTransition(R.anim.activity_in, R.anim.activity_out);
                        }

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
        int stockAvalable = 0, sales = 0, quiz = 0;

        if (db.getStockDone(store_cd).size() > 0) {
            stockAvalable = R.drawable.store_audit_done;
        } else {
            stockAvalable = R.drawable.store_audit;
        }

        if (db.getSalesDone(store_cd).size() > 0) {
            sales = R.drawable.rsp_detail_done;
        } else {
            sales = R.drawable.rsp_detail;
        }

        if (flag_quiz.equals("Y")) {
            if (db.getHeaderQuizData().size() > 0) {
                if (db.isQuizFilled(store_cd)) {
                    quiz = R.drawable.quiz_done;
                } else {
                    quiz = R.drawable.quiz;
                }
            } else {
                quiz = R.drawable.quiz_gray;
            }
        }else {
            quiz = R.drawable.quiz_gray;
        }

        int img[] = {stockAvalable, sales, quiz};
        String name[] = {"Stock Availability", "Sale Entry", "Quiz"};
        for (int i = 0; i < img.length; i++) {
            NavMenuItemGetterSetter recData = new NavMenuItemGetterSetter();
            recData.setIconImg(img[i]);
            recData.setIconName(name[i]);
            data.add(recData);
        }

        /*if (flag_quiz.equals("Y")) {
            int img[] = {stockAvalable, sales, quiz};
            String name[] = {"Stock Availability", "Sale Entry", "Quiz"};
            for (int i = 0; i < img.length; i++) {
                NavMenuItemGetterSetter recData = new NavMenuItemGetterSetter();
                recData.setIconImg(img[i]);
                recData.setIconName(name[i]);
                data.add(recData);
            }
        } else {
            int img[] = {stockAvalable, sales};
            String name[] = {"Stock Availability", "Sale Entry"};
            for (int i = 0; i < img.length; i++) {
                NavMenuItemGetterSetter recData = new NavMenuItemGetterSetter();
                recData.setIconImg(img[i]);
                recData.setIconName(name[i]);
                data.add(recData);
            }

        }*/

        return data;
    }

}
