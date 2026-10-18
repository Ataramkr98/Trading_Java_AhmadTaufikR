package com.ahmadtrportofolio.pulse;

import com.ahmadtrportofolio.pulse.copy.CopyService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PulseApplicationTests {

	@Test
	void copySettingsValidationMatchesFrontendRules() {
		assertEquals("Max loss should be at least $50", CopyService.validateSettings(
				new CopyService.CopySettings("PROPORTIONAL", null, new BigDecimal("49"), new BigDecimal("0.5"))));
		assertEquals("Max position size should be at least 0.01 lot", CopyService.validateSettings(
				new CopyService.CopySettings("PROPORTIONAL", null, new BigDecimal("100"), new BigDecimal("0.001"))));
		assertEquals("Fixed amount should be at least $25", CopyService.validateSettings(
				new CopyService.CopySettings("FIXED", new BigDecimal("24"), new BigDecimal("100"), new BigDecimal("0.5"))));
		assertNull(CopyService.validateSettings(
				new CopyService.CopySettings("FIXED", new BigDecimal("25"), new BigDecimal("100"), new BigDecimal("0.5"))));
	}
}
