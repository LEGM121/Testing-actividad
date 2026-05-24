package com.unisabana.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

/**
 * RegistryTest: Suite de pruebas unitarias para la clase Registry.
 * Implementa TDD, AAA, clases de equivalencia y valores límite.
 * 
 * HISTORIA TDD:
 * ITERACIÓN 1 (RED): Test isAdult - persona >= 18 años
 * ITERACIÓN 2 (GREEN): Implementación mínima de isAdult
 * ITERACIÓN 3 (REFACTOR): Extracción de constante MIN_AGE
 */
@DisplayName("Registry - Test Suite")
class RegistryTest {

    private Registry registryAdult;
    private Registry registryMinor;
    private Registry registryRetiree;

    @BeforeEach
    void setUp() {
        // ARRANGE: Preparar objetos de prueba
        registryAdult = new Registry("1001", "Juan Pérez García", 35, "MARRIED");
        registryMinor = new Registry("1002", "María López Rodríguez", 16, "SINGLE");
        registryRetiree = new Registry("1003", "Carlos Sánchez López", 70, "MARRIED");
    }

    @Nested
    @DisplayName("Validación de Edad - Clases de Equivalencia")
    class AgeEquivalenceClassesTest {

        @Test
        @DisplayName("Should throw exception when age is negative")
        void shouldThrowExceptionWhenAgeIsNegative() {
            // ARRANGE
            // ACT & ASSERT
            assertThatThrownBy(() -> new Registry("1", "Test", -5, "SINGLE"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("no puede ser negativa");
        }

        @Test
        @DisplayName("Should throw exception when age exceeds maximum (120)")
        void shouldThrowExceptionWhenAgeExceedsMaximum() {
            // ARRANGE & ACT & ASSERT
            assertThatThrownBy(() -> new Registry("1", "Test", 121, "SINGLE"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("excede el máximo permitido");
        }

        @Test
        @DisplayName("Should create registry with valid age (0)")
        void shouldCreateRegistryWithValidAgeZero() {
            // ARRANGE & ACT
            Registry baby = new Registry("1", "Newborn", 0, "SINGLE");
            
            // ASSERT
            assertThat(baby.getAge()).isEqualTo(0);
            assertThat(baby.isMinor()).isTrue();
        }

        @Test
        @DisplayName("Should create registry with valid age (120)")
        void shouldCreateRegistryWithValidAgeMaximum() {
            // ARRANGE & ACT
            Registry elder = new Registry("1", "Elder", 120, "SINGLE");
            
            // ASSERT
            assertThat(elder.getAge()).isEqualTo(120);
            assertThat(elder.isRetired()).isTrue();
        }

        @ParameterizedTest
        @ValueSource(ints = {0, 5, 10, 12})
        @DisplayName("Should identify children correctly (0-12 years)")
        void shouldIdentifyChildrenCorrectly(int age) {
            // ARRANGE & ACT
            Registry child = new Registry("1", "Child", age, "SINGLE");
            
            // ASSERT
            assertThat(child.getAgeCategory()).isEqualTo("CHILD");
            assertThat(child.isMinor()).isTrue();
        }

        @ParameterizedTest
        @ValueSource(ints = {13, 15, 17})
        @DisplayName("Should identify teenagers correctly (13-17 years)")
        void shouldIdentifyTeenagersCorrectly(int age) {
            // ARRANGE & ACT
            Registry teenager = new Registry("1", "Teen", age, "SINGLE");
            
            // ASSERT
            assertThat(teenager.getAgeCategory()).isEqualTo("TEENAGER");
            assertThat(teenager.isMinor()).isTrue();
            assertThat(teenager.isAdult()).isFalse();
        }

        @ParameterizedTest
        @ValueSource(ints = {18, 35, 64})
        @DisplayName("Should identify adults correctly (18-64 years)")
        void shouldIdentifyAdultsCorrectly(int age) {
            // ARRANGE & ACT
            Registry adult = new Registry("1", "Adult", age, "SINGLE");
            
            // ASSERT
            assertThat(adult.getAgeCategory()).isEqualTo("ADULT");
            assertThat(adult.isAdult()).isTrue();
            assertThat(adult.isMinor()).isFalse();
            assertThat(adult.isRetired()).isFalse();
        }

        @ParameterizedTest
        @ValueSource(ints = {65, 75, 100, 120})
        @DisplayName("Should identify seniors correctly (65+ years)")
        void shouldIdentifySeniorsCorrectly(int age) {
            // ARRANGE & ACT
            Registry senior = new Registry("1", "Senior", age, "SINGLE");
            
            // ASSERT
            assertThat(senior.getAgeCategory()).isEqualTo("SENIOR");
            assertThat(senior.isRetired()).isTrue();
            assertThat(senior.isAdult()).isTrue();
        }
    }

    @Nested
    @DisplayName("Validación de Estado Civil - BDD")
    class MaritalStatusValidationTest {

        @Test
        @DisplayName("Given empty marital status When creating registry Then throws exception")
        void shouldThrowExceptionWhenMaritalStatusIsEmpty() {
            // ARRANGE & ACT & ASSERT
            assertThatThrownBy(() -> new Registry("1", "Test", 25, ""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("no puede estar vacío");
        }

        @Test
        @DisplayName("Given invalid marital status When creating registry Then throws exception")
        void shouldThrowExceptionWhenMaritalStatusIsInvalid() {
            // ARRANGE & ACT & ASSERT
            assertThatThrownBy(() -> new Registry("1", "Test", 25, "INVALID"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Estado civil inválido");
        }

        @ParameterizedTest
        @ValueSource(strings = {"SINGLE", "MARRIED", "DIVORCED", "WIDOWED"})
        @DisplayName("Given valid marital status When creating registry Then succeeds")
        void shouldAcceptValidMaritalStatuses(String status) {
            // ARRANGE & ACT
            Registry registry = new Registry("1", "Test", 25, status);
            
            // ASSERT
            assertThat(registry.getMaritalStatus()).isEqualTo(status);
        }

        @Test
        @DisplayName("Given single person When updating to married Then status updates")
        void shouldUpdateMaritalStatus() {
            // ARRANGE
            Registry person = new Registry("1", "Test", 25, "SINGLE");
            
            // ACT
            person.updateMaritalStatus("MARRIED");
            
            // ASSERT
            assertThat(person.getMaritalStatus()).isEqualTo("MARRIED");
        }
    }

    @Nested
    @DisplayName("Estado de Vida - Life Status")
    class LifeStatusTest {

        @Test
        @DisplayName("Should initialize person as alive")
        void shouldInitializePersonAsAlive() {
            // ARRANGE & ACT & ASSERT
            assertThat(registryAdult.isAlive()).isTrue();
        }

        @Test
        @DisplayName("Should mark person as dead")
        void shouldMarkPersonAsDead() {
            // ARRANGE
            assertThat(registryAdult.isAlive()).isTrue();
            
            // ACT
            registryAdult.markAsDead();
            
            // ASSERT
            assertThat(registryAdult.isAlive()).isFalse();
        }

        @Test
        @DisplayName("Should throw exception when marking deceased person as dead again")
        void shouldThrowExceptionWhenMarkingDeceasedTwice() {
            // ARRANGE
            registryAdult.markAsDead();
            assertThat(registryAdult.isAlive()).isFalse();
            
            // ACT & ASSERT
            assertThatThrownBy(() -> registryAdult.markAsDead())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("ya fue marcada como fallecida");
        }
    }

    @Nested
    @DisplayName("Cálculo de Jubilación - Retirement Calculation")
    class RetirementCalculationTest {

        @Test
        @DisplayName("Should return 0 years to retirement when already retired")
        void shouldReturnZeroWhenAlreadyRetired() {
            // ARRANGE & ACT
            int years = registryRetiree.yearsUntilRetirement();
            
            // ASSERT
            assertThat(years).isEqualTo(0);
        }

        @Test
        @DisplayName("Should calculate years until retirement correctly")
        void shouldCalculateYearsUntilRetirement() {
            // ARRANGE: Person is 35, retirement age is 65
            // ACT
            int years = registryAdult.yearsUntilRetirement();
            
            // ASSERT
            assertThat(years).isEqualTo(30);
        }

        @Test
        @DisplayName("Should return correct years for person close to retirement (64 years old)")
        void shouldReturnOneYearForPersonAtAgeNinetysixtyFour() {
            // ARRANGE
            Registry personAt64 = new Registry("1", "Test", 64, "SINGLE");
            
            // ACT
            int years = personAt64.yearsUntilRetirement();
            
            // ASSERT
            assertThat(years).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("Getters y Atributos - Attributes")
    class AttributesTest {

        @Test
        @DisplayName("Should retrieve all person attributes correctly")
        void shouldRetrieveAllAttributesCorrectly() {
            // ARRANGE & ACT & ASSERT
            assertThat(registryAdult.getId()).isEqualTo("1001");
            assertThat(registryAdult.getFullName()).isEqualTo("Juan Pérez García");
            assertThat(registryAdult.getAge()).isEqualTo(35);
            assertThat(registryAdult.getMaritalStatus()).isEqualTo("MARRIED");
        }
    }

    @Nested
    @DisplayName("Valores Límite - Boundary Values")
    class BoundaryValuesTest {

        @Test
        @DisplayName("BV: Age = 17 (just before adult)")
        void boundaryValue_AgeSeventeen() {
            // ARRANGE & ACT
            Registry person = new Registry("1", "Test", 17, "SINGLE");
            
            // ASSERT
            assertThat(person.isMinor()).isTrue();
            assertThat(person.isAdult()).isFalse();
            assertThat(person.getAgeCategory()).isEqualTo("TEENAGER");
        }

        @Test
        @DisplayName("BV: Age = 18 (minimum adult age)")
        void boundaryValue_AgeEighteen() {
            // ARRANGE & ACT
            Registry person = new Registry("1", "Test", 18, "SINGLE");
            
            // ASSERT
            assertThat(person.isMinor()).isFalse();
            assertThat(person.isAdult()).isTrue();
            assertThat(person.getAgeCategory()).isEqualTo("ADULT");
        }

        @Test
        @DisplayName("BV: Age = 64 (just before retirement)")
        void boundaryValue_AgeSixtyfour() {
            // ARRANGE & ACT
            Registry person = new Registry("1", "Test", 64, "SINGLE");
            
            // ASSERT
            assertThat(person.isRetired()).isFalse();
            assertThat(person.getAgeCategory()).isEqualTo("ADULT");
            assertThat(person.yearsUntilRetirement()).isEqualTo(1);
        }

        @Test
        @DisplayName("BV: Age = 65 (minimum retirement age)")
        void boundaryValue_AgeSixtyfive() {
            // ARRANGE & ACT
            Registry person = new Registry("1", "Test", 65, "SINGLE");
            
            // ASSERT
            assertThat(person.isRetired()).isTrue();
            assertThat(person.getAgeCategory()).isEqualTo("SENIOR");
            assertThat(person.yearsUntilRetirement()).isEqualTo(0);
        }

        @Test
        @DisplayName("BV: Age = 12 (last child year)")
        void boundaryValue_AgeEleven() {
            // ARRANGE & ACT
            Registry person = new Registry("1", "Test", 12, "SINGLE");
            
            // ASSERT
            assertThat(person.getAgeCategory()).isEqualTo("CHILD");
        }

        @Test
        @DisplayName("BV: Age = 13 (first teenager year)")
        void boundaryValue_AgeThirteen() {
            // ARRANGE & ACT
            Registry person = new Registry("1", "Test", 13, "SINGLE");
            
            // ASSERT
            assertThat(person.getAgeCategory()).isEqualTo("TEENAGER");
        }
    }
}
