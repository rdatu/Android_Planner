package rdatu.android.cyscorpions.com.projectplanner.controller.model_managers;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import rdatu.android.cyscorpions.com.projectplanner.model.database_helpers.TasksDatabaseHelper;
import rdatu.android.cyscorpions.com.projectplanner.model.database_helpers.TasksDatabaseHelper.TaskCursor;
import rdatu.android.cyscorpions.com.projectplanner.model.objects.Tasks;

/**
 * Created by rayeldatu on 7/28/15.
 */
public class TaskManager {

    private static TaskManager sTaskManager;
    private Context mAppContext;
    private TasksDatabaseHelper mHelper;
    private ArrayList<Tasks> mTasksLists;
    private Tasks mLoadedTask;

    private TaskManager(Context context) {
        mAppContext = context;
        mHelper = new TasksDatabaseHelper(mAppContext);

        try {
            loadTasks();
        } catch (Exception e) {
            Log.e("Planner", "Something went wrong", e);
        }
    }

    public static TaskManager get(Context c) {
        if (sTaskManager == null) {
            sTaskManager = new TaskManager(c.getApplicationContext());
        }
        return sTaskManager;
    }


    public Tasks getLoadedTask() {
        return mLoadedTask;
    }

    public ArrayList<Tasks> getTasksForDate(String date) {
        TaskCursor cursor = mHelper.queryTaskForDate(date);
        cursor.moveToFirst();
        ArrayList<Tasks> taskList = cursor.getTasks();
        cursor.close();
        return taskList;
    }

    private void loadTasks() {
        TaskCursor cursor = mHelper.queryTask();
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            mTasksLists = cursor.getTasks();
        } else {
            mTasksLists = null;
        }
        cursor.close();
    }

    public void saveTask(Tasks task) {
        mTasksLists.add(task);
        mHelper.insertTasks(task.getTaskName(), task.getDescription(), task.getPlace(), task.getDate(), task.getTimeSlot(), task.getPriority());
    }


    public void deleteEntry(String date, String time) {
        mHelper.deleteWithCondition(date, time);
    }

    public void updateTasks(String name, String descr, String time, String date, String place, String priority) {
        mHelper.updateTask(name, descr, time, date, place, priority);
    }

    public void getSpecificTask(String date, String time) {
        TaskCursor cursor = mHelper.queryTask(date, time);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            mLoadedTask = cursor.getSpecificTask();
        } else {
            mLoadedTask = null;
        }
        cursor.close();
    }

    public boolean hasTasks(String date, String time) {
        boolean returnValue;
        TaskCursor cursor = mHelper.queryTask(date, time);
        Log.d("Planner", cursor.getCount() + " is the size");
        if (cursor.getCount() > 0) {
            returnValue = true;
        } else {
            returnValue = false;
        }
        cursor.close();
        return returnValue;
    }

    public ArrayList<Tasks> getTasksLists() {
        return mTasksLists;
    }
}
