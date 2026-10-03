package co.dontlookaway.cite.fragments;

import android.app.Activity;
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

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import co.dontlookaway.cite.R;
import co.dontlookaway.cite.api.ErrorResponse;
import co.dontlookaway.cite.api.group.GroupManager;
import co.dontlookaway.cite.api.group.GroupsObserver;
import co.dontlookaway.cite.components.adapters.ErrorAdapter;
import co.dontlookaway.cite.models.Error;
import co.dontlookaway.cite.models.Group;

public class CreateGroupFragment extends CustomFragment implements GroupsObserver {
    private String groupName;
    private Activity activity;

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        GroupManager.getInstance().subscribe(this);
        if (context instanceof Activity) {
            activity = (Activity) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        GroupManager.getInstance().unsubscribe(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_group, container, false);
        setupListeners(view);
        return view;
    }

    private void setupListeners(View view) {
        ConstraintLayout root = view.findViewById(R.id.createGroupConstraintLayout);
        Button createButton = view.findViewById(R.id.createGroupButton);
        Button cancelButton = view.findViewById(R.id.createGroupCancelButton);

        root.setOnClickListener(this::hideSoftKeyboard);
        createButton.setOnClickListener(this::makeNewGroup);
        cancelButton.setOnClickListener(this::cancel);
    }

    public void hideSoftKeyboard(View v) {
        InputMethodManager imm =  (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public void makeNewGroup(View v) {
        readInputFields();
        resetErrorFeedback();
        GroupManager.getInstance().createGroup(this.activity, this.groupName);
    }

    private void readInputFields() {
        EditText groupNameInput = this.activity.findViewById(R.id.groupNameInput);
        this.groupName = groupNameInput.getText().toString();
    }

    private void resetErrorFeedback() {
        TextView groupNameLabel = this.activity.findViewById(R.id.groupNameLabel);
        EditText groupNameInput = this.activity.findViewById(R.id.groupNameInput);

        groupNameLabel.setTextAppearance(this.activity, R.style.Widget_AppCompat_TextView_Valid);

        groupNameInput.setTextAppearance(this.activity, R.style.Widget_AppCompat_EditText_Valid);
        groupNameInput.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue_900)));
    }

    @Override
    public void onGroupsUpdated(Group[] groups) {
        GroupManager.getInstance().unsubscribe(this);
        this.activity.finish();
    }

    @Override
    public void onError(ErrorResponse errorResponse) {
        for (Error err : errorResponse.getErrors()) {
            if (err.getField() == null) continue;
            if (err.getField().equals("name")) markGroupNameAsInvalid();
        }
        updateErrorRecyclerView(errorResponse.getErrors());
    }

    private void markGroupNameAsInvalid() {
        if (this.activity == null) return;
        TextView groupNameLabel = this.activity.findViewById(R.id.groupNameLabel);
        EditText groupNameInput = this.activity.findViewById(R.id.groupNameInput);

        groupNameLabel.setTextAppearance(this.activity, R.style.Widget_AppCompat_TextView_Invalid);
        groupNameInput.setTextAppearance(this.activity, R.style.Widget_AppCompat_EditText_InvalidInput);
        groupNameInput.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
    }

    private void updateErrorRecyclerView(Error[] errors) {
        if (this.activity == null) return;
        ErrorAdapter errorAdapter = new ErrorAdapter(activity, errors);
        RecyclerView errorRecyclerView = activity.findViewById(R.id.newGroupErrorView);
        errorRecyclerView.setAdapter(errorAdapter);
        errorRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
    }

    public void cancel(View v) {
        this.activity.finish();
    }

}
