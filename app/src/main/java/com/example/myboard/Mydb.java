package com.example.myboard;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Mydb extends SQLiteOpenHelper
{
    ///naming my databse store's name DATABSE_NAME
    ///naming my database's table name

    /// naming primary (identity) key by which we can map that value. TAB_ID
    /// naming another column say TAB_NAME
    /// naming second column say TAB_DAYS

    private static final String DATABSE_NAME = "mydb1";
    private static final String TABLE_NAME = "mytab";

    private static final String TAB_ID = "id";
    private static final String TAB_STR = "name";
    private static final String TAB_TIME = "days";

    /// constructor of MyDBFunctions
    public Mydb(Context c)
    {
        /// it is invoking
        super(c, DATABSE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db)
    {
        /// SQL -> structured Query Language
        String s = "CREATE TABLE "+TABLE_NAME+" ("+TAB_ID+" INTEGER PRIMARY KEY, "+TAB_STR+" TEXT, "+TAB_TIME+" TEXT)";
        /// making a structure of containing a table where TAB_ID is primary key , TAB_NAME and TAB_DAYS are columns
        db.execSQL(s);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    // ---- ---- adding data to database --- -----

    void addingDataToTable(DataTemp dt){

        /// it takes writable access
        SQLiteDatabase sqd  = this.getWritableDatabase();

        /// sqd instance  can take content values so make content of column (TAB_NAME and TAB_DAYS)
        ContentValues cv = new ContentValues();
        cv.put(TAB_STR, dt.getstr());
        cv.put(TAB_TIME, dt.getTime());
        /// insert content values in that table
        sqd.insert(TABLE_NAME, null, cv);
        sqd.close();
    }


    // --- ---- showing data ------ ----

    String my_data() {

        String recvied_data="";
        /// Readable access from database
        SQLiteDatabase sq = this.getReadableDatabase();

        /// select all from our table
        String q = "SELECT * FROM "+TABLE_NAME;

        /// cursor is one kind of iterator by this q sql.
        Cursor c = sq.rawQuery(q, null);

        /// starting from first
        c.moveToFirst();

        /// if not null
        if(c.getCount()>0)
        {
            /// we will fill up recvied_data array with data from
            recvied_data = c.getString(c.getColumnIndex(TAB_STR + ""));
        }

        return recvied_data;
    }
}
