package com.virtualstore.virtualstore;

import android.app.Application;
import com.virtualstore.virtualstore.database.DataManager;

public class AdminApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DataManager.init(this);
    }
}
