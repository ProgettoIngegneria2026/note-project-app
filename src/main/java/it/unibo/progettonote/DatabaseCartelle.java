package it.unibo.progettonote;

import org.mapdb.Serializer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentNavigableMap;

/**
 * Gestore del repository delle Cartelle.
 * Utilizza DatabaseCore per garantire l'accesso esclusivo al file .db
 */
public class DatabaseCartelle {

    private static ConcurrentNavigableMap<String, Cartella> cartelleRepo;

    /**
     * Restituisce la mappa delle cartelle salvate nel database.
     * Se la mappa non esiste, la crea o la apre utilizzando la connessione centralizzata.
     */
    public static ConcurrentNavigableMap<String, Cartella> getCartelleRepo() {
        if (cartelleRepo == null) {
            // USIAMO IL CORE: non aprire mai il file direttamente qui!
            cartelleRepo = DatabaseCore.getDB()
                    .treeMap("cartelle", Serializer.STRING, Serializer.JAVA)
                    .createOrOpen();
        }
        return cartelleRepo;
    }

    /**
     * Resetta il riferimento locale (utile durante i test o la chiusura dell'app).
     */
    public static void close() {
        cartelleRepo = null;
    }

    // Compatibilità con test/vecchio codice
    public static void enableTestMode() {
        DatabaseCore.enableTestMode();
        close(); // reset cache
    }

    public static void disableTestMode() {
        DatabaseCore.disableTestMode();
        close(); // reset cache
    }

    public static List<Cartella> findByOwner(String owner) {
        List<Cartella> res = new ArrayList<>();
        for (Cartella c : getCartelleRepo().values()) {
            if (owner.equals(c.getProprietario())) {
                res.add(c);
            }
        }
        return res;
    }

    public static Cartella findByIdAndOwner(String id, String owner) {
        Cartella c = getCartelleRepo().get(id);
        if (c == null) return null;
        return owner.equals(c.getProprietario()) ? c : null;
    }

    public static boolean existsByNameAndOwner(String nome, String owner) {
        for (Cartella c : getCartelleRepo().values()) {
            if (owner.equals(c.getProprietario()) && nome.equalsIgnoreCase(c.getNome())) {
                return true;
            }
        }
        return false;
    }
}