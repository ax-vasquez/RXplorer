package com.scriptient.rxplorer.persistence.room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.scriptient.rxplorer.persistence.model.LoggerBotEntryParameter;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface LoggerBotEntryParameterDao {

    @Insert
    void insert( LoggerBotEntryParameter parameter );

    @Update
    void update( LoggerBotEntryParameter parameter );

    @Delete
    void delete( LoggerBotEntryParameter parameter );

    /**
     * Gets a Single that emits the list of parameters related to the specified log id when subscribed
     * to
     *
     * @param logId                     The ID of the log entry whose parameters are observed in the returned Single
     * @return                          A Single that emits the list of parameters associated with the specified log entry
     */
    @Query( "SELECT * FROM entry_parameters WHERE log_id IS :logId" )
    Single<List<LoggerBotEntryParameter>> getParametersForLogEntrySingle( int logId );

}
