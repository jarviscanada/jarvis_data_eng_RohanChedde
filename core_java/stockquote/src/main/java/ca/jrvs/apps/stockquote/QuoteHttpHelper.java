package ca.jrvs.apps.stockquote;
import ca.jrvs.apps.stockquote.JsonParser;
import ca.jrvs.apps.stockquote.StockQuote;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class QuoteHttpHelper {

    private final String apiKey;
    private final HttpClient client;

    public QuoteHttpHelper(String apiKey) {
        this.apiKey = apiKey;
        this.client = HttpClient.newHttpClient();
    }

    public StockQuote fetchQuoteInfo(String symbol) throws IllegalArgumentException {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(
                            "https://alpha-vantage.p.rapidapi.com/query?function=GLOBAL_QUOTE&symbol="
                                    + symbol + "&datatype=json"))
                    .header("X-RapidAPI-Key", apiKey)
                    .header("X-RapidAPI-Host", "alpha-vantage.p.rapidapi.com")
                    .GET()
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("HTTP error: " + response.statusCode());
            }

            String body = response.body();

            // Parse the "Global Quote" node into StockQuote using JsonParser
            String globalQuoteJson = JsonParser.toObjectFromJson(body, com.fasterxml.jackson.databind.JsonNode.class)
                    .get("Global Quote").toString();

            StockQuote quote = JsonParser.toObjectFromJson(globalQuoteJson, StockQuote.class);

            // Validate response
            if (quote.getSymbol() == null || quote.getSymbol().isEmpty()) {
                throw new IllegalArgumentException("Invalid ticker symbol: " + symbol);
            }

            return quote;

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch quote info: " + e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        String apiKey = "ebe9112052msh012e03c973d45f5p18bce4jsn5dd9f3d22fe4";
        QuoteHttpHelper helper = new QuoteHttpHelper(apiKey);

        try {
            StockQuote quote = helper.fetchQuoteInfo("MSFT");
            System.out.println(JsonParser.toJson(quote, true, false));
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}