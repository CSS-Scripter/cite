package co.dontlookaway.cite.api.group;

import co.dontlookaway.cite.api.CustomResponse;
import co.dontlookaway.cite.models.Group;

public class GetAllGroupsResponse extends CustomResponse {
    private Group[] groups;

    public GetAllGroupsResponse(Group[] groups) {
        this.groups = groups;
    }

    public Group[] getGroups() {
        return groups;
    }

    public void setGroups(Group[] groups) {
        this.groups = groups;
    }
}
