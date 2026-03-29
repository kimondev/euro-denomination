package kimondev.euro_denomination_backend.controller;

import kimondev.euro_denomination_backend.service.DenominationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class DenominationController {
    private static final Logger log = LoggerFactory.getLogger(DenominationController.class);
    private final DenominationService denominationService;

    public DenominationController(DenominationService denominationService) {
        this.denominationService = denominationService;
    }

    @GetMapping("/api/denomination")
    public Map<String, Integer> calculate(@RequestParam BigDecimal amount) {
        log.debug("Calculating denominations for amount={}", amount);

        Map<String, Integer> response = new LinkedHashMap<>();
        denominationService.calculate(amount)
                .forEach((denomination, count) -> response.put(denomination.toPlainString(), count));

        return response;
    }
}