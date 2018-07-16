package io.smalldata.basifgoal;

import android.os.Environment;

import java.util.Locale;

/**
 * Created by fnokeke on 1/3/18.
 * Centralized place for shared constants
 */

public class Constants {
    public static final Locale LOCALE = Locale.getDefault();

    public final static String ALARM_NOTIF_TITLE = "title";
    public final static String ALARM_NOTIF_CONTENT = "content";
    public final static String ALARM_APP_ID = "app_id";
    public final static String ALARM_MILLIS_SET = "alarm_millis";
    public final static String ALARM_NOTIF_WAS_DISMISSED = "was_dismissed";
    public static final String ALARM_PROTOCOL_METHOD = "method";

    public final static String NOTIFICATION_ID = "notification-id";
    public final static String NOTIFICATION = "notification";


    public static final String NOTIF_EVENT_LOGS_CSV = "notifLogs.csv";
    public static final String ANALYTICS_LOG_CSV = "analytics.csv";
    public static final String SURVEY_LOGS_CSV = Environment.getExternalStorageDirectory().getPath() + "/BeehiveSurvey.csv";
    public static final String PAM_LOGS_CSV = Environment.getExternalStorageDirectory().getPath() + "/BeehivePAM.csv";

    public static final String TYPE_PUSH_NOTIFICATION = "push_notification";

    public static final String NOTIF_TYPE = "notif_type";
    public static final String RS_TYPE = "rs_type";

}
