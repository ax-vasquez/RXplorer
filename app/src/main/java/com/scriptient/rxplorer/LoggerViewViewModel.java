package com.scriptient.rxplorer;

import android.arch.lifecycle.ViewModel;
import android.util.Log;
import android.view.View;

import com.scriptient.rxplorer.persistence.model.log.AppEmbeddedLogEntry;

import java.lang.ref.WeakReference;
import java.util.List;

import io.reactivex.Flowable;

public class LoggerViewViewModel extends ViewModel {

    private static final String TAG = "LoggerViewViewModel";

    private WeakReference<View> viewWeakReference;
    private LoggerBot loggerBot;

    public LoggerViewViewModel( View view ) {

        viewWeakReference = new WeakReference<>( view );
        loggerBot = LoggerBot.getInstance();

    }

    public Flowable<List<AppEmbeddedLogEntry>> getAllLogEntries() {

        Log.i(TAG, "getAllLogEntries: Returning Log Entries Flowable (per the currentLogState in the LoggerViewFragment: " + LoggerViewFragment.currentLogState +  ")");

        return loggerBot.getLogEntryFlowable( viewWeakReference.get().getContext() );

    }

}
