package com.example.auth.model;

public enum Code {
    SUCCESS("SUCCESS"),
    PERMIT("PERMIT"),
    A1("This user doesn't exist"),
    A2("The data provided is incorrect"),
    A3("User with this login already exists"),
    A4("User with this email already exists"),
    A5("Wskazany token jest pusty lub nie ważny"),
    A6("Użytkownik nie istnieje");

    public final String label;
    private Code(String label){
        this.label = label;
    }
}
