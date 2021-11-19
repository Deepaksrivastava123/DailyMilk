package com.cscodetech.marwarimarts.adepter;

import android.content.Context;
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
import com.cscodetech.marwarimarts.utility.MyDatabase;
import com.cscodetech.marwarimarts.utility.SessionManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartRegulerAdapter extends RecyclerView.Adapter<CartRegulerAdapter.MyViewHolder> {
    private Context mContext;
    private List<ProductdataItem> mCatlist;
    SessionManager sessionManager;
    MyDatabase myDatabase;

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
        TextView txtPriceD;
        @BindView(R.id.lvl_cart)
        LinearLayout lvlCart;
        @BindView(R.id.img_remove)
        ImageView imgRemove;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);


        }
    }

    public CartRegulerAdapter(Context mContext, List<ProductdataItem> mCatlist) {
        this.mContext = mContext;
        this.mCatlist = mCatlist;
        sessionManager = new SessionManager(mContext);
        myDatabase = new MyDatabase(mContext);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cartregular, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        ProductdataItem item = mCatlist.get(position);
        holder.txtTitle.setText("" + item.getProductTitle());

        holder.txtPack.setText("" + item.getProductVariation());
        Glide.with(mContext).load(APIClient.baseUrl + "/" + item.getProductImg()).into(holder.imageView);

        if (Double.parseDouble(item.getProductDiscount()) != 0) {
            Double price = Double.parseDouble(item.getProductRegularprice()) * Double.parseDouble(item.getProductDiscount()) / 100;
            price = Double.parseDouble(item.getProductRegularprice()) - price;
            holder.txtPrice.setText(sessionManager.getStringData(SessionManager.currency) + price);
            holder.txtPriceD.setText(sessionManager.getStringData(SessionManager.currency) + item.getProductRegularprice());
            holder.txtPriceD.setVisibility(View.VISIBLE);

        } else {
            holder.txtPrice.setText(sessionManager.getStringData(SessionManager.currency) + item.getProductRegularprice());
            holder.txtPriceD.setVisibility(View.GONE);
        }

        addCardData(holder.lvlCart, item, mContext);


        holder.imgRemove.setOnClickListener(v -> {
            AlertDialog myDelete = new AlertDialog.Builder(mContext)
                    .setTitle("Delete")
                    .setMessage("Do you want to Delete")
                    .setIcon(R.drawable.ic_delete)
                    .setPositiveButton("Delete", (dialog, whichButton) -> {
                        Log.d("sdj", "" + whichButton);
                        dialog.dismiss();
                        myDatabase.deleteRData(item.getProductId());
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
        holder.lvlItemclick.setOnClickListener(v -> {
        });
    }

    @Override
    public int getItemCount() {
        return mCatlist.size();

    }


    private void addCardData(LinearLayout lnrView, ProductdataItem datum, Context context) {
        lnrView.removeAllViews();
        final int[] count = {0};

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custome_additem, null);
        TextView txtcount = view.findViewById(R.id.txtcount);
        LinearLayout lvlAddremove = view.findViewById(R.id.lvl_addremove);
        LinearLayout lvlAddcart = view.findViewById(R.id.lvl_addcart);
        LinearLayout imgMins = view.findViewById(R.id.img_mins);
        LinearLayout imgPlus = view.findViewById(R.id.img_plus);


        int qrt = myDatabase.getCard(datum.getProductId());
        if (qrt != -1) {
            count[0] = qrt;
            txtcount.setText("" + count[0]);
            lvlAddremove.setVisibility(View.VISIBLE);
            lvlAddcart.setVisibility(View.GONE);
        } else {
            lvlAddremove.setVisibility(View.GONE);
            lvlAddcart.setVisibility(View.VISIBLE);

        }
        imgMins.setOnClickListener(v -> {

            count[0] = Integer.parseInt(txtcount.getText().toString());

            count[0] = count[0] - 1;
            if (count[0] <= 0) {

                myDatabase.deleteRData(datum.getProductId());
                mCatlist.remove(datum);
                notifyDataSetChanged();
                CartActivity.getInstance().updatecard();
            } else {
                txtcount.setVisibility(View.VISIBLE);
                txtcount.setText("" + count[0]);
                datum.setProductQty(String.valueOf(count[0]));
                myDatabase.insertData(datum);
            }
            CartActivity.getInstance().updatecard();


        });

        imgPlus.setOnClickListener(v -> {
            count[0] = Integer.parseInt(txtcount.getText().toString());
            count[0] = count[0] + 1;
            datum.setProductQty(String.valueOf(count[0]));
            if (myDatabase.insertData(datum)) {
                txtcount.setText("" + count[0]);
                CartActivity.getInstance().updatecard();


            }


        });
        lvlAddcart.setOnClickListener(v -> {
            count[0] = Integer.parseInt(txtcount.getText().toString());
            count[0] = count[0] + 1;
            datum.setProductQty(String.valueOf(count[0]));
            if (myDatabase.insertData(datum)) {
                lvlAddcart.setVisibility(View.GONE);
                lvlAddremove.setVisibility(View.VISIBLE);
                txtcount.setText("" + count[0]);
                CartActivity.getInstance().updatecard();

            }

        });
        lnrView.addView(view);

    }
}