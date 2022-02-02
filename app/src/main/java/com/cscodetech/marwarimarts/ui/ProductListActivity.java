package com.cscodetech.marwarimarts.ui;

import static com.cscodetech.marwarimarts.utility.SessionManager.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.cscodetech.marwarimarts.R;
import com.cscodetech.marwarimarts.adepter.ProductMainAdapter;
import com.cscodetech.marwarimarts.adepter.SubCategoryAdapter;
import com.cscodetech.marwarimarts.model.CatlistItem;
import com.cscodetech.marwarimarts.model.ProductdataItem;
import com.cscodetech.marwarimarts.model.SubCategory;
import com.cscodetech.marwarimarts.model.User;
import com.cscodetech.marwarimarts.retrofit.APIClient;
import com.cscodetech.marwarimarts.retrofit.GetResult;
import com.cscodetech.marwarimarts.utility.CustPrograssbar;
import com.cscodetech.marwarimarts.utility.MyDatabase;
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

public class ProductListActivity extends AppCompatActivity implements SubCategoryAdapter.RecyclerTouchListener, GetResult.MyListener {

    @BindView(R.id.recycler_subcat)
    RecyclerView recyclerSubcat;
    @BindView(R.id.recycler_product)
    RecyclerView recyclerProduct;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.txt_count)
    TextView txtCount;
    @BindView(R.id.txt_total)
    TextView txtTotal;
    @BindView(R.id.txt_proced)
    TextView txtProced;
    @BindView(R.id.img_notfound)
    ImageView imgNotfound;
    @BindView(R.id.linear_layout_cart)
    LinearLayout linearLayoutCard;
    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    User user;
    CatlistItem item;
    ProductMainAdapter productMainAdapter;
    MyDatabase myDatabase;
    int qty;
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
        handleProcedClick();
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(ProductListActivity.this);
        user = sessionManager.getUserDetails("");
        myDatabase = new MyDatabase(ProductListActivity.this);
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
                    productMainAdapter = new ProductMainAdapter(ProductListActivity.this, category.getSubcatproductlist(), new ProductMainAdapter.dataListener() {
                        @Override
                        public void addCount(int count, ProductdataItem item) {
                            qty = count;
                            showHideAddToCartLayout(item);
                            Log.d("prodlisct",String.valueOf(qty));
                        }
                    });
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

    private void showHideAddToCartLayout(ProductdataItem item){
        if (qty>0){
            calculateAndSetPrice(qty,item);
            item.setProductQty(String.valueOf(qty));
            myDatabase.insertData(item);
            linearLayoutCard.setVisibility(View.VISIBLE);
        }else {
            myDatabase.deleteCard();
            linearLayoutCard.setVisibility(View.GONE);
        }
    }

    private void calculateAndSetPrice(int qty, ProductdataItem item) {
        if (Double.parseDouble(item.getProductDiscount()) != 0) {
            Double price = Double.parseDouble(item.getProductRegularprice()) * Double.parseDouble(item.getProductDiscount()) / 100;
            price = Double.parseDouble(item.getProductRegularprice()) - price;
            txtCount.setText("Item " + qty);
            txtTotal.setText(sessionManager.getStringData(SessionManager.currency) + qty*price);

        } else {
            Double price = Double.parseDouble(item.getProductRegularprice());
            txtCount.setText("Item " + qty);
            txtTotal.setText(sessionManager.getStringData(SessionManager.currency) + qty*price);
        }
    }

  private void handleProcedClick(){
      txtProced.setOnClickListener(v -> {
          if (myDatabase.getAllData() != 0) {
              if (sessionManager.getBooleanData(login)) {
                  startActivity(new Intent(ProductListActivity.this, CartActivity.class));
              } else {
                  startActivity(new Intent(ProductListActivity.this, LoginActivity.class));
              }

          } else {
              Toast.makeText(ProductListActivity.this, "Oops ! Your cart is empty !", Toast.LENGTH_LONG).show();
          }
      });
  }
}