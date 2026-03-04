package it.unibo.progettonote;

import org.mapdb.*;
import java.util.concurrent.ConcurrentNavigableMap;

public class DatabaseUtenti {

    private static ConcurrentNavigableMap<String, Utente> utentiRepo;

    public static DB getDB() {
        return DatabaseCore.getDB();
    }

    public static ConcurrentNavigableMap<String, Utente> getUtentiRepo() {
        if (utentiRepo == null) {
            utentiRepo = getDB()
                    .treeMap("utenti", Serializer.STRING, Serializer.JAVA)
                    .createOrOpen();
        }
        return utentiRepo;
    }

    public static void close() {
        utentiRepo = null;
    }
}