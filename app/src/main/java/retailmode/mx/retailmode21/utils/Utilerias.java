package retailmode.mx.retailmode21.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gustavo.arellano on 1/5/2018.
 */

public class Utilerias {

    public static List<File> readVideosFromDirectory(){
        List<File> videos = new ArrayList<File>();
        File f = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        File files[] = f.listFiles();
        for (File file : files) {
            if (getExtension(file).equalsIgnoreCase("MP4")) {
                System.out.println(file.getName());

                videos.add(file);
            }
        }
        return videos;
    }

    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }

    /**
     * Método que sirve para obtener las propiedades desde AndroidManifest.xml
     *
     * @param context
     * @param name
     * @return
     */
    public static String getMetaData(Context context, String name) {
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            return bundle.getString(name);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("Utilerias", "Unable to load meta-data: " + e.getMessage());
        }
        return null;
    }
}
