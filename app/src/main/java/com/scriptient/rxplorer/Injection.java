package com.scriptient.rxplorer;

import android.view.View;

public class Injection {

    public static LoggerViewViewModelFactory provideLoggerViewModelFactory( View view ) {

        return new LoggerViewViewModelFactory( view );

    }

}
