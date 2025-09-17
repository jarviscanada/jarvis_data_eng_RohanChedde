package ca.jrvs.apps.stockquote;

public class API {

    public static void main(String[] args) {
        String symbol = "TSLA";
        String apiKey = "";

        QuoteHttpHelper helper = new QuoteHttpHelper(apiKey);
        try {
            Quote quote = helper.fetchQuoteInfo(symbol);
            System.out.println("Parsed StockQuote:");
            System.out.println(quote);

            QuoteDao quoteDao = new QuoteDao();
            quoteDao.save(quote);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
