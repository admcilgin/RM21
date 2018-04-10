package retailmode.mx.retailmode21.utils;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.UserManager;
import android.support.annotation.RequiresApi;

import retailmode.mx.retailmode21.receiver.RMDeviceAdminReceiver;

/**
 * Created by gustavo.arellano on 2/12/2018.
 */

public class DevicePolicyUtils {

    public static boolean changePassword(Context ctx) {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) ctx.getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName deviceAdminComponentName = new ComponentName(ctx, RMDeviceAdminReceiver.class);

        int currentPasswordQuality = devicePolicyManager.getPasswordQuality(null);
        devicePolicyManager.setPasswordQuality(deviceAdminComponentName, DevicePolicyManager.PASSWORD_QUALITY_SOMETHING);
        devicePolicyManager.setPasswordQuality(deviceAdminComponentName, currentPasswordQuality);

        boolean hasPassword = devicePolicyManager.isActivePasswordSufficient();
        return hasPassword;
    }

    public static void setPolicies(Context ctx) {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) ctx.getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName deviceAdminComponentName = new ComponentName(ctx, RMDeviceAdminReceiver.class);

        devicePolicyManager.setPasswordMinimumLetters(deviceAdminComponentName, 2);
        devicePolicyManager.setPasswordMinimumLowerCase(deviceAdminComponentName, 2);
        devicePolicyManager.setPasswordMinimumNumeric(deviceAdminComponentName, 2);
        devicePolicyManager.setPasswordMinimumUpperCase(deviceAdminComponentName, 2);
        devicePolicyManager.setPasswordMinimumLength(deviceAdminComponentName, 10);

        devicePolicyManager.lockNow();

        //wipeData(ctx);
    }

    public static void wipeData(Context ctx) {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) ctx.getSystemService(Context.DEVICE_POLICY_SERVICE);
        devicePolicyManager.wipeData(0);
    }

    public static void clearDeviceOwnerApp (Context ctx) {
        ComponentName deviceAdminComponentName = new ComponentName(ctx, RMDeviceAdminReceiver.class);
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) ctx.getSystemService(Context.DEVICE_POLICY_SERVICE);
        devicePolicyManager.setSecureSetting(deviceAdminComponentName, UserManager.DISALLOW_CONFIG_CREDENTIALS, "1");
    }

    @RequiresApi(api = 26)
    public static void clearResetPasswordToken (Context ctx) {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) ctx.getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName deviceAdminComponentName = new ComponentName(ctx, RMDeviceAdminReceiver.class);
        devicePolicyManager.clearResetPasswordToken(deviceAdminComponentName);
    }
}
