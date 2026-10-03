package co.dontlookaway.cite.api.quotes;

import com.android.volley.Request;

import co.dontlookaway.cite.api.CustomRequest;

public class EditQuoteRequest extends CustomRequest<EditQuoteResponse> {
    private String quoteId;
    private String newQuoteText;
    private String newQuotee;

    public EditQuoteRequest(String quoteId, String quote, String quotee) {
        this.quoteId = quoteId;
        this.newQuoteText = quote;
        this.newQuotee = quotee;
    }

    @Override
    public String getPath() {
        return "/quotes/edit";
    }

    @Override
    public int getMethod() {
        return Request.Method.POST;
    }

    @Override
    public Class<EditQuoteResponse> getClazz() {
        return EditQuoteResponse.class;
    }

    public String getNewQuoteText() {
        return newQuoteText;
    }

    public String getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(String quoteId) {
        this.quoteId = quoteId;
    }

    public void setNewQuoteText(String newQuoteText) {
        this.newQuoteText = newQuoteText;
    }

    public String getNewQuotee() {
        return newQuotee;
    }

    public void setNewQuotee(String newQuotee) {
        this.newQuotee = newQuotee;
    }
}
