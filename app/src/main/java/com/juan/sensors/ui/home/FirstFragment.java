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
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.juan.sensors.R;
import com.juan.sensors.model.SensorData;
import com.juan.sensors.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import dagger.android.AndroidInjection;

import dagger.android.AndroidInjection;

/**
 * Created by Juan Palomino on 24/09/2017.
 */

class FirstFragmentextends BaseActivity implements
        NavigationView.OnNavigationItemSelectedListener,HomeContract.View,SensorEventListener{

private static final String TAG=HomeActivity.class.getName();

@BindView(R.id.toolbar)
    Toolbar toolbar;

@BindView(R.id.drawer_layout)
    DrawerLayout drawer;

@BindView(R.id.nav_view)
    NavigationView navigationView;

@BindView(R.id.graph)
    GraphView graphView;

//@BindView(R.id.graph2)
//GraphView graphView2;

@Inject
    HomePresenter presenter;

private SensorManager sensorManager;
// private Sensor gravity;
private Sensor light;

private String[]colors;

private float lightValue;

@Override
protected int getContentResourceId(){
        return R.layout.activity_main;
        }

@Override
protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Snackbar.make(navigationView,"See Dashboard (buttons with actions)",Snackbar.LENGTH_LONG).setAction("Action",null).show();
        }

@Override
public View onCreateView(LayoutInflater inflater,ViewGroup container,
        Bundle savedInstanceState){
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.example_fragment,container,false);
        }
        }
