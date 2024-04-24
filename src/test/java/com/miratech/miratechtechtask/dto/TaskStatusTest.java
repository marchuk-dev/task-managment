package com.miratech.miratechtechtask.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TaskStatusTest {

    @DisplayName("Test 'fromStatus()' successful")
    @ParameterizedTest(name = "From {0} result => {1}")
    @MethodSource("getFromStatusSuccessfulArgs")
    void testFromStatusSuccessful(String value, TaskStatus result) {
        assertEquals(result, TaskStatus.fromStatus(value));
    }

    private static Stream<Arguments> getFromStatusSuccessfulArgs() {
        return Stream.of(
                Arguments.of("in progress", TaskStatus.IN_PROGRESS),
                Arguments.of("pending", TaskStatus.PENDING),
                Arguments.of("completed", TaskStatus.COMPLETED),
                Arguments.of("to do", TaskStatus.TO_DO),
                Arguments.of("in testing", TaskStatus.TESTING),

                Arguments.of("IN PROGRESS", TaskStatus.IN_PROGRESS),
                Arguments.of("PENDING", TaskStatus.PENDING),
                Arguments.of("COMPLETED", TaskStatus.COMPLETED),
                Arguments.of("TO DO", TaskStatus.TO_DO),
                Arguments.of("IN TESTING", TaskStatus.TESTING),

                Arguments.of("IN PRogrESS", TaskStatus.IN_PROGRESS),
                Arguments.of("PENdinG", TaskStatus.PENDING),
                Arguments.of("COMPLEteD", TaskStatus.COMPLETED),
                Arguments.of("to DO", TaskStatus.TO_DO),
                Arguments.of("IN testing", TaskStatus.TESTING)
        );
    }

    @DisplayName("Test 'fromStatus()' failed")
    @ParameterizedTest(name = "From ''{0}'' result => IllegalArgumentException")
    @NullSource
    @EmptySource
    @ValueSource(strings = {" ", "wrong"})
    void testFromStatus(String value) {
        assertThrows(IllegalArgumentException.class, () -> TaskStatus.fromStatus(value));
    }

}
