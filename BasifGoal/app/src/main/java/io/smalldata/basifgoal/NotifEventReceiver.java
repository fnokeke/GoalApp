package io.smalldata.basifgoal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.util.Locale;

/**
 * Handle all notification dismissed event
 * Created by fnokeke on 6/5/17.
 */

public class NotifEventReceiver extends BroadcastReceiver {
    private final Locale locale = Locale.getDefault();
    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        handleBundle(intent.getExtras());
    }

    private void handleBundle(Bundle bundle) {
        if (bundle == null) {
            AlarmHelper.showInstantNotif(mContext, "Error: handleBundle is null.", DateHelper.getFormattedTimestamp(), "", 2025);
            return;
        }
        saveNotifToLocalStorage(bundle);

        String method = bundle.getString(Constants.ALARM_PROTOCOL_METHOD);
        if (method == null) {
            AlarmHelper.showInstantNotif(mContext, "Error: method == null.", DateHelper.getFormattedTimestamp(), "", 2025);
            return;
        }

        boolean wasDismissed = bundle.getBoolean("was_dismissed");
        if (wasDismissed) {
            Toast.makeText(mContext, method + " was dismissed.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "Launching " + method, Toast.LENGTH_SHORT).show();
            if (method.equals(Constants.TYPE_PUSH_NOTIFICATION)) {
                mContext.startActivity(new Intent(mContext, MainActivity.class));
                String appIdToLaunch = bundle.getString(Constants.ALARM_APP_ID);
                IntentLauncher.launchApp(mContext, appIdToLaunch);
            }
        }

    }

    private void saveNotifToLocalStorage(Bundle bundle) {
        long alarmTimeMillis = bundle.getLong(Constants.ALARM_MILLIS_SET);
        String phoneRingerMode = DeviceInfo.getRingerMode(mContext);

//        Profile profile = new Profile(mContext);
        String username = "";
        String studyCode = "";

        String title = bundle.getString(Constants.ALARM_NOTIF_TITLE);
        String content = bundle.getString(Constants.ALARM_NOTIF_CONTENT);
        String appId = bundle.getString(Constants.ALARM_APP_ID);
        String method = bundle.getString(Constants.ALARM_PROTOCOL_METHOD);
        long timeOfClickOrDismiss = System.currentTimeMillis();
        boolean wasDismissed = bundle.getBoolean(Constants.ALARM_NOTIF_WAS_DISMISSED);

        String data = String.format(locale, "%s, %s, %d, %s, %s, %s, %s, %s, %s, %d\n",
                username,
                studyCode,
                alarmTimeMillis,
                phoneRingerMode,
                method,
                title,
                content,
                appId,
                wasDismissed,
                timeOfClickOrDismiss);

        LocalStorage.appendToFile(mContext, Constants.NOTIF_EVENT_LOGS_CSV, data);
    }

}

