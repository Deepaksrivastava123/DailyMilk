package com.cscodetech.marwarimarts.adepter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cscodetech.marwarimarts.R;
import com.cscodetech.marwarimarts.model.CollectionItem;
import com.cscodetech.marwarimarts.retrofit.APIClient;

import java.util.ArrayList;

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<CollectionItem> mCatlist;
    private RecyclerTouchListener listener;
    private String typeview;

    public interface RecyclerTouchListener {
        public void onClickCollectionItem(CollectionItem item, int position);
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

    public CollectionAdapter(Context mContext, ArrayList<CollectionItem> mCatlist, final RecyclerTouchListener listener, String viewtype) {
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
                    .inflate(R.layout.item_collecton_viewall, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_collecton, parent, false);
        }


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        CollectionItem category = mCatlist.get(position);
        holder.title.setText(category.getTitle() + "");
        Glide.with(mContext).load(APIClient.baseUrl + "/" + category.getImage()).into(holder.thumbnail);
        holder.lvlclick.setOnClickListener(v -> listener.onClickCollectionItem(category, position));
    }

    @Override
    public int getItemCount() {
        return mCatlist.size();

    }
}