package com.mka.filmviewer;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class OpenDatabase extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME="film.db";
    private static final int DATABASE_VERSION=1;
    public OpenDatabase(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {

    }

    public void DisplayRecords(SQLiteDatabase sqdb)
    {
        Cursor c = sqdb.rawQuery("SELECT * FROM FilmTable", null);
        if (c != null)
        {
            if (c.moveToFirst())
            {
                do
                {
//                    String id = c.getString(0);
//                    String songtitle = c.getString(1);
//                    String year = c.getString(2);
//                    String artist = c.getString(3);
//                    String album = c.getString(4);
//                    Log.w("SONG_TABLE", "ID = " + id + " Songtitle = " + songtitle);
                } while (c.moveToNext());
            }
        } c.close();
    } //public void DisplayRecords()

    public String allRecordsInSongtable(SQLiteDatabase sqdb)
    {
        String result = "";
        Cursor c = sqdb.rawQuery("SELECT * FROM FilmTable", null);
        if (c != null)
        {
            if (c.moveToFirst())
            {
                do
                {
//                    String id = c.getString(0);
//                    result = result + id + ",";
//                    String songtitle = c.getString(1);
//                    result = result + songtitle + ",";
//                    String year = c.getString(2);
//                    result = result + year + ",";
//                    String artist = c.getString(3);
//                    result = result + artist + ",";
//                    String album = c.getString(4);
////                    result = result + album + "\n"; // new line control character
//                    result = result + album; // new line control character
//                    Log.w("SONG_TABLE", "ID = " + id + " Songtitle = " + songtitle);
                } while (c.moveToNext());
            }
        }
        c.close();
        return result;
    } // public String allRecordsInSongtable(SQLiteDatabase sqdb)

    public String searchRecords(SQLiteDatabase sqdb, String searchYear)
    {
        String result = "";
        Cursor c = sqdb.rawQuery("SELECT * FROM FilmTable where year = '" + searchYear + "'",
                null);
        if (c != null)
        {
            if (c.moveToFirst())
            {
                do
                {
//                    String id = c.getString(0);
//                    result = result + id + ",";
//                    String songtitle = c.getString(1);
//                    result = result + songtitle + ",";
//                    String year = c.getString(2);
//                    result = result + year + ",";
//                    String artist = c.getString(3);
//                    result = result + artist + ",";
//                    String album = c.getString(4);
//                    result = result + album + "\n"; // new line control character
//                    Log.w("SONG_TABLE", "ID = " + id + " Songtitle = " + songtitle);
                } while (c.moveToNext());
            }
            else
            {
                result = "No Records Found for the Search Year = " + searchYear;
            }
        }
        c.close();
        return result;
    } // public String searchByYearInSongtable(SQLiteDatabase sqdb, String searchYear)

    public int countRecords(SQLiteDatabase sqdb)
    {
        int count = 0;
        Cursor c = sqdb.rawQuery("SELECT count(*) FROM FilmTable", null);
        if (c != null)
        {
            if (c.moveToFirst())
            {
                do
                {
                    String id = c.getString(0);
                    count = Integer.parseInt( id );
                } while (c.moveToNext());
            }
        }
        c.close();
        return count;
    } // public int countRecords(SQLiteDatabase sqdb)

}
