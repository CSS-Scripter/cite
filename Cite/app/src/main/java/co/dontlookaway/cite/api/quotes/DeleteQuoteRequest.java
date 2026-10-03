package co.dontlookaway.cite.api.quotes;

import com.android.volley.Request;

import co.dontlookaway.cite.api.CustomRequest;

public class DeleteQuoteRequest extends CustomRequest<DeleteQuoteResponse> {
    private String quoteId;

    public DeleteQuoteRequest(String quoteId) {
        this.quoteId = quoteId;
    }

    @Override
    public String getPath() {
        return "/quotes/delete";
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }

    @Override
    public Class<DeleteQuoteResponse> getClazz() {
        return DeleteQuoteResponse.class;
    }

    public String getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(String quoteId) {
        this.quoteId = quoteId;
    }
}
