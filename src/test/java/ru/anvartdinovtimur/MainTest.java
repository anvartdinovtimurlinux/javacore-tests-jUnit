package ru.anvartdinovtimur;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MainTest {

    @Test
    public void positiveTest()
    {
        // given:
        int a = 4;

        // when:
        double result = Math.pow(a, 2);

        // then:

        Assertions.assertEquals(result, 15);
    }

}
