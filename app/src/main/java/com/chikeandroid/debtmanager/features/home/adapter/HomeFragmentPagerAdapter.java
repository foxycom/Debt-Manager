package com.chikeandroid.debtmanager.features.home.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import androidx.fragment.app.FragmentPagerAdapter;
import com.chikeandroid.debtmanager.features.iowe.IOweFragment;
import com.chikeandroid.debtmanager.features.oweme.OweMeFragment;
import com.chikeandroid.debtmanager.features.people.PeopleFragment;

/**
 * Created by Chike on 3/14/2017.
 * PagerAdapter for MainActivity
 */

public class HomeFragmentPagerAdapter extends FragmentPagerAdapter {

    private final String[] mTabTitles = new String[] {"Owe Me", "I Owe", "People"};

    public HomeFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = OweMeFragment.newInstance();
                break;
            case 1:
                fragment = IOweFragment.newInstance();
                break;
            case 2:
                fragment = PeopleFragment.newInstance();
                break;
            default:
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabTitles[position];
    }
}
