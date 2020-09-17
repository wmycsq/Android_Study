package com.sm.touchevent;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class CustomLinerLayout extends LinearLayout {
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public CustomLinerLayout(Context context) {
        super(context);
    }

    public CustomLinerLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomLinerLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomLinerLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.d("test", "dispatchTouchEvent --> " + TouchActionHelper.transformAction(event.getAction())
                + " and name is " + name);
//        if (name.equals("group1")){
//            return super.dispatchTouchEvent(event);
//        } else {
//            return false;
//        }
        return super.dispatchTouchEvent(event);
//                return true;
//        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        Log.d("test", "onInterceptTouchEvent --> " + TouchActionHelper.transformAction(event.getAction())
                + " and name is " + name);
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("test", "onTouchEvent --> " + TouchActionHelper.transformAction(event.getAction())
                + " and name is " + name);
//        if (name.equals("group1")) {
//            return super.onTouchEvent(event);
//        } else {
//            return false;
//        }
        return super.onTouchEvent(event);
    }

}
