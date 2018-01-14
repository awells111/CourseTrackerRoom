package com.android.awells.coursetrackerroom;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
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
import static com.android.awells.coursetrackerroom.date.DatePickerFragment.formatMyDateTime;
import static com.android.awells.coursetrackerroom.date.TimePickerFragment.TIME_PICKER_TAG;
import static com.android.awells.coursetrackerroom.notification.AlarmReceiver.ALARM_RECEIVER_INTENT;
import static com.android.awells.coursetrackerroom.notification.AlarmReceiver.ALARM_RECEIVER_REQUEST;
import static com.android.awells.coursetrackerroom.notification.AlarmReceiver.NOTIFICATION_CONTENT_KEY;
import static com.android.awells.coursetrackerroom.notification.AlarmReceiver.NOTIFICATION_TITLE_KEY;
import static com.android.awells.coursetrackerroom.notification.AlarmReceiver.cancelAlarm;

public class AddAssessmentActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    private Assessment assessment;
    private Course course;

    private long courseId;
    private long assessmentId;

    private long assessmentScheduledDate = Long.MIN_VALUE;
    private boolean assessmentNotification = false;

    private TextView assessmentScheduledView;
    private TextView assessmentNotificationView;
    private EditText assessmentScoreEditText;
    private Spinner assessmentTypeView;
    private Switch assessmentNotificationSwitch;

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
        assessmentNotificationSwitch = findViewById(R.id.add_assessment_switch);

        setOnClickListeners();
        loadData();
        updateUI();
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

        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.show(getSupportFragmentManager(), TIME_PICKER_TAG);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);

        assessmentScheduledDate = c.getTimeInMillis(); //Set notification time

        updateUI();
    }

    private void loadData() {
        course = CourseTrackerDatabase.getInstance(getApplicationContext()).course().selectByCourseId(courseId);

        if (!isNewAssessment()) {
            assessment = CourseTrackerDatabase.getInstance(getApplicationContext()).assessment().selectByAssessmentId(assessmentId);

            assessmentScheduledDate = assessment.getScheduledTime();

            assessmentNotification = assessment.isStartNotification();

            if (assessment.getScore() >= 0) {
                assessmentScoreEditText.setText(Integer.toString(assessment.getScore()));
            }

            assessmentTypeView.setSelection(getIndex(assessmentTypeView, assessment.getType()));
        } else {
            assessment = new Assessment();
            assessment.setCourseId(courseId);
        }
    }

    private void updateUI() {
        if (assessmentScheduledDate == Long.MIN_VALUE) {
            assessmentScheduledView.setText(R.string.not_set);
        } else {
            assessmentScheduledView.setText(formatMyDateTime(assessmentScheduledDate));
        }

        if (assessmentScheduledDate < Calendar.getInstance().getTimeInMillis()) {
            assessmentNotificationView.setText(R.string.disabled);
            assessmentNotification = false;
            assessmentNotificationSwitch.setChecked(false);
            assessmentNotificationSwitch.setEnabled(false);
        } else {
            assessmentNotificationSwitch.setEnabled(true);
            if (assessmentNotification) {
                assessmentNotificationView.setText(R.string.enabled);
                assessmentNotificationSwitch.setChecked(true);
            }
        }

    }

    private void saveItem() {
        // Create assessment from fields
        assessment.setScheduledTime(assessmentScheduledDate);
        assessment.setStartNotification(assessmentNotification);
        assessment.setType(assessmentTypeView.getSelectedItem().toString());

        if (assessmentScoreEditText.getText().toString().equals("")) {
            assessment.setScore(-1);
        } else {
            assessment.setScore(Integer.parseInt(assessmentScoreEditText.getText().toString()));
        }

        //Insert Assessment into database
        if (isNewAssessment()) {
            assessmentId = CourseTrackerDatabase.getInstance(getApplicationContext()).assessment().insert(assessment);
        } else {
            CourseTrackerDatabase.getInstance(getApplicationContext()).assessment().update(assessment);
        }

        setAlarm();
    }

    private void setOnClickListeners() {
        assessmentScheduledView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment dialogFragment = new DatePickerFragment();
                dialogFragment.show(getSupportFragmentManager(), DATE_PICKER_TAG);
            }
        });

        assessmentNotificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton assessmentNotificationSwitch, boolean isChecked) {
                assessmentNotification = isChecked;
                updateUI();
            }
        });
    }

    private boolean isValidData() {
        return true;
    }

    private boolean isNewAssessment() { //Checks if we are creating a new course or editing the an one
        return assessmentId == CODE_NO_INPUT;
    }

    private void setAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra(ALARM_RECEIVER_INTENT, ALARM_RECEIVER_INTENT);
        intent.putExtra(ALARM_RECEIVER_REQUEST, Assessment.ALARM_REQUEST_CODE + assessmentId);
        if (assessmentNotification) { //Set the alarm
            intent.putExtra(NOTIFICATION_TITLE_KEY, course.getTitle());
            intent.putExtra(NOTIFICATION_CONTENT_KEY, course.getTitle() + " " + getString(R.string.assessment_time_notification));
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int) (long) (Assessment.ALARM_REQUEST_CODE + assessmentId), intent, 0);
            alarmManager.set(AlarmManager.RTC_WAKEUP, assessmentScheduledDate, pendingIntent);
        } else { //Cancel the alarm.
            cancelAlarm(this, intent);
        }
    }
}
