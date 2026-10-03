package co.dontlookaway.cite;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;;

public class CiteFirebaseInstanceIdService extends FirebaseMessagingService {
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d("NEW_TOKEN", s);

    }


}
