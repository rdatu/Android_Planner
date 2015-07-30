package rdatu.android.cyscorpions.com.projectplanner.controller;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import rdatu.android.cyscorpions.com.projectplanner.model.Tasks;
import rdatu.android.cyscorpions.com.projectplanner.model.TasksDatabaseHelper;
import rdatu.android.cyscorpions.com.projectplanner.model.TasksDatabaseHelper.TaskCursor;

/**
 * Created by rayeldatu on 7/28/15.
 */
public class TaskManager {

    private static TaskManager sTaskManager;
    private Context mAppContext;
    private TasksDatabaseHelper mHelper;
    private ArrayList<Tasks> mTasks;
    private Tasks mLoadedTask;

    private TaskManager(Context context, boolean all) {
        mAppContext = context;
        mHelper = new TasksDatabaseHelper(mAppContext);

        try {
            if (all) {
                loadTasks();
            } else {
                //loadSingleTask();
            }

        } catch (Exception e) {
            Log.d("Planner", e.toString());
        }
    }

    public static TaskManager get(Context c, boolean all) {
        if (sTaskManager == null) {
            sTaskManager = new TaskManager(c.getApplicationContext(), all);
        }
        return sTaskManager;
    }

    public void loadSingleTask(String date) {
        TaskCursor cursor = mHelper.queryTaskForDate(date);
        if (cursor.getSpecificTask() != null) {
            mLoadedTask = cursor.getSpecificTask();
            Log.d("Planner", mLoadedTask.toString());
        } else
            mLoadedTask = null;
        cursor.close();
    }

    public Tasks getLoadedTask(String date) {
        loadSingleTask(date);
        return mLoadedTask;
    }

    private void loadTasks() {
        Tasks task = null;
        TaskCursor cursor = mHelper.queryTask();
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            mTasks = cursor.getTasks();
        } else {
            mTasks = new ArrayList<Tasks>();
        }
        cursor.close();
    }

    public void deleteTasks(String date, String time) {
        mHelper.deleteSpecificTask(date, time);
    }

    public void saveTask(Tasks task) {
        mTasks.add(task);
        mHelper.insertTasks(task.getTaskName(), task.getDescription(), task.getPlace(), task.getDate(), task.getTimeSlot(), task.getPriority());
    }

    public void deleteAllTasks() {
        mHelper.deleteAll();
    }

    public void updateTasks(String name, String descr, String time, String date, String place, String priority) {
        mHelper.updateTask(name, descr, time, date, place, priority);
    }

    public boolean checkIfTasksExsists(String date, String time) {
        TaskCursor cursor = mHelper.queryTask(date, time);
        Log.d("Planner", cursor.getCount() + " is the size");
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;

        } else {
            cursor.close();
            return false;
        }

    }


    public ArrayList<Tasks> getTasks() {
        return mTasks;
    }


}
