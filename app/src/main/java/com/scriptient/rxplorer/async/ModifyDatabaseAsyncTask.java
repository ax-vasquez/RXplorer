package com.scriptient.rxplorer.async;

import android.os.AsyncTask;
import android.view.View;

import com.scriptient.rxplorer.persistence.model.log.AppEmbeddedLogEntry;
import com.scriptient.rxplorer.persistence.room.repository.AppEmbeddedLogEntryRepo;

import java.lang.ref.WeakReference;

public class ModifyDatabaseAsyncTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = "ModifyDatabaseAsyncTask";

    /**
     * Modify Operations
     */
    public static final String MODIFY_DELETE = "delete";
    public static final String MODIFY_UPDATE = "update";
    public static final String MODIFY_INSERT = "insert";

    private WeakReference<View> viewWeakReference;
    private String modifyOperation;
    private AppEmbeddedLogEntry appEmbeddedLogEntry;

    public ModifyDatabaseAsyncTask( View view, String modifyOperation, AppEmbeddedLogEntry appEmbeddedLogEntry ) {

        viewWeakReference = new WeakReference<>( view );
        this.modifyOperation = modifyOperation;
        this.appEmbeddedLogEntry = appEmbeddedLogEntry;

    }

    @Override
    protected Void doInBackground(Void... voids) {

        switch ( modifyOperation ) {

            case MODIFY_INSERT:
                AppEmbeddedLogEntryRepo.insertLogEntry( viewWeakReference.get().getContext(), appEmbeddedLogEntry );
                break;
            case MODIFY_UPDATE:
                AppEmbeddedLogEntryRepo.updateLogEntry(  viewWeakReference.get().getContext(), appEmbeddedLogEntry );
                break;
            case MODIFY_DELETE:
                AppEmbeddedLogEntryRepo.deleteLogEntry( viewWeakReference.get().getContext(), appEmbeddedLogEntry );
                break;

        }

        return null;
    }

}
