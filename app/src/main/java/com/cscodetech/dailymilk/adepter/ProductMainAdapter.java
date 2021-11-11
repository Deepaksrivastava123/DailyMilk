package com.cscodetech.dailymilk.adepter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cscodetech.dailymilk.R;
import com.cscodetech.dailymilk.model.Subcatproduct;

import java.util.List;

public class ProductMainAdapter extends RecyclerView.Adapter<ProductMainAdapter.MyViewHolder> {
    private Context mContext;
    private List<Subcatproduct> mCatlist;


    public interface RecyclerTouchListener {
        public void onClickCategoryItem(String item, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public RecyclerView recyclerView;


        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.txt_title);
            recyclerView = view.findViewById(R.id.recycler_productitem);


        }
    }

    public ProductMainAdapter(Context mContext, List<Subcatproduct> mCatlist) {
        this.mContext = mContext;
        this.mCatlist = mCatlist;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_productmain, parent, false);



        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        setFadeAnimation(holder.itemView);
        GridLayoutManager mLayoutManager1 = new GridLayoutManager(mContext, 1);
        mLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        holder.recyclerView.setLayoutManager(mLayoutManager1);
        holder.recyclerView.setAdapter(new ProductChaildAdapter(mContext, mCatlist.get(position).getProductdata()));
        holder.title.setText(""+mCatlist.get(position).getTitle());



    }

    @Override
    public int getItemCount() {
        return mCatlist.size();

    }

    public void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(500);
        view.startAnimation(anim);
    }

}