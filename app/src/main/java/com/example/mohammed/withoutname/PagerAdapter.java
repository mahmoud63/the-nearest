package com.example.mohammed.withoutname;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Mohammed on 21/06/2017.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    Context context;

    public PagerAdapter(FragmentManager fm)
    {
        super(fm);
    }
    public PagerAdapter(FragmentManager fm, int NumOfTabs,Context context) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.context=context;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                TabFragment2 tab1 = new TabFragment2(context);
                return tab1;
            case 1:
                TabFragment1 tab2 = new TabFragment1(context);
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return "Tab " + (position + 1);
    }
}
