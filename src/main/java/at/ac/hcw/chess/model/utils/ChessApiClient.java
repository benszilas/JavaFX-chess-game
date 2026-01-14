package at.ac.hcw.chess.model.utils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletionException;

public class ChessApiClient {

    private static final String baseUrl = "https://chess-api.com/v1";

    private final int depth;

    private String text;
    private String promotion;
    private String move;

    public ChessApiClient(int depth) {
        if (depth < 1 || depth > 10) {
            System.out.println("Invalid depth! Setting do default depth of 10.");
            this.depth = 10;
        } else this.depth = depth;
    }

    /**
     * <a href=https://openjdk.org/groups/net/httpclient/intro.html>used resource</a><br>
     * <a href=https://chess-api.com/>API docs</a><br>
     * sends the position to the chess api<br>
     * parses the response move
     *
     * @param fen the FEN notation of chess position
     */
    public void request(String fen) throws CompletionException, CancellationException, HttpTimeoutException {
        resetAttributes();
        String requestBody = """
                {
                    "fen": "%s",
                    "depth": %d
                }""".formatted(fen, depth);

        System.out.println("attempt to send request " + requestBody);
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .timeout(Duration.ofSeconds(10))
                    .build();
            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() / 100 != 2) {
                System.err.println("HTTP error: " + response.statusCode());
            }

            parseResponse(response.body());
        } catch (HttpTimeoutException e) {
            System.err.println("timeout after " + e);
            throw e;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void resetAttributes() {
        text = "";
        move = "";
        promotion = "";
    }

    private void parseResponse(String json) {
        this.text = extractValue(json, "text");
        this.move = extractValue(json, "move");

        String promotionValue = extractRawValue(json, "promotion");
        this.promotion = promotionValue;
    }

    private String extractValue(String json, String key) {
        String search = "\"" + key + "\":";
        int start = json.indexOf(search);
        if (start == -1) return null;

        start = json.indexOf("\"", start + search.length()) + 1;
        int end = json.indexOf("\"", start);
        return json.substring(start, end);
    }

    // Handles boolean OR string for "promotion"
    private String extractRawValue(String json, String key) {
        String search = "\"" + key + "\":";
        int start = json.indexOf(search);
        if (start == -1) return null;

        start += search.length();
        int end = json.indexOf(",", start);
        if (end == -1) {
            end = json.indexOf("}", start);
        }

        return json.substring(start, end).trim().replace("\"", "");
    }

    public String getText() {
        return text;
    }

    public String getPromotion() {
        return promotion;
    }

    public String getMove() {
        return move;
    }
}
