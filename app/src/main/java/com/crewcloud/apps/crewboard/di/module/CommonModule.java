package com.crewcloud.apps.crewboard.di.module;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Harry on 7/13/16.
 */
@Module
public class CommonModule {
    @Provides
    @Singleton
    Handler getHandler(){
        return new Handler();
    }

    @Provides
    @Named("vertical_linear")
    LinearLayoutManager verticalLinear(Context context){
        return new LinearLayoutManager(context);
    }

    @Provides
    @Named("horizontal_linear")
    LinearLayoutManager horizontalLinear(Context context){
        return new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false);
    }

}
