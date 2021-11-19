package com.cscodetech.marwarimarts.adepter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cscodetech.marwarimarts.R;
import com.cscodetech.marwarimarts.model.OrderHistoryItem;
import com.cscodetech.marwarimarts.utility.SessionManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MysucriptionAdapter extends RecyclerView.Adapter<MysucriptionAdapter.MyViewHolder> {

    private List<OrderHistoryItem> mCatlist;
    private RecyclerTouchListener listener;
    SessionManager sessionManager;

    public interface RecyclerTouchListener {
        public void onClickOrderItem(OrderHistoryItem item, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.lvl_itemclick)
        LinearLayout lvlItemclick;
        @BindView(R.id.txt_orderid)
        TextView txtOrderid;
        @BindView(R.id.txt_dname)
        TextView txtDname;
        @BindView(R.id.txt_date)
        TextView txtDate;
        @BindView(R.id.txt_total)
        TextView txtTotal;
        @BindView(R.id.txt_status)
        TextView txtStatus;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);


        }
    }

    public MysucriptionAdapter(Context mContext, List<OrderHistoryItem> mCatlist, final RecyclerTouchListener listener) {

        this.mCatlist = mCatlist;
        this.listener = listener;
        sessionManager = new SessionManager(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sub_orderhistry, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        OrderHistoryItem item = mCatlist.get(position);
        holder.txtOrderid.setText("OrderID " + item.getId());
        holder.txtStatus.setText(item.getStatus() + "");
        holder.txtDname.setText(item.getDeliveryName() + "");
        holder.txtDate.setText(item.getDate() + "");
        holder.txtTotal.setText(sessionManager.getStringData(SessionManager.currency) + item.getTotal());
        holder.lvlItemclick.setOnClickListener(v -> listener.onClickOrderItem(item, position));
    }

    @Override
    public int getItemCount() {
        return mCatlist.size();

    }
}