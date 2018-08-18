package com.scriptient.rxplorer.ui.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.scriptient.rxplorer.Injection;
import com.scriptient.rxplorer.LoggerBot;
import com.scriptient.rxplorer.R;
import com.scriptient.rxplorer.ui.adapter.LogEntryDetailParameterAdapter;
import com.scriptient.rxplorer.ui.viewmodel.LogEntryDetailViewModel;
import com.scriptient.rxplorer.ui.ViewModelFactory;
import com.trello.rxlifecycle2.components.RxActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class LogEntryActivity extends RxActivity {

    private static final String TAG = "LogEntryActivity";

    private LoggerBot loggerBot;

    private TextView mTextViewTimestamp;
    private TextView mTextViewEvent;
    private Switch mSwitchSavedEntry;
    private TextView mTextViewParentMethod;

    private LogEntryDetailViewModel viewModel;
    private ViewModelFactory viewModelFactory;

    private LogEntryDetailParameterAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    private CompositeDisposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_entry);

        loggerBot = LoggerBot.getInstance();

        disposable = new CompositeDisposable();

        View view = new View( this );

        viewModelFactory = Injection.provideLoggerViewModelFactory( view );
        viewModel = viewModelFactory.create( LogEntryDetailViewModel.class );
        mLayoutManager = new LinearLayoutManager( this );

        mTextViewTimestamp = findViewById( R.id.log_entry_timestamp );
        mTextViewEvent = findViewById( R.id.log_entry_event );
        mSwitchSavedEntry = findViewById( R.id.checkbox_log_entry_is_saved );
        mTextViewParentMethod = findViewById( R.id.log_entry_parent_method );

        mRecyclerView = findViewById( R.id.parameters_list );
        mRecyclerView.setLayoutManager( mLayoutManager );

    }

    @SuppressLint("CheckResult")
    @Override
    protected void onResume() {
        super.onResume();

        int logEntryId = getIntent().getIntExtra( LoggerViewFragment.ENTRY_ID_KEY, 0 );

        _subscribeLogEntrySingle( logEntryId );
        _subscribeLogEntryParametersListSingle( logEntryId );

    }

    @Override
    protected void onStop() {
        super.onStop();

        disposable.clear();
    }

    /**
     * Helper method to add the LogEntry Single to the CompositeDisposable
     *
     * @param logEntryId
     */
    private void _subscribeLogEntrySingle( Integer logEntryId ) {

        disposable.add(
                viewModel
                        .getLogEntryById( logEntryId )
                        .subscribeOn( Schedulers.io() )
                        .observeOn( AndroidSchedulers.mainThread() )
                        .subscribe(appEmbeddedLogEntry -> {

                            mTextViewTimestamp.setText( appEmbeddedLogEntry.getTimestamp() );
                            mTextViewParentMethod.setText( appEmbeddedLogEntry.getParentMethod() );
                            mTextViewEvent.setText( appEmbeddedLogEntry.getEvent() );
                            mSwitchSavedEntry.setChecked( appEmbeddedLogEntry.getSaved() );

                        })
        );

    }

    /**
     * Helper method to add the LogEntryParameters list Single to the CompositeDisposable
     *
     * @param logEntryId
     */
    private void _subscribeLogEntryParametersListSingle( Integer logEntryId ) {

        disposable.add(
                viewModel
                        .getLogEntryParameters( logEntryId )
                        .subscribeOn( Schedulers.io() )
                        .observeOn( AndroidSchedulers.mainThread() )
                        .subscribe(loggerBotEntryParameters -> {

                            // TODO: figure out why the parameters here are empty
                            Log.i(TAG, "_subscribeLogEntryParametersListSingle: Setting Adapter data with set size: " + loggerBotEntryParameters.size() );
                            mAdapter = new LogEntryDetailParameterAdapter( loggerBotEntryParameters );
                            mRecyclerView.setAdapter( mAdapter );

                        })
        );

    }

}
