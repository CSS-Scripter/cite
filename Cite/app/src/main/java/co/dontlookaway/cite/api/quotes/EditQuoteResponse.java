package co.dontlookaway.cite.api.quotes;

import co.dontlookaway.cite.api.CustomResponse;
import co.dontlookaway.cite.models.Quote;

public class EditQuoteResponse extends CustomResponse {
    private Quote quote;

    public EditQuoteResponse(Quote quote) {
        this.quote = quote;
    }

    public Quote getQuote() {
        return quote;
    }

    public void setQuote(Quote quote) {
        this.quote = quote;
    }
}
