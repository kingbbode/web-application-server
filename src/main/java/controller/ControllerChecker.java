package controller;

import commons.Request;
import commons.Response;
import enums.RequestType;
import model.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by YG-MAC on 2016. 12. 15..
 */
public class ControllerChecker {
    private Map<RequestType, Map<String, Controller>> controllers;

    public ControllerChecker() {
        this.controllers = new HashMap<>();
        Map<String, Controller> get = new HashMap<>();
        Map<String, Controller> post = new HashMap<>();

        //GET
        Map<String, Class> getParam = new HashMap<>();
        get.put("/user/list", new UserService(RequestType.GET, getParam));

        //POST
        Map<String, Class> postParam = new HashMap<>();
        postParam.put("user", User.class);
        post.put("/user/create", new JoinService(RequestType.POST, postParam));

        Map<String, Class> postParam2 = new HashMap<>();
        postParam2.put("userId", String.class);
        postParam2.put("password", String.class);
        post.put("/user/login", new LoginService(RequestType.POST, postParam2));

        //Controllers
        controllers.put(RequestType.GET, get);
        controllers.put(RequestType.POST, post);
    }

    public boolean matches(Request request) {
        if(!checkRegistered(request)){
            return false;
        }
        return checkParams(request);
        //타입체크까지는 도저히..
    }
    private boolean checkParams(Request request){
        return this.controllers.get(request.getType()).get(request.getUrl().getPath()).checkParams(request.getParameters());
    }

    private boolean checkRegistered(Request request) {
        return controllers.containsKey(request.getType())
                && controllers.get(request.getType()).containsKey(request.getUrl().getPath());
    }

    public void execute(Request request, Response response) throws IOException {
        controllers.get(request.getType()).get(request.getUrl().getPath()).action(request, response);
    }
}
