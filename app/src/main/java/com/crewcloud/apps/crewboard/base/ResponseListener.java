package com.crewcloud.apps.crewboard.base;

import android.support.annotation.NonNull;
import android.util.Log;

import com.crewcloud.apps.crewboard.dtos.ErrorDto;

import io.realm.Realm;
import rx.Observer;

/**
 * Created by mb on 3/18/16
 */
public abstract class ResponseListener<T> implements Observer<T> {
    private ErrorDto messageResponse = null;
    private boolean fromCache;

    @Override
    public final void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
        try {
            // Your onError handling code
            ErrorDto errorDto = new ErrorDto();
            errorDto.setMessage("Error");
            onError(errorDto);
        } catch (Exception e1) {
            // Catch the culprit who's causing this whole problem
            Log.e("ERROR", e1.getMessage());
        }


    }

    @Override
    public void onNext(T s) {
            onSuccess(s);
        if (!fromCache) {
            try {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                onSaveData(s, realm);
                realm.commitTransaction();
            } catch (Throwable e) {
            }
        }
    }

    public abstract void onSuccess(T result);

    public abstract void onError(@NonNull ErrorDto messageResponse);

    public T onFetchDataFromCacheBG(Realm realm) {
        return null;
    }

    public boolean isFromCache() {
        return fromCache;
    }

    public void onSaveData(T result, Realm realm) {
    }
}
