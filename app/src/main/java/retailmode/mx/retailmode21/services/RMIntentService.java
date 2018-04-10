package retailmode.mx.retailmode21.services;

import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import retailmode.mx.retailmode21.RetailModeActivity;

public class RMIntentService extends IntentService {

    boolean isActive = true;

    public RMIntentService() {
        super("RMIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        showToast("Starting IntentService");
        resetDisconnectTimer();
    }

    public static final long DISCONNECT_TIMEOUT = 100;

    private Handler disconnectHandler = new Handler() {
        public void handleMessage(Notification.MessagingStyle.Message msg) {
        }
    };

    private Runnable disconnectCallback = new Runnable() {
        @Override
        public void run() {
            // Perform any required operation for log out
            Intent intent = new Intent(RMIntentService.this, RetailModeActivity.class);
            startActivity(intent);
        }
    };

    public void resetDisconnectTimer() {
        disconnectHandler.removeCallbacks(disconnectCallback);
        disconnectHandler.postDelayed(disconnectCallback, DISCONNECT_TIMEOUT);

        /*new Thread(new Runnable(){
            public void run() {
                // TODO Auto-generated method stub
                while(isActive)
                {
                    try {
                        Thread.sleep(5000); // 5seg

                        disconnectHandler.removeCallbacks(disconnectCallback);
                        disconnectHandler.postDelayed(disconnectCallback, DISCONNECT_TIMEOUT);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }

            }
        }).start();*/
    }

    public void stopDisconnectTimer() {
        disconnectHandler.removeCallbacks(disconnectCallback);

        //showToast("Stoping IntentService");
    }

    protected void showToast(final String msg){
        //gets the main thread
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                // run this code in the main thread
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
