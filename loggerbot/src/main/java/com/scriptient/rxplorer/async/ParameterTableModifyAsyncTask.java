package com.scriptient.rxplorer.async;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.scriptient.rxplorer.persistence.model.LoggerBotEntryParameter;
import com.scriptient.rxplorer.persistence.room.repository.LoggerBotEntryParameterRepo;

import java.lang.ref.WeakReference;
import java.util.List;

public class ParameterTableModifyAsyncTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = "ParameterInsertTask";

    private WeakReference<View> viewWeakReference;
    private List<LoggerBotEntryParameter> loggerBotEntryParameters;

    public ParameterTableModifyAsyncTask( View view, List<LoggerBotEntryParameter> loggerBotEntryParameters ) {

        viewWeakReference = new WeakReference<>( view );
        this.loggerBotEntryParameters = loggerBotEntryParameters;

    }



    @Override
    protected Void doInBackground(Void... voids) {

        for ( LoggerBotEntryParameter parameter : loggerBotEntryParameters ) {

            LoggerBotEntryParameterRepo.insertLogEntryParameter( viewWeakReference.get().getContext(), parameter );

        }

        return null;
    }
}
