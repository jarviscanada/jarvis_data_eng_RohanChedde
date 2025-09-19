package ca.jrvs.apps.stockquote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Position {
	private final Logger logger = LoggerFactory.getLogger(Position.class);
	private String ticker; //id
	private int numOfShares;
	private double valuePaid; //total amount paid for shares
	
	public String getTicker() {
		return ticker;
	}
	public void setTicker(String ticker) {
		this.ticker = ticker;
	}
	public int getNumOfShares() {
		return numOfShares;
	}
	public void setNumOfShares(int numOfShares) {
		this.numOfShares = numOfShares;
		logger.info("Number of shares set to {}", numOfShares);

	}
	public double getValuePaid() {
		return valuePaid;
	}
	public void setValuePaid(double valuePaid) {
		this.valuePaid = valuePaid;
	}

	@Override
	public String toString() {
		return "Position [ticker=" + ticker + ", numOfShares=" + numOfShares + ", valuePaid=" + valuePaid + "]";
	}

}
