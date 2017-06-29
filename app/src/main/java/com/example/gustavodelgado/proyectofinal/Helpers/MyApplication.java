package com.example.gustavodelgado.proyectofinal.Helpers;

import android.app.Application;

import com.example.gustavodelgado.proyectofinal.ConnectivityReceiver;

/**
 * Created by gustavodelgado on 24/06/17.
 */

public class MyApplication extends Application {

    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
