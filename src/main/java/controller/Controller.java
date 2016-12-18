package controller;

import commons.Request;
import commons.Response;
import enums.RequestType;

import java.io.IOException;
import java.util.Map;

/**
 * Created by YG-MAC on 2016. 12. 18..
 */
public abstract class Controller {
    private RequestType requestType;
    private Map<String, Class> parameters;

    public Controller(RequestType requestType, Map<String, Class> parameters) {
        this.requestType = requestType;
        this.parameters = parameters;
    }

    public abstract void action(Request request, Response response) throws IOException;
}
