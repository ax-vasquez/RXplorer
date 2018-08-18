package com.scriptient.rxplorer.persistence.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Relation;
import android.content.Intent;

import java.util.List;
import java.util.Map;

/**
 * Model class representing an arbitrary in-app log object, used in the embedded diagnostic
 * logger RecyclerView
 *
 * @author Armando Vasquez
 */
@Entity(tableName = "logger_bot_entries")
public class LoggerBotEntry {

    /**
     * The ID of this log entry
     */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "log_id")
    private Long logId;

    /**
     * The ID of the session this log entry belongs to
     * <p>
     *     TODO: Create the Log Session Entity
     */
    @ColumnInfo(name = "session_id")
    private Long sessionId;

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
     * The content of this log message (e.g. the <q>main part</q>)
     */
    @ColumnInfo(name = "log_content")
    private String content;

    public Long getLogId() {
        return logId;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getEvent() {
        return event;
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

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setEvent(String event) {
        this.event = event;
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
