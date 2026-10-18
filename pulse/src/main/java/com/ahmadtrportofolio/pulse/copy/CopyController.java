package com.ahmadtrportofolio.pulse.copy;

import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/copy")
public class CopyController {

    private final CopyService copyService;

    public CopyController(CopyService copyService) {
        this.copyService = copyService;
    }

    public record FollowRequest(String traderId, String mode,
                                java.math.BigDecimal fixedAmount,
                                java.math.BigDecimal maxLoss,
                                java.math.BigDecimal maxPositionSize) {
        CopyService.CopySettings toSettings() {
            return new CopyService.CopySettings(mode, fixedAmount, maxLoss, maxPositionSize);
        }
    }

    @GetMapping("/leaderboard")
    public List<CopyService.TraderDto> leaderboard(@RequestParam(defaultValue = "roi") String sort) {
        return copyService.leaderboard(sort);
    }

    @GetMapping("/traders/{id}")
    public CopyService.TraderDto trader(@PathVariable String id) {
        return copyService.traderByFlexibleId(id);
    }

    @PostMapping("/follow")
    public void follow(@RequestBody FollowRequest request) {
        copyService.followFlexible(request.traderId(), request.toSettings());
    }

    @DeleteMapping("/follow/{id}")
    public void unfollow(@PathVariable UUID id) {
        copyService.unfollow(id);
    }

    @GetMapping("/my-copies")
    public List<CopyService.SubscriptionDto> myCopies() {
        return copyService.myCopies();
    }
}
