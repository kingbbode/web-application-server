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

    public boolean checkParams(Map<String, String> map){
        for(Class clazz : parameters.values()){
            if(isCustomClass(clazz)){

            }
        }


        for (Map.Entry<String, Class> entry : parameters.entrySet()) {
            if (!map.containsKey(entry.getKey()) || typeChecker(entry.getValue(), map.get(entry.getKey()))) {
                return false;
            }
        }
        return true;

    }

    private boolean typeChecker(Class clazz, Object object){
        return false;
    }

    //귀찬으니 그냥.
    private boolean isCustomClass(Class clazz){
        return clazz.equals(String.class) || clazz.equals(Integer.class);
    }
}
