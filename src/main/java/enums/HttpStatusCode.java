package enums;

/**
 * Created by YG-MAC on 2016. 12. 18..
 */
public enum HttpStatusCode {
    OK("200 OK"),
    NOT_FOUND("404 NOT FOUND"),
    FOUND("302 FOUND");


    private String message;

    HttpStatusCode(String s) {
        this.message = s;
    }

    public String getMessage() {
        return message;
    }
}
