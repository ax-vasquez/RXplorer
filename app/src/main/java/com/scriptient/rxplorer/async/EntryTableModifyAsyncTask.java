package com.scriptient.rxplorer.async;

import android.os.AsyncTask;
import android.view.View;

import com.scriptient.rxplorer.persistence.model.LoggerBotEntry;
import com.scriptient.rxplorer.persistence.room.repository.LoggerBotEntryRepo;

import java.lang.ref.WeakReference;

public class EntryTableModifyAsyncTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = "EntryTableModifyAsyncTask";

    /**
     * Modify Operations
     */
    public static final String MODIFY_DELETE = "delete";
    public static final String MODIFY_UPDATE = "update";
    public static final String MODIFY_INSERT = "insert";

    private WeakReference<View> viewWeakReference;
    private String modifyOperation;
    private LoggerBotEntry loggerBotEntry;

    public EntryTableModifyAsyncTask(View view, String modifyOperation, LoggerBotEntry loggerBotEntry) {

        viewWeakReference = new WeakReference<>( view );
        this.modifyOperation = modifyOperation;
        this.loggerBotEntry = loggerBotEntry;

    }

    @Override
    protected Void doInBackground(Void... voids) {

        switch ( modifyOperation ) {

            case MODIFY_INSERT:
                LoggerBotEntryRepo.insertLogEntry( viewWeakReference.get().getContext(), loggerBotEntry);
                break;
            case MODIFY_UPDATE:
                LoggerBotEntryRepo.updateLogEntry(  viewWeakReference.get().getContext(), loggerBotEntry);
                break;
            case MODIFY_DELETE:
                LoggerBotEntryRepo.deleteLogEntry( viewWeakReference.get().getContext(), loggerBotEntry);
                break;

        }

        return null;
    }

}
