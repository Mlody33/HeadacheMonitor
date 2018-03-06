package pl.mzap.headache;

import android.app.Application;
import android.arch.persistence.room.Room;

import pl.mzap.headache.database.HeadacheDB;
import pl.mzap.headache.database.entity.Headache;

public class App extends Application {

    private static App INSTANCE;
    private HeadacheDB database;

    @Override
    public void onCreate() {
        super.onCreate();

        database = Room.databaseBuilder(getApplicationContext(), HeadacheDB.class, "database")
                .build();

        INSTANCE = this;
    }

    public static App getInstance() {
        return INSTANCE;
    }

    public HeadacheDB getDatabase() {
        return database;
    }

    public void insertHeadache(Headache headache){
        database.headacheDao().insert(headache);
    }
}
