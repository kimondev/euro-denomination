package kimondev.euro_denomination_backend.service;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Map;

public class DenominationServiceTest {
    @Test
    void shouldCalculateDenominationFor158Euros() {
        DenominationService service = new DenominationService();

        Map<BigDecimal, Integer> result = service.calculate(new BigDecimal("158.00"));

        assertThat(result).containsEntry(new BigDecimal("100"), 1);
        assertThat(result).containsEntry(new BigDecimal("50"), 1);
        assertThat(result).containsEntry(new BigDecimal("5"), 1);
        assertThat(result).containsEntry(new BigDecimal("2"), 1);
        assertThat(result).containsEntry(new BigDecimal("1"), 1);
    }

    @Test
    void shouldUseLargestAllowedDenominationsUpTo200() {
        DenominationService service = new DenominationService();

        Map<BigDecimal, Integer> result = service.calculate(new BigDecimal("880.00"));

        assertThat(result).containsEntry(new BigDecimal("200"), 4);
        assertThat(result).containsEntry(new BigDecimal("50"), 1);
        assertThat(result).containsEntry(new BigDecimal("20"), 1);
        assertThat(result).containsEntry(new BigDecimal("10"), 1);
    }

    @Test
    void shouldCalculateDenominationForCentAmounts() {
        DenominationService service = new DenominationService();

        Map<BigDecimal, Integer> result = service.calculate(new BigDecimal("12.38"));

        assertThat(result).containsEntry(new BigDecimal("10"), 1);
        assertThat(result).containsEntry(new BigDecimal("2"), 1);
        assertThat(result).containsEntry(new BigDecimal("0.2"), 1);
        assertThat(result).containsEntry(new BigDecimal("0.1"), 1);
        assertThat(result).containsEntry(new BigDecimal("0.05"), 1);
        assertThat(result).containsEntry(new BigDecimal("0.02"), 1);
        assertThat(result).containsEntry(new BigDecimal("0.01"), 1);
    }

    @Test
    void shouldReturnEmptyResultForZeroAmount() {
        DenominationService service = new DenominationService();

        Map<BigDecimal, Integer> result = service.calculate(BigDecimal.ZERO);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldThrowExceptionForNegativeAmount() {
        DenominationService service = new DenominationService();

        assertThatThrownBy(() -> service.calculate(new BigDecimal("-5.00")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldThrowExceptionForMoreThanTwoDecimals() {
        DenominationService service = new DenominationService();

        assertThatThrownBy(() -> service.calculate(new BigDecimal("1.999")))
                .isInstanceOf(IllegalArgumentException.class);
    }
}