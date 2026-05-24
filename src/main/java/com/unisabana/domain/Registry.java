package com.unisabana.domain;

/**
 * Registry: Clase de dominio para gestionar el registro de personas.
 * Implementa reglas de negocio para validación de edad y estado civil.
 */
public class Registry {
    private static final int MIN_AGE = 18;
    private static final int MAX_AGE = 120;
    private static final int RETIREMENT_AGE = 65;

    private String id;
    private String fullName;
    private int age;
    private String maritalStatus;
    private boolean isAlive;

    public Registry(String id, String fullName, int age, String maritalStatus) {
        validateAge(age);
        validateMaritalStatus(maritalStatus);
        
        this.id = id;
        this.fullName = fullName;
        this.age = age;
        this.maritalStatus = maritalStatus;
        this.isAlive = true;
    }

    /**
     * Verifica si la persona es mayor de edad.
     */
    public boolean isAdult() {
        return age >= MIN_AGE;
    }

    /**
     * Verifica si la persona está en edad de jubilación.
     */
    public boolean isRetired() {
        return age >= RETIREMENT_AGE;
    }

    /**
     * Verifica si la persona es menor de edad.
     */
    public boolean isMinor() {
        return age < MIN_AGE;
    }

    /**
     * Obtiene la categoría de edad.
     */
    public String getAgeCategory() {
        if (age < 13) {
            return "CHILD";
        } else if (age < 18) {
            return "TEENAGER";
        } else if (age < 65) {
            return "ADULT";
        } else {
            return "SENIOR";
        }
    }

    /**
     * Registra el fallecimiento de la persona.
     */
    public void markAsDead() {
        if (!isAlive) {
            throw new IllegalStateException("La persona ya fue marcada como fallecida");
        }
        this.isAlive = false;
    }

    /**
     * Verifica si la persona está viva.
     */
    public boolean isAlive() {
        return isAlive;
    }

    /**
     * Actualiza el estado civil.
     */
    public void updateMaritalStatus(String newStatus) {
        validateMaritalStatus(newStatus);
        this.maritalStatus = newStatus;
    }

    /**
     * Calcula los años hasta la jubilación.
     */
    public int yearsUntilRetirement() {
        if (isRetired()) {
            return 0;
        }
        return RETIREMENT_AGE - age;
    }

    // Validaciones privadas

    private void validateAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("La edad no puede ser negativa");
        }
        if (age > MAX_AGE) {
            throw new IllegalArgumentException("La edad excede el máximo permitido: " + MAX_AGE);
        }
    }

    private void validateMaritalStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("El estado civil no puede estar vacío");
        }
        String validStatuses = "SINGLE|MARRIED|DIVORCED|WIDOWED";
        if (!status.matches(validStatuses)) {
            throw new IllegalArgumentException("Estado civil inválido: " + status);
        }
    }

    // Getters

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public int getAge() {
        return age;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }
}
