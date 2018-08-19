package com.scriptient.rxplorer.persistence.room.repository;

import android.content.Context;
import android.util.Log;

import com.scriptient.rxplorer.persistence.model.LoggerBotEntryParameter;
import com.scriptient.rxplorer.persistence.room.AppDatabase;

import java.util.List;

import io.reactivex.Single;

public class LoggerBotEntryParameterRepo {

    private static final String TAG = "LoggerBotEntryParameter";

    public static void insertLogEntryParameter( Context context, LoggerBotEntryParameter parameter ) {

        Log.i(TAG, "insertLogEntryParameter: Inserting Parameter");
        AppDatabase.getDatabase( context ).parameterDao().insert( parameter );

    }

    public static void updateLogEntryParameter( Context context, LoggerBotEntryParameter parameter ) {

        AppDatabase.getDatabase( context ).parameterDao().update( parameter );

    }

    public static void deleteLogEntryParameter( Context context, LoggerBotEntryParameter parameter ) {

        AppDatabase.getDatabase( context ).parameterDao().delete( parameter );

    }

    public static Single<List<LoggerBotEntryParameter>> getParametersSingleForLogEntryId(Context context, Long logId ) {

        return AppDatabase.getDatabase( context ).parameterDao().getParametersForLogEntrySingle( logId );

    }

    public static List<LoggerBotEntryParameter> getParametersListForLogEntryId( Context context, Long logEntryId ) {

        return AppDatabase.getDatabase( context ).parameterDao().getParametersForLogEntry( logEntryId );

    }

}
