package com.shahzaibaziz.filehub;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity {

    private ImageView  ivSplash;
    private ProgressBar pbSplash;
    private  static final int SPLASH_DISPLAY_LENGTH=2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);
//        getActionBar().hide();
        ivSplash= findViewById(R.id.iv_splash_background);
        pbSplash = findViewById(R.id.pb_splash_welcome);
        ivSplash.animate().alpha(0).setDuration(5000);
        pbSplash.animate().scaleY(0.5f).scaleX(0.5f).setDuration(3000);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this,SendReceiveActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);

    }
}
