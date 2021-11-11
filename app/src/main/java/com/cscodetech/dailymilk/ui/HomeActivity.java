package com.cscodetech.dailymilk.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cscodetech.dailymilk.BuildConfig;
import com.cscodetech.dailymilk.R;
import com.cscodetech.dailymilk.adepter.BannerAdapter;
import com.cscodetech.dailymilk.adepter.CategoryAdapter;
import com.cscodetech.dailymilk.adepter.CollectionAdapter;
import com.cscodetech.dailymilk.adepter.DiscountAdapter;
import com.cscodetech.dailymilk.adepter.FreashStockAdapter;
import com.cscodetech.dailymilk.model.BannerItem;
import com.cscodetech.dailymilk.model.CatlistItem;
import com.cscodetech.dailymilk.model.CollectionItem;
import com.cscodetech.dailymilk.model.CouponlistItem;
import com.cscodetech.dailymilk.model.Home;
import com.cscodetech.dailymilk.model.ProductdataItem;
import com.cscodetech.dailymilk.model.User;
import com.cscodetech.dailymilk.retrofit.APIClient;
import com.cscodetech.dailymilk.retrofit.GetResult;
import com.cscodetech.dailymilk.utility.CustPrograssbar;
import com.cscodetech.dailymilk.utility.MyDatabase;
import com.cscodetech.dailymilk.utility.ProductDetail;
import com.cscodetech.dailymilk.utility.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

import static com.cscodetech.dailymilk.utility.SessionManager.login;
import static com.cscodetech.dailymilk.utility.SessionManager.wallet;

public class HomeActivity extends AppCompatActivity implements CategoryAdapter.RecyclerTouchListener, FreashStockAdapter.RecyclerTouchListener, CollectionAdapter.RecyclerTouchListener, DiscountAdapter.RecyclerTouchListener, GetResult.MyListener, BannerAdapter.RecyclerTouchListener {

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;


    @BindView(R.id.my_recycler_banner)
    RecyclerView recyclerBanner;

    @BindView(R.id.recycler_category)
    RecyclerView recyclerCategory;

    @BindView(R.id.recycler_fstock)
    RecyclerView recyclerFstock;

    @BindView(R.id.recycler_collection)
    RecyclerView recyclerCollection;

    @BindView(R.id.recycler_discount)
    RecyclerView recyclerDiscount;

