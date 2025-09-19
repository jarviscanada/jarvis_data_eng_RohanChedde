package ca.jrvs.apps.stockquote;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;
import okhttp3.OkHttpClient;

public class API {

    public static void main(String[] args) throws Exception {
        String symbol = "TSLA";
        String apiKey = "ebe9112052msh012e03c973d45f5p18bce4jsn5dd9f3d22fe4";
        OkHttpClient client = new OkHttpClient();

        // Example properties setup (replace with your actual config)
        Map<String, String> properties = new HashMap<>();
        properties.put("server", "localhost");
        properties.put("port", "5432");
        properties.put("database", "stock_quote");
        properties.put("username", "postgres");
        properties.put("password", "password");

        String url = "jdbc:postgresql://" + properties.get("server") + ":" + properties.get("port") + "/" + properties.get("database");
        try (Connection c = DriverManager.getConnection(url, properties.get("username"), properties.get("password"))) {
            QuoteHttpHelper helper = new QuoteHttpHelper(apiKey, client, c);
            Quote quote = helper.fetchQuoteInfo(symbol);
            System.out.println("Parsed StockQuote:");
            System.out.println(quote);

            QuoteDao quoteDao = new QuoteDao(c);
            quoteDao.save(quote);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
