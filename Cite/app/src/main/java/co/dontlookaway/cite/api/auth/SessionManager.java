package co.dontlookaway.cite.api.auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import co.dontlookaway.cite.GroupOverviewActivity;
import co.dontlookaway.cite.LoginActivity;
import co.dontlookaway.cite.MainActivity;
import co.dontlookaway.cite.RegisterActivity;
import co.dontlookaway.cite.api.CustomResponse;
import co.dontlookaway.cite.api.ErrorResponse;
import co.dontlookaway.cite.api.GsonRequest;
import co.dontlookaway.cite.api.RequestCallback;
import co.dontlookaway.cite.models.Error;
import co.dontlookaway.cite.models.User;
import co.dontlookaway.cite.util.ErrorHelper;

public class SessionManager {

    private static SessionManager instance;

    private final String JWT_PREF_KEY = "jwt";
    private final String SHARED_PREFERENCES_AUTH_KEY = "auth";

    private SharedPreferences sharedPreferences;
    private User user;
    private String jwt;

    private Context toastContext;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (SessionManager.instance == null) {
            SessionManager.instance = new SessionManager();
        }
        return SessionManager.instance;
    }

    public Map<String, String> createAuthorizationHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + jwt);
        return headers;
    }

     public void setContext(Context context) {
         setSharedPreferences(context);
         getJwt();

         RequestCallback<MeResponse> callback = createCallback();
         toastContext = context;
         Request request = me(context, callback);
         RequestQueue queue = Volley.newRequestQueue(context);
         queue.add(request);
     }


    private RequestCallback<MeResponse> createCallback() {
        return new RequestCallback<MeResponse>() {
            @Override
            public void onResponse(MeResponse response) {
                setUser(response.getUser());
                toastContext = null;
                // This codebase is to make it possible to skip the main view. We decided to not use it for now.
//                if(toastContext instanceof MainActivity) {
//                    Intent groupActivity = new Intent(toastContext, GroupOverviewActivity.class);
//                    toastContext.startActivity(groupActivity);
//                    ((Activity) toastContext).finish();
//                }
            }

            @Override
            public void onError(int status, ErrorResponse error) {
                Log.d("er", "error");
                removeJwt();

                if(toastContext instanceof MainActivity || toastContext instanceof LoginActivity || toastContext instanceof RegisterActivity) { return; }
                try {
                    Toast toast = Toast.makeText(toastContext, "You are no longer logged in", Toast.LENGTH_LONG);
                    toast.show();
                } catch(Exception e) {
//                    e.printStackTrace();
                }
                Intent loginScreen = new Intent(toastContext, MainActivity.class);
                toastContext.startActivity(loginScreen);
                ((Activity) toastContext).finish();
                toastContext = null;
            }
        };
    }


    private RequestCallback<SignoutResponse> createSignoutCallback() {
        return new RequestCallback<SignoutResponse>() {
            @Override
            public void onResponse(SignoutResponse response) {
                Log.d("Signout", "Revoked jwt");
            }

            @Override
            public void onError(int status, ErrorResponse error) {
                Log.d("Signout Error", "error");
            }
        };
    }

    public GsonRequest<SignoutResponse> signout(Context context, RequestCallback<SignoutResponse> callback) {
        SignoutRequest signoutRequest = new SignoutRequest();
        return new GsonRequest<>(
                signoutRequest,
                response -> onSignout(context, response, callback),
                error -> new ErrorHelper<SignoutResponse>().handleVolleyError(error, callback));
    }

    private void onSignout(Context context, SignoutResponse response, RequestCallback<SignoutResponse> callback) {
        setSharedPreferences(context);
        setUser(null);
        callback.onResponse(response);
    }


    public GsonRequest<MeResponse> me(Context context, RequestCallback<MeResponse> callback) {
        MeRequest meRequest = new MeRequest();
        return new GsonRequest<>(
                meRequest,
                response -> onMe(context, response, callback),
                error -> new ErrorHelper<MeResponse>().handleVolleyError(error, callback));
    }

    private void onMe(Context context, MeResponse response, RequestCallback<MeResponse> callback) {
        setSharedPreferences(context);
        setUser(response.getUser());
        callback.onResponse(response);
    }

    public GsonRequest<LoginResponse> login(Context context, String email, String password, RequestCallback<LoginResponse> callback) {
        LoginRequest loginRequest = new LoginRequest(email, password);
        return new GsonRequest<>(
                loginRequest,
                response -> onLogin(context, response, callback),
                error -> new ErrorHelper<LoginResponse>().handleVolleyError(error, callback));
    }

    public GsonRequest<RegisterResponse> register(Context context, String email, String username, String password, RequestCallback<RegisterResponse> callback) {
        RegisterRequest registerRequest = new RegisterRequest(email, username, password);
        return new GsonRequest<>(
                registerRequest,
                response -> onRegister(context, response, callback),
                error -> new ErrorHelper<RegisterResponse>().handleVolleyError(error, callback));
    }

    private void onLogin(Context context, LoginResponse response, RequestCallback<LoginResponse> callback) {
        setSharedPreferences(context);
        setUser(response.getUser());
        setJwt(response.getJwt());
        callback.onResponse(response);
    }

    private void onRegister(Context context, RegisterResponse response, RequestCallback<RegisterResponse> callback) {
        setSharedPreferences(context);
        setUser(response.getUser());
        setJwt(response.getJwt());
        callback.onResponse(response);
    }

    private void setSharedPreferences(Context context) {
        this.sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_AUTH_KEY, Context.MODE_PRIVATE);
    }

    private void setUser(User user) {
        this.user = user;
        if ((this.user != null) && (this.user.getFavoriteGroups() == null)) {
            this.user.setFavoriteGroups(new String[]{});
        }
    }

    private void setJwt(String jwt) {
        this.jwt = jwt;
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(JWT_PREF_KEY, jwt);
        edit.apply();
    }

    private void getJwt() {
        String jwt = sharedPreferences.getString(JWT_PREF_KEY, null);
        this.jwt = jwt;
    }
    private void removeJwt() {
        this.jwt = "";
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.remove(JWT_PREF_KEY);
        edit.apply();

    }

    public void logOut(Context context) {
        RequestCallback<SignoutResponse> signoutCallback = createSignoutCallback();
        Request request = signout(context, signoutCallback);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
        removeJwt();
        setUser(null);
    }
    public User getUser() {
        return this.user;
    }

    public void updateCurrentUser(Context context, RequestCallback<CurrentUserResponse> callback) {
        this.sharedPreferences = context.getSharedPreferences(this.SHARED_PREFERENCES_AUTH_KEY, Context.MODE_PRIVATE);
        readJwtFromSharedPreferences();
        if (!jwt.equals("")) {
            GsonRequest<CurrentUserResponse> request = createCurrentUserRequest(context, callback);
            RequestQueue queue = Volley.newRequestQueue(context);
            queue.add(request);
        }
    }

    private void readJwtFromSharedPreferences() {
        this.jwt = this.sharedPreferences.getString(JWT_PREF_KEY, "");
    }

    private GsonRequest<CurrentUserResponse> createCurrentUserRequest(Context context, RequestCallback<CurrentUserResponse> callback) {
        CurrentUserRequest request = new CurrentUserRequest();
        return new GsonRequest<>(
                request,
                response -> onCurrentUserRetrieved(response, callback),
                error -> onCurrentUserError(error, callback));
    }

    private void onCurrentUserRetrieved(CurrentUserResponse response, RequestCallback<CurrentUserResponse> callback) {
        setUser(response.getCurrentUser());
        callback.onResponse(response);
    }

    private void onCurrentUserError(VolleyError error, RequestCallback<CurrentUserResponse> callback) {
        this.removeJwt();
        this.user = null;
        new ErrorHelper<CurrentUserResponse>().handleVolleyError(error, callback);
    }
}
