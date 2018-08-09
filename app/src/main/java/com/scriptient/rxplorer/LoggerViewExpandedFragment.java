package com.scriptient.rxplorer;


import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.scriptient.rxplorer.async.DatabaseFetchAsyncTask;
import com.scriptient.rxplorer.async.DatabaseResetAsyncTask;
import com.scriptient.rxplorer.async.ModifyDatabaseAsyncTask;
import com.scriptient.rxplorer.persistence.model.log.AppEmbeddedLogEntry;
import com.trello.rxlifecycle2.components.RxFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;


public class LoggerViewExpandedFragment extends RxFragment {

    private static final String TAG = "LoggerViewExpanded";

    public static final float X_VERTICAL_SWIPE_TOLERANCE = 30;
    public static final float Y_VERTICAL_SWIPE_COLLAPSE_REQUIREMENT = 100;

    public static final String LOG_LEVEL_VERBOSE = "verbose";
    public static final String LOG_LEVEL_INFO = "info";
    public static final String LOG_LEVEL_WARN = "warn";
    public static final String LOG_LEVEL_ERROR = "error";

    /**
     * The currently-selected log state
     */
    private String currentLogState;

    /**
     * The previously-selected log state (used when switching selections)
     */
    private String prevLogState;

    private RecyclerView mRecyclerView;
    private TextView mVerboseTextView;
    private TextView mInfoTextView;
    private TextView mWarnTextView;
    private TextView mErrorTextView;
    private Button mResetButton;

    LoggerViewCollapsedFragment collapsedFragment;

    private LoggerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseFetchAsyncTask fetchAsyncTask;
    private ModifyDatabaseAsyncTask modifyAsyncTask;
    private DatabaseResetAsyncTask databaseResetAsyncTask;
    private RecyclerView.AdapterDataObserver dataObserver;

