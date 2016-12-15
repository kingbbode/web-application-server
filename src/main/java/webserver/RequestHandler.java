package webserver;

import controller.Controller;
import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.*;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Map;

public class RequestHandler extends Thread {
    private static final int PATH = 0;
    private static final int PARAMETER = 1;

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final String root = "webapp";
    private static final String not_found = "/404.html";
    private static final String index = "/index.html";
    private static final String login_failed = "/user/login_failed";
    private Socket connection;
    private Controller controller;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        this.controller = new Controller();
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader din = new BufferedReader(new InputStreamReader(in));
            Map<String, String> header = RequestHeaderUtils.readHeader(din);
            String status = "200";
            String[] urls = UrlPatternUtils.parse(header.get("url"));
            if("GET".equals(header.get("type")) && controller.matchGet(urls[PATH])){
                Map<String, String> params = HttpRequestUtils.parseQueryString(urls[PARAMETER]);
                if(!ControllerUtils.isValidParameters(params, controller.getParameters(urls[PATH]))){
                    User user = new User(params);
                    DataBase.addUser(user);
                    log.info(user.toString());
                }
                urls[PATH] = index;
                status = "302";
            }else if("POST".equals(header.get("type")) && controller.matchPost(urls[PATH])){
                String body = IOUtils.readData(din, Integer.parseInt(header.get("length")));
                Map<String, String> params = HttpRequestUtils.parseQueryString(body);
                if("/user/create".equals(urls[PATH])) {
                    User user = new User(params);
                    DataBase.addUser(user);
                    log.info(user.toString());
                    urls[PATH] = index;
                    status = "302";
                }else if("/user/login".equals(urls[PATH])){
                    User user = DataBase.findUserById(params.get("userId"));
                    if(user == null){
                        urls[PATH] = login_failed;
                    }
                    if(!user.getPassword().equals(user)){
                        urls[PATH] = login_failed;
                    }
                }
            }

            DataOutputStream dos = new DataOutputStream(out);
            File file = new File(root + UrlPatternUtils.resolve(urls[PATH]));
            if(!file.isFile()){
                file = new File(root + not_found);
            }
            byte[] body = Files.readAllBytes(file.toPath());
            responseHeader(dos, body.length, header.get("accept"), status);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseHeader(DataOutputStream dos, int lengthOfBodyContent, String contentType, String status) {
        try {
            dos.writeBytes("HTTP/1.1 " + status + " OK \r\n");
            dos.writeBytes("Content-Type: " + contentType +";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
