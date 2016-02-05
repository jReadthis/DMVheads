package com.example.nano1.dmvheads;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class YearItemViewHolder extends RecyclerView.ViewHolder{

    TextView year;
    TextView first;
    TextView second;
    TextView third;

    public YearItemViewHolder(View itemView) {
        super(itemView);

        year = (TextView) itemView.findViewById(R.id.year);
        first = (TextView) itemView.findViewById(R.id.first);
        second = (TextView) itemView.findViewById(R.id.second);
        third = (TextView) itemView.findViewById(R.id.third);
    }
}
