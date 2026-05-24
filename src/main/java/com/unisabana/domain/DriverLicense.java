package com.unisabana.domain;

/**
 * DriverLicense: Clase de dominio para validar elegibilidad de licencia de conducción en Colombia.
 * Implementa reglas de negocio colombianas para obtener licencia de conducir CARROS.
 * 
 * REGLAS COLOMBIA PARA CARROS:
 * - Edad mínima conducción regular: 16 años
 * - Edad mínima servicio público (taxi, uber): 23 años
 * - Edad máxima: 80 años (renovación requerida después)
 * - Discapacidad visual severa: NO puede conducir
 * - Antecedentes penales graves: NO puede obtener licencia
 */
public class DriverLicense {
    
    // Constantes - Edades según normativa colombiana
    private static final int MIN_AGE_REGULAR = 16;           // Conducción normal de carros
    private static final int MIN_AGE_PUBLIC_SERVICE = 23;    // Servicio público (taxi, uber)
    private static final int MAX_AGE = 80;                   // Edad máxima legal
    
    // Identificación
    private String documentId;
    private String fullName;
    private int age;
    
    // Condiciones médicas
    private boolean hasSevereEyeDisability;  // Discapacidad visual severa (impide conducir)
    private boolean hasHearingDisability;    // Discapacidad auditiva (permite con adaptaciones)
    
    // Antecedentes
    private int seriousCriminalRecords;  // Número de antecedentes penales graves
    
    // Estado de la licencia
    private String licenseType;  // REGULAR, PUBLIC_SERVICE
    private String status;       // PENDING, APPROVED, REJECTED, SUSPENDED, EXPIRED
    
    /**
     * Constructor para solicitud de licencia de conducción de carros.
     * 
     * @param documentId Número de cédula
     * @param fullName Nombre completo
     * @param age Edad en años
     * @param hasSevereEyeDisability ¿Tiene discapacidad visual severa?
     * @param hasHearingDisability ¿Tiene discapacidad auditiva?
     * @param seriousCriminalRecords Número de antecedentes penales graves
     * @param licenseType REGULAR o PUBLIC_SERVICE
     */
    public DriverLicense(String documentId, String fullName, int age,
                        boolean hasSevereEyeDisability, boolean hasHearingDisability,
                        int seriousCriminalRecords, String licenseType) {
        
        validateAge(age);
        validateDocument(documentId);
        validateCriminalRecords(seriousCriminalRecords);
        validateLicenseType(licenseType);
        
        this.documentId = documentId;
        this.fullName = fullName;
        this.age = age;
        this.hasSevereEyeDisability = hasSevereEyeDisability;
        this.hasHearingDisability = hasHearingDisability;
        this.seriousCriminalRecords = seriousCriminalRecords;
        this.licenseType = licenseType;
        this.status = "PENDING";
    }
    
