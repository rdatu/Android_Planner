package rdatu.android.cyscorpions.com.projectplanner.controller.modelmanagers;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import rdatu.android.cyscorpions.com.projectplanner.model.databasehelpers.TasksDatabaseHelper;
import rdatu.android.cyscorpions.com.projectplanner.model.databasehelpers.TasksDatabaseHelper.TaskCursor;
import rdatu.android.cyscorpions.com.projectplanner.model.objects.Tasks;

/**
 * Created by rayeldatu on 7/28/15.
 */
public class TaskManager {

    private static TaskManager sTaskManager;
    private Context mAppContext;
    private TasksDatabaseHelper mHelper;
    private ArrayList<Tasks> mTasks;
    private Tasks mLoadedTask;

    private TaskManager(Context context, boolean shouldLoadAllTasks) {
        mAppContext = context;
        mHelper = new TasksDatabaseHelper(mAppContext);

        try {
            if (shouldLoadAllTasks) {
                loadTasks();
            } else {

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


    public Tasks getLoadedTask() {
        return mLoadedTask;
    }

    public ArrayList<Tasks> getTasksForDate(String date) {
        TaskCursor cursor = mHelper.queryTaskForDate(date);
        cursor.moveToFirst();
        ArrayList<Tasks> temp = cursor.getTasks();
        cursor.close();
        return temp;
    }

    private void loadTasks() {
        TaskCursor cursor = mHelper.queryTask();
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            mTasks = cursor.getTasks();
        } else {
            mTasks = new ArrayList<Tasks>();
        }
        cursor.close();
    }

    public void saveTask(Tasks task) {
        mTasks.add(task);
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
