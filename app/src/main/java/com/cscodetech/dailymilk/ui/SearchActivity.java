package com.cscodetech.dailymilk.ui;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cscodetech.dailymilk.R;
import com.cscodetech.dailymilk.adepter.ProductChaildAdapter;
import com.cscodetech.dailymilk.model.Searchproduct;
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

public class SearchActivity extends AppCompatActivity implements GetResult.MyListener {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.lvl_notfound)
    LinearLayout lvlNofound;
    @BindView(R.id.ed_search)
    EditText edSearch;
    @BindView(R.id.recycler_product)
    RecyclerView recyclerProduct;
    CustPrograssbar custPrograssbar;
    User user;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(SearchActivity.this);
        user = sessionManager.getUserDetails("");
        GridLayoutManager mLayoutManager1 = new GridLayoutManager(this, 1);
        mLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerProduct.setLayoutManager(mLayoutManager1);
        edSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                //do here your stuff f
                getProduct();
                return true;
            }
            return false;
        });

    }

    private void getProduct() {

        custPrograssbar.prograssCreate(this);
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("uid", user.getId());
            jsonObject.put("cityid", sessionManager.getStringData(SessionManager.cityid));
            jsonObject.put("keyword", edSearch.getText().toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().search(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");

    }

    @OnClick({R.id.img_back})
    public void onClick(View view) {
        if (view.getId() == R.id.img_back) {
            finish();
        }
    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                Searchproduct searchproduct = gson.fromJson(result.toString(), Searchproduct.class);
                if (searchproduct.getResult().equalsIgnoreCase("true")) {
                    recyclerProduct.setAdapter(new ProductChaildAdapter(this, searchproduct.getProductdata()));
                    recyclerProduct.setVisibility(View.VISIBLE);
                    lvlNofound.setVisibility(View.GONE);
                } else {
                    recyclerProduct.setVisibility(View.GONE);

                    lvlNofound.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e) {
e.toString();
        }

    }
}