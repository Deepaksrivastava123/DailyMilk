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
import com.cscodetech.dailymilk.model.CouponlistItem;
import com.cscodetech.dailymilk.retrofit.APIClient;

import java.util.List;

public class DiscountAdapter extends RecyclerView.Adapter<DiscountAdapter.MyViewHolder> {
    private Context mContext;
    private List<CouponlistItem> mCatlist;
    private RecyclerTouchListener listener;
    private String typeview;

    public interface RecyclerTouchListener {
        public void onClickDiscountItem(String item, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView txtDate;
        public ImageView thumbnail;
        public LinearLayout lvlclick;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.txt_title);
            txtDate = (TextView) view.findViewById(R.id.txt_date);
            thumbnail = view.findViewById(R.id.imageView);
            lvlclick = view.findViewById(R.id.lvl_itemclick);

        }
    }

    public DiscountAdapter(Context mContext, List<CouponlistItem> mCatlist, final RecyclerTouchListener listener, String viewtype) {
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
                    .inflate(R.layout.item_dicount, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_dicount, parent, false);
        }


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        CouponlistItem category = mCatlist.get(position);
        holder.title.setText(category.getCoupnCode() + "");
        holder.txtDate.setText(category.getCouponExpireDate() + "");
        Glide.with(mContext).load(APIClient.baseUrl + "/" + category.getCouponImg()).into(holder.thumbnail);
        holder.lvlclick.setOnClickListener(v -> listener.onClickDiscountItem("category", position));
    }

    @Override
    public int getItemCount() {
        return mCatlist.size();

    }
}