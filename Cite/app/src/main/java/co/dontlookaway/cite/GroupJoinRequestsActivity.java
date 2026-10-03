package co.dontlookaway.cite;

import android.content.Intent;
import android.os.Bundle;
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
import co.dontlookaway.cite.api.joinrequest.GetAllJoinRequestsResponse;
import co.dontlookaway.cite.api.joinrequest.JoinRequestManager;
import co.dontlookaway.cite.components.adapters.JoinRequestAdapter;
import co.dontlookaway.cite.models.Group;
import co.dontlookaway.cite.models.JoinRequest;
import co.dontlookaway.cite.util.Config;
import co.dontlookaway.cite.util.Logger;

public class GroupJoinRequestsActivity extends CustomActivity {

    private Group group;
    private GsonRequest<GetAllJoinRequestsResponse> request;

    private final Logger<GroupJoinRequestsActivity> logger = new Logger<>(GroupJoinRequestsActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_join_requests);
        setup();
    }

    private void setup() {
        getGroupFromIntent();
        setupTitle();
        setupRequest();
        executeRequest();
    }

    private void getGroupFromIntent() {
        Intent intent = getIntent();
        String groupJson = intent.getStringExtra(Config.INTENT_GROUP_KEY);
        this.group = new Gson().fromJson(groupJson, Group.class);
    }

    private void setupTitle() {
        TextView title = findViewById(R.id.group_join_requests_title);
        title.setText(this.group.getName());
    }

    private void setupRequest() {
        JoinRequestManager manager = new JoinRequestManager();
        RequestCallback<GetAllJoinRequestsResponse> callback = getCallback();
        this.request = manager.getAllGroupJoinRequestsRequest(this.group, callback);
    }

    private RequestCallback<GetAllJoinRequestsResponse> getCallback() {
        return new RequestCallback<GetAllJoinRequestsResponse>() {
            @Override
            public void onResponse(GetAllJoinRequestsResponse response) {
                logger.info("Gotten all requests");
                populateRequestsRecyclerView(response.getRequests());
            }

            @Override
            public void onError(int statusCode, ErrorResponse errorResponse) {
                logger.error("Error while retrieving all requests");
                logger.error(new Gson().toJson(errorResponse));
            }
        };
    }

    private void executeRequest() {
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(this.request);
    }

    private void populateRequestsRecyclerView(JoinRequest[] requests) {
        JoinRequestAdapter joinRequestAdapter = new JoinRequestAdapter(this, requests);
        RecyclerView joinRequestRecyclerView = findViewById(R.id.groupJoinRequestRecyclerView);
        joinRequestRecyclerView.setAdapter(joinRequestAdapter);
        joinRequestRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}