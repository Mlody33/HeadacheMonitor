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
import pl.mzap.headache.adapter.holder.MainViewHolder;
import pl.mzap.headache.database.entity.Headache;

public class MainAdapter extends RecyclerView.Adapter<MainViewHolder> {

    private Context context;
    private List<Headache> headaches;

    public MainAdapter(List<Headache> headaches, Context context) {
        this.headaches = headaches;
        this.context = context;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.headache_card, parent, false);
        return new MainViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MainViewHolder holder, final int position) {
        Headache headache = headaches.get(position);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(headache.getDate());
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        holder.date_label.setText(String.valueOf(day + " " + month));
        holder.time_label.setText(String.valueOf(hour + ":" + minute));
        holder.ratingBar.setRating(headache.getRating());

        int randomColorNumber = new Random().nextInt();
        setColor(holder, randomColorNumber);
    }

    @Override
    public int getItemCount() {
        return headaches.size();
    }

    private void setColor(MainViewHolder holder, int colorCode) {
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

