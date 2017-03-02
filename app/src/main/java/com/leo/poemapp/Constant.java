package com.leo.poemapp;

import android.os.Environment;

/**
 * Created by Leo on 2017-03-02.
 */

public class Constant {
    public static final String APP_DIR = Environment.getExternalStorageDirectory().getPath() + "/poems/";

    public static final String DB_NAME = "poems.db";
    public static final String DB_DIR = "/data/com.leo.poemapp/databases/";
    public static final String DB_PATH = "/data/com.leo.poemapp/databases/poems.db";
}
