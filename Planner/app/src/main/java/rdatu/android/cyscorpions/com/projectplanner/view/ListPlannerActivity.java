package rdatu.android.cyscorpions.com.projectplanner.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.Calendar;

import rdatu.android.cyscorpions.com.projectplanner.R;

/**
 * Created by rayeldatu on 7/27/15.
 */
public class ListPlannerActivity extends FragmentActivity {


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


        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.ViewPager);
        mCurrentCalendar = Calendar.getInstance();
        final ListPlannerFragment[] LIST_PLANNER = new ListPlannerFragment[3];
        setContentView(mViewPager);
        Calendar prevDay, nextDay;
        prevDay = (Calendar) mCurrentCalendar.clone();
        nextDay = (Calendar) mCurrentCalendar.clone();

        prevDay.set(Calendar.DAY_OF_MONTH, prevDay.get(Calendar.DAY_OF_MONTH) - 1);
        nextDay.set(Calendar.DAY_OF_MONTH, nextDay.get(Calendar.DAY_OF_MONTH) + 1);

        LIST_PLANNER[0] = ListPlannerFragment.newInstance(prevDay);
        LIST_PLANNER[1] = ListPlannerFragment.newInstance(mCurrentCalendar);
        LIST_PLANNER[2] = ListPlannerFragment.newInstance(nextDay);

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int i) {

                return LIST_PLANNER[i];
            }

            @Override
            public int getCount() {
                return LIST_PLANNER.length;
            }
        });

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

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


    }
}
