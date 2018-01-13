package com.android.awells.coursetrackerroom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.android.awells.coursetrackerroom.data.Course;
import com.android.awells.coursetrackerroom.data.CourseMentor;
import com.android.awells.coursetrackerroom.data.CourseTrackerDatabase;
import com.android.awells.coursetrackerroom.data.Note;

import static com.android.awells.coursetrackerroom.MainActivity.CODE_NO_INPUT;

public class AddNoteActivity extends AppCompatActivity {

    private long courseId;

    private EditText noteEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        courseId = getIntent().getLongExtra(Course.COLUMN_ID, CODE_NO_INPUT);

        noteEditText = findViewById(R.id.add_course_note);
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
                    Toast.makeText(AddNoteActivity.this, getString(R.string.enter_valid_data), Toast.LENGTH_LONG).show();
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveItem() {
        //Create Note from fields
        Note note = new Note();
        note.setText(noteEditText.getText().toString());
        note.setCourseId(courseId);

        //Insert Course into database
        CourseTrackerDatabase.getInstance(getApplicationContext()).note().insert(note);
    }

    private boolean isValidData() {
        return !noteEditText.getText().toString().equals("");
    }

}
