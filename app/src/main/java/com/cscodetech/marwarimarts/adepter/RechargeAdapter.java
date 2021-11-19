package com.cscodetech.marwarimarts.adepter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cscodetech.marwarimarts.R;
import com.cscodetech.marwarimarts.model.DeliverylistItem;

import java.util.List;

public class RechargeAdapter extends RecyclerView.Adapter<RechargeAdapter.MyViewHolder> {
    private Context mContext;
    private List<DeliverylistItem> mCatlist;

    int pposion = 0;


    public interface RecyclerTouchListener {
        public void onClickRechargeAdapterItem(String item, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;


        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.txt_title);


        }
    }

    public RechargeAdapter(Context mContext, List<DeliverylistItem> mCatlist) {
        this.mContext = mContext;
        this.mCatlist = mCatlist;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recharge, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {


        holder.title.setText(mCatlist.get(position).getTitle() + "");

        if (mCatlist.get(position).isSelect()) {
            holder.title.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_recharge));

        } else {
            holder.title.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_city1));


        }

        holder.title.setOnClickListener(v -> {
            DeliverylistItem item = mCatlist.get(pposion);
            item.setSelect(false);
            mCatlist.set(pposion, item);
            mCatlist.get(position).setSelect(!item.isSelect());
            pposion = position;
            notifyDataSetChanged();


        });
    }

    @Override
    public int getItemCount() {
        return mCatlist.size();

    }

    public int getlist() {
        return pposion;
    }

    public void setList(List<DeliverylistItem> list) {
        mCatlist = list;
    }
}