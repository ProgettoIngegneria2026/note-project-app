package it.unibo.progettonote;

import org.mapdb.Serializer;
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
}