package com.scriptient.rxplorer.ui.view;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.scriptient.rxplorer.LoggerBot;
import com.scriptient.rxplorer.R;
import com.scriptient.rxplorer.persistence.model.LoggerBotEntryParameter;
import com.scriptient.rxplorer.ui.view.LoggerViewFragment;
import com.trello.rxlifecycle2.components.RxActivity;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends RxActivity {

    private static final String TAG = "MainActivity";

    // Required for use with LoggerBot
    Class aClass;

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

        aClass = getClass();

    }

    @Override
    protected void onStart() {
        super.onStart();

        try {
            _initDummyButtons();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace( R.id.logger_view_placeholder, new LoggerViewFragment() );
        transaction.commit();
    }

    /**
     * Buttons to asynchronously-insert new log entries
     */
    public void _initDummyButtons() throws NoSuchMethodException {

        Method[] classMethods = aClass.getMethods();
        Method parentMethod = null;

        for ( Method method : classMethods ) {

            if ( method.getName().equals( "_initDummyButtons" ) ) {

                parentMethod = method;

            }

        }

        Method finalParentMethod = parentMethod;
        mNormalEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<String> parameterValues = new ArrayList<>();

                parameterValues.add( "View v" );

                LoggerBot.getInstance().logVerboseEvent(
                        v,
                        "Normal Button Click",
                        finalParentMethod,
                        parameterValues
                        );
            }
        });

        mInfoEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<String> parameterValues = new ArrayList<>();

                parameterValues.add( "View v" );

                LoggerBot.getInstance().logInfoEvent(
                        v,
                        "Normal Button Click",
                        finalParentMethod,
                        parameterValues
                );
            }
        });

        mWarnEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<String> parameterValues = new ArrayList<>();

                parameterValues.add( "View v" );

                LoggerBot.getInstance().logWarnEvent(
                        v,
                        "Normal Button Click",
                        finalParentMethod,
                        parameterValues
                );
            }
        });

        mErrorEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<String> parameterValues = new ArrayList<>();

                parameterValues.add( "View v" );

                LoggerBot.getInstance().logErrorEvent(
                        v,
                        "Normal Button Click",
                        finalParentMethod,
                        parameterValues
                );
            }
        });

    }

}
