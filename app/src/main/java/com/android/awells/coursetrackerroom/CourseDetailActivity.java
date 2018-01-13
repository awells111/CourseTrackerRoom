package com.android.awells.coursetrackerroom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.awells.coursetrackerroom.data.Assessment;
import com.android.awells.coursetrackerroom.data.Course;
import com.android.awells.coursetrackerroom.data.CourseTrackerDatabase;
import com.android.awells.coursetrackerroom.data.Term;

import static com.android.awells.coursetrackerroom.MainActivity.CODE_NO_INPUT;

public class CourseDetailActivity extends AppCompatActivity {

    private static String TAG = CourseDetailActivity.class.getSimpleName();

    private long courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        courseId = getIntent().getLongExtra(Course.COLUMN_ID, CODE_NO_INPUT);
        this.setTitle(CourseTrackerDatabase.getInstance(getApplicationContext()).course().selectByCourseId(courseId).getTitle()); //Set the title by retrieving the term

        updateUI();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            updateUI();
        }
    }

    private void updateUI() {
        //todo Show notes
        //todo Show assessments
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_course_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_add_assessment:
                Intent assessmentIntent = new Intent(CourseDetailActivity.this, AddAssessmentActivity.class);
                assessmentIntent.putExtra(Course.COLUMN_ID, CODE_NO_INPUT);
                startActivityForResult(assessmentIntent, 1);
                return true;
            case R.id.action_add_note:
                Intent noteIntent = new Intent(CourseDetailActivity.this, AddNoteActivity.class);
                noteIntent.putExtra(Course.COLUMN_ID, CODE_NO_INPUT);
                startActivityForResult(noteIntent, 1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
