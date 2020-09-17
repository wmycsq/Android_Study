package com.sm.touchevent;

public class TouchActionHelper {

    public static final String transformAction(int action){
        String s = "";
        switch (action){
            case 0:
                s = "Action Down";
                break;
            case 1:
                s = "Action Up";
                break;
            case 2:
                s = "Action Move";
                break;
        }
        return s;
    }
}
