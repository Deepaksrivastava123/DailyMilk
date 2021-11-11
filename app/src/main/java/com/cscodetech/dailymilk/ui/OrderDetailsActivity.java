package com.cscodetech.dailymilk.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cscodetech.dailymilk.R;
import com.cscodetech.dailymilk.model.OrderProductDataItem;
import com.cscodetech.dailymilk.model.OrderProductListItem;
import com.cscodetech.dailymilk.model.RestResponse;
import com.cscodetech.dailymilk.model.SubOrder;
import com.cscodetech.dailymilk.model.TotaldatesItem;
import com.cscodetech.dailymilk.model.User;
import com.cscodetech.dailymilk.retrofit.APIClient;
import com.cscodetech.dailymilk.retrofit.GetResult;
import com.cscodetech.dailymilk.utility.CustPrograssbar;
import com.cscodetech.dailymilk.utility.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class OrderDetailsActivity extends AppCompatActivity implements GetResult.MyListener {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.recycler_product)
    RecyclerView recyclerProduct;

    @BindView(R.id.price)
    TextView txtPrice;
    @BindView(R.id.priced)
    TextView txtPriced;
    @BindView(R.id.txt_itemtotal)
    TextView txtItemtotal;
    @BindView(R.id.txt_variant)
    TextView txtVariant;
    @BindView(R.id.qty)
    TextView qty;

    @BindView(R.id.txt_subtotal)
    TextView txtSubtotal;
    @BindView(R.id.txt_deliverychard)
    TextView txtDeliverychard;
    @BindView(R.id.txt_coupnamouunt)
    TextView txtCoupnamouunt;
    @BindView(R.id.txt_total)
    TextView txtTotal;
    @BindView(R.id.txt_stime)
    TextView txtStime;
    @BindView(R.id.txt_pmethod)
    TextView txtPmethod;
    @BindView(R.id.txt_address)
    TextView txtAddress;
    @BindView(R.id.cmd_note)
    androidx.cardview.widget.CardView cmdNote;
    @BindView(R.id.txt_note)
    TextView txtNote;
    @BindView(R.id.btn_cancel)
    TextView btnCancel;

    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    User user;
    String oid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        ButterKnife.bind(this);
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(OrderDetailsActivity.this);
        user = sessionManager.getUserDetails("");
        oid = getIntent().getStringExtra("oid");
        recyclerProduct.setLayoutManager(new LinearLayoutManager(OrderDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recyclerProduct.setItemAnimator(new DefaultItemAnimator());


        getOrder(oid);
    }

    private void getOrder(String oid) {
        custPrograssbar.prograssCreate(OrderDetailsActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", user.getId());
            jsonObject.put("order_id", oid);

        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().getOrderItme(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");

    }

    private void getOrderCancle(String oid) {
        custPrograssbar.prograssCreate(OrderDetailsActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", user.getId());
            jsonObject.put("order_id", oid);

        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().getOrdercancle(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "2");

    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                SubOrder subOrder = gson.fromJson(result.toString(), SubOrder.class);

                recyclerProduct.setAdapter(new ProductAdapter(this, subOrder.getOrderProduct().getOrderProductData()));
                OrderProductListItem productListItem = subOrder.getOrderProduct();
                txtSubtotal.setText(sessionManager.getStringData(SessionManager.currency) + productListItem.getOrderSubTotal());
                txtDeliverychard.setText(sessionManager.getStringData(SessionManager.currency) + productListItem.getDeliveryCharge());
                txtCoupnamouunt.setText(sessionManager.getStringData(SessionManager.currency) + productListItem.getCouponAmount());
                txtTotal.setText(sessionManager.getStringData(SessionManager.currency) + productListItem.getOrderTotal());
                txtPmethod.setText(productListItem.getPMethodName() + " (" + productListItem.getOrderTransactionId() + ")");
                txtStime.setText("" + productListItem.getDeliveryTimeslot());
                txtAddress.setText("" + productListItem.getCustomerAddress());

                if (productListItem.getAdditionalNote().equalsIgnoreCase("")) {
                    cmdNote.setVisibility(View.GONE);
                } else {
                    cmdNote.setVisibility(View.VISIBLE);
                    txtNote.setText("" + productListItem.getAdditionalNote());
                }
                if (productListItem.getOrderStatus().equalsIgnoreCase("Pending")) {
                    btnCancel.setVisibility(View.VISIBLE);
                } else {
                    btnCancel.setVisibility(View.GONE);
                }
                OrderProductDataItem dataItem = subOrder.getOrderProduct().getOrderProductData().get(0);

                if (Double.parseDouble(dataItem.getProductDiscount()) != 0) {
                    Double price = Double.parseDouble(dataItem.getProductPrice()) * Double.parseDouble(dataItem.getProductDiscount()) / 100;
                    price = Double.parseDouble(dataItem.getProductPrice()) - price;
                    txtPrice.setText(sessionManager.getStringData(SessionManager.currency) + price);
                    txtPriced.setText(sessionManager.getStringData(SessionManager.currency) + dataItem.getProductPrice());
                    txtPriced.setVisibility(View.VISIBLE);
                } else {
                    txtPrice.setText(sessionManager.getStringData(SessionManager.currency) + dataItem.getProductPrice());
                    txtPriced.setVisibility(View.GONE);
                }
                txtItemtotal.setText(sessionManager.getStringData(SessionManager.currency) + dataItem.getProductTotal());
                txtVariant.setText("" + dataItem.getProductVariation());
                qty.setText("" + dataItem.getProductQuantity());

            } else if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                RestResponse restResponse = gson.fromJson(result.toString(), RestResponse.class);
                Toast.makeText(this, restResponse.getResponseMsg(), Toast.LENGTH_LONG).show();
                if (restResponse.getResult().equalsIgnoreCase("true")) {
                    finish();
                    if(MyOrderListActivity.getInstance()!=null){
                        MyOrderListActivity.getInstance().getOrder();
                    }
                }
            }
        } catch (Exception e) {
            Log.e("Error", "-->" + e.toString());
        }
    }

    public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.BannerHolder> {
        private Context context;
        private List<OrderProductDataItem> itemList;
        int selectedPosition = 0;

        public ProductAdapter(Context context, List<OrderProductDataItem> mBanner) {
            this.context = context;
            this.itemList = mBanner;
        }

        @NonNull
        @Override
        public BannerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.adapter_product_item, parent, false);
            return new BannerHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull BannerHolder holder, int position) {
            OrderProductDataItem item = itemList.get(position);

            if (selectedPosition == position)
                holder.itemView.setBackground(getResources().getDrawable(R.drawable.border_border));
            else
                holder.itemView.setBackgroundColor(Color.parseColor("#00FFC529"));

            holder.txtName.setText("" + item.getProductName());
            Glide.with(context).load(APIClient.baseUrl + "/" + itemList.get(position).getProductImage()).thumbnail(Glide.with(context).load(R.drawable.ezgifresize)).centerCrop().into(holder.imgProduct);
            holder.imgProduct.setOnClickListener(v -> {
                selectedPosition = position;
                notifyDataSetChanged();

                if (Double.parseDouble(item.getProductDiscount()) != 0) {
                    Double price = Double.parseDouble(item.getProductPrice()) * Double.parseDouble(item.getProductDiscount()) / 100;
                    price = Double.parseDouble(item.getProductPrice()) - price;
                    txtPrice.setText(sessionManager.getStringData(SessionManager.currency) + price);
                    txtPriced.setText(sessionManager.getStringData(SessionManager.currency) + item.getProductPrice());
                    txtPriced.setVisibility(View.VISIBLE);

                } else {
                    txtPrice.setText(sessionManager.getStringData(SessionManager.currency) + item.getProductPrice());
                    txtPriced.setVisibility(View.GONE);
                }
                txtItemtotal.setText(sessionManager.getStringData(SessionManager.currency) + item.getProductTotal());
                txtVariant.setText("" + item.getProductVariation());
                qty.setText("" + item.getProductQuantity());

            });

        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }

        public class BannerHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.img_product)
            ImageView imgProduct;
            @BindView(R.id.txt_name)
            TextView txtName;

            public BannerHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    public class DateAdapter extends RecyclerView.Adapter<DateAdapter.DateHolder> {
        private Context context;
        private List<TotaldatesItem> mBanner;

        public DateAdapter(Context context, List<TotaldatesItem> mBanner) {
            this.context = context;
            this.mBanner = mBanner;
        }

        @NonNull
        @Override
        public DateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.adapter_date_item, parent, false);
            return new DateHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull DateHolder holder, int position) {

            holder.txtDate.setText(mBanner.get(position).getFormatDate().substring(0, 3) + "\n" + mBanner.get(position).getFormatDate().substring(4, 6));
            if (mBanner.get(position).getIsComplete() == 1) {
                holder.txtDate.setBackground(getResources().getDrawable(R.drawable.border_border));
                holder.txtDate.setTextColor(getResources().getColor(R.color.colorPrimary));
                holder.imgRight.setVisibility(View.VISIBLE);
            } else {
                holder.txtDate.setBackground(getResources().getDrawable(R.drawable.border));
                holder.txtDate.setTextColor(getResources().getColor(R.color.colorgrey));
                holder.imgRight.setVisibility(View.GONE);

            }
        }

        @Override
        public int getItemCount() {
            return mBanner.size();


        }

        public class DateHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.txt_date)
            TextView txtDate;
            @BindView(R.id.img_right)
            ImageView imgRight;


            public DateHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    @OnClick({R.id.img_back, R.id.btn_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_cancel:

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Are you sure you want to cancel this order");
                alertDialogBuilder.setPositiveButton("yes",
                        (arg0, arg1) -> {
                            getOrderCancle(oid);
                        });

                alertDialogBuilder.setNegativeButton("No", (dialog, which) -> finish());
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                break;

            default:
                break;
        }
    }
}