package pl.mzap.headache;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.mzap.headache.database.entity.Headache;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAINACTIVITY";

    private static final int SELF_DISAPPEARANCE = 2;
    private static final int ACTIVITY_FINISHED = 3;
    private static final int NEW_ONE_APPEARS = 4;

    @BindView(R.id.constraintLayout)
    ConstraintLayout constraintLayout;
    @BindView(R.id.ratingBar)
    RatingBar headacheRating;
    @BindView(R.id.progressBar)
    ProgressBar progressBarOfHeadacheDatabase;

    @BindView(R.id.openPopup)
    Button openPopup;

    private List<Headache> headaches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ratingBarListener();
        getDatabase();

        openPopupListener();


    }

    private void openPopupListener() {
        openPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
//                LayoutInflater inflater = getLayoutInflater();
//                builder.setView(inflater.inflate(R.layout.date_changer, null));
                builder.setTitle("tytul");
                builder.setMessage("zmiana daty");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void getDatabase() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                headaches = App.getInstance().getDatabase().headacheDao().getHeadaches();
                if (!headaches.isEmpty())
                    for (Headache ha : headaches)
                        Log.e(TAG, "Headache " + ha.getRating() + " on " + ha.getDate());
            }
        }).start();
    }

    private void ratingBarListener() {
        headacheRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (headacheRating.getRating() != 0f) {
                    progressBarOfHeadacheDatabase.setVisibility(View.VISIBLE);
                    final Headache headache = new Headache();
                    headache.setDate(new Date());
                    headache.setRating(ratingBar.getRating());
                    showHeadacheInformation(headache);
                } else
                    progressBarOfHeadacheDatabase.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void showHeadacheInformation(final Headache headache) {
        Snackbar snackbar = Snackbar.make(constraintLayout, R.string.headache_adding, Snackbar.LENGTH_SHORT);
        snackbar.setAction(R.string.headache_cancel_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                headacheRating.setRating(0f);
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
                progressBarOfHeadacheDatabase.setVisibility(View.INVISIBLE);
                headacheRating.setRating(0f);
            }
        }).start();
    }

}
