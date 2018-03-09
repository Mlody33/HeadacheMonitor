package pl.mzap.headache.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.mzap.headache.R;

public class HeaderViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.progress_bar_header)
    public ProgressBar progressBar;
    @BindView(R.id.date_header_label)
    public TextView dateLabel;
    @BindView(R.id.time_header_label)
    public TextView timeLabel;
    @BindView(R.id.rating_btn_1)
    public ImageButton ratingBtn1;
    @BindView(R.id.rating_btn_2)
    public ImageButton ratingBtn2;
    @BindView(R.id.rating_btn_3)
    public ImageButton ratingBtn3;
    @BindView(R.id.rating_btn_4)
    public ImageButton ratingBtn4;

    public HeaderViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

}
