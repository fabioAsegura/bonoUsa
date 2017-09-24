package com.juan.sensors.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;

import static com.juan.sensors.model.SensorData.TABLE_NAME;

/**
 * Created by tinashe on 2017/09/19.
 */
@Entity(tableName = TABLE_NAME)
@TypeConverters(SensorsTypeConverters.class)
public class SensorData {

    public static final String TABLE_NAME = "sensors";

    public static final String COLUMN_DATE = "date";

    @PrimaryKey
    @ColumnInfo(name = COLUMN_DATE)
    private Date date;

    private String name;

    private double recording;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRecording() {
        return recording;
    }

    public void setRecording(double recording) {
        this.recording = recording;
    }
}
