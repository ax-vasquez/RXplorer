package com.scriptient.rxplorer.async;

import android.os.AsyncTask;
import android.view.View;

import com.scriptient.rxplorer.persistence.model.log.AppEmbeddedLogEntry;
import com.scriptient.rxplorer.persistence.room.repository.AppEmbeddedLogEntryRepo;

import java.lang.ref.WeakReference;
import java.util.List;

public class DatabaseResetAsyncTask extends AsyncTask<Void, Void, Void> {

    private WeakReference<View> viewWeakReference;

    public DatabaseResetAsyncTask( View view ) {

        viewWeakReference = new WeakReference<>( view );

    }

    @Override
    protected Void doInBackground(Void... voids) {

        List<AppEmbeddedLogEntry> allEntries =
                AppEmbeddedLogEntryRepo.getAllLogEntries( viewWeakReference.get().getContext() );

        for ( AppEmbeddedLogEntry entry : allEntries ) {

            AppEmbeddedLogEntryRepo.deleteLogEntry( viewWeakReference.get().getContext(), entry );

        }

        return null;
    }

}
