package com.khach.feed.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "feed_db";
    private static final int DB_VERSION = 1;

    public static final String FEED_TABLE = "feeds";

    public static final String FEED_ID = "id";
    public static final String FEED_WEB_TITLE = "web_title";
    public static final String FEED_SECTION_NAME = "section_name";
    public static final String FEED_THUMBNAIL = "thumbnail";

    private static final String DB_CREATE = "CREATE TABLE " + FEED_TABLE + "("
            + FEED_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + FEED_WEB_TITLE + " text, "
            + FEED_SECTION_NAME + " text, "
            + FEED_THUMBNAIL + " text " + ");";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FEED_TABLE);
        onCreate(db);
    }
}
