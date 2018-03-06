package pl.mzap.headache.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import pl.mzap.headache.R;
import pl.mzap.headache.adapter.holder.HeaderViewHolder;
import pl.mzap.headache.adapter.holder.ItemViewHolder;
import pl.mzap.headache.database.entity.Headache;

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;

    private Context context;
    private List<Headache> headaches;
    private Random random;

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

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(headache.getDate());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).dateTime.setText("Header sample text setted by viewholder");
        } else if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).dateLabel.setText(String.valueOf(day + "4 " + month));
            if (minute < 10)
                ((ItemViewHolder) holder).timeLabel.setText(String.valueOf(hour + ":0" + minute));
            else
                ((ItemViewHolder) holder).timeLabel.setText(String.valueOf(hour + ":" + minute));
            ((ItemViewHolder) holder).ratingBar.setRating(headache.getRating());

            random = new Random();
            int randomColorNumber = random.nextInt(9 - 1 + 1) + 1;
            setColor(((ItemViewHolder) holder), randomColorNumber);
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

    private void setColor(ItemViewHolder holder, int colorCode) {
        switch (colorCode) {
            case 1:
                holder.relativeLayout.setBackgroundResource(R.color.card_color_10);
                break;
            case 2:
                holder.relativeLayout.setBackgroundResource(R.color.card_color_20);
                break;
            case 3:
                holder.relativeLayout.setBackgroundResource(R.color.card_color_30);
                break;
            case 4:
                holder.relativeLayout.setBackgroundResource(R.color.card_color_40);
                break;
            case 5:
                holder.relativeLayout.setBackgroundResource(R.color.card_color_50);
                break;
            case 6:
                holder.relativeLayout.setBackgroundResource(R.color.card_color_60);
                break;
            case 7:
                holder.relativeLayout.setBackgroundResource(R.color.card_color_70);
                break;
            case 8:
                holder.relativeLayout.setBackgroundResource(R.color.card_color_80);
                break;
            case 9:
                holder.relativeLayout.setBackgroundResource(R.color.card_color_90);
                break;
        }
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

