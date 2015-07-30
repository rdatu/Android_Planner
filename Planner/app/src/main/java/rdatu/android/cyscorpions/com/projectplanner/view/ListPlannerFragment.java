package rdatu.android.cyscorpions.com.projectplanner.view;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import rdatu.android.cyscorpions.com.projectplanner.R;
import rdatu.android.cyscorpions.com.projectplanner.controller.TaskManager;
import rdatu.android.cyscorpions.com.projectplanner.model.Tasks;

/**
 * Created by rayeldatu on 7/27/15.
 */
public class ListPlannerFragment extends ListFragment implements DatePickerDialog.Callbacks {

    public static final String FUNCTION_FORCHANGE = "jumpto";
    private static final String TAG = "Planner";
    private final String[] TIME_SLOT = {
            "24:00 - 01:00", "01:00 - 02:00",
            "02:00 - 03:00", "03:00 - 04:00",
            "04:00 - 05:00", "05:00 - 06:00",
            "06:00 - 07:00", "07:00 - 08:00",
            "08:00 - 09:00", "09:00 - 10:00",
            "10:00 - 11:00", "11:00 - 12:00",
            "12:00 - 13:00", "13:00 - 14:00",
            "14:00 - 15:00", "15:00 - 16:00",
            "16:00 - 17:00", "17:00 - 18:00",
            "18:00 - 19:00", "19:00 - 20:00",
            "20:00 - 21:00", "21:00 - 22:00",
            "22:00 - 23:00", "23:00 - 24:00",
    };

    private TaskManager mTaskManager;
    private Calendar mCalendar;
    private TextView mTextTask, mTimeSlot;
    private Callbacks mCallbacks;
    private Context mAppContext;
    private ArrayList<Tasks> mListTasks;


    public ListPlannerFragment() {

    }

    public ListPlannerFragment(Calendar a, Context c) {
        mCalendar = a;
        mAppContext = c;
        mTaskManager = TaskManager.get(mAppContext, true);

    }

    public static ListPlannerFragment newInstance(Calendar a, Context c) {
        return new ListPlannerFragment(a, c);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks) activity;
        mCallbacks.onListUpdate(getStringDate());
        Log.d(TAG, "onAttach successful..." + " : " + (mCalendar == null) + ", " + getStringDate());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        //Log.d("Planner", "Activity: " + getActivity());
        /*PopulateList backgroundTask = new PopulateList(getActivity());
        backgroundTask.execute();*/
        ListFragmentAdapter adapter = new ListFragmentAdapter(TIME_SLOT);
        setListAdapter(adapter);


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.planner_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_refresh:
                ArrayList<Tasks> tasks;
                tasks = mTaskManager.getTasks();
                mTaskManager.deleteAllTasks();
                Toast.makeText(getActivity().getApplicationContext(), "Number of Tasks Deleted: " + tasks.size(), Toast.LENGTH_SHORT).show();
                tasks.clear();
                tasks = mTaskManager.getTasks();
                Toast.makeText(getActivity().getApplicationContext(), "Number of Tasks: " + tasks.size(), Toast.LENGTH_SHORT).show();
                getListView().invalidateViews();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.context_menu, menu);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(TIME_SLOT[info.position]);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        Toast.makeText(getActivity().getApplicationContext(), "Deleted " + position, Toast.LENGTH_SHORT).show();
        return true;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        ListView lv = (ListView) v.findViewById(android.R.id.list);


        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        ListView lv = getListView();
        registerForContextMenu(lv);
        super.onActivityCreated(savedInstanceState);
    }

    protected final void onNextDay() {

        if (mCalendar.get(Calendar.DAY_OF_MONTH) == mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            mCalendar.add(Calendar.MONTH, 1);
            mCalendar.set(Calendar.DAY_OF_MONTH, 1);
        } else {
            mCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        try {
            mCallbacks.onListUpdate(getStringDate());
        } catch (Exception e) {
            Log.e("Planner", "Error", e);
        }

        getListView().invalidateViews();
    }


    protected final void onPreviousDay() {

        if (mCalendar.get(Calendar.DAY_OF_MONTH) == mCalendar.getActualMinimum(Calendar.DAY_OF_MONTH)) {
            mCalendar.add(Calendar.MONTH, -1);
            mCalendar.set(Calendar.DAY_OF_MONTH, mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        } else {
            mCalendar.add(Calendar.DAY_OF_MONTH, -1);
        }

        try {
            mCallbacks.onListUpdate(getStringDate());
        } catch (Exception e) {
            Log.e("Planner", "Error", e);
        }
        getListView().invalidateViews();

    }

    private String getStringDate() {
        SimpleDateFormat df = new SimpleDateFormat("MMM-dd-yyyy");
        return df.format(mCalendar.getTime());
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        String time = ((TextView) v.findViewById(R.id.time_slot)).getText().toString();
        mCallbacks.onTimeSlotSelected(time, getActivity().getTitle().toString());
    }

    public void onResume() {
        super.onResume();
        getListView().invalidateViews();
    }

    @Override
    public void onDateChanged(String date) {
        //Not Used
    }

    @Override
    public void onJumpTo(String date) {

    }

    private void showDatePicker() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        DatePickerDialog dialog = DatePickerDialog.newInstance(getActivity().getTitle().toString(), FUNCTION_FORCHANGE);
        dialog.show(fm, "datePicker");
    }

    public interface Callbacks {
        void onListUpdate(String date);

        void onTimeSlotSelected(String time, String date);
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

            mListTasks = new ArrayList<>();
            mListTasks.clear();
            mListTasks = mTaskManager.getTasks();

            String time = getItem(position);

            mTimeSlot = (TextView) convertView.findViewById(R.id.time_slot);
            mTimeSlot.setText(time);
            mTextTask = (TextView) convertView.findViewById(R.id.task_name);
            mTextTask.setText(getString(R.string.default_task_text));


            try {
                mTextTask.setText(getString(R.string.default_task_text));
                for (Tasks t : mListTasks) {
                    if (t.getDate().equals(getActivity().getTitle().toString())) {
                        if (t.getTimeSlot().equals(mTimeSlot.getText().toString())) {
                            mTextTask.setText(t.getTaskName());

                            break;
                        } else {
                            mTextTask.setText(getString(R.string.default_task_text));
                        }
                    } else {
                        mTextTask.setText(getString(R.string.default_task_text));
                    }
                }
            } catch (Exception e) {
                Log.e("Planner", "Error", e);

            }


            return convertView;
        }
    }
}
