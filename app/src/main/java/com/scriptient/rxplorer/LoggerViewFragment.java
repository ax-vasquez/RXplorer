package com.scriptient.rxplorer;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.scriptient.rxplorer.persistence.model.log.AppEmbeddedLogEntry;
import com.trello.rxlifecycle2.components.RxFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class LoggerViewFragment extends RxFragment {

    private static final String TAG = "LoggerViewExpanded";

    private LoggerBot loggerBot;

    /**
     * The currently-selected log state
     */
    public static String currentLogState;

    /**
     * The previously-selected log state (used when switching selections)
     */
    private String prevLogState;

    private CompositeDisposable disposable;

    private LoggerViewViewModel viewModel;
    private ViewModelFactory viewModelFactory;

    private RecyclerView mRecyclerView;
    private TextView mVerboseTextView;
    private TextView mInfoTextView;
    private TextView mWarnTextView;
    private TextView mErrorTextView;
    private Button mResetButton;
    private Switch mCollapseSwitch;
    private RelativeLayout mLoggerToolbar;

    private LoggerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.AdapterDataObserver dataObserver;

    public LoggerViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_logger_view, container, false);

        disposable = new CompositeDisposable();

        // Always create the Logger RecyclerView set to Verbose
        currentLogState = LoggerBot.LOG_LEVEL_VERBOSE;
        prevLogState = LoggerBot.LOG_LEVEL_VERBOSE;

        viewModelFactory = Injection.provideLoggerViewModelFactory( view );
        viewModel = viewModelFactory.create( LoggerViewViewModel.class );

        loggerBot = LoggerBot.getInstance();

        mLoggerToolbar = view.findViewById( R.id.logger_view_toolbar );
        mRecyclerView = view.findViewById( R.id.logger_view_log_output );
        mVerboseTextView = view.findViewById( R.id.verbose_label );
        mInfoTextView = view.findViewById( R.id.info_label );
        mWarnTextView = view.findViewById( R.id.warn_label );
        mErrorTextView = view.findViewById( R.id.error_label );
        mResetButton = view.findViewById( R.id.reset_button );
        mCollapseSwitch = view.findViewById( R.id.collapse_toggle );

        mCollapseSwitch.setOnClickListener( v -> {

            if ( !mCollapseSwitch.isChecked() ) {

                Log.i(TAG, "onClick: unchecked");
                mRecyclerView.setVisibility( View.GONE );

            } else {

                Log.i(TAG, "onClick: checked");
                mRecyclerView.setVisibility( View.VISIBLE );

            }

        });

        dataObserver = new RecyclerView.AdapterDataObserver() {};

        try {
            mAdapter = new LoggerViewAdapter( loggerBot.getAllLogEntriesByLogLevel( view, LoggerBot.LOG_LEVEL_VERBOSE ));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        mAdapter.registerAdapterDataObserver( dataObserver );

        mRecyclerView.setAdapter( mAdapter );
        mLayoutManager = new LinearLayoutManager( view.getContext() );
        mRecyclerView.setLayoutManager( mLayoutManager );

        mVerboseTextView.setTextColor( getResources().getColor( R.color.colorAccent ) );

        mVerboseTextView.setOnClickListener( new LogLevelFilterClickListener( LoggerBot.LOG_LEVEL_VERBOSE ) );
        mInfoTextView.setOnClickListener( new LogLevelFilterClickListener( LoggerBot.LOG_LEVEL_INFO ) );
        mWarnTextView.setOnClickListener( new LogLevelFilterClickListener( LoggerBot.LOG_LEVEL_WARN ) );
        mErrorTextView.setOnClickListener( new LogLevelFilterClickListener( LoggerBot.LOG_LEVEL_ERROR ) );

        mResetButton.setOnClickListener( new ResetDataClickListener() );

        return view;
    }

    /**
     * Subscribe in onResume to ensure subscription state isn't invalidated after device orientation
     * changes
     */
    @Override
    public void onResume() {
        super.onResume();

        // Subscribe to observables here
        disposable.add(
                viewModel
                        .getAllLogEntries()
                        .subscribeOn( Schedulers.io() )
                        .observeOn( AndroidSchedulers.mainThread() )
                        .subscribe( new ListConsumer() )
        );

    }

    @Override
    public void onStop() {
        super.onStop();

        // Unsubscribe from observables here
        disposable.clear();

    }

    /**
     * Helper method to execute the fetchAsyncTask used to obtain entire sets of data
     * <p>
     *     There is a temporary <q>empty list</q> that shows when switching between the selected
     *     toggle states - this step is required so that the RecyclerView does not encounter an
     *     inconsistent list size exception.
     *
     * @see #_clearCachedContentsFromRecyclerView()
     */
    private void _updateRecyclerViewDataSet( String logLevel ) {

        // Obtain the data set
        List<AppEmbeddedLogEntry> newDataSet = null;

        try {
            newDataSet = loggerBot.getAllLogEntriesByLogLevel( getView(), logLevel );
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        _clearCachedContentsFromRecyclerView();
        _updateRecyclerViewDataSet( newDataSet );

    }

    /**
     * Helper method to empty the contents currently contained within the RecyclerView (via its
     * adapter)
     * <p>
     *     This method captures an EXTREMELY IMPORTANT STEP - when reusing the same RecyclerView object with
     *     multiple data sources, <b>you must either ensure both the ORIGINAL list and NEW list have
     *     the same number of items or empty the contents from the recycler view's adapter.</b>.
     */
    private void _clearCachedContentsFromRecyclerView() {

        int numberOfItemsPreviouslyInRV = mAdapter.getCurrentLogData().size();
        mAdapter.setCurrentLogData( new ArrayList<>() );
        mAdapter.notifyItemRangeRemoved( LoggerViewAdapter.DATA_SET_START, numberOfItemsPreviouslyInRV );

    }

    /**
     * Helper method used to populate the recycler view with a new set of data depending on the currently
     * selected log level
     * <p>
     *     This method is what makes the view <q>reactive</q> when swapping out the entire backing
     *     data set (within the RecyclerView.Adapter object)
     *
     * @param newDataSet                The data set to populate the RecyclerView.Adapter with
     */
    private void _updateRecyclerViewDataSet( List<AppEmbeddedLogEntry> newDataSet ) {

        mAdapter.setCurrentLogData( newDataSet );
        mAdapter.notifyItemRangeInserted( LoggerViewAdapter.DATA_SET_START, newDataSet.size() );

    }

    /**
     * Helper method to remove all <b>unsaved log entries</b> from the app database
     */
    private void _clearUnsavedLogEntries() {

        Log.i(TAG, "_clearUnsavedLogEntries: clearing unsaved log entries");

        loggerBot.deleteUnsavedLogEntries( getView() );

        _clearCachedContentsFromRecyclerView();
        Log.i(TAG, "_clearUnsavedLogEntries: All old items flushed from adapter");

        List<AppEmbeddedLogEntry> remainingLogEntries = null;
        try {
            remainingLogEntries = loggerBot.getAllLogEntriesByLogLevel( getView(), currentLogState );
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.i(TAG, "_clearUnsavedLogEntries: Setting remaining log entries as new data set");
        _updateRecyclerViewDataSet( remainingLogEntries );

    }

    /**
     * Helper method to toggle the currently-selected log level to the <q>selected</q> state by
     * setting its color to {@color colorAccent}
     */
    private void _toggleLogLevelSelectedState() {

        switch ( currentLogState ) {

            case LoggerBot.LOG_LEVEL_VERBOSE:
                mVerboseTextView.setTextColor( getResources().getColor( R.color.colorAccent ) );
                _deselectPreviousLogState();
                break;
            case LoggerBot.LOG_LEVEL_INFO:
                mInfoTextView.setTextColor( getResources().getColor( R.color.colorAccent ) );
                _deselectPreviousLogState();
                break;
            case LoggerBot.LOG_LEVEL_WARN:
                mWarnTextView.setTextColor( getResources().getColor( R.color.colorAccent ) );
                _deselectPreviousLogState();
                break;
            case LoggerBot.LOG_LEVEL_ERROR:
                mErrorTextView.setTextColor( getResources().getColor( R.color.colorAccent ) );
                _deselectPreviousLogState();
                break;

        }

    }

    /**
     * Helper method used in changing the log level <q>selected state</q> - this method is used
     * to change the color of the previously-selected state to the standard log text color
     */
    private void _deselectPreviousLogState() {

        switch ( prevLogState ) {

            case LoggerBot.LOG_LEVEL_VERBOSE:
                mVerboseTextView.setTextColor( getResources().getColor( R.color.standardLogText ) );
                break;
            case LoggerBot.LOG_LEVEL_INFO:
                mInfoTextView.setTextColor( getResources().getColor( R.color.standardLogText ) );
                break;
            case LoggerBot.LOG_LEVEL_WARN:
                mWarnTextView.setTextColor( getResources().getColor( R.color.standardLogText ) );
                break;
            case LoggerBot.LOG_LEVEL_ERROR:
                mErrorTextView.setTextColor( getResources().getColor( R.color.standardLogText ) );
                break;

        }

        // Sets the prev log state to the current one once the old previous state has been deselected
        prevLogState = currentLogState;

    }

    /**
     * Click listener used for all log level filters
     */
    public class LogLevelFilterClickListener implements View.OnClickListener {

        private String logLevel;

        LogLevelFilterClickListener( String logLevel ) {

            this.logLevel = logLevel;

        }


        @Override
        public void onClick(View v) {

            currentLogState = logLevel;
            _updateRecyclerViewDataSet( logLevel );
            _toggleLogLevelSelectedState();

        }

    }

    /**
     * Click Listener for the Reset button
     */
    public class ResetDataClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v ) {

            new AlertDialog.Builder( v.getContext() )
                    .setTitle( "Clear Unsaved Log Entries" )
                    .setMessage( "Do you want to clear all unsaved log entries from the app database?" )
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            _clearUnsavedLogEntries();
                            Toast.makeText( Objects.requireNonNull(getView()).getContext(), "Cleared Unsaved Log Entries", Toast.LENGTH_SHORT ).show();
                        }
                    })
                    .setNegativeButton("NO", null).show();

        }

    }

    public class ListConsumer implements Consumer<List<AppEmbeddedLogEntry>> {

        @Override
        public void accept(List<AppEmbeddedLogEntry> logEntries) throws Exception {

            Log.i(TAG, "accept: List Updated with size: " + logEntries.size() );

            List<AppEmbeddedLogEntry> currentList = new ArrayList<>();

            for ( AppEmbeddedLogEntry logEntry : logEntries ) {

                if ( currentLogState.equals( LoggerBot.LOG_LEVEL_VERBOSE ) ) {

                    currentList = logEntries;

                } else {

                    if ( logEntry.getLogLevel().equals( currentLogState ) ) {

                        currentList.add( logEntry );

                    }

                }

            }

            mAdapter.setCurrentLogData( currentList );
            mAdapter.notifyItemInserted( mAdapter.getItemCount() );

        }

    }


}
