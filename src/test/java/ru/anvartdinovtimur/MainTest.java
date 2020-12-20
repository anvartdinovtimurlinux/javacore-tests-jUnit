package ru.anvartdinovtimur;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class MainTest {

    @Test
    void parseCSV_positiveTest() {
        // given:
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileCSVName = "src/test/resources/dataCorrect.csv";
        List<Employee> expected = new ArrayList<>();
        expected.add(new Employee(1, "John", "Smith", "USA", 25));
        expected.add(new Employee(2, "Inav", "Petrov", "RU", 23));

        // when:
        List<Employee> results = Main.parseCSV(columnMapping, fileCSVName);

        // then:
        assertEquals(expected, results);
    }

    @Test
    void parseCSV_negativeTest() {
        // given:
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileCSVName = "src/test/resources/dataWithOtherEmployees.csv";
        List<Employee> expected = new ArrayList<>();
        expected.add(new Employee(1, "John", "Smith", "USA", 25));
        expected.add(new Employee(2, "Inav", "Petrov", "RU", 23));

        // when:
        List<Employee> results = Main.parseCSV(columnMapping, fileCSVName);

        // then:
        assertNotEquals(expected, results);
    }

    @Test
    void parseCSV_throwCsvDataTypeMismatchException() {
        // given:
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileCSVName = "src/test/resources/dataUncorrect.csv";

        // expected:
        assertThrows(RuntimeException.class, () -> {
            Main.parseCSV(columnMapping, fileCSVName);
        });
    }

}
