package com.cscodetech.marwarimarts.ui;

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

import com.cscodetech.marwarimarts.R;
import com.cscodetech.marwarimarts.adepter.MysucriptionAdapter;
import com.cscodetech.marwarimarts.model.Order;
import com.cscodetech.marwarimarts.model.OrderHistoryItem;
import com.cscodetech.marwarimarts.model.User;
import com.cscodetech.marwarimarts.retrofit.APIClient;
import com.cscodetech.marwarimarts.retrofit.GetResult;
import com.cscodetech.marwarimarts.utility.CustPrograssbar;
import com.cscodetech.marwarimarts.utility.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class MyOrderListActivity extends AppCompatActivity implements GetResult.MyListener, MysucriptionAdapter.RecyclerTouchListener {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.lvl_notfound)
    LinearLayout lvlNotfound;
    @BindView(R.id.txt_notfound)
    TextView txtNotfound;
    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    User user;
    public static MyOrderListActivity activity;
    public static MyOrderListActivity getInstance(){
        return activity;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_list);
        ButterKnife.bind(this);
        activity=this;
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(MyOrderListActivity.this);
        user = sessionManager.getUserDetails("");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        getOrder();
    }

    public void getOrder() {
        custPrograssbar.prograssCreate(MyOrderListActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", user.getId());

        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().getOrder(bodyRequest);
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
                        recyclerView.setAdapter(new MysucriptionAdapter(MyOrderListActivity.this, order.getOrderHistory(), this));
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
        startActivity(new Intent(this, OrderDetailsActivity.class).putExtra("oid",item.getId()));

    }

    @OnClick({R.id.img_back})
    public void onClick(View view) {
        if (view.getId() == R.id.img_back) {
            finish();
        }
    }
}