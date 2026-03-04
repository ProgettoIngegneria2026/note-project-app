package it.unibo.progettonote;

import org.mapdb.Serializer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentNavigableMap;

public class DatabaseCartelle {

    private static ConcurrentNavigableMap<String, Cartella> cartelleRepo;

    public static ConcurrentNavigableMap<String, Cartella> getCartelleRepo() {
        if (cartelleRepo == null) {
            cartelleRepo = DatabaseCore.getDB()
                    .treeMap("cartelle", Serializer.STRING, Serializer.JAVA)
                    .createOrOpen();
        }
        return cartelleRepo;
    }

    public static void close() {
        cartelleRepo = null;
    }

    // Compatibilità con test/vecchio codice
    public static void enableTestMode() {
        DatabaseCore.enableTestMode();
        close(); // reset cache
    }

    public static void disableTestMode() {
        DatabaseCore.disableTestMode();
        close(); // reset cache
    }

    public static List<Cartella> findByOwner(String owner) {
        List<Cartella> res = new ArrayList<>();
        for (Cartella c : getCartelleRepo().values()) {
            if (owner.equals(c.getProprietario())) {
                res.add(c);
            }
        }
        return res;
    }

    public static Cartella findByIdAndOwner(String id, String owner) {
        Cartella c = getCartelleRepo().get(id);
        if (c == null) return null;
        return owner.equals(c.getProprietario()) ? c : null;
    }

    public static boolean existsByNameAndOwner(String nome, String owner) {
        for (Cartella c : getCartelleRepo().values()) {
            if (owner.equals(c.getProprietario()) && nome.equalsIgnoreCase(c.getNome())) {
                return true;
            }
        }
        return false;
    }
}