package com.cscodetech.marwarimarts.utility;

import static com.cscodetech.marwarimarts.utility.SessionManager.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cscodetech.marwarimarts.R;
import com.cscodetech.marwarimarts.model.ProductdataItem;
import com.cscodetech.marwarimarts.retrofit.APIClient;
import com.cscodetech.marwarimarts.ui.CartActivity;
import com.cscodetech.marwarimarts.ui.CreateSubscriptionActivity;
import com.cscodetech.marwarimarts.ui.LoginActivity;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.skyhope.showmoretextview.ShowMoreTextView;

public class ShowProductDetails extends ProductDetail{

    @Override
    public void bottonAddtoCard(Context context, ProductdataItem item) {
        sessionManager = new SessionManager(context);
        myDatabase = new MyDatabase(context);

        Activity activity = (Activity) context;
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(context);
        mBottomSheetDialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
        View rootView = activity.getLayoutInflater().inflate(R.layout.addcard_layout, null);
        mBottomSheetDialog.setContentView(rootView);

        ImageView imgProduct = rootView.findViewById(R.id.img_product);
        TextView txtCategory = rootView.findViewById(R.id.txt_category);
        TextView txtDiscount = rootView.findViewById(R.id.txt_discount);
        LinearLayout linearLayoutCart = rootView.findViewById(R.id.linear_layout_cart);
        TextView txtTitle = rootView.findViewById(R.id.txt_title);
        ShowMoreTextView txtPack = rootView.findViewById(R.id.txt_pack);
        txtPack.addShowMoreText("more");
        txtPack.addShowLessText("Less");
        txtPack.setShowMoreColor(Color.RED); // or other color
        txtPack.setShowLessTextColor(Color.RED); // or other color
//        txtPack.setShowingChar(numberOfCharacter);
        //number of line you want to short
        txtPack.setShowingLine(4);
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


        //hinding views
        txtSubscription.setVisibility(View.GONE);
        linearLayoutCart.setVisibility(View.GONE);

        mBottomSheetDialog.show();

    }
}
