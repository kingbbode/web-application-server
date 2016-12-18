package controller;

import commons.Request;
import enums.RequestType;
import model.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by YG-MAC on 2016. 12. 15..
 */
public class Controller {
    private Map<RequestType, Map<String, Map<String, Class>>> controllers;

    public Controller() {
        this.controllers = new HashMap<>();
        Map<String, Map<String, Class>> get = new HashMap<>();
        Map<String, Map<String, Class>> post = new HashMap<>();

        //GET
        Map<String, Class> getParam = new HashMap<>();
        getParam.put("userId", String.class);
        getParam.put("password", String.class);
        getParam.put("name", String.class);
        getParam.put("email", String.class);
        get.put("/user/create", getParam);

        //POST
        Map<String, Class> postParam = new HashMap<>();
        postParam.put("user", User.class);
        post.put("/user/create", postParam);

        Map<String, Class> postParam2 = new HashMap<>();
        postParam2.put("userId", String.class);
        postParam2.put("password", String.class);
        post.put("/user/login", postParam2);

        //Controllers
        controllers.put(RequestType.GET, get);
        controllers.put(RequestType.POST, post);
    }

    public boolean matches(Request request){
        return checkRegistered(request);
        //타입체크까지는 도저히..
    }

    private boolean checkRegistered(Request request){
        return controllers.containsKey(request.getType())
                && controllers.get(request.getType()).containsKey(request.getUrl().getPath());
    }
}
