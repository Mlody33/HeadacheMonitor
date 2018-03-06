package pl.mzap.headache.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import pl.mzap.headache.R;

public class ItemViewHolder extends RecyclerView.ViewHolder {

    public RelativeLayout relativeLayout;
    public TextView dateLabel, timeLabel;
    public RatingBar ratingBar;

    public ItemViewHolder(View itemView) {
        super(itemView);
        dateLabel = itemView.findViewById(R.id.date_label);
        timeLabel = itemView.findViewById(R.id.time_label);
        ratingBar = itemView.findViewById(R.id.rating_bar_header);

        relativeLayout = itemView.findViewById(R.id.headache_card_layout);
    }
}
