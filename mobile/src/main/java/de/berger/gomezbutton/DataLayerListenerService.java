package de.berger.gomezbutton;

import android.content.Intent;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * ListenerService um die App über die Wear zu starten.
 */
public class DataLayerListenerService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        if ("/MESSAGEWEAR".equals(messageEvent.getPath())) {
            // launch some Activity or do anything you like
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("wear", true);
            startActivity(intent);
        }
    }
}
