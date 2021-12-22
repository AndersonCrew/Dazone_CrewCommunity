package com.crewcloud.apps.crewboard.di.module;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.crewcloud.apps.crewboard.BuildConfig;
import com.crewcloud.apps.crewboard._Application;
import com.crewcloud.apps.crewboard.di.scope.AppScope;
import com.crewcloud.apps.crewboard.net.MyApi;
import com.crewcloud.apps.crewboard.net.ToStringConverterFactory;
import com.crewcloud.apps.crewboard.util.CrewBoardApplication;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mb on 7/27/16.
 */
@Module
@AppScope
public class AppModule {

    public AppModule() {
    }

    @Provides
    @AppScope
    public MyApi provideApi(OkHttpClient okHttpClient) {
        synchronized (MyApi.class) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(CrewBoardApplication.getInstance().getmPrefs().getServerSite())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(new ToStringConverterFactory())
                    .client(okHttpClient)
                    .build();
            return retrofit.create(MyApi.class);
        }
    }

    @Provides
    @AppScope
    protected OkHttpClient provideOkHttp() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.writeTimeout(15 * 60 * 1000, TimeUnit.MILLISECONDS);
        httpClient.readTimeout(60 * 1000, TimeUnit.MILLISECONDS);
        httpClient.connectTimeout(20 * 1000, TimeUnit.MILLISECONDS);
        return httpClient.build();
    }

    @Provides
    @AppScope
    protected Application provideApplication() {
        return CrewBoardApplication.getInstance();
    }

    @Provides
    public Realm provideRealm(RealmConfiguration realmConfiguration) {
        Realm.setDefaultConfiguration(realmConfiguration);
        return Realm.getDefaultInstance();
    }

    @Provides
    @AppScope
    protected RealmConfiguration provideRealmConfig(Application application, RealmMigration realmMigration) {
        return new RealmConfiguration
                .Builder(application)
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .migration(realmMigration)
                .build();
    }

    @Provides
    @AppScope
    protected RealmMigration provideRealmMigration() {
        return new RealmMigration() {
            @Override
            public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

            }
        };
    }

    @Provides
    @AppScope
    public EventBus provideEventBus() {
        return EventBus.getDefault();
    }


    @Provides
    Context provideContext() {
        return CrewBoardApplication.getInstance();
    }

}
