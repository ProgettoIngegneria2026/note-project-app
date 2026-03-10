package it.unibo.progettonote;

import java.util.HashMap;
import java.util.Map;

public class DatabaseUtenti {

    private Map<String, String> utentiRepo = new HashMap<>();

    // Restituisce repository utenti
    public Map<String, String> getUtentiRepo() {
        return utentiRepo;
    }

    // Chiude database (placeholder)
    public void close() {
        utentiRepo.clear();
    }
}
