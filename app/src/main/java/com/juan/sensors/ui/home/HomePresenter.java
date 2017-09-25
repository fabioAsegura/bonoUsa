package com.juan.sensors.ui.home;

import android.util.Log;

import com.juan.sensors.database.SensorsDao;
import com.juan.sensors.model.SensorData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by tinashe on 2017/08/31.
 */

class HomePresenter implements HomeContract.Presenter {

    private static final String TAG = HomePresenter.class.getName();

    private final SensorsDao sensorsDao;
    private HomeContract.View view;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private long lastLog;

    @Inject
    HomePresenter(SensorsDao sensorsDao) {

        this.sensorsDao = sensorsDao;


        Disposable disposable = Completable.fromAction(() -> sensorsDao.wipeTable())

                .subscribeOn(Schedulers.io())

                .observeOn(AndroidSchedulers.mainThread())

                .subscribe(() -> {


                }, throwable -> {

                    Log.e(TAG, throwable.getMessage(), throwable);

                });


        compositeDisposable.add(disposable);
    }


    @Override
    public void takeView(HomeContract.View view) {
        this.view = view;
        readSensorsData();
    }

    @Override
    public void dropView() {
        this.view = null;

        //Clear all subscriptions
        if (compositeDisposable.size() > 0) {
            compositeDisposable.clear();
        }
    }

    private void readSensorsData() {
        Disposable disposable = sensorsDao.listAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sensors -> {
                    Log.d(TAG, "SENSORS: " + sensors);

                    //Show view list of sensors
                    view.listSensors();

                    //TODO: Uncomment to test
                    view.makeTable(getHashmapData(sensors));
                    view.plotGraph(getHashmapData(sensors));

                }, throwable -> {
                    Log.e(TAG, throwable.getMessage(), throwable);
                });

        compositeDisposable.add(disposable);
    }

    private void readSensorsDataGraph2() {
        Disposable disposable = sensorsDao.listAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(sensors -> {
                    Log.d(TAG, "SENSORS: " + sensors);

                    //Show view list of sensors
                    view.listSensors();

                    //TODO: Uncomment to test
                    view.makeTable(getTestMap());
                    view.plotGraph(getHashmapData(sensors));

                }, throwable -> {
                    Log.e(TAG, throwable.getMessage(), throwable);
                });

        compositeDisposable.add(disposable);
    }

    /**
     * Convert SensorData list from database into Hashmap
     *
     * @param sensorData
     * @return
     */
    private HashMap<String, List<SensorData>> getHashmapData(List<SensorData> sensorData) {
        HashMap<String, List<SensorData>> hashMap = new HashMap<>();

        List<String> keys = new ArrayList<>();

        for (SensorData sensor : sensorData) {

            if (keys.contains(sensor.getName())) {
                continue;
            }

            keys.add(sensor.getName());
        }

        for (String key : keys) {
            List<SensorData> list = new ArrayList<>();

            for (SensorData data : sensorData) {
                if (key.equals(data.getName())) {
                    list.add(data);
                }
            }

            hashMap.put(key, list);
        }

        return hashMap;
    }

    /**
     * Use this method to test plotting graph data
     *
     * @return
     */
    private HashMap<String, List<SensorData>> getTestMap() {
        HashMap<String, List<SensorData>> hashMap = new HashMap<>();

        String[] names = {"Light", "Gyroscope A456 Sensor", "Battery"};

        Calendar calendar;
        Random random = new Random();

        for (String name : names) {
            List<SensorData> list = new ArrayList<>();
            calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 5);

            SensorData sensor = new SensorData();
            sensor.setDate(calendar.getTime());
            sensor.setName(name);
            sensor.setRecording(random.nextDouble());
            list.add(sensor);

            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
            sensor = new SensorData();
            sensor.setDate(calendar.getTime());
            sensor.setName(name);
            sensor.setRecording(random.nextDouble());
            list.add(sensor);

            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
            sensor = new SensorData();
            sensor.setDate(calendar.getTime());
            sensor.setName(name);
            sensor.setRecording(random.nextDouble());
            list.add(sensor);

            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
            sensor = new SensorData();
            sensor.setDate(calendar.getTime());
            sensor.setName(name);
            sensor.setRecording(random.nextDouble());
            list.add(sensor);

            hashMap.put(name, list);

        }


        return hashMap;
    }

    @Override
    public void recordSensorData(String name, double value) {
        long now = Calendar.getInstance().getTimeInMillis();

        if (lastLog > 0) {
            long seconds = (now - lastLog) / 1000;

            //Only log changes after 3 sec
            if (seconds < 1) {
                return;
            }
        }

        lastLog = now;

        SensorData sensor = new SensorData();
        sensor.setName(name);
        sensor.setRecording(value);
        sensor.setDate(Calendar.getInstance().getTime());

        Disposable disposable = Completable.fromAction(() -> sensorsDao.addRecording(sensor))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {

                }, throwable -> {
                    Log.e(TAG, throwable.getMessage(), throwable);
                });

        compositeDisposable.add(disposable);
    }
}
