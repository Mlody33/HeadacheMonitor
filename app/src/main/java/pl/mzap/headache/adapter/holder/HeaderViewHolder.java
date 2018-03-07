package pl.mzap.headache.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import pl.mzap.headache.R;

public class HeaderViewHolder extends RecyclerView.ViewHolder {

    public TextView dateLabel, timeLabel, introductionHeadacheRating;
    public RatingBar ratingBar;
    public ProgressBar progressBar;

    public HeaderViewHolder(View itemView) {
        super(itemView);
        dateLabel = itemView.findViewById(R.id.date_header_label);
        timeLabel = itemView.findViewById(R.id.time_header_label);
        introductionHeadacheRating = itemView.findViewById(R.id.intro_header_rating_bar);
        ratingBar = itemView.findViewById(R.id.rating_bar_header);
        progressBar = itemView.findViewById(R.id.progress_bar_header);
    }

}
