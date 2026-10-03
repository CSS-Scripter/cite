package co.dontlookaway.cite;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import co.dontlookaway.cite.api.ErrorResponse;
import co.dontlookaway.cite.api.GsonRequest;
import co.dontlookaway.cite.api.RequestCallback;
import co.dontlookaway.cite.api.quotes.CreateQuoteResponse;
import co.dontlookaway.cite.api.quotes.QuoteManager;
import co.dontlookaway.cite.components.adapters.ErrorAdapter;
import co.dontlookaway.cite.models.Error;
import co.dontlookaway.cite.models.Group;
import co.dontlookaway.cite.util.Logger;

public class CreateQuoteActivity extends CustomActivity {

    private Group group;
    private String quote;
    private String quotee;
    private GsonRequest<CreateQuoteResponse> request;
    private final QuoteManager quoteManager = new QuoteManager();
    private final Logger<CreateQuoteActivity> logger = new Logger<>(CreateQuoteActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quote);
        setupGroupFromIntent();
    }


    private void setupGroupFromIntent() {
        String groupKey = getString(R.string.intent_group_key);
        String groupValue = getIntent().getStringExtra(groupKey);
        this.group = new Gson().fromJson(groupValue, Group.class);
    }

    public void hideSoftKeyboard(View v) {
        InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public void onCreateButtonPressed(View view) {
        setupValuesFromInputs();
        resetErrorFeedback();
        setupRequest();
        executeRequest();
    }

    private void setupValuesFromInputs() {
        EditText quoteInput = findViewById(R.id.editQuote);
        EditText quoteeInput = findViewById(R.id.editQuotee);
        this.quote = quoteInput.getText().toString();
        this.quotee = quoteeInput.getText().toString();
    }

    private void resetErrorFeedback() {
        int validTextViewStyle = R.style.Widget_AppCompat_TextView_Valid;
        int validEditTextStyle = R.style.Widget_AppCompat_EditText_Valid;
        ColorStateList validColor = ColorStateList.valueOf(getResources().getColor(R.color.blue_900));

        TextView quoteLabel = findViewById(R.id.quoteLabel);
        EditText quoteInput = findViewById(R.id.editQuote);
        TextView quoteeLabel = findViewById(R.id.quoteeLabel);
        EditText quoteeInput = findViewById(R.id.editQuotee);

        quoteLabel.setTextAppearance(this, validTextViewStyle);
        quoteeLabel.setTextAppearance(this, validTextViewStyle);

        quoteInput.setTextAppearance(this, validEditTextStyle);
        quoteInput.setBackgroundTintList(validColor);
        quoteeInput.setTextAppearance(this, validEditTextStyle);
        quoteeInput.setBackgroundTintList(validColor);
    }

    private void setupRequest() {
        RequestCallback<CreateQuoteResponse> callback = createRequestCallback();
        this.request = this.quoteManager.createQuoteRequest(this.quote, this.quotee, this.group.getId(), callback);
    }

    private RequestCallback<CreateQuoteResponse> createRequestCallback() {
        return new RequestCallback<CreateQuoteResponse>() {
            @Override
            public void onResponse(CreateQuoteResponse response) {
                logger.info("Successfully created quote");
                finish();
            }

            @Override
            public void onError(int statusCode, ErrorResponse errorResponse) {
                logger.error("An error has occurred whilst creating a quote");
                for (Error error : errorResponse.getErrors()) {
                    if (error.getField() == null) continue;
                    if (error.getField().equals("quote")) markQuoteAsInvalid();
                    if (error.getField().equals("quotee")) markQuoteeAsInvalid();
                }
                updateErrorRecyclerView(errorResponse.getErrors());
            }
        };
    }

    private void markQuoteAsInvalid() {
        markTextViewAsInvalid(R.id.quoteLabel);
        markEditTextAsInvalid(R.id.editQuote);
    }

    private void markQuoteeAsInvalid() {
        markTextViewAsInvalid(R.id.quoteeLabel);
        markEditTextAsInvalid(R.id.editQuotee);
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
        RecyclerView errorRecyclerView = findViewById(R.id.createQuoteErrorRecyclerView);
        errorRecyclerView.setAdapter(errorAdapter);
        errorRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void executeRequest() {
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(this.request);
    }

    public void cancel(View v) {
        finish();
    }
}