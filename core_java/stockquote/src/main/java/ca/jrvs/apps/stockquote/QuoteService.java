package ca.jrvs.apps.stockquote;

import java.net.http.HttpClient;
import java.util.Optional;

import okhttp3.OkHttpClient;

public class QuoteService {
    private QuoteDao dao;
    private QuoteHttpHelper httpHelper;

    public QuoteService(QuoteDao qRepo, QuoteHttpHelper rcon) {
        this.dao = qRepo;
        this.httpHelper = rcon;
    }

    /**
	 * Fetches latest quote data from endpoint
	 * @param ticker
	 * @return Latest quote information or empty optional if ticker symbol not found
	 */
	public Optional<Quote> fetchQuoteDataFromAPI(String ticker) {
        try {
            Quote quote = httpHelper.fetchQuoteInfo(ticker);
            System.out.println(quote);
            if (quote.getSymbol() == null || quote.getSymbol().isEmpty()) {
                return Optional.empty();
            } else {
                return Optional.of(quote);
            }
        } catch (Exception e) {
            System.out.println("Ticker symbol not found: " + e.getMessage());
            return Optional.empty();
        }
    }
}
