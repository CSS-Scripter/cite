package co.dontlookaway.cite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import co.dontlookaway.cite.api.ErrorResponse;
import co.dontlookaway.cite.api.GsonRequest;
import co.dontlookaway.cite.api.RequestCallback;
import co.dontlookaway.cite.api.quotes.GetAllQuotesResponse;
import co.dontlookaway.cite.api.quotes.QuoteManager;
import co.dontlookaway.cite.components.adapters.QuoteAdapter;
import co.dontlookaway.cite.components.menus.GroupOptionsMenu;
import co.dontlookaway.cite.models.Group;
import co.dontlookaway.cite.models.Quote;
import co.dontlookaway.cite.util.Logger;

public class QuotesActivity extends CustomActivity {

    private GsonRequest<GetAllQuotesResponse> request;
    private Group group;
    private final Logger<QuotesActivity> logger = new Logger<>(QuotesActivity.class);

    @Override
    public void onResume() {
        super.onResume();
        renderQuotes();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotes);

        setupGroupFromIntent();
        renderView();
    }

    private void setupGroupFromIntent() {
        String groupKey = getString(R.string.intent_group_key);
        String groupValue = getIntent().getStringExtra(groupKey);
        this.group = new Gson().fromJson(groupValue, Group.class);
    }

    private void renderView() {
        setTitleToGroupName();
        renderQuotes();
    }

    private void setTitleToGroupName() {
        TextView title = findViewById(R.id.groupName);
        title.setText(this.group.getName());
    }

    private void renderQuotes() {
        createGetAllGroupsRequest();
        executeGetAllGroupsRequest();
    }

    private void createGetAllGroupsRequest() {
        RequestCallback<GetAllQuotesResponse> callback = createOnQuoteCallback();
        this.request = new QuoteManager().getQuotes(this.group, callback);
    }

    private void executeGetAllGroupsRequest() {
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(this.request);
    }

    private RequestCallback<GetAllQuotesResponse> createOnQuoteCallback() {
        return new RequestCallback<GetAllQuotesResponse>() {
            @Override
            public void onResponse(GetAllQuotesResponse response) {
                logger.info("Retrieved all quotes successfully");
                updateGroupsView(response.getQuotes());
            }

            @Override
            public void onError(int status, ErrorResponse error) {
                logger.error("Failed to retrieve quotes");
                couldNotLoadToast();
            }
        };
    }

    private void couldNotLoadToast() {
        String message = getString(R.string.quotes_toast_failed_to_load_message);
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }
    private void updateGroupsView(Quote[] quotes) {
        QuoteAdapter quoteAdapter = new QuoteAdapter(this, quotes);
        RecyclerView quoteRecyclerView = findViewById(R.id.quotesView);
        quoteRecyclerView.setAdapter(quoteAdapter);
        quoteRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void navigateToCreateQuote(View v) {
        Intent intent = new Intent(QuotesActivity.this, CreateQuoteActivity.class);
        String groupKey = getString(R.string.intent_group_key);
        String groupValue = new Gson().toJson(group);
        intent.putExtra(groupKey, groupValue);
        startActivity(intent);
    }

    public void openGroupOptions(View v) {
        GroupOptionsMenu optionsMenu = new GroupOptionsMenu(this, v, this.group);
        optionsMenu.show();
    }


}