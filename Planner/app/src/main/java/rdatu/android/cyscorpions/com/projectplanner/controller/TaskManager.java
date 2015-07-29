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

    public void loadSingleTask(String date, String fromTime) {
        TaskCursor cursor = mHelper.queryTaskForDate(date, fromTime);
        mLoadedTask = cursor.getSpecificTask();
    }

    public Tasks getLoadedTask() {
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
    }

    public void saveTask(Tasks task) {
        mTasks.add(task);
        mHelper.insertTasks(task.getTaskName(), task.getDescription(), task.getPlace(), task.getDate(), task.getTimeSlot(), task.getPriority());
    }

    public void deleteAllTasks() {
        mHelper.deleteAll();
    }

    public ArrayList<Tasks> getTasks() {
        return mTasks;
    }


}
