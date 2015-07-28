package rdatu.android.cyscorpions.com.projectplanner.controller;

import android.content.Context;

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

    private TaskManager(Context context) {
        mAppContext = context;
        mHelper = new TasksDatabaseHelper(mAppContext);

        try {
            loadTasks();
        } catch (Exception e) {

        }
    }

    public static TaskManager get(Context c) {
        if (sTaskManager == null) {
            sTaskManager = new TaskManager(c.getApplicationContext());
        }
        return sTaskManager;
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
    }

    public ArrayList<Tasks> getTasks() {
        return mTasks;
    }


}
