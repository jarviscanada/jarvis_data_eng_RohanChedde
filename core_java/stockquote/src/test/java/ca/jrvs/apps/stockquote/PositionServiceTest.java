package ca.jrvs.apps.stockquote;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PositionServiceTest {

    @Test
    public void testBuyWithSufficientVolume() {
        QuoteDao mockQuoteDao = mock(QuoteDao.class);
        PositionDao mockPositionDao = mock(PositionDao.class);
        Quote quote = new Quote();
        quote.setSymbol("MSFT");
        quote.setVolume(100);
        quote.setPrice(100.0);
        when(mockQuoteDao.findById("MSFT")).thenReturn(java.util.Optional.of(quote));
        PositionService service = new PositionService(mockPositionDao, mockQuoteDao);
        Position position = service.buy("MSFT", 1, 100.0);
        assertNotNull(position);
        assertEquals("MSFT", position.getTicker());
        assertEquals(1, position.getNumOfShares());
        assertEquals(100.0, position.getValuePaid());
    }

    @Test
    public void testBuyWithInsufficientVolume() {
        QuoteDao mockQuoteDao = mock(QuoteDao.class);
        PositionDao mockPositionDao = mock(PositionDao.class);
        Quote quote = new Quote();
        quote.setSymbol("MSFT");
        quote.setVolume(1000000000);
        quote.setPrice(100.0);
        when(mockQuoteDao.findById("MSFT")).thenReturn(java.util.Optional.of(quote));
        PositionService service = new PositionService(mockPositionDao, mockQuoteDao);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.buy("MSFT", 1000000000, 100.0);
        });
        assertTrue(exception.getMessage().contains("insufficient volume"));
    }

    @Test
    public void testBuyWithInvalidTicker() {
        QuoteDao mockQuoteDao = mock(QuoteDao.class);
        PositionDao mockPositionDao = mock(PositionDao.class);
        when(mockQuoteDao.findById("INVALID")).thenReturn(java.util.Optional.empty());
        PositionService service = new PositionService(mockPositionDao, mockQuoteDao);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.buy("INVALID", 1, 100.0);
        });
        assertTrue(exception.getMessage().contains("invalid ticker") || exception.getMessage().contains("insufficient volume"));
    }
}
