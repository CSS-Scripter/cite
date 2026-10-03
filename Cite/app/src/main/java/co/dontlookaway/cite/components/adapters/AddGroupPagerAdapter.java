package co.dontlookaway.cite.components.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import co.dontlookaway.cite.fragments.CreateGroupFragment;
import co.dontlookaway.cite.fragments.JoinGroupFragment;

public class AddGroupPagerAdapter extends FragmentStatePagerAdapter {
    private List<PageModel> pages;

    public AddGroupPagerAdapter(@NonNull FragmentManager fm) {
        super(fm, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        setupPages();
    }

    private void setupPages() {
        this.pages = new ArrayList<>();
        this.pages.add(new PageModel("Create", new CreateGroupFragment()));
        this.pages.add(new PageModel("Join", new JoinGroupFragment()));
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return this.pages.get(position).getFragment();
    }

    @Override
    public int getCount() {
        return this.pages.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return this.pages.get(position).getTitle();
    }
}
