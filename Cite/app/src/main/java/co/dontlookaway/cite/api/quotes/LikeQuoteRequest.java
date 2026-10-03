package co.dontlookaway.cite.api.quotes;

import com.android.volley.Request;

import co.dontlookaway.cite.api.CustomRequest;

public class LikeQuoteRequest extends CustomRequest<LikeQuoteResponse> {
    private String quoteId;

    public LikeQuoteRequest(String quoteId) {
        this.quoteId = quoteId;
    }

    @Override
    public String getPath() {
        return "/quotes/like";
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }

    @Override
    public Class<LikeQuoteResponse> getClazz() {
        return LikeQuoteResponse.class;
    }

    public String getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(String quoteId) {
        this.quoteId = quoteId;
    }
}
