package pl.mzap.headache.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import pl.mzap.headache.R;

public class ItemViewHolder extends RecyclerView.ViewHolder {

    public RelativeLayout headacheViewForeground;
    public RelativeLayout headacheViewBackground;
    public TextView dateLabel, timeLabel;
    public RatingBar ratingBar;

    public ItemViewHolder(View itemView) {
        super(itemView);
        dateLabel = itemView.findViewById(R.id.date_label);
        timeLabel = itemView.findViewById(R.id.time_label);
        ratingBar = itemView.findViewById(R.id.rating_bar_header);

        headacheViewForeground = itemView.findViewById(R.id.headache_row_layout);
        headacheViewBackground = itemView.findViewById(R.id.headache_view_background);
    }
}
