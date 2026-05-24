# Testing Workshop - Universidad de Sabana

## Descripción del Proyecto

**Dominio**: Gestión de Registro de Personas  
**Objetivo**: Aplicar TDD, BDD, AAA, clases de equivalencia y cobertura de código

## Integrantes

- LEGM121 (Estudiante)

## Contenido del Wiki

Para la documentación completa del taller, consulte el **[Wiki del Repositorio](https://github.com/LEGM121/testing-unisabana/wiki)**.

### Secciones del Wiki:

1. **[Inicio](https://github.com/LEGM121/testing-unisabana/wiki)** - Dominio, alcance y equipo
2. **[TDD: Red-Green-Refactor](https://github.com/LEGM121/testing-unisabana/wiki/TDD-History)** - 3+ iteraciones
3. **[Patrón AAA](https://github.com/LEGM121/testing-unisabana/wiki/AAA-Pattern)** - Arrange-Act-Assert
4. **[Clases de Equivalencia](https://github.com/LEGM121/testing-unisabana/wiki/Equivalence-Classes)** - Tabla y justificación
5. **[BDD: Given-When-Then](https://github.com/LEGM121/testing-unisabana/wiki/BDD-Scenarios)** - Escenarios
6. **[Resultados](https://github.com/LEGM121/testing-unisabana/wiki/Results)** - JaCoCo y conclusiones
7. **[Defectos](https://github.com/LEGM121/testing-unisabana/wiki/Defects)** - Análisis de defectos

## Cómo Ejecutar

### Compilar y ejecutar pruebas:
```bash
mvn clean test
```

### Generar reporte de cobertura:
```bash
mvn clean test jacoco:report
```

El reporte se generará en `target/site/jacoco/index.html`

### Verificar cobertura mínima:
```bash
mvn verify
```

## Estructura del Proyecto

```
testing-unisabana/
├── src/
│   ├── main/java/
│   │   └── com/unisabana/domain/
│   │       └── Registry.java          # Clase de dominio
│   └── test/java/
│       └── com/unisabana/domain/
│           └── RegistryTest.java      # Suite de pruebas
├── pom.xml                            # Configuración Maven + JaCoCo
├── .gitignore                         # Exclusiones Git
├── integrantes.txt                    # Información del equipo
└── README.md                          # Este archivo
```

## Clases de Equivalencia Cubiertas

| Clase | Rango | Tests |
|-------|-------|-------|
| Edad Negativa | < 0 | `shouldThrowExceptionWhenAgeIsNegative` |
| Edad Válida Mínima | 0 | `shouldCreateRegistryWithValidAgeZero` |
| Niños | 0-12 | `shouldIdentifyChildrenCorrectly` |
| Adolescentes | 13-17 | `shouldIdentifyTeenagersCorrectly` |
| Adultos | 18-64 | `shouldIdentifyAdultsCorrectly` |
| Jubilados | 65-120 | `shouldIdentifySeniorsCorrectly` |
| Edad Máxima Válida | 120 | `shouldCreateRegistryWithValidAgeMaximum` |
| Edad Excesiva | > 120 | `shouldThrowExceptionWhenAgeExceedsMaximum` |

## Valores Límite Identificados

| Límite | Valor | Test | Justificación |
|--------|-------|------|---------------|
| Mayoría de edad | 18 | `boundaryValue_AgeEighteen` | Transición minor→adult |
| Justo antes mayoría | 17 | `boundaryValue_AgeSeventeen` | Último día menor |
| Jubilación | 65 | `boundaryValue_AgeSixtyfive` | Edad legal jubilación |
| Justo antes jubilación | 64 | `boundaryValue_AgeSixtyfour` | Último año activo |
| Cambio niño→adolescente | 13 | `boundaryValue_AgeThirteen` | Inicio adolescencia |
| Último año infantil | 12 | `boundaryValue_AgeEleven` | Fin infancia |

## Patrón AAA (Arrange-Act-Assert)

Todos los tests siguen la estructura:

```java
@Test
@DisplayName("descripción clara en inglés")
void shouldDescribeBehavior() {
    // ARRANGE: Preparar datos de prueba
    Registry person = new Registry("1", "Test", 35, "MARRIED");
    
    // ACT: Ejecutar la acción a probar
    boolean isAdult = person.isAdult();
    
    // ASSERT: Verificar el resultado esperado
    assertThat(isAdult).isTrue();
}
```

## BDD: Escenarios Given-When-Then

Ejemplo de test con BDD:

```java
@Test
@DisplayName("Given empty marital status When creating registry Then throws exception")
void shouldThrowExceptionWhenMaritalStatusIsEmpty() {
    // Given: Estado civil vacío
    // When: Se intenta crear un registro
    // Then: Se lanza IllegalArgumentException
    assertThatThrownBy(() -> new Registry("1", "Test", 25, ""))
        .isInstanceOf(IllegalArgumentException.class);
}
```

## Requisitos

- Java 11+
- Maven 3.6+
- JUnit 5
- AssertJ
- JaCoCo

## Notas

- El proyecto es totalmente compilable: `mvn clean test` sin pasos adicionales
- Cobertura objetivo: ≥ 80%
- Todos los tests siguen nomenclatura: `should<Expected>When<Condition>()`
- El Wiki contiene documentación oficial (no PDF)

---

**Última actualización**: Mayo 2026  
**Estado**: En desarrollo
