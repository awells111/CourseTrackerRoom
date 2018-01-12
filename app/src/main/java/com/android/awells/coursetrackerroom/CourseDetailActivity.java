package com.android.awells.coursetrackerroom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.awells.coursetrackerroom.data.Course;
import com.android.awells.coursetrackerroom.data.CourseTrackerDatabase;
import com.android.awells.coursetrackerroom.data.Term;

public class CourseDetailActivity extends AppCompatActivity {

    private static String TAG = CourseDetailActivity.class.getSimpleName();

    private long courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        courseId = getIntent().getLongExtra(Course.COLUMN_ID, -1);
        this.setTitle(CourseTrackerDatabase.getInstance(getApplicationContext()).course().selectByCourseId(courseId).getTitle()); //Set the title by retrieving the term
    }
}
