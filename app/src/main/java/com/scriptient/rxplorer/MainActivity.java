package com.scriptient.rxplorer;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Button;

import com.trello.rxlifecycle2.components.RxActivity;

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

        _initDummyButtons();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace( R.id.logger_view_placeholder, new LoggerViewFragment() );
        transaction.commit();

    }

    /**
     * Buttons to asynchronously-insert new log entries
     */
    private void _initDummyButtons() {

        mNormalEventButton.setOnClickListener( v -> LoggerBot.getInstance().createNewLogEntry(
                v,
                "mNormalEventButton.onClick()" ,
                LoggerBot.LOG_LEVEL_VERBOSE,
                "Normal Event Button Click"
        ));

        mInfoEventButton.setOnClickListener(v -> LoggerBot.getInstance().createNewLogEntry(
                v,
                "mInfoEventButton.onClick()" ,
                LoggerBot.LOG_LEVEL_INFO,
                "Info Event Button Click"
        ));

        mWarnEventButton.setOnClickListener(v -> LoggerBot.getInstance().createNewLogEntry(
                v,
                "mNormalEventButton.onClick()" ,
                LoggerBot.LOG_LEVEL_WARN,
                "Warn Event Button Click"
         ));

        mErrorEventButton.setOnClickListener(v -> LoggerBot.getInstance().createNewLogEntry(
                v,
                "mNormalEventButton.onClick()" ,
                LoggerBot.LOG_LEVEL_ERROR,
                "Error Event Button Click"
        ));

    }

}
