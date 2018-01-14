package com.android.awells.coursetrackerroom;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.awells.coursetrackerroom.data.Assessment;
import com.android.awells.coursetrackerroom.data.Course;
import com.android.awells.coursetrackerroom.data.CourseTrackerDatabase;
import com.android.awells.coursetrackerroom.data.Note;
import com.android.awells.coursetrackerroom.data.Term;

import java.util.Calendar;

import static com.android.awells.coursetrackerroom.MainActivity.CODE_NO_INPUT;
import static com.android.awells.coursetrackerroom.date.DatePickerFragment.formatMyDateTime;

public class AssessmentDetailActivity extends AppCompatActivity {

    private static final String TAG = AssessmentDetailActivity.class.getSimpleName();

    private long assessmentId;
    private long courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_detail);

        assessmentId = getIntent().getLongExtra(Assessment.COLUMN_ID, CODE_NO_INPUT);
        courseId = getIntent().getLongExtra(Course.COLUMN_ID, CODE_NO_INPUT);

        updateUI();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK); // Let CourseDetailActivity know it needs to update the UI
            updateUI();
        }
    }

    private void updateUI() {
        String courseTitle = CourseTrackerDatabase.getInstance(getApplicationContext()).course().selectByCourseId(courseId).getTitle();
        Assessment assessment = CourseTrackerDatabase.getInstance(getApplicationContext()).assessment().selectByAssessmentId(assessmentId);
        String notSet = getString(R.string.not_set);

        this.setTitle(courseTitle + " " + assessment.getType());

        TextView scoreView = findViewById(R.id.assessment_score_detail);
        TextView scheduledTimeView = findViewById(R.id.assessment_scheduled_time_detail);
        TextView notificationTimeView = findViewById(R.id.assessment_notification_time_detail);

        if (assessment.getScore() < 0) { //If assessment is not taken
            scoreView.setText(notSet);
        } else {
            scoreView.setText(Integer.toString(assessment.getScore()));
        }

        if (assessment.getScheduledTime() == Long.MIN_VALUE) { //If assessment is not scheduled
            scheduledTimeView.setText(notSet);
        } else {
            scheduledTimeView.setText(formatMyDateTime(assessment.getScheduledTime()));
        }

        //If assessment notification is not set OR the notification time is passed, meaning it has already activated
        if (!assessment.isStartNotification() || assessment.getScheduledTime() < Calendar.getInstance().getTimeInMillis()) {
            notificationTimeView.setText(getString(R.string.disabled));
        } else {
           notificationTimeView.setText(getString(R.string.enabled));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_assessment_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_edit_assessment:
                Intent editIntent = new Intent(AssessmentDetailActivity.this, AddAssessmentActivity.class);
                editIntent.putExtra(Assessment.COLUMN_ID, assessmentId);
                editIntent.putExtra(Course.COLUMN_ID, courseId);
                startActivityForResult(editIntent, 1);
                return true;
            case R.id.action_delete_assessment:
                int count = CourseTrackerDatabase.getInstance(getApplicationContext()).assessment().deleteById(assessmentId);
                Log.d(TAG, "Deleted " + count + " course(s)");
                setResult(RESULT_OK); // Let CourseDetailActivity know it needs to update the UI
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
