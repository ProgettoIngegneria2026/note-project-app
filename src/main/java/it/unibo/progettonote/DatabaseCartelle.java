package it.unibo.progettonote;

import org.mapdb.*;
import java.util.concurrent.ConcurrentNavigableMap;

public class DatabaseCartelle {

    private static DB db;
    private static ConcurrentNavigableMap<String, Cartella> cartelleRepo;

    // se true: DB in memoria (test), se false: file DB (normale)
    private static boolean testMode = false;

    public static void enableTestMode() {
        testMode = true;
        close(); // resetta eventuale DB aperto
    }

    public static void disableTestMode() {
        testMode = false;
        close();
    }

    private static DB getDB() {
        if (db == null || db.isClosed()) {
            if (testMode) {
                db = DBMaker.memoryDB().transactionEnable().make();
            } else {
                db = DBMaker.fileDB("progetto_sweng.db").transactionEnable().make();
            }
        }
        return db;
    }

    public static ConcurrentNavigableMap<String, Cartella> getCartelleRepo() {
        if (cartelleRepo == null) {
            cartelleRepo = getDB()
                    .treeMap("cartelle", Serializer.STRING, Serializer.JAVA)
                    .createOrOpen();
        }
        return cartelleRepo;
    }

    public static void close() {
        if (db != null && !db.isClosed()) {
            db.close();
        }
        db = null;
        cartelleRepo = null;
    }
}