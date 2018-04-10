package retailmode.mx.retailmode21;

import android.Manifest;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import retailmode.mx.retailmode21.utils.Utilerias;

public class MainActivity extends AppCompatActivity {

    private TextView textViewProgressInfo = null;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.SYSTEM_ALERT_WINDOW
                }, 1);

        View decorView = getWindow().getDecorView();
        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        KeyguardManager keyguardManager = (KeyguardManager)getSystemService(Activity.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock lock = keyguardManager.newKeyguardLock(KEYGUARD_SERVICE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        lock.disableKeyguard();

        textViewProgressInfo = (TextView) findViewById(R.id.textInfoProgress);
        textViewProgressInfo.setText(getResources().getString(R.string.text_info_progress_start));

            try {
                if (null != Utilerias.readVideosFromDirectory() && Utilerias.readVideosFromDirectory().size() > 0) {
                    textViewProgressInfo.setText(getResources().getString(R.string.text_info_progress_playing_video));
                    startRetailModeActivity();
                } else {
                    progressBar = (ProgressBar) findViewById(R.id.loading_progress_xml);

                    progressBar.setVisibility(View.GONE);
                    textViewProgressInfo.setText("No hay contenido para reproducir");
                }
            } catch (Exception e) {
                progressBar = (ProgressBar) findViewById(R.id.loading_progress_xml);

                progressBar.setVisibility(View.GONE);
                textViewProgressInfo.setText("No hay contenido para reproducir");
            }
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        String DEBUG_TAG = "RM";
        int action = MotionEventCompat.getActionMasked(event);

        switch(action) {
            case (MotionEvent.ACTION_DOWN) :
                Log.d(DEBUG_TAG,"Action was DOWN");

                Intent i = new Intent(MainActivity.this, MainActivity.class);
                startActivity(i);

                return true;
            case (MotionEvent.ACTION_MOVE) :
                Log.d(DEBUG_TAG,"Action was MOVE");
                return true;
            case (MotionEvent.ACTION_UP) :
                Log.d(DEBUG_TAG,"Action was UP");
                return true;
            case (MotionEvent.ACTION_CANCEL) :
                Log.d(DEBUG_TAG,"Action was CANCEL");
                return true;
            case (MotionEvent.ACTION_OUTSIDE) :
                Log.d(DEBUG_TAG,"Movement occurred outside bounds " +
                        "of current screen element");
                return true;
            default :
                return super.onTouchEvent(event);
        }
    }

    public void startRetailModeActivity() {
        Intent i = new Intent(MainActivity.this, RetailModeActivity.class);
        startActivity(i);
    }

}