    /**
     * REGLA PRINCIPAL: ¿Es elegible para obtener licencia de conducción de carros?
     * 
     * Requisitos que DEBE cumplir:
     * 1. Edad mínima: 16 años (regular) o 23 años (servicio público)
     * 2. Edad máxima: 80 años
     * 3. NO tener discapacidad visual severa
     * 4. NO tener antecedentes penales graves
     */
    public boolean isEligibleForLicense() {
        // Validar edad según tipo de licencia
        if (!isAgeValidForLicenseType()) {
            return false;
        }
        
        // NO puede tener discapacidad ocular severa (RIESGO para conducir carros)
        if (hasSevereEyeDisability) {
            return false;
        }
        
        // NO puede tener antecedentes penales graves
        if (seriousCriminalRecords > 0) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Verifica si la edad es válida para el tipo de licencia.
     * 
     * REGULAR: 16-80 años
     * PUBLIC_SERVICE: 23-80 años
     */
    private boolean isAgeValidForLicenseType() {
        if ("REGULAR".equals(licenseType)) {
            return age >= MIN_AGE_REGULAR && age <= MAX_AGE;
        } else if ("PUBLIC_SERVICE".equals(licenseType)) {
            return age >= MIN_AGE_PUBLIC_SERVICE && age <= MAX_AGE;
        }
        return false;
    }
    
    /**
     * ¿Es mayor de edad para conducción regular (16 años)?
     */
    public boolean isAdultForRegularLicense() {
        return age >= MIN_AGE_REGULAR;
    }
    
    /**
     * ¿Es mayor de edad para servicio público de carros (23 años)?
     */
    public boolean isAdultForPublicService() {
        return age >= MIN_AGE_PUBLIC_SERVICE;
    }
    
    /**
     * ¿Está dentro del rango de edad máxima (80 años)?
     */
    public boolean isWithinMaximumAge() {
        return age <= MAX_AGE;
    }
    
    /**
     * Obtiene la categoría de edad para conducción de carros.
     */
    public String getAgeCategory() {
        if (age < MIN_AGE_REGULAR) {
            return "TOO_YOUNG";      // < 16 años: NO PUEDE CONDUCIR
        } else if (age < 18) {
            return "ADOLESCENT";     // 16-17 años: LICENCIA RESTRINGIDA
        } else if (age < MIN_AGE_PUBLIC_SERVICE) {
            return "YOUNG_ADULT";    // 18-22 años: CONDUCCIÓN REGULAR PERMITIDA
        } else if (age < 65) {
            return "ADULT";          // 23-64 años: SIN RESTRICCIONES
        } else if (age <= MAX_AGE) {
            return "SENIOR";         // 65-80 años: RENOVACIÓN ANUAL REQUERIDA
        } else {
            return "TOO_OLD";        // > 80 años: REQUIERE EVALUACIÓN MÉDICA ESPECIAL
        }
    }
    
    /**
     * Calcula cuántos años faltan para poder conducir en servicio público.
     */
    public int yearsUntilPublicService() {
        if (age >= MIN_AGE_PUBLIC_SERVICE) {
            return 0;
        }
        return MIN_AGE_PUBLIC_SERVICE - age;
    }
    
    /**
     * ¿Puede conducir con discapacidad visual?
     * En Colombia: Discapacidad visual SEVERA impide conducir.
     */
    public boolean canDriveWithEyeDisability() {
        return !hasSevereEyeDisability;
    }
    
    /**
     * ¿Puede conducir con discapacidad auditiva?
     * En Colombia: Discapacidad auditiva PERMITE conducción (con adaptaciones).
     */
    public boolean canDriveWithHearingDisability() {
        return true; // La discapacidad auditiva no impide conducción
    }
    
    /**
     * ¿Tiene antecedentes limpios?
     */
    public boolean hasCleanCriminalRecord() {
        return seriousCriminalRecords == 0;
    }
    
    /**
     * Obtiene el número de antecedentes penales graves.
     */
    public int getCriminalRecordCount() {
        return seriousCriminalRecords;
    }
    
    /**
     * APRUEBA la licencia si cumple TODOS los requisitos.
     * @throws IllegalStateException si NO cumple requisitos
     */
    public void approveLicense() {
        if (!isEligibleForLicense()) {
            throw new IllegalStateException(
                "Solicitante no cumple los requisitos para obtener licencia: " + 
                getRejectionReason()
            );
        }
        this.status = "APPROVED";
    }
    
    /**
     * RECHAZA la licencia.
     */
    public void rejectLicense() {
        this.status = "REJECTED";
    }
    
    /**
     * SUSPENDE la licencia (temporal - ej: multa grave).
     */
    public void suspendLicense() {
        if ("APPROVED".equals(status)) {
            this.status = "SUSPENDED";
        }
    }
    
    /**
     * REACTIVA una licencia suspendida.
     */
    public void reactivateLicense() {
        if ("SUSPENDED".equals(status)) {
            this.status = "APPROVED";
        }
    }
    
    /**
     * Obtiene el motivo específico de rechazo.
     */
    public String getRejectionReason() {
        if (!isWithinMaximumAge()) {
            return "Excede la edad máxima permitida (80 años)";
        }
        if ("REGULAR".equals(licenseType) && !isAdultForRegularLicense()) {
            return "No cumple edad mínima para licencia regular (16 años)";
        }
        if ("PUBLIC_SERVICE".equals(licenseType) && !isAdultForPublicService()) {
            return "No cumple edad mínima para servicio público (23 años)";
        }
        if (hasSevereEyeDisability) {
            return "Tiene discapacidad visual severa - Riesgo para conducir";
        }
        if (seriousCriminalRecords > 0) {
            return "Tiene " + seriousCriminalRecords + " antecedente(s) penal(es) grave(s)";
        }
        return "Cumple todos los requisitos";
    }
    
    // ============ VALIDACIONES PRIVADAS ============
    
    private void validateAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("La edad no puede ser negativa");
        }
        if (age > 150) {
            throw new IllegalArgumentException("La edad debe ser realista");
        }
    }
    
    private void validateDocument(String documentId) {
        if (documentId == null || documentId.trim().isEmpty()) {
            throw new IllegalArgumentException("El número de cédula no puede estar vacío");
        }
    }
    
    private void validateCriminalRecords(int records) {
        if (records < 0) {
            throw new IllegalArgumentException("El número de antecedentes no puede ser negativo");
        }
    }
    
    private void validateLicenseType(String type) {
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de licencia no puede estar vacío");
        }
        if (!type.matches("REGULAR|PUBLIC_SERVICE")) {
            throw new IllegalArgumentException("Tipo de licencia inválido: " + type + 
                                             ". Debe ser REGULAR o PUBLIC_SERVICE");
        }
    }
    
    // ============ GETTERS ============
    
    public String getDocumentId() {
        return documentId;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public int getAge() {
        return age;
    }
    
    public String getLicenseType() {
        return licenseType;
    }
    
    public String getStatus() {
        return status;
    }
    
    public boolean hasSevereEyeDisability() {
        return hasSevereEyeDisability;
    }
    
    public boolean hasHearingDisability() {
        return hasHearingDisability;
    }
}
