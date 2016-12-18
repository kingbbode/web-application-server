package webserver;

import commons.Request;
import commons.Response;
import controller.Controller;
import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

public class RequestHandler extends Thread {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final Controller controller = new Controller();
    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8")); OutputStream out = connection.getOutputStream()) {
            log.debug("------------ start generate Request ---------------");
            Request request = new Request(in.readLine(), in);
            log.debug("------------ end generate Request ---------------");

            log.debug("------------ start generate Response ---------------");
            Response response = new Response(request ,out);
            log.debug("------------ end generate Response ---------------");

            if(controller.matches(request)){
                //컨트롤러 구현 안되니까 암것도 못하겠음.
                if("/user/create".equals(request.getUrl().getPath())) {
                    User user = new User(request.getBody());
                    DataBase.addUser(user);
                    log.info(user.toString());
                }else if("/user/login".equals(request.getUrl().getPath())){
                    User user = DataBase.findUserById(request.getBody().get("userId"));
                    if(user == null){
                        //urls[PATH] = login_failed;
                    }
                    if(!user.getPassword().equals(user)){
                        //urls[PATH] = login_failed;
                    }
                }
            }
            response.forword(request.getUrl().getPath());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
