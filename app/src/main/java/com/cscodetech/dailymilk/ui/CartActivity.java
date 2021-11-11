package com.cscodetech.dailymilk.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cscodetech.dailymilk.R;
import com.cscodetech.dailymilk.adepter.CartRegulerAdapter;
import com.cscodetech.dailymilk.adepter.CartSubcriptionAdapter;
import com.cscodetech.dailymilk.model.Address;
import com.cscodetech.dailymilk.model.AddressListItem;
import com.cscodetech.dailymilk.model.Payment;
import com.cscodetech.dailymilk.model.PaymentItem;
import com.cscodetech.dailymilk.model.ProductdataItem;
import com.cscodetech.dailymilk.model.RestResponse;
import com.cscodetech.dailymilk.model.TimeDatum;
import com.cscodetech.dailymilk.model.Times;
import com.cscodetech.dailymilk.model.User;
import com.cscodetech.dailymilk.retrofit.APIClient;
import com.cscodetech.dailymilk.retrofit.GetResult;
import com.cscodetech.dailymilk.utility.CustPrograssbar;
import com.cscodetech.dailymilk.utility.MyDatabase;
import com.cscodetech.dailymilk.utility.SessionManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

import static com.cscodetech.dailymilk.utility.SessionManager.coupon;
import static com.cscodetech.dailymilk.utility.SessionManager.couponid;
import static com.cscodetech.dailymilk.utility.SessionManager.currency;
import static com.cscodetech.dailymilk.utility.SessionManager.wallet;
import static com.cscodetech.dailymilk.utility.Utility.paymentId;
import static com.cscodetech.dailymilk.utility.Utility.paymentsucsses;
import static com.cscodetech.dailymilk.utility.Utility.tragectionID;

