package com.android.awells.coursetrackerroom;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.awells.coursetrackerroom.data.CourseTrackerDatabase;
import com.android.awells.coursetrackerroom.data.Term;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    List<Term> mTerms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RecyclerView list = findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(list.getContext()));

        mTerms = CourseTrackerDatabase.getInstance(getApplicationContext()).term().selectAll();

        CourseAdapter courseAdapter = new CourseAdapter(mTerms);
        courseAdapter.setOnItemClickListener(mOnItemClickListener());
        list.setAdapter(courseAdapter);
    }

    public CourseAdapter.OnItemClickListener mOnItemClickListener() {
        return new CourseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Log.e(TAG, Integer.toString(position));
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

                titleView = itemView.findViewById(android.R.id.text1);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                postItemClick(this);
            }
        }

        private List<Term> mTerms;
        private OnItemClickListener mOnItemClickListener;

        public CourseAdapter(List<Term> terms) {
            mTerms = terms;
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            mOnItemClickListener = listener;
        }

        private void postItemClick(CourseHolder holder) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(holder.itemView, holder.getAdapterPosition());
            }
        }

        @Override
        public CourseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);

            return new CourseHolder(itemView);
        }

        @Override
        public void onBindViewHolder(CourseHolder holder, int position) {
            String title = mTerms.get(position).getTitle();

            holder.titleView.setText(title);
        }

        @Override
        public int getItemCount() {
            return mTerms.size();
        }

        public void setTerms(List<Term> terms) {
            mTerms = terms;
            notifyDataSetChanged();
        }
    }
}
