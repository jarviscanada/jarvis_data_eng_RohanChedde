package ca.jrvs.apps.stockquote;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PositionService_IntTest {

    @Test
    public void testBuyWithSufficientVolume() {
        QuoteDao mockQuoteDao = mock(QuoteDao.class);
        PositionDao mockPositionDao = mock(PositionDao.class);
        Quote quote = new Quote();
        
        quote.setSymbol("GOOG");
        quote.setVolume(100);
        quote.setPrice(100.0);

        when(mockQuoteDao.findById("GOOG")).thenReturn(java.util.Optional.of(quote));

        PositionService service = new PositionService(mockPositionDao, mockQuoteDao);
        Position position = service.buy("GOOG", 10, 100.0);

        assertNotNull(position);
        assertEquals("GOOG", position.getTicker());
        assertEquals(10, position.getNumOfShares());
    }

    @Test
    // Add more tests for insufficient volume, invalid ticker, etc.
    public void testBuyWithInsufficientVolume() {
        QuoteDao mockQuoteDao = mock(QuoteDao.class);
        PositionDao mockPositionDao = mock(PositionDao.class);
        Quote quote = new Quote();
        quote.setSymbol("MELI");
        quote.setVolume(1000000); // Insufficient volume
        quote.setPrice(100.0);

        when(mockQuoteDao.findById("MELI")).thenReturn(java.util.Optional.of(quote));

        PositionService service = new PositionService(mockPositionDao, mockQuoteDao);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.buy("MELI", 1000000, 100.0);
        });

        String expectedMessage = "insufficient volume";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
