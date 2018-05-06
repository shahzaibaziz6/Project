import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.widget.Toast;

import com.shahzaibaziz.filehub.SendActivity;

public class WifiDirectBroadcastReceiver extends BroadcastReceiver {
    static final int MESSAGE_READ=1;
    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private SendActivity mActivity;
    public WifiDirectBroadcastReceiver(WifiP2pManager m,WifiP2pManager.Channel c,SendActivity main)
    {
        mManager=m;
        mChannel =c;
        mActivity= main;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        String action= intent.getAction();
        if(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action))
        {
            int state= intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE,-1);
            if(state == WifiP2pManager.WIFI_P2P_STATE_ENABLED)
            {
                Toast.makeText(context,"Wifi is On",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(context,"Wifi is OFF",Toast.LENGTH_SHORT).show();

            }

        }
        else if(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action))
        {

            if (mManager!=null)
            {
//
// mManager.requestConnectionInfo(mChannel, mainActivity.peerListListener);
//                mManager.requestPeers(mChannel,mainActivity.peerListListener);
            }

        }
        else if(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action))
        {

            if(mManager==null)
            {
                return;
            }
            NetworkInfo networkInfo= intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
            if(networkInfo.isConnected())
            {
//                mManager.requestConnectionInfo(mChannel,mainActivity.connectionInfoListener);

            }
            else
            {
//                mainActivity.tvConnectionStatus.setText("device Disconnected");
            }



        }
        else if(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action))
        {

        }

    }
}
