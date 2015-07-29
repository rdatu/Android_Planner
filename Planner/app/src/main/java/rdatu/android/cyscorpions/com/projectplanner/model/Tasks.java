package rdatu.android.cyscorpions.com.projectplanner.model;

import java.util.UUID;

/**
 * Created by rayeldatu on 7/27/15.
 */
public class Tasks {
    private UUID mId;
    private String mTaskName, mDescription, mTimeSlot, mDate, mPlace, mPriority;

    public Tasks() {
        mId = UUID.randomUUID();
    }

    public UUID getId() {
        return mId;
    }

    public String getPriority() {
        return mPriority;
    }

    public void setPriority(String priority) {
        mPriority = priority;
    }

    public String getTaskName() {
        return mTaskName;
    }

    public void setTaskName(String taskName) {
        mTaskName = taskName;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getTimeSlot() {
        return mTimeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        mTimeSlot = timeSlot;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getPlace() {
        return mPlace;
    }

    public void setPlace(String place) {
        mPlace = place;
    }

    @Override
    public String toString() {
        return mTaskName;
    }
}
