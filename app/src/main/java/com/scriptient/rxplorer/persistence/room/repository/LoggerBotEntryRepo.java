package com.scriptient.rxplorer.persistence.room.repository;

import android.content.Context;

import com.scriptient.rxplorer.persistence.model.LoggerBotEntry;
import com.scriptient.rxplorer.persistence.room.AppDatabase;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * Repository Class for the app_embedded_log_entries table
 * <p>
 *     This class is also where you should obtain Observables to the underlying data from
 *
 * @author Armando Vasquez
 */
public class LoggerBotEntryRepo {

    public static void insertLogEntry( Context context, LoggerBotEntry loggerBotEntry) {

        AppDatabase.getDatabase( context ).logEntryDao().insert(loggerBotEntry);

    }

    public static void updateLogEntry( Context context, LoggerBotEntry loggerBotEntry) {

        AppDatabase.getDatabase( context ).logEntryDao().update(loggerBotEntry);

    }

    public static void deleteLogEntry( Context context, LoggerBotEntry loggerBotEntry) {

        AppDatabase.getDatabase( context ).logEntryDao().delete(loggerBotEntry);

    }

    /**
     * Uses the logEntryDao to fetch all log entries in the app_embedded_log_entries table
     *
     * @param context                   The context to use when getting the app database
     * @return                          All log entries in the app_embedded_log_entries table
     */
    public static List<LoggerBotEntry> getAllLogEntries(Context context ) {

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
    public static List<LoggerBotEntry> getAllLogEntriesForLogLevel(Context context, String logLevel ) {

        return AppDatabase.getDatabase( context ).logEntryDao().fetchAllForLogLevel( logLevel );

    }


    public static Flowable<List<LoggerBotEntry>> getAllLogEntriesFlowableForLogLevel(Context context ) {

        return AppDatabase.getDatabase( context ).logEntryDao().getAllEntriesFlowable();

    }

    public static Single<LoggerBotEntry> getLogEntry(Context context, int logId ) {

        return AppDatabase.getDatabase( context ).logEntryDao().getLogEntrySingle( logId );

    }

    /**
     * Uses the logEntryDao to fetch all log entries in the app_embedded_log_entries table with the
     * specified parent method
     *
     * @param context                   The context to use when getting the app database
     * @param parentMethod              The parent method of the entries to fetch
     * @return                          All log entries in the app_embedded_log_entries table with the specified parent method
     */
    public static List<LoggerBotEntry> getAllLogEntriesForParentMethod(Context context, String parentMethod ) {

        return AppDatabase.getDatabase( context ).logEntryDao().fetchAllForParentMethodName( parentMethod );

    }

}
