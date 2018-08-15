package com.scriptient.rxplorer.persistence.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;
import java.util.Map;

/**
 * Model class representing an arbitrary in-app log object, used in the embedded diagnostic
 * logger RecyclerView
 *
 * @author Armando Vasquez
 */
@Entity(tableName = "app_embedded_log_entries")
public class AppEmbeddedLogEntry {

    /**
     * The ID of this log entry
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "log_id")
    private int logId;

    /**
     * The ID of the session this log entry belongs to
     * <p>
     *     TODO: Create the Log Session Entity
     */
    @ColumnInfo(name = "session_id")
    private int sessionId;

    /**
     * The time this log event occurred at
     */
    @ColumnInfo(name = "timestamp")
    private String timestamp;

    /**
     * The event this log entry represents
     */
    @ColumnInfo(name = "event")
    private String event;

    /**
     * The log level of this log event
     */
    @ColumnInfo(name = "log_level")
    private String logLevel;

    /**
     * The name of the method this log event was logged from (including the parent class, ideally, though not required)
     */
    @ColumnInfo(name = "parent_method")
    private String parentMethod;

    /**
     * Indicates if the user has saved this log entry (saved log entries are protected from being
     * deleted when resetting the database
     */
    @ColumnInfo(name = "saved")
    private Boolean saved;

    /**
     * Map of parameter names as the key, with a corresponding string value representation of the
     * parameter's value (e.g. if a parameter is "int 1", the key is "int" and value is "1"
     */
    @ColumnInfo(name = "parameter_map")
    private Map<String, String> parameterMap;

    /**
     * The content of this log message (e.g. the <q>main part</q>)
     */
    @ColumnInfo(name = "log_content")
    private String content;

    public int getLogId() {
        return logId;
    }

    public int getSessionId() {
        return sessionId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public String getParentMethod() {
        return parentMethod;
    }

    public Boolean getSaved() {
        return saved;
    }

    public String getContent() {
        return content;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public void setParentMethod(String parentMethod) {
        this.parentMethod = parentMethod;
    }

    public void setSaved(Boolean saved) {
        this.saved = saved;
    }

    public void setContent(String content) {
        this.content = content;
    }
}