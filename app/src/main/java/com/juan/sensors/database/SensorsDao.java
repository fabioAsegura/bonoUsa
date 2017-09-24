package com.juan.sensors.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.juan.sensors.model.SensorData;

import java.util.List;

import io.reactivex.Flowable;

import static com.juan.sensors.model.SensorData.TABLE_NAME;

/**
 * Created by tinashe on 2017/08/31.
 */
@Dao
public interface SensorsDao {

    @Query("SELECT * FROM " + TABLE_NAME)
    Flowable<List<SensorData>> listAll();

    @Insert
    void addRecording(SensorData... sensors);

    @Query("DELETE FROM " + TABLE_NAME)
    void wipeTable();
}
