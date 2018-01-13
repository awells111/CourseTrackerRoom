package com.android.awells.coursetrackerroom;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.awells.coursetrackerroom.data.Assessment;
import com.android.awells.coursetrackerroom.data.Course;
import com.android.awells.coursetrackerroom.data.CourseTrackerDatabase;
import com.android.awells.coursetrackerroom.data.Note;
import com.android.awells.coursetrackerroom.data.Term;

import java.util.List;

import static com.android.awells.coursetrackerroom.DatePickerFragment.formatMyTime;
import static com.android.awells.coursetrackerroom.MainActivity.CODE_NO_INPUT;

public class CourseDetailActivity extends AppCompatActivity {

    private static String TAG = CourseDetailActivity.class.getSimpleName();

    private long courseId;
    private long termId;

    private List<Assessment> mAssessments;
    private List<Note> mNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        courseId = getIntent().getLongExtra(Course.COLUMN_ID, CODE_NO_INPUT);
        termId = getIntent().getLongExtra(Term.COLUMN_ID, CODE_NO_INPUT);

        this.setTitle(CourseTrackerDatabase.getInstance(getApplicationContext()).course().selectByCourseId(courseId).getTitle()); //Set the title by retrieving the term

        updateUI();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK); // Let TermDetailActivity know it needs to update the UI
            updateUI();
        }
    }

    private void updateUI() {
        Course course = CourseTrackerDatabase.getInstance(getApplicationContext()).course().selectByCourseId(courseId);
        this.setTitle(course.getTitle());

        TextView startDateView = findViewById(R.id.course_start_date_detail);
        TextView endDateView = findViewById(R.id.course_end_date_detail);

        startDateView.setText(formatMyTime(course.getStartDate()));
        endDateView.setText(formatMyTime(course.getEndDate()));


        final RecyclerView assessmentsRecyclerView = findViewById(R.id.assessments_view_course_detail);
        assessmentsRecyclerView.setLayoutManager(new LinearLayoutManager(assessmentsRecyclerView.getContext()));

        mAssessments = CourseTrackerDatabase.getInstance(getApplicationContext()).assessment().selectAssessmentsFromCourse(courseId);
        AssessmentAdapter assessmentAdapter = new AssessmentAdapter(getApplicationContext(), mAssessments);
        assessmentAdapter.setOnItemClickListener(mOnAssessmentClickListener());
        assessmentsRecyclerView.setAdapter(assessmentAdapter);

        final RecyclerView notesRecyclerView = findViewById(R.id.notes_view_course_detail);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(notesRecyclerView.getContext()));

        mNotes = CourseTrackerDatabase.getInstance(getApplicationContext()).note().selectNotesFromCourse(courseId);
        NoteAdapter noteAdapter = new NoteAdapter(mNotes);
        notesRecyclerView.setAdapter(noteAdapter);
    }

    public AssessmentAdapter.OnItemClickListener mOnAssessmentClickListener() {
        return new AssessmentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
//                Intent intent = new Intent(CourseDetailActivity.this, AssessmentDetailActivity.class);  //todo
//                intent.putExtra(Course.COLUMN_ID, mAssessments.get(position).getCourseId());
//                intent.putExtra(Assessment.COLUMN_ID, mAssessments.get(position).getId());
//
//                startActivityForResult(intent, 1);
            }
        };
    }
