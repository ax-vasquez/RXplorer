package com.scriptient.rxplorer.async;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.scriptient.rxplorer.persistence.model.LoggerBotEntry;
import com.scriptient.rxplorer.persistence.model.LoggerBotEntryParameter;
import com.scriptient.rxplorer.persistence.room.repository.LoggerBotEntryParameterRepo;
import com.scriptient.rxplorer.persistence.room.repository.LoggerBotEntryRepo;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InsertEntryAsyncTask extends AsyncTask<LoggerBotEntry, Void, Void> {

    private static final String TAG = "InsertEntryAsyncTask";

    private List<LoggerBotEntryParameter> entryParameters;
    private WeakReference<View> viewWeakReference;

    public InsertEntryAsyncTask( View view, Map<String, String> logEntryParameterMap ) {

        viewWeakReference = new WeakReference<>( view );
        entryParameters = new ArrayList<>();

        Set<String> mapKeys =  logEntryParameterMap.keySet();

        for ( String key : mapKeys ) {

            Log.i(TAG, "InsertEntryAsyncTask: Mapping \"" + key + "\"");

            LoggerBotEntryParameter newParameter = new LoggerBotEntryParameter();

            newParameter.setParameterDataType( key );
            newParameter.setParameterValue( logEntryParameterMap.get( key ) );

            entryParameters.add( newParameter );

        }

    }

    @Override
    protected Void doInBackground(LoggerBotEntry... loggerBotEntries) {

        if ( loggerBotEntries.length > 1 ) {    // Disallow multiple log entry insertion

            Log.i(TAG, "doInBackground: Inserting multiple entries is not currently supported - skipping insert operation");
            return null;

        } else {

            Log.i(TAG, "doInBackground: Inserting log entry");
            LoggerBotEntry entry = loggerBotEntries[0];
            Long entryId = LoggerBotEntryRepo.insertLogEntry( viewWeakReference.get().getContext(), entry );

            // Set the parent log ID for all parameters
            for ( LoggerBotEntryParameter parameter : entryParameters ) {

                Log.i(TAG, "doInBackground: Inserting new parameter of type: " + parameter.getParameterDataType() );
                parameter.setParentLogId( entryId );
                LoggerBotEntryParameterRepo.insertLogEntryParameter( viewWeakReference.get().getContext(), parameter );

            }

            return null;

        }

    }

}
