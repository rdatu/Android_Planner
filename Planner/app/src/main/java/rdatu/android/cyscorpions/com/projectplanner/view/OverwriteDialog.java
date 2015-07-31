package rdatu.android.cyscorpions.com.projectplanner.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;


/**
 * Created by rayeldatu on 7/29/15.
 */
public class OverwriteDialog extends DialogFragment implements AlertDialog.OnClickListener {

    private static final String EXTRA_NAME = "name";
    private static final String EXTRA_DESC = "descr";

    private static final String EXTRA_DATE = "date";
    private static final String EXTRA_PLACE = "place";
    private static final String EXTRA_PRIORITY = "priority";
    private static final String EXTRA_START = "start";
    private static final String EXTRA_END = "end";

    private Callbacks mCallbacks;
    private String mName, mDescription, mTime, mDate, mPlace, mPriority;
    private int mStart, mEnd;

    public OverwriteDialog() {

    }

    public static OverwriteDialog newInstance(String name, String descr, String place, String priority, String date, int start, int end) {
        OverwriteDialog fragment = new OverwriteDialog();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_NAME, name);
        args.putSerializable(EXTRA_DESC, descr);
        args.putSerializable(EXTRA_START, start);
        args.putSerializable(EXTRA_END, end);
        args.putSerializable(EXTRA_DATE, date);
        args.putSerializable(EXTRA_PLACE, place);
        args.putSerializable(EXTRA_PRIORITY, priority);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mName = getArguments().getString(EXTRA_NAME);
        mDescription = getArguments().getString(EXTRA_DESC);
        mEnd = getArguments().getInt(EXTRA_END);
        mStart = getArguments().getInt(EXTRA_START);
        mDate = getArguments().getString(EXTRA_DATE);
        mPlace = getArguments().getString(EXTRA_PLACE);
        mPriority = getArguments().getString(EXTRA_PRIORITY);
    }

    @Override
    public void onAttach(Activity a) {
        super.onAttach(a);
        mCallbacks = (Callbacks) a;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle("Too bad ;(").setMessage("Conflicts found, Do you want to Overwrite?").setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mCallbacks.onOverwrite(mName, mDescription, mPlace, mPriority, mStart, mEnd, mDate);
                        getActivity().finish();
                    }
                }).setNegativeButton("NO", null).create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {


    }


    public interface Callbacks {
        void onOverwrite(String name, String descr, String place, String priority, int start, int end, String date);
    }
}
