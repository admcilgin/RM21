package retailmode.mx.retailmode21.utils;

import android.os.Build;

/**
 * Created by gustavo.arellano on 12/2/2017.
 */

public class DeviceInfoUtils {

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model).replaceAll("\\s+", "");
        } else {
            return (capitalize(manufacturer) + "" + model).replaceAll("\\s+", "");
        }
    }


    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }
}
