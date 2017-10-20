package com.delaroystudios.fragrancecart.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by delaroy on 10/18/17.
 */

public class FragranceAuthenticatorService extends Service {
    // Instance field that stores the authenticator object
    private FragranceAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new FragranceAuthenticator(this);
    }

    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
