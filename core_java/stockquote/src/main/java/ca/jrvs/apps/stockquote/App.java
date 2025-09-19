package ca.jrvs.apps.stockquote;

import java.math.BigDecimal;
import java.security.Provider.Service;
import java.util.List;
import java.util.Optional;

import ca.jrvs.apps.stockquote.QuoteDao;
import ca.jrvs.apps.stockquote.Quote;

public class App {
    public static void main(String[] args) {
        QuoteDao quoteDao = new QuoteDao(null);
        Iterable<Quote> quotes = quoteDao.findAll();
        System.out.println("**** Quotes ****");
        System.out.println("\n*** Get_ALL ***");
        quotes.forEach(System.out::println);

    }
}
