package com.bjedrasik.elcontacto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DbAccess extends SQLiteOpenHelper {
    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "CONTACT";
        public static final String COLUMN_NAME_NAME = "NAME";
        public static final String COLUMN_NAME_SURNAME = "SURNAME";
        public static final String COLUMN_NAME_EMAIL = "EMAIL";
        public static final String COLUMN_NAME_PHONE = "PHONE";
        public static final String COLUMN_NAME_PHOTO = "PHOTO";
        public static final String COLUMN_NAME_NULLABLE = null;
    }

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Contacts.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" + FeedEntry._ID + " INTEGER PRIMARY KEY,"
            + FeedEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP
            + FeedEntry.COLUMN_NAME_SURNAME + TEXT_TYPE + COMMA_SEP
            + FeedEntry.COLUMN_NAME_EMAIL + TEXT_TYPE + COMMA_SEP
            + FeedEntry.COLUMN_NAME_PHONE + TEXT_TYPE + COMMA_SEP
            + FeedEntry.COLUMN_NAME_PHOTO + TEXT_TYPE +  " )";

    private static final String SQL_SELECT_ALL_DATA = "SELECT * FROM " + FeedEntry.TABLE_NAME;

    private static final String SQL_DELETE_ENTRIES ="DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;

    public DbAccess(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME);
        onCreate(db);
    }

    public String insertData(String name, String surname, String email, String phone, String photoURL) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(FeedEntry.COLUMN_NAME_NAME, name);
        contentValues.put(FeedEntry.COLUMN_NAME_SURNAME, surname);
        contentValues.put(FeedEntry.COLUMN_NAME_EMAIL, email);
        contentValues.put(FeedEntry.COLUMN_NAME_PHONE, phone);
        contentValues.put(FeedEntry.COLUMN_NAME_PHOTO, photoURL);

        long newRowId;
        newRowId = db.insert(
                FeedEntry.TABLE_NAME,
                FeedEntry.COLUMN_NAME_NULLABLE,
                contentValues
        );

        String id = Long.toString(newRowId);
        return id;

    }

    public Cursor readData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery(SQL_SELECT_ALL_DATA, null);
        return result;
    }
}
