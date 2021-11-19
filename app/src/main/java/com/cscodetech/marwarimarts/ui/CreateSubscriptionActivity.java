package com.cscodetech.marwarimarts.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cscodetech.marwarimarts.R;
import com.cscodetech.marwarimarts.adepter.DaysAdapter;
import com.cscodetech.marwarimarts.adepter.RechargeAdapter;
import com.cscodetech.marwarimarts.model.Days;
import com.cscodetech.marwarimarts.model.Delivery;
import com.cscodetech.marwarimarts.model.DeliverylistItem;
import com.cscodetech.marwarimarts.model.ProductdataItem;
import com.cscodetech.marwarimarts.model.TimeDatum;
import com.cscodetech.marwarimarts.model.Times;
import com.cscodetech.marwarimarts.model.User;
import com.cscodetech.marwarimarts.retrofit.APIClient;
import com.cscodetech.marwarimarts.retrofit.GetResult;
import com.cscodetech.marwarimarts.utility.CustPrograssbar;
import com.cscodetech.marwarimarts.utility.MyDatabase;
import com.cscodetech.marwarimarts.utility.SessionManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

import static com.cscodetech.marwarimarts.utility.SessionManager.login;

public class CreateSubscriptionActivity extends AppCompatActivity implements DaysAdapter.RecyclerTouchListener, GetResult.MyListener {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.img_product)
    ImageView imgProduct;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.txt_pack)
    TextView txtPack;
    @BindView(R.id.txt_price)
    TextView txtPrice;
    @BindView(R.id.txt_priced)
    TextView txtPriced;
    @BindView(R.id.txt_recharge)
    TextView txtRecharge;
    @BindView(R.id.lvl_addcart)
    LinearLayout lvlAddcart;
    @BindView(R.id.lvl_recharge)
    LinearLayout lvlRecharge;
    @BindView(R.id.lvl_startdate)
    LinearLayout lvlStartdate;
    @BindView(R.id.lvl_selecttime)
    LinearLayout lvlSelecttime;
    @BindView(R.id.txt_sdate)
    TextView txtSdate;
    @BindView(R.id.txt_stime)
    TextView txtStime;
    @BindView(R.id.recycle_day)
    RecyclerView recycleDay;
    List<Days> daylist = new ArrayList<>();
    DaysAdapter adapter;
    ProductdataItem item;
    SessionManager sessionManager;
    MyDatabase myDatabase;
    CustPrograssbar custPrograssbar;
    User user;
    List<DeliverylistItem> deliverylist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_subscription);
        ButterKnife.bind(this);
        myDatabase = new MyDatabase(this);
        custPrograssbar = new CustPrograssbar();
        GridLayoutManager mLayoutManager3 = new GridLayoutManager(this, 1);
        mLayoutManager3.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycleDay.setLayoutManager(mLayoutManager3);
        item = getIntent().getParcelableExtra("myclass");

        sessionManager = new SessionManager(this);
        user = sessionManager.getUserDetails("");
        daylist.add(new Days(false, "M"));
        daylist.add(new Days(false, "T"));
        daylist.add(new Days(false, "W"));
        daylist.add(new Days(false, "T"));
        daylist.add(new Days(false, "F"));
        daylist.add(new Days(false, "S"));
        daylist.add(new Days(false, "S"));

        if (item.getDay() != null && !item.getDay().isEmpty()) {
            List<String> items = Arrays.asList(item.getDay().split("\\s*,\\s*"));
            for (int i = 0; i < items.size(); i++) {
                switch (items.get(i)) {

                    case "0":
                        daylist.set(0, new Days(true, "M"));
                        break;
                    case "1":
                        daylist.set(1, new Days(true, "T"));
                        break;
                    case "2":
                        daylist.set(2, new Days(true, "W"));
                        break;
                    case "3":
                        daylist.set(3, new Days(true, "T"));
                        break;
                    case "4":
                        daylist.set(4, new Days(true, "F"));
                        break;
                    case "5":
                        daylist.set(5, new Days(true, "S"));
                        break;
                    case "6":
                        daylist.set(6, new Days(true, "S"));
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + items.get(i));
                }
            }
        } else {
            daylist.set(0, new Days(true, "M"));
            daylist.set(1, new Days(true, "T"));
            daylist.set(2, new Days(true, "W"));
            daylist.set(3, new Days(true, "T"));
            daylist.set(4, new Days(true, "F"));
            daylist.set(5, new Days(true, "S"));
            daylist.set(6, new Days(true, "S"));

            StringWriter sw = new StringWriter();
            for (int i = 0; i < daylist.size(); i++) {
                if (daylist.get(i).isSelect()) {
                    String a = String.valueOf(i);
                    sw.append(a + ",");
                }
            }
            days = sw.toString();
            if (days.length() > 0) {
                days = days.substring(0, days.length() - 1);
                item.setDay(days);
            }

        }
        adapter = new DaysAdapter(CreateSubscriptionActivity.this, daylist, false);
        recycleDay.setAdapter(adapter);

        addsubcribData(lvlAddcart, item, this);
        Glide.with(this).load(APIClient.baseUrl + "/" + item.getProductImg()).thumbnail(Glide.with(this).load(R.drawable.ezgifresize)).centerCrop().into(imgProduct);
        txtTitle.setText("" + item.getProductTitle());
        txtPack.setText("" + item.getProductVariation());
        txtPrice.setText(sessionManager.getStringData(SessionManager.currency) + item.getProductSubscribeprice());
        if (item.getTdelivery() != null)
            txtRecharge.setText("" + item.getTdelivery());
        if (item.getSdate() != null)
            txtSdate.setText("Starting on " + item.getSdate());
        if (item.getStime() != null)
            txtStime.setText("" + item.getStime());
        getDeliveryboy();

    }

    @OnClick({R.id.img_back, R.id.lvl_days, R.id.lvl_recharge, R.id.lvl_startdate, R.id.txt_subsubmit, R.id.lvl_selecttime})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.lvl_days:
                selectDays();
                break;
            case R.id.lvl_selecttime:
                getTimeSlot();
                break;
            case R.id.lvl_recharge:
                txtRecharge.setError(null);
                selectRecharge(deliverylist);
                break;
            case R.id.lvl_startdate:
                txtSdate.setError(null);
                openCeleder();
                break;
            case R.id.txt_subsubmit:
                if (sessionManager.getBooleanData(login)) {
                    if (item.getProductQty() == null || Integer.parseInt(item.getProductQty()) <= 0) {
                        Toast.makeText(this, "Add Qty", Toast.LENGTH_LONG).show();

                        break;
                    } else if (item.getTdeliverydigit() == null) {
                        txtRecharge.setError("Selet");
                        break;

                    } else if (item.getSdate() == null) {
                        txtSdate.setError("Selet");
                        break;

                    } else {
                        if (myDatabase.insertSubcription(item)) {
                            Toast.makeText(this, "Successfully subscribe product", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(this, CartActivity.class));
                            finish();
                        }
                    }
                } else {
                    startActivity(new Intent(this, LoginActivity.class));
                }
                break;


            default:
                break;
        }
    }

    @Override
    public void onClickDaysItem(String item, int position) {


    }

    public void selectRecharge(List<DeliverylistItem> daysList) {
        RechargeAdapter rechargeAdapter;
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.recharge_layout, null);
        mBottomSheetDialog.setContentView(sheetView);
        RecyclerView recycleDay = sheetView.findViewById(R.id.recycle_day);
        TextView txtConfirm = sheetView.findViewById(R.id.txt_confirm);

        GridLayoutManager mLayoutManager3 = new GridLayoutManager(this, 1);
        mLayoutManager3.setOrientation(LinearLayoutManager.VERTICAL);
        recycleDay.setLayoutManager(mLayoutManager3);

        rechargeAdapter = new RechargeAdapter(CreateSubscriptionActivity.this, daysList);
        recycleDay.setAdapter(rechargeAdapter);

        mBottomSheetDialog.show();

        txtConfirm.setOnClickListener(v -> {

            txtRecharge.setText(daysList.get(rechargeAdapter.getlist()).getTitle());
            item.setTdelivery(txtRecharge.getText().toString());
            item.setTdeliverydigit(daysList.get(rechargeAdapter.getlist()).getDeDigit());

            mBottomSheetDialog.cancel();
        });

    }

    public void openCeleder() {

        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.datepiker_layout, null);
        mBottomSheetDialog.setContentView(sheetView);
        DatePicker picker = sheetView.findViewById(R.id.datePicker1);
        TextView txtConfirm = sheetView.findViewById(R.id.txt_confirm);
        picker.setMinDate(System.currentTimeMillis());

        mBottomSheetDialog.show();

        txtConfirm.setOnClickListener(v -> {
            int day = picker.getDayOfMonth();
            int month = picker.getMonth() + 1;
            int year = picker.getYear();

            txtSdate.setText("" + day + "-" + month + "-" + year + "");
            item.setSdate("" + txtSdate.getText().toString());

            mBottomSheetDialog.cancel();
        });

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
            txtStime.setText(selectTime.getText().toString());
            item.setStime("" + txtStime.getText().toString());

            mBottomSheetDialog.cancel();
        });

    }


    public void selectDays() {
        DaysAdapter daysAdapter = new DaysAdapter(CreateSubscriptionActivity.this, daylist, true);
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.dayselect_layout, null);
        mBottomSheetDialog.setContentView(sheetView);
        RecyclerView recycleDay = sheetView.findViewById(R.id.recycle_day);
        TextView txtConfirm = sheetView.findViewById(R.id.txt_confirm);
        TextView txtDaily = sheetView.findViewById(R.id.txt_daily);
        TextView txtWeekdays = sheetView.findViewById(R.id.txt_weekdays);
        TextView txtWeekend = sheetView.findViewById(R.id.txt_weekend);
        GridLayoutManager mLayoutManager3 = new GridLayoutManager(this, 1);
        mLayoutManager3.setOrientation(LinearLayoutManager.HORIZONTAL);
        recycleDay.setLayoutManager(mLayoutManager3);
        recycleDay.setAdapter(daysAdapter);

        mBottomSheetDialog.show();
        txtDaily.setOnClickListener(v -> {
            daylist = new ArrayList<>();
            daylist.add(new Days(true, "M"));
            daylist.add(new Days(true, "T"));
            daylist.add(new Days(true, "W"));
            daylist.add(new Days(true, "T"));
            daylist.add(new Days(true, "F"));
            daylist.add(new Days(true, "S"));
            daylist.add(new Days(true, "S"));
            daysAdapter.setList(daylist);
            daysAdapter.notifyDataSetChanged();

        });

        txtWeekdays.setOnClickListener(v -> {
            daylist = new ArrayList<>();
            daylist.add(new Days(true, "M"));
            daylist.add(new Days(true, "T"));
            daylist.add(new Days(true, "W"));
            daylist.add(new Days(true, "T"));
            daylist.add(new Days(true, "F"));
            daylist.add(new Days(false, "S"));
            daylist.add(new Days(false, "S"));
            daysAdapter.setList(daylist);
            daysAdapter.notifyDataSetChanged();

        });

        txtWeekend.setOnClickListener(v -> {
            daylist = new ArrayList<>();
            daylist.add(new Days(false, "M"));
            daylist.add(new Days(false, "T"));
            daylist.add(new Days(false, "W"));
            daylist.add(new Days(false, "T"));
            daylist.add(new Days(false, "F"));
            daylist.add(new Days(true, "S"));
            daylist.add(new Days(true, "S"));
            daysAdapter.setList(daylist);
            daysAdapter.notifyDataSetChanged();

        });
        txtConfirm.setOnClickListener(v -> {
            daylist = daysAdapter.getlist();
            adapter.setList(daylist);
            adapter.notifyDataSetChanged();
            StringWriter sw = new StringWriter();
            for (int i = 0; i < daylist.size(); i++) {
                if (daylist.get(i).isSelect()) {
                    String a = String.valueOf(i);
                    sw.append(a + ",");
                }
            }
            days = sw.toString();
            if (days.length() > 0) {
                days = days.substring(0, days.length() - 1);
                item.setDay(days);
            }

            mBottomSheetDialog.cancel();
        });

    }

    String days = "0,1,2,3,4,5,6";

    private void getDeliveryboy() {
        custPrograssbar.prograssCreate(CreateSubscriptionActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("uid", user.getId());

        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody bodyRequest = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
        Call<JsonObject> call = APIClient.getInterface().deliverylist(bodyRequest);
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "1");

    }

    private void getTimeSlot() {
        custPrograssbar.prograssCreate(this);
        JSONObject jsonObject = new JSONObject();
        JsonParser jsonParser = new JsonParser();
        Call<JsonObject> call = APIClient.getInterface().getTimeslot((JsonObject) jsonParser.parse(jsonObject.toString()));
        GetResult getResult = new GetResult();
        getResult.setMyListener(this);
        getResult.callForLogin(call, "4");
    }


    @Override
    public void callback(JsonObject result, String callNo) {
        try {
            custPrograssbar.closePrograssBar();
            if (callNo.equalsIgnoreCase("1")) {
                Gson gson = new Gson();
                Delivery delivery = gson.fromJson(result.toString(), Delivery.class);
                if (delivery.getResult().equalsIgnoreCase("true")) {
                    deliverylist = delivery.getDeliverylist();
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

    public void addsubcribData(LinearLayout lnrView, ProductdataItem datum, Context context) {
        lnrView.removeAllViews();
        final int[] count = {0};

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custome_additem, null);
        TextView txtcount = view.findViewById(R.id.txtcount);
        LinearLayout lvlAddremove = view.findViewById(R.id.lvl_addremove);
        LinearLayout lvlAddcart = view.findViewById(R.id.lvl_addcart);
        LinearLayout imgmins = view.findViewById(R.id.img_mins);
        LinearLayout imgplus = view.findViewById(R.id.img_plus);
        if (myDatabase == null) {
            myDatabase = new MyDatabase(context);
        }
        int qrt = myDatabase.getCardS(datum.getProductId());
        if (qrt != -1) {
            count[0] = qrt;
            txtcount.setText("" + count[0]);
            lvlAddremove.setVisibility(View.VISIBLE);
            lvlAddcart.setVisibility(View.GONE);
        } else {
            lvlAddremove.setVisibility(View.GONE);
            lvlAddcart.setVisibility(View.VISIBLE);

        }
        imgmins.setOnClickListener(v -> {

            count[0] = Integer.parseInt(txtcount.getText().toString());

            count[0] = count[0] - 1;
            if (count[0] <= 0) {
                lvlAddremove.setVisibility(View.GONE);
                lvlAddcart.setVisibility(View.VISIBLE);
                txtcount.setText("0");
                item.setProductQty("0");
            } else {
                txtcount.setVisibility(View.VISIBLE);
                txtcount.setText("" + count[0]);
                datum.setProductQty(String.valueOf(count[0]));
                item.setProductQty("" + count[0]);

            }


        });

        imgplus.setOnClickListener(v -> {
            count[0] = Integer.parseInt(txtcount.getText().toString());
            count[0] = count[0] + 1;
            datum.setProductQty(String.valueOf(count[0]));

            txtcount.setText("" + count[0]);
            item.setProductQty("" + count[0]);


        });
        lvlAddcart.setOnClickListener(v -> {
            count[0] = Integer.parseInt(txtcount.getText().toString());
            count[0] = count[0] + 1;
            datum.setProductQty(String.valueOf(count[0]));

            lvlAddcart.setVisibility(View.GONE);
            lvlAddremove.setVisibility(View.VISIBLE);
            txtcount.setText("" + count[0]);
            item.setProductQty("" + count[0]);


        });
        lnrView.addView(view);

    }
}