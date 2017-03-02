package com.leo.poemapp;

import android.app.Application;
import android.util.Log;

import com.leo.poemapp.dao.DaoMaster;
import com.leo.poemapp.dao.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * Created by Leo on 2017-03-02.
 */

public class App extends Application {

    private static final boolean ENCRYPTED = false;
    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, ENCRYPTED ? "db-encrypted" : Constant.DB_NAME);
        Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
        Log.d("App", helper.getWritableDatabase().getPath());
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
