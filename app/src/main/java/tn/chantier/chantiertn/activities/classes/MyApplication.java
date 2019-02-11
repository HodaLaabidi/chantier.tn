package tn.chantier.chantiertn.activities.classes;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

    public static Context context ;

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }

    public static Context getAppContext(){
        return  MyApplication.context;
    }
}
