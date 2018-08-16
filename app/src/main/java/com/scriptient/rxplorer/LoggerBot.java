package com.scriptient.rxplorer;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.scriptient.rxplorer.async.EntryTableFetchAsyncTask;
import com.scriptient.rxplorer.async.EntryTableModifyAsyncTask;
import com.scriptient.rxplorer.async.EntryTableResetAsyncTask;
import com.scriptient.rxplorer.async.ParameterTableModifyAsyncTask;
import com.scriptient.rxplorer.persistence.model.LoggerBotEntry;
import com.scriptient.rxplorer.persistence.model.LoggerBotEntryParameter;
import com.scriptient.rxplorer.persistence.room.repository.LoggerBotEntryParameterRepo;
import com.scriptient.rxplorer.persistence.room.repository.LoggerBotEntryRepo;

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

    /**
     * This is used to assign unique IDs to log entries - even if the database is reset, DO NOT
     * reset this value. This ensures that all log IDs are unique
     */
    private static Integer totalLogEntries = 0;

    public static final String NO_DATA = "No Data";

    private static final String TAG = "LoggerBot";

    public static final String LOG_LEVEL_VERBOSE = "verbose";
    public static final String LOG_LEVEL_INFO = "info";
    public static final String LOG_LEVEL_WARN = "warn";
    public static final String LOG_LEVEL_ERROR = "error";

    private static LoggerBot sInstance;

    private EntryTableModifyAsyncTask modifyAsyncTask;
    private EntryTableResetAsyncTask resetAsyncTask;
    private EntryTableFetchAsyncTask fetchAsyncTask;

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
    public void createNewLogEntry(View view, String event, String parentMethod, String logLevel, String logContent, List<LoggerBotEntryParameter> parameters ) {

        LoggerBotEntry logEntry = new LoggerBotEntry();

        logEntry.setLogId( totalLogEntries ); // Set ID to the sequential count of this log entry

        logEntry.setEvent( event );
        logEntry.setTimestamp( _generateTimestamp() );
        logEntry.setLogLevel( logLevel );
        logEntry.setParentMethod( parentMethod );
        logEntry.setContent( logContent );
        logEntry.setSaved( false );

        for ( LoggerBotEntryParameter parameter : parameters ) {

            parameter.setParentLogId( totalLogEntries );

        }

        modifyAsyncTask = new EntryTableModifyAsyncTask( view, EntryTableModifyAsyncTask.MODIFY_INSERT, logEntry );
        modifyAsyncTask.execute();

        // CURRENT LIMITATION
        // Cannot use autogeneration of IDs for log entries as the ID won't be assigned until the entry has been added
        ParameterTableModifyAsyncTask parameterInsertTask = new ParameterTableModifyAsyncTask( view, parameters );
        parameterInsertTask.execute();

        totalLogEntries++;

    }

    /**
     * Obtain a reactive list of log entries
     *
     * @param context
     * @return
     */
    public Flowable<List<LoggerBotEntry>> getLogEntryFlowable( Context context) {

        return LoggerBotEntryRepo.getAllLogEntriesFlowableForLogLevel( context );

    }

    /**
     * Obtain a reactive LoggerBotEntry
     *
     * @param context
     * @param logId
     * @return
     */
    public Single<LoggerBotEntry> getLogEntryById( Context context, int logId ) {

        return LoggerBotEntryRepo.getLogEntry( context, logId );

    }

    /**
     * Obtain a reactive list of parameters for the given log ID
     *
     * @param context
     * @param logId
     * @return
     */
    public Single<List<LoggerBotEntryParameter>> getLogEntryParameters( Context context, int logId ) {

        return LoggerBotEntryParameterRepo.getParametersSingleForLogEntryId( context, logId );

    }

    /**
     * Get all log entries by log level
     *
     * @param view                  The view this method is being called from
     * @param logLevel              The log level of the entries to get
     * @return                      List of all log entries for the specified log level
     */
    public List<LoggerBotEntry> getAllLogEntriesByLogLevel(View view, String logLevel ) throws ExecutionException, InterruptedException {

        switch ( logLevel ) {

            case LOG_LEVEL_VERBOSE:
                fetchAsyncTask = new EntryTableFetchAsyncTask( view, EntryTableFetchAsyncTask.FETCH_ALL, null, null );
                break;
            case LOG_LEVEL_INFO:
                fetchAsyncTask = new EntryTableFetchAsyncTask( view, EntryTableFetchAsyncTask.FETCH_LOG_LEVEL, LOG_LEVEL_INFO, null );
                break;
            case LOG_LEVEL_WARN:
                fetchAsyncTask = new EntryTableFetchAsyncTask( view, EntryTableFetchAsyncTask.FETCH_LOG_LEVEL, LOG_LEVEL_WARN, null );
                break;
            case LOG_LEVEL_ERROR:
                fetchAsyncTask = new EntryTableFetchAsyncTask( view, EntryTableFetchAsyncTask.FETCH_LOG_LEVEL, LOG_LEVEL_ERROR, null );
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
    public List<LoggerBotEntry> getAllLogEntriesByParentMethod(View view, String parentMethod ) throws ExecutionException, InterruptedException {

        fetchAsyncTask = new EntryTableFetchAsyncTask( view, EntryTableFetchAsyncTask.FETCH_PARENT_METHOD, null, parentMethod );
        fetchAsyncTask.execute();
        return fetchAsyncTask.get();

    }

    /**
     * Deletes all unsaved log entries from the app database
     *
     * @param view                  The view this method is being called from
     */
    public void deleteUnsavedLogEntries( View view ) {

        resetAsyncTask = new EntryTableResetAsyncTask( view );
        resetAsyncTask.execute();

    }

    public static LoggerBot getInstance() {

        LoggerBot result = sInstance;

        if ( result == null ) {

            sInstance = new LoggerBot();

        }

        return result;

    }

    /**
     * Helper method to generate an ISO-8601 compliant timestamp
     *
     * @return
     */
    private String _generateTimestamp() {

        TimeZone timeZone = TimeZone.getTimeZone( "UTC" );
        DateFormat format = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US ); // "Z" to indicate UTC timezone
        format.setTimeZone( timeZone );
        return format.format( new Date() ); // Implicitly uses current time

    }

}
