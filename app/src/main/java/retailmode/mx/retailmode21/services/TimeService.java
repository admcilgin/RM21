package retailmode.mx.retailmode21.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import java.util.Timer;
import java.util.TimerTask;

import retailmode.mx.retailmode21.BuildConfig;
import retailmode.mx.retailmode21.RetailModeActivity;

public class TimeService extends Service implements View.OnTouchListener, View.OnKeyListener {

    private String TAG = this.getClass().getSimpleName();

    // window manager
    private WindowManager mWindowManager;
    // linear layout will use to detect touch event
    private LinearLayout touchLayout;

    PowerManager.WakeLock wl;
    PowerManager pm;

    // constant
    public static final long NOTIFY_INTERVAL = 15000;

    Runnable r;

    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    // timer handling
    private Timer mTimer = null;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        restartTimer();
        return true;
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        restartTimer();
        return false;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        System.out.println("TimeService->onCreate()");

        // create linear layout
        touchLayout = new LinearLayout(this);
        // set layout width 30 px and height is equal to full screen
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(30, LinearLayout.LayoutParams.MATCH_PARENT);
        touchLayout.setLayoutParams(lp);
        // set color if you want layout visible on screen
//		touchLayout.setBackgroundColor(Color.CYAN);
        // set on touch listener
        touchLayout.setOnTouchListener(this);
        touchLayout.setOnKeyListener(this);

        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_TOAST;
        }

        // fetch window manager object
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        // set layout parameter of window manager
        WindowManager.LayoutParams mParams = new WindowManager.LayoutParams(
                1, // width of layout 30 px
                1, // height is equal to full screen
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSPARENT);
        mParams.gravity = Gravity.LEFT | Gravity.TOP;
        mWindowManager.addView(touchLayout, mParams);

        // cancel if already existed
        if (mTimer != null) {
            mTimer.cancel();
        } else {
            // recreate new
            mTimer = new Timer();
        }
        // schedule task
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       return START_NOT_STICKY;
    }

    public void onDestroy() {
        stopHandler();

        if(mWindowManager != null) {
            if(touchLayout != null) mWindowManager.removeView(touchLayout);
        }

        System.out.println("TimeService->onDestroy :::");

        mTimer.cancel();
        super.onDestroy();
    }

    /*=================================================*/
    public void stopHandler() {
        mHandler.removeCallbacks(r);
    }

    public void startHandler() {
        mHandler.postDelayed(r, NOTIFY_INTERVAL);
    }

    /**
     * Método que reinicia el timer para la activación del Activity
     */
    private void restartTimer() {
        mTimer.cancel();
        mHandler.removeCallbacks(r);

        mTimer = new Timer();
        mHandler.postDelayed(r, NOTIFY_INTERVAL);
    }

    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {

            // run on another thread
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    mHandler = new Handler();
                    r = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                pm = (PowerManager)getSystemService(Context.POWER_SERVICE);

                                if (!pm.isScreenOn()) {
                                    wl = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "CollectData");
                                    System.out.println("Screen off - wake lock acquired");
                                    wl.acquire();
                                } else {
                                    System.out.println("Screen on - no need of wake lock");
                                }
                            } catch (Exception e) {

                            }

                            // TODO Auto-generated method stub
                            Intent intent = new Intent(TimeService.this, RetailModeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    };
                    startHandler();
                }
            });
        }
    }
}