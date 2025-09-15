package ca.jrvs.apps.stockquote;

import ca.jrvs.apps.stockquote.dto.StockQuote;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class API {

    public static void main(String[] args) {
        String symbol = "MSFT";
        String apiKey = "ebe9112052msh012e03c973d45f5p18bce4jsn5dd9f3d22fe4";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        "https://alpha-vantage.p.rapidapi.com/query?function=GLOBAL_QUOTE&symbol=" 
                        + symbol + "&datatype=json"))
                .header("X-RapidAPI-Key", apiKey)
                .header("X-RapidAPI-Host", "alpha-vantage.p.rapidapi.com")
                .GET()
                .build();

        try {
            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Raw JSON Response:");
            System.out.println(response.body());

            // Parse JSON using your JsonParser
            String globalQuoteJson = JsonParser.toObjectFromJson(response.body(), com.fasterxml.jackson.databind.JsonNode.class)
                    .get("Global Quote").toString();

            StockQuote quote = JsonParser.toObjectFromJson(globalQuoteJson, StockQuote.class);

            System.out.println("\nParsed StockQuote:");
            System.out.println("Symbol: " + quote.getSymbol());
            System.out.println("Price: " + quote.getPrice());
            System.out.println("Change: " + quote.getChange());
            System.out.println("Change Percent: " + quote.getChangePercent());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
