package pl.mzap.headache.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import pl.mzap.headache.R;

public class MainViewHolder extends RecyclerView.ViewHolder {

    public RelativeLayout relativeLayout;
    public TextView date_label, time_label;
    public RatingBar ratingBar;

    public MainViewHolder(View itemView) {
        super(itemView);
        date_label = itemView.findViewById(R.id.date_label);
        time_label = itemView.findViewById(R.id.time_label);
        ratingBar = itemView.findViewById(R.id.ratingBar);

        relativeLayout = itemView.findViewById(R.id.headache_card_layout);
    }
}
