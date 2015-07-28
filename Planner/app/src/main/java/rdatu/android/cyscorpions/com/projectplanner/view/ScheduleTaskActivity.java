package rdatu.android.cyscorpions.com.projectplanner.view;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import rdatu.android.cyscorpions.com.projectplanner.R;
import rdatu.android.cyscorpions.com.projectplanner.model.Tasks;

/**
 * Created by rayeldatu on 7/27/15.
 */
public class ScheduleTaskActivity extends FragmentActivity implements DatePickerFragment.Callbacks, TimePickerFragment.Callbacks {

    private String mTimeStart, mTimeEnd, mDateSelected;
    private EditText mFromTimeText, mToTimeText, mTaskNameText, mTaskDescriptionText, mPlaceText;
    private Button mDateButton, mDoneButton;

    @Override
    @TargetApi(11)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scheduled_activity);
        mDateSelected = getIntent().getStringExtra(ListPlannerActivity.EXTRA_DATE_SELECTED);
        String timeSelected = getIntent().getStringExtra(ListPlannerActivity.EXTRA_TIME_SELECTED);
        if (!timeSelected.equals(null)) {
            mTimeStart = timeSelected.substring(0, 5);
            mTimeEnd = timeSelected.substring(8, timeSelected.length());
        }

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            getActionBar().setTitle(timeSelected + ", " + mDateSelected);
        }

        mFromTimeText = (EditText) findViewById(R.id.fromTime);
        mFromTimeText.setText(mTimeStart);

        mToTimeText = (EditText) findViewById(R.id.toTime);
        mToTimeText.setText(mTimeEnd);
        mToTimeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });

        mDateButton = (Button) findViewById(R.id.dateButton);
        mDateButton.setText(mDateSelected);
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        mTaskNameText = (EditText) findViewById(R.id.inputTaskName);
        mTaskDescriptionText = (EditText) findViewById(R.id.inputDescription);
        mPlaceText = (EditText) findViewById(R.id.inputPlace);

        mDoneButton = (Button) findViewById(R.id.doneButton);
        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name, descr, date, time, place;
                name = mTaskNameText.getText().toString();
                descr = mTaskDescriptionText.getText().toString();
                date = mDateButton.getText().toString();
                time = mTimeStart + " - " + mTimeEnd;
                place = mPlaceText.getText().toString();

                Tasks task = new Tasks();
                task.setDate(date);
                task.setTimeSlot(time);
                task.setTaskName(name);
                task.setDescription(descr);
                task.setPlace(place);

                Toast.makeText(getApplicationContext(), "TODO: Confirmation Dialog", Toast.LENGTH_LONG).show();


            }
        });

    }


    private void showDatePicker() {
        FragmentManager fm = getSupportFragmentManager();
        DatePickerFragment dialog = DatePickerFragment.newInstance(mDateButton.getText().toString());
        dialog.show(fm, "datePicker");
    }

    private void showTimePicker() {
        FragmentManager fm = getSupportFragmentManager();
        TimePickerFragment dialog = TimePickerFragment.newInstance(mToTimeText.getText().toString());
        dialog.show(fm, "timePicker");
    }

    @Override
    public void onTimeChanged(String time) {
        mToTimeText.setText(time);

    }

    @Override
    public void onDateChanged(String date) {
        mDateButton.setText(date);
    }
}
