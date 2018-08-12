package com.scriptient.rxplorer;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.view.View;

import com.scriptient.rxplorer.persistence.model.log.AppEmbeddedLogEntry;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private LoggerBot loggerBot;
    private WeakReference<View> viewWeakReference;

    public ViewModelFactory(View view ) {

        viewWeakReference = new WeakReference<>( view );
        loggerBot = LoggerBot.getInstance();

    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        if ( modelClass.isAssignableFrom( LoggerViewViewModel.class ) ) {

            // Technically an unchecked cast - ignore the warning
            return (T) new LoggerViewViewModel( viewWeakReference.get() );

        } else {

            throw  new IllegalArgumentException("Unknown ViewModel class");

        }

    }

    private List<AppEmbeddedLogEntry> _fetchEntriesForLogLevel( String logLevel ) {

        List<AppEmbeddedLogEntry> logEntries = new ArrayList<>();

        try {
            logEntries = loggerBot.getAllLogEntriesByLogLevel( viewWeakReference.get(), logLevel );
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return logEntries;

    }

}
