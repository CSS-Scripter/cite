package co.dontlookaway.cite.api.quotes;

import co.dontlookaway.cite.api.CustomResponse;
import co.dontlookaway.cite.models.Quote;

public class GetAllQuotesResponse extends CustomResponse {
    private Quote[] quotes;

    public GetAllQuotesResponse(Quote[] quotes) {
        this.quotes = quotes;
    }

    public Quote[] getQuotes() {
        return quotes;
    }

    public void setQuotes(Quote[] quotes) {
        this.quotes = quotes;
    }
}
