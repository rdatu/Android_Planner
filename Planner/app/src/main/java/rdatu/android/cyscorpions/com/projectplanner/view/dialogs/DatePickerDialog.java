package rdatu.android.cyscorpions.com.projectplanner.view.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import rdatu.android.cyscorpions.com.projectplanner.view.activities_fragments.ListPlannerFragment;
import rdatu.android.cyscorpions.com.projectplanner.view.activities_fragments.ScheduleTaskActivity;

/**
 * Created by rayeldatu on 7/28/15.
 */
public class DatePickerDialog extends DialogFragment implements android.app.DatePickerDialog.OnDateSetListener {

    public static final String EXTRA_DATE = "date";
    public static final String EXTRA_FUNCTION = "function";
    private Callbacks mCallbacks;
    private String mStringDate, mDatePickerFunction;

    public DatePickerDialog() {

    }

    public static DatePickerDialog newInstance(String date, String function) {
        DatePickerDialog fragment = new DatePickerDialog();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_FUNCTION, function);
        args.putSerializable(EXTRA_DATE, date);
        fragment.setArguments(args);
        return fragment;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStringDate = getArguments().getString(EXTRA_DATE);
        mDatePickerFunction = getArguments().getString(EXTRA_FUNCTION);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        SimpleDateFormat df = new SimpleDateFormat("MMM-dd-yyyy");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        switch (mDatePickerFunction) {
            case ScheduleTaskActivity.FUNCTION_FORCHANGE:
                mCallbacks.onDateChanged(df.format(c.getTime()));
                break;
            case ListPlannerFragment.FUNCTION_FORCHANGE:
                mCallbacks.onJumpTo(df.format(c.getTime()));
                break;
        }

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MMM-dd-yyyy");
        Date date = null;
        try {
            date = df.parse(mStringDate);
        } catch (Exception e) {
            Log.e("Planner", "Something went wrong", e);
        }
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return new android.app.DatePickerDialog(getActivity(), this, year, month, day);
    }

    public interface Callbacks {
        void onDateChanged(String date);

        void onJumpTo(String date);
    }

}
