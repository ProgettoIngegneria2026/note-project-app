package it.unibo.progettonote;

public class ValidatoreNote {
    /**
     * Valida il contenuto di una nota.
     * @param contenuto Il testo da verificare.
     * @throws IllegalArgumentException se il testo supera i 280 caratteri.
     */
    public static void valida(String contenuto) {
        if (contenuto == null || contenuto.length() > 280) {
            throw new IllegalArgumentException("Il contenuto supera il limite di 280 caratteri.");
        }
    }
}