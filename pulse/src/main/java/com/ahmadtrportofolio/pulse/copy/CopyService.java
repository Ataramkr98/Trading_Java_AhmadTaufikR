package com.ahmadtrportofolio.pulse.copy;

import com.ahmadtrportofolio.pulse.common.NotFoundException;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CopyService {

    public record TraderDto(UUID id, String name, String style, BigDecimal roi, BigDecimal winRate,
                            int followers, BigDecimal pulseScore, BigDecimal drawdown,
                            boolean verified, int monthsTracked) {
        static TraderDto from(TopTrader t) {
            return new TraderDto(t.getId(), t.getDisplayName(), t.getStyle(), t.getRoi(), t.getWinRate(),
                    t.getFollowers(), t.getPulseScore(), t.getDrawdown(), t.isVerified(), t.getMonthsTracked());
        }
    }

    public record CopySettings(String mode, BigDecimal fixedAmount, BigDecimal maxLoss, BigDecimal maxPositionSize) {}

    public record SubscriptionDto(UUID id, UUID traderId, String traderName, String copyMode,
                                  BigDecimal fixedAmount, BigDecimal maxLoss, BigDecimal maxPositionSize,
                                  BigDecimal totalPnl, String startedAt) {
        static SubscriptionDto from(CopySubscription s) {
            return new SubscriptionDto(s.getId(), s.getTrader().getId(), s.getTrader().getDisplayName(),
                    s.getCopyMode(), s.getFixedAmount(), s.getMaxLossLimit(), s.getMaxPositionSize(),
                    s.getTotalPnl(), s.getStartedAt().toLocalDate().toString());
        }
    }

    private final TopTraderRepository traderRepository;
    private final CopySubscriptionRepository subscriptionRepository;
    private final com.ahmadtrportofolio.pulse.portfolio.PortfolioService portfolioService;

    public CopyService(TopTraderRepository traderRepository, CopySubscriptionRepository subscriptionRepository,
                       com.ahmadtrportofolio.pulse.portfolio.PortfolioService portfolioService) {
        this.traderRepository = traderRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.portfolioService = portfolioService;
    }

    @Transactional(readOnly = true)
    public List<TraderDto> leaderboard(String sort) {
        Comparator<TopTrader> comparator = switch (sort == null ? "roi" : sort) {
            case "winRate" -> Comparator.comparing(TopTrader::getWinRate).reversed();
            case "followers" -> Comparator.comparingInt(TopTrader::getFollowers).reversed();
            default -> Comparator.comparing(TopTrader::getRoi).reversed();
        };
        return traderRepository.findAll().stream().sorted(comparator).map(TraderDto::from).toList();
    }

    @Transactional(readOnly = true)
    public TraderDto trader(UUID id) {
        return traderRepository.findById(id).map(TraderDto::from)
                .orElseThrow(() -> new NotFoundException("Trader not found: " + id));
    }

    @Transactional(readOnly = true)
    public TraderDto traderByFlexibleId(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new NotFoundException("Trader not found: " + raw);
        }
        String trimmed = raw.trim();
        try {
            return trader(UUID.fromString(trimmed));
        } catch (IllegalArgumentException ignored) {
            // fall through to slug/display-name lookup
        }
        String slug = trimmed.toLowerCase();
        String compact = trimmed.toLowerCase().replaceAll("[^a-z0-9]", "");
        return traderRepository.findAll().stream()
                .filter(t -> slugOf(t.getDisplayName()).equals(slug)
                        || compactOf(t.getDisplayName()).equals(compact)
                        || t.getId().toString().equalsIgnoreCase(trimmed))
                .findFirst().map(TraderDto::from)
                .orElseThrow(() -> new NotFoundException("Trader not found: " + raw));
    }

    public static String slugOf(String displayName) {
        if (displayName == null) return "";
        return displayName.toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("^-|-$", "");
    }

    public static String compactOf(String displayName) {
        if (displayName == null) return "";
        return displayName.toLowerCase().replaceAll("[^a-z0-9]", "");
    }

    @Transactional
    public void followFlexible(String traderId, CopySettings settings) {
        TraderDto resolved = traderByFlexibleId(traderId);
        follow(resolved.id(), settings);
    }

    @Transactional
    public void follow(UUID traderId, CopySettings settings) {
        String error = validateSettings(settings);
        if (error != null) {
            throw new com.ahmadtrportofolio.pulse.common.OrderRejectedException(error);
        }
        TopTrader trader = traderRepository.findById(traderId)
                .orElseThrow(() -> new NotFoundException("Trader not found: " + traderId));
        if (subscriptionRepository.findByFollowerIdAndTraderIdAndActiveTrue(demoUserId(), traderId).isPresent()) {
            return; // already following
        }
        CopySubscription sub = new CopySubscription();
        sub.setFollowerId(demoUserId());
        sub.setTrader(trader);
        sub.setCopyMode(settings.mode() == null ? "PROPORTIONAL" : settings.mode());
        sub.setFixedAmount(settings.fixedAmount());
        sub.setMaxLossLimit(settings.maxLoss());
        sub.setMaxPositionSize(settings.maxPositionSize());
        sub.setActive(true);
        sub.setStartedAt(OffsetDateTime.now());
        sub.setTotalPnl(BigDecimal.ZERO);
        subscriptionRepository.save(sub);
        trader.setFollowers(trader.getFollowers() + 1);
        traderRepository.save(trader);
    }

    @Transactional
    public void unfollow(UUID subscriptionId) {
        CopySubscription sub = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new NotFoundException("Copy subscription not found: " + subscriptionId));
        sub.setActive(false);
        sub.setStoppedAt(OffsetDateTime.now());
        subscriptionRepository.save(sub);
        TopTrader trader = sub.getTrader();
        if (trader.getFollowers() > 0) {
            trader.setFollowers(trader.getFollowers() - 1);
            traderRepository.save(trader);
        }
    }

    @Transactional(readOnly = true)
    public List<SubscriptionDto> myCopies() {
        return subscriptionRepository.findByFollowerIdAndActiveTrue(demoUserId()).stream()
                .map(SubscriptionDto::from).toList();
    }

    public static String validateSettings(CopySettings settings) {
        if (settings.maxLoss() == null || settings.maxLoss().compareTo(new BigDecimal("50")) < 0) {
            return "Max loss should be at least $50";
        }
        if (settings.maxPositionSize() == null || settings.maxPositionSize().compareTo(new BigDecimal("0.01")) < 0) {
            return "Max position size should be at least 0.01 lot";
        }
        if ("FIXED".equals(settings.mode()) && (settings.fixedAmount() == null
                || settings.fixedAmount().compareTo(new BigDecimal("25")) < 0)) {
            return "Fixed amount should be at least $25";
        }
        return null;
    }

    private UUID demoUserId() {
        return portfolioService.demoAccount().getUserId();
    }
}
