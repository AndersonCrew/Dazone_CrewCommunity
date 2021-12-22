package com.crewcloud.apps.crewboard.util;

import android.os.AsyncTask;

import com.crewcloud.apps.crewboard.interfaces.ActionCallback;


/**
 * Created by mb on 7/27/16.
 */

public class BackgroundUtils {
    public static <T> void doAction(ActionCallback<T> actionCallback) {
        DoAction<T> doAction = new DoAction<>(actionCallback);
        doAction.execute();
    }

    private static class DoAction<T> extends AsyncTask<Void, Void, T> {
        private ActionCallback<T> actionCallback;

        public DoAction(ActionCallback<T> actionCallback) {
            this.actionCallback = actionCallback;
        }

        @Override
        protected T doInBackground(Void... params) {
            if (actionCallback != null) {
                return actionCallback.onBackground();
            }
            return null;
        }

        @Override
        protected void onPostExecute(T result) {
            if (actionCallback != null) {
                actionCallback.onForeground(result);
            }
        }
    }
}
