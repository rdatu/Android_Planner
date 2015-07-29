package rdatu.android.cyscorpions.com.projectplanner.view;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import rdatu.android.cyscorpions.com.projectplanner.R;
import rdatu.android.cyscorpions.com.projectplanner.controller.TaskManager;
import rdatu.android.cyscorpions.com.projectplanner.model.Tasks;

/**
 * Created by rayeldatu on 7/27/15.
 */
public class ScheduleTaskActivity extends FragmentActivity implements DatePickerFragment.Callbacks, TimePickerFragment.Callbacks {

    private final String PRIORITY_HIGH = "HIGH";
    private final String PRIORITY_LOW = "LOW";
    private String mTimeStart, mTimeEnd, mDateSelected;
    private EditText mFromTimeText, mToTimeText, mTaskNameText, mTaskDescriptionText, mPlaceText;
    private Button mDateButton, mDoneButton, mPriorityButton;
    private TaskManager mTaskManager;

    @Override
    @TargetApi(11)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scheduled_activity);

        mTaskManager = TaskManager.get(getApplicationContext());

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

        mPriorityButton = (Button) findViewById(R.id.priority_button);
        mPriorityButton.setText(PRIORITY_HIGH);
        mPriorityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String p = mPriorityButton.getText().toString();
                if (p.equals(PRIORITY_HIGH)) {
                    mPriorityButton.setText(PRIORITY_LOW);
                } else if (p.equals(PRIORITY_LOW)) {
                    mPriorityButton.setText(PRIORITY_HIGH);
                }
            }
        });
        mDoneButton = (Button) findViewById(R.id.doneButton);
        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (areFieldsFilled()) {
                    String name, descr, date, place, priority;
                    int interval;
                    int timeStart, timeEnd;

                    SimpleDateFormat df = new SimpleDateFormat("kk:mm");
                    Date dStart, dEnd;
                    dStart = dEnd = null;
                    name = mTaskNameText.getText().toString();
                    descr = mTaskDescriptionText.getText().toString();
                    date = mDateButton.getText().toString();
                    timeStart = Integer.parseInt(mTimeStart.substring(0, 2));
                    timeEnd = Integer.parseInt(mTimeEnd.substring(0, 2));
                    interval = Math.abs(timeStart - timeEnd);
                    place = mPlaceText.getText().toString();
                    priority = mPriorityButton.getText().toString();

                    Log.d("Planner", timeStart + " < " + (timeEnd - 1));


                    for (int i = timeStart; i < timeEnd; i++) {
                        try {
                            Tasks task = new Tasks();
                            dStart = df.parse(i + ":00");
                            dEnd = df.parse((i + 1) + ":00");
                            Log.d("Planner", df.format(dStart) + " - " + df.format(dEnd));
                            task.setDate(date);
                            task.setTaskName(name);
                            task.setDescription(descr);
                            task.setPlace(place);
                            task.setPriority(priority);
                            mTaskManager.saveTask(task);
                        } catch (Exception e) {
                            Log.d("Planner", "Something went wrong!");
                        }
                    }


                    Toast.makeText(getApplicationContext(), "TODO: Confirmation Dialog\nSaved: " + mTaskManager.getTasks().size(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "All fields are required.", Toast.LENGTH_SHORT).show();

                }


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
        mTimeEnd = mToTimeText.getText().toString();
    }

    @Override
    public void onDateChanged(String date) {
        mDateButton.setText(date);

    }

    private boolean areFieldsFilled() {
        boolean ready = true;

        ready = isNotEmpty(mTaskNameText);
        ready = isNotEmpty(mTaskDescriptionText);
        ready = isNotEmpty(mPlaceText);

        if (TextUtils.isEmpty(mFromTimeText.getText().toString()))
            ready = false;
        if (TextUtils.isEmpty(mToTimeText.getText().toString()))
            ready = false;
        if (TextUtils.isEmpty(mDateButton.getText().toString()))
            ready = false;

        return ready;
    }

    private boolean isNotEmpty(EditText item) {
        if (TextUtils.isEmpty(item.getText().toString())) {
            item.setError("This item must not be Empty");
            return false;
        }
        return true;

    }


}
