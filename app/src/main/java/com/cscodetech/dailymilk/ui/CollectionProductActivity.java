package com.cscodetech.dailymilk.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cscodetech.dailymilk.R;
import com.cscodetech.dailymilk.adepter.ProductChaildAdapter;
import com.cscodetech.dailymilk.model.ProductdataItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CollectionProductActivity extends AppCompatActivity {
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_tital)
    TextView txtTital;
    @BindView(R.id.recycler_collection)
    RecyclerView recyclerCollection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_product);
        ButterKnife.bind(this);

        GridLayoutManager mLayoutManager1 = new GridLayoutManager(this, 1);
        mLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerCollection.setLayoutManager(mLayoutManager1);
        ArrayList<ProductdataItem> productdata=getIntent().getParcelableArrayListExtra("mylist");

        recyclerCollection.setAdapter(new ProductChaildAdapter(this, productdata));
    }

    @OnClick({R.id.img_back})
    public void onClick(View view) {
        if (view.getId() == R.id.img_back) {
            finish();
        }
    }
}