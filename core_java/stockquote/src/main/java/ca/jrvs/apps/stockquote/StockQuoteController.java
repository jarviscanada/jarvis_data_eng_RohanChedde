package ca.jrvs.apps.stockquote;

import java.util.Optional;
import java.util.Scanner;

public class StockQuoteController {

	private QuoteService quoteService;
	private PositionService positionService;

	public StockQuoteController(QuoteService sQuote, PositionService sPos) {
        this.quoteService = sQuote;
        this.positionService = sPos;
    }

    /**
	 * User interface for our application
	 */
	public void initClient() {
		System.out.println("Welcome to the Stock Quote Application!");
        System.out.println("Available commands:");
        System.out.println("1. getQuote <symbol>");
        System.out.println("2. buy <symbol> <quantity> <price>");
        System.out.println("3. sell <symbol>");
        System.out.println("4. exit");
        
        Scanner scanner = new Scanner(System.in);
        String command;
        while (true) {
            System.out.print("Enter command: ");
            command = scanner.nextLine();
            String[] tokens = command.split(" ");
            String action = tokens[0];

            try {
                switch (action) {
                    case "getQuote":
                        if (tokens.length != 2) {
                            System.out.println("Usage: getQuote <symbol>");
                            break;
                        }
                        String symbol = tokens[1];
                        Optional<Quote> quote = quoteService.fetchQuoteDataFromAPI(symbol);
                        //System.out.println(quote);
                        break;

                    case "buy":
                        if (tokens.length != 4) {
                            System.out.println("Usage: buy <symbol> <quantity> <price>");
                            break;
                        }
                        symbol = tokens[1];
                        int quantity = Integer.parseInt(tokens[2]);
                        double price = Double.parseDouble(tokens[3]);
                        positionService.buy(symbol, quantity, price);
                        break;

                    case "sell":
                        if (tokens.length != 2) {
                            System.out.println("Usage: sell <symbol>");
                            break;
                        }
                        symbol = tokens[1];
                        positionService.sell(symbol);
                        break;

                    case "exit":
                        System.out.println("Exiting application.");
                        scanner.close();
                        return;

                    default:
                        System.out.println("Unknown command. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

	}

}
