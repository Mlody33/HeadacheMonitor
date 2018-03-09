package pl.mzap.headache;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.mzap.headache.adapter.MainAdapter;
import pl.mzap.headache.adapter.holder.ItemViewHolder;
import pl.mzap.headache.database.entity.Headache;
import pl.mzap.headache.touch.RecyclerItemTouchHelper;
import pl.mzap.headache.touch.RecyclerItemTouchHelperListener;

public class MainActivity extends AppCompatActivity implements RecyclerItemTouchHelperListener {

    private static final String TAG = "MAIN_ACTIVITY";

    private static final int SELF_DISAPPEARANCE = 2;
    private static final int ACTIVITY_FINISHED = 3;
    private static final int NEW_ONE_APPEARS = 4;

    @BindView(R.id.headacheRecyclerView)
    RecyclerView headacheRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.mainLinearLayout)
    LinearLayout mainLinearLayout;

    private List<Headache> headaches = new ArrayList<>();
    private Calendar selectedDate;
    private MainAdapter mainAdapter;

    private RatingBar ratingBar;
    private ProgressBar progressBar;
    private TextView dateLabel, timeLabel;
    private boolean isLabelsInitialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        selectedDate = Calendar.getInstance();
        selectedDate.setTime(new Date());

        if (getHeadachesHistory())
            setViewAdapter(headaches);
        headacheRecyclerViewInitializer();
        refreshSwipeOnRefreshingListener();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isLabelsInitialized)
            updateDateTimeLabel(new Date());
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        ((MenuBuilder) menu).setOptionalIconsVisible(true);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_set_current_date_time:
                updateDateTimeLabel(new Date());
                break;
            case R.id.menu_set_date:
                showDateEditor();
                break;
            case R.id.menu_set_time:
                showTimeEditor();
                break;
            case R.id.menu_refresh:
                getHeadachesHistory();
                updateAdapterList();
                break;
        }
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, final int position) {
        if (viewHolder instanceof ItemViewHolder) {

            final Headache headache = headaches.get(viewHolder.getAdapterPosition());
            final int deletedPosition = viewHolder.getAdapterPosition();

            mainAdapter.removeItem(viewHolder.getAdapterPosition());

            Snackbar snackbar = Snackbar.make(mainLinearLayout, R.string.item_headache_removed, Snackbar.LENGTH_LONG);
            snackbar.setAction(R.string.all_cancel_btn, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mainAdapter.restoreItem(headache, deletedPosition);
                }
            });
            snackbar.setActionTextColor(getResources().getColor(R.color.accent));
            snackbar.addCallback(new Snackbar.Callback() {
                @Override
                public void onDismissed(Snackbar snackbar, int event) {
                    if (event == SELF_DISAPPEARANCE | event == ACTIVITY_FINISHED | event == NEW_ONE_APPEARS)
                        removeHeadache(headache);
                }
            });
            snackbar.show();
        }
    }

    private void headacheRecyclerViewInitializer() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        headacheRecyclerView.setLayoutManager(layoutManager);
        headacheRecyclerView.setHasFixedSize(true);
        headacheRecyclerView.setItemAnimator(new DefaultItemAnimator());
        headacheRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        ItemTouchHelper.SimpleCallback itemToucheHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemToucheHelperCallback).attachToRecyclerView(headacheRecyclerView);
    }

    private void ratingBarChangeListener() {
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (ratingBar.getRating() != 0f) {
                    progressBar.setVisibility(View.VISIBLE);
                    final Headache headache = new Headache();
                    headache.setDate(selectedDate.getTime());
                    headache.setRating(ratingBar.getRating());
                    showHeadacheAddingInformation(headache);
                } else
                    progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void refreshSwipeOnRefreshingListener() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getHeadachesHistory();
                updateAdapterList();
            }
        });
    }

    private void updateAdapterList() {
        refreshLayout.setRefreshing(true);
        mainAdapter.updateItems(headaches);
        refreshLayout.setRefreshing(false);
        Toast.makeText(this, R.string.all_headaches_updated, Toast.LENGTH_SHORT).show();
    }

    private boolean getHeadachesHistory() {
        Thread databaseThread = new Thread(new Runnable() {
            @Override
            public void run() {
                headaches = App.getInstance().getDatabase().headacheDao().getHeadaches();
            }
        });
        databaseThread.start();
        try {
            databaseThread.join();
            return true;
        } catch (InterruptedException e) {
            String error_msg = "Can't get headaches history from local database";
            Toast.makeText(this, error_msg, Toast.LENGTH_LONG).show();
            Log.e(TAG, error_msg);
            return false;
        }

    }

    private void showDateEditor() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") final View layout = inflater.inflate(R.layout.view_date_changer, null);
        final DatePicker datePicker = layout.findViewById(R.id.datePicker);
        datePicker.updateDate(selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH));
        builder.setView(layout);
        builder.setPositiveButton(R.string.dialog_positive_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Calendar chosenDate = Calendar.getInstance();
                chosenDate.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
                        chosenDate.get(Calendar.HOUR_OF_DAY), chosenDate.get(Calendar.MINUTE));

                updateDateTimeLabel(chosenDate.getTime());
            }
        });
        builder.setNegativeButton(R.string.dialog_negative_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showTimeEditor() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") final View layout = inflater.inflate(R.layout.view_time_changer, null);
        final TimePicker timePicker = layout.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        timePicker.setHour(selectedDate.get(Calendar.HOUR_OF_DAY));
        timePicker.setMinute(selectedDate.get(Calendar.MINUTE));
        builder.setView(layout);
        builder.setPositiveButton(R.string.dialog_positive_button, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Calendar chosenTime = Calendar.getInstance();
                chosenTime.set(selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH),
                        selectedDate.get(Calendar.DAY_OF_MONTH),
                        timePicker.getHour(), timePicker.getMinute());

                updateDateTimeLabel(chosenTime.getTime());
            }
        });
        builder.setNegativeButton(R.string.dialog_negative_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setViewAdapter(List<Headache> headaches) {
        mainAdapter = new MainAdapter(headaches, this);
        headacheRecyclerView.setAdapter(mainAdapter);
    }

    private void updateDateTimeLabel(Date date) {
        selectedDate.setTime(date);
        dateLabel.setText(App.getInstance().getDateFormat().format(date));
        timeLabel.setText(App.getInstance().getTimeFormat().format(date));
    }

    public void showHeadacheAddingInformation(final Headache headache) {
        Snackbar snackbar = Snackbar.make(mainLinearLayout, R.string.all_headache_adding, Snackbar.LENGTH_SHORT);
        snackbar.setAction(R.string.all_cancel_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingBar.setRating(0f);
            }
        });
        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                if (event == SELF_DISAPPEARANCE | event == ACTIVITY_FINISHED | event == NEW_ONE_APPEARS) {
                    Toast.makeText(MainActivity.this, R.string.all_headache_added, Toast.LENGTH_SHORT).show();
                    insertHeadache(headache);
                }
            }
        });
        snackbar.show();
    }

    public void initializeHeaderLabels(TextView dateLabel, TextView timeLabel) {
        this.dateLabel = dateLabel;
        this.timeLabel = timeLabel;
        isLabelsInitialized = true;
        dateLabelOnClickListener();
        timeLabelOnClickListener();
    }

    private void dateLabelOnClickListener() {
        dateLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateEditor();
            }
        });
    }

    private void timeLabelOnClickListener() {
        timeLabel.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                showTimeEditor();
            }
        });
    }

    private void insertHeadache(final Headache headache) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                App.getInstance().getDatabase().headacheDao().insert(headache);
                progressBar.setVisibility(View.INVISIBLE);
                mainAdapter.addItem(headache);
                ratingBar.setRating(0f);
                selectedDate.setTime(new Date());
            }
        }).start();
    }

    private void removeHeadache(final Headache headache) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                App.getInstance().getDatabase().headacheDao().delete(headache);
            }
        }).start();
    }

    public void initializeRatingBar(RatingBar ratingBar) {
        this.ratingBar = ratingBar;
        ratingBarChangeListener();
    }

    public void initializeProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }
}