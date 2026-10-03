package co.dontlookaway.cite.api;

import co.dontlookaway.cite.models.Error;

public class ErrorResponse {
    Error[] errors;

    public ErrorResponse(Error[] errors) {
        this.errors = errors;
    }

    public Error[] getErrors() {
        return errors;
    }

    public void setErrors(Error[] errors) {
        this.errors = errors;
    }
}
