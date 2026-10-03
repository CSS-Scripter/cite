package co.dontlookaway.cite.api.quotes;

import com.android.volley.Request;

import co.dontlookaway.cite.api.CustomRequest;
import co.dontlookaway.cite.models.Group;

public class GetAllQuotesRequest extends CustomRequest<GetAllQuotesResponse> {
    private Group group;

    public GetAllQuotesRequest(Group group) {
        this.group = group;
    }

    @Override
    public String getPath() {
        return String.format("/quotes/%s", this.group.getId());
    }

    @Override
    public int getMethod() {
        return Request.Method.GET;
    }

    @Override
    public Class<GetAllQuotesResponse> getClazz() {
        return GetAllQuotesResponse.class;
    }
}
