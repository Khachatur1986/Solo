package com.khach.feed.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.khach.feed.models.PinnedModel;

import java.util.ArrayList;
import java.util.List;

public class TableControllerFeed extends DBHelper {
    public TableControllerFeed(Context context) {
        super(context);
    }

    public boolean create(PinnedModel pinnedModel) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FEED_WEB_TITLE, pinnedModel.getWebTitle());
        contentValues.put(FEED_SECTION_NAME, pinnedModel.getSectionName());
        contentValues.put(FEED_THUMBNAIL, pinnedModel.getThumbnail());

        SQLiteDatabase db = this.getWritableDatabase();

        boolean createSuccessful = db.insert(FEED_TABLE, null, contentValues) > 0;
        db.close();
        return createSuccessful;
    }

    public List<PinnedModel> read() {

        List<PinnedModel> recordsList = new ArrayList<PinnedModel>();

        String sql = "SELECT * FROM " + FEED_TABLE + " ORDER BY id DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {

                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(FEED_ID)));
                String webTitle = cursor.getString(cursor.getColumnIndex(FEED_WEB_TITLE));
                String sectionName = cursor.getString(cursor.getColumnIndex(FEED_SECTION_NAME));
                String thumbnail = cursor.getString(cursor.getColumnIndex(FEED_THUMBNAIL));

                PinnedModel pinnedModel = new PinnedModel(id, webTitle, sectionName, thumbnail);
                recordsList.add(pinnedModel);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return recordsList;
    }

    public List<PinnedModel> readByLimitAndOffset(int limit, int offset) {

        List<PinnedModel> recordsList = new ArrayList<PinnedModel>();

        String sql = "SELECT * FROM " + FEED_TABLE + " ORDER BY id DESC LIMIT " + limit + " OFFSET " + offset;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {

                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(FEED_ID)));
                String webTitle = cursor.getString(cursor.getColumnIndex(FEED_WEB_TITLE));
                String sectionName = cursor.getString(cursor.getColumnIndex(FEED_SECTION_NAME));
                String thumbnail = cursor.getString(cursor.getColumnIndex(FEED_THUMBNAIL));

                PinnedModel pinnedModel = new PinnedModel(id, webTitle, sectionName, thumbnail);
                recordsList.add(pinnedModel);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return recordsList;
    }

    public int count() {
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "SELECT * FROM " + FEED_TABLE;
        int recordCount = db.rawQuery(sql, null).getCount();
        db.close();
        return recordCount;
    }

    public PinnedModel readSingleRecord(int pinnedId) {
        PinnedModel pinnedModel = null;
        String sql = "SELECT * FROM " + FEED_TABLE + " WHERE id = " + pinnedId;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(FEED_ID)));
            String webTitle = cursor.getString(cursor.getColumnIndex(FEED_WEB_TITLE));
            String sectionName = cursor.getString(cursor.getColumnIndex(FEED_SECTION_NAME));
            String thumbnail = cursor.getString(cursor.getColumnIndex(FEED_THUMBNAIL));

            pinnedModel = new PinnedModel(id, webTitle, sectionName, thumbnail);
        }
        cursor.close();
        db.close();
        return pinnedModel;
    }

    public boolean update(PinnedModel pinnedModel) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FEED_WEB_TITLE, pinnedModel.getWebTitle());
        contentValues.put(FEED_SECTION_NAME, pinnedModel.getSectionName());
        contentValues.put(FEED_THUMBNAIL, pinnedModel.getThumbnail());

        String where = "id = ?";

        String[] whereArgs = {Integer.toString(pinnedModel.getId())};

        SQLiteDatabase db = this.getWritableDatabase();

        boolean updateSuccessful = db.update(FEED_TABLE, contentValues, where, whereArgs) > 0;
        db.close();
        return updateSuccessful;
    }

    public boolean delete(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean deleteSuccessful = db.delete(FEED_TABLE, "id ='" + id + "'", null) > 0;
        db.close();
        return deleteSuccessful;
    }
}
