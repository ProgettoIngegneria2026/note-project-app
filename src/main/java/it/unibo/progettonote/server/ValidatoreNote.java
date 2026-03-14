package it.unibo.progettonote.server;

public class ValidatoreNote {
    /**
     * Valida il contenuto di una nota.
     * @param contenuto Il testo da verificare.
     * @throws IllegalArgumentException se il testo supera i 280 caratteri o è null.
     */
    public static void valida(String contenuto) {
        if (contenuto == null || contenuto.length() > 280) {
            throw new IllegalArgumentException("Il contenuto non può essere nullo o superare i 280 caratteri.");
        }
    }
}