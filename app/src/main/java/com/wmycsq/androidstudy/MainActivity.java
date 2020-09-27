package com.wmycsq.androidstudy;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.wmycsq.androidstudy.entity.CustomMessage;
import com.wmycsq.androidstudy.services.RemoteService;

public class MainActivity extends AppCompatActivity {
    private IConnectionService connectionServiceProxy;
    private IMessageService messageServiceProxy;
    private IServiceManager serviceManagerProxy;
    private Messenger messengerProxy;
    private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            bundle.setClassLoader(CustomMessage.class.getClassLoader());
            final CustomMessage message = bundle.getParcelable("messenger");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, message.getContent(), Toast.LENGTH_SHORT).show();
                }
            }, 2000);
        }
    };
    private Messenger clientMessenger = new Messenger(handler);
    private IMessageReceiveListener messageReceiveListener = new IMessageReceiveListener.Stub() {
        @Override
        public void onRecevieMessage(final CustomMessage message) throws RemoteException {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this,
                            message.getContent(),
                            Toast.LENGTH_SHORT)
                            .show();
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Intent intent = new Intent(this, RemoteService.class);
        bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                try {
                    serviceManagerProxy = IServiceManager.Stub.asInterface(service);
                    connectionServiceProxy = IConnectionService.Stub.asInterface(
                            serviceManagerProxy.getService(
                                    IConnectionService.class.getSimpleName()));

                    messageServiceProxy = IMessageService.Stub.asInterface(
                            serviceManagerProxy.getService(
                                    IMessageService.class.getSimpleName()));
                    messengerProxy = new Messenger(serviceManagerProxy.getService(Messenger.class.getSimpleName()));

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        }, Service.BIND_AUTO_CREATE);
    }

    public void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.connect_btn:
                    connectionServiceProxy.connect();
                    break;
                case R.id.disconnect_btn:
                    connectionServiceProxy.disconnect();
                    break;
                case R.id.is_connect_btn:
                    connectionServiceProxy.isConnect();
                    break;
                case R.id.send_message:
                    CustomMessage message = new CustomMessage("Message from main");
                    messageServiceProxy.sendMessage(message);
                    break;
                case R.id.registerMessageListener:
                    messageServiceProxy.registerMessageReceiveListener(messageReceiveListener);
                    break;
                case R.id.unRegisterMessageListener:
                    messageServiceProxy.unRegisterMessageReceiveListener(messageReceiveListener);
                    break;
                case R.id.messenger_btn:
                    CustomMessage messengerMessage = new CustomMessage("Message from main by messenger");
                    Message data = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("messenger", messengerMessage);
                    data.setData(bundle);
                    data.replyTo = clientMessenger;
                    messengerProxy.send(data);
                    break;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}