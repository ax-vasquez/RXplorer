package com.scriptient.rxplorer.ui;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.Button;

import com.scriptient.rxplorer.LoggerBot;
import com.scriptient.rxplorer.R;
import com.scriptient.rxplorer.persistence.model.LoggerBotEntryParameter;
import com.scriptient.rxplorer.ui.LoggerViewFragment;
import com.trello.rxlifecycle2.components.RxActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends RxActivity {

    private static final String TAG = "MainActivity";

    // Temporary - for testing
    Button mNormalEventButton;
    Button mInfoEventButton;
    Button mWarnEventButton;
    Button mErrorEventButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNormalEventButton = findViewById( R.id.sim_normal_event_btn );
        mInfoEventButton = findViewById( R.id.sim_info_event_btn );
        mWarnEventButton = findViewById( R.id.sim_warn_event_btn );
        mErrorEventButton = findViewById( R.id.sim_error_event_btn );

    }

    @Override
    protected void onStart() {
        super.onStart();

        _initDummyButtons();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace( R.id.logger_view_placeholder, new LoggerViewFragment() );
        transaction.commit();
    }

    /**
     * Buttons to asynchronously-insert new log entries
     */
    private void _initDummyButtons() {

        List<LoggerBotEntryParameter> parameters = new ArrayList<>();
        LoggerBotEntryParameter parameter = new LoggerBotEntryParameter();
        parameter.setParameterDataType( "String" );
        parameter.setParameterName( "testName" );
        parameter.setParameterValue( "Test Value" );

        parameters.add( parameter );

        mNormalEventButton.setOnClickListener( v -> LoggerBot.getInstance().createNewLogEntry(
                v,
                "Button Click",
                "mNormalEventButton.onClick()",
                LoggerBot.LOG_LEVEL_VERBOSE,
                "Test Content",
                parameters
        ));

        mInfoEventButton.setOnClickListener(v -> LoggerBot.getInstance().createNewLogEntry(
                v,
                "Button Click",
                "mInfoEventButton.onClick()",
                LoggerBot.LOG_LEVEL_INFO,
                "Test Content",
                parameters

        ));

        mWarnEventButton.setOnClickListener(v -> LoggerBot.getInstance().createNewLogEntry(
                v,
                "Button Click",
                "mWarnEventButton.onClick()",
                LoggerBot.LOG_LEVEL_WARN,
                "Test Content",
                parameters
         ));

        mErrorEventButton.setOnClickListener(v -> LoggerBot.getInstance().createNewLogEntry(
                v,
                "Button Click",
                "mErrorEventButton.onClick()",
                LoggerBot.LOG_LEVEL_ERROR,
                "Test Content",
                parameters
        ));

    }

}
