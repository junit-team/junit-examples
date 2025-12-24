/*
 * Copyright 2015-2025 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package com.example.project;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class CalculatorTest {

    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    // =====================
    // Tests pour l'addition
    // =====================

    @Nested
    @DisplayName("Tests pour l'addition")
    class AdditionTests {

        @Test
        @DisplayName("Cas normal: addition de deux nombres positifs")
        void addTwoPositiveNumbers() {
            assertEquals(5, calculator.add(2, 3));
        }

        @Test
        @DisplayName("Cas normal: addition de deux nombres négatifs")
        void addTwoNegativeNumbers() {
            assertEquals(-5, calculator.add(-2, -3));
        }

        @Test
        @DisplayName("Cas normal: addition d'un positif et d'un négatif")
        void addPositiveAndNegativeNumber() {
            assertEquals(1, calculator.add(3, -2));
        }

        @Test
        @DisplayName("Cas limite: addition avec zéro")
        void addWithZero() {
            assertEquals(5, calculator.add(5, 0));
            assertEquals(5, calculator.add(0, 5));
        }

        @Test
        @DisplayName("Cas limite: addition de deux zéros")
        void addTwoZeros() {
            assertEquals(0, calculator.add(0, 0));
        }

        @Test
        @DisplayName("Cas limite: valeurs maximales d'entiers")
        void addMaxIntegerValues() {
            assertEquals(Integer.MAX_VALUE, calculator.add(Integer.MAX_VALUE, 0));
        }

        @Test
        @DisplayName("Cas limite: valeurs minimales d'entiers")
        void addMinIntegerValues() {
            assertEquals(Integer.MIN_VALUE, calculator.add(Integer.MIN_VALUE, 0));
        }

        @ParameterizedTest(name = "{0} + {1} = {2}")
        @CsvSource({
            "0, 0, 0",
            "1, 1, 2",
            "-1, 1, 0",
            "100, -50, 50",
            "-100, -100, -200"
        })
        void addParameterized(int a, int b, int expected) {
            assertEquals(expected, calculator.add(a, b));
        }
    }

    // ==========================
    // Tests pour la soustraction
    // ==========================

    @Nested
    @DisplayName("Tests pour la soustraction")
    class SubtractionTests {

        @Test
        @DisplayName("Cas normal: soustraction de deux nombres positifs")
        void subtractTwoPositiveNumbers() {
            assertEquals(2, calculator.subtract(5, 3));
        }

        @Test
        @DisplayName("Cas normal: soustraction résultant en nombre négatif")
        void subtractResultingInNegative() {
            assertEquals(-2, calculator.subtract(3, 5));
        }

        @Test
        @DisplayName("Cas limite: soustraction avec zéro")
        void subtractWithZero() {
            assertEquals(5, calculator.subtract(5, 0));
            assertEquals(-5, calculator.subtract(0, 5));
        }

        @Test
        @DisplayName("Cas limite: soustraction de nombres égaux")
        void subtractEqualNumbers() {
            assertEquals(0, calculator.subtract(10, 10));
        }

        @ParameterizedTest(name = "{0} - {1} = {2}")
        @CsvSource({
            "10, 5, 5",
            "5, 10, -5",
            "0, 0, 0",
            "-5, -3, -2",
            "-5, 3, -8"
        })
        void subtractParameterized(int a, int b, int expected) {
            assertEquals(expected, calculator.subtract(a, b));
        }
    }

    // ============================
    // Tests pour la multiplication
    // ============================

    @Nested
    @DisplayName("Tests pour la multiplication")
    class MultiplicationTests {

        @Test
        @DisplayName("Cas normal: multiplication de deux nombres positifs")
        void multiplyTwoPositiveNumbers() {
            assertEquals(15, calculator.multiply(3, 5));
        }

        @Test
        @DisplayName("Cas normal: multiplication de nombres de signes différents")
        void multiplyDifferentSigns() {
            assertEquals(-15, calculator.multiply(3, -5));
            assertEquals(-15, calculator.multiply(-3, 5));
        }

        @Test
        @DisplayName("Cas normal: multiplication de deux nombres négatifs")
        void multiplyTwoNegativeNumbers() {
            assertEquals(15, calculator.multiply(-3, -5));
        }

        @Test
        @DisplayName("Cas limite: multiplication par zéro")
        void multiplyByZero() {
            assertEquals(0, calculator.multiply(5, 0));
            assertEquals(0, calculator.multiply(0, 5));
            assertEquals(0, calculator.multiply(0, 0));
        }

        @Test
        @DisplayName("Cas limite: multiplication par un")
        void multiplyByOne() {
            assertEquals(5, calculator.multiply(5, 1));
            assertEquals(5, calculator.multiply(1, 5));
        }

        @ParameterizedTest(name = "{0} * {1} = {2}")
        @CsvSource({
            "2, 3, 6",
            "0, 100, 0",
            "-2, 3, -6",
            "-2, -3, 6",
            "1, 1, 1"
        })
        void multiplyParameterized(int a, int b, int expected) {
            assertEquals(expected, calculator.multiply(a, b));
        }
    }

    // ======================
    // Tests pour la division
    // ======================

    @Nested
    @DisplayName("Tests pour la division")
    class DivisionTests {

        @Test
        @DisplayName("Cas normal: division de deux nombres positifs")
        void divideTwoPositiveNumbers() {
            assertEquals(3, calculator.divide(15, 5));
        }

        @Test
        @DisplayName("Cas normal: division entière (avec reste)")
        void divideWithRemainder() {
            assertEquals(3, calculator.divide(10, 3));
        }

        @Test
        @DisplayName("Cas normal: division de nombres de signes différents")
        void divideDifferentSigns() {
            assertEquals(-3, calculator.divide(15, -5));
            assertEquals(-3, calculator.divide(-15, 5));
        }

        @Test
        @DisplayName("Cas normal: division de deux nombres négatifs")
        void divideTwoNegativeNumbers() {
            assertEquals(3, calculator.divide(-15, -5));
        }

        @Test
        @DisplayName("Cas limite: division par un")
        void divideByOne() {
            assertEquals(5, calculator.divide(5, 1));
        }

        @Test
        @DisplayName("Cas limite: zéro divisé par un nombre")
        void divideZeroByNumber() {
            assertEquals(0, calculator.divide(0, 5));
        }

        @Test
        @DisplayName("Exception: division par zéro")
        void divideByZeroThrowsException() {
            ArithmeticException exception = assertThrows(
                ArithmeticException.class,
                () -> calculator.divide(10, 0)
            );
            assertEquals("Division par zéro impossible", exception.getMessage());
        }

        @Test
        @DisplayName("Exception: zéro divisé par zéro")
        void divideZeroByZeroThrowsException() {
            assertThrows(
                ArithmeticException.class,
                () -> calculator.divide(0, 0)
            );
        }

        @ParameterizedTest(name = "{0} / {1} = {2}")
        @CsvSource({
            "10, 2, 5",
            "15, 3, 5",
            "-10, 2, -5",
            "10, -2, -5",
            "-10, -2, 5"
        })
        void divideParameterized(int a, int b, int expected) {
            assertEquals(expected, calculator.divide(a, b));
        }
    }
}

