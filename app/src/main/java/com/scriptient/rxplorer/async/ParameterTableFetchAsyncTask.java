package com.scriptient.rxplorer.async;

import android.os.AsyncTask;
import android.view.View;

import com.scriptient.rxplorer.persistence.model.LoggerBotEntryParameter;
import com.scriptient.rxplorer.persistence.room.repository.LoggerBotEntryParameterRepo;

import java.lang.ref.WeakReference;
import java.util.List;

public class ParameterTableFetchAsyncTask extends AsyncTask<Void, Void, List<LoggerBotEntryParameter>> {

    private WeakReference<View> viewWeakReference;

    private Long logEntryId;

    public ParameterTableFetchAsyncTask( View view, Long logEntryId ) {

        this.logEntryId = logEntryId;
        viewWeakReference = new WeakReference<>( view );

    }

    @Override
    protected List<LoggerBotEntryParameter> doInBackground(Void... voids) {

        return LoggerBotEntryParameterRepo.getParametersListForLogEntryId( viewWeakReference.get().getContext(), logEntryId );

    }

}
