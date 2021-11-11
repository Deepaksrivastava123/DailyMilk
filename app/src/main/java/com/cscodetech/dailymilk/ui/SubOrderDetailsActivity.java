package com.cscodetech.dailymilk.ui;

import static com.cscodetech.dailymilk.utility.SessionManager.wallet;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

public class SubOrderDetailsActivity extends AppCompatActivity implements GetResult.MyListener {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.recycler_product)
    androidx.recyclerview.widget.RecyclerView recyclerProduct;
    @BindView(R.id.recycler_date)
    androidx.recyclerview.widget.RecyclerView recyclerDate;
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
    @BindView(R.id.txt_sdate)
    TextView txtSdate;
    @BindView(R.id.tdelivery)
    TextView tdelivery;
    @BindView(R.id.txt_stime)
    TextView txtStime;
    @BindView(R.id.txt_subtotal)
    TextView txtSubtotal;
    @BindView(R.id.txt_deliverychard)
    TextView txtDeliverychard;
    @BindView(R.id.txt_coupnamouunt)
    TextView txtCoupnamouunt;
    @BindView(R.id.txt_total)
    TextView txtTotal;
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
    ProductAdapter productAdapter;
    CustPrograssbar custPrograssbar;
    SessionManager sessionManager;
    User user;
    String oid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_order_details);
        ButterKnife.bind(this);
        custPrograssbar = new CustPrograssbar();
        sessionManager = new SessionManager(SubOrderDetailsActivity.this);
        user = sessionManager.getUserDetails("");
        oid = getIntent().getStringExtra("oid");
        recyclerProduct.setLayoutManager(new LinearLayoutManager(SubOrderDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recyclerProduct.setItemAnimator(new DefaultItemAnimator());

        recyclerDate.setLayoutManager(new LinearLayoutManager(SubOrderDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recyclerDate.setItemAnimator(new DefaultItemAnimator());
        getSubcription(oid);
    }

    private void getSubcription(String oid) {
        custPrograssbar.prograssCreate(SubOrderDetailsActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", user.getId());
            jsonObject.put("order_id", oid);

        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().getSubcriptionItem(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");

    }

    private void getOrderCancle(String oid) {
        custPrograssbar.prograssCreate(this);
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
                productAdapter = new ProductAdapter(this, subOrder.getOrderProduct().getOrderProductData());
                recyclerProduct.setAdapter(productAdapter);
                OrderProductListItem productListItem = subOrder.getOrderProduct();
                txtSubtotal.setText(sessionManager.getStringData(SessionManager.currency) + productListItem.getOrderSubTotal());
                txtDeliverychard.setText(sessionManager.getStringData(SessionManager.currency) + productListItem.getDeliveryCharge());
                txtCoupnamouunt.setText(sessionManager.getStringData(SessionManager.currency) + productListItem.getCouponAmount());
                txtTotal.setText(sessionManager.getStringData(SessionManager.currency) + productListItem.getOrderTotal());
                if(productListItem.getWalletAmount().equalsIgnoreCase("0")){
                    txtPmethod.setText(productListItem.getPMethodName() + " (" + productListItem.getOrderTransactionId() + ")");
                }else {
                    txtPmethod.setText(productListItem.getPMethodName() + " (" + productListItem.getWalletAmount() + ")");
                }

                txtAddress.setText("" + productListItem.getCustomerAddress());
                if (productListItem.getAdditionalNote().equalsIgnoreCase("")) {
                    cmdNote.setVisibility(View.GONE);
                } else {
                    cmdNote.setVisibility(View.VISIBLE);
                    txtNote.setText("" + productListItem.getAdditionalNote());

                }
                if (productListItem.getOrderStatus().equalsIgnoreCase("Pending")) {
                    btnCancel.setVisibility(View.GONE);
                } else {
                    btnCancel.setVisibility(View.GONE);
                }
                OrderProductDataItem dataItem = subOrder.getOrderProduct().getOrderProductData().get(0);
                recyclerDate.setAdapter(new DateAdapter(this, dataItem.getTotaldates()));
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
                txtSdate.setText("" + dataItem.getStartdate());
                tdelivery.setText("" + dataItem.getTotaldelivery());
                txtStime.setText("" + dataItem.getDeliveryTimeslot());
            } else if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                RestResponse restResponse = gson.fromJson(result.toString(), RestResponse.class);
                Toast.makeText(this, restResponse.getResponseMsg(), Toast.LENGTH_LONG).show();
                if (restResponse.getResult().equalsIgnoreCase("true")) {
                    finish();
                    if (MySubcriptionListActivity.getInstance() != null) {
                        MySubcriptionListActivity.getInstance().getSubcription();
                    }
                }
            } else if (callNo.equalsIgnoreCase("3")) {
                Gson gson = new Gson();
                RestResponse restResponse = gson.fromJson(result.toString(), RestResponse.class);
                Toast.makeText(this, restResponse.getResponseMsg(), Toast.LENGTH_LONG).show();
                if (restResponse.getResult().equalsIgnoreCase("true")) {
                    sessionManager.setIntData(wallet, Integer.parseInt(restResponse.getWallet()));
                    finish();
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
        public ProductAdapter.BannerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.adapter_product_item, parent, false);
            return new ProductAdapter.BannerHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductAdapter.BannerHolder holder, int position) {
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

                recyclerDate.setAdapter(new DateAdapter(context, item.getTotaldates()));

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
                txtSdate.setText("" + item.getStartdate());
                tdelivery.setText("" + item.getTotaldelivery());
                txtStime.setText("" + item.getDeliveryTimeslot());

            });

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

        public int getItemCount() {
            return itemList.size();

        }

        public OrderProductDataItem getDatelist() {

            return itemList.get(selectedPosition);

        }
    }


    public class DateAdapter extends RecyclerView.Adapter<DateAdapter.DateHolder> {
        private Context context;
        private List<TotaldatesItem> mDateList;


        public DateAdapter(Context context, List<TotaldatesItem> mBanner) {
            this.context = context;
            this.mDateList = mBanner;
        }

        @NonNull
        @Override
        public DateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.adapter_date_item, parent, false);
            return new DateHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull DateHolder holder, int position) {

            holder.txtDate.setText(mDateList.get(position).getFormatDate().substring(0, 3) + "\n" + mDateList.get(position).getFormatDate().substring(4, 6));
            if (mDateList.get(position).getIsComplete() == 1) {
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
            return mDateList.size();


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

    @OnClick({R.id.img_back, R.id.img_skip, R.id.btn_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_skip:
                openTime();
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

    public void openTime() {

        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.skip_layout, null);
        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.show();
        TextView txtSkip = sheetView.findViewById(R.id.txt_skip);
        TextView txtExtend = sheetView.findViewById(R.id.txt_extend);
        RecyclerView recyclerView = sheetView.findViewById(R.id.recycler_date);

        GridLayoutManager mLayoutManager4 = new GridLayoutManager(this, 4);
        List<TotaldatesItem> totaldates;
        OrderProductDataItem dataItem = productAdapter.getDatelist();
        totaldates = dataItem.getTotaldates();
        recyclerView.setLayoutManager(mLayoutManager4);
        recyclerView.setAdapter(new SkipDateAdapter(this, totaldates));

        txtSkip.setOnClickListener(v -> {

            AlertDialog myDelete = new AlertDialog.Builder(this)
                    .setTitle("Skip")
                    .setMessage("Do you want to skip deliver the product today ?")
                    .setPositiveButton("Yes", (dialog, whichButton) -> {
                        dialog.dismiss();
                        JSONObject jsonObject = new JSONObject();
                        try {

                            jsonObject.put("uid", user.getId());
                            jsonObject.put("order_id", oid);
                            jsonObject.put("item_id", dataItem.getSubscribeId());
                            jsonObject.put("total", dataItem.getProductTotal());
                            jsonObject.put("status", "skip");
                            JsonArray array = new JsonArray();
                            for (int i = 0; i < totaldates.size(); i++) {
                                if (totaldates.get(i).getIsSelect() == 1) {
                                    array.add(totaldates.get(i).getDate());
                                }
                            }
                            jsonObject.put("sday", array);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        skipOrderitem(jsonObject);
                        mBottomSheetDialog.cancel();

                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        dialog.dismiss();

                    })
                    .create();
            myDelete.setCancelable(false);
            myDelete.show();


        });

        txtExtend.setOnClickListener(v -> {

            AlertDialog myDelete = new AlertDialog.Builder(this)
                    .setTitle("Skip")
                    .setMessage("Do you want to skip deliver the product today ?")
                    .setPositiveButton("Yes", (dialog, whichButton) -> {
                        dialog.dismiss();
                        JSONObject jsonObject = new JSONObject();
                        try {

                            jsonObject.put("uid", user.getId());
                            jsonObject.put("item_id", dataItem.getSubscribeId());
                            jsonObject.put("status", "extended");
                            JsonArray array = new JsonArray();
                            for (int i = 0; i < totaldates.size(); i++) {
                                if (totaldates.get(i).getIsSelect() == 1) {
                                    array.add(totaldates.get(i).getDate());
                                }
                            }
                            jsonObject.put("sday", array);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        skipOrderitem(jsonObject);
                        mBottomSheetDialog.cancel();
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        dialog.dismiss();


                    })
                    .create();
            myDelete.setCancelable(false);
            myDelete.show();


        });

    }

    private void skipOrderitem(JSONObject jsonObject) {
        custPrograssbar.prograssCreate(this);

        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().setSkipday(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "3");

    }

    public class SkipDateAdapter extends RecyclerView.Adapter<SkipDateAdapter.DateHolder> {
        private Context context;
        private List<TotaldatesItem> mDateList;
        int pposion = -1;


        public SkipDateAdapter(Context context, List<TotaldatesItem> mBanner) {
            this.context = context;
            this.mDateList = mBanner;
        }

        @NonNull
        @Override
        public DateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.adapter_date_item, parent, false);
            return new DateHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull DateHolder holder, int position) {

            holder.txtDate.setText(mDateList.get(position).getFormatDate().substring(0, 3) + "\n" + mDateList.get(position).getFormatDate().substring(4, 6));
            if (mDateList.get(position).getIsComplete() == 1) {
                holder.txtDate.setBackground(getResources().getDrawable(R.drawable.rounded_box2));
                holder.txtDate.setTextColor(getResources().getColor(R.color.black));
                holder.imgRight.setVisibility(View.VISIBLE);
                holder.imgRight.setImageResource(R.drawable.ic_delivered);

            }else {
                if (mDateList.get(position).getIsSelect() == 1) {
                    holder.txtDate.setBackground(getResources().getDrawable(R.drawable.border_border));
                    holder.txtDate.setTextColor(getResources().getColor(R.color.colorPrimary));
                    holder.imgRight.setVisibility(View.VISIBLE);
                    holder.imgRight.setImageResource(R.drawable.ic_selected_city);

                } else {
                    holder.txtDate.setBackground(getResources().getDrawable(R.drawable.rounded_box));
                    holder.txtDate.setTextColor(getResources().getColor(R.color.colorgrey));
                    holder.imgRight.setVisibility(View.GONE);

                }

            }
            holder.txtDate.setOnClickListener(v -> {
                if(mDateList.get(position).getIsComplete()==0){

                    if (mDateList.get(position).getIsSelect() == 0) {

                        mDateList.get(position).setIsSelect(1);
                        pposion = position;
                        notifyDataSetChanged();
                    } else {
                        TotaldatesItem item = mDateList.get(position);
                        item.setIsSelect(0);
                        mDateList.set(position, item);
                        notifyDataSetChanged();

                    }
                }


            });

        }

        @Override
        public int getItemCount() {
            return mDateList.size();


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
}