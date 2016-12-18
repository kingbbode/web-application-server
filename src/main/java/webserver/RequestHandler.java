package webserver;

import commons.Request;
import commons.Response;
import controller.ControllerChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class RequestHandler extends Thread {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final ControllerChecker controller = new ControllerChecker();
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

            if(!controller.matches(request)){
                response.forward(request.getUrl().getPath(), null);
                return;
            }
            controller.execute(request, response);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
