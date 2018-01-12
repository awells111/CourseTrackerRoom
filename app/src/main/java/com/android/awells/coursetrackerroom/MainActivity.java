package com.android.awells.coursetrackerroom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

        mTerms = CourseTrackerDatabase.getInstance(getApplicationContext()).term().selectAll();

        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        TermAdapter termAdapter = new TermAdapter(mTerms);
        termAdapter.setOnItemClickListener(mOnItemClickListener());
        recyclerView.setAdapter(termAdapter);
    }

    public TermAdapter.OnItemClickListener mOnItemClickListener() {
        return new TermAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(MainActivity.this, TermDetailActivity.class);
                intent.putExtra(Term.COLUMN_ID, mTerms.get(position).getId());

                startActivity(intent);
            }
        };
    }

    static class TermAdapter extends RecyclerView.Adapter<TermAdapter.TermHolder> {

        public interface OnItemClickListener {
            void onItemClick(View v, int position);
        }

        public class TermHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView titleView;

            public TermHolder(View itemView) {
                super(itemView);

                titleView = itemView.findViewById(R.id.term_title_list);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                postItemClick(this);
            }
        }

        private List<Term> mTerms;
        private OnItemClickListener mOnItemClickListener;

        public TermAdapter(List<Term> terms) {
            mTerms = terms;
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            mOnItemClickListener = listener;
        }

        private void postItemClick(TermHolder holder) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(holder.itemView, holder.getAdapterPosition());
            }
        }

        @Override
        public TermHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_term, parent, false);

            return new TermHolder(itemView);
        }

        @Override
        public void onBindViewHolder(TermHolder holder, int position) {
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
