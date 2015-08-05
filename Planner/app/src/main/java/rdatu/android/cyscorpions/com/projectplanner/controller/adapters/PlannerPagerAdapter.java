package rdatu.android.cyscorpions.com.projectplanner.controller.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import rdatu.android.cyscorpions.com.projectplanner.view.activities_fragments.ListPlannerFragment;

/**
 * Created by rayeldatu on 7/27/15.
 */
public class PlannerPagerAdapter extends FragmentPagerAdapter {

    private ListPlannerFragment[] mFragmentList;

    public PlannerPagerAdapter(FragmentManager fm, ListPlannerFragment[] fragList) {
        super(fm);
        mFragmentList = fragList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList[position];
    }

    @Override
    public int getCount() {
        return mFragmentList.length;
    }

}
