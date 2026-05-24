# Defectos Encontrados y Análisis

## Defecto #1: Validación de Edad Negativa

**ID**: DEF-001  
**Estado**: CERRADO  
**Severidad**: ALTA  

### Descripción del Caso
Al crear un `Registry` con edad negativa (-5), el sistema no rechaza la operación.

### Resultado Esperado
`IllegalArgumentException` con mensaje: "La edad no puede ser negativa"

### Resultado Obtenido (ANTES)
Se creaba un `Registry` con edad negativa, permitiendo estados inconsistentes en el dominio.

### Causa Probable
Falta de validación en el constructor de `Registry` para verificar que la edad esté en rango válido.

### Solución Implementada
Se agregó validación en el constructor:
```java
private void validateAge(int age) {
    if (age < 0) {
        throw new IllegalArgumentException("La edad no puede ser negativa");
    }
}
```

### Test que lo detecta
`RegistryTest.shouldThrowExceptionWhenAgeIsNegative()`

---

## Defecto #2: Edad Máxima sin Límite

**ID**: DEF-002  
**Estado**: CERRADO  
**Severidad**: MEDIA  

### Descripción del Caso
Al crear un `Registry` con edad superior a 120 años (ej: 150), el sistema no rechaza.

### Resultado Esperado
`IllegalArgumentException` con mensaje: "La edad excede el máximo permitido: 120"

### Resultado Obtenido (ANTES)
Se permitían edades no realistas, comprometiendo la integridad del dominio.

### Causa Probable
Falta de límite máximo en la validación de edad.

### Solución Implementada
Se agregó validación de máximo en `validateAge()`:
```java
if (age > MAX_AGE) {
    throw new IllegalArgumentException("La edad excede el máximo permitido: " + MAX_AGE);
}
```

### Test que lo detecta
`RegistryTest.shouldThrowExceptionWhenAgeExceedsMaximum()`

---

## Defecto #3: Estado Civil Vacío sin Validación

**ID**: DEF-003  
**Estado**: CERRADO  
**Severidad**: MEDIA  

### Descripción del Caso
Al crear un `Registry` con estado civil vacío o null, no se valida.

### Resultado Esperado
`IllegalArgumentException` con mensaje: "El estado civil no puede estar vacío"

### Resultado Obtenido (ANTES)
Se permitía crear registros sin estado civil definido.

### Causa Probable
Falta de validación de string vacío/null en el constructor.

### Solución Implementada
Se agregó validación en `validateMaritalStatus()`:
```java
if (status == null || status.trim().isEmpty()) {
    throw new IllegalArgumentException("El estado civil no puede estar vacío");
}
```

### Test que lo detecta
`RegistryTest.shouldThrowExceptionWhenMaritalStatusIsEmpty()`

---

## Defecto #4: Estado Civil Inválido

**ID**: DEF-004  
**Estado**: CERRADO  
**Severidad**: MEDIA  

### Descripción del Caso
Se permite crear un `Registry` con estado civil no permitido (ej: "VIVO", "UNKNOWN").

### Resultado Esperado
`IllegalArgumentException` indicando estado civil inválido

### Resultado Obtenido (ANTES)
Se aceptaban valores arbitrarios de estado civil.

### Causa Probable
Falta de validación contra lista de estados permitidos.

### Solución Implementada
Se agregó validación con regex en `validateMaritalStatus()`:
```java
String validStatuses = "SINGLE|MARRIED|DIVORCED|WIDOWED";
if (!status.matches(validStatuses)) {
    throw new IllegalArgumentException("Estado civil inválido: " + status);
}
```

### Test que lo detecta
`RegistryTest.shouldThrowExceptionWhenMaritalStatusIsInvalid()`

---

## Defecto #5: Marcar Persona Fallecida Dos Veces

**ID**: DEF-005  
**Estado**: CERRADO  
**Severidad**: BAJA  

### Descripción del Caso
Se permite llamar a `markAsDead()` múltiples veces sobre la misma persona.

### Resultado Esperado
Una vez marcada como fallecida, un segundo `markAsDead()` debe lanzar `IllegalStateException`

### Resultado Obtenido (ANTES)
No había validación, permitiendo marcar múltiples veces.

### Causa Probable
Falta de control de estado en el método `markAsDead()`.

### Solución Implementada
Se agregó validación en `markAsDead()`:
```java
public void markAsDead() {
    if (!isAlive) {
        throw new IllegalStateException("La persona ya fue marcada como fallecida");
    }
    this.isAlive = false;
}
```

### Test que lo detecta
`RegistryTest.shouldThrowExceptionWhenMarkingDeceasedTwice()`

---

## Resumen de Defectos

| ID | Descripción | Estado | Severidad | Test |
|-----|-------------|--------|-----------|------|
| DEF-001 | Edad negativa | ✅ CERRADO | ALTA | shouldThrowExceptionWhenAgeIsNegative |
| DEF-002 | Edad > 120 | ✅ CERRADO | MEDIA | shouldThrowExceptionWhenAgeExceedsMaximum |
| DEF-003 | Estado civil vacío | ✅ CERRADO | MEDIA | shouldThrowExceptionWhenMaritalStatusIsEmpty |
| DEF-004 | Estado civil inválido | ✅ CERRADO | MEDIA | shouldThrowExceptionWhenMaritalStatusIsInvalid |
| DEF-005 | Marcar muerto 2x | ✅ CERRADO | BAJA | shouldThrowExceptionWhenMarkingDeceasedTwice |

---

**Lecciones Aprendidas**:
- Los tests ayudaron a identificar falta de validaciones en el dominio
- La validación en el constructor es crítica para mantener invariantes del dominio
- Los casos límite frecuentemente revelan defectos en la lógica
