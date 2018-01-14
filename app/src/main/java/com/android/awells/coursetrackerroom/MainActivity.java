package com.android.awells.coursetrackerroom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.awells.coursetrackerroom.data.CourseTrackerDatabase;
import com.android.awells.coursetrackerroom.data.Term;

import java.util.List;

import static com.android.awells.coursetrackerroom.date.DatePickerFragment.formatMyDate;

public class MainActivity extends AppCompatActivity {

    public static long CODE_NO_INPUT = -1; //Used as the default with getIntent().getLongExtra()

    List<Term> mTerms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updateUI();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // If a term was changed or deleted
        if (resultCode == RESULT_OK) {
            updateUI();
        }
    }

    private void updateUI() {
        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        mTerms = CourseTrackerDatabase.getInstance(getApplicationContext()).term().selectAll();
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

                startActivityForResult(intent, 1);
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_add_term:
                Intent intent = new Intent(this, AddTermActivity.class);
                startActivityForResult(intent, 1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    static class TermAdapter extends RecyclerView.Adapter<TermAdapter.TermHolder> {

        public interface OnItemClickListener {
            void onItemClick(View v, int position);
        }

        public class TermHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView titleView;
            TextView startDateView;
            TextView endDateView;

            public TermHolder(View itemView) {
                super(itemView);

                titleView = itemView.findViewById(R.id.term_title_list);
                startDateView = itemView.findViewById(R.id.term_start_date_list);
                endDateView = itemView.findViewById(R.id.term_end_date_list);

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
            Term term = mTerms.get(position);

            String title = term.getTitle();
            String startDate = formatMyDate(term.getStartDate());
            String endDate = formatMyDate(term.getEndDate());

            holder.titleView.setText(title);
            holder.startDateView.setText(startDate);
            holder.endDateView.setText(endDate);
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