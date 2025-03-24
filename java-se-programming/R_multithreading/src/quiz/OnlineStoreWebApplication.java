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
import com.sun.net.httpserver.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.*;
import java.util.concurrent.*;

public class OnlineStoreWebApplication {
    private static final int PORT = 8080;
    private static final int THREAD_POOL_SIZE = 8;
    private final HttpClient httpClient;
    private final ExecutorService executorService;

    public OnlineStoreWebApplication() {
        // Create a fixed thread pool for handling HTTP requests
        executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        // Create a non-blocking HTTP client that shares the same executor
        httpClient = HttpClient.newBuilder()
                .executor(executorService)
                .build();
    }

    public void startHttpServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        HttpContext context = server.createContext("/");
        context.setHandler(this::handleHttpRequest);

        server.setExecutor(executorService); // Assign the executor to the server
        server.start();
        System.out.println("Server started on port " + PORT);
    }

    private void handleHttpRequest(HttpExchange exchange) {
        int numberOfProducts;
        try {
            numberOfProducts = parseRequest(exchange);
        } catch (Exception e) {
            sendErrorResponse(exchange, "Invalid request format");
            return;
        }

        URI requestURI = URI.create(String.format("http://best-online-store/products?number-of-products=%d", numberOfProducts));

        // Asynchronous HTTP request to the Products Application
        CompletableFuture<HttpResponse<String>> responseFuture = httpClient.sendAsync(
                HttpRequest.newBuilder()
                        .GET()
                        .uri(requestURI)
                        .build(),
                HttpResponse.BodyHandlers.ofString()
        );

        // When response is received, send it back to the user
        responseFuture.thenAccept(response -> sendWebpageToUser(exchange, response))
                .exceptionally(ex -> {
                    sendErrorResponse(exchange, "Failed to fetch products");
                    return null;
                });
    }

    private int parseRequest(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        if (query == null || !query.contains("number-of-products=")) {
            throw new IllegalArgumentException("Missing number-of-products parameter");
        }
        return Integer.parseInt(query.split("=")[1]);
    }

    private void sendWebpageToUser(HttpExchange exchange, HttpResponse<String> response) {
        try {
            byte[] responseBytes = response.body().getBytes();
            exchange.sendResponseHeaders(200, responseBytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendErrorResponse(HttpExchange exchange, String message) {
        try {
            byte[] responseBytes = message.getBytes();
            exchange.sendResponseHeaders(500, responseBytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        executorService.shutdown();
    }

    public static void main(String[] args) {
        try {
            new OnlineStoreWebApplication().startHttpServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
