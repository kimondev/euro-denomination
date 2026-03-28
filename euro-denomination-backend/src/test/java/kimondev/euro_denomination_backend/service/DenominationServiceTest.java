package kimondev.euro_denomination_backend.service;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @Test
    void shouldUseLargestDenominationsIncluding200And500() {
        DenominationService service = new DenominationService();

        Map<Integer, Integer> result = service.calculate(880);

        assertThat(result).containsEntry(500, 1);
        assertThat(result).containsEntry(200, 1);
        assertThat(result).containsEntry(100, 1);
        assertThat(result).containsEntry(50, 1);
        assertThat(result).containsEntry(20, 1);
        assertThat(result).containsEntry(10, 1);
    }

    @Test
    void shouldReturnEmptyResultForZeroAmount() {
        DenominationService service = new DenominationService();

        Map<Integer, Integer> result = service.calculate(0);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldThrowExceptionForNegativeAmount() {
        DenominationService service = new DenominationService();

        assertThatThrownBy(() -> service.calculate(-5))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
