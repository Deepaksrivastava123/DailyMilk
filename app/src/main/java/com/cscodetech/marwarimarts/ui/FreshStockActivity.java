package com.cscodetech.marwarimarts.ui;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cscodetech.marwarimarts.R;
import com.cscodetech.marwarimarts.adepter.FreashStockAdapter;
import com.cscodetech.marwarimarts.model.ProductdataItem;
import com.cscodetech.marwarimarts.utility.ProductDetail;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FreshStockActivity extends AppCompatActivity implements FreashStockAdapter.RecyclerTouchListener {

    @BindView(R.id.recycler_freshstock)
    RecyclerView recyclerFreshstock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fresh_stock);
        ButterKnife.bind(this);
        ArrayList<ProductdataItem> catlistItems=getIntent().getParcelableArrayListExtra("mylist");
        GridLayoutManager mLayoutManager1 = new GridLayoutManager(this, 3);
        mLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerFreshstock.setLayoutManager(mLayoutManager1);
        recyclerFreshstock.setAdapter(new FreashStockAdapter(FreshStockActivity.this,catlistItems, this, "fhsk"));
    }
    @OnClick({R.id.img_back})
    public void onClick(View view) {
        if (view.getId() == R.id.img_back) {
            finish();
        }
    }

    @Override
    public void onClickFreashStockItem(ProductdataItem item, int position) {
        new ProductDetail().bottonAddtoCard(this,item);

    }
}