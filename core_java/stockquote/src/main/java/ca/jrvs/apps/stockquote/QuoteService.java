package ca.jrvs.apps.stockquote;

import java.util.Optional;

public class QuoteService {
	
	private QuoteDao dao;
	private QuoteHttpHelper httpHelper;

	/**
	 * Fetches latest quote data from endpoint
	 * @param ticker
	 * @return Latest quote information or empty optional if ticker symbol not found
	 */
	public Optional<Quote> fetchQuoteDataFromAPI(String ticker) {
        String symbol = ticker;
        String apiKey = "ebe9112052msh012e03c973d45f5p18bce4jsn5dd9f3d22fe4";

        QuoteHttpHelper helper = new QuoteHttpHelper(apiKey);
        try {
            Quote quote = helper.fetchQuoteInfo(symbol);
            System.out.println(quote);
            if (quote.getSymbol() == null || quote.getSymbol().isEmpty()) {
                // Symbol does not exist in API response
                return Optional.empty();
            } else {
                // Symbol exists
                return Optional.of(quote);
            }

        } catch (Exception e) {
            System.out.println("Ticker symbol not found: " + e.getMessage());
            return Optional.empty();
        }
        
	}

    public static void main(String[] args){
        QuoteService service = new QuoteService();
        service.fetchQuoteDataFromAPI("GOOG");
    }

}
