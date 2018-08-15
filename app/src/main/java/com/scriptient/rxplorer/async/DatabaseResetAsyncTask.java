package com.scriptient.rxplorer.async;

import android.os.AsyncTask;
import android.view.View;

import com.scriptient.rxplorer.persistence.model.LoggerBotEntry;
import com.scriptient.rxplorer.persistence.room.repository.LoggerBotEntryRepo;

import java.lang.ref.WeakReference;
import java.util.List;

public class DatabaseResetAsyncTask extends AsyncTask<Void, Void, Void> {

    private WeakReference<View> viewWeakReference;

    public DatabaseResetAsyncTask( View view ) {

        viewWeakReference = new WeakReference<>( view );

    }

    @Override
    protected Void doInBackground(Void... voids) {

        List<LoggerBotEntry> allEntries =
                LoggerBotEntryRepo.getAllLogEntries( viewWeakReference.get().getContext() );

        for ( LoggerBotEntry entry : allEntries ) {

            if ( !entry.getSaved() ) {

                LoggerBotEntryRepo.deleteLogEntry( viewWeakReference.get().getContext(), entry );

            }

        }

        return null;
    }

}
