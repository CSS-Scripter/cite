package co.dontlookaway.cite.api.quotes;

import co.dontlookaway.cite.api.CustomResponse;
import co.dontlookaway.cite.models.Quote;

public class LikeQuoteResponse extends CustomResponse {
    private Quote quote;

    public LikeQuoteResponse(Quote quote) {
        this.quote = quote;
    }

    public Quote getQuote() {
        return quote;
    }

    public void setQuote(Quote quote) {
        this.quote = quote;
    }
}
