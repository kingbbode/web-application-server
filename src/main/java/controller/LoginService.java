package controller;

import commons.Request;
import commons.Response;
import db.DataBase;
import enums.RequestType;
import model.User;

import java.util.Map;

/**
 * Created by YG-MAC on 2016. 12. 18..
 */
public class LoginService extends Controller {

    public LoginService(RequestType requestType, Map<String, Class> parameters) {
        super(requestType, parameters);
    }

    @Override
    public void action(Request request, Response response) {
        String des = "http://localhost:9000/user/login_failed.html";
        User user = DataBase.findUserById(getParameters(request).get("userId"));

        if (user != null && user.matchPassword(getParameters(request).get("password"))) {
            request.getHeaders().put("Set-Cookie", "logined=true");
            des = "http://localhost:9000/index.html";
        }

        response.redirect(des);
    }
}
