package controller;

import commons.Request;
import commons.Response;
import db.DataBase;
import enums.RequestType;
import model.User;
import util.HttpRequestUtils;
import util.PathUtils;

import java.io.IOException;
import java.util.Collection;
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
        if (!isLogin(request.getHeaders().get("Cookie"))) {
            response.redirect(PathUtils.LOGIN);
            return;
        }
        Collection<User> users = DataBase.findAll();
        StringBuilder sb = new StringBuilder();
        int count = 1;
        for (User user : users) {
            sb.append("<tr>");
            sb.append("<th scope='row'>" +count + "</th>");
            sb.append("<td>" + user.getUserId() + "</td>");
            sb.append("<td>" + user.getName() + "</td>");
            sb.append("<td>" + user.getEmail() + "</td>");
            sb.append("</tr>");
            count++;
        }

        response.forward(request.getUrl().getPath() + ".html", before -> new String(before).replace("${user}", sb.toString()).getBytes());
    }

    private boolean isLogin(String cookieValue) {
        Map<String, String> cookies = HttpRequestUtils.parseCookies(cookieValue);
        String value = cookies.get("logined");
        if (value == null) {
            return false;
        }
        return Boolean.parseBoolean(value);
    }
}
