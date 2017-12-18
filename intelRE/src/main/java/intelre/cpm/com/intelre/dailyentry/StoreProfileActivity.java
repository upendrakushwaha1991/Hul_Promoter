package intelre.cpm.com.intelre.dailyentry;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import intelre.cpm.com.intelre.R;
import intelre.cpm.com.intelre.constant.CommonString;
import intelre.cpm.com.intelre.gpsenable.LocationEnableCommon;

public class StoreProfileActivity extends AppCompatActivity {
    EditText storeProfile_userN, storeProfile_address_1, storeProfile_ownerN,
            storeProfile_contctN, storeProfile_visibtLo, storeProfile_dimention;
    Spinner storeProfile_spinCity;
    TextView storeProfile_dob, storeProfile_doa;
    FloatingActionButton btn_save, btn_next;
    ImageView img_dob, img_doa;
    SharedPreferences preferences;
    String visit_date, userId, user_type, store_cd, store_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_profile);
    }

    private void declaration() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btn_save = findViewById(R.id.btn_save);
        btn_next = findViewById(R.id.btn_next);
        storeProfile_userN = findViewById(R.id.storeProfile_userN);
        storeProfile_address_1 = findViewById(R.id.storeProfile_address_1);
        storeProfile_ownerN = findViewById(R.id.storeProfile_ownerN);
        storeProfile_contctN = findViewById(R.id.storeProfile_contctN);
        storeProfile_visibtLo = findViewById(R.id.storeProfile_visibtLo);
        storeProfile_dimention = findViewById(R.id.storeProfile_dimention);
        storeProfile_spinCity = findViewById(R.id.storeProfile_spinCity);
        storeProfile_dob = findViewById(R.id.storeProfile_dob);
        storeProfile_doa = findViewById(R.id.storeProfile_doa);
        img_dob = findViewById(R.id.img_dob);
        img_doa = findViewById(R.id.img_doa);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        visit_date = preferences.getString(CommonString.KEY_DATE, null);
        userId = preferences.getString(CommonString.KEY_USERNAME, null);
        user_type = preferences.getString(CommonString.KEY_USER_TYPE, null);
        store_cd = preferences.getString(CommonString.KEY_STORE_CD, null);
        getSupportActionBar().setTitle("Store Profile - " + visit_date);
    }

}
