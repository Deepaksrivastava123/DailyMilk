package com.cscodetech.dailymilk.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cscodetech.dailymilk.R;
import com.cscodetech.dailymilk.adepter.CityAdapter;
import com.cscodetech.dailymilk.model.City;
import com.cscodetech.dailymilk.retrofit.APIClient;
import com.cscodetech.dailymilk.retrofit.GetResult;
import com.cscodetech.dailymilk.utility.CustPrograssbar;
import com.cscodetech.dailymilk.utility.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

import static com.cscodetech.dailymilk.utility.SessionManager.cityid;
import static com.cscodetech.dailymilk.utility.SessionManager.cityname;

public class CityActivity extends AppCompatActivity implements CityAdapter.RecyclerTouchListener, GetResult.MyListener {
    @BindView(R.id.recycle_city)
    RecyclerView recycleCity;
    @BindView(R.id.txt_countinus)
    TextView txtCountinus;
    CityAdapter adapter;
    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(CityActivity.this);
        custPrograssbar = new CustPrograssbar();


        recycleCity.setLayoutManager(new GridLayoutManager(this, 3));
        recycleCity.setItemAnimator(new DefaultItemAnimator());

        getcityList();
    }

    private void getcityList() {
        custPrograssbar.prograssCreate(this);
        JSONObject jsonObject = new JSONObject();
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().dCity(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");

    }

    @Override
    public void onClickCityItem(String id, String name) {

        sessionManager.setStringData(cityid, id);
        sessionManager.setStringData(cityname, name);


    }

    @OnClick({R.id.txt_countinus})
    public void onClick(View view) {
        if (view.getId() == R.id.txt_countinus) {
            if (sessionManager.getStringData(cityid) == null || sessionManager.getStringData(cityid).equalsIgnoreCase("0")) {
                Toast.makeText(CityActivity.this, "Select city", Toast.LENGTH_LONG).show();
            } else {

                startActivity(new Intent(CityActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                finish();
            }
        }
    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                City city = gson.fromJson(result.toString(), City.class);
                if (city.getResult().equalsIgnoreCase("true")) {
                    adapter = new CityAdapter(CityActivity.this, city.getCityitem(), this);
                    recycleCity.setAdapter(adapter);
                }
            }


        } catch (Exception e) {
            e.toString();
        }
    }

    @Override
    public void onBackPressed() {

    }
}