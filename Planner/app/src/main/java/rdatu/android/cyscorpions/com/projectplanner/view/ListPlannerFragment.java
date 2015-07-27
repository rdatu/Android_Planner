package rdatu.android.cyscorpions.com.projectplanner.view;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import rdatu.android.cyscorpions.com.projectplanner.R;

/**
 * Created by rayeldatu on 7/27/15.
 */
public class ListPlannerFragment extends ListFragment {

    private final String[] TIME_SLOT = {
            "00:00 - 01:00", "01:00 - 02:00",
            "02:00 - 03:00", "03:00 - 04:00",
            "04:00 - 05:00", "05:00 - 06:00",
            "06:00 - 07:00", "07:00 - 08:00",
            "08:00 - 09:00", "09:00 - 10:00",
            "10:00 - 11:00", "11:00 - 12:00",
            "12:00 - 01:00", "01:00 - 02:00",
            "02:00 - 03:00", "03:00 - 04:00",
            "04:00 - 05:00", "05:00 - 06:00",
            "06:00 - 07:00", "07:00 - 08:00",
            "08:00 - 09:00", "09:00 - 10:00",
            "10:00 - 11:00", "11:00 - 12:00",
    };
    private Calendar mCalendar;
    private TextView mTextTask, mTimeSlot;

    public ListPlannerFragment(Calendar a) {
        mCalendar = a;
    }

    static ListPlannerFragment newInstance(Calendar a) {
        return new ListPlannerFragment(a);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ListFragmentAdapter adapter = new ListFragmentAdapter(TIME_SLOT);
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        ListView lv = (ListView) v.findViewById(R.id.listView);
        return v;
    }

    protected final void onNextDay() {
        if (mCalendar.get(Calendar.DAY_OF_MONTH) == mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            mCalendar.set(mCalendar.MONTH, mCalendar.MONTH + 1);
        } else {
            mCalendar.set(Calendar.DAY_OF_MONTH, mCalendar.get(Calendar.DAY_OF_MONTH) + 1);
        }

        SimpleDateFormat df = new SimpleDateFormat("MMM-dd-yyyy");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            String dateString = df.format(mCalendar.getTime());
            Log.d("DATEPROBLEM", "Next " + dateString);

        }

    }

    protected final void onPreviousDay() {
        if (mCalendar.get(Calendar.DAY_OF_MONTH) == mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            mCalendar.set(mCalendar.MONTH, mCalendar.MONTH - 1);
        } else {
            mCalendar.set(Calendar.DAY_OF_MONTH, mCalendar.get(Calendar.DAY_OF_MONTH) - 1);
        }

        SimpleDateFormat df = new SimpleDateFormat("MMM-dd-yyyy");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            String dateString = df.format(mCalendar.getTime());
            Log.d("DATEPROBLEM", "PREVIOUS " + dateString);

        }

    }

    private class ListFragmentAdapter extends ArrayAdapter<String> {


        public ListFragmentAdapter(String[] time_slot) {
            super(getActivity(), android.R.layout.simple_list_item_1, time_slot);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.daily_list_item, null);
            }

            String time = getItem(position);

            mTimeSlot = (TextView) convertView.findViewById(R.id.time_slot);
            mTimeSlot.setText(time);
            mTextTask = (TextView) convertView.findViewById(R.id.task_name);
            mTextTask.setText(getString(R.string.default_task_text));

            return convertView;
        }
    }


}
