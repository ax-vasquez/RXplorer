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
        childColumns = "parent_log_id",
        onDelete = CASCADE
))
public class LoggerBotEntryParameter {

    @PrimaryKey(autoGenerate = true)
    private Integer id;

    @ColumnInfo(name = "parent_log_id")
    private Integer parentLogId;

    @ColumnInfo(name = "data_type")
    private String parameterDataType;

    @ColumnInfo(name = "name")
    private String parameterName;

    @ColumnInfo(name = "value")
    private String parameterValue;

    public Integer getId() {
        return id;
    }

    public Integer getParentLogId() {
        return parentLogId;
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

    public void setId(Integer id) {
        this.id = id;
    }

    public void setParentLogId(Integer logId) {
        this.parentLogId = logId;
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
