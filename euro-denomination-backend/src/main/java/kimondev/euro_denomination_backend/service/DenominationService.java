package kimondev.euro_denomination_backend.service;

import java.util.HashMap;
import java.util.Map;

public class DenominationService {

    public Map<Integer, Integer> calculate(int amount) {
        System.out.println("Calculating denomination for amount: " + amount);

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
