package com.wisdom.nhoa.supervision.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @authorzhanglichao
 * @date2018/7/25 10:10
 * @package_name com.wisdom.nhoa.supervision.fragment
 */
public class SupervisionFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;

    public SupervisionFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        fragmentList = fragments;
        fm.beginTransaction().commitAllowingStateLoss();
    }

    @Override
    public Fragment getItem(int position) {
        return this.fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return this.fragmentList.size();
    }
}
