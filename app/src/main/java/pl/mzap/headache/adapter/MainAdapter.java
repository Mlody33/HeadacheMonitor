package pl.mzap.headache.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    public static final int HEADER_POSITION = 0;

    private List<Headache> headaches;

    public MainAdapter(List<Headache> headaches) {
        this.headaches = headaches;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View headerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header, parent, false);
            return new HeaderViewHolder(headerView);
        } else if (viewType == TYPE_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_headache, parent, false);
            return new ItemViewHolder(itemView);
        }
        throw new RuntimeException("No match for " + viewType);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        Headache headache = headaches.get(position);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd MMM");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).dateLabel.setText(dateFormat.format(new Date()));
            ((HeaderViewHolder) holder).timeLabel.setText(timeFormat.format(new Date()));
        } else if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).dateLabel.setText(dateFormat.format(headache.getDate()));
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
        if (isHeader(position))
            return TYPE_HEADER;
        else
            return TYPE_ITEM;
    }

    public boolean isHeader(int position) {
        return position == HEADER_POSITION;
    }

    public void removeItem(int position) {
        headaches.remove(position);
        notifyItemRemoved(position);
    }

    public void addItem(Headache headache) {
        headaches.add(HEADER_POSITION + 1, headache);
        notifyItemInserted(HEADER_POSITION + 1);
    }

    public void restoreItem(Headache headache, int deletedPosition) {
        headaches.add(deletedPosition, headache);
        notifyItemInserted(deletedPosition);
    }
}

