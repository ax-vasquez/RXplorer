# LoggerBot
LoggerBot is an in-app logging utility intended to simplify the testing and debugging process. _It is not intended as a replacement to `logcat`_. Instead, LoggerBot should be used to log highly-important events in the app's operation, such as crucial network calls or important long-running calculations.

LoggerBot implementations should be set up so that there is little-to-no need for a tester to connect the device to Android Studio to obtain the logs. Exactly how this is implemented is dependent on your needs.

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
