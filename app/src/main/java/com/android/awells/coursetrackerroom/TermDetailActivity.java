package com.android.awells.coursetrackerroom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.awells.coursetrackerroom.data.Course;
import com.android.awells.coursetrackerroom.data.CourseTrackerDatabase;
import com.android.awells.coursetrackerroom.data.Term;

import java.util.List;

public class TermDetailActivity extends AppCompatActivity {

    private static final String TAG = TermDetailActivity.class.getSimpleName();

    private long termId;
    private List<Course> mCourses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_detail);

        termId = getIntent().getLongExtra(Term.COLUMN_ID, -1); // Retrieve the term's ID from the intent
        this.setTitle(CourseTrackerDatabase.getInstance(getApplicationContext()).term().selectById(termId).getTitle()); //Set the title by retrieving the term
        mCourses = CourseTrackerDatabase.getInstance(getApplicationContext()).course().selectCoursesFromTerm(termId); //Retrieve the list of courses from the database

        final RecyclerView recyclerView = findViewById(R.id.recycler_view_term_detail);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        CourseAdapter courseAdapter = new CourseAdapter(mCourses);
        courseAdapter.setOnItemClickListener(mOnItemClickListener());
        recyclerView.setAdapter(courseAdapter);
    }

    public CourseAdapter.OnItemClickListener mOnItemClickListener() {
        return new CourseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(TermDetailActivity.this, CourseDetailActivity.class);
                intent.putExtra(Course.COLUMN_ID, mCourses.get(position).getId());

                startActivity(intent);
            }
        };
    }

    static class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseHolder> {

        public interface OnItemClickListener {
            void onItemClick(View v, int position);
        }

        public class CourseHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView titleView;

            public CourseHolder(View itemView) {
                super(itemView);

                titleView = itemView.findViewById(R.id.course_title_list);

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
            String title = mCourses.get(position).getTitle();

            holder.titleView.setText(title);
        }

        @Override
        public int getItemCount() {
            return mCourses.size();
        }

        public void setCourses(List<Course> courses) {
            mCourses = courses;
            notifyDataSetChanged();
        }
    }
}
