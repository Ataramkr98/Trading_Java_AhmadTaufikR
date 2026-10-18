package com.ahmadtrportofolio.pulse;

import com.ahmadtrportofolio.pulse.trading.TradingService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderTypeValidationTests {

    @Test
    void rejectsUnknownSide() {
        assertEquals("Side must be BUY or SELL", TradingService.validateSide("HOLD"));
    }

    @Test
    void acceptsKnownSides() {
        assertNull(TradingService.validateSide("BUY"));
        assertNull(TradingService.validateSide("SELL"));
    }

    @Test
    void rejectsUnknownOrderType() {
        assertEquals("Order type must be MARKET, LIMIT, or STOP", TradingService.validateOrderType("FOK"));
    }

    @Test
    void classifiesPendingTypes() {
        assertTrue(TradingService.isPendingType("LIMIT"));
        assertTrue(TradingService.isPendingType("STOP"));
    }
}
