package it.unibo.progettonote;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;
import java.util.concurrent.ConcurrentNavigableMap;

public class DatabaseManager {
    private static DB db;
    private static ConcurrentNavigableMap<String, Nota> noteRepo;

    public static DB getDB() {
        if (db == null || db.isClosed()) {
            // Crea o apre il file del database
            db = DBMaker.fileDB("progetto_sweng.db")
                        .transactionEnable() // Abilita transazioni per coerenza dati
                        .closeOnJvmShutdown() // Chiude il DB automaticamente allo spegnimento
                        .make();
        }
        return db;
    }

    public static ConcurrentNavigableMap<String, Nota> getNoteRepo() {
        // Ci assicuriamo che il DB sia aperto prima di accedere alla mappa
        if (db == null || db.isClosed()) {
            getDB();
        }
        
        if (noteRepo == null) {
            // Sostituiamo il vecchio NotaSerializer con Serializer.JAVA
            // Utilizziamo Serializer.STRING per la chiave (ID della nota)
            noteRepo = db.treeMap("notes", Serializer.STRING, Serializer.JAVA)
                         .createOrOpen();
        }
        return noteRepo;
    }

    // Metodo di utilità per chiudere il database manualmente se necessario
    public static void closeDB() {
        if (db != null && !db.isClosed()) {
            db.close();
        }
    }
}