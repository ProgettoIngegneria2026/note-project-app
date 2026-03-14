package it.unibo.progettonote.server;

import java.util.HashMap;
import java.util.Map;

import it.unibo.progettonote.client.Utente;

public class DatabaseUtenti {
    private static Map<String, Utente> utentiRepo = new HashMap<>();

    public static Map<String, Utente> getUtentiRepo() {
        return utentiRepo;
    }

    public static void close() {
        utentiRepo.clear();
    }
}