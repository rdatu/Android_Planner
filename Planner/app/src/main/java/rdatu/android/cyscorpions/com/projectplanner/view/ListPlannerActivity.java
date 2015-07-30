package rdatu.android.cyscorpions.com.projectplanner.view;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import rdatu.android.cyscorpions.com.projectplanner.R;
import rdatu.android.cyscorpions.com.projectplanner.controller.PlannerPagerAdapter;

/**
 * Created by rayeldatu on 7/27/15.
 */
public class ListPlannerActivity extends FragmentActivity implements ListPlannerFragment.Callbacks, DatePickerDialog.Callbacks {

    public static final String FUNCTION_FORCHANGE = "jumpto";
    public static final String EXTRA_TIME_SELECTED = "timeselected";
    public static final String EXTRA_DATE_SELECTED = "dateselected";
    public static final String EXTRA_NEWDATE = "newdate";
    private static final int PAGE_MIDDLE = 1;
    final ListPlannerFragment[] LIST_PLANNER = new ListPlannerFragment[3];
    private ViewPager mViewPager;
    private Calendar mCurrentCalendar;
    private int mSelectedPageIndex;
    private PlannerPagerAdapter mAdapter;


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    @SuppressWarnings("deprecation")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Planner", "OnCreate");

        Calendar prevDay, nextDay;

        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.ViewPager);

        if (getIntent().hasExtra(EXTRA_NEWDATE)) {
            SimpleDateFormat df = new SimpleDateFormat("MMM-dd-yyyy");
            try {
                mCurrentCalendar = Calendar.getInstance();
                mCurrentCalendar.setTime(df.parse(getIntent().getStringExtra(EXTRA_NEWDATE)));
            } catch (Exception e) {

            }
        } else {
            mCurrentCalendar = Calendar.getInstance();
        }

        setContentView(mViewPager);

        prevDay = (Calendar) mCurrentCalendar.clone();
        nextDay = (Calendar) mCurrentCalendar.clone();

        prevDay.add(Calendar.DAY_OF_MONTH, -1);
        nextDay.add(Calendar.DAY_OF_MONTH, 1);

        LIST_PLANNER[0] = ListPlannerFragment.newInstance(prevDay, getApplicationContext());
        LIST_PLANNER[1] = ListPlannerFragment.newInstance(mCurrentCalendar, getApplicationContext());
        LIST_PLANNER[2] = ListPlannerFragment.newInstance(nextDay, getApplicationContext());

        mAdapter = new PlannerPagerAdapter(getSupportFragmentManager(), LIST_PLANNER);

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

        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(1, false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_jump:
                showDatePicker();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showDatePicker() {
        FragmentManager fm = getSupportFragmentManager();
        DatePickerDialog dialog = DatePickerDialog.newInstance(getTitle().toString(), FUNCTION_FORCHANGE);
        dialog.show(fm, "datePicker");
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

    @Override
    public void onTimeSlotSelected(String time, String date) {
        Intent i = new Intent(getApplicationContext(), ScheduleTaskActivity.class);
        i.putExtra(EXTRA_TIME_SELECTED, time);
        i.putExtra(EXTRA_DATE_SELECTED, date);
        startActivity(i);
    }

    @Override
    public void onDateChanged(String date) {
        //Not Used here
    }

    @TargetApi(11)
    @Override
    public void onJumpTo(String date) {

        Intent i = getIntent();
        i.putExtra(EXTRA_NEWDATE, date);
        finish();
        startActivity(i);
    }
}
