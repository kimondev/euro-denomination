package kimondev.euro_denomination_backend.controller;


import kimondev.euro_denomination_backend.service.DenominationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DenominationController.class)
public class DenominationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DenominationService service;

    @Test
    void shouldReturnDenominationForAmount() throws Exception {
        when(service.calculate(new BigDecimal("137")))
                .thenReturn(Map.of(
                        new BigDecimal("100"), 1,
                        new BigDecimal("20"), 1,
                        new BigDecimal("10"), 1,
                        new BigDecimal("5"), 1,
                        new BigDecimal("2"), 1
                ));
        mockMvc.perform(get("/api/denomination?amount=137"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['100']").value(1))
                .andExpect(jsonPath("$['20']").value(1))
                .andExpect(jsonPath("$['10']").value(1))
                .andExpect(jsonPath("$['5']").value(1))
                .andExpect(jsonPath("$['2']").value(1));
    }

    @Test
    void shouldReturnDenominationForCentAmount() throws Exception {
        when(service.calculate(new BigDecimal("12.38")))
                .thenReturn(Map.of(
                        new BigDecimal("10"), 1,
                        new BigDecimal("2"), 1,
                        new BigDecimal("0.2"), 1,
                        new BigDecimal("0.1"), 1,
                        new BigDecimal("0.05"), 1,
                        new BigDecimal("0.02"), 1,
                        new BigDecimal("0.01"), 1
                ));

        mockMvc.perform(get("/api/denomination").param("amount", "12.38"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['10']").value(1))
                .andExpect(jsonPath("$['2']").value(1))
                .andExpect(jsonPath("$['0.2']").value(1))
                .andExpect(jsonPath("$['0.1']").value(1))
                .andExpect(jsonPath("$['0.05']").value(1))
                .andExpect(jsonPath("$['0.02']").value(1))
                .andExpect(jsonPath("$['0.01']").value(1));
    }

    @Test
    void shouldReturnBadRequestForNegativeAmount() throws Exception {

        when(service.calculate(new BigDecimal("-10")))
                .thenThrow(new IllegalArgumentException("Amount must be positive"));

        mockMvc.perform(get("/api/denomination")
                        .param("amount", "-10"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid request"))
                .andExpect(jsonPath("$.message").value("Amount must be positive"));
    }

    @Test
    void shouldReturnBadRequestWhenAmountMissing() throws Exception {
        mockMvc.perform(get("/api/denomination"))
                .andExpect(status().isBadRequest());
    }
}
