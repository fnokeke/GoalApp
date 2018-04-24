package io.smalldata.frangoal;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Operations for locally storing data in files
 * that will later be transferred to a server endpoint
 * Created by fnokeke on April 24, 2018
 */

public class LocalStorage {
    private static final String TAG = "LocalStorage";

    public static void appendToFile(Context context, String filename, String data) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(filename, Context.MODE_APPEND);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e(TAG, "appendToFile: error" + e.toString());
//            AlarmHelper.showInstantNotif(context, "appendToFile error", e.toString(), "", 5003);
        }
    }

    static String readFromFile(Context context, String filename) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(filename);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString;
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e(TAG, "File not found: " + e.toString());
//            AlarmHelper.showInstantNotif(context, "File not found error", e.toString(), "", 5113);
        } catch (IOException e) {
            Log.e(TAG, "Can not read file: " + e.toString());
//            AlarmHelper.showInstantNotif(context, "Cannot read file", e.toString(), "", 5223);
        }

        return ret;
    }

    static void resetFile(Context context, String filename) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            outputStreamWriter.write("");
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e(TAG, "resetFile: error" + e.toString());
//            AlarmHelper.showInstantNotif(context, "resetFile error", e.toString(), "", 5333);
        }
    }
}
