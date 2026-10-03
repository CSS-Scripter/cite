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
import co.dontlookaway.cite.api.auth.RegisterResponse;
import co.dontlookaway.cite.api.auth.SessionManager;
import co.dontlookaway.cite.components.adapters.ErrorAdapter;
import co.dontlookaway.cite.models.Error;

public class RegisterActivity extends CustomActivity {

    private String email;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }


    public void hideSoftKeyboard(View v) {
        InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public void register(View view) {
        setupCredentialsFromInputs();
        resetErrorFeedback();
        makeRegisterRequest();
    }

    private void setupCredentialsFromInputs() {
        EditText emailField = findViewById(R.id.emailTextInput);
        EditText usernameField = findViewById(R.id.usernameTextInput);
        EditText passwordField = findViewById(R.id.passwordTextInput);

        this.email = emailField.getText().toString();
        this.username = usernameField.getText().toString();
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
        TextView usernameLabel = findViewById(R.id.usernameText);
        EditText usernameInput = findViewById(R.id.usernameTextInput);

        emailLabel.setTextAppearance(this, validTextViewStyle);
        passwordLabel.setTextAppearance(this, validTextViewStyle);
        usernameLabel.setTextAppearance(this, validTextViewStyle);

        emailInput.setTextAppearance(this, validEditTextStyle);
        emailInput.setBackgroundTintList(validColor);
        passwordInput.setTextAppearance(this, validEditTextStyle);
        passwordInput.setBackgroundTintList(validColor);
        usernameInput.setTextAppearance(this, validEditTextStyle);
        usernameInput.setBackgroundTintList(validColor);
    }

    private void makeRegisterRequest() {
        RequestCallback<RegisterResponse> callback = createCallback();
        SessionManager sessionManager = SessionManager.getInstance();
        GsonRequest<RegisterResponse> request = sessionManager.register(this, this.email, this.username, this.password, callback);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private RequestCallback<RegisterResponse> createCallback() {
        return new RequestCallback<RegisterResponse>() {
            @Override
            public void onResponse(RegisterResponse response) {
                Intent intent = new Intent(RegisterActivity.this, GroupOverviewActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(int status, ErrorResponse error) {
                Log.d("AcLogin", "Failed to register");
                if (status == 400) {
                    for ( Error err : error.getErrors()) {
                        if (err.getField() == null) continue;
                        if (err.getField().equals("email")) markEmailAsInvalid();
                        if (err.getField().equals("password")) markPasswordAsInvalid();
                        if (err.getField().equals("username")) markUsernameAsInvalid();
                    }
                    showInvalidInputsToast();
                }
                updateErrorRecyclerView(error.getErrors());
            }
        };
    }

    private void updateErrorRecyclerView(Error[] errors) {
        ErrorAdapter errorAdapter = new ErrorAdapter(this, errors);
        RecyclerView errorRecyclerView = findViewById(R.id.registerErrorView);
        errorRecyclerView.setAdapter(errorAdapter);
        errorRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void showInvalidInputsToast() {
        String invalidInputsMessage = getString(R.string.register_toast_invalid_inputs);
        Toast toast = Toast.makeText(this, invalidInputsMessage, Toast.LENGTH_LONG);
        toast.show();
    }

    private void markEmailAsInvalid() {
        markTextViewAsInvalid(R.id.emailText);
        markEditTextAsInvalid(R.id.emailTextInput);
    }

    private void markUsernameAsInvalid() {
        markTextViewAsInvalid(R.id.usernameText);
        markEditTextAsInvalid(R.id.usernameTextInput);
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

    public void cancel(View view) {
        finish();
    }
}