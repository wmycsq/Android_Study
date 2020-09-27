// IMessageService.aidl
package com.wmycsq.androidstudy;
import com.wmycsq.androidstudy.entity.CustomMessage;
import com.wmycsq.androidstudy.IMessageReceiveListener;

interface IMessageService {
    void sendMessage(in CustomMessage customMessage);

    void registerMessageReceiveListener(IMessageReceiveListener listener);

    void unRegisterMessageReceiveListener(IMessageReceiveListener listener);
}
