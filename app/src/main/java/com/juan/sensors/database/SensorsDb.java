package com.juan.sensors.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.juan.sensors.model.SensorData;

/**
 * Created by tinashe on 2017/08/31.
 */

@Database(entities = SensorData.class, version = 2)
public abstract class SensorsDb extends RoomDatabase {

    public static final String DB_NAME = "sensors-db";

    public abstract SensorsDao sensorsDao();
}
