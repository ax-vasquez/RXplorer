package com.scriptient.rxplorer.ui;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.scriptient.rxplorer.LoggerBot;

import java.lang.ref.WeakReference;

public class ViewModelFactory implements ViewModelProvider.Factory {
    
    private static final String TAG = "ViewModelFactory";

    private WeakReference<View> viewWeakReference;

    // TODO: Figure out how to fix the code so that this attribute is not needed
    // The issue has to do with when the Singleton is created
    private LoggerBot loggerBot;

    public ViewModelFactory( View view ) {

        Log.i(TAG, "ViewModelFactory: Initializing ViewModelFactory");
        viewWeakReference = new WeakReference<>( view );
        loggerBot = LoggerBot.getInstance();

    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        View view = viewWeakReference.get();

        Log.i(TAG, "create: Creating new ViewModel");
        if ( modelClass.isAssignableFrom( LoggerViewViewModel.class ) ) {

            Log.i(TAG, "create: Creating LoggerView");
            return (T) new LoggerViewViewModel( view );

        }  else if ( modelClass.isAssignableFrom( LogEntryDetailViewModel.class ) ) {

            Log.i(TAG, "create: Creating LogEntryDetailViewModel");
            return (T) new LogEntryDetailViewModel( view );

        }  else {

            throw  new IllegalArgumentException("Unknown ViewModel class");

        }

    }

}
