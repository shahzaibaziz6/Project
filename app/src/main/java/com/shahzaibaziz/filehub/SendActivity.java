package com.shahzaibaziz.filehub;

import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import java.util.ArrayList;

public class SendActivity extends AppCompatActivity implements AudioSendFragment.OnFragmentInteractionListener,VideoSendFragment.OnFragmentInteractionListener,PictureSendFragment.OnFragmentInteractionListener{


    private SectionsPageAdapter mSectionsPageAdapter;

    private ViewPager mViewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        tabLayout= findViewById(R.id.tl_send_tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Audio"));
        tabLayout.addTab(tabLayout.newTab().setText("Video"));
        tabLayout.addTab(tabLayout.newTab().setText("Pictures"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mViewPager = findViewById(R.id.vp_send_container);
        mSectionsPageAdapter= new SectionsPageAdapter(getSupportFragmentManager(),3);
        mViewPager.setAdapter(mSectionsPageAdapter);
        mViewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
