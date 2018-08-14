package com.scriptient.rxplorer.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.scriptient.rxplorer.Injection;
import com.scriptient.rxplorer.R;
import com.scriptient.rxplorer.persistence.model.AppEmbeddedLogEntry;
import com.trello.rxlifecycle2.components.RxActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LogEntryActivity extends RxActivity {

    private TextView mTextViewTimestamp;
    private TextView mTextViewParentMethod;
    private TextView mTextViewContent;
    private Switch mSwitchSavedEntry;

    private LogEntryDetailViewModel viewModel;
    private ViewModelFactory viewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_entry);

        View view = new View( this );

        viewModelFactory = Injection.provideLoggerViewModelFactory( view );
        viewModel = viewModelFactory.create( LogEntryDetailViewModel.class );

        mTextViewTimestamp = findViewById( R.id.log_entry_timestamp );
        mTextViewParentMethod = findViewById( R.id.log_entry_parent_method );
        mTextViewContent = findViewById( R.id.log_entry_content );
        mSwitchSavedEntry = findViewById( R.id.checkbox_log_entry_is_saved );

    }

    @SuppressLint("CheckResult")
    @Override
    protected void onResume() {
        super.onResume();

        viewModel
                .getLogEntryById( getIntent().getIntExtra( LoggerViewFragment.ENTRY_ID_KEY, 0 ) )
                .subscribeOn( Schedulers.io() )
                .observeOn( AndroidSchedulers.mainThread() )
                .subscribe(appEmbeddedLogEntry -> {

                    mTextViewTimestamp.setText( appEmbeddedLogEntry.getTimestamp() );
                    mTextViewParentMethod.setText( appEmbeddedLogEntry.getParentMethod() );
                    mTextViewContent.setText( appEmbeddedLogEntry.getContent() );
                    mSwitchSavedEntry.setChecked( appEmbeddedLogEntry.getSaved() );

                });

    }
}
