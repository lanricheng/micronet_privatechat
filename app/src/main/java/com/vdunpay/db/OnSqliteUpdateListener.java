package com.vdunpay.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by HY on 2018/8/17.
 */

public interface OnSqliteUpdateListener {
    public void onSqliteUpdateListener(SQLiteDatabase db, int oldVersion, int newVersion);
}
