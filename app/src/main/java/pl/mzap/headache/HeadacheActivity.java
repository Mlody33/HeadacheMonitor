package pl.mzap.headache;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.mzap.headache.database.entity.Headache;

public class HeadacheActivity extends AppCompatActivity {

    private static final int SELF_DISAPPEARANCE = 2;
    private static final int ACTIVITY_FINISHED = 3;
    private static final int NEW_ONE_APPEARS = 4;

    private Calendar selectedDate = Calendar.getInstance();

    @BindView(R.id.headache_toolbar)
    Toolbar toolbar;
    @BindView(R.id.headache_layout)
    CoordinatorLayout headacheLayout;
    @BindView(R.id.headache_swipe_refresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.date_header_label)
    TextView dateLabel;
    @BindView(R.id.time_header_label)
    TextView timeLabel;
    @BindView(R.id.rating_btn_1)
    ImageButton ratingOne;
    @BindView(R.id.rating_btn_2)
    ImageButton ratingTwo;
    @BindView(R.id.rating_btn_3)
    ImageButton ratingThree;
    @BindView(R.id.rating_btn_4)
    ImageButton ratingFour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_headache);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        updateDateTimeLabel(new Date());

        dateLabelOnClickListener();
        timeLabelOnClickListener();
        ratingButtonsOnClickListener();
        swipeOnRefreshingListener();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.headache_menu, menu);
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
        }
        return true;
    }

    private void swipeOnRefreshingListener() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateDateTimeLabel(new Date());
            }
        });
    }

    private void ratingButtonsOnClickListener() {
        final Headache headache = new Headache();

        List<ImageButton> ratingButtons = Arrays.asList(ratingOne, ratingTwo, ratingThree, ratingFour);

        for (final ImageButton rating : ratingButtons) {
            rating.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    if (rating.getId() == ratingOne.getId())
                        headache.setRating(1f);
                    if (rating.getId() == ratingTwo.getId())
                        headache.setRating(2f);
                    if (rating.getId() == ratingThree.getId())
                        headache.setRating(3f);
                    if (rating.getId() == ratingFour.getId())
                        headache.setRating(4f);
                    headache.setDate(selectedDate.getTime());
                    rating.setColorFilter(getColor(R.color.accent));
                    showHeadacheAddingInformation(headache);
                }
            });
        }
    }

    public void showHeadacheAddingInformation(final pl.mzap.headache.database.entity.Headache headache) {
        Snackbar snackbar = Snackbar.make(headacheLayout, R.string.all_headache_adding, Snackbar.LENGTH_SHORT);
        snackbar.setAction(R.string.all_cancel_btn, new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                ratingOne.clearColorFilter();
                ratingTwo.clearColorFilter();
                ratingThree.clearColorFilter();
                ratingFour.clearColorFilter();
            }
        });
        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                if (event == SELF_DISAPPEARANCE | event == ACTIVITY_FINISHED | event == NEW_ONE_APPEARS) {
                    Toast.makeText(HeadacheActivity.this, R.string.all_headache_added, Toast.LENGTH_SHORT).show();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("Class", headache);
                    setResult(45698, resultIntent);
                    finish();
                }
            }
        });
        snackbar.show();
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

    private void showDateEditor() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HeadacheActivity.this);
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
                        selectedDate.get(Calendar.HOUR_OF_DAY), selectedDate.get(Calendar.MINUTE));

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
        AlertDialog.Builder builder = new AlertDialog.Builder(HeadacheActivity.this);
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

    private void updateDateTimeLabel(Date date) {
        refreshLayout.setRefreshing(true);
        selectedDate.setTime(date);
        dateLabel.setText(App.getInstance().getDateFormat().format(date));
        timeLabel.setText(App.getInstance().getTimeFormat().format(date));
        Toast.makeText(this, R.string.all_date_updated, Toast.LENGTH_SHORT).show();
        refreshLayout.setRefreshing(false);
    }

}
