package hu.pinting.jeversi.sqlite.table;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class BoardTable {
    public static final String TABLE_BOARD = "lines";

    private static final String DATABASE_CREATE = "create table " + TABLE_BOARD + "("
            + Columns._id.name() + " integer primary key autoincrement, "
            + Columns.pos_x.name() + " integer not null, "
            + Columns.pos_y.name() + " integer not null, "
            + Columns.cell.name() + " integer not null" + ");";

    public static void onCreate(final SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(final SQLiteDatabase database, final int oldVersion, final int newVersion) {
        Log.w(BoardTable.class.getName(), "Upgrading from version " + oldVersion + " to " + newVersion);
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_BOARD);
        onCreate(database);
    }

    public enum Columns {
        _id,
        pos_x,
        pos_y,
        cell
    }
}