package com.fcis.gp.aya.aya;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohse on 2/14/2018.
 */

public class SectionPageAdapter extends FragmentPagerAdapter {
private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentNamesList = new ArrayList<>();

    public void addFragment(Fragment f, String title )
    {
        mFragmentList.add(f);
        mFragmentNamesList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentNamesList.get(position);
    }

    public SectionPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
