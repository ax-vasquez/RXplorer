package com.scriptient.rxplorer.persistence.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.scriptient.rxplorer.persistence.model.LoggerBotEntry;
import com.scriptient.rxplorer.ui.LoggerViewFragment;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * Data Access Object for app-embedded log entries
 *
 * @author Armando Vasquez
 */
@Dao
public interface LoggerBotEntryDao {

    @Insert
    void insert( LoggerBotEntry loggerBotEntry );

    @Update
    void update( LoggerBotEntry loggerBotEntry );

    @Delete
    void delete( LoggerBotEntry loggerBotEntry );

    /**
     * Query to select all entries in the app_embedded_log_entries table with the specified parent
     * method name
     *
     * @param parentMethodName          The name of the parent method of the entries to fetch
     * @return                          All entries with the specified parent method name
     */
    @Query("SELECT * FROM logger_bot_entries WHERE parent_method IS :parentMethodName")
    List<LoggerBotEntry> fetchAllForParentMethodName(String parentMethodName );

    /**
     * Query to select all entries in the app_embedded_log_entries table of the corresponding
     * log level
     *
     * @param logLevel                  The log level of the entries to fetch
     * @return                          All entries with the specified log level
     */
    @Query("SELECT * FROM logger_bot_entries WHERE log_level IS :logLevel")
    List<LoggerBotEntry> fetchAllForLogLevel(String logLevel );

    /**
     * Returns a Flowable that reflects the current state of the entire list of log entries in the
     * app database
     * <p>
     *     This is essential to setting up the Room API to be reactive using RxJava2 - Consumers
     *     are subscribed to this flowable. Each time there is a <b>change of any kind</b> in the
     *     list of log entries, the flowable emits the entire list of log entries in its current state
     *     after the change takes place
     * <p>
     *     The Consumer's behavior is defined in {@link com.scriptient.rxplorer.ui.LoggerViewFragment.ListConsumer}
     *
     * @return                          A Flowable that emits the current list of all log entries to its observers after a change takes place
     */
    @Query("SELECT * FROM logger_bot_entries")
    Flowable<List<LoggerBotEntry>> getAllEntriesFlowable();

    /**
     * Technically a <q>reactive</q> element, though this is not actively used in a reactive manner.
     * All this is currently used for is obtaining log entry data using the RxJava2 Single object
     * <p>
     *     Note that this RxJava object behaves quite differently from a Flowable in that the
     *     single emits its contents onSubscribe, whereas a Flowable emits its contents each time
     *     there is a change
     *
     * @param logId                     The ID of the log entry to fetch
     * @return                          A Single that emits all data for this log entry when its subscribed to
     */
    @Query( "SELECT * FROM logger_bot_entries WHERE log_id IS :logId" )
    Single<LoggerBotEntry> getLogEntrySingle(Integer logId );

    /**
     * Query to select all entries in the app_embedded_log_entries table
     *
     * @return              All entries in app_embedded_log_entries table
     */
    @Query( "SELECT * FROM logger_bot_entries" )
    List<LoggerBotEntry> fetchAll();

}
