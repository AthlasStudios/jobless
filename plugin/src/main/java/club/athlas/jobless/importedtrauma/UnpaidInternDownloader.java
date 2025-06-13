package club.athlas.jobless.importedtrauma;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UnpaidInternDownloader {

    private static final ExecutorService executors = Executors.newSingleThreadExecutor();

    private final OkHttpClient client = new OkHttpClient();
    private final Logger logger;

    public UnpaidInternDownloader(Logger logger) {
        this.logger = logger;
    }

    public CompletableFuture<Set<String>> scrapeJobTrauma(String url) {
        return CompletableFuture.supplyAsync(() -> {
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected response code: " + response.code());
                }

                String body = response.body().string();
                return Set.of(body.split("\n"));
            } catch (IOException e) {
                throw new RuntimeException("Unable to get words", e);
            }

        }, executors);
    }

    public void shutdown() {
        client.dispatcher().cancelAll();
        client.connectionPool().evictAll();
        client.dispatcher().executorService().shutdownNow();

        try {
            if (!client.dispatcher().executorService().awaitTermination(5, java.util.concurrent.TimeUnit.SECONDS)) {
                logger.severe("OkHttp executor did not terminate in time.");
            }
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "OkHttp executor did not terminate in time.", e);
        }

        executors.shutdownNow();

        try {
            if (!executors.awaitTermination(5, java.util.concurrent.TimeUnit.SECONDS)) {
                logger.severe("Custom executor did not terminate in time.");
            }
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Custom executor did not terminate in time.", e);
        }
    }

}
