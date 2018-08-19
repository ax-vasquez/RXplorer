package com.scriptient.rxplorer.persistence.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.scriptient.rxplorer.persistence.model.LoggerBotEntry;
import com.scriptient.rxplorer.persistence.model.LoggerBotEntryParameter;
import com.scriptient.rxplorer.persistence.room.dao.LoggerBotEntryDao;
import com.scriptient.rxplorer.persistence.room.dao.LoggerBotEntryParameterDao;

/**
 * Database for the application
 *
 * @see <a href="https://android.jlelse.eu/android-architecture-components-room-relationships-bf473510c14a">Room Relationships</a>
 */
@Database( entities = {
        LoggerBotEntry.class,
        LoggerBotEntryParameter.class
}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase database;

    public abstract LoggerBotEntryDao logEntryDao();

    public abstract LoggerBotEntryParameterDao parameterDao();

    /**
     * Thread-safe method to retrieve the app database
     * <p>
     *     If the database has not been created yet, then this method will create the
     *     database and name it <q>app_data</q>
     *
     * @param context       The application context to use
     * @return              The app database
     */
    public static AppDatabase getDatabase(final Context context) {
        if (database == null) {
            synchronized (AppDatabase.class) {
                if (database == null) {

                    // Create database here
                    database = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "app_data")
                            .build();

                }
            }
        }
        return database;
    }

}
