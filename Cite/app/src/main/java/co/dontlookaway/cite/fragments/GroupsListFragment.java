package co.dontlookaway.cite.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import co.dontlookaway.cite.components.adapters.GroupAdapter;
import co.dontlookaway.cite.models.Group;
import co.dontlookaway.cite.util.Logger;

public class GroupsListFragment extends CustomFragment {
    private Group[] groups;
    private int recyclerViewId;
    private int layoutId;

    public GroupsListFragment(int recyclerViewId, int layoutId) {
        this.recyclerViewId = recyclerViewId;
        this.layoutId = layoutId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(layoutId, container, false);
    }

    public void setGroups(Group[] groups) {
        this.groups = groups;
        updateRecyclerView();
    }

    private void updateRecyclerView() {
        try {
            GroupAdapter groupsAdapter = new GroupAdapter(activity, this.groups);
            RecyclerView groupRecyclerView = activity.findViewById(this.recyclerViewId);
            groupRecyclerView.setAdapter(groupsAdapter);
            groupRecyclerView.setLayoutManager(new GridLayoutManager(activity, 2));

        } catch (Exception e) {
            Logger logger = new Logger<>(GroupsListFragment.class);
            logger.info(e.getMessage());
        }
    }

}
