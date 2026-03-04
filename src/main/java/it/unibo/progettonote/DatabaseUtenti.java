package it.unibo.progettonote;

import org.mapdb.Serializer;

import java.util.concurrent.ConcurrentNavigableMap;

public class DatabaseUtenti {

    private static ConcurrentNavigableMap<String, Utente> utentiRepo;

    public static ConcurrentNavigableMap<String, Utente> getUtentiRepo() {
        if (utentiRepo == null) {
            // USIAMO IL CORE! Questo garantisce che il file sia lo stesso per tutti
            utentiRepo = DatabaseCore.getDB()
                    .treeMap("utenti", Serializer.STRING, Serializer.JAVA)
                    .createOrOpen();
        }
        return utentiRepo;
    }

    // Commit centralizzato (utile quando salvi/modifichi utenti)
    public static void commit() {
        DatabaseCore.commit();
    }

    // Reset cache locale (utile in test / chiusura app)
    public static void close() {
        utentiRepo = null;
    }
}