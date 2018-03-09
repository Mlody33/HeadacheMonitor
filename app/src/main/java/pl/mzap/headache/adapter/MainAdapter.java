package pl.mzap.headache.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pl.mzap.headache.App;
import pl.mzap.headache.MainActivity;
import pl.mzap.headache.R;
import pl.mzap.headache.adapter.holder.HeaderViewHolder;
import pl.mzap.headache.adapter.holder.ItemViewHolder;
import pl.mzap.headache.database.entity.Headache;

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int HEADER_POSITION = 0;

    private List<Headache> headaches;
    private MainActivity main;

    public MainAdapter(List<Headache> headaches, MainActivity main) {
        this.headaches = headaches;
        this.main = main;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View headerView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_header, parent, false);
            return new HeaderViewHolder(headerView);
        } else if (viewType == TYPE_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_headache, parent, false);
            return new ItemViewHolder(itemView);
        }
        throw new RuntimeException("No match for " + viewType);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        Headache headache = headaches.get(position);

        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).dateLabel.setText(App.getInstance().getDateFormat().format(new Date()));
            ((HeaderViewHolder) holder).timeLabel.setText(App.getInstance().getTimeFormat().format(new Date()));
            List<ImageButton> ratingButtons = new ArrayList<ImageButton>() {
                {
                    add(((HeaderViewHolder) holder).ratingBtn1);
                    add(((HeaderViewHolder) holder).ratingBtn2);
                    add(((HeaderViewHolder) holder).ratingBtn3);
                    add(((HeaderViewHolder) holder).ratingBtn4);
                }
            };

            main.initializeHeaderLabels(((HeaderViewHolder) holder).dateLabel, ((HeaderViewHolder) holder).timeLabel);
            main.initializeProgressBar(((HeaderViewHolder) holder).progressBar);
            main.initializeRatingButtons(ratingButtons);
        } else if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).dateLabel.setText(App.getInstance().getDateFormat().format(headache.getDate()));
            ((ItemViewHolder) holder).timeLabel.setText(App.getInstance().getTimeFormat().format(headache.getDate()));
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

    private boolean isHeader(int position) {
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

    public void updateItems(List<Headache> headaches) {
        this.headaches = headaches;
        notifyDataSetChanged();
    }
}

