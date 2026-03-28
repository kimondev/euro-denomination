package kimondev.euro_denomination_backend.service;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

public class DenominationServiceTest {
    @Test
    void shouldCalculateDenominationFor158Euros() {
        DenominationService service = new DenominationService();

        Map<Integer, Integer> result = service.calculate(158);

        assertThat(result).containsEntry(100, 1);
        assertThat(result).containsEntry(50, 1);
        assertThat(result).containsEntry(5, 1);
        assertThat(result).containsEntry(2, 1);
        assertThat(result).containsEntry(1, 1);
    }
}
