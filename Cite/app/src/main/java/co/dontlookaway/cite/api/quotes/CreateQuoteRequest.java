package co.dontlookaway.cite.api.quotes;

import com.android.volley.Request;

import co.dontlookaway.cite.api.CustomRequest;

public class CreateQuoteRequest extends CustomRequest<CreateQuoteResponse> {
    private String quote;
    private String quotee;
    private String group;

    public CreateQuoteRequest(String quote, String quotee, String group) {
        this.quote = quote;
        this.quotee = quotee;
        this.group = group;
    }

    @Override
    public String getPath() {
        return "/quotes/quote";
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }

    @Override
    public Class<CreateQuoteResponse> getClazz() {
        return CreateQuoteResponse.class;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getQuotee() {
        return quotee;
    }

    public void setQuotee(String quotee) {
        this.quotee = quotee;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
