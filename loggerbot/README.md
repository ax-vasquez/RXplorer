# LoggerBot
LoggerBot is an in-app logging utility intended to simplify the testing and debugging process. _It is not intended as a replacement to `logcat`_. Instead, LoggerBot should be used to log highly-important events in the app's operation, such as crucial network calls or important long-running calculations.

LoggerBot implementations should be set up so that there is little-to-no need for a tester to connect the device to Android Studio to obtain the logs. Exactly how this is implemented is dependent on your needs.

_**IMPORTANT: You should not enable LoggerBot by default - it's only intended to be used for debugging purposes. This is not a blind warning; LoggerBot relies on the Reflection API to obtain method data. The Reflection API adds a substantial amount of performance overhead and should therefore only be enabled in testing.**_

## Log Levels
LoggerBot contains 4 log levels - there are currently no restrictions or recommendations on which log level to use for a given event. The log level only changes the appearance of the entry in the Logger View's RecyclerView. The available log levels to you are:
- `Verbose`
    - Defaults to gray text
- `Info`
    - Defaults to blue text
- `Warn`
    - Defaults to orange text
- `Error`
    - Defaults to red text

## Logging Events
There are four methods used to log events:
1. [`logVerboseEvent()`](https://github.com/ax-vasquez/RXplorer/blob/1cc614187f9199d1532eb35adfca31973c92cf5d/loggerbot/src/main/java/com/scriptient/rxplorer/LoggerBot.java#L57)
2. [`logInfoEvent()`](https://github.com/ax-vasquez/RXplorer/blob/1cc614187f9199d1532eb35adfca31973c92cf5d/loggerbot/src/main/java/com/scriptient/rxplorer/LoggerBot.java#L79)
3. [`logWarnEvent()`](https://github.com/ax-vasquez/RXplorer/blob/1cc614187f9199d1532eb35adfca31973c92cf5d/loggerbot/src/main/java/com/scriptient/rxplorer/LoggerBot.java#L101)
4. [`logErrorEvent()`](https://github.com/ax-vasquez/RXplorer/blob/1cc614187f9199d1532eb35adfca31973c92cf5d/loggerbot/src/main/java/com/scriptient/rxplorer/LoggerBot.java#L123)

All four of these methods take the same 4 parameters:
1. `View view`
    - The view the log event was called from (used for `Context` when inserting into the app database)
2. `String event`
    - The custom event string (arbitrary value set by the developer)
3. `Method method`
    - The parent method the log event method was called from
    - The method can be obtained using the [Reflection API](https://docs.oracle.com/javase/tutorial/reflect/index.html)
      - An example of this is shown below and in the linked class at the end of this section
4. `List<String> parameterValues`
    - The list of parameter values, _in order of the method signature parameter sequence_

### Creating the Parent Class
The first step to using LoggerBot is to establish a reference to the parent class object where LoggerBot is being used. For example, at the top of `DummyButtonClickListener` in RXplorer, the following variable is declared:
```java
private Class aClass;
```
- This object _is not directly-used by LoggerBot_
- It's only used to obtain the `Method` object where a log entry is being created

### Obtaining the Method
To obtain the `Method` in which a LoggerBot entry is created, `DummyButtonClickListener` does the following:
```java
Method thisMethod = null;
for (Method method : aClass.getMethods()) {
    if ( method.getName().equals( "onClick" ) ) {
        thisMethod = method;
    }
}
```
- This iterates all methods defined in `DummyButtonClickListener` until `"onClick"` is found
  - This is how you locate the `onClick` method - to locate any other method, simply provide the method's name
  - _**This is a required manual step in LoggerBot - it is a limitation that I acknowledge and am actively working to change**_

### The Complete Call
As of now, much of LoggerBot is pretty flexible (and manual). Here is the call used in RXplorer to create a new log entry, complete with a list of parameters (with a size of 1):
```java
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
```
- Depending on the button the user clicked, this method will log a `Verbose`, `Info`, `Warn` or `Error` log entry
- Note that I manually-create a `List<String>` containing string values representing the names of the parameters passed-in to the method call
  - In this case, there is only one parameter, `view`
- Once you make any of the `log*Event` methods, they will handle all asynchronous operations and the logger view will update reactively
  - This includes the log entry detail view (which can be seen by clicking on any given log entry method text in the logger view)

To see how RXplorer logs events in the demo, see the [DummyButtonClickListener class](https://github.com/ax-vasquez/RXplorer/blob/master/app/src/main/java/com/scriptient/rxplorer/DummyButtonClickListener.java).

## Adding LoggerView to an Activity
To add the LoggerView to a given activity:
1. Create an empty `RelativeLayout` container in the target activity
2. In the target activity's `onCreate()` method, add the following calls:

```java
FragmentTransaction transaction = getFragmentManager().beginTransaction();
transaction.replace( R.id.logger_view_placeholder, new LoggerViewFragment() );
transaction.commit();
```
- This will replace the empty placeholder container with the LoggerView fragment
- There is _nothing else you need to do_
  - The LoggerView is completely reactive out-of-box
    - All changes to the underlying log entries data table will immediately be reflected in this recyclerview
  - Simply log events anywhere in the app and they will show up in the LoggerView
