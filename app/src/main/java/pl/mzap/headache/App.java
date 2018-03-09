package pl.mzap.headache;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.persistence.room.Room;

import java.text.SimpleDateFormat;

import pl.mzap.headache.database.HeadacheDatabase;
import pl.mzap.headache.database.entity.Headache;

public class App extends Application {

    private static App INSTANCE;
    private HeadacheDatabase database;
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd MMM");
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

    @Override
    public void onCreate() {
        super.onCreate();

        database = Room.databaseBuilder(getApplicationContext(), HeadacheDatabase.class, "database")
                .build();

        INSTANCE = this;
    }

    public static App getInstance() {
        return INSTANCE;
    }

    public HeadacheDatabase getDatabase() {
        return database;
    }

    public SimpleDateFormat getDateFormat(){
        return dateFormat;
    }

    public SimpleDateFormat getTimeFormat(){
        return timeFormat;
    }

}
