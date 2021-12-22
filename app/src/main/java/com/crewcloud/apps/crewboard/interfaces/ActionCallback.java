package com.crewcloud.apps.crewboard.interfaces;

/**
 * Created by dazone on 2/20/2017.
 */

public interface ActionCallback<T> {
    T onBackground();

    void onForeground(T result);
}
