package cz.vancura.contentprovider_publisher2020.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import cz.vancura.contentprovider_publisher2020.data.MyDataContract;

public class MyDbHelper extends SQLiteOpenHelper {

    private static String TAG = "myTAG-MyDbHelper";

    private static final String DATABASE_NAME = "tasksDb.db";
    private static final int VERSION = 1;

    // Constructor
    MyDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.d(TAG, "onCreate");

        // Create tasks table (careful to follow SQL formatting rules)
        final String CREATE_TABLE = "CREATE TABLE "  + MyDataContract.TaskEntry.TABLE_NAME + " (" +
                MyDataContract.TaskEntry._ID                + " INTEGER PRIMARY KEY, " +
                MyDataContract.TaskEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                MyDataContract.TaskEntry.COLUMN_PRIORITY    + " INTEGER NOT NULL);";

        db.execSQL(CREATE_TABLE);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MyDataContract.TaskEntry.TABLE_NAME);
        onCreate(db);
    }
}