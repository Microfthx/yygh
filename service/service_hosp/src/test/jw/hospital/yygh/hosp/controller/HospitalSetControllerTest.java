package jw.hospital.yygh.hosp.controller;

import jw.hospital.yygh.hosp.ServiceHospApplication;
import jw.hospital.yygh.hosp.service.HospitalSetService;
import jw.hospital.yygh.model.hosp.HospitalSet;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import java.math.BigDecimal;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofMinutes;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration("classpath:application.properties")
public class HospitalSetControllerTest {

    @Resource(name="hospitalSetService")
    private HospitalSetService hospitalSetService;

    private final HospitalSet hospitalSet = hospitalSetService.list().get(0);


    @Test
    void groupedAssertions() {
        // In a grouped assertion all assertions are executed, and all
        // failures will be reported together.
        assertAll("hospitalSet",
                () -> assertEquals("Jane", hospitalSet.getHosname()),
                () -> assertEquals("Doe", hospitalSet.getHoscode())
        );
    }



    @Test
    void dependentAssertions() {
        // Within a code block, if an assertion fails the
        // subsequent code in the same block will be skipped.
        assertAll("properties",
                () -> {
                    String firstName = hospitalSet.getHosname();
                    assertNotNull(firstName);

                    // Executed only if the previous assertion is valid.
                    assertAll("hosname",
//                            () -> assertTrue(firstName.startsWith("J")),
                            () -> assertTrue(firstName.endsWith("院"))
                    );
                },
                () -> {
                    // Grouped assertion, so processed independently
                    // of results of first name assertions.
                    String lastName = hospitalSet.getHoscode();
                    assertNotNull(lastName);

                    // Executed only if the previous assertion is valid.
                    assertAll("last name",
                            () -> assertTrue(lastName.startsWith("D")),
                            () -> assertTrue(lastName.endsWith("e"))
                    );
                }
        );
    }

    @Test
    void exceptionTesting() {
        Exception exception = assertThrows(ArithmeticException.class, () ->
                new BigDecimal(1).divide(BigDecimal.valueOf(0)));
        assertEquals("/ by zero", exception.getMessage());
    }
}
