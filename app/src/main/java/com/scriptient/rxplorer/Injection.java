package com.scriptient.rxplorer;

import android.view.View;

public class Injection {

    public static ViewModelFactory provideLoggerViewModelFactory(View view ) {

        return new ViewModelFactory( view );

    }

}
