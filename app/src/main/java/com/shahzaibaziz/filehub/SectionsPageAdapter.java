package com.shahzaibaziz.filehub;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class SectionsPageAdapter extends FragmentStatePagerAdapter {
    private  int numOfTab;
    public SectionsPageAdapter(FragmentManager fm,int length) {

        super(fm);
        numOfTab=length;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
            {
                AudioSendFragment fragment= new AudioSendFragment();

                return fragment;
            }
            case 1:
            {
                VideoSendFragment fragment= new VideoSendFragment();
                return  fragment;
            }
            case 2:
            {
                PictureSendFragment fragment= new PictureSendFragment();
                return  fragment;
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return numOfTab;
    }
}
