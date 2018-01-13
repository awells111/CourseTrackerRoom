package com.android.awells.coursetrackerroom;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.awells.coursetrackerroom.data.Course;
import com.android.awells.coursetrackerroom.data.CourseTrackerDatabase;
import com.android.awells.coursetrackerroom.data.Term;

import java.util.List;

import static com.android.awells.coursetrackerroom.DatePickerFragment.formatMyTime;
import static com.android.awells.coursetrackerroom.MainActivity.CODE_NO_INPUT;

public class TermDetailActivity extends AppCompatActivity {

    private static final String TAG = TermDetailActivity.class.getSimpleName();

    private long termId;
    private List<Course> mCourses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail);

        termId = getIntent().getLongExtra(Term.COLUMN_ID, CODE_NO_INPUT); // Retrieve the term's ID from the intent

        updateUI();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // If a course was changed or deleted
        if (resultCode == RESULT_OK) {
            updateUI();
        }
    }

    private void updateUI() {
        Term term = CourseTrackerDatabase.getInstance(getApplicationContext()).term().selectById(termId);
        this.setTitle(term.getTitle());

        TextView startDateView = findViewById(R.id.term_start_date_detail);
        TextView endDateView = findViewById(R.id.term_end_date_detail);

        startDateView.setText(formatMyTime(term.getStartDate()));
        endDateView.setText(formatMyTime(term.getEndDate()));

        final RecyclerView recyclerView = findViewById(R.id.recycler_view_term_detail);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        mCourses = CourseTrackerDatabase.getInstance(getApplicationContext()).course().selectCoursesFromTerm(termId); //Retrieve the list of courses from the database
        CourseAdapter courseAdapter = new CourseAdapter(mCourses);
        courseAdapter.setOnItemClickListener(mOnItemClickListener());
        recyclerView.setAdapter(courseAdapter);
    }

    public CourseAdapter.OnItemClickListener mOnItemClickListener() {
        return new CourseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(TermDetailActivity.this, CourseDetailActivity.class);
                intent.putExtra(Term.COLUMN_ID, termId);
                intent.putExtra(Course.COLUMN_ID, mCourses.get(position).getId());

                startActivityForResult(intent, 1);
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_term_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_add_course:
                Intent intent = new Intent(this, AddCourseActivity.class);
                intent.putExtra(Term.COLUMN_ID, termId); //Input Term ID so we can add a course to it
                startActivityForResult(intent, 1);
                return true;
            case R.id.action_delete_term:
                showDeleteDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void showDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(TermDetailActivity.this);

        builder.setMessage(getText(R.string.delete_term_confirmation))
                .setPositiveButton(getText(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if (mCourses.size() == 0) { //if the term has no courses
                            int count = CourseTrackerDatabase.getInstance(getApplicationContext()).term().deleteById(termId);
                            Log.d(TAG, "Deleted " + count + " term(s)");
                            setResult(RESULT_OK); // Let MainActivity know it needs to update the UI
                            finish();
                        } else { //else if the term still has courses
                            Toast.makeText(TermDetailActivity.this, getString(R.string.delete_courses_first), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton(getText(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    static class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseHolder> {

        public interface OnItemClickListener {
            void onItemClick(View v, int position);
        }

        public class CourseHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView titleView;
            TextView statusView;
            TextView startDateview;
            TextView endDateView;

            public CourseHolder(View itemView) {
                super(itemView);

                titleView = itemView.findViewById(R.id.course_title_list);
                statusView = itemView.findViewById(R.id.course_status_list);
                startDateview = itemView.findViewById(R.id.course_start_date_list);
                endDateView = itemView.findViewById(R.id.course_end_date_list);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                postItemClick(this);
            }
        }

        private List<Course> mCourses;
        private CourseAdapter.OnItemClickListener mOnItemClickListener;

        public CourseAdapter(List<Course> courses) {
            mCourses = courses;
        }

        public void setOnItemClickListener(CourseAdapter.OnItemClickListener listener) {
            mOnItemClickListener = listener;
        }

        private void postItemClick(CourseAdapter.CourseHolder holder) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(holder.itemView, holder.getAdapterPosition());
            }
        }

        @Override
        public CourseAdapter.CourseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_course, parent, false);

            return new CourseAdapter.CourseHolder(itemView);
        }

        @Override
        public void onBindViewHolder(CourseAdapter.CourseHolder holder, int position) {
            Course course = mCourses.get(position);

            holder.titleView.setText(course.getTitle());
            holder.statusView.setText(course.getStatus());
            holder.startDateview.setText(formatMyTime(course.getStartDate()));
            holder.endDateView.setText(formatMyTime(course.getEndDate()));
        }

        @Override
        public int getItemCount() {
            return mCourses.size();
        }

    }
}
