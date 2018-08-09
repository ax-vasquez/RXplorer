package com.scriptient.rxplorer;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.scriptient.rxplorer.persistence.model.log.AppEmbeddedLogEntry;
import com.trello.rxlifecycle2.components.RxActivity;

public class MainActivity extends RxActivity {

    private static final String TAG = "MainActivity";

    // Temporary - for testing
    Button mNormalEventButton;
    Button mInfoEventButton;
    Button mWarnEventButton;
    Button mErrorEventButton;

    LoggerViewExpandedFragment loggerViewExpandedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNormalEventButton = findViewById( R.id.sim_normal_event_btn );
        mInfoEventButton = findViewById( R.id.sim_info_event_btn );
        mWarnEventButton = findViewById( R.id.sim_warn_event_btn );
        mErrorEventButton = findViewById( R.id.sim_error_event_btn );

        loggerViewExpandedFragment = new LoggerViewExpandedFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add( R.id.logger_view, loggerViewExpandedFragment).commit();

        _initDummyButtons();

    }

    /**
     * Simple helper method to
     */
    private void _initDummyButtons() {

        mNormalEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i(TAG, "onClick: Dummy Normal Log Event");

                AppEmbeddedLogEntry logEntry = new AppEmbeddedLogEntry();

                Long timestampLong = System.currentTimeMillis();
                String timestamp = timestampLong.toString();

                logEntry.setTimestamp( timestamp );
                logEntry.setLogLevel( LoggerViewExpandedFragment.LOG_LEVEL_VERBOSE );
                logEntry.setParentMethod( TAG );
                logEntry.setSaved( false );

                loggerViewExpandedFragment.addNewLogEntry( v, logEntry );

            }
        });

        mInfoEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i(TAG, "onClick: Dummy Info Log Event");

                AppEmbeddedLogEntry logEntry = new AppEmbeddedLogEntry();

                Long timestampLong = System.currentTimeMillis();
                String timestamp = timestampLong.toString();

                logEntry.setTimestamp( timestamp );
                logEntry.setLogLevel( LoggerViewExpandedFragment.LOG_LEVEL_INFO );
                logEntry.setParentMethod( TAG );
                logEntry.setSaved( false );

                loggerViewExpandedFragment.addNewLogEntry( v, logEntry );

            }
        });

        mWarnEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i(TAG, "onClick: Dummy Warn Log Event");

                AppEmbeddedLogEntry logEntry = new AppEmbeddedLogEntry();

                Long timestampLong = System.currentTimeMillis();
                String timestamp = timestampLong.toString();

                logEntry.setTimestamp( timestamp );
                logEntry.setLogLevel( LoggerViewExpandedFragment.LOG_LEVEL_WARN );
                logEntry.setParentMethod( TAG );
                logEntry.setSaved( false );

                loggerViewExpandedFragment.addNewLogEntry( v, logEntry );

            }
        });

        mErrorEventButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Log.i(TAG, "onClick: Dummy Error Log Event");

                AppEmbeddedLogEntry logEntry = new AppEmbeddedLogEntry();

                Long timestampLong = System.currentTimeMillis();
                String timestamp = timestampLong.toString();

                logEntry.setTimestamp( timestamp );
                logEntry.setLogLevel( LoggerViewExpandedFragment.LOG_LEVEL_ERROR );
                logEntry.setParentMethod( TAG );
                logEntry.setSaved( false );

                loggerViewExpandedFragment.addNewLogEntry( v, logEntry );

            }
        });

    }

}
