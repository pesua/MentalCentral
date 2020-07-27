package com.noosphere.mental_central.web.rest.errors;

public class PatientAlreadyExistsException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;


    public PatientAlreadyExistsException() {
        super(ErrorConstants.PATIENT_ALREADY_EXISTS_TYPE, "Patient with that name and birthday already exists!", "Patient", "patientexists");
    }
}
