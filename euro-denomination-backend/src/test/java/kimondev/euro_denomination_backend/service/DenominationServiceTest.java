package kimondev.euro_denomination_backend.service;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

public class DenominationServiceTest {
    @Test
    void shouldCalculateDenominationFor137Euros() {
        DenominationService service = new DenominationService();

        Map<Integer, Integer> result = service.calculate(137);

        assertThat(result).containsEntry(100, 1);
        assertThat(result).containsEntry(20, 1);
        assertThat(result).containsEntry(10, 1);
        assertThat(result).containsEntry(5, 1);
        assertThat(result).containsEntry(2, 1);
    }
}
