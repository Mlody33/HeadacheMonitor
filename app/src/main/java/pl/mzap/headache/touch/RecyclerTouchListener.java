package pl.mzap.headache.touch;

import android.view.View;

public interface RecyclerTouchListener {
    void onClick(View view, int position);
    void onLongClick(View view, int position);
}
