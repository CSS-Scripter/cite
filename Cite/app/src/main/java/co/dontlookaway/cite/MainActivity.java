package co.dontlookaway.cite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import co.dontlookaway.cite.api.ErrorResponse;
import co.dontlookaway.cite.api.RequestCallback;
import co.dontlookaway.cite.api.auth.CurrentUserResponse;
import co.dontlookaway.cite.api.auth.SessionManager;

public class MainActivity extends CustomActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        SessionManager.getInstance().updateCurrentUser(this, createOnCurrentUserCallback(this));
    }

    public void toLogin(View view) {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void toRegister(View view) {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SessionManager.getInstance().updateCurrentUser(this, createOnCurrentUserCallback(this));
    }

    private RequestCallback<CurrentUserResponse> createOnCurrentUserCallback(Context context) {
        return new RequestCallback<CurrentUserResponse>() {
            @Override
            public void onResponse(CurrentUserResponse response) {
                String currentUsername = response.getCurrentUser().getUsername();
                Toast.makeText(context, String.format("Welcome back %s!", currentUsername), Toast.LENGTH_SHORT).show();
                showContinueAsButton(currentUsername);
            }

            @Override
            public void onError(int statusCode, ErrorResponse errorResponse) {
                Toast.makeText(context, "Session has expired", Toast.LENGTH_SHORT).show();
                hideContinueAsButton();
            }
        };
    }

    private void showContinueAsButton(String username) {
        Button button = findViewById(R.id.continueAs);
        button.setText(String.format("Continue as %s", username));
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(this::continueAs);
        findViewById(R.id.mainLogout).setVisibility(View.VISIBLE);
        findViewById(R.id.mainLoginButton).setVisibility(View.INVISIBLE);
        findViewById(R.id.mainRegisterButton).setVisibility(View.INVISIBLE);
    }

    private void hideContinueAsButton() {
        Button button = findViewById(R.id.continueAs);
        button.setVisibility(View.INVISIBLE);
        button.setOnClickListener((view) -> Toast.makeText(this, "lol u thought", Toast.LENGTH_LONG));
        findViewById(R.id.mainLogout).setVisibility(View.INVISIBLE);
        findViewById(R.id.mainLoginButton).setVisibility(View.VISIBLE);
        findViewById(R.id.mainRegisterButton).setVisibility(View.VISIBLE);
    }

    private void continueAs(View view) {
        Intent intent = new Intent(MainActivity.this, GroupOverviewActivity.class);
        startActivity(intent);
    }

    public void logout(View view) {
//        Toast.makeText(this, "Not implemented yet", Toast.LENGTH_LONG).show();
        SessionManager.getInstance().logOut(this);
        hideContinueAsButton();
    }
}