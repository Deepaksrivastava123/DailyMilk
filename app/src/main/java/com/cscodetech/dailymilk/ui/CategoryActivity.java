package com.cscodetech.dailymilk.ui;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cscodetech.dailymilk.R;
import com.cscodetech.dailymilk.adepter.CategoryAdapter;
import com.cscodetech.dailymilk.model.CatlistItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CategoryActivity extends AppCompatActivity implements CategoryAdapter.RecyclerTouchListener {

    @BindView(R.id.recycler_category)
    RecyclerView recyclerCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        ButterKnife.bind(this);

        ArrayList<CatlistItem> catlistItems=getIntent().getParcelableArrayListExtra("mylist");
        GridLayoutManager mLayoutManager1 = new GridLayoutManager(this, 3);
        mLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerCategory.setLayoutManager(mLayoutManager1);
        recyclerCategory.setAdapter(new CategoryAdapter(CategoryActivity.this, catlistItems, this, "fhsk"));
    }

    @Override
    public void onClickCategoryItem(CatlistItem item, int position) {

    }

    @OnClick({R.id.img_back})
    public void onClick(View view) {
        if (view.getId() == R.id.img_back) {
            finish();
        }
    }
}