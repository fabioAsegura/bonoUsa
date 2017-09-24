package com.juan.sensors.injection;

import com.juan.sensors.ui.home.HomeActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by tinashe on 2017/08/31.
 */

@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector(modules = {
            AppModule.class
    })
    abstract HomeActivity bindHomeActivity();
}
