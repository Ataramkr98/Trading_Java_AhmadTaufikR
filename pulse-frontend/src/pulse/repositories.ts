import { candlesFor, courses as bundledCourses, equityCurve as bundledCurve, hydrateMarketData, primeQuoteFromSymbol, quotes, symbolCatalog } from "./mockData";
import { deleteJson, getJson, mapTimeframe, postJson } from "./apiClient";
import { usePulseStore } from "./store";
import type {
  Candle,
  CopySettings,
  Course,
  MarketRepository,
  OrderDraft,
  OrderConfirmation,
  OrderPreview,
  PortfolioRepository,
  PortfolioSummary,
  Position,
  Quote,
  SymbolInfo,
  TradingRepository,
  Trader,
} from "./types";

const wait = (ms: number) => new Promise((resolve) => window.setTimeout(resolve, ms));

let hydration: Promise<void> | null = null;

/**
 * Pulls the canonical datasets from the PULSE backend once and hydrates the
 * statically-imported mock datasets so the UI renders live data. Falls back to
 * the bundled mock data when the backend is unreachable (offline prototype).
 */
export function ensureBackendData(): Promise<void> {
  hydration ??= (async () => {
    try {
      const [symbolList, leaderboard, watchlists, courses, equityCurve, positions] = await Promise.all([
        getJson<SymbolInfo[]>("/market/symbols"),
        getJson<Trader[]>("/copy/leaderboard?sort=roi"),
        getJson<{ id: string; name: string; symbols: string[] }[]>("/watchlists"),
        getJson<Course[]>("/academy/courses"),
        getJson<number[]>("/portfolio/equity-curve"),
        getJson<Position[]>("/positions"),
      ]);
      const quoteList = symbolList.length
        ? await postJson<Quote[]>("/market/quotes", { symbols: symbolList.map((item) => item.symbol) })
        : [];
      hydrateMarketData({ quotes: quoteList, symbolCatalog: symbolList, traders: leaderboard, courses, equityCurve, watchlist: watchlists[0]?.symbols });
      usePulseStore.getState().setWatchlist(watchlists[0]?.symbols ?? []);
      usePulseStore.getState().setPositions(positions);
    } catch {
      hydration = null; // allow retry on next call; mock data stays in place
    }
  })();
  return hydration;
}

export const marketRepository: MarketRepository = {
  async getQuotes(symbols) {
    await ensureBackendData();
    try {
      const fetched = await postJson<Quote[]>("/market/quotes", { symbols });
      for (const quote of fetched) {
        primeQuoteFromSymbol({ symbol: quote.symbol, name: quote.name, category: quote.category, digits: quote.digits, minLot: quote.minLot, contractSize: quote.contractSize });
      }
      hydrateMarketData({ quotes: fetched });
      return fetched;
    } catch {
      return symbols.map((symbol) => quotes[symbol]).filter(Boolean);
    }
  },
  async getQuote(symbol) {
    await ensureBackendData();
    try {
      const quote = await getJson<Quote>(`/market/quote/${encodeURIComponent(symbol)}`);
      hydrateMarketData({ quotes: [quote] });
      return quote;
    } catch (error) {
      const fallback = quotes[symbol];
      if (!fallback) throw error;
      return fallback;
    }
  },
  async getCandles(symbol, timeframe) {
    await ensureBackendData();
    try {
      return await getJson<Candle[]>(`/market/ohlcv/${encodeURIComponent(symbol)}?tf=${mapTimeframe(timeframe)}&limit=16`);
    } catch {
      await wait(60);
      return candlesFor(symbol, timeframe);
    }
  },
  async searchSymbols(query) {
    await ensureBackendData();
    try {
      return await getJson<SymbolInfo[]>(`/market/search?q=${encodeURIComponent(query)}`);
    } catch {
      await wait(60);
      const normalized = query.trim().toLowerCase();
      return symbolCatalog.filter((item) => !normalized || item.symbol.toLowerCase().includes(normalized) || item.name.toLowerCase().includes(normalized));
    }
  },
};

export const tradingRepository: TradingRepository = {
  async previewOrder(input) {
    await ensureBackendData();
    return postJson<OrderPreview>("/orders/preview", { ...input });
  },
  async placeOrder(input) {
    await ensureBackendData();
    const confirmation = await postJson<OrderConfirmation>("/orders", { ...input });
    try {
      await tradingRepository.getPositions(); // refresh open positions into the store
    } catch {
      // offline prototype keeps the optimistic position from the store
    }
    return confirmation;
  },
  async getPositions() {
    await ensureBackendData();
    try {
      const positions = await getJson<Position[]>("/positions");
      usePulseStore.getState().setPositions(positions);
      return positions;
    } catch {
      return usePulseStore.getState().positions;
    }
  },
  async closePosition(id, quantity) {
    await postJson<void>(`/positions/${encodeURIComponent(id)}/close`, quantity ? { quantity } : undefined);
    try {
      await tradingRepository.getPositions();
    } catch {
      usePulseStore.getState().removePosition(id);
    }
  },
  async getClosedPositions() {
    await ensureBackendData();
    try {
      return await getJson<Position[]>("/positions/closed");
    } catch {
      return [];
    }
  },
  async getOrders() {
    await ensureBackendData();
    try {
      return await getJson<import("./types").OrderHistoryItem[]>("/orders");
    } catch {
      return [];
    }
  },
  async cancelOrder(id: string) {
    await deleteJson<void>(`/orders/${encodeURIComponent(id)}`);
  },
};

export const portfolioRepository: PortfolioRepository = {
  async getSummary() {
    await ensureBackendData();
    try {
      return await getJson<PortfolioSummary>("/portfolio/summary");
    } catch {
      return { balance: 10248.6, equity: 10248.6, dailyPnl: 248.6, dailyPnlPct: 2.49, marginUsed: 47.36, freeMargin: 10201.24, marginLevel: 21641 };
    }
  },
  async getEquityCurve() {
    await ensureBackendData();
    try {
      const points = await getJson<number[]>("/portfolio/equity-curve");
      hydrateMarketData({ equityCurve: points });
      return points;
    } catch {
      return [...bundledCurve];
    }
  },
  async getStats() {
    await ensureBackendData();
    try {
      return await getJson<import("./types").PortfolioStats>("/portfolio/stats");
    } catch {
      return { winRate: 68.4, profitFactor: 1.84, maxDrawdown: -6.2, avgWin: 42, avgLoss: 24, closedTrades: 0 };
    }
  },
};

export const academyRepository = {
  async getCourses() {
    await ensureBackendData();
    try {
      const courses = await getJson<Course[]>("/academy/courses");
      hydrateMarketData({ courses });
      return courses;
    } catch {
      return [...bundledCourses];
    }
  },
  async completeLesson(courseId: string, lessonIndex: number) {
    return postJson<{ ok: boolean }>(`/academy/lessons/${encodeURIComponent(courseId)}/complete`, { lessonIndex });
  },
};

export const copyTradingRepository = {
  async getLeaderboard(sort: "roi" | "winRate" | "followers") {
    await ensureBackendData();
    const traders = await getJson<Trader[]>(`/copy/leaderboard?sort=${sort}`);
    if (sort === "roi") hydrateMarketData({ traders });
    return traders;
  },
  async followTrader(traderId: string, settings: CopySettings) {
    await postJson<void>("/copy/follow", {
      traderId,
      mode: settings.mode,
      fixedAmount: settings.fixedAmount,
      maxLoss: settings.maxLoss,
      maxPositionSize: settings.maxPositionSize,
    });
  },
};