    @BindView(R.id.txt_cattotal)
    TextView txtCttotal;
    @BindView(R.id.txt_location)
    TextView txtLocation;
    @BindView(R.id.txt_username)
    TextView txtUsername;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.txt_login)
    TextView txtLogin;
    @BindView(R.id.txt_noticount)
    TextView txtNoticount;
    @BindView(R.id.rl_cart)
    RelativeLayout rlCart;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.userTextcaption)
            TextView userTextcaption;

    ActionBarDrawerToggle toggle;
    SessionManager sessionManager;
    CustPrograssbar custPrograssbar;
    User user;
    MyDatabase myDatabase;
    ArrayList<CatlistItem> catlist = new ArrayList<>();
    ArrayList<ProductdataItem> productdataItems = new ArrayList<>();
    ArrayList<CollectionItem> collection = new ArrayList<>();
    ArrayList<CouponlistItem> couponlistItems = new ArrayList<>();
    public static HomeActivity homeActivity;
    BannerAdapter bannerAdapter;

    public static HomeActivity getInstance() {
        return homeActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        homeActivity = this;
        myDatabase = new MyDatabase(HomeActivity.this);


        setDrawerLayout();
        sessionManager = new SessionManager(HomeActivity.this);
        user = sessionManager.getUserDetails("");

        custPrograssbar = new CustPrograssbar();
        if (sessionManager.getStringData(SessionManager.cityid) == null || sessionManager.getStringData(SessionManager.cityid).equalsIgnoreCase("0")) {
            startActivity(new Intent(this, CityActivity.class));
            finish();
        }
        if (sessionManager.getBooleanData(login)) {
            txtUsername.setText("" + user.getFname());
            txtTitle.setText("Welcome " + user.getFname());
            txtLogin.setText(getResources().getString(R.string.sign_out));
        } else {
            txtUsername.setText("Marwari Mart");
            txtTitle.setText("Welcome Marwari Mart");
            txtLogin.setText(getResources().getString(R.string.login));
        }

        StringBuilder sb = new StringBuilder(sessionManager.getStringData(SessionManager.cityname));
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        txtLocation.setText("" + sb);


        LinearLayoutManager layoutManager = new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerBanner.setLayoutManager(layoutManager);


        GridLayoutManager mLayoutManager1 = new GridLayoutManager(this, 1);
        mLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerCategory.setLayoutManager(mLayoutManager1);

        GridLayoutManager mLayoutManager2 = new GridLayoutManager(this, 2);
        mLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerFstock.setLayoutManager(mLayoutManager2);

        GridLayoutManager mLayoutManager3 = new GridLayoutManager(this, 1);
        mLayoutManager3.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerCollection.setLayoutManager(mLayoutManager3);

        GridLayoutManager mLayoutManager4 = new GridLayoutManager(this, 1);
        mLayoutManager4.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerDiscount.setLayoutManager(mLayoutManager4);
        getHome();
        txtCttotal.setText("" + new MyDatabase(HomeActivity.this).getCData().size());

        swipeRefreshLayout.setOnRefreshListener(() -> {
            getHome();
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    public void setCountitem(String count) {
        txtCttotal.setText(count);
    }

    private void getHome() {
        custPrograssbar.prograssCreate(HomeActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("uid", user.getId());
            jsonObject.put("cityid", sessionManager.getStringData(SessionManager.cityid));

        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().getHome(bodyRequest);
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
                Home home = gson.fromJson(result.toString(), Home.class);
                if (home.getResult().equalsIgnoreCase("true")) {
                    catlist = home.getResultData().getCatlist();
                    productdataItems = home.getResultData().getFStock();
                    collection = home.getResultData().getCollection();
                    couponlistItems = home.getResultData().getCouponlist();
                    bannerAdapter = new BannerAdapter(HomeActivity.this, home.getResultData().getBanner(),this);
                    recyclerBanner.setAdapter(bannerAdapter);
                    recyclerCategory.setAdapter(new CategoryAdapter(HomeActivity.this, home.getResultData().getCatlist(), this, "fhsk"));
                    recyclerFstock.setAdapter(new FreashStockAdapter(HomeActivity.this, home.getResultData().getFStock(), this, "fhsk"));
                    recyclerCollection.setAdapter(new CollectionAdapter(HomeActivity.this, home.getResultData().getCollection(), this, "fhsk"));
                    recyclerDiscount.setAdapter(new DiscountAdapter(HomeActivity.this, home.getResultData().getCouponlist(), this, "fhsk"));
                    recyclerBanner.post(new Runnable() {

                        @Override
                        public void run() {
                            recyclerBanner.smoothScrollToPosition(bannerAdapter.getItemCount() - 1);
                        }
                    });
                    sessionManager.setStringData(SessionManager.currency, home.getResultData().getMainData().getCurrency());
                    sessionManager.setStringData(SessionManager.policy, home.getResultData().getMainData().getPolicy());
                    sessionManager.setStringData(SessionManager.about, home.getResultData().getMainData().getAbout());
                    sessionManager.setStringData(SessionManager.contact, home.getResultData().getMainData().getContact());
                    sessionManager.setStringData(SessionManager.terms, home.getResultData().getMainData().getTerms());
                    sessionManager.setStringData(SessionManager.productlimit, home.getResultData().getMainData().getPLimit());
                    sessionManager.setIntData(wallet, Integer.parseInt(home.getResultData().getWallet()));

                }


            }
        } catch (Exception e) {
            e.toString();
        }
    }

    public void setDrawerLayout() {
        toggle = new ActionBarDrawerToggle(HomeActivity.this, drawerLayout, R.string.nav_app_bar_open_drawer_description, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


    }


    @OnClick({R.id.img_menu, R.id.txt_viewllc, R.id.txt_viewllf, R.id.txt_viewllcollect, R.id.txt_viewllDiccount, R.id.rl_noti, R.id.rl_cart, R.id.lvl_search, R.id.subcription, R.id.m_wallet, R.id.m_order, R.id.m_address, R.id.m_refer, R.id.m_share, R.id.m_support, R.id.m_about, R.id.m_privacy, R.id.m_logout, R.id.txt_location})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_menu:
                drawerLayout.openDrawer(Gravity.LEFT);
                break;
            case R.id.txt_viewllc:
                startActivity(new Intent(HomeActivity.this, CategoryActivity.class).putParcelableArrayListExtra("mylist", catlist));

                break;
            case R.id.txt_viewllf:
                startActivity(new Intent(HomeActivity.this, FreshStockActivity.class).putParcelableArrayListExtra("mylist", productdataItems));
                break;
            case R.id.txt_viewllcollect:
                startActivity(new Intent(HomeActivity.this, CollectonActivity.class).putParcelableArrayListExtra("mylist", collection));
                break;
            case R.id.txt_viewllDiccount:
                startActivity(new Intent(HomeActivity.this, DiscountActivity.class).putParcelableArrayListExtra("mylist", couponlistItems));
                break;
            case R.id.rl_noti:
                startActivity(new Intent(HomeActivity.this, NotificationActivity.class));
                break;
            case R.id.rl_cart:

                if (myDatabase.getAllData() != 0) {
                    if (sessionManager.getBooleanData(login)) {
                        startActivity(new Intent(HomeActivity.this, CartActivity.class));
                    } else {
                        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                    }
                } else {
                    Toast.makeText(this, "Oops ! Your cart is empty !", Toast.LENGTH_LONG).show();
                }


                break;
            case R.id.lvl_search:
                startActivity(new Intent(HomeActivity.this, SearchActivity.class));
                break;

            case R.id.subcription:
                if (sessionManager.getBooleanData(login)) {
                    startActivity(new Intent(HomeActivity.this, MySubcriptionListActivity.class));
                } else {
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                }
                drawerLayout.closeDrawers();
                break;
            case R.id.m_wallet:
                if (sessionManager.getBooleanData(login)) {
                    startActivity(new Intent(HomeActivity.this, WalletActivity.class));
                } else {
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                }
                drawerLayout.closeDrawers();
                break;
            case R.id.m_order:
                if (sessionManager.getBooleanData(login)) {
                    startActivity(new Intent(HomeActivity.this, MyOrderListActivity.class));
                } else {
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                }
                drawerLayout.closeDrawers();
                break;
            case R.id.m_address:
                if (sessionManager.getBooleanData(login)) {
                    startActivity(new Intent(HomeActivity.this, AddressActivity.class));
                } else {
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                }

                drawerLayout.closeDrawers();
                break;
            case R.id.m_refer:
                drawerLayout.closeDrawers();
                if (sessionManager.getBooleanData(login)) {
                    startActivity(new Intent(HomeActivity.this, ReferlActivity.class));
                } else {
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                }

                break;
            case R.id.m_share:
                drawerLayout.closeDrawers();
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
                String shareMessage = "Hey! Now use our app to share with your family or friends. User will get wallet amount on your 1st successful order. Enter my referral code *" + 1234 + "* & Enjoy your shopping !!!";
                shareMessage = shareMessage + " https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
                break;
            case R.id.m_support:
                drawerLayout.closeDrawers();
                startActivity(new Intent(HomeActivity.this, SupportActivity.class));
                break;
            case R.id.m_about:
                drawerLayout.closeDrawers();
                startActivity(new Intent(HomeActivity.this, AboutActivity.class));

                break;
//            case R.id.m_spin:
//                drawerLayout.closeDrawers();
//                startActivity(new Intent(HomeActivity.this, PrivacyActivity.class));
//
//                break;

            case R.id.m_privacy:
                drawerLayout.closeDrawers();
                startActivity(new Intent(HomeActivity.this, PrivacyActivity.class));

                break;
            case R.id.m_logout:
                drawerLayout.closeDrawers();
                if (sessionManager.getBooleanData(login)) {
                    sessionManager.logoutUser();
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                }

                break;
            case R.id.txt_location:
                startActivity(new Intent(HomeActivity.this, CityActivity.class));

                break;


            default:
                break;
        }
    }

    @Override
    public void onClickBannerItem(BannerItem item, int position) {
        CatlistItem cat=new CatlistItem(item.getCatImg(),item.getCatId(),item.getTitle());
        startActivity(new Intent(HomeActivity.this, ProductListActivity.class).putExtra("myclass", cat));

    }

    @Override
    public void onClickCategoryItem(CatlistItem item, int position) {
        startActivity(new Intent(HomeActivity.this, ProductListActivity.class).putExtra("myclass", item));

    }

    @Override
    public void onClickFreashStockItem(ProductdataItem item, int position) {
        new ProductDetail().bottonAddtoCard(this, item);

    }

    @Override
    public void onClickCollectionItem(CollectionItem item, int position) {

        startActivity(new Intent(HomeActivity.this, CollectionProductActivity.class).putParcelableArrayListExtra("mylist", item.getProductdata()));

    }

    @Override
    public void onClickDiscountItem(String item, int position) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (txtCttotal != null) {
            txtCttotal.setText("" + myDatabase.getAllData());
        }
    }


}