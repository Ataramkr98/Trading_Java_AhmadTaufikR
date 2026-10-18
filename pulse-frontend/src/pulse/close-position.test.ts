import { describe, expect, it } from "vitest";
import { usePulseStore } from "./store";

describe("close position wiring", () => {
  it("removes closed position from store", () => {
    usePulseStore.getState().setPositions([
      { id: "p1", symbol: "EURUSD", side: "BUY", quantity: 0.1, openPrice: 1, currentPrice: 1, unrealizedPnl: 0, margin: 0, openedAt: "now" },
    ]);
    usePulseStore.getState().removePosition("p1");
    expect(usePulseStore.getState().positions.map((p) => p.id)).not.toContain("p1");
  });
});
