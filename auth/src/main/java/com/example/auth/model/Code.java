package com.example.auth.model;

public enum Code {
    SUCCESS("SUCCESS"),
    PERMIT("PERMIT"),
    A1("This user doesn't exist or hasn't activated his account"),
    A2("The data provided is incorrect"),
    A3("Wskazany token jest pusty lub nie ważny"),
    A4("Użytkownik o podanej nazwie juz istnieje"),
    A5("Użytkownik o podanmym mailu juz istnieje"),
    A6("Użytkownik nie istnieje");

    public final String label;
    private Code(String label){
        this.label = label;
    }
}
