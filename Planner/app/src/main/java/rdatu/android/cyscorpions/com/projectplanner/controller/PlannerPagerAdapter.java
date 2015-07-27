package rdatu.android.cyscorpions.com.projectplanner.controller;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import rdatu.android.cyscorpions.com.projectplanner.view.ListPlannerFragment;

/**
 * Created by rayeldatu on 7/27/15.
 */
public class PlannerPagerAdapter extends FragmentPagerAdapter {

    ListPlannerFragment[] fragList;

    public PlannerPagerAdapter(FragmentManager fm, ListPlannerFragment[] fragList) {
        super(fm);
        this.fragList = fragList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragList[position];
    }

    @Override
    public int getCount() {
        return fragList.length;
    }

}
