package ca.jrvs.apps.stockquote;
import ca.jrvs.apps.stockquote.JsonParser;
import ca.jrvs.apps.stockquote.Quote;
import okhttp3.OkHttpClient;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;

public class QuoteHttpHelper {

    private final String apiKey;
    private final HttpClient client;
    private final Connection c; // Add this field

    public QuoteHttpHelper(String apiKey, OkHttpClient okClient, Connection c) {
        this.apiKey = apiKey;
        this.client = HttpClient.newHttpClient();
        this.c = c; // Store the connection
    }

    public Quote fetchQuoteInfo(String symbol) throws IllegalArgumentException {
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

            Quote quote = JsonParser.toObjectFromJson(globalQuoteJson, Quote.class);
            Quote newQuote = new Quote();
            QuoteDao quoteDao = new QuoteDao(c); // Use the valid connection
            newQuote.setSymbol(quote.getSymbol());
            newQuote.setOpen(quote.getOpen());
            newQuote.setHigh(quote.getHigh());
            newQuote.setLow(quote.getLow());
            newQuote.setPrice(quote.getPrice());
            newQuote.setVolume(quote.getVolume());
            newQuote.setLatestTradingDay(quote.getLatestTradingDay());
            newQuote.setPreviousClose(quote.getPreviousClose());
            newQuote.setChange(quote.getChange());
            newQuote.setChangePercent(quote.getChangePercent());
            newQuote.setTimestamp(quote.getTimestamp());
            
            newQuote = quoteDao.save(newQuote);
            // Validate response
            if (quote.getSymbol() == null || quote.getSymbol().isEmpty()) {
                throw new IllegalArgumentException("Invalid ticker symbol: " + symbol);
            }

            return newQuote;

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch quote info: " + e.getMessage(), e);
        }
    }

}