package io.smalldata.basifgoal;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import org.json.JSONObject;

import io.smalldata.basifgoal.api.CallAPI;
import io.smalldata.basifgoal.api.VolleyJsonCallback;

public class AppJobService extends JobService {

    private static final String TAG = "BeehiveAppJobService";

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        updateServerRecords(getApplicationContext());
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    public void updateServerRecords(Context context) {
        sendAllLocalData(context);
    }

    private void sendAllLocalData(Context context) {
        sendNotifLogs(context);
        sendPAMLogs(context);
        sendSurveyLogs(context);
        sendInAppAnalytics(context);
    }

    public static void registerMobileUserOnLoginComplete(Context context) {
        JSONObject data = new JSONObject();
        JsonHelper.setJSONValue(data, "email", "email");
        JsonHelper.setJSONValue(data, "code", "code");
        JSONObject fromPhoneDetails = DeviceInfo.getPhoneDetails(context);
        JsonHelper.copy(fromPhoneDetails, data);
        CallAPI.registerMobileUser(context, data, getLogResponseHandler(context, null));
    }

    private void sendNotifLogs(Context context) {
        String filename = Constants.NOTIF_EVENT_LOGS_CSV;
        JSONObject data = getLocalData(context, filename);
        CallAPI.submitNotifLogs(context, data, getLogResponseHandler(context, filename));
    }

    private void sendPAMLogs(Context context) {
        String filename = Constants.PAM_LOGS_CSV;
        JSONObject data = getLocalData(context, filename);
        if (!data.optString("logs").equals("")) {
            CallAPI.submitPAMLog(context, data, getLogResponseHandler(context, filename));
        }
    }

    private void sendSurveyLogs(Context context) {
        String filename = Constants.SURVEY_LOGS_CSV;
        JSONObject data = getLocalData(context, filename);
        if (!data.optString("logs").equals("")) {
            CallAPI.submitSurveyLog(context, data, getLogResponseHandler(context, filename));
        }
    }

    private void sendInAppAnalytics(Context context) {
        String filename = Constants.ANALYTICS_LOG_CSV;
        JSONObject data = getLocalData(context, filename);
        CallAPI.submitAnalytics(context, data, getLogResponseHandler(context, filename));
    }

    private static JSONObject getLocalData(Context context, String filename) {
        JSONObject params = new JSONObject();
        JsonHelper.setJSONValue(params, "email", "email");
        JsonHelper.setJSONValue(params, "code", "code");
        JsonHelper.setJSONValue(params, "logs", LocalStorage.readFromFile(context, filename));
        return params;
    }

    private static VolleyJsonCallback getLogResponseHandler(final Context context, final String filenameToReset) {
        return new VolleyJsonCallback() {
            @Override
            public void onConnectSuccess(JSONObject result) {
                Log.i(TAG, filenameToReset + " Submit Response: " + result.toString());
                if (filenameToReset != null) {
                    LocalStorage.resetFile(context, filenameToReset);
                }
            }

            @Override
            public void onConnectFailure(VolleyError error) {
                String msg = "Contact researcher: " + error.toString();
                Log.e(TAG, filenameToReset + " StatsError: " + msg);
                // FIXME: 1/24/18 remove debug code
                AlarmHelper.showInstantNotif(context,
                        "At " + DateHelper.getFormattedTimestamp() + " sent failed!",
                        "Error: " + msg,
                        "",
                        8961);
            }
        };

    }

}
