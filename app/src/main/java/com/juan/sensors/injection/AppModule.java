package com.juan.sensors.injection;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;

import com.juan.sensors.database.SensorsDao;
import com.juan.sensors.database.SensorsDb;

import dagger.Module;
import dagger.Provides;

import static com.juan.sensors.database.SensorsDb.DB_NAME;

/**
 * Created by tinashe on 2017/08/31.
 */
@Module
public class AppModule {

    @Provides
    Context provideContext(Application application) {
        return application;
    }

    @Provides
    SensorsDb provideDatabase(Context context) {
        return Room.databaseBuilder(context, SensorsDb.class, DB_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }

    @Provides
    SensorsDao provideSensorsDao(SensorsDb database) {
        return database.sensorsDao();
    }
}
