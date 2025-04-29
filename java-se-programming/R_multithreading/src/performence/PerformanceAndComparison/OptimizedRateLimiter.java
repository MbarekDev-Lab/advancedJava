package performence.PerformanceAndComparison;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.*;

public class OptimizedRateLimiter {
    private static final int MAX_CONCURRENT_REQUESTS = 3; // Limit to 3 concurrent API calls
    private static final int TOTAL_REQUESTS = 10;         // Total requests

    private static final Semaphore semaphore = new Semaphore(MAX_CONCURRENT_REQUESTS);
    private static final ExecutorService virtualThreadExecutor = Executors.newVirtualThreadPerTaskExecutor();
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    public static void main(String[] args) throws InterruptedException {
        for (int i = 1; i <= TOTAL_REQUESTS; i++) {
            final int requestId = i;
            virtualThreadExecutor.submit(() -> {
                try {
                    semaphore.acquire();  // Wait if limit reached
                    makeApiCall(requestId);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    semaphore.release();  // Free up slot
                }
            });
        }

        virtualThreadExecutor.shutdown();
        virtualThreadExecutor.awaitTermination(1, TimeUnit.MINUTES);
        System.out.println("All API requests completed.");
    }

    private static void makeApiCall(int requestId) {
        System.out.println("Sending API request: " + requestId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://jsonplaceholder.typicode.com/todos/" + requestId)) // Dummy API
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Response for request " + requestId + ": " + response.statusCode());
        } catch (Exception e) {
            System.err.println("Request " + requestId + " failed: " + e.getMessage());
        }

        System.out.println("Completed API request: " + requestId);
    }
}

