package com.bruhascended.badinage;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

class pager extends FragmentStatePagerAdapter {
    private int tabCount;

    pager(FragmentManager fm, int tabCount2) {
        super(fm);
        this.tabCount = tabCount2;
    }

    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new chat();
            case 1:
                return new Newsfeed();
            default:
                return null;
        }
    }

    public int getCount() {
        return this.tabCount;
    }
}
