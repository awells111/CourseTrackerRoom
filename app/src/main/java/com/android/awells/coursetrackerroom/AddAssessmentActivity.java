package com.android.awells.coursetrackerroom;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.android.awells.coursetrackerroom.data.Assessment;
import com.android.awells.coursetrackerroom.data.Course;
import com.android.awells.coursetrackerroom.data.CourseMentor;
import com.android.awells.coursetrackerroom.data.CourseTrackerDatabase;

import java.util.Calendar;

import static com.android.awells.coursetrackerroom.DatePickerFragment.DATE_FORMAT;
import static com.android.awells.coursetrackerroom.DatePickerFragment.DATE_PICKER_TAG;

public class AddAssessmentActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private String datePickerKey = "";

    //todo add views

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        //todo set views

        setOnClickListeners();
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
                    setResult(RESULT_OK, null); // Let CourseDetailActivity know it needs to update the UI
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

//        if (datePickerKey.equals(getString(R.string.start_date))) { // If start date view was set todo determine date views
//            courseStartDate = c.getTimeInMillis();
//            updateDateDisplay();
//        } else if (datePickerKey.equals(getString(R.string.end_date))) { // If end date view was set
//            courseEndDate = c.getTimeInMillis();
//            updateDateDisplay();
//        }

        datePickerKey = "";
    }

    private void updateDateDisplay() {
        // todo updateDateDisplay
    }

    private void saveItem() {
        //todo saveItem
        // Create assessment from fields
        Assessment assessment = new Assessment();

        //Insert Assessment into database
        CourseTrackerDatabase.getInstance(getApplicationContext()).assessment().insert(assessment);
    }

    private void setOnClickListeners() {
        //todo setOnClickListeners
    }

    private boolean isValidData() {
        //todo isValidData
        return true;
    }
}
