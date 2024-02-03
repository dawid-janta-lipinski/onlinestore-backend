package com.example.auth.model;

public enum Code {
    SUCCESS("SUCCESS"),
    PERMIT("PERMIT"),
    A1("This user doesn't exist or hasn't activated his account"),
    A2("The data provided is incorrect"),
    A3("User with this login already exists"),
    A4("User with this email already exists"),
    A5("Given token is empty or expired"),
    A6("The user you trying to activate doesn't exist in our database");
    public final String label;
    private Code(String label){
        this.label = label;
    }
}
