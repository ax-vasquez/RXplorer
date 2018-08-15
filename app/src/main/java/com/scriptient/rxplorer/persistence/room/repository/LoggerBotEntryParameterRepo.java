package com.scriptient.rxplorer.persistence.room.repository;

import android.content.Context;

import com.scriptient.rxplorer.persistence.model.LoggerBotEntryParameter;
import com.scriptient.rxplorer.persistence.room.AppDatabase;

import java.util.List;

import io.reactivex.Single;

public class LoggerBotEntryParameterRepo {

    private static LoggerBotEntryParameterRepo sInstance = null;

    private static final Object lockObject = new Object();

    private LoggerBotEntryParameterRepo() {

    }

    public static Single<List<LoggerBotEntryParameter>> getParametersForLogEntryId( Context context, int logId ) {

        return AppDatabase.getDatabase( context ).parameterDao().getParametersForLogEntrySingle( logId );

    }

    /**
     * This method returns a thread-safe reference to this repository singleton
     *
     * @return          The LoggerBotEntryRepo singleton
     */
    public static LoggerBotEntryParameterRepo getInstance() {

        LoggerBotEntryParameterRepo result = sInstance;

        if ( result == null ) {

            // Ensures thread safety
            synchronized ( lockObject ) {

                result = sInstance;
                if( result == null ) {

                    sInstance = result = new LoggerBotEntryParameterRepo();

                }

            }

        }

        return result;

    }

}
