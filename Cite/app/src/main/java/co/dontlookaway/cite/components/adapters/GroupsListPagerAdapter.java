package co.dontlookaway.cite.components.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import co.dontlookaway.cite.api.ErrorResponse;
import co.dontlookaway.cite.api.auth.SessionManager;
import co.dontlookaway.cite.api.group.GroupManager;
import co.dontlookaway.cite.api.group.GroupsObserver;
import co.dontlookaway.cite.fragments.AllGroupsListFragment;
import co.dontlookaway.cite.fragments.FavoriteGroupsListFragment;
import co.dontlookaway.cite.fragments.GroupsListFragment;
import co.dontlookaway.cite.models.Group;
import co.dontlookaway.cite.util.Logger;

public class GroupsListPagerAdapter extends FragmentStatePagerAdapter implements GroupsObserver {
    private final Logger<GroupsListPagerAdapter> logger = new Logger<>(GroupsListPagerAdapter.class);
    private List<PageModel> pages;
    private PageModel favoritesPage;
    private PageModel allPage;

    public GroupsListPagerAdapter(@NonNull FragmentManager fm) {
        super(fm, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        setupPages();
        subscribeToGroupManager();
    }

    private void setupPages() {
        this.pages = new ArrayList<>();
        favoritesPage = new PageModel("Favorites", new FavoriteGroupsListFragment());
        allPage = new PageModel("All", new AllGroupsListFragment());
        this.pages.add(favoritesPage);
        this.pages.add(allPage);
    }

    private void subscribeToGroupManager() {
        GroupManager.getInstance().subscribe(this);
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

    @Override
    public void onGroupsUpdated(Group[] groups) {
        Group[] favoriteGroups = GroupManager.getInstance().getFavoriteGroups();
        ((GroupsListFragment) favoritesPage.getFragment()).setGroups(favoriteGroups);
        ((GroupsListFragment) allPage.getFragment()).setGroups(groups);

        logger.debug("Updated groups!");
    }

    @Override
    public void onError(ErrorResponse errorResponse) { }
}
