package com.crewcloud.apps.crewboard.di.component;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

import com.crewcloud.apps.crewboard.base.BaseActivity;
import com.crewcloud.apps.crewboard.base.BaseFragment;
import com.crewcloud.apps.crewboard.di.module.AppModule;
import com.crewcloud.apps.crewboard.di.module.CommonModule;
import com.crewcloud.apps.crewboard.di.scope.AppScope;
import com.crewcloud.apps.crewboard.module.profile.changepass.ChangePassPresenterImp;

import javax.inject.Named;

import dagger.Component;

/**
 * Created by mb on 7/27/16.
 */
@AppScope
@Component(modules = {AppModule.class, CommonModule.class})
public interface AppComponent {

    void inject(BaseActivity baseActivity);

    void inject(BaseFragment baseFragment);

    void inject(ChangePassPresenterImp changePassPresenterImp);


    Context context();

    @Named("vertical_linear")
    LinearLayoutManager vertical_linear();

    @Named("horizontal_linear")
    LinearLayoutManager horizontal_linear();
}
