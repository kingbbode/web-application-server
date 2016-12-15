package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.*;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RequestHandler extends Thread {
    private static final int PATH = 0;
    private static final int PARAMETER = 1;

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final String root = "webapp";
    private static final String not_found = "/404.html";
    private static final String index = "/index.html";
    private Socket connection;
    private Map<String, Set<String>> get;
    private Set<String> post;

    private Map<String, User> user;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        this.get = new HashMap<>();
        this.post = new HashSet<>();

        //GET
        Set<String> parameter = new HashSet<>();
        parameter.add("userId");
        parameter.add("password");
        parameter.add("name");
        parameter.add("email");
        this.get.put("/user/create", parameter);

        //POST
        post.add("/user/create");
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader din = new BufferedReader(new InputStreamReader(in));
            Map<String, String> header = RequestHeaderUtils.readHeader(din);
            String status = "200";
            String[] urls = UrlPatternUtils.parse(header.get("url"));
            if("GET".equals(header.get("type")) && get.containsKey(urls[PATH])){
                Map<String, String> params = HttpRequestUtils.parseQueryString(urls[PARAMETER]);
                if(!ControllerUtils.isValidParameters(params, get.get(urls[PATH]))){
                    DataBase.addUser(new User(params));
                    log.info(user.toString());
                }
                urls[PATH] = index;
                status = "302";
            }else if("POST".equals(header.get("type")) && post.contains(urls[PATH])){
                String body = IOUtils.readData(din, Integer.parseInt(header.get("length")));
                Map<String, String> params = HttpRequestUtils.parseQueryString(body);
                DataBase.addUser(new User(params));
                urls[PATH] = index;
                status = "302";
                log.info(user.toString());
            }else if("POST".equals(header.get("type"))){
                urls[PATH] = not_found;
            }

            DataOutputStream dos = new DataOutputStream(out);
            File file = new File(root + UrlPatternUtils.resolve(header.get("url")));
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
