package com.example.nano1.dmvheads;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;



public class YearAdapter extends RecyclerView.Adapter<YearItemViewHolder> {

    ArrayList<Year> mArrayOfYears;

    public YearAdapter(ArrayList<Year> years){
        this.mArrayOfYears = years;
    }

    @Override
    public YearItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_post,parent, false);
        return new YearItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(YearItemViewHolder holder, int position) {
        holder.year.setText(mArrayOfYears.get(position).year);
        holder.first.setText(mArrayOfYears.get(position).firstPlace);
        holder.second.setText(mArrayOfYears.get(position).secondPlace);
        holder.third.setText(mArrayOfYears.get(position).thirdPlace);
    }

    @Override
    public int getItemCount() {
        return mArrayOfYears.size();
    }
}
