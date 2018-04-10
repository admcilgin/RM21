package retailmode.mx.retailmode21.receiver;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import retailmode.mx.retailmode21.RetailModeActivity;

/**
 * Created by gustavo.arellano on 1/3/2018.
 */

public class RMDeviceAdminReceiver extends DeviceAdminReceiver {

    public static final String ACTION_DISABLED = "device_admin_action_disabled";
    public static final String ACTION_ENABLED = "device_admin_action_enabled";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        super.onDisabled(context, intent);
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(ACTION_DISABLED));
    }
    @Override
    public void onEnabled(Context context, Intent intent) {
        super.onEnabled(context, intent);
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(ACTION_ENABLED));
    }

    /**
     * Called on the new profile when device owner provisioning has completed. Device owner
     * provisioning is the process of setting up the device so that its main profile is managed by
     * the mobile device management (MDM) application set up as the device owner.
     */
    @Override
    public void onProfileProvisioningComplete(Context context, Intent intent) {
        // Enable the profile
        DevicePolicyManager manager =
                (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName componentName = getComponentName(context);
        manager.setProfileName(componentName, "RetailModeProfile");
        // Open the main screen
        Intent launch = new Intent(context, RetailModeActivity.class);
        launch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(launch);
    }

    /**
     * @return A newly instantiated {@link ComponentName} for this
     * DeviceAdminReceiver.
     */
    public static ComponentName getComponentName(Context context) {
        return new ComponentName(context.getApplicationContext(), RMDeviceAdminReceiver.class);
    }
}
