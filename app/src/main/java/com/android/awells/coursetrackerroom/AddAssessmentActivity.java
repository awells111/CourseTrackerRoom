package com.android.awells.coursetrackerroom;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.awells.coursetrackerroom.data.Assessment;
import com.android.awells.coursetrackerroom.data.Course;
import com.android.awells.coursetrackerroom.data.CourseMentor;
import com.android.awells.coursetrackerroom.data.CourseTrackerDatabase;

import java.util.Calendar;

import static com.android.awells.coursetrackerroom.CourseTrackerHelper.getIndex;
import static com.android.awells.coursetrackerroom.DatePickerFragment.DATE_FORMAT;
import static com.android.awells.coursetrackerroom.DatePickerFragment.DATE_PICKER_TAG;
import static com.android.awells.coursetrackerroom.MainActivity.CODE_NO_INPUT;

public class AddAssessmentActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private Assessment assessment;

    private long courseId;
    private long assessmentId;

    private long assessmentScheduledDate = Long.MIN_VALUE;
    private long assessmentNotificationDate = Long.MIN_VALUE;
    private String datePickerKey = "";

    private TextView assessmentScheduledView;
    private TextView assessmentNotificationView;
    private EditText assessmentScoreEditText;
    private Spinner assessmentTypeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assessment);

        courseId = getIntent().getLongExtra(Course.COLUMN_ID, CODE_NO_INPUT);
        assessmentId = getIntent().getLongExtra(Assessment.COLUMN_ID, CODE_NO_INPUT);

        assessmentScheduledView = findViewById(R.id.add_assessment_scheduled_time);
        assessmentNotificationView = findViewById(R.id.add_assessment_notification_time);
        assessmentScoreEditText = findViewById(R.id.add_assessment_score);
        assessmentTypeView = findViewById(R.id.add_assessment_type);

        setOnClickListeners();
        loadAssessmentInfo();
        updateDateDisplay();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_save:
                if (isValidData()) { // Check for valid inputs
                    saveItem();
                    setResult(RESULT_OK); // Let CourseDetailActivity know it needs to update the UI
                    finish();
                } else { // If data is invalid, notify user with a Toast
                    Toast.makeText(AddAssessmentActivity.this, getString(R.string.enter_valid_data), Toast.LENGTH_LONG).show();
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        //Set to noon on the selected day
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.HOUR_OF_DAY, 12);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        if (datePickerKey.equals(getString(R.string.scheduled_time_colon))) {
            assessmentScheduledDate = c.getTimeInMillis();
            updateDateDisplay();
        } else if (datePickerKey.equals(getString(R.string.notification_time_colon))) {
            assessmentNotificationDate = c.getTimeInMillis();
            updateDateDisplay();
        }

        datePickerKey = "";
    }

    private void loadAssessmentInfo() {
        if (!isNewAssessment()) {
            assessment = CourseTrackerDatabase.getInstance(getApplicationContext()).assessment().selectByAssessmentId(assessmentId);

            assessmentScheduledDate = assessment.getScheduledTime();

            assessmentNotificationDate = assessment.getStartNotification();

            assessmentScoreEditText.setText(Integer.toString(assessment.getScore()));

            assessmentTypeView.setSelection(getIndex(assessmentTypeView, assessment.getType()));
        } else {
            assessment = new Assessment();
            assessment.setCourseId(courseId);
        }
    }

    private void updateDateDisplay() {
        if (assessmentScheduledDate == Long.MIN_VALUE) {
            assessmentScheduledView.setText(R.string.not_set);
        } else {
            CharSequence formatted = DateFormat.format(DATE_FORMAT, assessmentScheduledDate);
            assessmentScheduledView.setText(formatted);
        }

        if (assessmentNotificationDate == Long.MIN_VALUE) {
            assessmentNotificationView.setText(R.string.not_set);
        } else {
            CharSequence formatted = DateFormat.format(DATE_FORMAT, assessmentNotificationDate);
            assessmentNotificationView.setText(formatted);
        }
    }

    private void saveItem() {
        // Create assessment from fields
        assessment.setScheduledTime(assessmentScheduledDate);
        assessment.setStartNotification(assessmentNotificationDate);
        assessment.setScore(Integer.parseInt(assessmentScoreEditText.getText().toString())); //todo See if we need to validate the score
        assessment.setType(assessmentTypeView.getSelectedItem().toString());

        //Insert Assessment into database
        CourseTrackerDatabase.getInstance(getApplicationContext()).assessment().insert(assessment);
    }

    private void setOnClickListeners() {
        assessmentScheduledView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerKey = getString(R.string.scheduled_time_colon);
                DatePickerFragment dialogFragment = new DatePickerFragment();
                dialogFragment.show(getSupportFragmentManager(), DATE_PICKER_TAG);
            }
        });

        assessmentNotificationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerKey = getString(R.string.notification_time_colon);
                DatePickerFragment dialogFragment = new DatePickerFragment();
                dialogFragment.show(getSupportFragmentManager(), DATE_PICKER_TAG);
            }
        });
    }

    private boolean isValidData() {
        //todo isValidData
        return true;
    }

    private boolean isNewAssessment() { //Checks if we are creating a new course or editing the an one
        return assessmentId == CODE_NO_INPUT;
    }
}
