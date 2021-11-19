package com.cscodetech.marwarimarts.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import androidx.appcompat.app.AppCompatDelegate;

import com.cscodetech.marwarimarts.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cdflynn.android.library.checkview.CheckView;

public class CompleOrderActivity extends RootActivity {
    CheckView mcheckview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comple_order);
        ButterKnife.bind(this);
        mcheckview = findViewById(R.id.check);
        mcheckview.check();
    }

    @OnClick({R.id.img_back, R.id.btn_myorder})
    public void onClick(View view) {
        if (view.getId() == R.id.img_back || view.getId() == R.id.btn_myorder) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }
}