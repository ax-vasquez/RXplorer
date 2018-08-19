package com.scriptient.rxplorer;

import android.view.View;

import com.scriptient.rxplorer.ui.ViewModelFactory;

public class Injection {

    public static ViewModelFactory provideLoggerViewModelFactory(View view ) {

        return new ViewModelFactory( view );

    }

}
