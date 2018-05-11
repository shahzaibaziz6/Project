package com.shahzaibaziz.filehub;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static com.shahzaibaziz.filehub.WifiDirectBroadcastReceiver.MESSAGE_READ;

public class ConnectionActivity extends AppCompatActivity implements  View.OnClickListener {
    WifiP2pManager wifiP2pManager;
    WifiP2pManager.Channel wifiP2PChannel;
    BroadcastReceiver mReceiver;
    IntentFilter intentFilter;
    List<WifiP2pDevice> peer = new ArrayList<WifiP2pDevice>();
    public String[] deviceNameArray;
    public WifiP2pDevice[] deviceArray;
    ServerClass server;
    ClientClass client;
    SendReveice sendReveice;

    EditText etvSendData;
    Button btnSend, btnDiscover;

    ListView lvDeviceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
        etvSendData = findViewById(R.id.etv_connection_data);
        Intent intent = getIntent();
        btnSend = findViewById(R.id.btn_connection_send);
        String data = intent.getStringExtra("send");
        etvSendData.setText(data);
        btnDiscover = findViewById(R.id.btn_connection_discover);
        wifiP2pManager = (WifiP2pManager) getApplicationContext().getSystemService(Context.WIFI_P2P_SERVICE);
        wifiP2PChannel = wifiP2pManager.initialize(this, getMainLooper(), null);

        lvDeviceList = findViewById(R.id.lv_connection_clients);
        mReceiver = new WifiDirectBroadcastReceiver(wifiP2pManager, wifiP2PChannel, this);
        intentFilter = new IntentFilter();
        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        btnDiscover.setOnClickListener(this);
        btnSend= findViewById(R.id.btn_connection_send);
        btnSend.setOnClickListener(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_connection_discover: {

                onWifiP2pDiscovery();
                break;
            }
            case R.id.btn_connection_send:
            {
                String msg= etvSendData.getText().toString();
                sendReveice.write(msg.getBytes());
                break;
            }


        }

    }

    private void onWifiP2pDiscovery() {
        wifiP2pManager.discoverPeers(wifiP2PChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(), "Discovery is on", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int reason) {

                Toast.makeText(getApplicationContext(), "Something is wrong could not on Discovery", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public final WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peers) {

            if (!peers.getDeviceList().equals(peer))

            {
                peer.clear();
                peer.addAll(peers.getDeviceList());
                deviceNameArray = new String[peers.getDeviceList().size()];
                deviceArray = new WifiP2pDevice[peers.getDeviceList().size()];
                int index = 0;

                for (WifiP2pDevice device : peers.getDeviceList()) {
                    deviceNameArray[index] = device.deviceName;
                    deviceArray[index] = device;
                    index++;
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext()
                        , android.R.layout.simple_list_item_1, deviceNameArray);
                lvDeviceList.setAdapter(adapter);
                lvDeviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        final WifiP2pDevice device = deviceArray[position];
                        WifiP2pConfig cofig = new WifiP2pConfig();
                        cofig.deviceAddress = device.deviceAddress;


                        wifiP2pManager.connect(wifiP2PChannel, cofig, new WifiP2pManager.ActionListener() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(getApplicationContext(), "Conneted to " + device.deviceName, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(int reason) {
                                Toast.makeText(getApplicationContext(), " Not Conneted to " + device.deviceName, Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                });


                if (peer.size() == 0) {
                    Toast.makeText(getApplicationContext(), "No device Found", Toast.LENGTH_SHORT).show();
                    return;
                }

            }

        }
    };
    WifiP2pManager.ConnectionInfoListener connectionInfoListener= new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo info) {

            final InetAddress groupOwnerAddress= info.groupOwnerAddress;

            if(info.groupFormed&& info.isGroupOwner)
            {

                server= new ServerClass();
                server.start();

            }
            else if(info.groupFormed)
            {
                client = new ClientClass(groupOwnerAddress);
                client.start();


            }
        }
    } ;


    public  class ServerClass extends  Thread
    {
        Socket socket;
        ServerSocket serverSocket;

        @Override
        public void run() {
            try {
                serverSocket= new ServerSocket(4321);
                socket= serverSocket.accept();
                sendReveice= new SendReveice(socket);
                sendReveice.start();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }

        }
    }


    public class ClientClass extends Thread
    {
        Socket socket;
        String hostAdd;
        public ClientClass(InetAddress hostAddress)
        {
            hostAdd= hostAddress.getHostAddress();
            socket = new Socket();
        }

        @Override
        public void run() {
            try {
                socket.connect(new InetSocketAddress(hostAdd,4321),5000);
                sendReveice= new SendReveice(socket);
                sendReveice.start();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
         switch (msg.what)
         {
             case MESSAGE_READ:
             {
                 byte[] readBuff = (byte[]) msg.obj;
                 String tempMsg= new String(readBuff,0,msg.arg1);
                 etvSendData.setText(tempMsg);
                 break;
             }
         }

            return true;
        }
    });


    public  class SendReveice extends Thread{
        private Socket socket;
        private InputStream inputStream;
        private OutputStream outputStream;

        public SendReveice(Socket skt)
        {
            socket=skt;
            try {
                inputStream=socket.getInputStream();
                outputStream= socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void run() {
            byte[] buffer = new byte[10024];
            int bytes;
            while (socket != null) {
                try {
                    bytes = inputStream.read(buffer);
                    if (bytes > 0) {
                        handler.obtainMessage(MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                    }
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

        }
        public void write (byte [] bytes)
        {

            try {
                outputStream.write(R.id.etv_connection_data);
                Toast.makeText(getApplicationContext(),outputStream.toString(),Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    }


}
