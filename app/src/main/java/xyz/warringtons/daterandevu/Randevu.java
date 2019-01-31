package xyz.warringtons.daterandevu;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;

import com.facebook.stetho.Stetho;
import com.google.firebase.FirebaseApp;

import xyz.warringtons.daterandevu.Modules.DaoMaster;
import xyz.warringtons.daterandevu.Modules.DaoSession;
import xyz.warringtons.daterandevu.Modules.RandevuDaoMaster;

/**
 * Created by Warrington on 8/12/17.
 */


public class Randevu extends Application {

    static Randevu sInstance;
    private static DaoSession daoSession;
    private DaoMaster.DevOpenHelper helper;
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private static SharedPreferences mSettings;
    private static SharedPreferences.Editor editor;

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;
        Stetho.initializeWithDefaults(this);

        helper = new RandevuDaoMaster.DevOpenHelper(this, "randevu-db", null);
        db = helper.getWritableDatabase();
        daoMaster = new RandevuDaoMaster(db);

        if(daoMaster.getSchemaVersion()>7){
            helper.onUpgrade(db, 7, daoMaster.getSchemaVersion());
        }
        daoSession = daoMaster.newSession();

        mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        editor = mSettings.edit();

    }


    public static Context getContext() {
        return sInstance.getApplicationContext();
    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }


    public static SharedPreferences getmSettings() {
        return mSettings;
    }

    public static SharedPreferences.Editor getEditor() {
        return editor;
    }
}
