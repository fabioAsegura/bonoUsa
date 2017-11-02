package com.juan.sensors.injection;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;

import com.juan.sensors.BuildConfig;
import com.juan.sensors.database.SensorsDao;
import com.juan.sensors.database.SensorsDb;
import com.juan.sensors.retrofit.SensorsApi;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

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

    @Provides
    SensorsApi provideSensorsApi() {

        int timeoutSeconds = 60;

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl("http://127.0.0.1") //Todo: Change baseUrl here to your api base url
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()));

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.readTimeout(timeoutSeconds, TimeUnit.SECONDS);
        clientBuilder.connectTimeout(timeoutSeconds, TimeUnit.SECONDS);
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            clientBuilder.addInterceptor(interceptor);
        }


        Retrofit retrofitDefault = retrofitBuilder
                .client(clientBuilder.build())
                .build();

        return retrofitDefault.create(SensorsApi.class);
    }
}
