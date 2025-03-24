package quiz;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OnlineStoreWebApplication {
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ExecutorService executorService = Executors.newFixedThreadPool(10); // Thread pool to handle multiple requests

    public static void main(String[] args) throws IOException {
        OnlineStoreWebApplication app = new OnlineStoreWebApplication();
        app.startHttpServer();
    }

    /** Starts an HTTP Server listening on port 8080 **/
    private void startHttpServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) {
                executorService.submit(() -> handleHttpRequest(exchange)); // Handle request asynchronously
            }
        });
        server.start();
        System.out.println("Server started on port 8080...");
    }

    /** Handles an incoming HTTP request from a user **/
    private void handleHttpRequest(HttpExchange httpExchange) {
        try {
            int numberOfProducts = parseRequest(httpExchange);
            URI requestURI = URI.create(String.format("http://best-online-store.com/products?number-of-products=%d", numberOfProducts));

            HttpResponse<String> response = httpClient.send(HttpRequest.newBuilder()
                    .GET()
                    .uri(requestURI)
                    .build(), HttpResponse.BodyHandlers.ofString());

            sendWebpageToUser(httpExchange, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Parses the number of products from the request **/
    private int parseRequest(HttpExchange httpExchange) {
        String query = httpExchange.getRequestURI().getQuery();
        if (query != null && query.contains("number-of-products=")) {
            return Integer.parseInt(query.split("=")[1]);
        }
        return 1; // Default to 1 product if no parameter is provided
    }

    /** Sends a web page response to the user **/
    private void sendWebpageToUser(HttpExchange httpExchange, HttpResponse<String> response) throws IOException {
        String responseBody = "<html><body><h1>Products:</h1><p>" + response.body() + "</p></body></html>";
        httpExchange.sendResponseHeaders(200, responseBody.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(responseBody.getBytes());
        os.close();
    }
}
