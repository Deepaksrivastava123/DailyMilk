package com.cscodetech.marwarimarts.adepter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cscodetech.marwarimarts.R;
import com.cscodetech.marwarimarts.model.ProductdataItem;
import com.cscodetech.marwarimarts.retrofit.APIClient;
import com.cscodetech.marwarimarts.ui.CartActivity;
import com.cscodetech.marwarimarts.ui.CreateSubscriptionActivity;
import com.cscodetech.marwarimarts.utility.MyDatabase;
import com.cscodetech.marwarimarts.utility.SessionManager;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartSubcriptionAdapter extends RecyclerView.Adapter<CartSubcriptionAdapter.MyViewHolder> {
    private Context mContext;
    private List<ProductdataItem> mCatlist;
    MyDatabase myDatabase;
    SessionManager sessionManager;

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
        @BindView(R.id.txt_totalp)
        TextView txtTotalp;
        @BindView(R.id.txt_tdelivary)
        TextView txtTdelivary;
        @BindView(R.id.img_remove)
        ImageView imgRemove;

        @BindView(R.id.txt_date)
        TextView txtSDate;
        @BindView(R.id.txt_time)
        TextView txtTime;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);


        }
    }

    public CartSubcriptionAdapter(Context mContext, List<ProductdataItem> mCatlist) {
        this.mContext = mContext;
        this.mCatlist = mCatlist;
        myDatabase = new MyDatabase(mContext);
        sessionManager = new SessionManager(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cartsubcription, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        ProductdataItem item = mCatlist.get(position);
        holder.txtTitle.setText("" + item.getProductTitle());
        holder.txtPrice.setText(sessionManager.getStringData(SessionManager.currency) + item.getProductSubscribeprice() + " x " + item.getProductQty());
        holder.txtPack.setText("" + item.getProductVariation());
        holder.txtSDate.setText("Starting on " + item.getSdate());
        holder.txtTime.setText("Starting time on " + item.getStime());
        Glide.with(mContext).load(APIClient.baseUrl + "/" + item.getProductImg()).into(holder.imageView);
        double temp = Double.parseDouble(item.getProductSubscribeprice()) * Integer.parseInt(item.getProductQty());
        temp = temp * Integer.parseInt(item.getTdeliverydigit());
        holder.txtTotalp.setText(sessionManager.getStringData(SessionManager.currency) + new DecimalFormat("##.##").format(temp));
        holder.txtTdelivary.setText("" + item.getTdelivery());
        holder.imgRemove.setOnClickListener(v -> {
            AlertDialog myDelete = new AlertDialog.Builder(mContext)
                    .setTitle("Delete")
                    .setMessage("Do you want to Delete")
                    .setIcon(R.drawable.ic_delete)
                    .setPositiveButton("Delete", (dialog, whichButton) -> {
                        Log.d("sdj", "" + whichButton);
                        dialog.dismiss();
                        myDatabase.deleteRDataS(item.getProductId());
                        mCatlist.remove(item);
                        notifyDataSetChanged();
                        CartActivity.getInstance().updatecard();
                    })
                    .setNegativeButton("cancel", (dialog, which) -> {
                        Log.d("sdj", "" + which);
                        dialog.dismiss();
                    })
                    .create();
            myDelete.show();
        });
        holder.txtSDate.setOnClickListener(v -> {
            mContext.startActivity(new Intent(mContext, CreateSubscriptionActivity.class).putExtra("myclass", item));


        });

    }

    @Override
    public int getItemCount() {
        return mCatlist.size();
    }




}