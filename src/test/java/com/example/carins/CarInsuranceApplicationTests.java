package com.example.carins;

import com.example.carins.service.CarService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CarInsuranceApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CarService service;

    @Test
    void insuranceValidityBasic() {
        assertTrue(service.isInsuranceValid(1L, LocalDate.parse("2024-06-01")));
        assertTrue(service.isInsuranceValid(1L, LocalDate.parse("2025-06-01")));
        assertFalse(service.isInsuranceValid(2L, LocalDate.parse("2025-02-01")));
    }

    @Test
    void testDates() throws Exception {
        mockMvc.perform(get("/api/cars/{carId}/insurance-valid", 1)
                        .param("date", "1848-05-01"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/api/cars/{carId}/insurance-valid", 1)
                        .param("date", "2025-35-35"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/api/cars/{carId}/insurance-valid", 7)
                        .param("date", "2025-09-14"))
                .andExpect(status().isNotFound());
    }
}
