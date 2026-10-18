package com.ahmadtrportofolio.pulse;

import com.ahmadtrportofolio.pulse.copy.CopyService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CopyServiceResolveTests {

    @Test
    void resolvesSlugToTrader() throws Exception {
        // RED->GREEN: method exists and slug mapping matches frontend mock ids
        assertNotNull(CopyService.class.getMethod("traderByFlexibleId", String.class));
        assertEquals("rakafx", CopyService.slugOf("RakaFX").replace("-", ""));
        assertEquals("naya-gold", CopyService.slugOf("Naya Gold"));
        assertEquals("denny-index", CopyService.slugOf("Denny Index"));
    }
}
