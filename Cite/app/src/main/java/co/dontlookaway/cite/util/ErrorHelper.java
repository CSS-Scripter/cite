package co.dontlookaway.cite.util;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import co.dontlookaway.cite.CustomActivity;
import co.dontlookaway.cite.api.CustomResponse;
import co.dontlookaway.cite.api.ErrorResponse;
import co.dontlookaway.cite.api.RequestCallback;

import co.dontlookaway.cite.api.auth.SessionManager;
import co.dontlookaway.cite.models.Error;

public class ErrorHelper<T extends CustomResponse> {
    private final Logger<ErrorHelper> logger;

    public ErrorHelper() {
        this.logger = new Logger<>(ErrorHelper.class);
    }

    /**
     * Handle volley (request) errors
     * @param volleyError error object
     * @param callback request callback
     */
    public void handleVolleyError(VolleyError volleyError, RequestCallback<T> callback) {
        if (volleyError.networkResponse == null) {
            String message = "";
            if (volleyError instanceof NetworkError) {
                message = "Cannot connect to Internet...Please check your connection!";
            } else if (volleyError instanceof ServerError) {
                message = "The server could not be found. Please try again after some time!!";
            } else if (volleyError instanceof AuthFailureError) {
                message = "Cannot connect to Internet...Please check your connection!";
            } else if (volleyError instanceof ParseError) {
                message = "Parsing error! Please try again after some time!!";
            } else if (volleyError instanceof NoConnectionError) {
                message = "Cannot connect to Internet...Please check your connection!";
            } else if (volleyError instanceof TimeoutError) {
                message = "Connection TimeOut! Please check your internet connection.";
            }

            if(!message.isEmpty()) {
                Error[] es = new Error[]{new Error(message)};

                ErrorResponse errorResponse =  new ErrorResponse(es);

                callback.onError(500, errorResponse);
                return;
            }

            Log.d("Error", message);
            handleUnknownError(volleyError);
            return;
        }

        logError(volleyError);
        int statusCode = volleyError.networkResponse.statusCode;
        ErrorResponse errorResponse = parseVolleyErrorToErrorResponse(volleyError);
        callback.onError(statusCode, errorResponse);
    }

    private void handleUnknownError(VolleyError error) {
        this.logger.wtf("Something went wrong and we don't have a single clue WTF happened");
        this.logger.wtf(new Gson().toJson(error));
    }

    private void logError(VolleyError error) {
        String message = new String(error.networkResponse.data);
        this.logger.error(message);
        error.printStackTrace();
    }

    private ErrorResponse parseVolleyErrorToErrorResponse(VolleyError volleyError) {
        try {

            String errorData = new String(volleyError.networkResponse.data);
            return new Gson().fromJson(errorData, ErrorResponse.class);
        } catch (Exception e) {

            e.printStackTrace();

            Error[] es = new Error[]{new Error("Unknown network exception")};

            return new ErrorResponse(es);

        }
    }
}
