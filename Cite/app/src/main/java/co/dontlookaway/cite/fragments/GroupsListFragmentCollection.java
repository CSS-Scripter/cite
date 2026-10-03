package co.dontlookaway.cite.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import co.dontlookaway.cite.R;
import co.dontlookaway.cite.api.group.GroupManager;
import co.dontlookaway.cite.components.adapters.GroupsListPagerAdapter;

public class GroupsListFragmentCollection extends CustomFragment {
    private GroupsListPagerAdapter pagerAdapter;
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_groups_list_collection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.pagerAdapter = new GroupsListPagerAdapter(getChildFragmentManager());
        this.viewPager = view.findViewById(R.id.pager);
        this.viewPager.setAdapter(this.pagerAdapter);

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(this.viewPager);
    }

    @Override
    public void onDestroyView() {
        GroupManager.getInstance().unsubscribe(pagerAdapter);
        super.onDestroyView();
    }
}
