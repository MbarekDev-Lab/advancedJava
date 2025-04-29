package performence.optimizingForLatency;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.*;

public class ScheduledApiRateLimiter {
    private static final int RATE_LIMIT = 3;  // 3 requests per second
    private static final int TOTAL_REQUESTS = 10;

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(RATE_LIMIT);
    private static final HttpClient httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();

    public static void main(String[] args) {
        for (int i = 1; i <= TOTAL_REQUESTS; i++) {
            final int requestId = i;
            scheduler.schedule(() -> makeApiCall(requestId), i / RATE_LIMIT, TimeUnit.SECONDS);
        }

        scheduler.shutdown();
    }

    private static void makeApiCall(int requestId) {
        System.out.println("Sending API request: " + requestId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/todos/" + requestId))
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response " + requestId + ": " + response.statusCode());
        } catch (Exception e) {
            System.err.println("Request " + requestId + " failed.");
        }
    }
}

