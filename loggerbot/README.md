# LoggerBot
LoggerBot is an in-app logging utility intended to simplify the testing and debugging process. _It is not intended as a replacement to `logcat`_. Instead, LoggerBot should be used to log highly-important events in the app's operation, such as crucial network calls or important long-running calculations.

LoggerBot implementations should be set up so that there is little-to-no need for a tester to connect the device to Android Studio to obtain the logs. Exactly how this is implemented is dependent on your needs.
