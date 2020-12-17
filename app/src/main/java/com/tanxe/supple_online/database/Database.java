package com.tanxe.supple_online.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import static com.tanxe.supple_online.database.Constant.CREATE_PRODUCT_CART_TABLE;
import static com.tanxe.supple_online.database.Constant.PRODUCT_CART_TABLE;

public class Database extends SQLiteOpenHelper {
    public Database(@Nullable Context context) {
        super(context, "supple.sql", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PRODUCT_CART_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PRODUCT_CART_TABLE);

    }
}
