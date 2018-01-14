package com.android.awells.coursetrackerroom;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.awells.coursetrackerroom.data.Assessment;
import com.android.awells.coursetrackerroom.data.Course;
import com.android.awells.coursetrackerroom.data.CourseTrackerDatabase;
import com.android.awells.coursetrackerroom.date.DatePickerFragment;
import com.android.awells.coursetrackerroom.date.TimePickerFragment;
import com.android.awells.coursetrackerroom.notification.AlarmReceiver;

import java.util.Calendar;

import static com.android.awells.coursetrackerroom.CourseTrackerHelper.getIndex;
import static com.android.awells.coursetrackerroom.MainActivity.CODE_NO_INPUT;
import static com.android.awells.coursetrackerroom.date.DatePickerFragment.DATE_PICKER_TAG;
import static com.android.awells.coursetrackerroom.date.DatePickerFragment.formatMyDate;
import static com.android.awells.coursetrackerroom.date.DatePickerFragment.formatMyDateTime;
import static com.android.awells.coursetrackerroom.date.TimePickerFragment.TIME_PICKER_TAG;
import static com.android.awells.coursetrackerroom.notification.AlarmReceiver.ALARM_RECEIVER_INTENT;
import static com.android.awells.coursetrackerroom.notification.AlarmReceiver.NOTIFICATION_CONTENT_KEY;
import static com.android.awells.coursetrackerroom.notification.AlarmReceiver.NOTIFICATION_TITLE_KEY;

public class AddAssessmentActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    public static final String TAG = AddAssessmentActivity.class.getSimpleName();

    private Assessment assessment;
    private Course course;

    private long courseId;
    private long assessmentId;

    private long assessmentScheduledDate = Long.MIN_VALUE;
    private long assessmentNotificationDate = Long.MIN_VALUE;
    private String datePickerKey = "";

    private TextView assessmentScheduledView;
    private TextView assessmentNotificationView;
    private EditText assessmentScoreEditText;
    private Spinner assessmentTypeView;

    private Calendar c;

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
        loadData();
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
        //Set to midnight on the selected day
        c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        if (datePickerKey.equals(getString(R.string.scheduled_time_colon))) {
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            assessmentScheduledDate = c.getTimeInMillis();
            updateDateDisplay();
        } else if (datePickerKey.equals(getString(R.string.notification_time_colon))) {
            TimePickerFragment timePickerFragment = new TimePickerFragment();
            timePickerFragment.show(getSupportFragmentManager(), TIME_PICKER_TAG);
        }

        datePickerKey = "";
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);

        assessmentNotificationDate = c.getTimeInMillis(); //Set notification time

        if (assessmentNotificationDate < Calendar.getInstance().getTimeInMillis()) { //If notification time is earlier than now, reset it
            assessmentNotificationDate = Long.MIN_VALUE;
        }

        updateDateDisplay();
        setAlarm();
    }

    private void loadData() {
        course = CourseTrackerDatabase.getInstance(getApplicationContext()).course().selectByCourseId(courseId);

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
            assessmentScheduledView.setText(formatMyDate(assessmentScheduledDate));
        }

        if (assessmentNotificationDate == Long.MIN_VALUE) {
            assessmentNotificationView.setText(R.string.not_set);
        } else {
            assessmentNotificationView.setText(formatMyDateTime(assessmentNotificationDate));
        }
    }

    private void saveItem() {
        // Create assessment from fields
        assessment.setScheduledTime(assessmentScheduledDate);
        assessment.setStartNotification(assessmentNotificationDate);
        assessment.setScore(Integer.parseInt(assessmentScoreEditText.getText().toString()));
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

    private void setAlarm() {
        if (assessmentNotificationDate > Calendar.getInstance().getTimeInMillis()) { //If the notification date is later than now

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, AlarmReceiver.class);
            intent.putExtra(ALARM_RECEIVER_INTENT, ALARM_RECEIVER_INTENT);
            intent.putExtra(NOTIFICATION_TITLE_KEY, course.getTitle());
            intent.putExtra(NOTIFICATION_CONTENT_KEY, course.getTitle() + " " + getString(R.string.assessment_time_notification));
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
            alarmManager.set(AlarmManager.RTC_WAKEUP, assessmentNotificationDate, pendingIntent);
        } else { //Else cancel the alarm.
            //todo cancel alarm
        }
    }
}
