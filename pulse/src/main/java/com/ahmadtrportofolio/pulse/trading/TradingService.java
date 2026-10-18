package com.ahmadtrportofolio.pulse.trading;

import com.ahmadtrportofolio.pulse.common.NotFoundException;
import com.ahmadtrportofolio.pulse.common.OrderRejectedException;
import com.ahmadtrportofolio.pulse.market.Symbol;
import com.ahmadtrportofolio.pulse.market.SymbolRepository;
import com.ahmadtrportofolio.pulse.portfolio.PortfolioService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TradingService {

    public static final BigDecimal FEE_PER_LOT = new BigDecimal("2.00");
    public static final BigDecimal SWAP_PER_LOT = new BigDecimal("0.12");
    public static final BigDecimal MARGIN_PERCENT = new BigDecimal("0.01");
    private static final BigDecimal MAX_LOT = new BigDecimal("100");

    public static String validateSide(String side) {
        if (!"BUY".equals(side) && !"SELL".equals(side)) {
            return "Side must be BUY or SELL";
        }
        return null;
    }

    public static String validateOrderType(String orderType) {
        String type = orderType == null ? "MARKET" : orderType;
        if (!"MARKET".equals(type) && !"LIMIT".equals(type) && !"STOP".equals(type)) {
            return "Order type must be MARKET, LIMIT, or STOP";
        }
        return null;
    }

    public static boolean isPendingType(String orderType) {
        return "LIMIT".equals(orderType) || "STOP".equals(orderType);
    }

    public record OrderDraft(String symbol, String side, String orderType, BigDecimal quantity,
                             BigDecimal stopLoss, BigDecimal takeProfit) {}

    public record OrderPreviewDto(BigDecimal marginRequired, BigDecimal estimatedFee, BigDecimal estimatedSwap,
                                  BigDecimal availableMargin, boolean isValid, String error) {}

    public record OrderConfirmationDto(UUID id, String symbol, String side, BigDecimal quantity,
                                       BigDecimal filledPrice, String timestamp, String status) {}

    public record PositionDto(UUID id, String symbol, String side, BigDecimal quantity, BigDecimal openPrice,
                              BigDecimal currentPrice, BigDecimal unrealizedPnl, BigDecimal margin, String openedAt) {}

    public record OrderDto(UUID id, String symbol, String side, String orderType, BigDecimal quantity,
                           String status, BigDecimal filledQty, BigDecimal avgFillPrice, String placedAt) {}

    private final SymbolRepository symbolRepository;
    private final TradingAccountRepository accountRepository;
    private final OrderRepository orderRepository;
    private final PositionRepository positionRepository;
    private final PortfolioService portfolioService;

    public TradingService(SymbolRepository symbolRepository, TradingAccountRepository accountRepository,
                          OrderRepository orderRepository, PositionRepository positionRepository,
                          PortfolioService portfolioService) {
        this.symbolRepository = symbolRepository;
        this.accountRepository = accountRepository;
        this.orderRepository = orderRepository;
        this.positionRepository = positionRepository;
        this.portfolioService = portfolioService;
    }

    public TradingAccount demoAccount() {
        return accountRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new NotFoundException("Demo trading account not found"));
    }

    public BigDecimal availableMargin(TradingAccount account) {
        return portfolioService.summaryFor(account).freeMargin();
    }

    public OrderPreviewDto preview(OrderDraft draft) {
        if (draft.quantity() == null) {
            TradingAccount account = demoAccount();
            BigDecimal available = availableMargin(account);
            return new OrderPreviewDto(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, available, false, "Quantity is required");
        }
        Symbol symbol = symbolRepository.findByCodeIgnoreCase(draft.symbol())
                .orElseThrow(() -> new NotFoundException("Symbol not found: " + draft.symbol()));
        TradingAccount account = demoAccount();
        BigDecimal available = availableMargin(account);
        BigDecimal marginRequired = symbol.getLast()
                .multiply(symbol.getContractSize())
                .multiply(draft.quantity())
                .multiply(MARGIN_PERCENT)
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal estimatedFee = draft.quantity().multiply(FEE_PER_LOT).setScale(2, RoundingMode.HALF_UP);
        BigDecimal estimatedSwap = draft.quantity().multiply(SWAP_PER_LOT).setScale(2, RoundingMode.HALF_UP);
        String error = validate(draft, symbol, marginRequired, available);
        return new OrderPreviewDto(marginRequired, estimatedFee, estimatedSwap, available, error == null, error);
    }

    private String validate(OrderDraft draft, Symbol symbol, BigDecimal marginRequired, BigDecimal availableMargin) {
        String sideError = validateSide(draft.side());
        if (sideError != null) return sideError;
        String typeError = validateOrderType(draft.orderType());
        if (typeError != null) return typeError;
        BigDecimal qty = draft.quantity();
        if (qty == null || qty.compareTo(symbol.getMinLot()) < 0 || qty.compareTo(MAX_LOT) > 0) {
            return "Lot size must be between " + symbol.getMinLot().setScale(2, RoundingMode.HALF_UP) + " and 100.00";
        }
        String side = draft.side();
        BigDecimal last = symbol.getLast();
        if (draft.stopLoss() != null) {
            boolean wrong = "BUY".equals(side) ? draft.stopLoss().compareTo(last) >= 0 : draft.stopLoss().compareTo(last) <= 0;
            if (wrong) return "For " + ("BUY".equals(side) ? "Buy" : "Sell") + ", stop loss is on the wrong side of the market";
        }
        if (draft.takeProfit() != null) {
            boolean wrong = "BUY".equals(side) ? draft.takeProfit().compareTo(last) <= 0 : draft.takeProfit().compareTo(last) >= 0;
            if (wrong) return "For " + ("BUY".equals(side) ? "Buy" : "Sell") + ", take profit is on the wrong side of the market";
        }
        if (draft.stopLoss() != null && draft.takeProfit() != null) {
            boolean reversed = "BUY".equals(side) ? draft.stopLoss().compareTo(draft.takeProfit()) >= 0 : draft.stopLoss().compareTo(draft.takeProfit()) <= 0;
            if (reversed) return "For " + ("BUY".equals(side) ? "Buy" : "Sell") + ", stop loss and take profit are reversed";
        }
        if (marginRequired.compareTo(availableMargin) > 0) {
            return "Insufficient free margin for this order";
        }
        return null;
    }

    @Transactional
    public OrderConfirmationDto place(OrderDraft draft) {
        OrderPreviewDto preview = preview(draft);
        if (!preview.isValid()) {
            throw new OrderRejectedException(preview.error());
        }
        Symbol symbol = symbolRepository.findByCodeIgnoreCase(draft.symbol()).orElseThrow();
        TradingAccount account = demoAccount();

        Order order = new Order();
        order.setAccountId(account.getId());
        order.setOrderNumber("PULSE-" + System.currentTimeMillis());
        order.setSymbol(symbol.getCode());
        order.setSide(draft.side());
        order.setOrderType(draft.orderType() == null ? "MARKET" : draft.orderType());
        order.setQuantity(draft.quantity());
        order.setSlPrice(draft.stopLoss());
        order.setTpPrice(draft.takeProfit());
        order.setPlacedAt(OffsetDateTime.now());

        String resolvedType = order.getOrderType();
        if (isPendingType(resolvedType)) {
            order.setStatus("PENDING");
            order.setFilledQty(BigDecimal.ZERO);
            orderRepository.save(order);
            BigDecimal indicative = "BUY".equals(draft.side()) ? symbol.getAsk() : symbol.getBid();
            String timestamp = OffsetDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            return new OrderConfirmationDto(order.getId(), symbol.getCode(), draft.side(), draft.quantity(), indicative, timestamp, "PENDING");
        }

        order.setStatus("FILLED");
        order.setFilledQty(draft.quantity());
        BigDecimal fillPrice = "BUY".equals(draft.side()) ? symbol.getAsk() : symbol.getBid();
        order.setAvgFillPrice(fillPrice);
        order.setPlacedAt(OffsetDateTime.now());
        order.setFilledAt(OffsetDateTime.now());
        orderRepository.save(order);

        Position position = new Position();
        position.setAccountId(account.getId());
        position.setSymbol(symbol.getCode());
        position.setSide(draft.side());
        position.setQuantity(draft.quantity());
        position.setOpenPrice(fillPrice);
        position.setSlPrice(draft.stopLoss());
        position.setTpPrice(draft.takeProfit());
        position.setCommission(draft.quantity().multiply(FEE_PER_LOT).setScale(2, RoundingMode.HALF_UP));
        position.setRealizedPnl(BigDecimal.ZERO);
        position.setStatus("OPEN");
        position.setOpenedAt(OffsetDateTime.now());
        positionRepository.save(position);

        String timestamp = OffsetDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        return new OrderConfirmationDto(position.getId(), symbol.getCode(), draft.side(), draft.quantity(), fillPrice, timestamp, "FILLED");
    }

    @Transactional(readOnly = true)
    public List<PositionDto> openPositions() {
        TradingAccount account = demoAccount();
        List<Position> positions = positionRepository.findByAccountIdAndStatusOrderByOpenedAtDesc(account.getId(), "OPEN");
        return positions.stream().map(p -> {
            Symbol symbol = symbolRepository.findByCodeIgnoreCase(p.getSymbol()).orElseThrow();
            BigDecimal current = symbol.getLast();
            BigDecimal pnl = unrealizedPnl(p, symbol);
            BigDecimal margin = p.getOpenPrice().multiply(symbol.getContractSize()).multiply(p.getQuantity())
                    .multiply(MARGIN_PERCENT).setScale(2, RoundingMode.HALF_UP);
            String openedAt = p.getOpenedAt().format(DateTimeFormatter.ofPattern("MMM d, HH:mm"));
            return new PositionDto(p.getId(), p.getSymbol(), p.getSide(), p.getQuantity(), p.getOpenPrice(),
                    current, pnl, margin, openedAt);
        }).toList();
    }

    public BigDecimal unrealizedPnl(Position p, Symbol symbol) {
        BigDecimal direction = "BUY".equals(p.getSide()) ? BigDecimal.ONE : BigDecimal.ONE.negate();
        return symbol.getLast().subtract(p.getOpenPrice())
                .multiply(direction)
                .multiply(p.getQuantity())
                .multiply(symbol.getContractSize())
                .setScale(2, RoundingMode.HALF_UP);
    }

    @Transactional
    public void closePosition(UUID positionId, BigDecimal quantity) {
        Position position = positionRepository.findById(positionId)
                .orElseThrow(() -> new NotFoundException("Position not found: " + positionId));
        if (!"OPEN".equals(position.getStatus())) {
            throw new OrderRejectedException("Position is already closed");
        }
        Symbol symbol = symbolRepository.findByCodeIgnoreCase(position.getSymbol())
                .orElseThrow(() -> new NotFoundException("Symbol not found: " + position.getSymbol()));
        BigDecimal closeQty = quantity == null ? position.getQuantity() : quantity;
        if (closeQty.compareTo(BigDecimal.ZERO) <= 0 || closeQty.compareTo(position.getQuantity()) > 0) {
            throw new OrderRejectedException("Invalid close quantity");
        }
        BigDecimal direction = "BUY".equals(position.getSide()) ? BigDecimal.ONE : BigDecimal.ONE.negate();
        BigDecimal pnl = symbol.getLast().subtract(position.getOpenPrice())
                .multiply(direction)
                .multiply(closeQty)
                .multiply(symbol.getContractSize())
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal fee = closeQty.multiply(FEE_PER_LOT).setScale(2, RoundingMode.HALF_UP);
        BigDecimal net = pnl.subtract(fee);

        TradingAccount account = accountRepository.findById(position.getAccountId()).orElseThrow();
        account.setBalance(account.getBalance().add(net));
        accountRepository.save(account);
        portfolioService.recordTransaction(account, "TRADE", net,
                "Close " + closeQty.stripTrailingZeros().toPlainString() + " lot " + position.getSymbol());

        if (closeQty.compareTo(position.getQuantity()) == 0) {
            position.setQuantity(BigDecimal.ZERO);
            position.setRealizedPnl(net);
            position.setStatus("CLOSED");
            position.setClosedAt(OffsetDateTime.now());
        } else {
            position.setQuantity(position.getQuantity().subtract(closeQty));
            position.setRealizedPnl(position.getRealizedPnl().add(net));
        }
        positionRepository.save(position);
    }

    @Transactional(readOnly = true)
    public List<OrderDto> orderHistory() {
        TradingAccount account = demoAccount();
        return orderRepository.findByAccountIdOrderByPlacedAtDesc(account.getId()).stream()
                .map(o -> new OrderDto(o.getId(), o.getSymbol(), o.getSide(), o.getOrderType(), o.getQuantity(),
                        o.getStatus(), o.getFilledQty(), o.getAvgFillPrice(),
                        o.getPlacedAt().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)))
                .toList();
    }

    @Transactional(readOnly = true)
    public OrderDto orderDetail(java.util.UUID id) {
        TradingAccount account = demoAccount();
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found: " + id));
        if (!order.getAccountId().equals(account.getId())) {
            throw new NotFoundException("Order not found: " + id);
        }
        return new OrderDto(order.getId(), order.getSymbol(), order.getSide(), order.getOrderType(), order.getQuantity(),
                order.getStatus(), order.getFilledQty(), order.getAvgFillPrice(),
                order.getPlacedAt().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
    }

    @Transactional
    public void cancelOrder(java.util.UUID id) {
        TradingAccount account = demoAccount();
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found: " + id));
        if (!order.getAccountId().equals(account.getId())) {
            throw new NotFoundException("Order not found: " + id);
        }
        if (!"PENDING".equals(order.getStatus())) {
            throw new OrderRejectedException("Only pending orders can be cancelled");
        }
        order.setStatus("CANCELLED");
        orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public List<PositionDto> closedPositions() {
        TradingAccount account = demoAccount();
        List<Position> positions = positionRepository.findByAccountIdAndStatusOrderByOpenedAtDesc(account.getId(), "CLOSED");
        return positions.stream().limit(50).map(p -> {
            Symbol symbol = symbolRepository.findByCodeIgnoreCase(p.getSymbol()).orElse(null);
            java.math.BigDecimal current = symbol == null ? p.getOpenPrice() : symbol.getLast();
            java.math.BigDecimal margin = p.getOpenPrice().multiply(symbol == null ? java.math.BigDecimal.ONE : symbol.getContractSize())
                    .multiply(p.getQuantity()).multiply(MARGIN_PERCENT).setScale(2, RoundingMode.HALF_UP);
            String openedAt = p.getOpenedAt() == null ? "" : p.getOpenedAt().format(DateTimeFormatter.ofPattern("MMM d, HH:mm"));
            return new PositionDto(p.getId(), p.getSymbol(), p.getSide(), p.getQuantity(), p.getOpenPrice(),
                    current, p.getRealizedPnl() == null ? java.math.BigDecimal.ZERO : p.getRealizedPnl(), margin, openedAt);
        }).toList();
    }
}
