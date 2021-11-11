package com.cscodetech.dailymilk.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cscodetech.dailymilk.R;
import com.cscodetech.dailymilk.locationpick.LocationGetActivity;
import com.cscodetech.dailymilk.locationpick.MapUtility;
import com.cscodetech.dailymilk.model.Address;
import com.cscodetech.dailymilk.model.AddressListItem;
import com.cscodetech.dailymilk.model.User;
import com.cscodetech.dailymilk.retrofit.APIClient;
import com.cscodetech.dailymilk.retrofit.GetResult;
import com.cscodetech.dailymilk.utility.CustPrograssbar;
import com.cscodetech.dailymilk.utility.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class AddressActivity extends AppCompatActivity implements GetResult.MyListener {
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.recycle_address)
    RecyclerView recycleAddress;
    @BindView(R.id.lvl_notfound)
    LinearLayout lvlNotfound;
    @BindView(R.id.txt_addaddress)
    TextView txtAddaddress;
    SessionManager sessionManager;
    CustPrograssbar custPrograssbar;
    User user;
    int requestIdMultiplePermissions = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        ButterKnife.bind(this);
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(AddressActivity.this);
        user = sessionManager.getUserDetails("");
        LinearLayoutManager layoutManager = new LinearLayoutManager(AddressActivity.this, LinearLayoutManager.VERTICAL, false);
        recycleAddress.setLayoutManager(layoutManager);
        getAddressList();
        checkAndRequestPermissions();
    }

    private void getAddressList() {
        custPrograssbar.prograssCreate(this);
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("uid", user.getId());


        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().getAddress(bodyRequest);
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
                Address address = gson.fromJson(result.toString(), Address.class);
                if (address.getResult().equalsIgnoreCase("true")) {
                    if (address.getAddressList().isEmpty()) {
                        lvlNotfound.setVisibility(View.VISIBLE);
                        recycleAddress.setVisibility(View.GONE);
                    } else {
                        recycleAddress.setVisibility(View.VISIBLE);
                        lvlNotfound.setVisibility(View.GONE);
                        recycleAddress.setAdapter(new AdepterAddress(AddressActivity.this, address.getAddressList()));
                    }
                } else {
                    lvlNotfound.setVisibility(View.VISIBLE);
                    recycleAddress.setVisibility(View.GONE);

                }
            }
        } catch (Exception e) {
e.toString();
        }
    }

    @OnClick({R.id.img_back, R.id.txt_addaddress})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
//                finish();
//                break;
            case R.id.txt_addaddress:
                startActivity(new Intent(AddressActivity.this, LocationGetActivity.class)
                        .putExtra(MapUtility.latitude, 0.0)
                        .putExtra(MapUtility.longitude, 0.0)
//                        .putExtra("landmark","")
//                        .putExtra("hno", "")
                        .putExtra("atype", "Home")
                        .putExtra("newuser", "Newuser")
                        .putExtra("userid", user.getId())
                        .putExtra("aid", "0"));

                break;
            default:
                break;
        }
    }

    private boolean checkAndRequestPermissions() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        Permissions.check(this, permissions, null, null, new PermissionHandler() {
            @Override
            public void onGranted() {

            }
            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                // permission denied, block the feature.
                checkAndRequestPermissions();
            }
        });
//        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
//        int coarsePermision = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
//        List<String> listPermissionsNeeded = new ArrayList<>();
//
//        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
//            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
//        }
//        if (coarsePermision != PackageManager.PERMISSION_GRANTED) {
//            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
//        }
//
//        if (!listPermissionsNeeded.isEmpty()) {
//            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[0]), requestIdMultiplePermissions);
//            return false;
//        }
//
        return true;

    }


    public class AdepterAddress extends RecyclerView.Adapter<AdepterAddress.BannerHolder> {
        private Context context;
        private List<AddressListItem> listItems;

        public AdepterAddress(Context context, List<AddressListItem> mBanner) {
            this.context = context;
            this.listItems = mBanner;
        }

        @NonNull
        @Override
        public BannerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.addresss_item, parent, false);
            return new BannerHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull BannerHolder holder, int position) {
            holder.txtType.setText("" + listItems.get(position).getType());
            holder.txtName.setText("" + listItems.get(position).getName() + "-" + listItems.get(position).getMobile());

            holder.txtHomeaddress.setText(listItems.get(position).getHno() + listItems.get(position).getLandmark() + "," + listItems.get(position).getAddress());
            Glide.with(context).load(APIClient.baseUrl + "/" + listItems.get(position).getAddressImage()).into(holder.imgBanner);
            holder.imgMenu.setOnClickListener(v -> {
                PopupMenu popup = new PopupMenu(context, holder.imgMenu);
                popup.inflate(R.menu.address_menu);
                popup.setOnMenuItemClickListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.menu_select:
                            sessionManager.setIntData(SessionManager.address, position);
                            break;
                        case R.id.menu_edit:
                            startActivity(new Intent(AddressActivity.this, LocationGetActivity.class)
                                    .putExtra(MapUtility.latitude, listItems.get(position).getLatMap())
                                    .putExtra(MapUtility.longitude, listItems.get(position).getLongMap())
                                    .putExtra("landmark", listItems.get(position).getLandmark())
                                    .putExtra("hno", listItems.get(position).getHno())
                                    .putExtra("atype", listItems.get(position).getType())
                                    .putExtra("newuser", "old")
                                    .putExtra("userid", user.getId())
                                    .putExtra("name", listItems.get(position).getName())
                                    .putExtra("mobile", listItems.get(position).getMobile())
                                    .putExtra("aid", listItems.get(position).getId()));

                            break;
                        default:
                            break;
                    }
                    return false;
                });
                popup.show();
            });

        }

        @Override
        public int getItemCount() {
            return listItems.size();
        }

        public class BannerHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.img_banner)
            ImageView imgBanner;
            @BindView(R.id.img_menu)
            ImageView imgMenu;
            @BindView(R.id.txt_homeaddress)
            TextView txtHomeaddress;
            @BindView(R.id.txt_tital)
            TextView txtType;
            @BindView(R.id.txt_name)
            TextView txtName;

            @BindView(R.id.lvl_home)
            LinearLayout lvlHome;

            public BannerHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sessionManager != null) {
            getAddressList();
        }


    }
}