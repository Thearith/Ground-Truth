package org.smcnus.groundtruth;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FileLogger {

    // constants
    private static final String TAG                     = FileLogger.class.getSimpleName();

    private static final String GROUND_TRUTH_FOLDER     = "iRace Heel Strike";
    private static final String LOG_FILE_EXTENSION      = ".txt";
    private static final String SEPERATOR               = "\t";
    private static final String TIMESTAMP_LOG_FORMAT    = // index timestamp
            "%d" + SEPERATOR + "%s";
    private static final String TIMESTAMP_FORMAT        = "MMMM dd yyyy | HH:mm:ss:SSS";

    private static FileLogger fileLogger;
    private ArrayList<String> logs;

    /*
    * Constructor
    * */

    public static FileLogger getInstance() {
        if(fileLogger == null) {
            fileLogger = new FileLogger();
        }

        return fileLogger;
    }

    private FileLogger() {
        logs = new ArrayList<>();
    }


    /*
    * public methods
    * */

    public void writeLogsToFile(ArrayList<Long> timestamps) {
        long firstTimestamp = timestamps.get(0);
        String recordDirPath = createGroundTruthFolderIfNotExist();
        String fileName = recordDirPath + "/" + getFileName(firstTimestamp) + LOG_FILE_EXTENSION;
        File logFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), fileName);

        Log.d(TAG, fileName);

        addTimestampsToLog(timestamps);

        try {
            if (!logFile.exists())
                logFile.createNewFile();

            BufferedWriter outputStreamWriter = new BufferedWriter(new FileWriter(logFile));

            for(int index=0; index<logs.size(); index++) {
                String log = logs.get(index) + "\n";
                outputStreamWriter.write(log);
            }

            outputStreamWriter.close();
            logs.clear();

        } catch (IOException e) {
            Log.e(TAG, "File write failed: " + e.toString());
        }
    }

    private void addTimestampsToLog(ArrayList<Long> timestamps) {
        for(int index=0; index<timestamps.size(); index++)
            addTimestampToLog(index+1, timestamps.get(index));
    }

    private void addTimestampToLog(int index, long timestamp) {
        String log = String.format(TIMESTAMP_LOG_FORMAT, index, String.valueOf(timestamp));
        logs.add(log);
    }


    /*
    * Helper method
    * */

    private String getFileName(long timestamp) {
        return "Ground Truth - " + getDateFormat(timestamp);
    }

    private String getDateFormat(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat formatter = new SimpleDateFormat(TIMESTAMP_FORMAT);

        return formatter.format(date);
    }

    private String createGroundTruthFolderIfNotExist() {
        String recordDirPath = GROUND_TRUTH_FOLDER;
        File recordDirectory =
                new File(Environment.getExternalStorageDirectory().getAbsolutePath(), recordDirPath);

        if(!recordDirectory.exists())
            recordDirectory.mkdirs();

        return recordDirPath;
    }
}
