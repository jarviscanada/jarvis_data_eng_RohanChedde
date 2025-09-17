package ca.jrvs.apps.stockquote;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PositionServiceTest {

    @Test
    public void testBuyWithSufficientVolume() {
        PositionService service = new PositionService();
        String symbol = "GOOG";
        int sharesToBuy = 1;
        double price = 100.0;
        Position position = service.buy(symbol, sharesToBuy, price);
        assertNotNull(position);
        assertEquals(symbol, position.getTicker());
        assertEquals(sharesToBuy, position.getNumOfShares());
        assertEquals(sharesToBuy * price, position.getValuePaid());
    }

    @Test
    public void testBuyWithInsufficientVolume() {
        PositionService service = new PositionService();
        String symbol = "GOOG";
        int sharesToBuy = 1000000000;
        double price = 100.0;
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.buy(symbol, sharesToBuy, price);
        });
        assertTrue(exception.getMessage().contains("insufficient volume"));
    }

    @Test
    public void testBuyWithInvalidTicker() {
        PositionService service = new PositionService();
        String symbol = "INVALID";
        int sharesToBuy = 1;
        double price = 100.0;
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.buy(symbol, sharesToBuy, price);
        });
        assertTrue(exception.getMessage().contains("invalid ticker"));
    }
}
