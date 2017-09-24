package com.juan.sensors.model;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Created by tinashe on 2017/09/19.
 */

public class SensorsTypeConverters {

    @TypeConverter
    public static long dateToLong(Date date) {
        return date.getTime();
    }

    @TypeConverter
    public static Date longToDate(long time) {
        return new Date(time);
    }
}
