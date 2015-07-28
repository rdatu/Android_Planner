package rdatu.android.cyscorpions.com.projectplanner.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by rayeldatu on 7/28/15.
 */
public class TasksDatabaseHelper extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "task.sqlite";
    private static String TABLE_TASKS = "tasks";
    private static String COLUMN_TASK_ID = "_id";
    private static String COLUMN_TASK_NAME = "task_name";
    private static String COLUMN_TASK_DESC = "task_description";
    private static String COLUMN_PLACE = "task_place";
    private static String COLUMN_DATE = "task_date";
    private static String COLUMN_TIMESLOT = "task_time";
    private static String COLUMN_PRIORITY = "task_priority";

    public TasksDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table tasks (_id integer primary key autoincrement, task_date varchar(30),task_time varchar(15), task_name varchar(100), task_description varchar(200), task_place varchar(100),task_priority varchar(10))");
    }

    public long insertTasks(String name, String desc, String place, String date, String time, String priority) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_DATE, date);
        cv.put(COLUMN_TIMESLOT, time);
        cv.put(COLUMN_TASK_NAME, name);
        cv.put(COLUMN_TASK_DESC, desc);
        cv.put(COLUMN_PLACE, place);
        cv.put(COLUMN_PRIORITY, priority);
        return getWritableDatabase().insert(TABLE_TASKS, null, cv);
    }

    public TaskCursor queryTask(String date, String time) {
        Cursor wrapped = getReadableDatabase().query(TABLE_TASKS, null, COLUMN_DATE + " = ? AND " + COLUMN_TIMESLOT + " = ?", new String[]{date, time}, null, null, null);
        return new TaskCursor(wrapped);
    }

    public TaskCursor queryTask() {
        Cursor wrapped = getReadableDatabase().query(TABLE_TASKS, null, null, null, null, null, null);
        return new TaskCursor(wrapped);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public class TaskCursor extends CursorWrapper {


        public TaskCursor(Cursor cursor) {
            super(cursor);
        }

        public Tasks getSpecificTask() {
            if (isBeforeFirst() || isAfterLast()) {
                return null;
            }
            Tasks task = new Tasks();
            task.setTaskName(getString(getColumnIndex(COLUMN_TASK_NAME)));
            task.setDescription(getString(getColumnIndex(COLUMN_TASK_DESC)));
            task.setPlace(getString(getColumnIndex(COLUMN_PLACE)));
            task.setDate(getString(getColumnIndex(COLUMN_DATE)));
            task.setTimeSlot(getString(getColumnIndex(COLUMN_TIMESLOT)));
            task.setPriority(getString(getColumnIndex(COLUMN_PRIORITY)));
            return task;
        }

        public ArrayList<Tasks> getTasks() {
            if (isBeforeFirst() || isAfterLast()) {
                return null;
            }
            ArrayList<Tasks> arrTasks = new ArrayList<>();
            while (!isAfterLast()) {
                Tasks task = new Tasks();
                task.setTaskName(getString(getColumnIndex(COLUMN_TASK_NAME)));
                task.setDescription(getString(getColumnIndex(COLUMN_TASK_DESC)));
                task.setPlace(getString(getColumnIndex(COLUMN_PLACE)));
                task.setDate(getString(getColumnIndex(COLUMN_DATE)));
                task.setTimeSlot(getString(getColumnIndex(COLUMN_TIMESLOT)));
                task.setPriority(getString(getColumnIndex(COLUMN_PRIORITY)));
                arrTasks.add(task);
                moveToNext();
            }
            return arrTasks;
        }
    }
}


