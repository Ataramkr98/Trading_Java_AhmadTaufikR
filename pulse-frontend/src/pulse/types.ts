export type AssetCategory = "FOREX" | "COMMODITY" | "INDEX";
export type OrderSide = "BUY" | "SELL";
export type OrderType = "MARKET" | "LIMIT" | "STOP";
export type AppTab = "home" | "portfolio" | "copy" | "academy" | "profile";

export interface SymbolInfo {
  symbol: string;
  name: string;
  category: AssetCategory;
  digits: number;
  minLot: number;
  contractSize: number;
}

export interface Quote extends SymbolInfo {
  bid: number;
  ask: number;
  last: number;
  change: number;
  changePct: number;
  spread: number;
  volume: string;
  marketStatus: "OPEN" | "CLOSED";
  updatedAt: string;
}

export interface Candle {
  open: number;
  high: number;
  low: number;
  close: number;
}

export interface WatchlistItem {
  symbol: string;
  favorite: boolean;
}

export interface Position {
  id: string;
  symbol: string;
  side: OrderSide;
  quantity: number;
  openPrice: number;
  currentPrice: number;
  unrealizedPnl: number;
  margin: number;
  openedAt: string;
}

export interface PortfolioSummary {
  balance: number;
  equity: number;
  dailyPnl: number;
  dailyPnlPct: number;
  marginUsed: number;
  freeMargin: number;
  marginLevel: number;
}

export interface OrderDraft {
  symbol: string;
  side: OrderSide;
  orderType: OrderType;
  quantity: number;
  stopLoss?: number;
  takeProfit?: number;
}

export interface OrderPreview {
  marginRequired: number;
  estimatedFee: number;
  estimatedSwap: number;
  availableMargin: number;
  isValid: boolean;
  error?: string;
}

export interface OrderConfirmation {
  id: string;
  symbol: string;
  side: OrderSide;
  quantity: number;
  filledPrice: number;
  timestamp: string;
  status?: "FILLED" | "PENDING" | string;
}

export interface OrderHistoryItem {
  id: string;
  symbol: string;
  side: OrderSide | string;
  orderType: string;
  quantity: number;
  status: string;
  filledQty: number | null;
  avgFillPrice: number | null;
  placedAt: string;
}

export interface Trader {
  id: string;
  name: string;
  style: string;
  roi: number;
  winRate: number;
  followers: number;
  pulseScore: number;
  drawdown: number;
  verified: boolean;
  monthsTracked: number;
}

export interface CopySettings {
  mode: "PROPORTIONAL" | "FIXED";
  fixedAmount: number;
  maxLoss: number;
  maxPositionSize: number;
}

export interface Course {
  id: string;
  title: string;
  level: string;
  lessons: number;
  completed: number;
  duration: string;
}

export interface MarketRepository {
  getQuotes(symbols: string[]): Promise<Quote[]>;
  getQuote(symbol: string): Promise<Quote>;
  getCandles(symbol: string, timeframe: string): Promise<Candle[]>;
  searchSymbols(query: string): Promise<SymbolInfo[]>;
}

export interface TradingRepository {
  previewOrder(input: OrderDraft): Promise<OrderPreview>;
  placeOrder(input: OrderDraft): Promise<OrderConfirmation>;
  getPositions(): Promise<Position[]>;
  closePosition(id: string, quantity?: number): Promise<void>;
  getClosedPositions(): Promise<Position[]>;
  getOrders(): Promise<OrderHistoryItem[]>;
  cancelOrder(id: string): Promise<void>;
}

export interface PortfolioStats {
  winRate: number;
  profitFactor: number;
  maxDrawdown: number;
  avgWin: number;
  avgLoss: number;
  closedTrades: number;
}

export interface PortfolioRepository {
  getSummary(): Promise<PortfolioSummary>;
  getEquityCurve(): Promise<number[]>;
  getStats(): Promise<PortfolioStats>;
}

export interface CopyTradingRepository {
  getLeaderboard(sort: "roi" | "winRate" | "followers"): Promise<Trader[]>;
  followTrader(traderId: string, settings: CopySettings): Promise<void>;
}
