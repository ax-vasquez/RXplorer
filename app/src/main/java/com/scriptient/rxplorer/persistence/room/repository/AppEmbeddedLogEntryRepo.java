package com.scriptient.rxplorer.persistence.room.repository;

import android.content.Context;

import com.scriptient.rxplorer.persistence.model.log.AppEmbeddedLogEntry;
import com.scriptient.rxplorer.persistence.room.AppDatabase;

import java.util.List;

import io.reactivex.Observable;

/**
 * Repository Class for the app_embedded_log_entries table
 * <p>
 *     This class is also where you should obtain Observables to the underlying data from
 *
 * @author Armando Vasquez
 */
public class AppEmbeddedLogEntryRepo {

    private static final String TAG = "AppEmbeddedLogEntryRepo";

    private static AppEmbeddedLogEntryRepo sInstance = null;

    private static final Object lockObject = new Object();

    private AppEmbeddedLogEntryRepo(){

    }

    public static void insertLogEntry( Context context, AppEmbeddedLogEntry appEmbeddedLogEntry ) {

        AppDatabase.getDatabase( context ).logEntryDao().insert( appEmbeddedLogEntry );

    }

    public static void updateLogEntry( Context context, AppEmbeddedLogEntry appEmbeddedLogEntry ) {

        AppDatabase.getDatabase( context ).logEntryDao().update( appEmbeddedLogEntry );

    }

    public static void deleteLogEntry( Context context, AppEmbeddedLogEntry appEmbeddedLogEntry ) {

        AppDatabase.getDatabase( context ).logEntryDao().delete( appEmbeddedLogEntry );

    }

    @SuppressWarnings("unchecked")
    public static Observable<List<AppEmbeddedLogEntry>> getAllLogEntriesObservable( Context context ) {

        return Observable
                .fromArray( AppDatabase.getDatabase(context).logEntryDao().fetchAll() );

    }

    @SuppressWarnings("unchecked")
    public static Observable<List<AppEmbeddedLogEntry>> getAllLogEntriesForLogLevelObservable( Context context, String logLevel ) {

        return Observable
                .fromArray( AppDatabase.getDatabase(context).logEntryDao().fetchAllForLogLevel( logLevel ) );

    }

    @SuppressWarnings("unchecked")
    public static Observable<List<AppEmbeddedLogEntry>> getAllLogEntriesForParentMethodObservable ( Context context, String parentMethod ) {

        return Observable
                .fromArray( AppDatabase.getDatabase(context).logEntryDao().fetchAllForParentMethodName( parentMethod ) );

    }

    /**
     * Uses the logEntryDao to fetch all log entries in the app_embedded_log_entries table
     *
     * @param context                   The context to use when getting the app database
     * @return                          All log entries in the app_embedded_log_entries table
     */
    public static List<AppEmbeddedLogEntry> getAllLogEntries( Context context ) {

        return AppDatabase.getDatabase( context ).logEntryDao().fetchAll();

    }

    /**
     * Uses the logEntryDao to fetch all log entries in the app_embedded_log_entries table with
     * the specified log level
     *
     * @param context                   The context to use when getting the app database
     * @param logLevel                  The log level of the entries to fetch
     * @return                          All log entries in the app_embedded_log_entries table with the specified log level
     */
    public static List<AppEmbeddedLogEntry> getAllLogEntriesForLogLevel( Context context, String logLevel ) {

        return AppDatabase.getDatabase( context ).logEntryDao().fetchAllForLogLevel( logLevel );

    }

    /**
     * Uses the logEntryDao to fetch all log entries in the app_embedded_log_entries table with the
     * specified parent method
     *
     * @param context                   The context to use when getting the app database
     * @param parentMethod              The parent method of the entries to fetch
     * @return                          All log entries in the app_embedded_log_entries table with the specified parent method
     */
    public static List<AppEmbeddedLogEntry> getAllLogEntriesForParentMethod( Context context, String parentMethod ) {

        return AppDatabase.getDatabase( context ).logEntryDao().fetchAllForParentMethodName( parentMethod );

    }

    /**
     * This method returns a thread-safe reference to this repository singleton
     *
     * @return          The AppEmbeddedLogEntryRepo singleton
     */
    public static AppEmbeddedLogEntryRepo getInstance() {

        AppEmbeddedLogEntryRepo result = sInstance;

        if ( result == null ) {

            // Ensures thread safety
            synchronized ( lockObject ) {

                result = sInstance;
                if( result == null ) {

                    sInstance = result = new AppEmbeddedLogEntryRepo();

                }

            }

        }

        return result;

    }

}
