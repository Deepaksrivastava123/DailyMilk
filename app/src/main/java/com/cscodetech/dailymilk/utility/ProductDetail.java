package com.cscodetech.dailymilk.utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cscodetech.dailymilk.R;
import com.cscodetech.dailymilk.model.ProductdataItem;
import com.cscodetech.dailymilk.retrofit.APIClient;
import com.cscodetech.dailymilk.ui.CartActivity;
import com.cscodetech.dailymilk.ui.CreateSubscriptionActivity;
import com.cscodetech.dailymilk.ui.HomeActivity;
import com.cscodetech.dailymilk.ui.LoginActivity;
import com.cscodetech.dailymilk.ui.ProductListActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

import static com.cscodetech.dailymilk.utility.SessionManager.login;

public class ProductDetail {

    MyDatabase myDatabase;
    SessionManager sessionManager;
    TextView txtCount;
    TextView txtTotal;

    public void bottonAddtoCard(Context context, ProductdataItem item) {
        sessionManager = new SessionManager(context);
        myDatabase = new MyDatabase(context);

        Activity activity = (Activity) context;
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(context);

        View rootView = activity.getLayoutInflater().inflate(R.layout.addcard_layout, null);
        mBottomSheetDialog.setContentView(rootView);

        ImageView imgProduct = rootView.findViewById(R.id.img_product);
        TextView txtCategory = rootView.findViewById(R.id.txt_category);
        TextView txtDiscount = rootView.findViewById(R.id.txt_discount);
        TextView txtTitle = rootView.findViewById(R.id.txt_title);
        TextView txtPack = rootView.findViewById(R.id.txt_pack);
        TextView txtPrice = rootView.findViewById(R.id.txt_price);
        TextView txtPriced = rootView.findViewById(R.id.txt_priced);
        TextView txtSubscription = rootView.findViewById(R.id.txt_subscription);
        txtCount = rootView.findViewById(R.id.txt_count);
        txtTotal = rootView.findViewById(R.id.txt_total);
        TextView txtProced = rootView.findViewById(R.id.txt_proced);
        LinearLayout lvlAddcart = rootView.findViewById(R.id.lvl_addcart);

        int qrts = myDatabase.getCardS(item.getProductId());

        if (qrts != -1) {


            txtSubscription.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_subscription_white_rounded_selected, 0, 0, 0);

        } else {
            txtSubscription.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_subscription_white_rounded, 0, 0, 0);

        }


        Glide.with(context).load(APIClient.baseUrl + "/" + item.getProductImg()).thumbnail(Glide.with(context).load(R.drawable.ezgifresize)).centerCrop().into(imgProduct);
        txtCategory.setText("" + item.getCatname());
        txtTitle.setText("" + item.getProductTitle());
        txtPack.setText("" + item.getProductVariation());


        if (Double.parseDouble(item.getProductDiscount()) != 0) {
            Double price = Double.parseDouble(item.getProductRegularprice()) * Double.parseDouble(item.getProductDiscount()) / 100;
            price = Double.parseDouble(item.getProductRegularprice()) - price;
            txtPrice.setText(sessionManager.getStringData(SessionManager.currency) + price);
            txtPriced.setText(sessionManager.getStringData(SessionManager.currency) + item.getProductRegularprice());
            txtPriced.setVisibility(View.VISIBLE);
            txtDiscount.setText(" " + item.getProductDiscount() + "% Discount ");

        } else {
            txtPrice.setText(sessionManager.getStringData(SessionManager.currency) + item.getProductRegularprice());
            txtPriced.setVisibility(View.GONE);
            txtDiscount.setVisibility(View.GONE);
        }

        txtSubscription.setOnClickListener(v -> {
            mBottomSheetDialog.cancel();
            if (qrts != -1) {
                context.startActivity(new Intent(context, CreateSubscriptionActivity.class).putExtra("myclass", myDatabase.getItems(item.getProductId())));
            } else {
                context.startActivity(new Intent(context, CreateSubscriptionActivity.class).putExtra("myclass", item));

            }

        });
        txtProced.setOnClickListener(v -> {

            if (myDatabase.getAllData() != 0) {
                mBottomSheetDialog.cancel();
                if (sessionManager.getBooleanData(login)) {
                    context.startActivity(new Intent(context, CartActivity.class));
                } else {
                    context.startActivity(new Intent(context, LoginActivity.class));
                }

            } else {
                Toast.makeText(context, "Oops ! Your cart is empty !", Toast.LENGTH_LONG).show();
            }


        });
        addCardData(lvlAddcart, item, context);

        mBottomSheetDialog.show();


    }

    private void addCardData(LinearLayout lnrView, ProductdataItem datum, Context context) {
        lnrView.removeAllViews();
        final int[] count = {0};

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custome_additem, null);
        TextView txtcount = view.findViewById(R.id.txtcount);
        LinearLayout lvlAddremove = view.findViewById(R.id.lvl_addremove);
        LinearLayout lvlAddcart = view.findViewById(R.id.lvl_addcart);
        LinearLayout imgMins = view.findViewById(R.id.img_mins);
        LinearLayout imgPlus = view.findViewById(R.id.img_plus);

        cardData(myDatabase.getCData());


        int qrt = myDatabase.getCard(datum.getProductId());
        if (qrt != -1) {
            count[0] = qrt;
            txtcount.setText("" + count[0]);
            lvlAddremove.setVisibility(View.VISIBLE);
            lvlAddcart.setVisibility(View.GONE);
        } else {
            lvlAddremove.setVisibility(View.GONE);
            lvlAddcart.setVisibility(View.VISIBLE);

        }
        imgMins.setOnClickListener(v -> {

            count[0] = Integer.parseInt(txtcount.getText().toString());

            count[0] = count[0] - 1;
            if (count[0] <= 0) {
                lvlAddremove.setVisibility(View.GONE);
                lvlAddcart.setVisibility(View.VISIBLE);
                txtcount.setText("0");
                myDatabase.deleteRData(datum.getProductId());
            } else {
                txtcount.setVisibility(View.VISIBLE);
                txtcount.setText("" + count[0]);
                datum.setProductQty(String.valueOf(count[0]));
                myDatabase.insertData(datum);
            }
            cardData(myDatabase.getCData());

        });

        imgPlus.setOnClickListener(v -> {
            count[0] = Integer.parseInt(txtcount.getText().toString());
            count[0] = count[0] + 1;
            datum.setProductQty(String.valueOf(count[0]));
            if (myDatabase.insertData(datum)) {
                txtcount.setText("" + count[0]);
                cardData(myDatabase.getCData());

            }


        });
        lvlAddcart.setOnClickListener(v -> {
            count[0] = Integer.parseInt(txtcount.getText().toString());
            count[0] = count[0] + 1;
            datum.setProductQty(String.valueOf(count[0]));
            if (myDatabase.insertData(datum)) {
                lvlAddcart.setVisibility(View.GONE);
                lvlAddremove.setVisibility(View.VISIBLE);
                txtcount.setText("" + count[0]);
                cardData(myDatabase.getCData());
            }

        });
        lnrView.addView(view);

    }

    public void cardData(List<ProductdataItem> list) {
        if (HomeActivity.getInstance() != null) {
            HomeActivity.getInstance().setCountitem("" + myDatabase.getAllData());
        }
        if (!list.isEmpty()) {
            double totle = 0.0;
            int qty = 0;
            for (int i = 0; i < list.size(); i++) {

                double res3 = (Double.parseDouble(list.get(i).getProductRegularprice()) / 100.0f) * Double.parseDouble(list.get(i).getProductDiscount());
                double pp = Double.parseDouble(list.get(i).getProductRegularprice()) - res3;
                pp = pp * Integer.parseInt(list.get(i).getProductQty());
                totle = totle + pp;
                qty = qty + 1;


            }
            txtCount.setText("Item " + qty);
            txtTotal.setText(sessionManager.getStringData(SessionManager.currency) + totle);
            if (ProductListActivity.getInstance() != null) {
                ProductListActivity.getInstance().notifiyChange();
            }


        }else {
            txtCount.setText("Item ");
            txtTotal.setText("");
            if (ProductListActivity.getInstance() != null) {
                ProductListActivity.getInstance().notifiyChange();
            }

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
        LinearLayout imgMins = view.findViewById(R.id.img_mins);
        LinearLayout imgPlus = view.findViewById(R.id.img_plus);
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
        imgMins.setOnClickListener(v -> {

            count[0] = Integer.parseInt(txtcount.getText().toString());

            count[0] = count[0] - 1;
            if (count[0] <= 0) {
                lvlAddremove.setVisibility(View.GONE);
                lvlAddcart.setVisibility(View.VISIBLE);
                txtcount.setText("0");
                myDatabase.deleteRDataS(datum.getProductId());
            } else {
                txtcount.setVisibility(View.VISIBLE);
                txtcount.setText("" + count[0]);
                datum.setProductQty(String.valueOf(count[0]));
                myDatabase.insertSubcription(datum);
            }


        });

        imgPlus.setOnClickListener(v -> {
            count[0] = Integer.parseInt(txtcount.getText().toString());
            count[0] = count[0] + 1;
            datum.setProductQty(String.valueOf(count[0]));
            if (myDatabase.insertSubcription(datum)) {
                txtcount.setText("" + count[0]);

            }


        });
        lvlAddcart.setOnClickListener(v -> {
            count[0] = Integer.parseInt(txtcount.getText().toString());
            count[0] = count[0] + 1;
            datum.setProductQty(String.valueOf(count[0]));
            if (myDatabase.insertSubcription(datum)) {
                lvlAddcart.setVisibility(View.GONE);
                lvlAddremove.setVisibility(View.VISIBLE);
                txtcount.setText("" + count[0]);

            }

        });
        lnrView.addView(view);

    }

    public void bottonCardClear(Context context, String s) {
        if (myDatabase == null) {
            myDatabase = new MyDatabase(context);
        }
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(context);
        Activity activity = (Activity) context;
        View sheetView = activity.getLayoutInflater().inflate(R.layout.crearcard_layout, null);
        mBottomSheetDialog.setContentView(sheetView);

        TextView txtCrear = sheetView.findViewById(R.id.txt_crear);
        TextView txtNo = sheetView.findViewById(R.id.txt_no);
        TextView txtMsg = sheetView.findViewById(R.id.txt_msg);
        if (s.equalsIgnoreCase("sub")) {
            txtMsg.setText("Your cart contains Subscription items. Do you want to clear the cart and add items?");
        } else {
            txtMsg.setText("Your cart contains items. Do you want to clear the cart and add items?");
        }

        mBottomSheetDialog.show();

        txtCrear.setOnClickListener(v -> {
            if (s.equalsIgnoreCase("sub")) {
                myDatabase.deleteCardS();

            } else {
                myDatabase.deleteCard();

            }
            mBottomSheetDialog.cancel();


        });
        txtNo.setOnClickListener(v -> mBottomSheetDialog.cancel());
    }



}
