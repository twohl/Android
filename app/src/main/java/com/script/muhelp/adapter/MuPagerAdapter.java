package com.script.muhelp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.script.muhelp.fragment.MuChatFragment;
import com.script.muhelp.fragment.MuFindFragment;
import com.script.muhelp.fragment.MuMineFragment;
import com.script.muhelp.fragment.MuNWFragment;
import com.script.muhelp.fragment.MuPersonFragment;

/**
 * Created by hongl on 2018/2/18.
 */

public class MuPagerAdapter extends FragmentPagerAdapter {
    private MuChatFragment chatFragment;
    private MuPersonFragment personFragment;
    private MuFindFragment findFragment;
    private MuNWFragment nwFragment;
    private MuMineFragment mineFragment;

    public MuPagerAdapter(FragmentManager fm) {
        super(fm);
        chatFragment = new MuChatFragment();
        personFragment = new MuPersonFragment();
        findFragment = new MuFindFragment();
        nwFragment = new MuNWFragment();
        mineFragment = new MuMineFragment();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return chatFragment;
            case 1:
                return personFragment;
            case 3:
                return findFragment;
            case 4:
                return mineFragment;
            default:
                return nwFragment;
        }

    }

    @Override
    public int getCount() {
        return 5;
    }
}