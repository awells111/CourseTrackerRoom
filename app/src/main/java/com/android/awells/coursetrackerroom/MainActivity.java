package com.android.awells.coursetrackerroom;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.awells.coursetrackerroom.data.CourseTrackerDatabase;
import com.android.awells.coursetrackerroom.data.Term;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TermAdapter mTermAdapter;
    List<Term> mTerms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RecyclerView list = findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(list.getContext()));

        mTerms = CourseTrackerDatabase.getInstance(getApplicationContext()).term().selectAll();
        mTermAdapter = new TermAdapter(mTerms);
        list.setAdapter(mTermAdapter);
    }

    private static class TermAdapter extends RecyclerView.Adapter<TermAdapter.ViewHolder> {

        private List<Term> mTerms;

        public TermAdapter(List<Term> terms) {
            setTerms(terms);
        }
        //todo onItemClickListener

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            String title = mTerms.get(position).getTitle();

            holder.mText.setText(title);
        }

        @Override
        public int getItemCount() {
            return mTerms.size();
        }

        void setTerms(List<Term> terms) {
            mTerms = terms;
            notifyDataSetChanged();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {

            TextView mText;

            ViewHolder(ViewGroup parent) {
                super(LayoutInflater.from(parent.getContext()).inflate(
                        android.R.layout.simple_list_item_1, parent, false));
                mText = itemView.findViewById(android.R.id.text1); //todo make our own layout
            }

        }

    }
}
