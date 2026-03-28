package kimondev.euro_denomination_backend.controller;

import kimondev.euro_denomination_backend.service.DenominationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class DenominationController {
    private static final Logger log = LoggerFactory.getLogger(DenominationController.class);
    private final DenominationService denominationService;

    public DenominationController(DenominationService denominationService) {
        this.denominationService = denominationService;
    }

    @GetMapping("/api/denomination")
    public Map<Integer, Integer> calculate(@RequestParam int amount) {
        log.debug("Calculating denominations for amount={}", amount);
        return denominationService.calculate(amount);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleIllegalArgumentException() {
    }
}