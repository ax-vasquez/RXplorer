package com.scriptient.rxplorer.persistence.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.scriptient.rxplorer.persistence.model.log.AppEmbeddedLogEntry;

import java.util.List;

/**
 * Data Access Object for app-embedded log entries
 *
 * @author Armando Vasquez
 */
@Dao
public interface AppEmbeddedLogEntryDao {

    @Insert
    void insert( AppEmbeddedLogEntry appEmbeddedLogEntry );

    @Update
    void update( AppEmbeddedLogEntry appEmbeddedLogEntry );

    @Delete
    void delete( AppEmbeddedLogEntry appEmbeddedLogEntry );

    /**
     * Query to select all entries in the app_embedded_log_entries table with the specified parent
     * method name
     *
     * @param parentMethodName          The name of the parent method of the entries to fetch
     * @return                          All entries with the specified parent method name
     */
    @Query("SELECT * FROM app_embedded_log_entries WHERE parent_method IS :parentMethodName")
    List<AppEmbeddedLogEntry> fetchAllForParentMethodName( String parentMethodName );

    /**
     * Query to select all entries in the app_embedded_log_entries table of the corresponding
     * log level
     *
     * @param logLevel                  The log level of the entries to fetch
     * @return                          All entries with the specified log level
     */
    @Query("SELECT * FROM app_embedded_log_entries WHERE log_level IS :logLevel")
    List<AppEmbeddedLogEntry> fetchAllForLogLevel( String logLevel );

    /**
     * Query to select all entries in the app_embedded_log_entries table
     *
     * @return              All entries in app_embedded_log_entries table
     */
    @Query( "SELECT * FROM app_embedded_log_entries" )
    List<AppEmbeddedLogEntry> fetchAll();

}
