package com.cscodetech.dailymilk.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.cscodetech.dailymilk.R;
import com.cscodetech.dailymilk.adepter.ProductMainAdapter;
import com.cscodetech.dailymilk.adepter.SubCategoryAdapter;
import com.cscodetech.dailymilk.model.CatlistItem;
import com.cscodetech.dailymilk.model.SubCategory;
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

public class ProductListActivity extends AppCompatActivity implements SubCategoryAdapter.RecyclerTouchListener, GetResult.MyListener {

    @BindView(R.id.recycler_subcat)
    RecyclerView recyclerSubcat;
    @BindView(R.id.recycler_product)
    RecyclerView recyclerProduct;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.img_notfound)
    ImageView imgNotfound;
    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    User user;
    CatlistItem item;
    ProductMainAdapter productMainAdapter;
    public static ProductListActivity activity;

    public static ProductListActivity getInstance() {
        return activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        activity = this;
        ButterKnife.bind(this);
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(ProductListActivity.this);
        user = sessionManager.getUserDetails("");
        item = getIntent().getParcelableExtra("myclass");
        txtTitle.setText("" + item.getTitle());

        StaggeredGridLayoutManager mLayoutManager1 = new StaggeredGridLayoutManager(1, GridLayoutManager.HORIZONTAL);
        recyclerSubcat.setLayoutManager(mLayoutManager1);

        GridLayoutManager mLayoutManager3 = new GridLayoutManager(this, 1);
        mLayoutManager3.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerProduct.setLayoutManager(mLayoutManager3);

        getProduct();
    }

    private void getProduct() {
        custPrograssbar.prograssCreate(ProductListActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("uid", user.getId());
            jsonObject.put("cityid", sessionManager.getStringData(SessionManager.cityid));
            jsonObject.put("catid", item.getId());

        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().sSubList(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");

    }

    @Override
    public void onClickSubCategoryItem(String item, int position) {

        recyclerProduct.getLayoutManager().scrollToPosition(position);


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
                SubCategory category = gson.fromJson(result.toString(), SubCategory.class);
                if (category.getResult().equalsIgnoreCase("true")) {
                    recyclerSubcat.setAdapter(new SubCategoryAdapter(ProductListActivity.this, category.getSubcatproductlist(), this));
                    productMainAdapter = new ProductMainAdapter(ProductListActivity.this, category.getSubcatproductlist());
                    recyclerProduct.setAdapter(productMainAdapter);
                } else {
                    imgNotfound.setVisibility(View.VISIBLE);
                }

            }
        } catch (Exception e) {
            e.toString();
        }
    }

    public void notifiyChange() {
        productMainAdapter.notifyDataSetChanged();
    }
}