    public LoggerViewExpandedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_logger_view_expanded, container, false);

        mRecyclerView = view.findViewById( R.id.logger_view_log_output );
        mVerboseTextView = view.findViewById( R.id.verbose_label );
        mInfoTextView = view.findViewById( R.id.info_label );
        mWarnTextView = view.findViewById( R.id.warn_label );
        mErrorTextView = view.findViewById( R.id.error_label );
        mResetButton = view.findViewById( R.id.collapse_button );

        dataObserver = new RecyclerView.AdapterDataObserver() {};

        mAdapter = new LoggerViewAdapter( _populateRecyclerViewData() );
        mAdapter.registerAdapterDataObserver( dataObserver );

        mRecyclerView.setAdapter( mAdapter );
        mLayoutManager = new LinearLayoutManager( view.getContext() );
        mRecyclerView.setLayoutManager( mLayoutManager );

        // Always create the Logger RecyclerView set to Verbose
        currentLogState = LOG_LEVEL_VERBOSE;
        prevLogState = LOG_LEVEL_VERBOSE;

        mVerboseTextView.setTextColor( getResources().getColor( R.color.colorAccent ) );

        mVerboseTextView.setOnClickListener( new VerboseLevelClickListener() );
        mInfoTextView.setOnClickListener( new InfoLevelClickListener() );
        mWarnTextView.setOnClickListener( new WarnLevelClickListener() );
        mErrorTextView.setOnClickListener( new ErrorLevelClickListener() );
        mResetButton.setOnClickListener( new ResetDataClickListener() );

        view.setOnTouchListener( new View.OnTouchListener() {

            private float startX;
            private float startY;
            private float endX;
            private float endY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch ( event.getAction() ) {

                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                    case MotionEvent.ACTION_UP:
                        endX = event.getX();
                        endY = event.getY();
                        if ( _checkSwipeXTolerance( startX, endX ) && _checkSwipeYTolerance( startY, endY ) ) {

                            collapsedFragment = new LoggerViewCollapsedFragment();
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.add( R.id.logger_view_collapsed, collapsedFragment ).commit();
                            return true;
                            
                        }

                }

                return false;
            }

        });

        return view;
    }

    /**
     * Helper method to check if the swipe gesture is within the tolerance (too far left or right
     * of the start X should be ignored)
     *
     * @param startX
     * @param endX
     * @return
     */
    private boolean _checkSwipeXTolerance( float startX, float endX ) {

        float maxToleranceLeft = startX - X_VERTICAL_SWIPE_TOLERANCE;
        float maxToleranceRight = startX + X_VERTICAL_SWIPE_TOLERANCE;

        return endX >= maxToleranceLeft && endX <= maxToleranceRight;

    }

    /**
     * Helper method to check if the swipe gesture has moved down far enough to swipe the logger view
     * down
     *
     * @param startY
     * @param endY
     * @return
     */
    private boolean _checkSwipeYTolerance( float startY, float endY ) {

        float minYValue = startY - Y_VERTICAL_SWIPE_COLLAPSE_REQUIREMENT;

        return endY <= minYValue;

    }

    /**
     * Helper method to set the initial data set for the RecyclerView
     *
     * @return                      List of all log entries currently in the table
     */
    private List<AppEmbeddedLogEntry> _populateRecyclerViewData() {

        fetchAsyncTask = new DatabaseFetchAsyncTask( mRecyclerView, DatabaseFetchAsyncTask.FETCH_ALL, null, null );

        fetchAsyncTask.execute();
        List<AppEmbeddedLogEntry> dataSet = new ArrayList<>();

        try {
            dataSet = fetchAsyncTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return dataSet;

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
    private void _updateRecyclerViewDataSet() {

        fetchAsyncTask.execute();

        // Obtain the data set
        List<AppEmbeddedLogEntry> newDataSet = new ArrayList<>();
        try {
            newDataSet = fetchAsyncTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
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
        mAdapter.setCurrentLogData( new ArrayList<AppEmbeddedLogEntry>() );
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
     * Helper method used to populate the recycler view with a new set of data depending on the currently
     * selected log level
     * <p>
     *     This method is what makes the view <q>reactive</q> when swapping out the entire backing
     *     data set (within the RecyclerView.Adapter object)
     */
    private void _updateRecyclerViewDataSetForSingleEntry() {

        fetchAsyncTask.execute();

        // Obtain the data set
        List<AppEmbeddedLogEntry> newDataSet = new ArrayList<>();
        try {
            newDataSet = fetchAsyncTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        mAdapter.setCurrentLogData( newDataSet );
        mAdapter.notifyItemInserted( newDataSet.size() );

    }

    /**
     * Helper method to remove all <b>unsaved log entries</b> from the app database
     */
    private void _clearUnsavedLogEntries() {

        Log.i(TAG, "_clearUnsavedLogEntries: clearing unsaved log entries");

        databaseResetAsyncTask = new DatabaseResetAsyncTask( this.mRecyclerView );
        databaseResetAsyncTask.execute();

        _clearCachedContentsFromRecyclerView();
        Log.i(TAG, "_clearUnsavedLogEntries: All old items flushed from adapter");

        switch ( currentLogState ) {

            case LOG_LEVEL_VERBOSE:
                fetchAsyncTask = new DatabaseFetchAsyncTask( getView(), DatabaseFetchAsyncTask.FETCH_ALL, null, null );
                break;
            case LOG_LEVEL_INFO:
                fetchAsyncTask = new DatabaseFetchAsyncTask( getView(), DatabaseFetchAsyncTask.FETCH_LOG_LEVEL, LOG_LEVEL_INFO, null );
                break;
            case LOG_LEVEL_WARN:
                fetchAsyncTask = new DatabaseFetchAsyncTask( getView(), DatabaseFetchAsyncTask.FETCH_LOG_LEVEL, LOG_LEVEL_WARN, null );
                break;
            case LOG_LEVEL_ERROR:
                fetchAsyncTask = new DatabaseFetchAsyncTask( getView(), DatabaseFetchAsyncTask.FETCH_LOG_LEVEL, LOG_LEVEL_ERROR, null );
                break;

        }

        fetchAsyncTask.execute();

        List<AppEmbeddedLogEntry> remainingLogEntries = new ArrayList<>();
        try {
            Log.i(TAG, "_clearUnsavedLogEntries: Fetching remaining log entries");
            remainingLogEntries = fetchAsyncTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Log.i(TAG, "_clearUnsavedLogEntries: Setting remaining log entries as new data set");
        _updateRecyclerViewDataSet( remainingLogEntries );

    }

    /**
     * Adds a new log entry to the RecyclerView.Adapter
     */
    public void addNewLogEntry( View v,  AppEmbeddedLogEntry logEntry ) {

        // Set up and execute the insert async task
        modifyAsyncTask = new ModifyDatabaseAsyncTask( v, ModifyDatabaseAsyncTask.MODIFY_INSERT, logEntry );
        modifyAsyncTask.execute();

        // Configure the Fetch Async Task, depending on the selected log filter
        switch ( currentLogState ) {

            case LOG_LEVEL_VERBOSE:
                fetchAsyncTask = new DatabaseFetchAsyncTask( v, DatabaseFetchAsyncTask.FETCH_ALL, null, null );
                break;
            case LOG_LEVEL_INFO:
                fetchAsyncTask = new DatabaseFetchAsyncTask( v, DatabaseFetchAsyncTask.FETCH_LOG_LEVEL, LOG_LEVEL_INFO, null );
                break;
            case LOG_LEVEL_WARN:
                fetchAsyncTask = new DatabaseFetchAsyncTask( v, DatabaseFetchAsyncTask.FETCH_LOG_LEVEL, LOG_LEVEL_WARN, null );
                break;
            case LOG_LEVEL_ERROR:
                fetchAsyncTask = new DatabaseFetchAsyncTask( v, DatabaseFetchAsyncTask.FETCH_LOG_LEVEL, LOG_LEVEL_ERROR, null );
                break;

        }

        _updateRecyclerViewDataSetForSingleEntry(  );

    }

    /**
     * Helper method to toggle the currently-selected log level to the <q>selected</q> state by
     * setting its color to {@color colorAccent}
     */
    public void toggleLogLevelSelectedState() {

        // Configure the Fetch Async Task, depending on the selected log filter
        switch ( currentLogState ) {

            case LOG_LEVEL_VERBOSE:
                mVerboseTextView.setTextColor( getResources().getColor( R.color.colorAccent ) );
                _deselectPreviousLogState();
                break;
            case LOG_LEVEL_INFO:
                mInfoTextView.setTextColor( getResources().getColor( R.color.colorAccent ) );
                _deselectPreviousLogState();
                break;
            case LOG_LEVEL_WARN:
                mWarnTextView.setTextColor( getResources().getColor( R.color.colorAccent ) );
                _deselectPreviousLogState();
                break;
            case LOG_LEVEL_ERROR:
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

        // Configure the Fetch Async Task, depending on the selected log filter
        switch ( prevLogState ) {

            case LOG_LEVEL_VERBOSE:
                mVerboseTextView.setTextColor( getResources().getColor( R.color.standardLogText ) );
                break;
            case LOG_LEVEL_INFO:
                mInfoTextView.setTextColor( getResources().getColor( R.color.standardLogText ) );
                break;
            case LOG_LEVEL_WARN:
                mWarnTextView.setTextColor( getResources().getColor( R.color.standardLogText ) );
                break;
            case LOG_LEVEL_ERROR:
                mErrorTextView.setTextColor( getResources().getColor( R.color.standardLogText ) );
                break;

        }

        // Sets the prev log state to the current one once the old previous state has been deselected
        prevLogState = currentLogState;

    }



    /**
     * Click Listener for the <q>Verbose</q> log filter text view
     */
    public class VerboseLevelClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            mVerboseTextView.setTextColor( getResources().getColor( R.color.colorAccent ) );
            currentLogState = LOG_LEVEL_VERBOSE;

            Log.i(TAG, "onClick: VERBOSE");

            fetchAsyncTask = new DatabaseFetchAsyncTask(
                    v,
                    DatabaseFetchAsyncTask.FETCH_ALL,
                    null,
                    null
            );

            _updateRecyclerViewDataSet();
            toggleLogLevelSelectedState();

        }

    }

    /**
     * Click Listener for the <q>Info</q> log filter text view
     */
    public class InfoLevelClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            currentLogState = LOG_LEVEL_INFO;

            Log.i(TAG, "onClick: INFO");

            fetchAsyncTask = new DatabaseFetchAsyncTask(
                    v,
                    DatabaseFetchAsyncTask.FETCH_LOG_LEVEL,
                    LOG_LEVEL_INFO,
                    null
            );

            _updateRecyclerViewDataSet();
            toggleLogLevelSelectedState();

        }

    }

    /**
     * Click Listener for the <q>Warn</q> log filter text view
     */
    public class WarnLevelClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            currentLogState = LOG_LEVEL_WARN;

            Log.i(TAG, "onClick: WARN");

            fetchAsyncTask = new DatabaseFetchAsyncTask(
                    v,
                    DatabaseFetchAsyncTask.FETCH_LOG_LEVEL,
                    LOG_LEVEL_WARN,
                    null
            );

            _updateRecyclerViewDataSet();
            toggleLogLevelSelectedState();

        }

    }

    /**
     * Click Listener for the <q>Error</q> log filter text view
     */
    public class ErrorLevelClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            currentLogState = LOG_LEVEL_ERROR;

            Log.i(TAG, "onClick: ERROR");

            fetchAsyncTask = new DatabaseFetchAsyncTask(
                    v,
                    DatabaseFetchAsyncTask.FETCH_LOG_LEVEL,
                    LOG_LEVEL_ERROR,
                    null
            );

            _updateRecyclerViewDataSet();
            toggleLogLevelSelectedState();

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
}
