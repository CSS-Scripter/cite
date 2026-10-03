package co.dontlookaway.cite;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import co.dontlookaway.cite.api.ErrorResponse;
import co.dontlookaway.cite.api.GsonRequest;
import co.dontlookaway.cite.api.RequestCallback;
import co.dontlookaway.cite.api.auth.LoginResponse;
import co.dontlookaway.cite.api.auth.SessionManager;
import co.dontlookaway.cite.components.adapters.ErrorAdapter;
import co.dontlookaway.cite.models.Error;

public class LoginActivity extends CustomActivity {

    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void hideSoftKeyboard(View v) {
        InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public void login(View view) {
        setupCredentialsFromInputs();
        resetErrorFeedback();
        makeLoginRequest();
    }

    private void setupCredentialsFromInputs() {
        EditText emailField = findViewById(R.id.emailTextInput);
        EditText passwordField = findViewById(R.id.passwordTextInput);

        this.email = emailField.getText().toString();
        this.password = passwordField.getText().toString();
    }

    private void resetErrorFeedback() {
        int validTextViewStyle = R.style.Widget_AppCompat_TextView_Valid;
        int validEditTextStyle = R.style.Widget_AppCompat_EditText_Valid;
        ColorStateList validColor = ColorStateList.valueOf(getResources().getColor(R.color.blue_900));

        TextView emailLabel = findViewById(R.id.emailText);
        EditText emailInput = findViewById(R.id.emailTextInput);
        TextView passwordLabel = findViewById(R.id.passwordText);
        EditText passwordInput = findViewById(R.id.passwordTextInput);

        emailLabel.setTextAppearance(this, validTextViewStyle);
        passwordLabel.setTextAppearance(this, validTextViewStyle);

        emailInput.setTextAppearance(this, validEditTextStyle);
        emailInput.setBackgroundTintList(validColor);
        passwordInput.setTextAppearance(this, validEditTextStyle);
        passwordInput.setBackgroundTintList(validColor);
    }

    private void makeLoginRequest() {
        RequestCallback<LoginResponse> callback = createCallback();
        SessionManager sessionManager = SessionManager.getInstance();
        GsonRequest<LoginResponse> request = sessionManager.login(this, this.email, this.password, callback);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private RequestCallback<LoginResponse> createCallback() {
        return new RequestCallback<LoginResponse>() {
            @Override
            public void onResponse(LoginResponse response) {
                Intent intent = new Intent(LoginActivity.this, GroupOverviewActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(int status, ErrorResponse error) {
                Log.d("AcLogin", "Failed to login");
                if (status == 400) {
                    for ( Error err : error.getErrors()) {
                        if (err.getField() == null) continue;
                        if (err.getField().equals("email")) markEmailAsInvalid();
                        if (err.getField().equals("password")) markPasswordAsInvalid();
                    }
                    showInvalidInputsToast();
                }
                updateErrorRecyclerView(error.getErrors());
            }
        };
    }

    private void showInvalidInputsToast() {
        String invalidInputsMessage = getString(R.string.login_toast_invalid_inputs);
        Toast toast = Toast.makeText(this, invalidInputsMessage, Toast.LENGTH_LONG);
        toast.show();
    }

    private void markEmailAsInvalid() {
        markTextViewAsInvalid(R.id.emailText);
        markEditTextAsInvalid(R.id.emailTextInput);
    }

    private void markPasswordAsInvalid() {
        markTextViewAsInvalid(R.id.passwordText);
        markEditTextAsInvalid(R.id.passwordTextInput);
    }

    private void markTextViewAsInvalid(int id) {
        TextView invalidTextView = findViewById(id);
        invalidTextView.setTextAppearance(this, R.style.Widget_AppCompat_TextView_Invalid);
    }

    private void markEditTextAsInvalid(int id) {
        EditText invalidInputField = findViewById(id);
        invalidInputField.setTextAppearance(this, R.style.Widget_AppCompat_EditText_InvalidInput);
        invalidInputField.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
    }

    private void updateErrorRecyclerView(Error[] errors) {
        ErrorAdapter errorAdapter = new ErrorAdapter(this, errors);
        RecyclerView errorRecyclerView = findViewById(R.id.errorView);
        errorRecyclerView.setAdapter(errorAdapter);
        errorRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void cancel(View view) {
        finish();
    }
}