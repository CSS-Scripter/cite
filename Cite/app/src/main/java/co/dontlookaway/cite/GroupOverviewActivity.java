package co.dontlookaway.cite;

import android.content.Intent;
import android.os.Bundle;
import android.se.omapi.Session;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import co.dontlookaway.cite.api.ErrorResponse;
import co.dontlookaway.cite.api.auth.SessionManager;
import co.dontlookaway.cite.api.group.GroupManager;
import co.dontlookaway.cite.api.group.GroupsObserver;
import co.dontlookaway.cite.models.Group;

public class GroupOverviewActivity extends CustomActivity implements GroupsObserver {

    @Override
    public void onResume() {
        super.onResume();
        GroupManager.getInstance().subscribe(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_overview);
        GroupManager.getInstance().subscribe(this);
        GroupManager.getInstance().updateGroups(this);
    }

    private void hideProgressBar() {
        ProgressBar progressBar = findViewById(R.id.groupOverviewProgressBar);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onGroupsUpdated(Group[] groups) {
        hideProgressBar();
    }

    @Override
    public void onError(ErrorResponse errorResponse) {
        hideProgressBar();
        Toast toast = Toast.makeText(this, "Something went wrong while updating groups", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void navigateToNewGroup(View v) {
        GroupManager.getInstance().unsubscribe(this);
        Intent intent = new Intent(GroupOverviewActivity.this, NewGroupActivity.class);
        startActivity(intent);
    }
}