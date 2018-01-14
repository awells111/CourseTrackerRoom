package com.android.awells.coursetrackerroom;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.awells.coursetrackerroom.data.CourseTrackerDatabase;
import com.android.awells.coursetrackerroom.data.Term;
import com.android.awells.coursetrackerroom.date.DatePickerFragment;

import java.util.Calendar;

import static com.android.awells.coursetrackerroom.date.DatePickerFragment.DATE_FORMAT;
import static com.android.awells.coursetrackerroom.date.DatePickerFragment.DATE_PICKER_TAG;
import static com.android.awells.coursetrackerroom.date.DatePickerFragment.formatMyDate;

public class AddTermActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private long termStartDate = Long.MIN_VALUE;
    private long termEndDate = Long.MIN_VALUE;
    private String datePickerKey = "";

    private EditText termTitleEditText;
    private TextView termStartView;
    private TextView termEndView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_term);

        termTitleEditText = findViewById(R.id.add_term_title);
        termStartView = findViewById(R.id.add_term_start);
        termEndView = findViewById(R.id.add_term_end);

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
                    setResult(RESULT_OK); // Let MainActivity know it needs to update the UI
                    finish();
                } else { // If data is invalid, notify user with a Toast
                    Toast.makeText(AddTermActivity.this, getString(R.string.enter_valid_data), Toast.LENGTH_LONG).show();
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
            termStartDate = c.getTimeInMillis();
            updateDateDisplay();
        } else if (datePickerKey.equals(getString(R.string.end_date))) { // If end date view was set
            termEndDate = c.getTimeInMillis();
            updateDateDisplay();
        }

        datePickerKey = "";
    }

    private void updateDateDisplay() {
        if (termStartDate == Long.MIN_VALUE) {
            termStartView.setText(R.string.not_set);
        } else {
            termStartView.setText(formatMyDate(termStartDate));
        }

        if (termEndDate == Long.MIN_VALUE) {
            termEndView.setText(R.string.not_set);
        } else {
            termEndView.setText(formatMyDate(termEndDate));
        }
    }

    private void saveItem() {
        //Create Term from fields
        Term term = new Term();
        term.setTitle(termTitleEditText.getText().toString());
        term.setStartDate(termStartDate);
        term.setEndDate(termEndDate);

        //Insert Term into database
        CourseTrackerDatabase.getInstance(getApplicationContext()).term().insert(term);
    }

    private void setOnClickListeners() {
        termStartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerKey = getString(R.string.start_date);
                DatePickerFragment dialogFragment = new DatePickerFragment();
                dialogFragment.show(getSupportFragmentManager(), DATE_PICKER_TAG);
            }
        });

        termEndView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerKey = getString(R.string.end_date);
                DatePickerFragment dialogFragment = new DatePickerFragment();
                dialogFragment.show(getSupportFragmentManager(), DATE_PICKER_TAG);
            }
        });
    }

    private boolean isValidData() {
        if (termTitleEditText.getText().toString().equals("")) { //If title is not set
            return false;
        } else if (termStartDate == Long.MIN_VALUE) { //If start date is not set
            return false;
        } else if (termEndDate == Long.MIN_VALUE) { //If end date is not set
            return false;
        }
        return true;
    }
}
