package com.ahmadtrportofolio.pulse.watchlist;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "watchlist_items")
public class WatchlistItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "watchlist_id", nullable = false)
    private Watchlist watchlist;

    @Column(name = "symbol", nullable = false)
    private String symbol;

    @Column(name = "favorite", nullable = false)
    private boolean favorite;

    @Column(name = "item_position", nullable = false)
    private int itemPosition;

    public UUID getId() { return id; }
    public Watchlist getWatchlist() { return watchlist; }
    public void setWatchlist(Watchlist watchlist) { this.watchlist = watchlist; }
    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }
    public boolean isFavorite() { return favorite; }
    public void setFavorite(boolean favorite) { this.favorite = favorite; }
    public int getItemPosition() { return itemPosition; }
    public void setItemPosition(int itemPosition) { this.itemPosition = itemPosition; }
}
