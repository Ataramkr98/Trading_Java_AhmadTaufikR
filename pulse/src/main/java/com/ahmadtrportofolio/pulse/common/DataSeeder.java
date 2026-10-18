package com.ahmadtrportofolio.pulse.common;

import com.ahmadtrportofolio.pulse.academy.Course;
import com.ahmadtrportofolio.pulse.academy.CourseRepository;
import com.ahmadtrportofolio.pulse.academy.LessonCompletion;
import com.ahmadtrportofolio.pulse.academy.LessonCompletionRepository;
import com.ahmadtrportofolio.pulse.copy.TopTrader;
import com.ahmadtrportofolio.pulse.copy.TopTraderRepository;
import com.ahmadtrportofolio.pulse.market.Ohlcv;
import com.ahmadtrportofolio.pulse.market.OhlcvRepository;
import com.ahmadtrportofolio.pulse.market.Symbol;
import com.ahmadtrportofolio.pulse.market.SymbolRepository;
import com.ahmadtrportofolio.pulse.portfolio.EquityCurvePoint;
import com.ahmadtrportofolio.pulse.portfolio.EquityCurvePointRepository;
import com.ahmadtrportofolio.pulse.trading.Position;
import com.ahmadtrportofolio.pulse.trading.PositionRepository;
import com.ahmadtrportofolio.pulse.trading.TradingAccount;
import com.ahmadtrportofolio.pulse.trading.TradingAccountRepository;
import com.ahmadtrportofolio.pulse.watchlist.Watchlist;
import com.ahmadtrportofolio.pulse.watchlist.WatchlistItem;
import com.ahmadtrportofolio.pulse.watchlist.WatchlistRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataSeeder implements ApplicationRunner {

    private final SymbolRepository symbolRepository;
    private final OhlcvRepository ohlcvRepository;
    private final TradingAccountRepository accountRepository;
    private final PositionRepository positionRepository;
    private final TopTraderRepository traderRepository;
    private final CourseRepository courseRepository;
    private final LessonCompletionRepository completionRepository;
    private final WatchlistRepository watchlistRepository;
    private final EquityCurvePointRepository equityCurvePointRepository;

    private static final UUID DEMO_USER = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID DEMO_ACCOUNT = UUID.fromString("22222222-2222-2222-2222-222222222222");

    public DataSeeder(SymbolRepository symbolRepository, OhlcvRepository ohlcvRepository,
                      TradingAccountRepository accountRepository, PositionRepository positionRepository,
                      TopTraderRepository traderRepository, CourseRepository courseRepository,
                      LessonCompletionRepository completionRepository, WatchlistRepository watchlistRepository,
                      EquityCurvePointRepository equityCurvePointRepository) {
        this.symbolRepository = symbolRepository;
        this.ohlcvRepository = ohlcvRepository;
        this.accountRepository = accountRepository;
        this.positionRepository = positionRepository;
        this.traderRepository = traderRepository;
        this.courseRepository = courseRepository;
        this.completionRepository = completionRepository;
        this.watchlistRepository = watchlistRepository;
        this.equityCurvePointRepository = equityCurvePointRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (symbolRepository.count() > 0) return;

        seedSymbols();
        seedCandles();
        seedAccount();
        seedTraders();
        seedCourses();
        seedWatchlist();
        seedEquityCurve();
    }

    private record QuoteSeed(String code, String name, String category, int digits, BigDecimal minLot,
                             BigDecimal contractSize, BigDecimal bid, BigDecimal ask, BigDecimal last,
                             BigDecimal change, BigDecimal changePct, BigDecimal spread, String volume) {}

    private void seedSymbols() {
        List<QuoteSeed> seeds = List.of(
            new QuoteSeed("EURUSD", "Euro / US Dollar", "FOREX", 5, bd("0.01"), bd("100000"), bd("1.08542"), bd("1.08545"), bd("1.08544"), bd("0.00012"), bd("0.011"), bd("0.3"), "254.8M"),
            new QuoteSeed("GBPUSD", "British Pound / US Dollar", "FOREX", 5, bd("0.01"), bd("100000"), bd("1.26884"), bd("1.26890"), bd("1.26887"), bd("-0.00308"), bd("-0.242"), bd("0.6"), "181.2M"),
            new QuoteSeed("USDJPY", "US Dollar / Japanese Yen", "FOREX", 3, bd("0.01"), bd("100000"), bd("154.332"), bd("154.349"), bd("154.341"), bd("0.232"), bd("0.151"), bd("1.7"), "198.6M"),
            new QuoteSeed("XAUUSD", "Gold / US Dollar", "COMMODITY", 2, bd("0.01"), bd("100"), bd("2368.36"), bd("2368.58"), bd("2368.47"), bd("13.72"), bd("0.582"), bd("0.22"), "92.4M"),
            new QuoteSeed("US30", "Dow Jones 30", "INDEX", 1, bd("0.10"), bd("1"), bd("40762.1"), bd("40763.2"), bd("40762.7"), bd("-128.4"), bd("-0.314"), bd("1.1"), "64.1M"),
            new QuoteSeed("NAS100", "Nasdaq 100", "INDEX", 1, bd("0.10"), bd("1"), bd("19342.6"), bd("19343.8"), bd("19343.2"), bd("84.7"), bd("0.440"), bd("1.2"), "77.8M")
        );
        for (QuoteSeed s : seeds) {
            Symbol symbol = new Symbol();
            symbol.setCode(s.code());
            symbol.setName(s.name());
            symbol.setCategory(s.category());
            symbol.setDigits(s.digits());
            symbol.setMinLot(s.minLot());
            symbol.setMaxLot(bd("100"));
            symbol.setContractSize(s.contractSize());
            symbol.setBid(s.bid());
            symbol.setAsk(s.ask());
            symbol.setLast(s.last());
            symbol.setChangeVal(s.change());
            symbol.setChangePct(s.changePct());
            symbol.setSpreadPips(s.spread());
            symbol.setVolumeLabel(s.volume());
            symbol.setMarketStatus("OPEN");
            symbolRepository.save(symbol);
        }
    }

    private static BigDecimal bd(String value) {
        return new BigDecimal(value);
    }

    private void seedCandles() {
        for (Symbol symbol : symbolRepository.findAll()) {
            for (String tf : List.of("1m", "15m", "1D")) {
                double multiplier = tf.equals("1D") ? 1.8 : tf.equals("15m") ? 0.72 : 1;
                double start = symbol.getLast().doubleValue() - 16 * multiplier;
                double[] steps = {0, 2, -1, 4, 3, 6, 5, 8, 7, 10, 9, 12, 11, 14, 13, 16};
                double volatility = symbol.getLast().doubleValue() * 0.00055;
                double previous = start;
                for (int i = 0; i < steps.length; i++) {
                    double open = previous;
                    double close = start + steps[i] * multiplier * (symbol.getLast().doubleValue() * 0.00012);
                    double high = Math.max(open, close) + volatility * (0.6 + (i % 3) * 0.14);
                    double low = Math.min(open, close) - volatility * (0.45 + (i % 2) * 0.18);
                    previous = close;
                    Ohlcv bar = new Ohlcv();
                    bar.setSymbol(symbol.getCode());
                    bar.setTimeframe(tf);
                    bar.setBarIndex(i);
                    bar.setOpen(d5(open));
                    bar.setHigh(d5(high));
                    bar.setLow(d5(low));
                    bar.setClose(d5(close));
                    ohlcvRepository.save(bar);
                }
            }
        }
    }

    private void seedAccount() {
        TradingAccount account = new TradingAccount();
        account.setId(DEMO_ACCOUNT);
        account.setUserId(DEMO_USER);
        account.setAccountNumber("PULSE-2026-000001");
        account.setAccountType("STANDARD");
        account.setCurrency("USD");
        account.setLeverage(100);
        account.setBalance(new BigDecimal("10000.00"));
        account.setStatus("active");
        account.setOpenedAt(OffsetDateTime.now());
        accountRepository.save(account);

        Position position = new Position();
        position.setAccountId(DEMO_ACCOUNT);
        position.setSymbol("XAUUSD");
        position.setSide("BUY");
        position.setQuantity(new BigDecimal("0.02"));
        position.setOpenPrice(new BigDecimal("2354.1"));
        position.setCommission(new BigDecimal("0.04"));
        position.setRealizedPnl(BigDecimal.ZERO);
        position.setStatus("OPEN");
        position.setOpenedAt(OffsetDateTime.now());
        positionRepository.save(position);
    }

    private void seedTraders() {
        traderRepository.saveAll(List.of(
            trader("raka-fx", "RakaFX", "Trend + macro", "142.6", "78.4", 2340, "87", "8.2", true, 18),
            trader("naya-gold", "Naya Gold", "Gold swing", "96.2", "71.8", 1680, "82", "11.4", true, 12),
            trader("denny-index", "Denny Index", "Index momentum", "74.8", "69.3", 980, "79", "9.8", true, 9),
            trader("ika-steady", "Ika Steady", "Low-risk intraday", "48.1", "83.1", 721, "76", "4.9", false, 7)
        ));
    }

    private TopTrader trader(String id, String name, String style, String roi, String winRate,
                             int followers, String score, String drawdown, boolean verified, int months) {
        TopTrader t = new TopTrader();
        t.setId(UUID.nameUUIDFromBytes(id.getBytes()));
        t.setDisplayName(name);
        t.setStyle(style);
        t.setRoi(new BigDecimal(roi));
        t.setWinRate(new BigDecimal(winRate));
        t.setFollowers(followers);
        t.setPulseScore(new BigDecimal(score));
        t.setDrawdown(new BigDecimal(drawdown));
        t.setVerified(verified);
        t.setMonthsTracked(months);
        return t;
    }

    private void seedCourses() {
        course(UUID.nameUUIDFromBytes("forex-101".getBytes()), "Forex 101", "Beginner", 8, "34 min", 5);
        course(UUID.nameUUIDFromBytes("risk-basics".getBytes()), "Risk management", "Essential", 6, "26 min", 2);
        course(UUID.nameUUIDFromBytes("technical".getBytes()), "Technical analysis", "Intermediate", 10, "48 min", 0);
    }

    private void course(UUID id, String title, String level, int lessons, String duration, int completed) {
        Course saved = courseRepository.saveAndFlush(courseEntity(id, title, level, lessons, duration));
        for (int i = 0; i < completed; i++) {
            LessonCompletion lc = new LessonCompletion();
            lc.setUserId(DEMO_USER);
            lc.setCourse(saved);
            lc.setLessonIndex(i);
            lc.setCompletedAt(OffsetDateTime.now());
            completionRepository.save(lc);
        }
    }

    private Course courseEntity(UUID id, String title, String level, int lessons, String duration) {
        Course c = new Course();
        c.setId(id);
        c.setTitle(title);
        c.setLevel(level);
        c.setLessonsCount(lessons);
        c.setDuration(duration);
        return c;
    }

    private void seedWatchlist() {
        Watchlist list = new Watchlist();
        list.setUserId(DEMO_USER);
        list.setName("Default");
        list.setCreatedAt(OffsetDateTime.now());
        int pos = 0;
        for (String symbol : List.of("EURUSD", "XAUUSD", "US30", "NAS100")) {
            WatchlistItem item = new WatchlistItem();
            item.setWatchlist(list);
            item.setSymbol(symbol);
            item.setFavorite(false);
            item.setItemPosition(pos++);
            list.getItems().add(item);
        }
        watchlistRepository.save(list);
    }

    private void seedEquityCurve() {
        double[] points = {9950, 10012, 9978, 10044, 10028, 10126, 10088, 10172, 10248};
        for (int i = 0; i < points.length; i++) {
            EquityCurvePoint p = new EquityCurvePoint();
            p.setUserId(DEMO_USER);
            p.setPointIndex(i);
            p.setValue(BigDecimal.valueOf(points[i]));
            equityCurvePointRepository.save(p);
        }
    }

    private static BigDecimal d5(double value) {
        return BigDecimal.valueOf(value).setScale(5, RoundingMode.HALF_UP);
    }
}
