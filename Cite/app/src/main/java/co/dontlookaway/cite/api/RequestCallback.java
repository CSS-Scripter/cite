package co.dontlookaway.cite.api;

public abstract class RequestCallback<T extends CustomResponse> {
    public abstract void onResponse(T response);
    public abstract void onError(int statusCode, ErrorResponse errorResponse);
}
