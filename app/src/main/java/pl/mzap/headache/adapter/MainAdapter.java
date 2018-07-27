package pl.mzap.headache.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import pl.mzap.headache.App;
import pl.mzap.headache.MainActivity;
import pl.mzap.headache.R;
import pl.mzap.headache.adapter.holder.ItemViewHolder;
import pl.mzap.headache.database.entity.Headache;

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Headache> headaches;
    private MainActivity main;

    public MainAdapter(List<Headache> headaches, MainActivity main) {
        this.headaches = headaches;
        this.main = main;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_headache, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        Headache headache = headaches.get(position);

        ((ItemViewHolder) holder).dateLabel.setText(App.getInstance().getDateFormat().format(headache.getDate()));
        ((ItemViewHolder) holder).timeLabel.setText(App.getInstance().getTimeFormat().format(headache.getDate()));

        String ratingTextScale[] = main.getApplicationContext().getResources().getStringArray(R.array.headache_text_scale);
        switch ((int) headache.getRating()) {
            case 1:
                ((ItemViewHolder) holder).ratingImage.setImageResource(R.drawable.rating_btn_1);
                ((ItemViewHolder) holder).ratingText.setText(ratingTextScale[(int) headache.getRating() - 1]);
                break;
            case 2:
                ((ItemViewHolder) holder).ratingImage.setImageResource(R.drawable.rating_btn_2);
                ((ItemViewHolder) holder).ratingText.setText(ratingTextScale[(int) headache.getRating() - 1]);
                break;
            case 3:
                ((ItemViewHolder) holder).ratingImage.setImageResource(R.drawable.rating_btn_3);
                ((ItemViewHolder) holder).ratingText.setText(ratingTextScale[(int) headache.getRating() - 1]);
                break;
            case 4:
                ((ItemViewHolder) holder).ratingImage.setImageResource(R.drawable.rating_btn_4);
                ((ItemViewHolder) holder).ratingText.setText(ratingTextScale[(int) headache.getRating() - 1]);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return headaches.size();
    }

    public void removeItem(int position) {
        headaches.remove(position);
        notifyItemRemoved(position);
    }

    public void addItem(Headache headache) {
        headaches.add(0, headache);
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

