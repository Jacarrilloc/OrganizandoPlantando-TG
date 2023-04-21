package com.example.opcv.repository.local_db;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Garden.class}, version = 1, exportSchema = false)
public abstract class GardenDatabase extends RoomDatabase {
    public abstract GardenDao gardenDao();

    private static volatile GardenDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static GardenDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (GardenDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    GardenDatabase.class, "garden_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