public class CartActivity extends AppCompatActivity implements GetResult.MyListener {
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_rdate)
    TextView txtRdate;
    @BindView(R.id.txt_dayname)
    TextView txtDayname;
    @BindView(R.id.txt_ardate)
    TextView txtArdate;
    @BindView(R.id.txt_artime)
    TextView txtArtime;
    @BindView(R.id.img_rdate)
    ImageView imgRdate;
    @BindView(R.id.recycle_reguler)
    RecyclerView recycleReguler;

    @BindView(R.id.recycle_subcription)
    RecyclerView recycleSubcription;
    @BindView(R.id.txt_capply)
    TextView txtCapply;
    @BindView(R.id.txt_subtotal)
    TextView txtSubtotal;
    @BindView(R.id.txt_discount)
    TextView txtDiscount;
    @BindView(R.id.txt_delivery)
    TextView txtDelivery;
    @BindView(R.id.txt_total)
    TextView txtTotal;
    @BindView(R.id.txt_atype)
    TextView txtAtype;
    @BindView(R.id.txt_address)
    TextView txtAddress;
    @BindView(R.id.img_aupdate)
    ImageView imgAupdate;
    @BindView(R.id.txt_count)
    TextView txtCount;
    @BindView(R.id.txt_totalf)
    TextView txtTotalf;
    @BindView(R.id.txt_proced)
    TextView txtProced;
    @BindView(R.id.txt_wallet)
    TextView txtWallet;
    @BindView(R.id.lvl_cart)
    LinearLayout lvlCart;
    @BindView(R.id.lvl_subcription)
    LinearLayout lvlSubcription;
    @BindView(R.id.lvl_wallet)
    CardView lvlWallet;
    @BindView(R.id.img_wallet)
    ImageView imgWallet;
    @BindView(R.id.ed_note)
    EditText edNote;
    MyDatabase myDatabase;
    List<ProductdataItem> cartData = new ArrayList<>();
    List<ProductdataItem> cartDataS = new ArrayList<>();
    SessionManager sessionManager;
    public static CartActivity cartActivity;
    CustPrograssbar custPrograssbar;
    User user;
    List<PaymentItem> paymentList = new ArrayList<>();
    boolean isWallet = false;
    double dcharge = 0.0;
    AddressListItem addressItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ButterKnife.bind(this);
        cartActivity = this;
        sessionManager = new SessionManager(CartActivity.this);
        myDatabase = new MyDatabase(CartActivity.this);
        custPrograssbar = new CustPrograssbar();
        cartData = myDatabase.getCData();
        cartDataS = myDatabase.getCDataS();

        if (cartData.size() == 0) {
            lvlCart.setVisibility(View.GONE);
        }

        if (cartDataS.size() == 0) {
            lvlSubcription.setVisibility(View.GONE);
            isSubcrib = false;
        } else {
            isSubcrib = true;
        }

        user = sessionManager.getUserDetails("");
        sessionManager.setIntData(coupon, 0);
        LinearLayoutManager layoutManager = new LinearLayoutManager(CartActivity.this, LinearLayoutManager.VERTICAL, false);
        recycleReguler.setLayoutManager(layoutManager);
        recycleReguler.setAdapter(new CartRegulerAdapter(CartActivity.this, cartData));

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(CartActivity.this, LinearLayoutManager.VERTICAL, false);
        recycleSubcription.setLayoutManager(layoutManager1);
        recycleSubcription.setAdapter(new CartSubcriptionAdapter(CartActivity.this, cartDataS));

        curruntday();
        getAddressList();
    }

    public static CartActivity getInstance() {
        return cartActivity;
    }

    double total = 0;
    double subtotal = 0;
    double tempWallet = 0;
    boolean isSubcrib = false;

    public void updatecard() {
        if (cartData.size() == 0) {
            lvlCart.setVisibility(View.GONE);
        }
        if (cartDataS.size() == 0) {
            lvlSubcription.setVisibility(View.GONE);
            isSubcrib = false;
        } else {
            isSubcrib = true;
        }
        subtotal = 0;
        for (int i = 0; i < cartData.size(); i++) {
            ProductdataItem item = cartData.get(i);
            double temp;
            if (Double.parseDouble(item.getProductDiscount()) != 0) {
                Double price = Double.parseDouble(item.getProductRegularprice()) * Double.parseDouble(item.getProductDiscount()) / 100;
                price = Double.parseDouble(item.getProductRegularprice()) - price;
                temp = price * Integer.parseInt(item.getProductQty());
                subtotal = subtotal + temp;
            } else {
                temp = Double.parseDouble(item.getProductRegularprice()) * Integer.parseInt(item.getProductQty());
                subtotal = subtotal + temp;
            }

        }
        for (int i = 0; i < cartDataS.size(); i++) {
            ProductdataItem item = cartDataS.get(i);

            double temp = Double.parseDouble(item.getProductSubscribeprice()) * Integer.parseInt(item.getProductQty());
            temp = temp * Integer.parseInt(item.getTdeliverydigit());
            subtotal = subtotal + temp;
        }
        if (cartData.size() == 0 && cartDataS.size() == 0) {
            finish();
        }
        if (sessionManager.getIntData(coupon) != 0) {
            txtCapply.setText("Remove");
        } else {
            txtCapply.setText("Apply");
        }
        total = subtotal - sessionManager.getIntData(coupon);
        total = total + dcharge;

        if (sessionManager.getIntData(wallet) != 0) {
            lvlWallet.setVisibility(View.VISIBLE);
            if (isWallet) {
                if (sessionManager.getIntData(SessionManager.wallet) <= total) {

                    total = total - sessionManager.getIntData(SessionManager.wallet);
                    txtWallet.setText(sessionManager.getStringData(SessionManager.currency) + "0");
                    tempWallet = sessionManager.getIntData(SessionManager.wallet);
                } else {
                    tempWallet = sessionManager.getIntData(SessionManager.wallet) - total;
                    txtWallet.setText(sessionManager.getStringData(SessionManager.currency) + tempWallet);
                    tempWallet = total;
                    total = 0;

                }
            } else {
                txtWallet.setText(sessionManager.getStringData(SessionManager.currency) + sessionManager.getIntData(SessionManager.wallet));
            }
        } else {
            lvlWallet.setVisibility(View.GONE);
        }


        txtDiscount.setText(sessionManager.getStringData(currency) + sessionManager.getIntData(coupon));
        txtSubtotal.setText(sessionManager.getStringData(currency) + subtotal);
        txtDelivery.setText(sessionManager.getStringData(currency) + dcharge);
        txtTotal.setText(sessionManager.getStringData(currency) + total);
        txtTotalf.setText(sessionManager.getStringData(currency) + total);

    }

    @OnClick({R.id.img_back, R.id.img_rdate, R.id.img_rtime, R.id.txt_capply, R.id.img_aupdate, R.id.txt_proced, R.id.img_wallet})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_rdate:
                openCeleder();
                break;
            case R.id.img_rtime:
                getTimeSlot();
                break;
            case R.id.img_aupdate:
                startActivity(new Intent(CartActivity.this, AddressActivity.class));

                break;
            case R.id.txt_proced:
                if (isSubcrib) {
                    List<PaymentItem> arrayList = new ArrayList<>();
                    for (int i = 0; i < paymentList.size(); i++) {
                        if (paymentList.get(i).getpShow().equalsIgnoreCase("1")) {
                            arrayList.add(paymentList.get(i));
                        }
                    }
                    paymentList = arrayList;
                } else {
                    if (txtArtime.getText().length() == 0) {
                        Toast.makeText(CartActivity.this, "Select time slot", Toast.LENGTH_LONG).show();
                        getTimeSlot();
                        return;
                    }
                }
                if (total == 0) {
                    paymentId = "5";
                    new AsyncTaskRunner().execute("");

                } else {
                    bottonPaymentList();
                }


                break;
            case R.id.img_wallet:
                if (isWallet) {
                    isWallet = false;
                    imgWallet.setImageResource(R.drawable.ic_inactive_toggle);

                } else {
                    imgWallet.setImageResource(R.drawable.ic_active_toggle);
                    isWallet = true;
                }
                updatecard();

                break;
            case R.id.txt_capply:
                if (sessionManager.getIntData(coupon) != 0) {
                    txtCapply.setText("Apply");
                    sessionManager.setIntData(coupon, 0);
                    updatecard();
                } else {
                    int temtoal = (int) Math.round(total);
                    startActivity(new Intent(CartActivity.this, CoupunActivity.class)
                            .putExtra("amount", temtoal));
                }

                break;

            default:
                break;
        }
    }


    private class AsyncTaskRunner extends AsyncTask<String, String, JSONArray> {
        @Override
        protected JSONArray doInBackground(String... params) {
            JSONArray jsonArray = new JSONArray();
            if (cartData.size() == 0) {
                cartData = new ArrayList<>();
                cartData = cartDataS;
            }
            for (int i = 0; i < cartData.size(); i++) {
                ProductdataItem item = cartData.get(i);
                JSONObject jsonObject = new JSONObject();
                try {

                    jsonObject.put("title", item.getProductTitle());
                    jsonObject.put("variation", item.getProductVariation());
                    jsonObject.put("price", item.getProductRegularprice());
                    jsonObject.put("sprice", item.getProductSubscribeprice());
                    jsonObject.put("qty", item.getProductQty());
                    jsonObject.put("discount", item.getProductDiscount());
                    jsonObject.put("image", item.getProductImg());
                    jsonObject.put("select_days", item.getDay());
                    jsonObject.put("total_deliveries", item.getTdeliverydigit());
                    jsonObject.put("startdate", item.getSdate());
                    jsonObject.put("tslot", item.getStime());
                    jsonArray.put(jsonObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return jsonArray;
        }


        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            // execution of result of Long time consuming operation
            orderPlace(jsonArray);
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(String... text) {

        }
    }

    public void orderPlace(JSONArray jsonArray) {
        custPrograssbar.prograssCreate(CartActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", user.getId());
            jsonObject.put("mobile", addressItem.getMobile());
            jsonObject.put("name", addressItem.getName());
            jsonObject.put("a_note", edNote.getText().toString());
            jsonObject.put("product_subtotal", subtotal);
            jsonObject.put("product_total", total);
            jsonObject.put("transaction_id", tragectionID);
            jsonObject.put("ndate", txtArdate.getText().toString().replaceAll("Arriving by ", ""));
            jsonObject.put("wall_amt", tempWallet);
            jsonObject.put("cou_id", sessionManager.getIntData(couponid));
            jsonObject.put("cou_amt", sessionManager.getIntData(coupon));
            jsonObject.put("d_charge", dcharge);
            jsonObject.put("full_address", addressItem.getHno() + "," + addressItem.getLandmark() + "," + addressItem.getAddress());
            jsonObject.put("landmark", addressItem.getLandmark());
            jsonObject.put("p_method_id", paymentId);
            if (isSubcrib) {
                jsonObject.put("type", "Subscribe");
            } else {
                jsonObject.put("type", "Normal");
                jsonObject.put("tslot", txtArtime.getText().toString());


            }

            jsonObject.put("ProductData", jsonArray);
            RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
            Call<JsonObject> call = APIClient.getInterface().getOrderNow(bodyRequest);
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "3");
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    private void getPayment() {
        custPrograssbar.prograssCreate(CartActivity.this);

        JSONObject jsonObject = new JSONObject();
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().getPaymentList(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "2");

    }

    public void bottonPaymentList() {
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.custome_payment, null);
        LinearLayout listView = sheetView.findViewById(R.id.lvl_list);
        TextView txtTotal = sheetView.findViewById(R.id.txt_total);
        txtTotal.setText("item total " + sessionManager.getStringData(currency) + total);
        for (int i = 0; i < paymentList.size(); i++) {
            LayoutInflater inflater = LayoutInflater.from(CartActivity.this);
            PaymentItem paymentItem = paymentList.get(i);
            View view = inflater.inflate(R.layout.custome_paymentitem, null);
            ImageView imageView = view.findViewById(R.id.img_icon);
            TextView txtTitle = view.findViewById(R.id.txt_title);
            TextView txtSubtitel = view.findViewById(R.id.txt_subtitel);
            txtTitle.setText("" + paymentList.get(i).getmTitle());
            txtSubtitel.setText("" + paymentList.get(i).getSubtitle());
            Glide.with(CartActivity.this).load(APIClient.baseUrl + "/" + paymentList.get(i).getmImg()).thumbnail(Glide.with(CartActivity.this).load(R.drawable.ezgifresize)).into(imageView);
            int finalI = i;
            view.setOnClickListener(v -> {
                paymentId = paymentList.get(finalI).getmId();
                try {
                    switch (paymentList.get(finalI).getmTitle()) {
                        case "Razorpay":
                            int temtoal = (int) Math.round(total);
                            mBottomSheetDialog.cancel();
                            startActivity(new Intent(CartActivity.this, RazerpayActivity.class).putExtra("amount", temtoal).putExtra("detail", paymentItem));
                            break;
                        case "Cash On Delivery":
                            new AsyncTaskRunner().execute("");
                            mBottomSheetDialog.cancel();
                            break;
                        case "Paypal":
                            mBottomSheetDialog.cancel();
                            startActivity(new Intent(CartActivity.this, PaypalActivity.class).putExtra("amount", total).putExtra("detail", paymentItem));
                            break;
                        case "Stripe":
                            mBottomSheetDialog.cancel();
                            startActivity(new Intent(CartActivity.this, StripPaymentActivity.class).putExtra("amount", total).putExtra("detail", paymentItem));
                            break;
                        default:
                            break;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            listView.addView(view);
        }
        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.show();
    }

    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                Address address = gson.fromJson(result.toString(), Address.class);
                if (address.getResult().equalsIgnoreCase("true")) {
                    if (!address.getAddressList().isEmpty() && sessionManager.getIntData(SessionManager.address) <= address.getAddressList().size()) {
                        addressItem = address.getAddressList().get(sessionManager.getIntData(SessionManager.address));
                        txtAtype.setText("" + addressItem.getType());
                        txtAddress.setText("" + addressItem.getHno() + "," + addressItem.getLandmark() + "," + addressItem.getAddress());
                        dcharge = Double.parseDouble(addressItem.getDeliveryCharge());
                        updatecard();
                        getPayment();
                    } else {
                        startActivity(new Intent(CartActivity.this, AddressActivity.class));
                    }

                } else {
                    startActivity(new Intent(CartActivity.this, AddressActivity.class));
                }
            } else if (callNo.equalsIgnoreCase("2")) {
                Gson gson = new Gson();
                Payment payment = gson.fromJson(result.toString(), Payment.class);
                paymentList = new ArrayList<>();
                if (isSubcrib) {
                    for (int i = 0; i < payment.getData().size(); i++) {
                        if (payment.getData().get(i).getpShow().equalsIgnoreCase("1")) {
                            paymentList.add(payment.getData().get(i));
                        }
                    }

                } else {
                    paymentList = payment.getData();
                }

            } else if (callNo.equalsIgnoreCase("3")) {
                Gson gson = new Gson();
                RestResponse restResponse = gson.fromJson(result.toString(), RestResponse.class);
                Toast.makeText(CartActivity.this, restResponse.getResponseMsg(), Toast.LENGTH_LONG).show();
                if (restResponse.getResult().equalsIgnoreCase("true")) {
                    myDatabase.deleteCard();
                    myDatabase.deleteCardS();
                    sessionManager.setIntData(wallet, Integer.parseInt(restResponse.getWallet()));
                    startActivity(new Intent(CartActivity.this, CompleOrderActivity.class));
                    finish();
                }

            } else if (callNo.equalsIgnoreCase("4")) {

                Gson gson = new Gson();
                Times times = gson.fromJson(result.toString(), Times.class);
                if (times.getResult().equalsIgnoreCase("true")) {
                    openTime(times.getData());
                }

            }


        } catch (Exception e) {
            e.toString();
        }
    }

    private void getTimeSlot() {
        custPrograssbar.prograssCreate(this);
        JSONObject jsonObject = new JSONObject();
        JsonParser jsonParser = new JsonParser();
        Call<JsonObject> call = APIClient.getInterface().getTimeslot((JsonObject) jsonParser.parse(jsonObject.toString()));
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "4");
        txtProced.setText("Place Order");

    }

    public void openCeleder() {

        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.datepiker_layout, null);
        mBottomSheetDialog.setContentView(sheetView);
        DatePicker picker = sheetView.findViewById(R.id.datePicker1);
        TextView txtConfirm = sheetView.findViewById(R.id.txt_confirm);
        TextView txtTital = sheetView.findViewById(R.id.txt_tital);
        txtTital.setText("Change delivery date");
        picker.setMinDate(System.currentTimeMillis());

        mBottomSheetDialog.show();

        txtConfirm.setOnClickListener(v -> {

            int day = picker.getDayOfMonth();
            int month = picker.getMonth() + 1;
            int year = picker.getYear();

            SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy");
            try {
                Date myDate = inFormat.parse(day + "-" + month + "-" + year);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MMM");
                String dayName = simpleDateFormat.format(myDate);
                String mounthName = simpleDateFormat1.format(myDate);

                txtDayname.setText("" + dayName);
                txtRdate.setText(day + "\n" + mounthName);
                txtArdate.setText("Arriving by " + day + "-" + month + "-" + year + "");

            } catch (ParseException e) {
                e.printStackTrace();
            }


            mBottomSheetDialog.cancel();
        });

    }

    public void curruntday() {
        SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, 1);
            String strDate = inFormat.format(calendar.getTime());
            Date myDate = inFormat.parse(strDate);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MMM");
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd");
            String dayName = simpleDateFormat.format(myDate);
            String mounthName = simpleDateFormat1.format(myDate);
            String mounthday = simpleDateFormat2.format(myDate);

            txtDayname.setText("" + dayName);
            txtRdate.setText(mounthday + "\n" + mounthName);
            txtArdate.setText("Arriving by " + strDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void openTime(List<TimeDatum> list) {

        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.timepiker_layout, null);
        mBottomSheetDialog.setContentView(sheetView);

        TextView txtConfirm = sheetView.findViewById(R.id.txt_confirm);
        RadioGroup rdgTime = sheetView.findViewById(R.id.radiogroup);
        RadioButton rdbtn = null;
        for (int i = 0; i < list.size(); i++) {
            rdbtn = new RadioButton(this);
            rdbtn.setId(View.generateViewId());
            rdbtn.setText(list.get(i).getMintime() + " : " + list.get(i).getMaxtime());

            rdgTime.addView(rdbtn);
        }
        rdgTime.check(rdbtn.getId());
        mBottomSheetDialog.show();

        txtConfirm.setOnClickListener(v -> {

            int selectedId = rdgTime.getCheckedRadioButtonId();
            RadioButton selectTime = rdgTime.findViewById(selectedId);
            txtArtime.setText(selectTime.getText().toString());

            mBottomSheetDialog.cancel();
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (paymentsucsses == 1) {
            paymentsucsses = 0;
            new AsyncTaskRunner().execute("0");

        } else {
            if (myDatabase != null && sessionManager != null) {
                getAddressList();
            }
        }


    }
}