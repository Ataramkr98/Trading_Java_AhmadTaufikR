import { describe, expect, it } from "vitest";
import { calculateOrderPreview, formatPrice, formatSignedMoney, sortWatchlist, validateCopySettings } from "./logic";
import { quotes } from "./mockData";

describe("pulse trading logic", () => {
  it("formats market prices with the instrument precision", () => {
    expect(formatPrice(quotes.EURUSD.last, quotes.EURUSD.digits)).toBe("1.08544");
    expect(formatPrice(quotes.XAUUSD.last, quotes.XAUUSD.digits)).toBe("2,368.47");
  });

  it("formats signed money without losing the sign", () => {
    expect(formatSignedMoney(42.8)).toBe("+$42.80");
    expect(formatSignedMoney(-18.2)).toBe("−$18.20");
  });

  it("calculates fee and margin for a valid order", () => {
    const preview = calculateOrderPreview({ symbol: "EURUSD", side: "BUY", orderType: "MARKET", quantity: 0.1 }, quotes.EURUSD, 10172.84);
    expect(preview.isValid).toBe(true);
    expect(preview.estimatedFee).toBe(0.2);
    expect(preview.marginRequired).toBeGreaterThan(0);
  });

  it("rejects an order when free margin is too low", () => {
    const preview = calculateOrderPreview({ symbol: "XAUUSD", side: "BUY", orderType: "MARKET", quantity: 5 }, quotes.XAUUSD, 100);
    expect(preview.isValid).toBe(false);
    expect(preview.error).toBe("Insufficient free margin for this order");
  });

  it("rejects stops placed on the wrong side of the market", () => {
    const preview = calculateOrderPreview({ symbol: "EURUSD", side: "BUY", orderType: "MARKET", quantity: 0.1, stopLoss: 1.09 }, quotes.EURUSD, 10172.84);
    expect(preview.isValid).toBe(false);
    expect(preview.error).toContain("stop loss is on the wrong side");
  });

  it("sorts watchlist rows by market change", () => {
    const sorted = sortWatchlist([{ symbol: "US30", favorite: true }, { symbol: "XAUUSD", favorite: true }, { symbol: "EURUSD", favorite: true }], quotes, "change");
    expect(sorted.map((item) => item.symbol)).toEqual(["XAUUSD", "EURUSD", "US30"]);
  });

  it("enforces safe copy-trading limits", () => {
    expect(validateCopySettings({ mode: "PROPORTIONAL", fixedAmount: 100, maxLoss: 20, maxPositionSize: 0.5 })).toBe("Max loss should be at least $50");
    expect(validateCopySettings({ mode: "FIXED", fixedAmount: 10, maxLoss: 200, maxPositionSize: 0.5 })).toBe("Fixed amount should be at least $25");
    expect(validateCopySettings({ mode: "PROPORTIONAL", fixedAmount: 100, maxLoss: 200, maxPositionSize: 0.5 })).toBeUndefined();
  });
});
