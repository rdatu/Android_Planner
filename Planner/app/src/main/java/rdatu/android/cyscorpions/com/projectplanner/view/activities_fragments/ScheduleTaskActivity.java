package rdatu.android.cyscorpions.com.projectplanner.view.activities_fragments;

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
import android.widget.LinearLayout;
import android.widget.Toast;

import rdatu.android.cyscorpions.com.projectplanner.R;
import rdatu.android.cyscorpions.com.projectplanner.controller.modelmanagers.TaskManager;
import rdatu.android.cyscorpions.com.projectplanner.model.objects.Tasks;
import rdatu.android.cyscorpions.com.projectplanner.view.dialogs.DatePickerDialog;
import rdatu.android.cyscorpions.com.projectplanner.view.dialogs.OverwriteDialog;
import rdatu.android.cyscorpions.com.projectplanner.view.dialogs.TimePickerDialog;

/**
 * Created by rayeldatu on 7/27/15.
 */
public class ScheduleTaskActivity extends FragmentActivity implements DatePickerDialog.Callbacks, TimePickerDialog.Callbacks, OverwriteDialog.Callbacks {

    public static final String FUNCTION_FORCHANGE = "simplepicker";
    public static final String ACTIVITY_FUNCTION = "FUNCTION";
    private final String PRIORITY_HIGH = "HIGH";
    private final String PRIORITY_LOW = "LOW";

    private String mTimeStart, mTimeEnd, mDateSelected, mPlace, mName, mDescription, mPriority;
    private EditText mFromTimeText, mToTimeText, mTaskNameText, mTaskDescriptionText, mPlaceText;
    private Button mDateButton, mDoneButton, mPriorityButton;
    private LinearLayout mLayout;
    private TaskManager mTaskManager;


    public static String getFormattedTime(int hour) {
        String s, e;
        s = String.format("%02d", hour);
        e = String.format("%02d", hour + 1);
        return (s + ":00") + " - " + (e + ":00");
    }

