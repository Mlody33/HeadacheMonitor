package pl.mzap.headache;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.mzap.headache.ClickListener.ClickListener;
import pl.mzap.headache.ClickListener.RecyclerTouchListener;
import pl.mzap.headache.adapter.MainAdapter;
import pl.mzap.headache.database.entity.Headache;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAIN_ACTIVITY";

    private static final int SELF_DISAPPEARANCE = 2;
    private static final int ACTIVITY_FINISHED = 3;
    private static final int NEW_ONE_APPEARS = 4;

    @BindView(R.id.headacheRecyclerView)
    RecyclerView headacheRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.mainLinearLayout)
    LinearLayout mainLinearLayout;

    private List<Headache> headaches = new ArrayList<>();
    private Calendar selectedDate;
    private MainAdapter mainAdapter;

    private RatingBar ratingBar;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        selectedDate = Calendar.getInstance();
        selectedDate.setTime(new Date());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        headacheRecyclerView.setLayoutManager(layoutManager);
        headacheRecyclerView.setHasFixedSize(true);
        headacheRecyclerView.setItemAnimator(new DefaultItemAnimator());

        getHeadachesFromDatabase();
        headacheRecyclerViewOnClickListener();

    }

    @Override
    protected void onResume() {
        super.onResume();
        selectedDate.setTime(new Date());
//        updateDateTimeLabel(selectedDate.getTime());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.set_current_date_time_menu:
                selectedDate.setTime(new Date());
//                updateDateTimeLabel(selectedDate.getTime());
                break;
            case R.id.set_date:
                showDateEditor();
                break;
            case R.id.set_time:
                showTimeEditor();
                break;
        }
        return true;
    }

    private void getHeadachesFromDatabase() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                headaches = App.getInstance().getDatabase().headacheDao().getHeadaches();
                if (!headaches.isEmpty())
                    setViewAdapter(headaches);
            }
        }).start();
    }

    private void headacheRecyclerViewOnClickListener() {
        headacheRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), headacheRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ratingBar = view.findViewById(R.id.rating_bar_header);
                progressBar = view.findViewById(R.id.progress_bar_header);
                ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        if (ratingBar.getRating() != 0f) {
                            progressBar.setVisibility(View.VISIBLE);
                            final Headache headache = new Headache();
                            headache.setDate(selectedDate.getTime());
                            headache.setDate(new Date());
                            headache.setRating(ratingBar.getRating());
                            showHeadacheInformation(headache);
                        } else
                            progressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void showDateEditor() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") final View layout = inflater.inflate(R.layout.dialog_date_changer, null);
        final DatePicker datePicker = layout.findViewById(R.id.datePicker);
        builder.setView(layout);
        builder.setPositiveButton(R.string.dialog_positive_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int selectedDay = datePicker.getDayOfMonth();
                int selectedMonth = datePicker.getMonth();
                int selectedYear = datePicker.getYear();

                int currentHour = selectedDate.get(Calendar.HOUR_OF_DAY);
                int currentMinute = selectedDate.get(Calendar.MINUTE);

                selectedDate.set(selectedYear, selectedMonth, selectedDay, currentHour, currentMinute);
//                updateDateTimeLabel(selectedDate.getTime());
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

    private void showTimeEditor() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams") final View layout = inflater.inflate(R.layout.dialog_time_changer, null);
        final TimePicker timePicker = layout.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        builder.setView(layout);
        builder.setPositiveButton(R.string.dialog_positive_button, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M) //TODO Works only with Marshmallow API
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int currentDay = selectedDate.get(Calendar.DAY_OF_MONTH);
                int currentMonth = selectedDate.get(Calendar.MONTH);
                int currentYear = selectedDate.get(Calendar.YEAR);

                int selectedHour = timePicker.getHour();
                int selectedMinute = timePicker.getMinute();

                selectedDate.set(currentYear, currentMonth, currentDay, selectedHour, selectedMinute);
//                updateDateTimeLabel(selectedDate.getTime());
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
        mainAdapter = new MainAdapter(headaches, getApplicationContext());
        headacheRecyclerView.setAdapter(mainAdapter);
    }

    public void showHeadacheInformation(final Headache headache) {
        Snackbar snackbar = Snackbar.make(mainLinearLayout, R.string.headache_adding, Snackbar.LENGTH_SHORT);
        snackbar.setAction(R.string.headache_cancel_btn, new View.OnClickListener() {
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
                    Toast.makeText(MainActivity.this, R.string.headache_added, Toast.LENGTH_SHORT).show();
                    insertHeadache(headache);
                }
            }
        });
        snackbar.show();
    }

    private void insertHeadache(final Headache headache) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                App.getInstance().getDatabase().headacheDao().insert(headache);
                progressBar.setVisibility(View.INVISIBLE);
                ratingBar.setRating(0f);
                mainAdapter.addItem(headache);
            }
        }).start();
    }

}
