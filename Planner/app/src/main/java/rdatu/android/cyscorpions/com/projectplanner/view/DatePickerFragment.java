package rdatu.android.cyscorpions.com.projectplanner.view;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by rayeldatu on 7/28/15.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    public static final String EXTRA_DATE = "date";
    private Callbacks mCallbacks;
    private String mStringDate;

    public DatePickerFragment() {

    }

    public static DatePickerFragment newInstance(String date) {
        DatePickerFragment fragment = new DatePickerFragment();
        Bundle args = new Bundle();

        args.putSerializable(EXTRA_DATE, date);
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStringDate = getArguments().getString(EXTRA_DATE);
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
        mCallbacks.onDateChanged(df.format(c.getTime()));
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MMM-dd-yyyy");
        Date date = null;
        try {
            date = df.parse(mStringDate);
        } catch (Exception e) {
            Log.d("Planner", "Can't parse");
        }
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public interface Callbacks {
        void onDateChanged(String date);
    }

}