//
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
            case R.id.action_edit_course:
                Intent editIntent = new Intent(CourseDetailActivity.this, AddCourseActivity.class);
                editIntent.putExtra(Term.COLUMN_ID, termId);
                editIntent.putExtra(Course.COLUMN_ID, courseId);
                startActivityForResult(editIntent, 1);
                return true;
            case R.id.action_delete_course:
                int count = CourseTrackerDatabase.getInstance(getApplicationContext()).course().deleteById(courseId);
                Log.d(TAG, "Deleted " + count + " course(s)");
                setResult(RESULT_OK); // Let TermDetailActivity know it needs to update the UI
                finish();
                return true;
            case R.id.action_add_assessment:
                Intent assessmentIntent = new Intent(CourseDetailActivity.this, AddAssessmentActivity.class);
                assessmentIntent.putExtra(Course.COLUMN_ID, courseId);
                startActivityForResult(assessmentIntent, 1);
                return true;
            case R.id.action_add_note:
                Intent noteIntent = new Intent(CourseDetailActivity.this, AddNoteActivity.class);
                noteIntent.putExtra(Course.COLUMN_ID, courseId);
                startActivityForResult(noteIntent, 1);
                return true;
            case R.id.action_share_notes:
                //Create one String made from every note in mNotes
                StringBuilder sb = new StringBuilder();
                for (Note n : mNotes) { //For each note in mNotes
                    sb.append(n.getText()); //Add the note text to the StringBuilder
                    sb.append("\n"); //Move to a new line
                }

                //Send the String to an email app
                Intent shareIntent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:?subject=" + this.getTitle() + " " + getString(R.string.notes) +  "&body=" + sb.toString());
                shareIntent.setData(data);
                startActivity(shareIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    static class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.AssessmentHolder> {

        public interface OnItemClickListener {
            void onItemClick(View v, int position);
        }

        public class AssessmentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView typeView;
            TextView scoreView;
            TextView scheduledTimeView;
            TextView notificationTimeView;

            public AssessmentHolder(View itemView) {
                super(itemView);

                typeView = itemView.findViewById(R.id.assessment_type_list);
                scoreView = itemView.findViewById(R.id.assessment_score_list);
                scheduledTimeView = itemView.findViewById(R.id.assessment_scheduled_time_list);
                notificationTimeView = itemView.findViewById(R.id.assessment_notification_time_list);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                postItemClick(this);
            }
        }

        private Context mContext;

        private List<Assessment> mAssessments;
        private AssessmentAdapter.OnItemClickListener mOnItemClickListener;

        public AssessmentAdapter(Context context, List<Assessment> assessments) {
            mContext = context;
            mAssessments = assessments;
        }

        public void setOnItemClickListener(AssessmentAdapter.OnItemClickListener listener) {
            mOnItemClickListener = listener;
        }

        private void postItemClick(AssessmentAdapter.AssessmentHolder holder) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(holder.itemView, holder.getAdapterPosition());
            }
        }

        @Override
        public AssessmentAdapter.AssessmentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_assessment, parent, false);

            return new AssessmentAdapter.AssessmentHolder(itemView);
        }

        @Override
        public void onBindViewHolder(AssessmentAdapter.AssessmentHolder holder, int position) {
            String notSet = mContext.getResources().getString(R.string.not_set);
            Log.e("tag", notSet);
            Assessment assessment = mAssessments.get(position);
            //Type score scheduled notification
            holder.typeView.setText(assessment.getType());

            if (assessment.getScore() < 0) { //If assessment is not taken
                holder.scoreView.setText(notSet);
            } else {
                holder.scoreView.setText(Integer.toString(assessment.getScore()));
            }

            if (assessment.getScheduledTime() == Long.MIN_VALUE) { //If assessment is not scheduled
                holder.scheduledTimeView.setText(notSet);
            } else {
                holder.scheduledTimeView.setText(formatMyTime(assessment.getScheduledTime()));
            }

            if (assessment.getStartNotification() == Long.MIN_VALUE) { //If assessment notification is not set
                holder.notificationTimeView.setText(notSet);
            } else {
                holder.notificationTimeView.setText(formatMyTime(assessment.getStartNotification()));
            }
        }

        @Override
        public int getItemCount() {
            return mAssessments.size();
        }
    }

    static class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {

        public class NoteHolder extends RecyclerView.ViewHolder {

            TextView textView;

            public NoteHolder(View itemView) {
                super(itemView);

                textView = itemView.findViewById(R.id.note_text);
            }
        }

        private List<Note> mNotes;

        public NoteAdapter(List<Note> notes) {
            mNotes = notes;
        }

        @Override
        public NoteAdapter.NoteHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_note, parent, false);

            return new NoteAdapter.NoteHolder(itemView);
        }

        @Override
        public void onBindViewHolder(NoteAdapter.NoteHolder holder, int position) {
            String text = mNotes.get(position).getText();

            holder.textView.setText(text);
        }

        @Override
        public int getItemCount() {
            return mNotes.size();
        }
    }
}
