package com.example.auth.model;

public enum Code {
    SUCCESS("SUCCESS"),
    PERMIT("PERMIT"),
    A1("This user doesn't exist"),
    A2("The data provided is incorrect"),
    A3("User with this login already exists"),
    A4("User with this email already exists"),
    A5("Given token is empty or expired"),
    A6("Chuj wie, jakis inny błąd");

    public final String label;
    private Code(String label){
        this.label = label;
    }
}
