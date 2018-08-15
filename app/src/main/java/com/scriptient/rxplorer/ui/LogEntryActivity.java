package com.scriptient.rxplorer.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.scriptient.rxplorer.Injection;
import com.scriptient.rxplorer.LoggerBot;
import com.scriptient.rxplorer.R;
import com.trello.rxlifecycle2.components.RxActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class LogEntryActivity extends RxActivity {

    private TextView mTextViewTimestamp;
    private TextView mTextViewEvent;
    private Switch mSwitchSavedEntry;
    private TextView mTextViewParentMethod;

    private LogEntryDetailViewModel viewModel;
    private ViewModelFactory viewModelFactory;

    private CompositeDisposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_entry);

        disposable = new CompositeDisposable();

        View view = new View( this );

        viewModelFactory = Injection.provideLoggerViewModelFactory( view );
        viewModel = viewModelFactory.create( LogEntryDetailViewModel.class );

        mTextViewTimestamp = findViewById( R.id.log_entry_timestamp );
        mTextViewEvent = findViewById( R.id.log_entry_event );
        mSwitchSavedEntry = findViewById( R.id.checkbox_log_entry_is_saved );
        mTextViewParentMethod = findViewById( R.id.log_entry_parent_method );

    }

    @SuppressLint("CheckResult")
    @Override
    protected void onResume() {
        super.onResume();

        disposable.add(
            viewModel
                .getLogEntryById( getIntent().getIntExtra( LoggerViewFragment.ENTRY_ID_KEY, 0 ) )
                .subscribeOn( Schedulers.io() )
                .observeOn( AndroidSchedulers.mainThread() )
                .subscribe(appEmbeddedLogEntry -> {

                    mTextViewTimestamp.setText( appEmbeddedLogEntry.getTimestamp() );
                    mTextViewParentMethod.setText( appEmbeddedLogEntry.getParentMethod() );
                    mTextViewEvent.setText( LoggerBot.NO_DATA );
                    mSwitchSavedEntry.setChecked( appEmbeddedLogEntry.getSaved() );

                })
        );

    }

    @Override
    protected void onStop() {
        super.onStop();

        disposable.clear();
    }
}
