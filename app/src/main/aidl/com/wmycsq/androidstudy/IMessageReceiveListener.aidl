// IMessageReceiveListener.aidl
package com.wmycsq.androidstudy;
import com.wmycsq.androidstudy.entity.CustomMessage;

// Declare any non-default types here with import statements

interface IMessageReceiveListener {
    void onRecevieMessage(in CustomMessage message);
}
