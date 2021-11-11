package com.cscodetech.dailymilk.ui;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cscodetech.dailymilk.R;
import com.cscodetech.dailymilk.adepter.DiscountAdapter;
import com.cscodetech.dailymilk.model.CouponlistItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DiscountActivity extends AppCompatActivity implements DiscountAdapter.RecyclerTouchListener {
    @BindView(R.id.recycler_discount)
    RecyclerView recyclerDiscount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discount);
        ButterKnife.bind(this);
        GridLayoutManager mLayoutManager1 = new GridLayoutManager(this, 2);
        mLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        ArrayList<CouponlistItem> catlistItems=getIntent().getParcelableArrayListExtra("mylist");

        recyclerDiscount.setLayoutManager(mLayoutManager1);
        recyclerDiscount.setAdapter(new DiscountAdapter(DiscountActivity.this,catlistItems, this, "fhsk"));
    }



    @OnClick({R.id.img_back})
    public void onClick(View view) {
        if (view.getId() == R.id.img_back) {
            finish();
        }
    }

    @Override
    public void onClickDiscountItem(String item, int position) {

    }
}