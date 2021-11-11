package com.cscodetech.dailymilk.adepter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cscodetech.dailymilk.R;
import com.cscodetech.dailymilk.model.CityitemItem;
import com.cscodetech.dailymilk.retrofit.APIClient;

import java.util.ArrayList;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<CityitemItem> mCatlist;
    private RecyclerTouchListener listener;

    int pposion = 0;

    public interface RecyclerTouchListener {
        public void onClickCityItem(String titel, String position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView thumbnail;
        public LinearLayout lvlclick;
        public LinearLayout imgSelect;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.txt_title);
            thumbnail = view.findViewById(R.id.imageView);
            imgSelect = view.findViewById(R.id.img_select);
            lvlclick = view.findViewById(R.id.lvl_itemclick);
        }
    }

    public CityAdapter(Context mContext, ArrayList<CityitemItem> mCatlist, final RecyclerTouchListener listener) {
        this.mContext = mContext;
        this.mCatlist = mCatlist;
        this.listener = listener;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_city, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {


        CityitemItem city = mCatlist.get(position);
        if (city.isSelect()) {
            holder.imgSelect.setVisibility(View.VISIBLE);
        } else {
            holder.imgSelect.setVisibility(View.GONE);
        }
        StringBuilder sb = new StringBuilder(city.getTitle());
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));

        holder.title.setText("" + sb);
        Glide.with(mContext).load(APIClient.baseUrl + "/" + city.getCimg()).thumbnail(Glide.with(mContext).load(R.drawable.ezgifresize)).into(holder.thumbnail);

        holder.lvlclick.setOnClickListener(v -> {
            if(Integer.parseInt(city.getTotalProduct())==0){
                Toast.makeText(mContext,"We don't deliver here yet",Toast.LENGTH_LONG).show();
            }else {
                CityitemItem city1 = mCatlist.get(pposion);
                city1.setSelect(false);
                mCatlist.set(pposion, city1);
                mCatlist.get(position).setSelect(!city.isSelect());
                pposion = position;
                notifyDataSetChanged();
                listener.onClickCityItem(city.getId(), city.getTitle());
            }

        });
    }

    @Override
    public int getItemCount() {

        return mCatlist.size();
    }

    @Override
    public void onViewRecycled(@NonNull MyViewHolder holder) {
        final int position = holder.getAdapterPosition();
        Log.e("Postion", "-->" + position);
        super.onViewRecycled(holder);

    }
}