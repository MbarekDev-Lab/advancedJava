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
    private static final int PORT = 8080;
    private final HttpClient httpClient;

    public OnlineStoreWebApplication() {
        // Use Virtual Threads for massive concurrency
        ExecutorService virtualThreadExecutor = Executors.newVirtualThreadPerTaskExecutor();

        // Create a non-blocking HTTP client that uses Virtual Threads
        httpClient = HttpClient.newBuilder()
                .executor(virtualThreadExecutor)
                .build();
    }

    public void startHttpServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/", this::handleHttpRequest);

        // Assign Virtual Threads to the server
        server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());

        server.start();
        System.out.println("🚀 Server started on port " + PORT);
    }

    private void handleHttpRequest(HttpExchange exchange) {
        try {
            int numberOfProducts = parseRequest(exchange);
            URI requestURI = URI.create(String.format("http://best-online-store/products?number-of-products=%d", numberOfProducts));

            // Asynchronous non-blocking request using Virtual Threads
            httpClient.sendAsync(
                            HttpRequest.newBuilder()
                                    .GET()
                                    .uri(requestURI)
                                    .build(),
                            HttpResponse.BodyHandlers.ofString()
                    ).thenAccept(response -> sendWebpageToUser(exchange, response))
                    .exceptionally(ex -> {
                        sendErrorResponse(exchange, "❌ Failed to fetch products");
                        return null;
                    });

        } catch (Exception e) {
            sendErrorResponse(exchange, "❌ Invalid request format");
        }
    }

    private int parseRequest(HttpExchange exchange) {
        String query = exchange.getRequestURI().getQuery();
        if (query == null || !query.contains("number-of-products=")) {
            throw new IllegalArgumentException("Missing number-of-products parameter");
        }
        return Integer.parseInt(query.split("=")[1]);
    }

    private void sendWebpageToUser(HttpExchange exchange, HttpResponse<String> response) {
        sendResponse(exchange, 200, response.body());
    }

    private void sendErrorResponse(HttpExchange exchange, String message) {
        sendResponse(exchange, 500, message);
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String message) {
        try {
            byte[] responseBytes = message.getBytes();
            exchange.sendResponseHeaders(statusCode, responseBytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            new OnlineStoreWebApplication().startHttpServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
