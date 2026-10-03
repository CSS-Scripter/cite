package co.dontlookaway.cite.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public abstract class CustomRequest<T extends CustomResponse> {
    /**
     * Create parameters for a request
     * @return JSON Object
     */
    public JsonObject createParameters() {
        Gson gson = new Gson();
        String jsonString = gson.toJson(this);
        return gson.fromJson(jsonString, JsonObject.class);
    }

    public abstract String getPath();

    public abstract int getMethod();

    public abstract Class<T> getClazz();
}
