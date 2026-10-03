package co.dontlookaway.cite.models;

public class Error {
    private String message;
    private String field;

    public Error(String message) {
        this(message, null);
    }

    public Error(String message, String field) {
        this.message = message;
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

}
