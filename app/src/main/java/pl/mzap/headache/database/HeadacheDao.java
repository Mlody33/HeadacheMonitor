package pl.mzap.headache.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import pl.mzap.headache.database.entity.Headache;

@Dao
public interface HeadacheDao {

    @Query("SELECT * FROM Headache")
    List<Headache> getHeadaches();

    @Insert
    void insertAll(List<Headache> headaches);

    @Insert
    void insert(Headache headache);

    @Update
    void update(Headache headache);

    @Delete
    void delete(Headache headache);

}
