package kimondev.euro_denomination_backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class DenominationService {
    private static final Logger log = LoggerFactory.getLogger(DenominationService.class);

    private static final int[] DENOMINATIONS_IN_CENTS = {
            20000, 10000, 5000, 2000, 1000, 500, 200, 100, 50, 20, 10, 5, 2, 1
    };

    public Map<BigDecimal, Integer> calculate(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount is required");
        }

        if (amount.signum() < 0) {
            throw new IllegalArgumentException("Amount is negative");
        }

        if (amount.scale() > 2) {
            throw new IllegalArgumentException("Amount must have at most 2 decimal places");
        }

        log.debug("Calculating denomination for amount={}", amount);

        int remainingCents = amount
                .setScale(2, RoundingMode.UNNECESSARY)
                .movePointRight(2)
                .intValueExact();

        Map<BigDecimal, Integer> result = new LinkedHashMap<>();

        for (int denominationInCents : DENOMINATIONS_IN_CENTS) {
            if (remainingCents >= denominationInCents) {
                int count = remainingCents / denominationInCents;
                result.put(toEuroAmount(denominationInCents), count);
                remainingCents = remainingCents % denominationInCents;
            }
        }

        return result;
    }

    private BigDecimal toEuroAmount(int denominationInCents) {
        BigDecimal normalized = BigDecimal.valueOf(denominationInCents, 2).stripTrailingZeros();
        return normalized.scale() < 0 ? normalized.setScale(0, RoundingMode.UNNECESSARY) : normalized;
    }
}
