package model;

public interface Validatable {

    boolean validate();

    default String validationMessage() {
        return validate() ? "Valid" : "Invalid";
    }

    static boolean isValid(Validatable v) {
        return v != null && v.validate();
    }
}

