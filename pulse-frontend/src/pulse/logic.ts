import { z } from "zod";
import type { Candle, CopySettings, OrderDraft, OrderPreview, Quote, WatchlistItem } from "./types";

export const orderDraftSchema = z.object({
  symbol: z.string().min(1),
  side: z.enum(["BUY", "SELL"]),
  orderType: z.enum(["MARKET", "LIMIT", "STOP"]),
  quantity: z.number().min(0.01, "Minimum 0.01 lot"),
  stopLoss: z.number().positive().optional(),
  takeProfit: z.number().positive().optional(),
});

export function formatMoney(value: number, currency = "USD") {
  return new Intl.NumberFormat("en-US", { style: "currency", currency, maximumFractionDigits: 2 }).format(value);
}

export function formatPrice(value: number, digits = 2) {
  return value.toLocaleString("en-US", { minimumFractionDigits: digits, maximumFractionDigits: digits });
}

export function formatSignedMoney(value: number) {
  return `${value >= 0 ? "+" : "−"}${formatMoney(Math.abs(value))}`;
}

export function formatSignedPercent(value: number) {
  return `${value >= 0 ? "+" : "−"}${Math.abs(value).toFixed(2)}%`;
}

export function calculateOrderPreview(input: OrderDraft, quote: Quote, availableMargin: number): OrderPreview {
  const marginRequired = quote.last * quote.contractSize * input.quantity * 0.01;
  const estimatedFee = input.quantity * 2;
  const estimatedSwap = input.quantity * 0.12;
  const invalidStopLoss = input.stopLoss !== undefined && (input.side === "BUY" ? input.stopLoss >= quote.last : input.stopLoss <= quote.last);
  const invalidTakeProfit = input.takeProfit !== undefined && (input.side === "BUY" ? input.takeProfit <= quote.last : input.takeProfit >= quote.last);
  const invalidStops = input.stopLoss !== undefined && input.takeProfit !== undefined && (input.side === "BUY" ? input.stopLoss >= input.takeProfit : input.stopLoss <= input.takeProfit);
  const invalidSize = input.quantity < quote.minLot || input.quantity > 100;
  const error = invalidSize ? `Lot size must be between ${quote.minLot.toFixed(2)} and 100.00` : invalidStopLoss ? `For ${input.side === "BUY" ? "Buy" : "Sell"}, stop loss is on the wrong side of the market` : invalidTakeProfit ? `For ${input.side === "BUY" ? "Buy" : "Sell"}, take profit is on the wrong side of the market` : invalidStops ? `For ${input.side === "BUY" ? "Buy" : "Sell"}, stop loss and take profit are reversed` : marginRequired > availableMargin ? "Insufficient free margin for this order" : undefined;
  return { marginRequired, estimatedFee, estimatedSwap, availableMargin, isValid: !error, error };
}

export function sortWatchlist(items: WatchlistItem[], quotesBySymbol: Record<string, Quote>, sort: "alphabetical" | "change" | "volume") {
  return [...items].sort((a, b) => {
    if (sort === "alphabetical") return a.symbol.localeCompare(b.symbol);
    if (sort === "change") return (quotesBySymbol[b.symbol]?.changePct ?? 0) - (quotesBySymbol[a.symbol]?.changePct ?? 0);
    return (quotesBySymbol[b.symbol]?.volume ?? "").localeCompare(quotesBySymbol[a.symbol]?.volume ?? "");
  });
}

export function validateCopySettings(settings: CopySettings) {
  if (settings.maxLoss < 50) return "Max loss should be at least $50";
  if (settings.maxPositionSize < 0.01) return "Max position size should be at least 0.01 lot";
  if (settings.mode === "FIXED" && settings.fixedAmount < 25) return "Fixed amount should be at least $25";
  return undefined;
}

export function chartBounds(candles: Candle[]) {
  const values = candles.flatMap((candle) => [candle.high, candle.low]);
  return { min: Math.min(...values), max: Math.max(...values) };
}
