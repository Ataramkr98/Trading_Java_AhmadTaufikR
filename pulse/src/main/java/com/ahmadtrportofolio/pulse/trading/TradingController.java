package com.ahmadtrportofolio.pulse.trading;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class TradingController {

    private final TradingService tradingService;

    public TradingController(TradingService tradingService) {
        this.tradingService = tradingService;
    }

    public record OrderRequest(@NotBlank String symbol, @NotBlank String side,
                               @NotBlank String orderType,
                               @NotNull @jakarta.validation.constraints.DecimalMin(value = "0.01", message = "quantity must be at least 0.01") BigDecimal quantity,
                               BigDecimal stopLoss, BigDecimal takeProfit) {
        TradingService.OrderDraft toDraft() {
            return new TradingService.OrderDraft(symbol, side, orderType, quantity, stopLoss, takeProfit);
        }
    }

    @PostMapping("/orders/preview")
    public TradingService.OrderPreviewDto preview(@Valid @RequestBody OrderRequest request) {
        return tradingService.preview(request.toDraft());
    }

    @PostMapping("/orders")
    public TradingService.OrderConfirmationDto place(@Valid @RequestBody OrderRequest request) {
        return tradingService.place(request.toDraft());
    }

    @GetMapping("/orders")
    public List<TradingService.OrderDto> orders() {
        return tradingService.orderHistory();
    }

    @GetMapping("/orders/{id}")
    public TradingService.OrderDto order(@PathVariable UUID id) {
        return tradingService.orderDetail(id);
    }

    @DeleteMapping("/orders/{id}")
    public void cancel(@PathVariable UUID id) {
        tradingService.cancelOrder(id);
    }

    @GetMapping("/positions")
    public List<TradingService.PositionDto> positions() {
        return tradingService.openPositions();
    }

    @GetMapping("/positions/closed")
    public List<TradingService.PositionDto> closed() {
        return tradingService.closedPositions();
    }

    public record CloseRequest(BigDecimal quantity) {}

    @PostMapping("/positions/{id}/close")
    public void close(@PathVariable UUID id, @RequestBody(required = false) CloseRequest request) {
        tradingService.closePosition(id, request == null ? null : request.quantity());
    }

    public record MarginRequest(String symbol, BigDecimal quantity) {}

    @PostMapping("/calculator/margin")
    public TradingService.OrderPreviewDto calculator(@RequestBody MarginRequest request) {
        TradingService.OrderDraft draft = new TradingService.OrderDraft(
                request.symbol(), "BUY", "MARKET", request.quantity(), null, null);
        return tradingService.preview(draft);
    }
}
