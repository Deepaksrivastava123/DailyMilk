package com.cscodetech.marwarimarts.adepter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cscodetech.marwarimarts.R;
import com.cscodetech.marwarimarts.model.ProductdataItem;
import com.cscodetech.marwarimarts.retrofit.APIClient;
import com.cscodetech.marwarimarts.ui.CreateSubscriptionActivity;
import com.cscodetech.marwarimarts.utility.MyDatabase;
import com.cscodetech.marwarimarts.utility.ProductDetail;
import com.cscodetech.marwarimarts.utility.SessionManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductChaildAdapter extends RecyclerView.Adapter<ProductChaildAdapter.MyViewHolder> {
    private Context mContext;
    private List<ProductdataItem> mCatlist;
    SessionManager sessionManager;
    MyDatabase myDatabase;
    final int[] count = {0};

    public interface RecyclerTouchListener {
        public void onClickCategoryItem(String item, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.lvl_itemclick)
        LinearLayout lvlItemclick;
        @BindView(R.id.imageView)
        ImageView imageView;
        @BindView(R.id.txt_title)
        TextView txtTitle;
        @BindView(R.id.txt_pack)
        TextView txtPack;
        @BindView(R.id.txt_price)
        TextView txtPrice;
        @BindView(R.id.txt_priced)
        TextView txtPriced;
//        @BindView(R.id.txt_subscription)
//        TextView txtSubscription;
        @BindView(R.id.lvl_addcart)
        LinearLayout lvlAddcart;
        @BindView(R.id.lvl_addremove)
        LinearLayout lvlAddRemove;
//        @BindView(R.id.txt_addproduct)
//        TextView txtAddproduct;
        @BindView(R.id.img_mins)
        LinearLayout imgMinus;
        @BindView(R.id.txtcount)
        TextView txtCount;
        @BindView(R.id.img_plus)
        LinearLayout imgPlus;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);


        }
    }

    public ProductChaildAdapter(Context mContext, List<ProductdataItem> mCatlist) {
        this.mContext = mContext;
        this.mCatlist = mCatlist;
        sessionManager = new SessionManager(mContext);
        myDatabase = new MyDatabase(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_productchaild, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        ProductdataItem item = mCatlist.get(position);
        holder.txtTitle.setText("" + item.getProductTitle());
        if (Double.parseDouble(item.getProductDiscount()) != 0) {
            Double price = Double.parseDouble(item.getProductRegularprice()) * Double.parseDouble(item.getProductDiscount()) / 100;
            price = Double.parseDouble(item.getProductRegularprice()) - price;
            holder.txtPrice.setText(sessionManager.getStringData(SessionManager.currency) + price);
            holder.txtPriced.setText(sessionManager.getStringData(SessionManager.currency) + item.getProductRegularprice());
            holder.txtPriced.setVisibility(View.VISIBLE);

        } else {
            holder.txtPrice.setText(sessionManager.getStringData(SessionManager.currency) + item.getProductRegularprice());
            holder.txtPriced.setVisibility(View.GONE);
        }


        holder.txtPack.setText("" + item.getProductVariation());
        Glide.with(mContext).load(APIClient.baseUrl + "/" + item.getProductImg()).into(holder.imageView);
        int qrts = myDatabase.getCardS(item.getProductId());

        if (qrts != -1) {

//            holder.txtSubscription.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_subscription_white_rounded_selected, 0, 0, 0);

        } else {
//            holder.txtSubscription.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_subscription_white_rounded, 0, 0, 0);

        }
        int qrts1 = myDatabase.getCard(item.getProductId());

        if (qrts1 != -1) {
//
//            holder.txtAddproduct.setText("");
//            holder.txtAddproduct.setBackgroundResource(R.drawable.ic_selected_item);

        } else {
//            holder.txtAddproduct.setText("+");
//            holder.txtAddproduct.setBackgroundResource(R.drawable.rounded_sub);


        }

//        holder.txtAddproduct.setOnClickListener(v -> new ProductDetail().bottonAddtoCard(mContext, item));
//        holder.txtSubscription.setOnClickListener(v -> mContext.startActivity(new Intent(mContext, CreateSubscriptionActivity.class).putExtra("myclass", item)));
//        holder.lvlItemclick.setOnClickListener(v -> new ProductDetail().bottonAddtoCard(mContext, item));
        holder.lvlAddcart.setOnClickListener(v -> {
            holder.lvlAddcart.setVisibility(View.GONE);
            holder.lvlAddRemove.setVisibility(View.VISIBLE);
        });

        holder.imgMinus.setOnClickListener(v -> {

            count[0] = Integer.parseInt(holder.txtCount.getText().toString());
            count[0] = count[0] - 1;
            if (count[0] <= 0) {
                holder.lvlAddRemove.setVisibility(View.GONE);
                holder.lvlAddcart.setVisibility(View.VISIBLE);
                holder.txtCount.setText("0");

            } else {
                holder.txtCount.setVisibility(View.VISIBLE);
                holder.txtCount.setText("" + count[0]);
            }
        });

        holder.imgPlus.setOnClickListener(v -> {
            count[0] = Integer.parseInt(holder.txtCount.getText().toString());
            count[0] = count[0] + 1;
            holder.txtCount.setText("" + count[0]);
        });
    }



    @Override
    public int getItemCount() {
        return mCatlist.size();

    }


}