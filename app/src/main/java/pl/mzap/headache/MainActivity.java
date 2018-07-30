package pl.mzap.headache;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
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
    private static final int NEW_HEADACHE_ADDED = 11;

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
    @BindView(R.id.fab)
    FloatingActionButton addingNewHeadacheButton;

    private List<Headache> headaches = new ArrayList<>();
    private MainAdapter mainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        if (getHeadachesHistory())
            setViewAdapter(headaches);

        headacheRecyclerViewInitializer();
        swipeOnRefreshingListener();
        floatingActionButtonListener();
    }

    private void floatingActionButtonListener() {
        addingNewHeadacheButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent headacheActivity = new Intent(getApplicationContext(), HeadacheActivity.class);
                startActivityForResult(headacheActivity, NEW_HEADACHE_ADDED);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getHeadachesHistory();
        updateViewAdapter();
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
            case R.id.menu_refresh:
                refreshLayout.setRefreshing(true);
                updateViewAdapter();
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case NEW_HEADACHE_ADDED:
                if (resultCode == 45698) {
                    Headache headache = (Headache) data.getSerializableExtra("Class");
                    insertHeadache(headache);
                }
                break;
        }
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

    private void setViewAdapter(List<Headache> headaches) {
        mainAdapter = new MainAdapter(headaches, this);
        headacheRecyclerView.setAdapter(mainAdapter);
    }

    private void updateViewAdapter() {
        getHeadachesHistory();
        mainAdapter.updateItems(headaches);
        refreshLayout.setRefreshing(false);
        Toast.makeText(this, R.string.all_headaches_updated, Toast.LENGTH_SHORT).show();
    }


    private void swipeOnRefreshingListener() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                for (Headache headache : headaches) {
                    Log.d(TAG, "ID: " + headache.getId() + ", Date: " + headache.getDate().toString() + ", Rating: " + headache.getRating());
                }
                updateViewAdapter();
            }
        });
    }

    private boolean getHeadachesHistory() {
        Thread databaseThread = new Thread(new Runnable() {
            @Override
            public void run() {
                headaches = App.getInstance().getDatabase().headacheDao().getHeadaches();
            }
        });
        try {
            databaseThread.start();
            databaseThread.join();
            return true;
        } catch (InterruptedException e) {
            String error_msg = "Can't get headaches history from local database";
            Toast.makeText(this, error_msg, Toast.LENGTH_LONG).show();
            Log.e(TAG, error_msg);
            return false;
        }

    }

    private void insertHeadache(final Headache headache) {
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void run() {
                App.getInstance().getDatabase().headacheDao().insert(headache);
                mainAdapter.addItem(headache);
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

}