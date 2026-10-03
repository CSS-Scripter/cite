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
import co.dontlookaway.cite.components.adapters.AddGroupPagerAdapter;

public class AddGroupFragmentCollection extends CustomFragment {
    private AddGroupPagerAdapter pagerAdapter;
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_group, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.pagerAdapter = new AddGroupPagerAdapter(getChildFragmentManager());
        this.viewPager = view.findViewById(R.id.pager);
        this.viewPager.setAdapter(this.pagerAdapter);

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(this.viewPager);
    }
}
