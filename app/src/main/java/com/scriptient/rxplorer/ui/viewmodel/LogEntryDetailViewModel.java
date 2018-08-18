package com.scriptient.rxplorer.ui.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.view.View;

import com.scriptient.rxplorer.LoggerBot;
import com.scriptient.rxplorer.persistence.model.LoggerBotEntry;
import com.scriptient.rxplorer.persistence.model.LoggerBotEntryParameter;

import java.lang.ref.WeakReference;
import java.util.List;

import io.reactivex.Single;

public class LogEntryDetailViewModel extends ViewModel {

    private LoggerBot loggerBot;
    private WeakReference<View> viewWeakReference;

    public LogEntryDetailViewModel( View view ) {

        loggerBot = LoggerBot.getInstance();
        viewWeakReference = new WeakReference<>( view );

    }

    public Single<LoggerBotEntry> getLogEntryById( int logId ) {

        return loggerBot.getLogEntryById( viewWeakReference.get().getContext(), logId );

    }

    public Single<List<LoggerBotEntryParameter>> getLogEntryParameters( int logId ) {

        return loggerBot.getLogEntryParameters( viewWeakReference.get().getContext(), logId );

    }

}
