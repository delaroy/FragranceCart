package com.delaroystudios.fragrancecart.firebase;

import android.content.ContentResolver;
import android.os.Bundle;
import android.util.Log;

import com.delaroystudios.fragrancecart.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static com.delaroystudios.fragrancecart.sync.FragranceSyncAdapter.getSyncAccount;

/**
 * Created by delaroy on 10/25/17.
 */

public class FirebaseMsgService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMsgService";

    public static final String AUTHORITY = "com.delaroystudios.fragrancecart";
    public static final String ACCOUNT_TYPE = "fragrancecart.delaroystudios.com";
    public static final String ACCOUNT = "FragranceCart";
    public static final String KEY_SYNC_REQUEST = "fragrancecart.delaroystudios.com.KEY_SYNC_REQUEST";

    public FirebaseMsgService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Notification Title: " +
                    remoteMessage.getNotification().getTitle());

            Log.d(TAG, "Notification Message: " +
                    remoteMessage.getNotification().getBody());
        }

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " );
           // remoteMessage.getData().toString();
            if(remoteMessage.getData().get(KEY_SYNC_REQUEST).toString().equals("sync")){
                ContentResolver.requestSync(getSyncAccount(this), AUTHORITY, new Bundle());
                Log.d(TAG, "SyncAdapter triggered: " );

            }




        }
    }


}
