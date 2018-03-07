package pl.mzap.headache.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import pl.mzap.headache.database.converter.TimeConverter;
import pl.mzap.headache.database.entity.Headache;

@Database(entities = {Headache.class}, version = 1)
@TypeConverters({TimeConverter.class})
public abstract class HeadacheDatabase extends RoomDatabase {
    public abstract HeadacheDao headacheDao();
}
