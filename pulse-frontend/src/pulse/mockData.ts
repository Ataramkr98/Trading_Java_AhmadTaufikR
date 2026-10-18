import type { Candle, Course, Position, Quote, SymbolInfo, Trader } from "./types";

const quoteBase = {
  EURUSD: { name: "Euro / US Dollar", category: "FOREX" as const, digits: 5, minLot: 0.01, contractSize: 100000, bid: 1.08542, ask: 1.08545, last: 1.08544, change: 0.00012, changePct: 0.011, spread: 0.3, volume: "254.8M" },
  GBPUSD: { name: "British Pound / US Dollar", category: "FOREX" as const, digits: 5, minLot: 0.01, contractSize: 100000, bid: 1.26884, ask: 1.26890, last: 1.26887, change: -0.00308, changePct: -0.242, spread: 0.6, volume: "181.2M" },
  USDJPY: { name: "US Dollar / Japanese Yen", category: "FOREX" as const, digits: 3, minLot: 0.01, contractSize: 100000, bid: 154.332, ask: 154.349, last: 154.341, change: 0.232, changePct: 0.151, spread: 1.7, volume: "198.6M" },
  XAUUSD: { name: "Gold / US Dollar", category: "COMMODITY" as const, digits: 2, minLot: 0.01, contractSize: 100, bid: 2368.36, ask: 2368.58, last: 2368.47, change: 13.72, changePct: 0.582, spread: 0.22, volume: "92.4M" },
  US30: { name: "Dow Jones 30", category: "INDEX" as const, digits: 1, minLot: 0.1, contractSize: 1, bid: 40762.1, ask: 40763.2, last: 40762.7, change: -128.4, changePct: -0.314, spread: 1.1, volume: "64.1M" },
  NAS100: { name: "Nasdaq 100", category: "INDEX" as const, digits: 1, minLot: 0.1, contractSize: 1, bid: 19342.6, ask: 19343.8, last: 19343.2, change: 84.7, changePct: 0.440, spread: 1.2, volume: "77.8M" },
};

export const quotes: Record<string, Quote> = Object.fromEntries(
  Object.entries(quoteBase).map(([symbol, quote]) => [
    symbol,
    { symbol, ...quote, marketStatus: "OPEN", updatedAt: "08:42:16 WIB" },
  ]),
);

export const symbolCatalog = Object.values(quotes).map(({ symbol, name, category, digits, minLot, contractSize }) => ({
  symbol,
  name,
  category,
  digits,
  minLot,
  contractSize,
}));

export const defaultWatchlist = ["EURUSD", "XAUUSD", "US30", "NAS100"];

export const initialPositions: Position[] = [
  {
    id: "pos-xau-001",
    symbol: "XAUUSD",
    side: "BUY",
    quantity: 0.02,
    openPrice: 2354.1,
    currentPrice: quotes.XAUUSD.last,
    unrealizedPnl: 28.74,
    margin: 47.36,
    openedAt: "Today, 06:18",
  },
];

export const equityCurve = [9950, 10012, 9978, 10044, 10028, 10126, 10088, 10172, 10248];

export const traders: Trader[] = [
  { id: "raka-fx", name: "RakaFX", style: "Trend + macro", roi: 142.6, winRate: 78.4, followers: 2340, pulseScore: 87, drawdown: 8.2, verified: true, monthsTracked: 18 },
  { id: "naya-gold", name: "Naya Gold", style: "Gold swing", roi: 96.2, winRate: 71.8, followers: 1680, pulseScore: 82, drawdown: 11.4, verified: true, monthsTracked: 12 },
  { id: "denny-index", name: "Denny Index", style: "Index momentum", roi: 74.8, winRate: 69.3, followers: 980, pulseScore: 79, drawdown: 9.8, verified: true, monthsTracked: 9 },
  { id: "ika-steady", name: "Ika Steady", style: "Low-risk intraday", roi: 48.1, winRate: 83.1, followers: 721, pulseScore: 76, drawdown: 4.9, verified: false, monthsTracked: 7 },
];

export const courses: Course[] = [
  { id: "forex-101", title: "Forex 101", level: "Beginner", lessons: 8, completed: 5, duration: "34 min" },
  { id: "risk-basics", title: "Risk management", level: "Essential", lessons: 6, completed: 2, duration: "26 min" },
  { id: "technical", title: "Technical analysis", level: "Intermediate", lessons: 10, completed: 0, duration: "48 min" },
];

function series(start: number, steps: number[], volatility: number): Candle[] {
  let previous = start;
  return steps.map((step, index) => {
    const open = previous;
    const close = start + step;
    const high = Math.max(open, close) + volatility * (0.6 + (index % 3) * 0.14);
    const low = Math.min(open, close) - volatility * (0.45 + (index % 2) * 0.18);
    previous = close;
    return { open, high, low, close };
  });
}

export function candlesFor(symbol: string, timeframe: string): Candle[] {
  const quote = quotes[symbol] ?? quotes.EURUSD;
  const multiplier = timeframe === "1D" ? 1.8 : timeframe === "15m" ? 0.72 : 1;
  const steps = [0, 2, -1, 4, 3, 6, 5, 8, 7, 10, 9, 12, 11, 14, 13, 16].map((value, index) => (value + ((index % 4) - 1.5) * 1.2) * multiplier);
  return series(quote.last - 16 * multiplier, steps.map((value) => value * (quote.last * 0.00012)), quote.last * 0.00055);
}

/**
 * Hydrates the module-level datasets (imported statically by the UI) from the
 * PULSE backend. Mutates the exported arrays/records in place so every consumer
 * referencing `quotes`, `symbolCatalog`, `traders`, `courses`, or `equityCurve`
 * observes backend data without changing its import shape.
 */
export function hydrateMarketData(payload: {
  quotes?: Quote[];
  symbolCatalog?: SymbolInfo[];
  traders?: Trader[];
  courses?: Course[];
  equityCurve?: number[];
  watchlist?: string[];
}) {
  if (payload.quotes) {
    for (const quote of payload.quotes) quotes[quote.symbol] = quote;
  }
  if (payload.symbolCatalog) {
    symbolCatalog.splice(0, symbolCatalog.length, ...payload.symbolCatalog);
  }
  if (payload.traders) {
    traders.splice(0, traders.length, ...payload.traders);
  }
  if (payload.courses) {
    courses.splice(0, courses.length, ...payload.courses);
  }
  if (payload.equityCurve) {
    equityCurve.splice(0, equityCurve.length, ...payload.equityCurve);
  }
  if (payload.watchlist) {
    defaultWatchlist.splice(0, defaultWatchlist.length, ...payload.watchlist);
  }
}

export function primeQuoteFromSymbol(info: SymbolInfo) {
  if (!quotes[info.symbol]) {
    quotes[info.symbol] = { ...quotes.EURUSD, symbol: info.symbol, name: info.name, category: info.category, digits: info.digits, minLot: info.minLot, contractSize: info.contractSize };
    symbolCatalog.push(info);
  }
}

