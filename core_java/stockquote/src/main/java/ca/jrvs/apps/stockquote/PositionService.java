package ca.jrvs.apps.stockquote;

public class PositionService {
	
	private PositionDao dao;
    private static QuoteDao quoteDao;

    public PositionService(PositionDao pRepo, QuoteDao qRepo) {
        this.dao = pRepo;
        PositionService.quoteDao = qRepo;
    }


	/**
	 * Processes a buy order and updates the database accordingly
	 * @param ticker
	 * @param numberOfShares
	 * @param price
	 * @return The position in our database after processing the buy
	 */
	public Position buy(String ticker, int numberOfShares, double price) {
        Position position = new Position();
        position.setTicker(ticker);
        position.setNumOfShares(numberOfShares);
        Quote quote = quoteDao.findById(ticker).orElse(null);
        if (quote == null) {
            throw new IllegalArgumentException("Buy order cannot be processed due to invalid ticker.");
        }
        price = quote.getPrice();
        position.setValuePaid(numberOfShares * price);

        if (numberOfShares <= 0 || price <= 0) {
            throw new IllegalArgumentException("Number of shares and price must be positive.");
        }

        if(buy(ticker, numberOfShares) == false) {
            throw new IllegalArgumentException("Buy order cannot be processed due to insufficient volume or invalid ticker. (insufficient volume)");
        }
        dao.save(position);
        System.out.println("Bought Position:" + position);
        return position;
	}

	/**
	 * Sells all shares of the given ticker symbol
	 * @param ticker
	 */
    public void sell(String ticker) {
        Position position = dao.findById(ticker).orElse(null);
        Quote quote = quoteDao.findById(ticker).orElse(null);
        if (position == null || quote == null) {
            System.out.println("Cannot calculate profit: msissing position or quote for " + ticker);
        } else {
            double profit = (quote.getPrice() * position.getNumOfShares()) - position.getValuePaid();
            System.out.println("Profit for " + ticker + ": " + profit);
        }
        dao.deleteById(ticker);
        System.out.println("Sold: " + ticker);
    }
	

    public boolean buy(String symbol, int sharesToBuy) {
        Quote quote = quoteDao.findById(symbol).orElse(null);
        if (quote == null) {
            // Symbol not found
            return false;
        }
        if (sharesToBuy >= quote.getVolume()) {
            // Not enough volume available
            return false;
        }
        // Proceed with buy logic
        return true;
    }

}
