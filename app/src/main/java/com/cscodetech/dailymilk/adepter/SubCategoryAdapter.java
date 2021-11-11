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
import com.cscodetech.dailymilk.model.Subcatproduct;
import com.cscodetech.dailymilk.retrofit.APIClient;

import java.util.List;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.MyViewHolder> {
    private Context mContext;
    private List<Subcatproduct> mCatlist;
    private RecyclerTouchListener listener;


    public interface RecyclerTouchListener {
        public void onClickSubCategoryItem(String item, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView thumbnail;
        public LinearLayout lvlclick;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.txt_title);
            thumbnail = view.findViewById(R.id.imageView);
            lvlclick = view.findViewById(R.id.lvl_itemclick);

        }
    }

    public SubCategoryAdapter(Context mContext, List<Subcatproduct> mCatlist, final RecyclerTouchListener listener) {
        this.mContext = mContext;
        this.mCatlist = mCatlist;
        this.listener = listener;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;


        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_subcategory, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        Subcatproduct subcatproduct = mCatlist.get(position);
        holder.title.setText(subcatproduct.getTitle() + "");
        Glide.with(mContext).load(APIClient.baseUrl + "/" + subcatproduct.getImage()).into(holder.thumbnail);
        holder.lvlclick.setOnClickListener(v -> listener.onClickSubCategoryItem("category", position));
    }

    @Override
    public int getItemCount() {
        return mCatlist.size();

    }
}