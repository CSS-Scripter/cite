package co.dontlookaway.cite.api.quotes;

import co.dontlookaway.cite.api.GsonRequest;
import co.dontlookaway.cite.api.RequestCallback;
import co.dontlookaway.cite.models.Group;
import co.dontlookaway.cite.util.ErrorHelper;

public class QuoteManager {

    public QuoteManager() { }

    /**
     * Fetch all quotes from a group
     * @param group group
     * @param callback Request callback
     * @return GSON request
     */
    public GsonRequest<GetAllQuotesResponse> getQuotes(Group group, RequestCallback<GetAllQuotesResponse> callback) {
        return new GsonRequest<>(
                new GetAllQuotesRequest(group),
                callback::onResponse,
                error -> new ErrorHelper<GetAllQuotesResponse>().handleVolleyError(error, callback));
    }


    /**
     * Save a quote
     * @param quote quote text
     * @param quotee person getting quoted
     * @param group groupId
     * @param callback Request callback
     * @return GSON request
     */
    public GsonRequest<CreateQuoteResponse> createQuoteRequest(String quote, String quotee, String group, RequestCallback<CreateQuoteResponse> callback) {
        CreateQuoteRequest createQuoteRequest = new CreateQuoteRequest(quote, quotee, group);
        return new GsonRequest<>(
                createQuoteRequest,
                callback::onResponse,
                error -> new ErrorHelper<CreateQuoteResponse>().handleVolleyError(error, callback));
    }

    /**
     * Edit a quote
     * @param quoteId quote id
     * @param quote quote text
     * @param quotee person getting quoted
     * @param callback Request callback
     * @return GSON request
     */
    public static GsonRequest<EditQuoteResponse> editQuoteRequest(String quoteId, String quote, String quotee, RequestCallback<EditQuoteResponse> callback) {
        EditQuoteRequest editQuoteRequest = new EditQuoteRequest(quoteId, quote, quotee);
        return new GsonRequest<>(
                editQuoteRequest,
                callback::onResponse,
                error -> new ErrorHelper<EditQuoteResponse>().handleVolleyError(error, callback));
    }

    /**
     * Delete a quote
     * @param quoteId quote id
     * @param callback Request callback
     * @return GSON request
     */
    public static GsonRequest<DeleteQuoteResponse> deleteQuoteRequest(String quoteId, RequestCallback<DeleteQuoteResponse> callback) {
        DeleteQuoteRequest deleteQuoteRequest = new DeleteQuoteRequest(quoteId);
        return new GsonRequest<>(
                deleteQuoteRequest,
                callback::onResponse,
                error -> new ErrorHelper<DeleteQuoteResponse>().handleVolleyError(error, callback));
    }

    public static GsonRequest<LikeQuoteResponse> likeQuoteRequest(String quoteId, RequestCallback<LikeQuoteResponse> callback) {
        LikeQuoteRequest likeQuoteRequest = new LikeQuoteRequest(quoteId);
        return new GsonRequest<>(
                likeQuoteRequest,
                callback::onResponse,
                error -> new ErrorHelper<LikeQuoteResponse>().handleVolleyError(error, callback));
    }
}
