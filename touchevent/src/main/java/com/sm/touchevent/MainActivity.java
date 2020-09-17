package com.sm.touchevent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.view_group1)
    CustomLinerLayout viewGroup1;

    @BindView(R.id.view_group2)
    CustomLinerLayout viewGroup2;

    @BindView(R.id.text_view1)
    CustomTextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        viewGroup1.setName("group1");
        viewGroup2.setName("group2");
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.d("test", "dispatchTouchEvent --> " + TouchActionHelper.transformAction(event.getAction())
                + " and name is activity");
        Log.d("moveTest", TouchActionHelper.transformAction(event.getAction()) + " --> X value is " + event.getX());
        Log.d("moveTest", TouchActionHelper.transformAction(event.getAction()) + " --> Y value is " + event.getY());

        return super.dispatchTouchEvent(event);
//        return true;
//        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("test", "onTouchEvent --> " + TouchActionHelper.transformAction(event.getAction())
                + " and name is activity");
        return super.onTouchEvent(event);
    }
}
