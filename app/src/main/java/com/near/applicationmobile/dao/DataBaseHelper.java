package com.near.applicationmobile.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.near.applicationmobile.entities.Product;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "project.db";
    private static final int DB_VERSION = 1;
    private final String TAG = this.getClass().getCanonicalName();


    private static DataBaseHelper dataBaseHelper;

    public DataBaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static DataBaseHelper getInstance(Context context){
        if (dataBaseHelper == null){
            dataBaseHelper = new DataBaseHelper(context);
        }
        return dataBaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.i(TAG,"onCreate avant la creation");
        db.execSQL(Product.CREATE_TABLE);
        Log.i(TAG,"base de donnees créée");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
