package rdatu.android.cyscorpions.com.projectplanner.view;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by rayeldatu on 7/28/15.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    public static final String EXTRA_TIME = "time";
    private String mStringTime;
    private Callbacks mCallbacks;


    public TimePickerFragment() {
    }


    public static TimePickerFragment newInstance(String time) {
        Bundle args = new Bundle();
        TimePickerFragment fragment = new TimePickerFragment();
        args.putSerializable(EXTRA_TIME, time);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStringTime = getArguments().getString(EXTRA_TIME);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Date t = null;
        SimpleDateFormat df = new SimpleDateFormat("kk:mm");
        Calendar c = Calendar.getInstance();
        try {
            t = df.parse(mStringTime);
        } catch (Exception e) {

        }
        c.setTime(t);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, minute, true);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        SimpleDateFormat df = new SimpleDateFormat("kk:mm");
        Calendar c = Calendar.getInstance();

        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        //Minute is set to zero because the app only allows hourly time slots
        //whatever minute value you choose will result to zero.
        c.set(Calendar.MINUTE, 0);

        mCallbacks.onTimeChanged(df.format(c.getTime()));
    }


    public interface Callbacks {
        void onTimeChanged(String time);
    }
}
