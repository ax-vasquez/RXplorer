package com.scriptient.rxplorer;

import android.view.View;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class DummyButtonClickListener implements View.OnClickListener {

    private static final String TAG = "ClickListener";

    private Class aClass;
    private String logEventText;
    private String logLevel;

    public DummyButtonClickListener( String logEventText, String logLevel ) {

        this.logEventText = logEventText;
        this.logLevel = logLevel;
        aClass = getClass();

    }

    @Override
    public void onClick( View view ) {

        Method thisMethod = null;

        for (Method method : aClass.getMethods()) {

            if ( method.getName().equals( "onClick" ) ) {

                thisMethod = method;

            }

        }

        List<String> parameterValues = new ArrayList<>();
        parameterValues.add( "view" );

        switch ( logLevel ) {

            case LoggerBot.LOG_LEVEL_VERBOSE:

                LoggerBot.getInstance().logVerboseEvent(
                        view,
                        logEventText,
                        thisMethod,
                        parameterValues
                );

                break;
            case LoggerBot.LOG_LEVEL_INFO:

                LoggerBot.getInstance().logInfoEvent(
                        view,
                        logEventText,
                        thisMethod,
                        parameterValues
                );

                break;
            case LoggerBot.LOG_LEVEL_WARN:

                LoggerBot.getInstance().logWarnEvent(
                        view,
                        logEventText,
                        thisMethod,
                        parameterValues
                );

                break;
            case LoggerBot.LOG_LEVEL_ERROR:

                LoggerBot.getInstance().logErrorEvent(
                        view,
                        logEventText,
                        thisMethod,
                        parameterValues
                );

                break;

        }





    }

}
