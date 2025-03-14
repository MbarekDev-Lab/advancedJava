package performence.optimizingForThroughput;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ThroughputHttpServer {
    private static final String INPUT_FILE = "D:/Code/javaCode/java-se-programming/R_multithreading/resources/war_and_peace.txt";
    private static final int NUMBER_OF_THREADS = 8;

    public static void main(String[] args) {
        try {
            // Ensure file exists before reading
            if (!Files.exists(Paths.get(INPUT_FILE))) {
                System.err.println("Error: File not found at " + INPUT_FILE);
                return;
            }
           // String text = new String(Files.readAllBytes(Paths.get(INPUT_FILE)));

            // Read file content safely
            String text = Files.readString(Paths.get(INPUT_FILE), StandardCharsets.UTF_8);
            startServer(text);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    public static void startServer(String text) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/search", new WordCountHandler(text));
        Executor executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        server.setExecutor(executor);
        server.start();
        System.out.println("Server started on http://localhost:8000/search?word=example");
    }

    private static class WordCountHandler implements HttpHandler {
        private final String text;

        public WordCountHandler(String text) {
            this.text = text;
        }

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String query = httpExchange.getRequestURI().getQuery();

            if (query == null || !query.startsWith("word=")) {
                sendResponse(httpExchange, 400, "Invalid query. Use: ?word=example");
                return;
            }

            String word = query.substring(5);
            long count = countWord(word);

            sendResponse(httpExchange, 200, Long.toString(count));
        }

        private long countWord(String word) {
            long count = 0;
            int index = 0;
            while ((index = text.indexOf(word, index)) != -1) {
                count++;
                index++;
            }
            return count;
        }

        private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
            byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(statusCode, responseBytes.length);
            try (OutputStream outputStream = exchange.getResponseBody()) {
                outputStream.write(responseBytes);
            }
        }
    }
}