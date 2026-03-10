package it.unibo.progettonote;

import java.util.concurrent.ConcurrentNavigableMap;

public class DatabaseUtenti {

    private static ConcurrentNavigableMap<String, Utente> userRepo;

    public static ConcurrentNavigableMap<String, Utente> getUserRepo() {
        if (userRepo == null) {
            userRepo = DatabaseCore.getDB()
                .treeMap("users", org.mapdb.Serializer.STRING, org.mapdb.Serializer.JAVA)
                .createOrOpen();
        }
        return userRepo;
    }

    public static void close() {
        userRepo = null;
    }
}
