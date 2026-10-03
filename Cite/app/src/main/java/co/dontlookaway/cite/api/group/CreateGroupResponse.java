package co.dontlookaway.cite.api.group;

import co.dontlookaway.cite.api.CustomResponse;
import co.dontlookaway.cite.models.Group;

public class CreateGroupResponse extends CustomResponse {
    private Group group;

    public CreateGroupResponse(Group group) {
        this.group = group;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
