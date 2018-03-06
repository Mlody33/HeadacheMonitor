package pl.mzap.headache.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import pl.mzap.headache.R;
import pl.mzap.headache.adapter.holder.HeaderViewHolder;
import pl.mzap.headache.adapter.holder.ItemViewHolder;
import pl.mzap.headache.database.entity.Headache;

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private Context context;
    private List<Headache> headaches;

    public MainAdapter(List<Headache> headaches, Context context) {
        this.headaches = headaches;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View headerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_headache_header, parent, false);
            return new HeaderViewHolder(headerView);
        } else if (viewType == TYPE_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_headache_item, parent, false);
            return new ItemViewHolder(itemView);
        }
        throw new RuntimeException("No match for " + viewType);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        Headache headache = headaches.get(position);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat shortDateFormat = new SimpleDateFormat("dd MMM");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat fullDateFormat = new SimpleDateFormat("dd MMM HH:mm");

        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).dateTime.setText(fullDateFormat.format(new Date()));
            ((HeaderViewHolder) holder).ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    Toast.makeText(context, "Rating bar changed", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).dateLabel.setText(shortDateFormat.format(headache.getDate()));
            ((ItemViewHolder) holder).timeLabel.setText(timeFormat.format(headache.getDate()));
            ((ItemViewHolder) holder).ratingBar.setRating(headache.getRating());
        }
    }

    @Override
    public int getItemCount() {
        return headaches.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;
        else
            return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    public void removeItem(int position) {
        headaches.remove(position);
        notifyItemRemoved(position);
    }

    public void addItem(Headache headache) {
        int position = headaches.size();
        headaches.add(headache);
        notifyItemInserted(position);
    }

}

