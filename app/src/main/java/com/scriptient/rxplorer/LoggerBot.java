package com.scriptient.rxplorer;


import android.content.Context;
import android.view.View;

import com.scriptient.rxplorer.async.DatabaseFetchAsyncTask;
import com.scriptient.rxplorer.async.DatabaseResetAsyncTask;
import com.scriptient.rxplorer.async.ModifyDatabaseAsyncTask;
import com.scriptient.rxplorer.persistence.model.AppEmbeddedLogEntry;
import com.scriptient.rxplorer.persistence.room.repository.AppEmbeddedLogEntryRepo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

import io.reactivex.Flowable;
import io.reactivex.Single;

public class LoggerBot {

    private static final String TAG = "LoggerBot";

    public static final String LOG_LEVEL_VERBOSE = "verbose";
    public static final String LOG_LEVEL_INFO = "info";
    public static final String LOG_LEVEL_WARN = "warn";
    public static final String LOG_LEVEL_ERROR = "error";

    private static LoggerBot sInstance;

    private ModifyDatabaseAsyncTask modifyAsyncTask;
    private DatabaseResetAsyncTask resetAsyncTask;
    private DatabaseFetchAsyncTask fetchAsyncTask;

    private LoggerBot() {

    }

    /**
     * Create and save a new log entry to the app database
     *
     * @param view                  The view this method is being called from
     * @param parentMethod          The parent method this is being called from
     * @param logLevel              The log level to create this entry with
     * @param logContent            The log content for this log entry
     * @return
     */
    public void createNewLogEntry(View view, String parentMethod, String logLevel, String logContent ) {

        AppEmbeddedLogEntry logEntry = new AppEmbeddedLogEntry();

        TimeZone timeZone = TimeZone.getTimeZone( "UTC" );
        DateFormat format = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US ); // "Z" to indicate UTC timezone
        format.setTimeZone( timeZone );
        String timestamp = format.format( new Date() ); // Implicitly uses current time

        logEntry.setTimestamp( timestamp );
        logEntry.setLogLevel( logLevel );
        logEntry.setParentMethod( parentMethod );
        logEntry.setContent( logContent );
        logEntry.setSaved( false );

        modifyAsyncTask = new ModifyDatabaseAsyncTask( view, ModifyDatabaseAsyncTask.MODIFY_INSERT, logEntry );
        modifyAsyncTask.execute();

    }

    public Flowable<List<AppEmbeddedLogEntry>> getLogEntryFlowable(Context context) {

        return AppEmbeddedLogEntryRepo.getAllLogEntriesFlowableForLogLevel( context );

    }

    public Single<AppEmbeddedLogEntry> getLogEntryById( Context context, int logId ) {

        return AppEmbeddedLogEntryRepo.getLogEntry( context, logId );

    }

    /**
     * Get all log entries by log level
     *
     * @param view                  The view this method is being called from
     * @param logLevel              The log level of the entries to get
     * @return                      List of all log entries for the specified log level
     */
    public List<AppEmbeddedLogEntry> getAllLogEntriesByLogLevel( View view, String logLevel ) throws ExecutionException, InterruptedException {

        switch ( logLevel ) {

            case LOG_LEVEL_VERBOSE:
                fetchAsyncTask = new DatabaseFetchAsyncTask( view, DatabaseFetchAsyncTask.FETCH_ALL, null, null );
                break;
            case LOG_LEVEL_INFO:
                fetchAsyncTask = new DatabaseFetchAsyncTask( view, DatabaseFetchAsyncTask.FETCH_LOG_LEVEL, LOG_LEVEL_INFO, null );
                break;
            case LOG_LEVEL_WARN:
                fetchAsyncTask = new DatabaseFetchAsyncTask( view, DatabaseFetchAsyncTask.FETCH_LOG_LEVEL, LOG_LEVEL_WARN, null );
                break;
            case LOG_LEVEL_ERROR:
                fetchAsyncTask = new DatabaseFetchAsyncTask( view, DatabaseFetchAsyncTask.FETCH_LOG_LEVEL, LOG_LEVEL_ERROR, null );
                break;

        }

        fetchAsyncTask.execute();
        return fetchAsyncTask.get();

    }

    /**
     * Get all log entries by parent method
     *
     * @param view                  The view this method is being called from
     * @param parentMethod          The parent method of the log entries to get
     * @return                      All log entries with the matching parent method
     */
    public List<AppEmbeddedLogEntry> getAllLogEntriesByParentMethod( View view, String parentMethod ) throws ExecutionException, InterruptedException {

        fetchAsyncTask = new DatabaseFetchAsyncTask( view, DatabaseFetchAsyncTask.FETCH_PARENT_METHOD, null, parentMethod );
        fetchAsyncTask.execute();
        return fetchAsyncTask.get();

    }

    /**
     * Deletes all unsaved log entries from the app database
     *
     * @param view                  The view this method is being called from
     */
    public void deleteUnsavedLogEntries( View view ) {

        resetAsyncTask = new DatabaseResetAsyncTask( view );
        resetAsyncTask.execute();

    }

    public static LoggerBot getInstance() {

        LoggerBot result = sInstance;

        if ( result == null ) {

            sInstance = new LoggerBot();

        }

        return result;

    }

}
