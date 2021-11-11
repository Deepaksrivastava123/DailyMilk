package com.cscodetech.dailymilk.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cscodetech.dailymilk.R;
import com.cscodetech.dailymilk.adepter.MysucriptionAdapter;
import com.cscodetech.dailymilk.model.Order;
import com.cscodetech.dailymilk.model.OrderHistoryItem;
import com.cscodetech.dailymilk.model.User;
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

public class MySubcriptionListActivity extends AppCompatActivity implements GetResult.MyListener, MysucriptionAdapter.RecyclerTouchListener {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.lvl_notfound)
    LinearLayout lvlNotfound;
    @BindView(R.id.txt_notfound)
    TextView txtNotfound;
    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    User user;
    public static MySubcriptionListActivity activity;

    public static MySubcriptionListActivity getInstance() {
        return activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_subcription_list);
        ButterKnife.bind(this);
        activity = this;
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(MySubcriptionListActivity.this);
        user = sessionManager.getUserDetails("");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        getSubcription();
    }

    public void getSubcription() {
        custPrograssbar.prograssCreate(MySubcriptionListActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", user.getId());

        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().getSubcription(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");

    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                Order order = gson.fromJson(result.toString(), Order.class);
                if (order.getResult().equalsIgnoreCase("true")) {
                    if (order.getOrderHistory().isEmpty()) {
                        lvlNotfound.setVisibility(View.VISIBLE);
                    } else {
                        recyclerView.setAdapter(new MysucriptionAdapter(MySubcriptionListActivity.this, order.getOrderHistory(), this));
                    }
                } else {
                    lvlNotfound.setVisibility(View.VISIBLE);

                }
            }
        } catch (Exception e) {
e.toString();
        }
    }

    @Override
    public void onClickOrderItem(OrderHistoryItem item, int position) {
        startActivity(new Intent(MySubcriptionListActivity.this, SubOrderDetailsActivity.class).putExtra("oid", item.getId()));
    }

    @OnClick({R.id.img_back})
    public void onClick(View view) {
        if (view.getId() == R.id.img_back) {
            finish();
        }
    }
}