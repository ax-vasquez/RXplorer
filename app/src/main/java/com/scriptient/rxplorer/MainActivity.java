package com.scriptient.rxplorer;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.scriptient.rxplorer.ui.view.LoggerViewFragment;

public class MainActivity extends AppCompatActivity {

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


    public void _initDummyButtons() {

        mNormalEventButton.setOnClickListener(new DummyButtonClickListener( "Normal Button Click", LoggerBot.LOG_LEVEL_VERBOSE ));
        mInfoEventButton.setOnClickListener(new DummyButtonClickListener( "Info Button Click", LoggerBot.LOG_LEVEL_INFO ));
        mWarnEventButton.setOnClickListener(new DummyButtonClickListener( "Warn Button Click", LoggerBot.LOG_LEVEL_WARN ));
        mErrorEventButton.setOnClickListener(new DummyButtonClickListener( "Error Button Click", LoggerBot.LOG_LEVEL_ERROR ));

    }

}
