package com.scriptient.rxplorer.persistence.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Entity class representing parameters saved for a LoggerBotEntry
 */
@Entity(tableName = "entry_parameters",
foreignKeys = @ForeignKey(
        entity = LoggerBotEntry.class,
        parentColumns = "log_id",
        childColumns = "log_id",
        onDelete = CASCADE
))
public class LoggerBotEntryParameter {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "log_id")
    private int logId;

    @ColumnInfo(name = "data_type")
    private String parameterDataType;

    @ColumnInfo(name = "name")
    private String parameterName;

    @ColumnInfo(name = "value")
    private String parameterValue;

    public int getId() {
        return id;
    }

    public int getLogId() {
        return logId;
    }

    public String getParameterDataType() {
        return parameterDataType;
    }

    public String getParameterName() {
        return parameterName;
    }

    public String getParameterValue() {
        return parameterValue;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public void setParameterDataType(String parameterDataType) {
        this.parameterDataType = parameterDataType;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    public void setParameterValue(String parameterValue) {
        this.parameterValue = parameterValue;
    }

}
