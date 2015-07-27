package rdatu.android.cyscorpions.com.projectplanner.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import rdatu.android.cyscorpions.com.projectplanner.R;

/**
 * Created by rayeldatu on 7/27/15.
 */
public class HomeActivity extends Activity {
    private Button mStartPlannerButton;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        mStartPlannerButton = (Button) findViewById(R.id.startPlannerButton);
        mStartPlannerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ListPlannerActivity.class);
                startActivity(i);
            }
        });


    }
}
