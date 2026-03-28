package kimondev.euro_denomination_backend.controller;


import kimondev.euro_denomination_backend.service.DenominationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

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
        when(service.calculate(137))
                .thenReturn(Map.of(
                        100, 1,
                        20, 1,
                        10, 1,
                        5, 1,
                        2, 1
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
    void shouldReturnBadRequestForNegativeAmount() throws Exception {
        when(service.calculate(-10))
                .thenThrow(new IllegalArgumentException());

        mockMvc.perform(get("/api/denomination")
                        .param("amount", "-10"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenAmountMissing() throws Exception {
        mockMvc.perform(get("/api/denomination"))
                .andExpect(status().isBadRequest());
    }
}
