//package com.platform.spreadranking;
//
//import com.platform.spreadranking.domain.spread.Spread;
//import org.junit.jupiter.api.Test;
//
//import java.math.BigDecimal;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class SpreadTest {
//
//    @Test
//    void shouldCalculateSpread() {
//        var spread = Spread.calculate(
//                new BigDecimal("4.2610"),
//                new BigDecimal("4.5997")
//        );
//
//        assertTrue(spread.value().doubleValue() > 0);
//    }
//
//    @Test
//    void shouldDetectMissingData() {
//        assertThrows(IllegalArgumentException.class, () ->
//                Spread.calculate(null, new BigDecimal("1"))
//        );
//    }
//}