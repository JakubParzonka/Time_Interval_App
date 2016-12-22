package com.jparzonka.time_interval_app.log_handler;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by Jakub on 2016-12-19.
 */

public class LogHandler {
    private static int counter;

    public LogHandler() {
        counter = 0;
    }


    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    private static File getAlbumStorageDir(Context context, String data, int number) {
        // Create the folder.
        File folder = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "LOG");
        // Create the file.
        System.out.println("counter: " + counter);
        File file = new File(folder, "log" + String.valueOf(number) + ".txt");
        try {
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(data);
            Toast.makeText(context, "Log has been added!", Toast.LENGTH_SHORT).show();
            myOutWriter.close();
            fOut.flush();
            fOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("UPPS", "Directory not created. Let me do it.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Filename: " + file.getName() + "\nFilepather: " + file.getPath());
        if (file.exists()) {
            file.mkdirs();
            Log.e("UPPS", "Directory not created. Let me do it.");
        }

        return file;
    }

    public static void handleLog(Context context, String data, int number) {
        if (LogHandler.isExternalStorageWritable()) {
            LogHandler.getAlbumStorageDir(context, data, number);
        }

    }

}
