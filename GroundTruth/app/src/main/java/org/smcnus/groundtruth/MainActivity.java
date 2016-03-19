package org.smcnus.groundtruth;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String SINGPAORE_TIMEZONE = "Asia/Singapore";

    private FileLogger fileLogger;

    private TextView stepsCountTextView;

    private Calendar calendar;

    private ArrayList<Long> timestampList;
    private int stepCount;


    /*
    * Activity methods
    * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        initializeFileLogger();
        initializeCalendar();
        initializeData();
        initializeWidgets();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /*
    * overridden methods
    * */

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.recordTimestampBtn:
                long timestamp = calendar.getTimeInMillis();
                addTimestampList(timestamp);
                incrementStepCount();
                break;

            case R.id.stopButton:
                writeLogsToFile();
                makeToast();
                break;
        }
    }


    /*
    * File logger
    * */

    private void initializeFileLogger() {
        fileLogger = FileLogger.getInstance();
    }

    private void writeLogsToFile() {
        if(timestampList.size() > 0) {
            fileLogger.writeLogsToFile(timestampList);
        }
    }


    /*
    * Calendar methods
    * */

    private void initializeCalendar() {
        TimeZone timeZone = TimeZone.getTimeZone(SINGPAORE_TIMEZONE);
        calendar = Calendar.getInstance(timeZone);
    }

    /*
    * Data methods
    * */

    private void initializeData() {
        initializeStepCount();
        initializeTimestampList();
    }

    private void initializeStepCount() {
        stepCount = 0;
    }

    private void initializeTimestampList() {
        timestampList = new ArrayList<>();
    }

    private void incrementStepCount() {
        stepCount += 1;
        updateStepCountTextView();
    }

    private void addTimestampList(long timestamp) {
        timestampList.add(timestamp);
    }


    /*
    * Widgets method
    * */

    private void initializeWidgets() {
        initializeStepCountTextView();
        initializeRecordButton();
        initializeStopButton();
    }

    private void initializeStepCountTextView() {
        stepsCountTextView = (TextView) findViewById(R.id.stepsCountTextView);
    }

    private void updateStepCountTextView() {
        stepsCountTextView.setText(String.valueOf(stepCount));
    }

    private void initializeRecordButton() {
        LinearLayout recordTimestampBtn = (LinearLayout) findViewById(R.id.recordTimestampBtn);
        recordTimestampBtn.setOnClickListener(this);
    }

    private void initializeStopButton() {
        LinearLayout stopButton = (LinearLayout) findViewById(R.id.stopButton);
        stopButton.setOnClickListener(this);
    }

    private void makeToast() {
        Toast.makeText(this, "Recording Finished", Toast.LENGTH_SHORT).show();
    }
}
