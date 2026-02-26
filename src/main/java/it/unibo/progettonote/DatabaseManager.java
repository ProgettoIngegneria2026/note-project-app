package it.unibo.progettonote;
import org.mapdb.*;
import java.io.File;

public class DatabaseManager {
    private static DB db;
    private static ConcurrentNavigableMap<String, Nota> noteRepo;

    public static DB getDB() {
        if (db == null || db.isClosed()) {
            // Crea o apre il file del database [cite: 36, 69]
            db = DBMaker.fileDB("progetto_sweng.db")
                       .transactionEnable() // Abilita transazioni per coerenza dati
                       .make();
        }
        return db;
    }

    public static ConcurrentNavigableMap<String, Nota> getNoteRepo() {
        if (noteRepo == null) {
            // Crea una mappa persistente per le note
            noteRepo = getDB().treeMap("note", Serializer.STRING, new NotaSerializer())
                             .createOrOpen();
        }
        return noteRepo;
    }
}