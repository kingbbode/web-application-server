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
public class JoinService extends Controller {


    public JoinService(RequestType requestType, Map<String, Class> parameters) {
        super(requestType, parameters);
    }

    @Override
    public void action(Request request, Response response) {
        DataBase.addUser(new User(request.getBody()));
        response.redirect("localhost:9000/index.html");
    }
}
