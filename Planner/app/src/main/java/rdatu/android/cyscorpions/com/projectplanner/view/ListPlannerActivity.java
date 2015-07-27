package rdatu.android.cyscorpions.com.projectplanner.view;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import java.util.Calendar;

import rdatu.android.cyscorpions.com.projectplanner.R;
import rdatu.android.cyscorpions.com.projectplanner.controller.PlannerPagerAdapter;

/**
 * Created by rayeldatu on 7/27/15.
 */
public class ListPlannerActivity extends FragmentActivity implements ListPlannerFragment.Callbacks {


    private static final int PAGE_LEFT = 0;
    private static final int PAGE_MIDDLE = 1;
    private static final int PAGE_RIGHT = 2;
    private ViewPager mViewPager;
    private Calendar mCurrentCalendar;
    private int mSelectedPageIndex;

    @Override
    @SuppressWarnings("deprecation")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ListPlannerFragment[] LIST_PLANNER = new ListPlannerFragment[3];
        Calendar prevDay, nextDay;

        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.ViewPager);
        mCurrentCalendar = Calendar.getInstance();

        setContentView(mViewPager);

        prevDay = (Calendar) mCurrentCalendar.clone();
        nextDay = (Calendar) mCurrentCalendar.clone();

        prevDay.set(Calendar.DAY_OF_MONTH, prevDay.get(Calendar.DAY_OF_MONTH) - 1);
        nextDay.set(Calendar.DAY_OF_MONTH, nextDay.get(Calendar.DAY_OF_MONTH) + 1);

        LIST_PLANNER[0] = ListPlannerFragment.newInstance(prevDay);
        LIST_PLANNER[1] = ListPlannerFragment.newInstance(mCurrentCalendar);
        LIST_PLANNER[2] = ListPlannerFragment.newInstance(nextDay);

        FragmentManager fm = getSupportFragmentManager();
        PlannerPagerAdapter adapter = new PlannerPagerAdapter(fm, LIST_PLANNER);


        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                //Intentionally left Blank!
            }

            @Override
            public void onPageSelected(int i) {
                mSelectedPageIndex = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                if (i == ViewPager.SCROLL_STATE_IDLE) {
                    if (mSelectedPageIndex < PAGE_MIDDLE) {
                        LIST_PLANNER[0].onPreviousDay();
                        LIST_PLANNER[1].onPreviousDay();
                        LIST_PLANNER[2].onPreviousDay();
                    } else if (mSelectedPageIndex > PAGE_MIDDLE) {
                        LIST_PLANNER[0].onNextDay();
                        LIST_PLANNER[1].onNextDay();
                        LIST_PLANNER[2].onNextDay();
                    }
                    mViewPager.setCurrentItem(1, false);
                }
            }
        });
        //SET Current Selected item to index 1,
        //index 1 is the middle one which should be the item that is always selected
        mViewPager.setCurrentItem(1, false);
        mViewPager.setAdapter(adapter);
    }

    @TargetApi(11)
    @Override
    public void onListUpdate(String date) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (getActionBar() != null) {
                setTitle(date);
            }
        }

    }
}
