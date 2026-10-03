package co.dontlookaway.cite.fragments;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import co.dontlookaway.cite.R;
import co.dontlookaway.cite.api.ErrorResponse;
import co.dontlookaway.cite.api.GsonRequest;
import co.dontlookaway.cite.api.RequestCallback;
import co.dontlookaway.cite.api.group.GroupManager;
import co.dontlookaway.cite.api.group.JoinGroupResponse;
import co.dontlookaway.cite.components.adapters.ErrorAdapter;
import co.dontlookaway.cite.models.Error;
import co.dontlookaway.cite.util.Logger;

public class JoinGroupFragment extends CustomFragment {

    private final Logger<JoinGroupFragment> logger = new Logger<>(JoinGroupFragment.class);

    private String groupCode;
    private GsonRequest<JoinGroupResponse> request;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_join_group, container, false);
        setupListeners(view);
        return view;
    }

    private void setupListeners(View view) {
        ConstraintLayout root = view.findViewById(R.id.joinGroupConstraintLayout);
        Button joinButton = view.findViewById(R.id.joinGroupButton);
        Button cancelButton = view.findViewById(R.id.joinGroupCancelButton);

        root.setOnClickListener(this::hideSoftKeyboard);
        joinButton.setOnClickListener(this::joinGroup);
        cancelButton.setOnClickListener(this::cancel);
    }

    public void hideSoftKeyboard(View v) {
        InputMethodManager imm =  (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public void joinGroup(View v) {
        readInputVariables();
        resetErrorFeedback();
        setupRequest();
        executeRequest();
    }

    private void readInputVariables() {
        EditText groupCodeInput = activity.findViewById(R.id.groupCodeInput);
        this.groupCode = groupCodeInput.getText().toString();
    }

    private void resetErrorFeedback() {
        TextView groupCodeLabel = activity.findViewById(R.id.groupNameLabel);
        EditText groupCodeInput = activity.findViewById(R.id.groupNameInput);

        groupCodeLabel.setTextAppearance(activity, R.style.Widget_AppCompat_TextView_Valid);

        groupCodeInput.setTextAppearance(activity, R.style.Widget_AppCompat_EditText_Valid);
        groupCodeInput.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue_900)));
    }

    private void setupRequest() {
        RequestCallback<JoinGroupResponse> callback = createCallback();
        this.request = GroupManager.getInstance().joinGroupRequest(this.groupCode, callback);
    }

    private RequestCallback<JoinGroupResponse> createCallback() {
        return new RequestCallback<JoinGroupResponse>() {
            @Override
            public void onResponse(JoinGroupResponse response) {
                Toast toast = Toast.makeText(activity, "Successfully sent join request, wait till accepted", Toast.LENGTH_LONG);
                toast.show();
                activity.finish();
            }

            @Override
            public void onError(int statusCode, ErrorResponse errorResponse) {
                logger.error("Error while trying to create a join request");
                Toast toast = Toast.makeText(activity, "Error while trying to create a join request, try again.", Toast.LENGTH_LONG);
                toast.show();
                updateErrorRecyclerView(errorResponse.getErrors());
            }
        };
    }

    private void updateErrorRecyclerView(Error[] errors) {
        ErrorAdapter errorAdapter = new ErrorAdapter(activity, errors);
        RecyclerView errorRecyclerView = activity.findViewById(R.id.joinGroupErrorView);
        errorRecyclerView.setAdapter(errorAdapter);
        errorRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
    }

    private void executeRequest() {
        RequestQueue queue = Volley.newRequestQueue(activity);
        queue.add(this.request);
    }

    public void cancel(View v) {
        activity.finish();
    }
}
