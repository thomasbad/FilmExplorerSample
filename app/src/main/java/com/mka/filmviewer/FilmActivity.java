package com.mka.filmviewer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleCursorAdapter;
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
    //private static final String DATABASE_NAME = "film.db";
    private static final String LOG_TAG = "FILM_DB";
    private String blockCharacterSet = "~#^|$%&*!";
    LinearLayout mainWindow; //set up layout for hide keyboard purpose
    Context ctx;
    OpenDatabase sqh;
    SQLiteDatabase sqdb;

    Button displayAllRecordsButton, searchButton, buttonOK, buttonCancel;
    TextView resultsTextView, searchEditText, numRecordTextView, editTextFilmTitleEdit, editTexYearEdit, editTextDirectorEdit, editTextCountryEdit, editTextID, editTextFilmTitle, editTexYear, editTextDirector, editTextCountry;

    // Objects used for the Spinner
    Spinner spinnerEditRecord;
    ArrayList<String> spinnerList;
    ArrayAdapter spinnerAdapter;
    // Spinner array
    String[] spinnerValues = new String[] {"Choose Additional Functions", "Add New Record", "Edit Current Record"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filmview);
        mainWindow = findViewById(R.id.mainWindow); //set up layout for hide keyboard purpose

        setupDatabaseStrings();
        SETUPDatabase();

        InitDataBase();
        setupControls();

        numRecordTextView = findViewById(R.id.numRecordTextView);
        numRecordTextView.setText(Integer.toString(sqh.countRecords(sqdb)));
    }


    //-------------DB Setup---------------//
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
    //-----------------End of DB Setup---------------------------//

    //Copy default database from asset folder if not exist
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
    //Setup Database location
    private void setupDatabaseStrings() {
        CHECK_DATABASES_FOLDER = "/data/data/" + getApplicationContext().getPackageName() + "/databases";
        DATABASE_PATH_AND_NAME = "/data/data/" + getApplicationContext().getPackageName() + "/databases/" + OpenDatabase.DATABASE_NAME;

        Log.i("DATABASE_PATH_AND_NAME", "DATABASE_PATH_AND_NAME" + DATABASE_PATH_AND_NAME);
        Log.i("CHECK_DATABASES_FOLDER", "CHECK_DATABASES_FOLDER" + CHECK_DATABASES_FOLDER);
    }

    protected void setupControls() {

        //------------------Display all function----------------------------//
        displayAllRecordsButton = findViewById(R.id.displayAllRecordsButton);
        resultsTextView = findViewById(R.id.resultsTextView);
        displayAllRecordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resultsTextView.setText(sqh.allRecords(sqdb));
                //Hide soft Keyboard after button click to avoid the keyboard blocking the result view
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mainWindow.getWindowToken(), 0);
            }
        });
        //------------------End of Display all function----------------------------//

        //------------------Search function----------------------------------------//
        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resultsTextView.setText(sqh.searchRecords(sqdb, searchEditText.getText().toString()));
                //Hide soft Keyboard after button click to avoid the keyboard blocking the result view
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mainWindow.getWindowToken(), 0);
            }
        });
        //------------------End of Search function----------------------------------------//
        //-------------------------Setup Spinner------------------------------------------//

        //Spinner Options
        spinnerEditRecord = (Spinner)findViewById(R.id.spinnerEditRecord);
        spinnerList = new ArrayList<String>();
        for (int i = 0; i < spinnerValues.length; ++i)
        {
            spinnerList.add(spinnerValues[i]);
        }

        //Setup and connect adapter
        spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, spinnerList);
        spinnerEditRecord.setAdapter(spinnerAdapter);

        //Spinner function
        spinnerEditRecord.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                if (position != 0) // do they select the first entry "" empty!
                {

                }
                else if (position == 1)
                {
                    AddRecord();
                }
                else if (position == 2)
                {

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //-------------------------End of Setup Spinner------------------------------------------//
    }

    // Pop-up Window setup
    public void AddRecord() {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        editTextFilmTitleEdit = findViewById(R.id.editTextFilmTitleEdit);
        editTexYearEdit = findViewById(R.id.editTexYearEdit);
        editTextDirectorEdit = findViewById(R.id.editTextDirectorEdit);
        editTextCountryEdit = findViewById(R.id.editTextCountryEdit);


        //OK button function
        buttonOK = findViewById(R.id.buttonOKEdit);
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqh.addRecords(sqdb, editTextFilmTitleEdit.getText().toString(), editTexYearEdit.getText().toString(), editTextDirectorEdit.getText().toString(), editTextCountryEdit.getText().toString());
                popupWindow.dismiss();
            }
        });

        //Cancel button function
        buttonCancel = findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        //Dismiss pop up window when non-pop-window area is touched, or back/home key is pressed
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false; //return true to disable this function
            }
        });
    }

    public void EditRecord(View view) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window_edit, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);


        //Cancel button function
        buttonCancel = findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        //Dismiss pop up window when non-pop-window area is touched, or back/home key is pressed
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false; //return true to disable this function
            }
        });
    }
}

