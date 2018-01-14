package com.android.awells.coursetrackerroom;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.awells.coursetrackerroom.data.Course;
import com.android.awells.coursetrackerroom.data.CourseMentor;
import com.android.awells.coursetrackerroom.data.CourseTrackerDatabase;
import com.android.awells.coursetrackerroom.data.Term;
import com.android.awells.coursetrackerroom.date.DatePickerFragment;

import java.util.Calendar;

import static com.android.awells.coursetrackerroom.CourseTrackerHelper.getIndex;
import static com.android.awells.coursetrackerroom.date.DatePickerFragment.DATE_FORMAT;
import static com.android.awells.coursetrackerroom.date.DatePickerFragment.DATE_PICKER_TAG;
import static com.android.awells.coursetrackerroom.MainActivity.CODE_NO_INPUT;
import static com.android.awells.coursetrackerroom.date.DatePickerFragment.formatMyDate;

public class AddCourseActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private Course course;

    private long courseId; //Used to see if we are editing an old course or adding a new one
    private long termId;

    private long courseStartDate = Long.MIN_VALUE;
    private long courseEndDate = Long.MIN_VALUE;
    private String datePickerKey = "";

    private EditText courseTitleEditText;
    private TextView courseStartView;
    private TextView courseEndView;
    private Spinner courseStatusView;
    private EditText courseMentorNameView;
    private EditText courseMentorPhoneView;
    private EditText courseMentorEmailView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        courseId = getIntent().getLongExtra(Course.COLUMN_ID, CODE_NO_INPUT); //Are we adding a new course?
        termId = getIntent().getLongExtra(Term.COLUMN_ID, CODE_NO_INPUT);

        courseTitleEditText = findViewById(R.id.add_course_title);
        courseStartView = findViewById(R.id.add_course_start);
        courseEndView = findViewById(R.id.add_course_end);
        courseStatusView = findViewById(R.id.add_course_status);
        courseMentorNameView = findViewById(R.id.add_course_mentor_name);
        courseMentorPhoneView = findViewById(R.id.add_course_mentor_phone);
        courseMentorEmailView = findViewById(R.id.add_course_mentor_email);

        setOnClickListeners();
        loadCourseInfo();
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
                    setResult(RESULT_OK); // Let TermDetailActivity know it needs to update the UI
                    finish();
                } else { // If data is invalid, notify user with a Toast
                    Toast.makeText(AddCourseActivity.this, getString(R.string.enter_valid_data), Toast.LENGTH_LONG).show();
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        //Set to midnight on the selected day
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        if (datePickerKey.equals(getString(R.string.start_date))) { // If start date view was set
            courseStartDate = c.getTimeInMillis();
            updateDateDisplay();
        } else if (datePickerKey.equals(getString(R.string.end_date))) { // If end date view was set
            courseEndDate = c.getTimeInMillis();
            updateDateDisplay();
        }

        datePickerKey = "";
    }

    private void loadCourseInfo() { //Use only for existing courses, not new ones
        if (!isNewCourse()) { //If we are editing a course
            course = CourseTrackerDatabase.getInstance(getApplicationContext()).course().selectByCourseId(courseId); //Retrieve the course

            //Populate all fields with the course info
            courseTitleEditText.setText(course.getTitle()); //Set the title

            //Set the date fields
            courseStartDate = course.getStartDate();
            courseEndDate = course.getEndDate();

            courseStatusView.setSelection(getIndex(courseStatusView, course.getStatus())); //Set the course status

            //Set CourseMentor info
            CourseMentor courseMentor = course.getCourseMentor();
            courseMentorNameView.setText(courseMentor.getName());
            courseMentorPhoneView.setText(courseMentor.getPhoneNumber());
            courseMentorEmailView.setText(courseMentor.getEmail());
        } else {
            course = new Course();
            course.setTermId(termId);
        }
    }

    private void updateDateDisplay() {
        if (courseStartDate == Long.MIN_VALUE) {
            courseStartView.setText(R.string.not_set);
        } else {
            courseStartView.setText(formatMyDate(courseStartDate));
        }

        if (courseEndDate == Long.MIN_VALUE) {
            courseEndView.setText(R.string.not_set);
        } else {
            courseEndView.setText(formatMyDate(courseEndDate));
        }
    }

    private void saveItem() {
        //Create Course from fields
        course.setTitle(courseTitleEditText.getText().toString());
        course.setStatus(courseStatusView.getSelectedItem().toString());
        course.setStartDate(courseStartDate);
        course.setEndDate(courseEndDate);

        //Create CourseMentor from fields
        CourseMentor courseMentor = new CourseMentor();
        courseMentor.setName(courseMentorNameView.getText().toString());
        courseMentor.setPhoneNumber(courseMentorPhoneView.getText().toString());
        courseMentor.setEmail(courseMentorEmailView.getText().toString());
        course.setCourseMentor(courseMentor);

        //Insert Course into database
        if (isNewCourse()) {
            courseId = CourseTrackerDatabase.getInstance(getApplicationContext()).course().insert(course);
        } else {
            CourseTrackerDatabase.getInstance(getApplicationContext()).course().update(course);
        }
    }

    private void setOnClickListeners() {
        courseStartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerKey = getString(R.string.start_date);
                DatePickerFragment dialogFragment = new DatePickerFragment();
                dialogFragment.show(getSupportFragmentManager(), DATE_PICKER_TAG);
            }
        });

        courseEndView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerKey = getString(R.string.end_date);
                DatePickerFragment dialogFragment = new DatePickerFragment();
                dialogFragment.show(getSupportFragmentManager(), DATE_PICKER_TAG);
            }
        });
    }

    private boolean isValidData() {
        if (courseTitleEditText.getText().toString().equals("")) { //If title is not set
            return false;
        } else if (courseStartDate == Long.MIN_VALUE) { //If start date is not set
            return false;
        } else if (courseEndDate == Long.MIN_VALUE) { //If end date is not set
            return false;
        } else if (courseMentorNameView.getText().toString().equals("")) { //If mentor name is not set
            return false;
        } else if (courseMentorPhoneView.getText().toString().equals("")) { //If mentor phone is not set
            return false;
        } else if (courseMentorEmailView.getText().toString().equals("")) { //If mentor email is not set
            return false;
        }
        return true;
    }

    private boolean isNewCourse() { //Checks if we are creating a new course or editing the an one
        return courseId == CODE_NO_INPUT;
    }
}