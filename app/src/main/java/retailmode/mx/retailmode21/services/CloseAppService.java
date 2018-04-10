package retailmode.mx.retailmode21.services;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class CloseAppService extends Service {
    private String TAG = this.getClass().getSimpleName();

    public CloseAppService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);

        Log.d(TAG, "Close App Service - trigger");

        try {
            Intent launchIntent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
            launchIntent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS | Intent.FLAG_ACTIVITY_NEW_TASK);
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            launchIntent.setComponent(new ComponentName("retailmode.mx.retailmode21", "retailmode.mx.retailmode21.RetailModeActivity"));
            getApplicationContext().startActivity(launchIntent);
        } catch (Throwable e) {
            new RuntimeException("Failed restart with Instrumentation", e);
        }
    }
}
