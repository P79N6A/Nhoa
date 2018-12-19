package com.wisdom.nhoa.sendreceive.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @author HanXueFeng
 * @ProjectName project： Nhoa
 * @class package：com.wisdom.nhoa.approval.adapter
 * @class describe：
 * @time 2018/3/14 10:45
 * @change
 */

public class ApprovalFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;
    private FragmentManager fragmetnmanager;

    public ApprovalFragmentAdapter(List<Fragment> fragmentList, FragmentManager fragmetnmanager) {
        super(fragmetnmanager);
        this.fragmentList = fragmentList;
        this.fragmetnmanager = fragmetnmanager;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
