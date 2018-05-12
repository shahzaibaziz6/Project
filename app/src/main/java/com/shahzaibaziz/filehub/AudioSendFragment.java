package com.shahzaibaziz.filehub;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AudioSendFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AudioSendFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AudioSendFragment extends Fragment {
   private ArrayList<String> audioList,audioData;
   private  ListView lvAudio;
   private String sendData;


    private Button btnSend;

    private OnFragmentInteractionListener mListener;

    public AudioSendFragment() {
        // Required empty public constructor
    }

    public static AudioSendFragment newInstance() {
        AudioSendFragment fragment = new AudioSendFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view2 = inflater.inflate(R.layout.audio_send_fragment, container, false);
        lvAudio = view2.findViewById(R.id.lv_send_fragment_audio);
        btnSend= view2.findViewById(R.id.btn_audio_send_fragment_send);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sendData!=null)
                {
                    if(onWifi()) {

                        Intent intent = new Intent(getActivity(), ConnectionActivity.class);
                        intent.putExtra("send", sendData);
                        startActivity(intent);
                    }
                }
                else
                {
                    Toast.makeText(getActivity(),"Please select any item",Toast.LENGTH_SHORT).show();
                }

            }
        });

        showList();

        return view2;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }



    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
    private void showList()
    {

        audioList = new ArrayList<>();
        audioData= new ArrayList<>();


        String[] proj = { MediaStore.Audio.Media._ID,MediaStore.Audio.Media.DISPLAY_NAME};// Can include more data for more details and check it.

        Cursor audioCursor = getActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, proj, null, null, null);

        if(audioCursor != null){
            if(audioCursor.moveToFirst()){
                do{
                    int audioIndex = audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME);
//                    int audiodataindex= audioCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM);
//                    String ablum =audioCursor.getString(audiodataindex);

                    audioList.add(audioCursor.getString(audioIndex));


                }while(audioCursor.moveToNext());
            }
        }
        audioCursor.close();

        ArrayAdapter adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,audioList);

        lvAudio.setAdapter(adapter);
        lvAudio.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(),audioList.get(position),Toast.LENGTH_SHORT).show();
//                sendingList.add(audioList.get(position));
                sendData = audioList.get(position);
            }
        });

    }

    private boolean onWifi()
    {
        WifiManager wifiManager= (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(!wifiManager.isWifiEnabled())
        {
            wifiManager.setWifiEnabled(true);
        }
        return true;
    }

}
