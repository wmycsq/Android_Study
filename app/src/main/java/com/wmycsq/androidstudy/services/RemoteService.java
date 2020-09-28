package com.wmycsq.androidstudy.services;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.wmycsq.androidstudy.IConnectionService;
import com.wmycsq.androidstudy.IMessageReceiveListener;
import com.wmycsq.androidstudy.IMessageService;
import com.wmycsq.androidstudy.IServiceManager;
import com.wmycsq.androidstudy.entity.CustomMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/*
 * 管理和提供子进程的连接和消息服务
 * */
public class RemoteService extends Service {
    private boolean isConnect = false;
    private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Messenger clientMessenger = msg.replyTo;

            Bundle bundle = msg.getData();
            bundle.setClassLoader(CustomMessage.class.getClassLoader());
            CustomMessage message = bundle.getParcelable("messenger");
            Toast.makeText(RemoteService.this, message.getContent(), Toast.LENGTH_SHORT).show();

            try {
                CustomMessage messengerMessage = new CustomMessage("Message from remote by messenger");
                Message data = new Message();
                Bundle clientBundle = new Bundle();
                clientBundle.putParcelable("messenger", messengerMessage);
                data.setData(clientBundle);
                clientMessenger.send(data);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

//    private List<IMessageReceiveListener> messageReceiveListenerList = new ArrayList<>();

    //使用系统的RemoteCallbackList 可以准确的管理MessageReceive的对象，
    // 不能使用list，
    // 因为binder在序列化和反序列化的时候不能准确找到
    private RemoteCallbackList<IMessageReceiveListener> messageReceiveListenerList = new RemoteCallbackList<>();

    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
    private ScheduledFuture scheduledFuture;

    private IConnectionService connectionService = new IConnectionService.Stub() {
        @Override
        public void connect() throws RemoteException {
            try {
                Thread.sleep(5000);
                isConnect = true;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RemoteService.this, "connect", Toast.LENGTH_SHORT).show();
                    }
                });

                //模拟定时发送消息给主进程
                scheduledFuture = scheduledThreadPoolExecutor.scheduleAtFixedRate(new Runnable() {
                    @Override
                    public void run() {
//                        for (IMessageReceiveListener listener : messageReceiveListenerList) {
//                            try {
//                                CustomMessage message = new CustomMessage("this message from remote");
//                                listener.onRecevieMessage(message);
//                            } catch (RemoteException e) {
//                                e.printStackTrace();
//                            }
//                        }

                        int size = messageReceiveListenerList.beginBroadcast();
                        for (int i = 0; i < size; i++){
                            try {
                                CustomMessage message = new CustomMessage("this message from remote");
                                messageReceiveListenerList.getBroadcastItem(i).onRecevieMessage(message);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                        messageReceiveListenerList.finishBroadcast();
                    }
                }, 5000, 5000, TimeUnit.MILLISECONDS);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void disconnect() throws RemoteException {
            isConnect = false;
            scheduledFuture.cancel(true);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(RemoteService.this, "disconnect", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public boolean isConnect() throws RemoteException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(RemoteService.this, String.valueOf(isConnect), Toast.LENGTH_SHORT).show();
                }
            });
            return isConnect;
        }
    };

    private IMessageService messageService = new IMessageService.Stub() {
        @Override
        public void sendMessage(final CustomMessage customMessage) throws RemoteException {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(RemoteService.this, customMessage.getContent(), Toast.LENGTH_SHORT).show();
                }
            });
            customMessage.setSendSuccess(isConnect);
        }

        @Override
        public void registerMessageReceiveListener(IMessageReceiveListener listener) throws RemoteException {
            if (listener != null) {
                messageReceiveListenerList.register(listener);
            }
        }

        @Override
        public void unRegisterMessageReceiveListener(IMessageReceiveListener listener) throws RemoteException {
            if (listener != null) {
                messageReceiveListenerList.unregister(listener);
            }
        }
    };

    private Messenger messenger = new Messenger(handler);

    private IServiceManager serviceManager = new IServiceManager.Stub() {
        @Override
        public IBinder getService(String name) throws RemoteException {
            if (IConnectionService.class.getSimpleName().equals(name)) {
                return connectionService.asBinder();
            } else if (IMessageService.class.getSimpleName().equals(name)) {
                return messageService.asBinder();
            } else if (Messenger.class.getSimpleName().equals(name)){
                return messenger.getBinder();
            } else return null;
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return serviceManager.asBinder();
    }
}
