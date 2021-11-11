package com.cscodetech.dailymilk.adepter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cscodetech.dailymilk.R;
import com.cscodetech.dailymilk.model.ProductdataItem;
import com.cscodetech.dailymilk.retrofit.APIClient;

import java.util.ArrayList;

public class FreashStockAdapter extends RecyclerView.Adapter<FreashStockAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<ProductdataItem> mCatlist;
    private RecyclerTouchListener listener;
    private String typeview;

    public interface RecyclerTouchListener {
        public void onClickFreashStockItem(ProductdataItem item, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView thumbnail;
        public LinearLayout lvlclick;
        public TextView textView_price;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.txt_title);
            thumbnail = view.findViewById(R.id.imageView);
            lvlclick = view.findViewById(R.id.lvl_itemclick);
            textView_price = view.findViewById(R.id.textView_price);

        }
    }

    public FreashStockAdapter(Context mContext, ArrayList<ProductdataItem> mCatlist, final RecyclerTouchListener listener, String viewtype) {
        this.mContext = mContext;
        this.mCatlist = mCatlist;
        this.listener = listener;
        this.typeview = viewtype;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (typeview.equalsIgnoreCase("viewall")) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_freshstock, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_freshstock, parent, false);
        }


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        ProductdataItem category = mCatlist.get(position);
        holder.title.setText(category.getProductTitle()+ "");
        holder.textView_price.setText("₹"+getDiscountPrice(category.getProductRegularprice() , category.getProductDiscount()));
        Glide.with(mContext).load(APIClient.baseUrl + "/" + category.getProductImg()).thumbnail(Glide.with(mContext).load(R.drawable.ezgifresize)).into(holder.thumbnail);
        holder.lvlclick.setOnClickListener(v -> listener.onClickFreashStockItem(category, position));
    }

    public String getDiscountPrice(String regularpirce, String discount)
    {
        String discount_price;
        Double price = Double.parseDouble(regularpirce)* Double.parseDouble(discount) / 100;
        price = Double.parseDouble(regularpirce) - price;
        discount_price = String.valueOf(price);
        return discount_price;
    }

    @Override
    public int getItemCount() {
        return mCatlist.size();

    }
}