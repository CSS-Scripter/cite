package co.dontlookaway.cite.api;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Map;

import co.dontlookaway.cite.api.auth.SessionManager;
import co.dontlookaway.cite.util.Config;

public class GsonRequest<T extends CustomResponse> extends Request<T> {
    private final Gson gson = new Gson();
    private final Class<T> clazz;
    private final Map<String, String> headers;
    private final Response.Listener<T> listener;
    private final JsonObject dataIn;

    public GsonRequest(String path, CustomRequest<T> request, Class<T> clazz, Map<String, String> headers, int method,
                       Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, Config.API_BASE_URL + path, errorListener);
        this.dataIn = request.createParameters();
        this.clazz = clazz;
        this.headers = headers;
        this.listener = listener;
    }

    public GsonRequest(CustomRequest<T> request, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(request.getMethod(), Config.API_BASE_URL + request.getPath(), errorListener);
        this.dataIn = request.createParameters();
        this.clazz = request.getClazz();
        this.headers = SessionManager.getInstance().createAuthorizationHeaders();
        this.listener = listener;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    public String getBodyContentType() {
        return "application/json; charset=utf-8";
    }

    @Override
    public byte[] getBody() {
        // THOM I FIXED IT YES
        // ISSUE WAS DAT HIJ HET NIET GOED OMZETTE
        // GUCCI GUCCI GUCCI GANG
        // hallo docent, bedankt voor het staren naar de code, ik ben hier oprecht trots op
        // daarom laten we deze mooie comments staan
        return dataIn.toString().getBytes();
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(
                    gson.fromJson(json, clazz),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch ( UnsupportedEncodingException | JsonSyntaxException e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }
}
