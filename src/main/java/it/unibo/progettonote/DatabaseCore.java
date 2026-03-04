package it.unibo.progettonote;

import org.mapdb.DB;
import org.mapdb.DBMaker;

public class DatabaseCore {

    private static DB db;
    private static boolean testMode = false;

    public static void enableTestMode() {
        testMode = true;
        if (db != null) {
            db.close();
            db = null;
        }
    }

    public static DB getDB() {
        if (db == null) {
            if (testMode) {
                db = DBMaker.memoryDB().transactionEnable().make();
            } else {
                db = DBMaker.fileDB("progetto_sweng.db").transactionEnable().make();
            }
        }
        return db;
    }

    public static void commit() {
        getDB().commit();
    }

    public static void close() {
        if (db != null) {
            db.close();
            db = null;
        }
    }
}