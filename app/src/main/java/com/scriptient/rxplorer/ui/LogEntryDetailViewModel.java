package com.scriptient.rxplorer.ui;

import android.arch.lifecycle.ViewModel;
import android.view.View;

import com.scriptient.rxplorer.LoggerBot;
import com.scriptient.rxplorer.persistence.model.LoggerBotEntry;

import java.lang.ref.WeakReference;

import io.reactivex.Single;

public class LogEntryDetailViewModel extends ViewModel {

    private LoggerBot loggerBot;
    private WeakReference<View> viewWeakReference;

    public LogEntryDetailViewModel( View view ) {

        loggerBot = LoggerBot.getInstance();
        viewWeakReference = new WeakReference<>( view );

    }

    public Single<LoggerBotEntry> getLogEntryById(int logId ) {

        return loggerBot.getLogEntryById( viewWeakReference.get().getContext(), logId );

    }

}
