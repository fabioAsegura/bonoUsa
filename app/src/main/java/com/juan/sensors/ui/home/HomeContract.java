package com.juan.sensors.ui.home;

import com.juan.sensors.model.SensorData;
import com.juan.sensors.ui.base.BasePresenter;
import com.juan.sensors.ui.base.BaseView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by tinashe on 2017/08/31.
 */

public interface HomeContract {

    interface View extends BaseView<Presenter> {

        void listSensors();

        void plotGraph(HashMap<String, List<SensorData>> hashMap);

        void makeTable(HashMap<String, List<SensorData>> hashMap);

    }

    interface Presenter extends BasePresenter<View> {
        void recordSensorData(String name, double value);
    }
}
