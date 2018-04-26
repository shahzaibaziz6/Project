package com.shahzaibaziz.filehub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SendReceiveActivity extends AppCompatActivity implements View.OnClickListener {

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
        Intent intent =null;
        switch (v.getId())
        {
            case (R.id.btn_send_receive_send):
            {

                intent=new Intent(SendReceiveActivity.this,SendActivity.class);
                break;
            }
            case (R.id.btn_send_receive_receive):
            {
                intent= new Intent(SendReceiveActivity.this,ReceivceActivity.class);
                break;
            }
        }
        if(intent!=null)
        {
            startActivity(intent);
        }
    }
}
