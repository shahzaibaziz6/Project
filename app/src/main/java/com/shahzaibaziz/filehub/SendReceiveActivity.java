package com.shahzaibaziz.filehub;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SendReceiveActivity extends AppCompatActivity implements View.OnClickListener {

    private  View view;
    private static final int _READ_EXTERNAL_STROAGE = 1;
    private Button btnSend,btnReceive;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_receivce);
        initGui();
        initClickable();
    }
    private void initGui()
    {
        btnReceive= findViewById(R.id.btn_send_receive_receive);
        btnSend= findViewById(R.id.btn_send_receive_send);
    }
    private  void initClickable()
    {
        btnSend.setOnClickListener(this);
        btnReceive.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
       view= v;
        askPermission(view);
    }

    private void askPermission(View v) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {



            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                _READ_EXTERNAL_STROAGE);
            }

        }
        else {
            // Permission has already been granted
            newActivity(view);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case _READ_EXTERNAL_STROAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was grantoned, yay! Do the
                    // contacts-related task you need to do.
                    newActivity(view);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
private void newActivity( View v)
{
    Intent intent = null;
    switch (v.getId()) {
        case (R.id.btn_send_receive_send): {

            intent = new Intent(SendReceiveActivity.this, SendActivity.class);
            break;
        }
        case (R.id.btn_send_receive_receive): {
            intent = new Intent(SendReceiveActivity.this, ReceivceActivity.class);
            break;
        }
    }
    if (intent != null) {
        startActivity(intent);
    }
}
}
