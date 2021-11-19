package com.cscodetech.marwarimarts.adepter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cscodetech.marwarimarts.R;
import com.cscodetech.marwarimarts.model.Days;

import java.util.List;

public class DaysAdapter extends RecyclerView.Adapter<DaysAdapter.MyViewHolder> {
    private boolean isclick;
    private Context mContext;
    private List<Days> mCatlist;



    public interface RecyclerTouchListener {
        public void onClickDaysItem(String item, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;


        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.txt_title);


        }
    }

    public DaysAdapter(Context mContext, List<Days> mCatlist, boolean isclick) {
        this.mContext = mContext;
        this.mCatlist = mCatlist;
        this.isclick = isclick;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_days, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {


        holder.title.setText(mCatlist.get(position).getDayname() + "");
        if (mCatlist.get(position).isSelect()) {
            holder.title.setBackground(mContext.getResources().getDrawable(R.drawable.circle1));
            holder.title.setTextColor(mContext.getResources().getColor(R.color.white));
        } else {
            holder.title.setBackground(mContext.getResources().getDrawable(R.drawable.circle2));
            holder.title.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
        }
        holder.title.setOnClickListener(v -> {
            if(isclick){
                if (mCatlist.get(position).isSelect()) {
                    mCatlist.set(position, new Days(false, mCatlist.get(position).getDayname()));
                } else {
                    mCatlist.set(position, new Days(true, mCatlist.get(position).getDayname()));
                }
                notifyDataSetChanged();
            }

        });
    }

    @Override
    public int getItemCount() {
        return mCatlist.size();

    }
    public List<Days> getlist(){
        return mCatlist;
    }
    public void setList(List<Days> list){
        mCatlist=list;
    }
}