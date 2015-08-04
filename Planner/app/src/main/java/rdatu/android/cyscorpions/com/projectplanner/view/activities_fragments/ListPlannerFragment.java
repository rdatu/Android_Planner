package rdatu.android.cyscorpions.com.projectplanner.view.activities_fragments;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import rdatu.android.cyscorpions.com.projectplanner.R;
import rdatu.android.cyscorpions.com.projectplanner.controller.model_managers.TaskManager;
import rdatu.android.cyscorpions.com.projectplanner.model.objects.Tasks;
import rdatu.android.cyscorpions.com.projectplanner.view.dialogs.DatePickerDialog;

/**
 * Created by rayeldatu on 7/27/15.
 */
public class ListPlannerFragment extends ListFragment implements DatePickerDialog.Callbacks {

    public static final String DIALOG_FUNCTION = "jumpto";
    private final String[] TIME_SLOT = {
            "00:00 - 01:00", "01:00 - 02:00",
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
    private Callbacks mCallbacks;
    private Context mAppContext;
    private ArrayList<Tasks> mListTasks;

    public ListPlannerFragment() {
        //Blank default Constructor is required, Don't Remove
    }

    public ListPlannerFragment(Calendar calendar, Context c) {
        mCalendar = calendar;
        mAppContext = c;
        mTaskManager = TaskManager.get(mAppContext);
    }

    public static ListPlannerFragment newInstance(Calendar calendar, Context c) {
        return new ListPlannerFragment(calendar, c);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks) activity;
        mCallbacks.onListUpdate(getStringDate());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        mListTasks = new ArrayList<>();
        mListTasks = mTaskManager.getTasksLists();
        ListFragmentAdapter adapter = new ListFragmentAdapter(TIME_SLOT);
        adapter.setNotifyOnChange(true);
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        Tasks t = mTaskManager.getLoadedTask();

        if (t == null)
            return;

        getActivity().getMenuInflater().inflate(R.menu.context_menu, menu);
        mTaskManager.getSpecificTask(getActivity().getTitle().toString(), TIME_SLOT[info.position]);

        menu.setHeaderTitle(TIME_SLOT[info.position]);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;

        switch (item.getItemId()) {
            case R.id.menu_item_delete:
                mTaskManager.deleteEntry(getActivity().getTitle().toString(), TIME_SLOT[position]);
                getListView().invalidateViews();
        }
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
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

        try {
            getListView().invalidateViews();
        } catch (Exception e) {

        }
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
            Log.e("Planner", "Something went wrong", e);
        }
        try {
            getListView().invalidateViews();
        } catch (Exception e) {
            Log.e("Planner", "Something went wrong", e);
        }
    }

    private String getStringDate() {
        SimpleDateFormat df = new SimpleDateFormat("MMM-dd-yyyy");
        return df.format(mCalendar.getTime());
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        String name, place, descr, priority, task;
        Tasks tasks;
        String time = ((TextView) v.findViewById(R.id.time_slot)).getText().toString();
        mTaskManager.getSpecificTask(getActivity().getTitle().toString(), time);
        tasks = mTaskManager.getLoadedTask();
        if (tasks != null) {
            name = tasks.getTaskName();
            place = tasks.getPlace();
            descr = tasks.getDescription();
            priority = tasks.getPriority();
            task = "edit";
        } else {
            name = "";
            place = "";
            descr = "";
            priority = "LOW";
            task = "add";
        }
        mCallbacks.onTimeSlotSelected(time, getActivity().getTitle().toString(), name, place, descr, priority, task);
    }

    public void onResume() {
        super.onResume();
        getListView().invalidateViews();
    }

    @Override
    public void onDateChanged(String date) {
        //Not Used implemented cannot be removed
    }

    @Override
    public void onJumpTo(String date) {
        //Not Used implemented cannot be removed
    }

    public interface Callbacks {
        void onListUpdate(String date);

        void onTimeSlotSelected(String time, String date, String name, String place, String descr, String priority, String task);
    }

    static class ViewHolder {
        TextView timeSlotText, taskText, descriptionText;
        LinearLayout linearLayout;
    }

    private class ListFragmentAdapter extends ArrayAdapter<String> {
        public ListFragmentAdapter(String[] time_slot) {
            super(getActivity(), android.R.layout.simple_list_item_1, time_slot);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;

            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.daily_list_item, null);
                viewHolder = new ViewHolder();
                viewHolder.timeSlotText = (TextView) convertView.findViewById(R.id.time_slot);
                viewHolder.taskText = (TextView) convertView.findViewById(R.id.task_name);
                viewHolder.descriptionText = (TextView) convertView.findViewById(R.id.task_description);
                viewHolder.linearLayout = (LinearLayout) convertView.findViewById(R.id.list_item_back);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            String time = getItem(position);

            viewHolder.timeSlotText.setText(time);
            viewHolder.taskText.setText(getString(R.string.default_task_text));
            viewHolder.descriptionText.setText("No Description");
            viewHolder.linearLayout.setBackgroundColor(Color.WHITE);
            viewHolder.taskText.setTextColor(Color.DKGRAY);
            viewHolder.descriptionText.setTextColor(Color.DKGRAY);

            mListTasks = mTaskManager.getTasksForDate(getActivity().getTitle().toString());
            try {
                viewHolder.taskText.setText(getString(R.string.default_task_text));
                if (mListTasks == null) {
                    return convertView;
                }
                for (Tasks t : mListTasks) {
                    if (t.getDate().equals(getActivity().getTitle().toString())) {
                        if (t.getTimeSlot().equals(viewHolder.timeSlotText.getText().toString())) {
                            viewHolder.taskText.setText(t.getTaskName());
                            viewHolder.descriptionText.setText(t.getDescription());

                            int color = (t.getPriority().equals("HIGH")) ? Color.RED : Color.GREEN;
                            viewHolder.linearLayout.setBackgroundColor(color);
                            break;
                        } else {
                            viewHolder.taskText.setText(getString(R.string.default_task_text));
                        }
                    }
                }
            } catch (Exception e) {
                Log.e("Planner", "Error", e);
            }
            return convertView;
        }
    }
}
