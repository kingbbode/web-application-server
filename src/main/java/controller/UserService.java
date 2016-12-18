package controller;

import commons.Request;
import commons.Response;
import enums.RequestType;
import util.PathUtils;

import java.io.IOException;
import java.util.Map;

/**
 * Created by YG-MAC on 2016. 12. 18..
 */
public class UserService extends Controller {


    public UserService(RequestType requestType, Map<String, Class> parameters) {
        super(requestType, parameters);
    }

    @Override
    public void action(Request request, Response response) throws IOException {
        if(!Boolean.parseBoolean(request.getHeaders().get("logined"))){
            response.redirect(PathUtils.LOGIN);
            return;
        }
        response.forward(request.getUrl().getPath());
    }
}
