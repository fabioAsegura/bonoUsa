package com.juan.sensors.ui.home;


import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.juan.sensors.R;
import com.juan.sensors.model.SensorData;
import com.juan.sensors.ui.base.BaseActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import dagger.android.AndroidInjection;

public class HomeActivity extends BaseActivity implements
        NavigationView.OnNavigationItemSelectedListener, HomeContract.View, SensorEventListener {

    private static final String TAG = HomeActivity.class.getName();

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    // @BindView(R.id.graph)
    //GraphView graphView;

    //@BindView(R.id.graph2)
    //GraphView graphView2;

    @Inject
    HomePresenter presenter;

    private SensorManager sensorManager;
    // private Sensor gravity;
    private Sensor light;

    private String[] colors;

    private float lightValue;

    private String activePage = "Dashboard";

    @Override
    protected int getContentResourceId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);


        setSupportActionBar(toolbar);
        setTitle("Dashboard");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        colors = getResources().getStringArray(R.array.random_colors);

        displaySelectedScreen(R.id.nav_dash);

        presenter.takeView(this);


    }


    @Override
    protected void onResume() {
        super.onResume();
        presenter.takeView(this);

        if (sensorManager == null) {
            return;
        }


        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorManager.registerListener(this, lightSensor, 1000000);

        Handler handler = new Handler();
        int delay = 1000; //milliseconds

        handler.postDelayed(new Runnable() {
            public void run() {
                presenter.recordSensorData(lightSensor.getName(), lightValue);
                handler.postDelayed(this, delay);
            }
        }, delay);


        // Commented to test only one sensor
        //List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        /*
        for (Sensor sensor : sensors) {
            sensorManager.registerListener(this, sensor, 1000000);
        }
        */
    }


    @Override
    protected void onStop() {
        presenter.dropView();
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.

        displaySelectedScreen(item.getItemId());

        item.setChecked(true);

        drawer.closeDrawers();
        return true;
    }

    private void displaySelectedScreen(int id) {
        Fragment fragment = null;

        switch (id) {
            case R.id.nav_dash:
                fragment = new Dashboard();
                break;
            case R.id.nav_db:
                fragment = new Tables();
                break;
            case R.id.nav_graph:
                fragment = new Graphs();
                break;
            case R.id.nav_about:
                fragment = new About();
                break;
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }


    }


    @Override
    public void listSensors() {
        if (sensorManager != null) {
            return;
        }
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

    }

    @Override
    public void plotGraph(HashMap<String, List<SensorData>> hashMap) {

        GraphView graphView = (GraphView) findViewById(R.id.graph);
        graphView.removeAllSeries();
        graphView.getLegendRenderer().setVisible(false);

        for (String name : hashMap.keySet()) {
            List<SensorData> dataList = hashMap.get(name);

            List<DataPoint> dataPoints = new ArrayList<>();

            for (int i = 0; i < dataList.size(); i++) {
                dataPoints.add(new DataPoint(i + 1, dataList.get(i).getRecording()));
            }

            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(
                    dataPoints.toArray(new DataPoint[dataPoints.size()]));

            List<String> nameList = new ArrayList<>(hashMap.keySet());
            int pos = nameList.indexOf(name);
            if (pos < 0 || pos >= colors.length) {
                pos = 0;
            }

            series.setTitle(name);
            series.setAnimated(false);
            series.setColor(Color.parseColor(colors[pos]));
            series.setDrawDataPoints(true);

            graphView.addSeries(series);

        }

        // legend
        // graphView.getGridLabelRenderer().setVerticalAxisTitle("Intensidad");
        //graphView.getGridLabelRenderer().setHorizontalAxisTitle("Tiempo");
        graphView.getLegendRenderer().setVisible(true);
        //graphView.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
    }


    @Override
    public void makeTable(HashMap<String, List<SensorData>> hashMap) {

        TableLayout tabla = (TableLayout) findViewById(R.id.table_layout);

        for (String name : hashMap.keySet()) {
            List<SensorData> dataList = hashMap.get(name);

            List<DataPoint> dataPoints = new ArrayList<>();

            for (int i = 0; i < dataList.size(); i++) {
                dataPoints.add(new DataPoint(dataList.get(i).getDate(), dataList.get(i).getRecording()));
            }

            TableRow row = (TableRow) findViewById(R.id.row_layout);

            tabla.addView(row);

        }


    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Log.d(TAG, "SENSOR CHANGED: " + sensorEvent.sensor.getName() + ", " + sensorEvent.values);

        lightValue = sensorEvent.values[0];
        //presenter.recordSensorData(sensorEvent.sensor.getName(), sensorEvent.values[0]);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        Log.d(TAG, "onAccuracyChanged: " + sensor);
    }
}
