package co.dontlookaway.cite;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import co.dontlookaway.cite.api.auth.SessionManager;

public class CustomActivity extends AppCompatActivity {
    @Override
    protected void onResume() {
        SessionManager.getInstance().setContext(this);
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
