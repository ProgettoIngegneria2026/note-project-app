package it.unibo.progettonote;

import org.mapdb.*;
import java.util.concurrent.ConcurrentNavigableMap;

public class DatabaseUtenti {
    private static DB db;
    private static ConcurrentNavigableMap<String, Utente> utentiRepo;

    public static DB getDB() {
        if (db == null || db.isClosed()) {
            db = DBMaker.fileDB("progetto_sweng.db").transactionEnable().make();
        }
        return db;
    }

    public static ConcurrentNavigableMap<String, Utente> getUtentiRepo() {
        if (utentiRepo == null) {
            // Usiamo l'email come chiave per garantire l'unicità richiesta
            utentiRepo = getDB().treeMap("utenti", Serializer.STRING, Serializer.JAVA).createOrOpen();
        }
        return utentiRepo;
    }
}