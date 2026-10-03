package co.dontlookaway.cite.api.group;

import co.dontlookaway.cite.api.ErrorResponse;
import co.dontlookaway.cite.models.Group;

public interface GroupsObserver {

    public void onGroupsUpdated(Group[] groups);
    public void onError(ErrorResponse errorResponse);
}
