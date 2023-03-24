package com.mka.filmviewer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static String DATABASE_PATH_AND_NAME;
    private static String CHECK_DATABASES_FOLDER;
    private static final String DATABASE_NAME = "film.db";
    private static final String LOG_TAG = "FILM_DB";
    Context ctx;

    DBOpenHelper dbm;
    SQLiteDatabase sqdb;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupDatabaseStrings();
        setUpDatabase(); // Copy the Database music.db to the databases folder
        InitDataBase(); // open the music.db for reading and writing
        //SQLAdapter();

        //Enter the Explorer
        Button buttonEnter = findViewById(R.id.buttonEnter);
        buttonEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent filmmanager = new Intent(MainActivity.this,FilmExplorer.class);
                startActivity(filmmanager);
            }
        });

        //Create variable of About and Help buttons with their functions
        Button aboutButton = findViewById(R.id.buttonAbout);
                aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
            }
        });


        Button helpButton = findViewById(R.id.buttonHelp);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, HelpActivity.class));
            }
        });

    }

    protected void setupDatabaseStrings()
    {
        // Full path to where we will copy music.db to on the emulator!
        DATABASE_PATH_AND_NAME = "/data/data/" + getApplicationContext().getPackageName() +
                "/databases/" + DATABASE_NAME;
        // Used to check if the "databases" folder exists
        CHECK_DATABASES_FOLDER = "/data/data/" + getApplicationContext().getPackageName() +
                "/databases";
        // Debug information
        Log.i("DATABASE_PATH_AND_NAME","DATABASE_PATH_AND_NAME = " + DATABASE_PATH_AND_NAME);
        Log.i("CHECK_DATABASES_FOLDER","CHECK_DATABASES_FOLDER = " + CHECK_DATABASES_FOLDER);
    } // protected void setupDatabaseStrings()

    protected void setUpDatabase()
    {
        ctx = this.getBaseContext();
        try
        {
            CopyDataBaseFromAsset();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    } // protected void setUpDatabase()

    protected void CopyDataBaseFromAsset() throws IOException
    {
        Log.w( LOG_TAG , "Starting copying...");
        String outputFileName = DATABASE_PATH_AND_NAME;
        File databaseFolder = new File( CHECK_DATABASES_FOLDER );
        // databases folder exists ? No - Create it and copy !!!
        if ( !databaseFolder.exists() )
        {
            databaseFolder.mkdir();
            // Open the sqlite database "music.db" found in the assets folder
            InputStream in = ctx.getAssets().open(DATABASE_NAME);
            OutputStream out = new FileOutputStream(outputFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ( (length = in.read(buffer)) > 0 )
            {
                out.write(buffer,0,length);
            } // while ( (length = in.read(buffer)) > 0 )
            out.flush();
            out.close();
            in.close();
            Log.w(LOG_TAG, "Completed.");
        } // if ( !databaseFolder.exists() )
    } // protected void CopyDataBaseFromAsset() throws IOException

    public void InitDataBase()
    {
        // Init the SQLite Helper Class
        dbm = new DBOpenHelper(this);

        // RETRIEVE A READABLE AND WRITEABLE DATABASE
        sqdb = dbm.getWritableDatabase();

    } // public void InitDataBase()

}