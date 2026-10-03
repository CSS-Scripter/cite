package co.dontlookaway.cite.models;

public class Quote {
    private String[] likes;
    private String quote;
    private String quotee;
    private String group;
    private User quoter;
    private String __v;
    private String id;

    public Quote(String[] likes, String quote, String quotee, String group, User quoter, String __v, String id) {
        this.likes = likes;
        this.quote = quote;
        this.quotee = quotee;
        this.group = group;
        this.quoter = quoter;
        this.__v = __v;
        this.id = id;
    }

    public String[] getLikes() {
        return likes;
    }

    public void setLikes(String[] likes) {
        this.likes = likes;
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

    public User getQuoter() {
        return quoter;
    }

    public void setQuoter(User quoter) {
        this.quoter = quoter;
    }

    public String get__v() {
        return __v;
    }

    public void set__v(String __v) {
        this.__v = __v;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
