package com.cscodetech.marwarimarts.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cscodetech.marwarimarts.R;
import com.cscodetech.marwarimarts.adepter.CollectionAdapter;
import com.cscodetech.marwarimarts.model.CollectionItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CollectonActivity extends AppCompatActivity implements CollectionAdapter.RecyclerTouchListener {

    @BindView(R.id.recycler_collection)
    RecyclerView recyclerCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collecton);
        ButterKnife.bind(this);

        ArrayList<CollectionItem> catlistItems = getIntent().getParcelableArrayListExtra("mylist");
        GridLayoutManager mLayoutManager3 = new GridLayoutManager(this, 2);
        mLayoutManager3.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerCollection.setLayoutManager(mLayoutManager3);
        recyclerCollection.setAdapter(new CollectionAdapter(CollectonActivity.this, catlistItems, this, "viewall"));
    }

    @OnClick({R.id.img_back})
    public void onClick(View view) {
        if (view.getId() == R.id.img_back) {
            finish();
        }
    }

    @Override
    public void onClickCollectionItem(CollectionItem item, int position) {

        startActivity(new Intent(CollectonActivity.this, CollectionProductActivity.class).putParcelableArrayListExtra("mylist", item.getProductdata()));
    }
}