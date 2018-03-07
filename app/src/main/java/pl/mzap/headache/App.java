package pl.mzap.headache;

import android.app.Application;
import android.arch.persistence.room.Room;

import pl.mzap.headache.database.HeadacheDatabase;
import pl.mzap.headache.database.entity.Headache;

public class App extends Application {

    private static App INSTANCE;
    private HeadacheDatabase database;

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

}
