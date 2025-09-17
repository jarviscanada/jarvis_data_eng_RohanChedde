package ca.jrvs.apps.stockquote;

public class PositionService {
	
	private PositionDao dao;
    private static QuoteDao quoteDao;

    public PositionService() {
        this.dao = new PositionDao();
        this.quoteDao = new QuoteDao();
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
        position.setValuePaid(numberOfShares * price);

        if (numberOfShares <= 0 || price <= 0) {
            throw new IllegalArgumentException("Number of shares and price must be positive.");
        }        
        
        if(buy(ticker, numberOfShares) == false) {
            throw new IllegalArgumentException("Buy order cannot be processed due to insufficient volume or invalid ticker.");
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
		dao.deleteById(ticker);
        System.out.println("Sold: " + ticker);
	}

    public boolean buy(String symbol, int sharesToBuy) {
        Quote quote = quoteDao.findById(symbol).orElse(null);
        if (quote == null) {
            // Symbol not found
            return false;
        }
        if (sharesToBuy > quote.getVolume()) {
            // Not enough volume available
            return false;
        }
        // Proceed with buy logic
        return true;
    }

    public static void main(String[] args){
        PositionService service = new PositionService();
        String symbol = "GOOG";
        Quote quote = quoteDao.findById(symbol).orElse(null);
        //service.buy(symbol, 30, Math.round(quote.getPrice()/100.0)*100.0);
        service.sell("GOOG");
    }

}
