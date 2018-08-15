package com.scriptient.rxplorer.async;

import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.view.View;

import com.scriptient.rxplorer.persistence.model.LoggerBotEntry;
import com.scriptient.rxplorer.persistence.room.repository.LoggerBotEntryRepo;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Async Task to handle all fetch operations with the app_embedded_log_entries table
 *
 * @author Armando Vasquez
 */
public class DatabaseFetchAsyncTask extends AsyncTask<Void, Void, List<LoggerBotEntry>> {

    private static final String TAG = "DatabaseFetchAsyncTask";

    public static final String FETCH_ALL = "all";

    public static final String FETCH_LOG_LEVEL = "log_level";

    public static final String FETCH_LOG_LEVEL_FLOWABLE = "log_level_flowable";

    public static final String FETCH_PARENT_METHOD = "parent_method";

    private WeakReference<View> viewWeakReference;

    private String fetchOperation;
    private String logLevel;
    private String parentMethod;

    /**
     * Constructor
     *
     * @param view                              The view that this Async Task is being set up within (used to obtain the context)
     * @param fetchOperation                    The type of fetch operation this async task will perform when executed
     * @param logLevel                          The log level of the entries to fetch (only used when fetchOperation is FETCH_LOG_LEVEL)
     * @param parentMethod                      The name of the parent method of the entries to fetch (only used when fetchOperation is FETCH_PARENT_METHOD)
     */
    public DatabaseFetchAsyncTask(View view, String fetchOperation, @Nullable String logLevel, @Nullable String parentMethod ) {

        viewWeakReference = new WeakReference<>( view );
        this.fetchOperation = fetchOperation;

        if ( logLevel != null ) {

            this.logLevel = logLevel;

        }

        if ( parentMethod != null ) {

            this.parentMethod = parentMethod;

        }

    }

    /**
     * Determines which fetch method to use before ultimately returning the results to the caller
     * asynchronously
     *
     * @param voids                     Not used
     * @return                          List of results for the query this task was configured to use
     */
    @Override
    protected List<LoggerBotEntry> doInBackground(Void... voids) {

        List<LoggerBotEntry> results = new ArrayList<>();

        switch ( fetchOperation ) {

            case FETCH_ALL:
                results = LoggerBotEntryRepo.getAllLogEntries( viewWeakReference.get().getContext() );
                break;
            case FETCH_LOG_LEVEL:
                results = LoggerBotEntryRepo.getAllLogEntriesForLogLevel( viewWeakReference.get().getContext(), logLevel );
                break;
            case FETCH_PARENT_METHOD:
                results = LoggerBotEntryRepo.getAllLogEntriesForParentMethod( viewWeakReference.get().getContext(), parentMethod );
                break;

        }

        return results;
    }

}
