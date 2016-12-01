package hu.pinting.jeversi.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import hu.pinting.jeversi.sqlite.table.BoardTable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "jeversi.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase sqLiteDatabase) {
        BoardTable.onCreate(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase sqLiteDatabase, final int oldVersion, final int newVersion) {
        BoardTable.onUpgrade(sqLiteDatabase, oldVersion, newVersion);
    }
}