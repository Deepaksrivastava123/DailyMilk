package com.cscodetech.marwarimarts.ui;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.cscodetech.marwarimarts.R;
import com.cscodetech.marwarimarts.utility.SessionManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.cscodetech.marwarimarts.utility.SessionManager.policy;

public class PrivacyActivity extends AppCompatActivity {
    @BindView(R.id.txt_data)
    TextView txtData;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        ButterKnife.bind(this);

        sessionManager = new SessionManager(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            txtData.setText(Html.fromHtml(sessionManager.getStringData(policy), Html.FROM_HTML_MODE_COMPACT));
        } else {
            txtData.setText(Html.fromHtml(sessionManager.getStringData(policy)));
        }
    }

    @OnClick({R.id.img_back})
    public void onClick(View view) {
        if (view.getId() == R.id.img_back) {
            finish();
        }
    }
}