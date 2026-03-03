package it.unibo.progettonote;

import org.mapdb.Serializer;

import java.util.concurrent.ConcurrentNavigableMap;

public class DatabaseCartelle {

    private static ConcurrentNavigableMap<String, Cartella> cartelleRepo;

    public static void enableTestMode() {
        DatabaseCore.enableTestMode();
        cartelleRepo = null;
    }

    public static void disableTestMode() {
        DatabaseCore.disableTestMode();
        cartelleRepo = null;
    }

    public static ConcurrentNavigableMap<String, Cartella> getCartelleRepo() {
        if (cartelleRepo == null) {
            cartelleRepo = DatabaseCore.getDB()
                    .treeMap("cartelle", Serializer.STRING, Serializer.JAVA)
                    .createOrOpen();
        }
        return cartelleRepo;
    }

    public static void close() {
        cartelleRepo = null; // NON chiudere DatabaseCore qui
    }
}