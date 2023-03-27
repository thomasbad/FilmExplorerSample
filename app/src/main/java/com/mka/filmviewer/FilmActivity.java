package com.mka.filmviewer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class FilmActivity extends AppCompatActivity {
    private static String DATABASE_PATH_AND_NAME;
    private static String CHECK_DATABASES_FOLDER;
    //private static final String DATABASE_NAME = "music.db";
    private static final String LOG_TAG = "MUSIC_DB";
    Context ctx;
    OpenDatabase sqh;
    SQLiteDatabase sqdb;

    Button displayAllRecordsButton, searchButton;
    TextView resultsTextView, searchEditText, numRecordTextView;

    ListView listview;
    ArrayList<String> list;
    ArrayAdapter adapter;
    String[] values = new String[] { "Android", "iPhone",
            "WindowsMobile","Blackberry", "WebOS", "Ubuntu",
            "Windows7", "Max OS X","Linux", "OS/2", "Ubuntu" };

    // Objects used for the Spinner
    Spinner spinner;
    ArrayList<String> spinnerList;
    ArrayAdapter spinnerAdapter;
    // First Value is blank/empty.
    String[] spinnerValues = new String[] { "Choose a State", "Alabama", "Alaska",
            "Arizona","Arkansas", "California", "Colorado", "Connecticut",
            "Delaware","Florida", "Georgia", "Hawaii", "Idaho", "Illinois",
            "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana",
            "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota",
            "Mississippi", "Missouri", "Montana", "Nebraska", "Nevada",
            "New Hampshire", "New Jersey", "New Mexico", "New York",
            "North Carolina",
            "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania",
            "Rhode Island", "South Carolina", "South Dakota", "Tennessee",
            "Texas","Utah", "Vermont", "Virginia", "Washington", "Washington D.C.",
            "West Virginia", "Wisconsin", "Wyoming" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filmview);

        setupDatabaseStrings();
        SETUPDatabase();

        InitDataBase();
//        sqh.DisplayRecords(sqdb);
        setupControls();

        numRecordTextView = findViewById(R.id.numRecordTextView);
        numRecordTextView.setText(Integer.toString(sqh.countRecords(sqdb)));
    }

//    private void DisplayRecords()
//    {
//        Cursor c = sqdb.rawQuery("SELECT * FROM songtable", null);
//        if (c != null)
//        {
//            if (c.moveToFirst())
//            {
//                do
//                {
//                    String id = c.getString(0);
//                    String songtitle = c.getString(1);
//                    String year = c.getString(2);
//                    String artist = c.getString(3);
//                    String album = c.getString(4);
//                    Log.w("SONG_TABLE", "ID = " + id + " Songtitle = " + songtitle);
//                } while (c.moveToNext());
//            }
//        } c.close();
//    } //public void DisplayRecords()*/

    private void InitDataBase() {
        sqh = new OpenDatabase(this);
        sqdb = sqh.getWritableDatabase();
    }

    private void SETUPDatabase() {
        ctx = this.getBaseContext();
        Log.w("CTX", "ctx = " + ctx);
        Log.w("getBaseContext()", "getBaseContext = " + getBaseContext());
        try {
            CopyDatabaseFromAsset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void CopyDatabaseFromAsset() throws IOException {

        Log.w(LOG_TAG, "Starting copy...");
        String outputFileName = DATABASE_PATH_AND_NAME;
        File databaseFolder = new File(CHECK_DATABASES_FOLDER);
        if (!databaseFolder.exists()) {
            databaseFolder.mkdir();
            InputStream in = ctx.getAssets().open(OpenDatabase.DATABASE_NAME);
            OutputStream out = new FileOutputStream(outputFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            out.flush();
            out.close();
            in.close();
            Log.w(LOG_TAG, "Complete");
        }

    }

    private void setupDatabaseStrings() {
        CHECK_DATABASES_FOLDER = "/data/data/" + getApplicationContext().getPackageName() + "/databases";
        DATABASE_PATH_AND_NAME = "/data/data/" + getApplicationContext().getPackageName() + "/databases/" + OpenDatabase.DATABASE_NAME;

        Log.i("DATABASE_PATH_AND_NAME", "DATABASE_PATH_AND_NAME" + DATABASE_PATH_AND_NAME);
        Log.i("CHECK_DATABASES_FOLDER", "CHECK_DATABASES_FOLDER" + CHECK_DATABASES_FOLDER);
    }

    protected void setupControls() {
        displayAllRecordsButton = findViewById(R.id.displayAllRecordsButton);
//        resultsTextView = findViewById(R.id.resultsTextView);
        displayAllRecordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                resultsTextView.setText(sqh.allRecordsInSongtable(sqdb));
            }
        });

        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resultsTextView.setText(sqh.searchRecords(sqdb,
                        searchEditText.getText().toString()));
            }
        });

    }
}
