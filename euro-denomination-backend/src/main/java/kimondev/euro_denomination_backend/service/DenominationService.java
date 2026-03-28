package kimondev.euro_denomination_backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DenominationService {
    private static final Logger log = LoggerFactory.getLogger(DenominationService.class);

    public Map<Integer, Integer> calculate(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount is negative");
        }
        log.debug("Calculating denomination for amount={}", amount);

        int[] denominations = {500, 200, 100, 50, 20, 10, 5, 2, 1};

        Map<Integer, Integer> result = new HashMap<>();

        for (int denom : denominations) {
            if (amount >= denom) {
                int count = amount / denom;
                result.put(denom, count);
                amount = amount % denom;
            }
        }

        return result;
    }
}
