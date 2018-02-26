package pl.mzap.headache;

import android.app.Application;
import android.arch.persistence.room.Room;

import pl.mzap.headache.database.HeadacheApp;

public class App extends Application {

    private static App INSTANCE;
    private HeadacheApp database;

    @Override
    public void onCreate() {
        super.onCreate();

        database = Room.databaseBuilder(getApplicationContext(), HeadacheApp.class, "database")
                .build();

        INSTANCE = this;
    }

    public static App getInstance() {
        return INSTANCE;
    }

    public HeadacheApp getDatabase() {
        return database;
    }
}
