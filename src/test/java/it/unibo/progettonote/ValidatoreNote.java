package it.unibo.progettonote;

public class ValidatoreNote {
    public static void valida(String contenuto) {
        if (contenuto.length() > 280) throw new IllegalArgumentException("Contenuto troppo lungo");
    }
}
