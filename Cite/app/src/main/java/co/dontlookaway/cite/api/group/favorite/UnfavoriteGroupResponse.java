package co.dontlookaway.cite.api.group.favorite;

import co.dontlookaway.cite.api.CustomResponse;

public class UnfavoriteGroupResponse extends CustomResponse {
    private String[] favoriteGroups;

    public String[] getFavoriteGroups() {
        return favoriteGroups;
    }
}
