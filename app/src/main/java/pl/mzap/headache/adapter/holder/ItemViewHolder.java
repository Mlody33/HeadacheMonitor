package pl.mzap.headache.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.mzap.headache.R;

public class ItemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.headache_view_background)
    public RelativeLayout headacheViewBackground;
    @BindView(R.id.headache_row_layout)
    public RelativeLayout headacheViewForeground;
    @BindView(R.id.date_label)
    public TextView dateLabel;
    @BindView(R.id.time_label)
    public TextView timeLabel;
    @BindView(R.id.rating_image)
    public ImageView ratingImage;
    @BindView(R.id.rating_text_label)
    public TextView ratingText;

    public ItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
