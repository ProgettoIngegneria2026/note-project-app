package it.unibo.progettonote;

import org.mapdb.DB;
import org.mapdb.DBMaker;

/**
 * Gestore centralizzato del database MapDB.
 * Implementa il pattern Singleton per garantire che un solo processo
 * alla volta acceda al file progetto_sweng.db.
 */
public class DatabaseCore {

    private static DB db;
    private static boolean testMode = false;

    /**
     * Attiva la modalità test (Database in memoria RAM).
     * Utile per gli Unit Test per non sporcare il file fisico.
     */
    public static void enableTestMode() {
        testMode = true;
        close(); // forza riapertura in RAM
    }

    /**
     * Disattiva la modalità test e torna al database su file.
     */
    public static void disableTestMode() {
        testMode = false;
        close(); // torna su file
    }

    /**
     * Restituisce l'istanza attiva del database.
     * synchronized per prevenire accessi contemporanei da thread diversi.
     */
    public static synchronized DB getDB() {
        if (db == null || db.isClosed()) {
            if (testMode) {
                // Database temporaneo in RAM
                db = DBMaker.memoryDB()
                        .transactionEnable()
                        .make();
            } else {
                // Database persistente su FILE
                db = DBMaker.fileDB("progetto_sweng.db")
                        .transactionEnable()
                        .closeOnJvmShutdown() // rilascia il lock del file alla chiusura dell'app
                        .make();
            }
        }
        return db;
    }

    /**
     * Salva permanentemente le modifiche su disco.
     */
    public static void commit() {
        if (db != null && !db.isClosed()) {
            db.commit();
        }
    }

    /**
     * Chiude la connessione al database e libera il file.
     */
    public static void close() {
        if (db != null && !db.isClosed()) {
            db.close();
        }
        db = null;
    }
}