package com.cscodetech.dailymilk.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bluehomestudio.luckywheel.LuckyWheel;
import com.bluehomestudio.luckywheel.OnLuckyWheelReachTheTarget;
import com.bluehomestudio.luckywheel.WheelItem;
import com.cscodetech.dailymilk.R;

import java.util.ArrayList;
import java.util.List;

public class SpinAndwinActivity extends AppCompatActivity {
    List<WheelItem> wheelItems;
    LuckyWheel lwv;
    Button button_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spin_andwin);
        lwv = findViewById(R.id.lwv);
        wheelItems = new ArrayList<WheelItem>();
        wheelItems.add(new WheelItem(Color.LTGRAY ,BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_eye_open),"1"));
        wheelItems.add(new WheelItem(Color.LTGRAY ,BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_eye_open),"2"));
        wheelItems.add(new WheelItem(Color.LTGRAY ,BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_eye_open),"3"));
        wheelItems.add(new WheelItem(Color.LTGRAY ,BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_eye_open),"4"));
        wheelItems.add(new WheelItem(Color.LTGRAY ,BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_eye_open),"5+" +
                "+++"));
//        button_start.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            }
//        });
//        wheelItems.add(new WheelItem(Color.LTGRAY, BitmapFactory.decodeResource(getResources(),
//                R.drawable.ic_eye_open) , "text 1"));
//
//        wheelItems.add(new WheelItem(Color.BLUE, BitmapFactory.decodeResource(getResources(),
//                R.drawable.ic_action_name) , "text 2"));
//
//        wheelItems.add(new WheelItem(Color.BLACK, BitmapFactory.decodeResource(getResources(),
//                R.drawable.ic_action_name) , "text 3"));
//
//        wheelItems.add(new WheelItem(Color.GRAY, BitmapFactory.decodeResource(getResources(),
//                R.drawable.ic_action_name) , "text 4"));
    }
}