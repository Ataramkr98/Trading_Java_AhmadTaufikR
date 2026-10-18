import { create } from "zustand";
import { defaultWatchlist, initialPositions } from "./mockData";
import { deleteJson, postJson } from "./apiClient";
import type { AppTab, OrderConfirmation, OrderSide, Position } from "./types";

interface PulseState {
  activeTab: AppTab;
  watchlist: string[];
  positions: Position[];
  followedTraderIds: string[];
  setActiveTab: (tab: AppTab) => void;
  setWatchlist: (symbols: string[]) => void;
  setPositions: (positions: Position[]) => void;
  removePosition: (id: string) => void;
  addToWatchlist: (symbol: string) => void;
  removeFromWatchlist: (symbol: string) => void;
  addFilledPosition: (confirmation: OrderConfirmation) => void;
  followTrader: (traderId: string) => void;
}

export const usePulseStore = create<PulseState>((set) => ({
  activeTab: "home",
  watchlist: defaultWatchlist,
  positions: initialPositions,
  followedTraderIds: [],
  setActiveTab: (activeTab) => set({ activeTab }),
  setWatchlist: (watchlist) => set({ watchlist }),
  setPositions: (positions) => set({ positions }),
  removePosition: (id) => set((state) => ({ positions: state.positions.filter((p) => p.id !== id) })),
  addToWatchlist: (symbol) =>
    set((state) => {
      if (state.watchlist.includes(symbol)) return state;
      void postJson("/watchlists/items", { symbol }).catch(() => undefined);
      return { watchlist: [...state.watchlist, symbol] };
    }),
  removeFromWatchlist: (symbol) =>
    set((state) => {
      if (!state.watchlist.includes(symbol)) return state;
      void deleteJson(`/watchlists/items/${encodeURIComponent(symbol)}`).catch(() => undefined);
      return { watchlist: state.watchlist.filter((item) => item !== symbol) };
    }),
  addFilledPosition: (confirmation) => set((state) => ({ positions: [{ id: confirmation.id, symbol: confirmation.symbol, side: confirmation.side as OrderSide, quantity: confirmation.quantity, openPrice: confirmation.filledPrice, currentPrice: confirmation.filledPrice, unrealizedPnl: 0, margin: confirmation.filledPrice * confirmation.quantity * 100000 * 0.01, openedAt: "Just now" }, ...state.positions] })),
  followTrader: (traderId) => set((state) => ({ followedTraderIds: state.followedTraderIds.includes(traderId) ? state.followedTraderIds : [...state.followedTraderIds, traderId] })),
}));