    @Override
    @TargetApi(11)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scheduled_activity);

        mLayout = (LinearLayout) findViewById(R.id.list_item_back);
        mTaskManager = TaskManager.get(getApplicationContext(), true);

        mDateSelected = getIntent().getStringExtra(ListPlannerActivity.EXTRA_DATE_SELECTED);
        String timeSelected = getIntent().getStringExtra(ListPlannerActivity.EXTRA_TIME_SELECTED);
        mName = getIntent().getStringExtra(ListPlannerActivity.EXTRA_TASKNAME);
        mDescription = getIntent().getStringExtra(ListPlannerActivity.EXTRA_DESC);
        mPlace = getIntent().getStringExtra(ListPlannerActivity.EXTRA_PLACE);
        mPriority = getIntent().getStringExtra(ListPlannerActivity.EXTRA_PRIORITY);


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
        mTaskNameText.setText(mName);

        mTaskDescriptionText = (EditText) findViewById(R.id.inputDescription);
        mTaskDescriptionText.setText(mDescription);

        mPlaceText = (EditText) findViewById(R.id.inputPlace);
        mPlaceText.setText(mPlace);

        mPriorityButton = (Button) findViewById(R.id.priority_button);
        mPriorityButton.setText(mPriority);
        mPriorityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String p = mPriorityButton.getText().toString();
                if (p.equals(PRIORITY_HIGH)) {
                    mPriorityButton.setText(PRIORITY_LOW);
                } else if (p.equals(PRIORITY_LOW)) {
                    mPriorityButton.setText(PRIORITY_HIGH);
                } else {
                    mPriorityButton.setText(PRIORITY_LOW);
                }
            }
        });
        mDoneButton = (Button) findViewById(R.id.doneButton);

        if (getIntent().getStringExtra(ACTIVITY_FUNCTION).equals("edit")) {
            mDoneButton.setText("Save Changes");
            mToTimeText.setEnabled(false);
            mDateButton.setEnabled(false);
        } else {
            mDateButton.setEnabled(true);
            mToTimeText.setClickable(true);
            mDoneButton.setText("Done");
        }

        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (areFieldsFilled()) {
                    String name, descr, date, place, priority, time;
                    int interval;
                    int timeStart, timeEnd;
                    boolean hasDuplicate = false;

                    name = mTaskNameText.getText().toString();
                    descr = mTaskDescriptionText.getText().toString();
                    date = mDateButton.getText().toString();
                    timeStart = Integer.parseInt(mTimeStart.substring(0, 2));
                    timeEnd = Integer.parseInt(mTimeEnd.substring(0, 2));
                    interval = (timeStart - timeEnd) * -1;
                    place = mPlaceText.getText().toString();
                    priority = mPriorityButton.getText().toString();


                    if (interval < 0) {
                        mToTimeText.setError("Please set the time forward this is not a time machine:)");
                        return;
                    }

                    if (getIntent().getStringExtra(ACTIVITY_FUNCTION).equals("edit")) {
                        mTaskManager.updateTasks(name, descr, getFormattedTime(timeStart), date, place, priority);
                        finish();
                        return;
                    }

                    for (int i = timeStart; i < timeEnd; i++) {
                        try {
                            hasDuplicate = mTaskManager.hasTasks(date, getFormattedTime(i));
                            if (hasDuplicate)
                                break;
                        } catch (Exception e) {
                            Log.e("Planner", "Something went wrong!", e);
                        }
                    }

                    if (hasDuplicate) {
                        showOverwriteDialog(name, descr, place, priority, date, timeStart, timeEnd);
                    } else {
                        for (int i = timeStart; i < timeEnd; i++) {
                            saveTasks(date, name, descr, place, priority, getFormattedTime(i));
                        }
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "All fields are required.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void saveTasks(String date, String name, String descr, String place, String priority, String time) {
        Tasks task = new Tasks();
        task.setDate(date);
        task.setTaskName(name);
        task.setDescription(descr);
        task.setPlace(place);
        task.setPriority(priority);
        task.setTimeSlot(time);
        mTaskManager.saveTask(task);
        finish();

    }

    private void showDatePicker() {
        FragmentManager fm = getSupportFragmentManager();
        DatePickerDialog dialog = DatePickerDialog.newInstance(mDateButton.getText().toString(), FUNCTION_FORCHANGE);
        dialog.show(fm, "datePicker");
    }

    private void showTimePicker() {
        FragmentManager fm = getSupportFragmentManager();
        TimePickerDialog dialog = TimePickerDialog.newInstance(mToTimeText.getText().toString());
        dialog.show(fm, "timePicker");
    }

    private void showOverwriteDialog(String name, String descr, String place, String priority, String date, int start, int end) {
        FragmentManager fm = getSupportFragmentManager();
        OverwriteDialog dialog = OverwriteDialog.newInstance(name, descr, place, priority, date, start, end);
        dialog.show(fm, "overWrite");
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

    @Override
    public void onJumpTo(String date) {
        //Not used in this Activity
    }

    private boolean areFieldsFilled() {
        return ((isNotEmpty(mTaskNameText) &
                isNotEmpty(mTaskDescriptionText) &
                isNotEmpty(mPlaceText))
        );
    }

    private boolean isNotEmpty(EditText item) {
        boolean returnValue = true;
        if (TextUtils.isEmpty(item.getText().toString())) {
            item.setError("This item must not be Empty");
            returnValue = false;
        }
        return returnValue;
    }


    @Override
    public void onOverwrite(String name, String descr, String place, String priority, int start, int end, String date) {
        for (int i = start; i < end; i++) {
            try {
                if (mTaskManager.hasTasks(date, getFormattedTime(i))) {
                    mTaskManager.updateTasks(name, descr, getFormattedTime(i), date, place, priority);
                } else {
                    saveTasks(date, name, descr, place, priority, getFormattedTime(i));
                }
            } catch (Exception e) {
                Log.e("Planner", "Something went wrong!", e);
            }
        }
        Log.d("Planner", "Update Done");
    }
}
